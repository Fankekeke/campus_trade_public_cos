package cc.mrbird.febs.stock.service.impl;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.cos.dao.UserInfoMapper;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.stock.dao.PharmacyInfoMapper;
import cc.mrbird.febs.stock.entity.*;
import cc.mrbird.febs.stock.dao.GoodsOrderInfoMapper;
import cc.mrbird.febs.stock.entity.vo.GoodsOrderInfoVo;
import cc.mrbird.febs.stock.entity.vo.OrderDetailVo;
import cc.mrbird.febs.stock.entity.vo.OrderSubVo;
import cc.mrbird.febs.stock.service.*;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsOrderInfoServiceImpl extends ServiceImpl<GoodsOrderInfoMapper, GoodsOrderInfo> implements IGoodsOrderInfoService {

    private final IOrderDetailService orderDetailService;

    private final IPharmacyInventoryService pharmacyInventoryService;

    private final IInventoryStatisticsService inventoryStatisticsService;

    private final PharmacyInfoMapper pharmacyInfoMapper;

    private final UserInfoMapper userInfoMapper;

    /**
     * 分页获取订单信息
     *
     * @param page     分页对象
     * @param goodsOrderInfo 订单信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectOrderPage(Page<GoodsOrderInfo> page, GoodsOrderInfo goodsOrderInfo) {
        return baseMapper.selectOrderPage(page, goodsOrderInfo);
    }

    /**
     * 添加订单信息
     *
     * @param orderInfoVo 订单信息
     * @param flag 是否付款完成（0.否 1.是）
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean orderAdd(GoodsOrderInfoVo orderInfoVo, boolean flag) {
        // 添加订单信息
        GoodsOrderInfo goodsOrderInfo = new GoodsOrderInfo();
        goodsOrderInfo.setOrderStatus(flag ? 3 : 0);
        goodsOrderInfo.setCode("OR-" + System.currentTimeMillis());
        goodsOrderInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        goodsOrderInfo.setPharmacyId(orderInfoVo.getPharmacyId());
        // 所属用户
        UserInfo userInfo = userInfoMapper.selectOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, orderInfoVo.getUserId()));
        if (userInfo != null) {
            goodsOrderInfo.setUserId(userInfo.getId());
        }
        this.save(goodsOrderInfo);
        // 添加订单详情信息
        List<OrderDetail> detailList = JSONUtil.toList(orderInfoVo.getOrderDetailList(), OrderDetail.class);
        if (CollectionUtil.isNotEmpty(detailList)) {
            BigDecimal totalCost = BigDecimal.ONE;
            // 计算商品总价，绑定订单
            for (OrderDetail e : detailList) {
                e.setOrderId(goodsOrderInfo.getId());
                e.setAllPrice(e.getUnitPrice().multiply(BigDecimal.valueOf(e.getQuantity())));
                totalCost = totalCost.add(e.getAllPrice());
            }
            goodsOrderInfo.setTotalCost(totalCost);
            orderDetailService.saveBatch(detailList);
        }
        boolean result = this.updateById(goodsOrderInfo);
        if (flag) {
            this.orderPaymentPlatform(goodsOrderInfo.getCode(), orderInfoVo.getStaffCode());
        }
        // 重新更新订单信息

        return result;
    }

    /**
     * 平台内订单付款
     *
     * @param orderCode 订单编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderPaymentPlatform(String orderCode, String staffCode) {
        if (StrUtil.isEmpty(orderCode)) {
            return;
        }
        // 获取订单信息
        GoodsOrderInfo goodsOrderInfo = this.getOne(Wrappers.<GoodsOrderInfo>lambdaQuery().eq(GoodsOrderInfo::getCode, orderCode));
        // 订单详情
        List<OrderDetail> detailList = orderDetailService.list(Wrappers.<OrderDetail>lambdaQuery().eq(OrderDetail::getOrderId, goodsOrderInfo.getId()));
        Map<Integer, Integer> detailMap = detailList.stream().collect(Collectors.toMap(OrderDetail::getDrugId, OrderDetail::getQuantity));
        // 根据商品ID获取库存信息
        List<PharmacyInventory> inventoryList = pharmacyInventoryService.list(Wrappers.<PharmacyInventory>lambdaQuery().in(PharmacyInventory::getDrugId, detailMap.keySet()).eq(PharmacyInventory::getPharmacyId, goodsOrderInfo.getPharmacyId()));
        List<InventoryStatistics> statisticsList = new ArrayList<>();

        String finalStaffCode = staffCode;
        inventoryList.forEach(e -> {
            InventoryStatistics inventoryStatistics = new InventoryStatistics();
            inventoryStatistics.setDrugId(e.getDrugId());
            inventoryStatistics.setPharmacyId(e.getPharmacyId());
            inventoryStatistics.setQuantity(detailMap.get(e.getDrugId()));
            inventoryStatistics.setStorageType(1);
            inventoryStatistics.setCustodian(finalStaffCode);
            inventoryStatistics.setCreateDate(DateUtil.formatDateTime(new Date()));
            statisticsList.add(inventoryStatistics);
            e.setReserve(e.getReserve() - detailMap.get(e.getDrugId()));
        });
        // 修改库存信息
        pharmacyInventoryService.updateBatchById(inventoryList);
        // 添加库房统计
        inventoryStatisticsService.saveBatch(statisticsList);
        goodsOrderInfo.setOrderStatus(1);
        this.updateById(goodsOrderInfo);
    }

    /**
     * 获取用户订单统计
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectOrderRateByUser(Integer userId) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        if (userId == null) {
            return result;
        }
        // 获取用户信息
        UserInfo userInfo = userInfoMapper.selectOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, userId));
        if (userInfo == null) {
            return result;
        }
        result.put("user", userInfo);
        // 获取用户订单
        List<GoodsOrderInfo> orderList = this.list(Wrappers.<GoodsOrderInfo>lambdaQuery().eq(GoodsOrderInfo::getUserId, userInfo.getUserId()));
        if (CollectionUtil.isEmpty(orderList)) {
            return result;
        }
        LinkedHashMap<String, Object> rate = new LinkedHashMap<>();
        return null;
    }

    /**
     * 用户提交订单
     *
     * @param orderDetailVo 订单信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean orderSubmit(OrderDetailVo orderDetailVo) throws FebsException {
        // 获取用户信息
        UserInfo userInfo = userInfoMapper.selectOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, orderDetailVo.getUserId()));
        if (null == userInfo) {
            throw new FebsException("未获取到用户信息");
        }
        // 获取订单信息
        List<OrderSubVo> orderSubVos = JSONUtil.toList(orderDetailVo.getDrugString(), OrderSubVo.class);
        // 根据商家分组
        Map<Integer, List<OrderSubVo>> orderSubMap = orderSubVos.stream().collect(Collectors.groupingBy(OrderSubVo::getPharmacyId));
        // 设置要添加订单
        List<GoodsOrderInfo> orderList = new ArrayList<>();
        // 添加的订单详情
        List<OrderDetail> orderDetailList = new ArrayList<>();

        orderSubMap.forEach((key, value) -> {
            GoodsOrderInfo orderItem = new GoodsOrderInfo();
            orderItem.setCode("GOR-" + StrUtil.toString(System.currentTimeMillis()) + key);
            orderItem.setPharmacyId(key);
            orderItem.setCreateDate(DateUtil.formatDateTime(new Date()));
            orderItem.setOrderStatus(0);
            orderItem.setUserId(userInfo.getId());
            this.save(orderItem);
            // 总价格
            BigDecimal totalCost = BigDecimal.ZERO;
            for (OrderSubVo orderSubItem: value) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setDrugId(orderSubItem.getDrugId());
                orderDetail.setQuantity(orderSubItem.getTotal().intValue());
                orderDetail.setUnitPrice(orderSubItem.getUnitPrice());
                orderDetail.setOrderId(orderItem.getId());
                orderDetail.setAllPrice(orderDetail.getUnitPrice().multiply(orderSubItem.getTotal()));
                totalCost = totalCost.add(orderDetail.getAllPrice());
                orderDetailList.add(orderDetail);
            }
            orderItem.setTotalCost(totalCost);
            this.updateById(orderItem);
        });
        return orderDetailService.saveBatch(orderDetailList);
    }

    /**
     * 用户提交支付订单
     *
     * @param orderDetailVo 订单信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String orderSubmitPay(OrderDetailVo orderDetailVo) throws FebsException {
        // 获取用户信息
        UserInfo userInfo = userInfoMapper.selectOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, orderDetailVo.getUserId()));
        if (null == userInfo) {
            throw new FebsException("未获取到用户信息");
        }
        // 获取订单信息
        List<OrderSubVo> orderSubVos = JSONUtil.toList(orderDetailVo.getDrugString(), OrderSubVo.class);
        // 根据商家分组
        Map<Integer, List<OrderSubVo>> orderSubMap = orderSubVos.stream().collect(Collectors.groupingBy(OrderSubVo::getPharmacyId));
        // 设置要添加订单
        List<GoodsOrderInfo> orderList = new ArrayList<>();
        // 添加的订单详情
        List<OrderDetail> orderDetailList = new ArrayList<>();

        orderSubMap.forEach((key, value) -> {
            GoodsOrderInfo orderItem = new GoodsOrderInfo();
            orderItem.setCode(StrUtil.toString(System.currentTimeMillis()) + key);
            orderItem.setPharmacyId(key);
            orderItem.setCreateDate(DateUtil.formatDateTime(new Date()));
            orderItem.setOrderStatus(0);
            orderItem.setUserId(userInfo.getId());
            this.save(orderItem);
            // 总价格
            BigDecimal totalCost = BigDecimal.ZERO;
            for (OrderSubVo orderSubItem: value) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setDrugId(orderSubItem.getDrugId());
                orderDetail.setQuantity(orderSubItem.getTotal().intValue());
                orderDetail.setUnitPrice(orderSubItem.getUnitPrice());
                orderDetail.setOrderId(orderItem.getId());
                orderDetail.setAllPrice(orderDetail.getUnitPrice().multiply(orderSubItem.getTotal()));
                totalCost = totalCost.add(orderDetail.getAllPrice());
                orderDetailList.add(orderDetail);
            }
            orderItem.setTotalCost(totalCost);
            this.updateById(orderItem);
        });
        orderDetailService.saveBatch(orderDetailList);
        return null;
    }

    /**
     * 订单付款
     *
     * @param orderCode 订单编号
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean orderPayment(String orderCode) {
        if (StrUtil.isEmpty(orderCode)) {
            return false;
        }
        // 获取订单信息
        GoodsOrderInfo goodsOrderInfo = this.getOne(Wrappers.<GoodsOrderInfo>lambdaQuery().eq(GoodsOrderInfo::getCode, orderCode));
        // 修改订单状态
        goodsOrderInfo.setOrderStatus(1);
        // 订单详情
        List<OrderDetail> detailList = orderDetailService.list(Wrappers.<OrderDetail>lambdaQuery().eq(OrderDetail::getOrderId, goodsOrderInfo.getId()));
        Map<Integer, Integer> detailMap = detailList.stream().collect(Collectors.toMap(OrderDetail::getDrugId, OrderDetail::getQuantity));
        // 根据商品ID获取库存信息
        List<PharmacyInventory> inventoryList = pharmacyInventoryService.list(Wrappers.<PharmacyInventory>lambdaQuery().in(PharmacyInventory::getDrugId, detailMap.keySet()).eq(PharmacyInventory::getPharmacyId, goodsOrderInfo.getPharmacyId()));
        List<InventoryStatistics> statisticsList = new ArrayList<>();

        inventoryList.forEach(e -> {
            e.setReserve(e.getReserve() - detailMap.get(e.getDrugId()));
            InventoryStatistics inventoryStatistics = new InventoryStatistics();
            inventoryStatistics.setDrugId(e.getDrugId());
            inventoryStatistics.setPharmacyId(e.getPharmacyId());
            inventoryStatistics.setQuantity(e.getReserve());
            inventoryStatistics.setStorageType(1);
            inventoryStatistics.setCreateDate(DateUtil.formatDateTime(new Date()));
            statisticsList.add(inventoryStatistics);
        });

        // 修改库存信息
        pharmacyInventoryService.updateBatchById(inventoryList);
        // 添加库房统计
        inventoryStatisticsService.saveBatch(statisticsList);
        return this.updateById(goodsOrderInfo);
    }

    /**
     * 订单打印小票
     *
     * @param orderId 订单ID
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> receipt(Integer orderId) {
        if (orderId == null) {
            return null;
        }
        // 获取订单信息
        GoodsOrderInfo goodsOrderInfo = this.getById(orderId);
        // 商家信息
        PharmacyInfo pharmacyInfo = pharmacyInfoMapper.selectOne(Wrappers.<PharmacyInfo>lambdaQuery().eq(PharmacyInfo::getId, goodsOrderInfo.getPharmacyId()));
        goodsOrderInfo.setPharmacyName(pharmacyInfo.getName());
        // 客户信息
        UserInfo userInfo = userInfoMapper.selectOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getId, goodsOrderInfo.getUserId()));
        goodsOrderInfo.setUserName(userInfo.getName());
        // 订单详情
        List<LinkedHashMap<String, Object>> detailList = orderDetailService.selectDetailByOrder(orderId);
        // 返回数据
        return new LinkedHashMap<String, Object>() {
            {
                put("order", goodsOrderInfo);
                put("detail", detailList);
            }
        };
    }
}
