package cc.mrbird.febs.stock.service;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.stock.entity.GoodsOrderInfo;
import cc.mrbird.febs.stock.entity.vo.GoodsOrderInfoVo;
import cc.mrbird.febs.stock.entity.vo.OrderDetailVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IGoodsOrderInfoService extends IService<GoodsOrderInfo> {

    /**
     * 分页获取订单信息
     *
     * @param page      分页对象
     * @param goodsOrderInfo 订单信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectOrderPage(Page<GoodsOrderInfo> page, GoodsOrderInfo goodsOrderInfo);

    /**
     * 添加订单信息
     *
     * @param orderInfoVo 订单信息
     * @return 结果
     */
    boolean orderAdd(GoodsOrderInfoVo orderInfoVo, boolean flag);

    /**
     * 订单付款
     *
     * @param orderCode 订单编号
     * @return 结果
     */
    boolean orderPayment(String orderCode);

    /**
     * 订单打印小票
     *
     * @param orderId 订单ID
     * @return 结果
     */
    LinkedHashMap<String, Object> receipt(Integer orderId);

    /**
     * 订单付款
     *
     * @param orderCode 订单编号
     * @param staffCode 员工编号
     */
    void orderPaymentPlatform(String orderCode, String staffCode);

    /**
     * 获取用户订单统计
     *
     * @param userId 用户ID
     * @return 结果
     */
    LinkedHashMap<String, Object> selectOrderRateByUser(Integer userId);

    /**
     * 用户提交订单
     *
     * @param orderDetailVo 订单信息
     * @return 结果
     */
    boolean orderSubmit(OrderDetailVo orderDetailVo) throws FebsException;

    /**
     * 用户提交支付订单
     *
     * @param orderDetailVo 订单信息
     * @return 结果
     */
    String orderSubmitPay(OrderDetailVo orderDetailVo) throws FebsException;
}
