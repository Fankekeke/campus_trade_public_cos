package cc.mrbird.febs.stock.service.impl;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.cos.entity.BulletinInfo;
import cc.mrbird.febs.cos.service.IBulletinInfoService;
import cc.mrbird.febs.stock.dao.OrderDetailMapper;
import cc.mrbird.febs.stock.dao.GoodsOrderInfoMapper;
import cc.mrbird.febs.stock.entity.*;
import cc.mrbird.febs.stock.dao.PharmacyInfoMapper;
import cc.mrbird.febs.stock.service.*;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PharmacyInfoServiceImpl extends ServiceImpl<PharmacyInfoMapper, PharmacyInfo> implements IPharmacyInfoService {

    private final IGoodsOrderInfoService orderInfoService;

    private final IInventoryStatisticsService inventoryStatisticsService;

    private final IPharmacyInventoryService pharmacyInventoryService;

    private final IGoodsInfoService drugInfoService;

    private final GoodsOrderInfoMapper orderInfoMapper;

    private final OrderDetailMapper orderDetailMapper;

    private final PharmacyInfoMapper pharmacyInfoMapper;

    private final IBulletinInfoService bulletinInfoService;


    /**
     * 分页获取商家信息
     *
     * @param page     分页对象
     * @param pharmacyInfo 商家信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectPharmacyPage(Page<PharmacyInfo> page, PharmacyInfo pharmacyInfo) {
        return baseMapper.selectPharmacyPage(page, pharmacyInfo);
    }

    /**
     * 查询本月订单数量排行
     *
     * @param type 1.按订单数 2.按交易金额
     * @return 结果
     */
    @Override
    public List<PharmacyOrderRank> selectOrderRank(Integer type) {
        // 所有商家信息
        List<PharmacyInfo> pharmacyInfoList = this.list(Wrappers.<PharmacyInfo>lambdaQuery().eq(PharmacyInfo::getBusinessStatus, 1));
        // 本月订单数据
        List<GoodsOrderInfo> goodsOrderInfoList = orderInfoMapper.selectOrderByMonth(null);
        if (CollectionUtil.isEmpty(goodsOrderInfoList) || CollectionUtil.isEmpty(pharmacyInfoList)) {
            return Collections.emptyList();
        }
        Map<Integer, List<GoodsOrderInfo>> orderMap = goodsOrderInfoList.stream().collect(Collectors.groupingBy(GoodsOrderInfo::getPharmacyId));
        List<PharmacyOrderRank> result = new ArrayList<>();
        pharmacyInfoList.forEach(e -> {
            PharmacyOrderRank pharmacyOrderRank = new PharmacyOrderRank(e.getId(), e.getName(), 0, BigDecimal.ZERO);
            List<GoodsOrderInfo> goodsOrderInfoItemList = orderMap.get(e.getId());
            if (CollectionUtil.isNotEmpty(goodsOrderInfoItemList)) {
                pharmacyOrderRank.setOrderNum(goodsOrderInfoItemList.size());
                BigDecimal totalPrice = goodsOrderInfoItemList.stream().map(GoodsOrderInfo::getTotalCost).reduce(BigDecimal.ZERO, BigDecimal::add);
                pharmacyOrderRank.setTotalPrice(totalPrice);
            }
            result.add(pharmacyOrderRank);
        });
        // 排序
        if (type == 0) {
            return result.stream().sorted(Comparator.comparing(PharmacyOrderRank::getOrderNum)).collect(Collectors.toList());
        } else {
            return result.stream().sorted(Comparator.comparing(PharmacyOrderRank::getTotalPrice)).collect(Collectors.toList());
        }
    }

    /**
     * 查询近十天内各家订单收益统计
     *
     * @return 结果
     */
    @Override
    public List<LinkedHashMap<String, Object>> selectOrderPriceDays() {
        // 正在营业供应商
        List<PharmacyInfo> pharmacyInfoList = this.list(Wrappers.<PharmacyInfo>lambdaQuery().eq(PharmacyInfo::getBusinessStatus, 1));
        if (CollectionUtil.isEmpty(pharmacyInfoList)) {
            return Collections.emptyList();
        }
        // 返回数据
        List<LinkedHashMap<String, Object>> result = new ArrayList<>();
        pharmacyInfoList.forEach(e -> {
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>() {
                {
                    put("name", e.getName());
                    put("value", orderInfoMapper.selectOrderPriceWithinDays(e.getId()));
                }
            };
            result.add(item);
        });
        return result;
    }

    /**
     * 查询近十天内各家订单数量统计
     *
     * @return 结果
     */
    @Override
    public List<LinkedHashMap<String, Object>> selectOrderNumDays() {
        // 正在营业供应商
        List<PharmacyInfo> pharmacyInfoList = this.list(Wrappers.<PharmacyInfo>lambdaQuery().eq(PharmacyInfo::getBusinessStatus, 1));
        if (CollectionUtil.isEmpty(pharmacyInfoList)) {
            return Collections.emptyList();
        }
        // 返回数据
        List<LinkedHashMap<String, Object>> result = new ArrayList<>();
        pharmacyInfoList.forEach(e -> {
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>() {
                {
                    put("name", e.getName());
                    put("value", orderInfoMapper.selectOrderNumWithinDays(e.getId()));
                }
            };
            result.add(item);
        });
        return result;
    }

    /**
     * 查询商家库存信息
     *
     * @param pharmacyId 商家ID
     * @return 结果
     */
    @Override
    public List<LinkedHashMap<String, Object>> selectStockByPharmacy(Integer pharmacyId) {
        // 商家库存信息
        List<PharmacyInventory> pharmacyInventoryList = pharmacyInventoryService.list(Wrappers.<PharmacyInventory>lambdaQuery().eq(PharmacyInventory::getPharmacyId, pharmacyId));
        // 商家信息
        List<PharmacyInfo> pharmacyInfoList = this.list(Wrappers.<PharmacyInfo>lambdaQuery().eq(PharmacyInfo::getBusinessStatus, 1));
        // 商品信息
        List<GoodsInfo> goodsInfoList = drugInfoService.list();
        if (CollectionUtil.isEmpty(pharmacyInventoryList) || CollectionUtil.isEmpty(pharmacyInfoList) || CollectionUtil.isEmpty(goodsInfoList)) {
            return Collections.emptyList();
        }

        List<LinkedHashMap<String, Object>> result = new ArrayList<>();

        // 库房信息根据商家ID转MAP
        Map<Integer, List<PharmacyInventory>> pharmacyInventoryMap = pharmacyInventoryList.stream().collect(Collectors.groupingBy(PharmacyInventory::getPharmacyId));
        // 商家信息转MAP
        Map<Integer, String> pharmacyMap = pharmacyInfoList.stream().collect(Collectors.toMap(PharmacyInfo::getId, PharmacyInfo::getName));
        // 商品信息转MAP
        Map<Integer, String> drugMap = goodsInfoList.stream().collect(Collectors.toMap(GoodsInfo::getId, GoodsInfo::getName));

        pharmacyInfoList.forEach(e -> {
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>() {
                {
                    put("pharmacyName", pharmacyMap.get(e.getId()));
                }
            };
            List<PharmacyInventory> inventoryList = pharmacyInventoryMap.get(e.getId());
            if (CollectionUtil.isEmpty(inventoryList)) {
                item.put("inventory", Collections.emptyList());
            }
            List<Map<String, Object>> drugList = new ArrayList<>();
            inventoryList.forEach(inventory -> {
                Map<String, Object> inventoryItem = new HashMap<String, Object>(16) {
                    {
                        put("name", drugMap.get(inventory.getDrugId()));
                        put("count", inventory.getReserve());
                        put("status", inventory.getShelfStatus());
                    }
                };
                drugList.add(inventoryItem);
            });
            item.put("inventory", drugList);
            result.add(item);
        });
        return result;
    }

    /**
     * 主页数据
     *
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> homeData(Integer pharmacyId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        // 商家信息
        PharmacyInfo pharmacyInfo = this.getOne(Wrappers.<PharmacyInfo>lambdaQuery().eq(PharmacyInfo::getUserId, pharmacyId));
        if (pharmacyInfo != null) {
            pharmacyId = pharmacyInfo.getId();
        }

        // 总订单数量
        result.put("orderCode", orderInfoService.count());
        // 总收益
        result.put("orderPrice", orderInfoMapper.selectOrderPrice(pharmacyId));
        // 店铺数量
        result.put("pharmacyNum", pharmacyInfoMapper.selectCount(Wrappers.<PharmacyInfo>lambdaQuery().eq(PharmacyInfo::getBusinessStatus, 1)));

        // 本月订单数量
        List<GoodsOrderInfo> orderList = orderInfoMapper.selectOrderByMonth(pharmacyId);
        result.put("monthOrderNum", CollectionUtil.isEmpty(orderList) ? 0 : orderList.size());
        BigDecimal orderPrice = orderList.stream().map(GoodsOrderInfo::getTotalCost).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 获取本月收益
        result.put("monthOrderPrice", orderPrice);

        // 本年订单数量
        List<GoodsOrderInfo> orderYearList = orderInfoMapper.selectOrderByYear(pharmacyId);
        result.put("yearOrderNum", CollectionUtil.isEmpty(orderYearList) ? 0 : orderYearList.size());
        // 本年总收益
        BigDecimal orderYearPrice = orderYearList.stream().map(GoodsOrderInfo::getTotalCost).reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("yearOrderPrice", orderYearPrice);

        // 公告信息
        result.put("bulletin", bulletinInfoService.list(Wrappers.<BulletinInfo>lambdaQuery()));

        // 近十天内订单统计
        result.put("orderNumWithinDays", orderInfoMapper.selectOrderNumWithinDays(pharmacyId));
        // 近十天内收益统计
        result.put("orderPriceWithinDays", orderInfoMapper.selectOrderPriceWithinDays(pharmacyId));
        // 订单销售类别统计
        result.put("orderDrugType", orderInfoMapper.selectOrderDrugType(pharmacyId));
        return result;
    }

    /**
     * 根据月份获取商品统计情况
     *
     * @param date 日期
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectStatisticsByMonth(String date) throws FebsException {
        if (StrUtil.isEmpty(date)) {
            throw new FebsException("参数不能为空");
        }

        int year = DateUtil.year(DateUtil.parseDate(date));
        int month = DateUtil.month(DateUtil.parseDate(date)) + 1;

        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("num", Collections.emptyList());
                put("price", Collections.emptyList());
            }
        };

        // 获取订单详情
        List<GoodsOrderInfo> orderList = orderInfoMapper.selectOrderByCheckMonth(year, month);
        if (CollectionUtil.isEmpty(orderList)) {
            return result;
        }

        List<Map<String, Object>> numMap = new ArrayList<>();
        List<Map<String, Object>> priceMap = new ArrayList<>();

        List<Integer> orderIds = orderList.stream().map(GoodsOrderInfo::getId).collect(Collectors.toList());
        List<OrderDetail> detailList = orderDetailMapper.selectList(Wrappers.<OrderDetail>lambdaQuery().in(OrderDetail::getOrderId, orderIds));
        // 按商品ID分组
        Map<Integer, List<OrderDetail>> drugDetailMap = detailList.stream().collect(Collectors.groupingBy(OrderDetail::getDrugId));

        // 商品信息
        List<GoodsInfo> goodsInfoList = (List<GoodsInfo>) drugInfoService.listByIds(drugDetailMap.keySet());
        Map<Integer, String> drugMap = goodsInfoList.stream().collect(Collectors.toMap(GoodsInfo::getId, GoodsInfo::getName));

        drugDetailMap.forEach((key, value) -> {
            String drugName = drugMap.get(key);
            Map<String, Object> numItem = new HashMap<String, Object>() {
                {
                    put("name", drugName);
                }
            };
            Map<String, Object> priceItem = new HashMap<String, Object>() {
                {
                    put("name", drugName);
                }
            };
            // 本月商品销售数量统计
            int num = value.stream().map(OrderDetail::getQuantity).reduce(0, Integer::sum);
            numItem.put("value", num);

            // 本月商品销售金额统计
            BigDecimal price = value.stream().map(OrderDetail::getAllPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            priceItem.put("value", price);
            numMap.add(numItem);
            priceMap.add(priceItem);
        });

        result.put("num", numMap);
        result.put("price", priceMap);
        return result;
    }
}
