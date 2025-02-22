package cc.mrbird.febs.stock.controller;


import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.stock.entity.GoodsOrderInfo;
import cc.mrbird.febs.stock.entity.vo.GoodsOrderInfoVo;
import cc.mrbird.febs.stock.entity.vo.OrderDetailVo;
import cc.mrbird.febs.stock.service.IGoodsOrderInfoService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/stock/order-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsOrderInfoController {

    private final IGoodsOrderInfoService orderInfoService;

    /**
     * 分页获取订单信息
     *
     * @param page      分页对象
     * @param goodsOrderInfo 订单信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<GoodsOrderInfo> page, GoodsOrderInfo goodsOrderInfo) {
        return R.ok(orderInfoService.selectOrderPage(page, goodsOrderInfo));
    }

    /**
     * 用户提交订单
     *
     * @param orderDetailVo 订单信息
     * @return 结果
     */
    @PostMapping("/orderSubmit")
    public R orderSubmit(OrderDetailVo orderDetailVo) throws FebsException {
        return R.ok(orderInfoService.orderSubmit(orderDetailVo));
    }

    /**
     * 订单付款
     *
     * @param orderCode 订单编号
     * @return 结果
     */
    @GetMapping("/rollback")
    public R rollback(@RequestParam("orderCode") String orderCode) {
        return R.ok(orderInfoService.update(Wrappers.<GoodsOrderInfo>lambdaUpdate().set(GoodsOrderInfo::getOrderStatus, 3).eq(GoodsOrderInfo::getCode, orderCode)));
    }

    /**
     * 用户提交支付订单
     *
     * @param orderDetailVo 订单信息
     * @return 结果
     */
    @PostMapping("/orderSubmitPay")
    public R orderSubmitPay(OrderDetailVo orderDetailVo) throws FebsException {
        return R.ok(orderInfoService.orderSubmitPay(orderDetailVo));
    }

    /**
     * 平台内添加订单
     *
     * @param orderInfoVo 订单信息
     * @return 结果
     */
    @PostMapping("/platform")
    public R saveOrderByPlatform(GoodsOrderInfoVo orderInfoVo) {
        return R.ok(orderInfoService.orderAdd(orderInfoVo, true));
    }

    /**
     * 添加订单
     *
     * @param orderInfoVo 订单信息
     * @return 结果
     */
    @PostMapping("/order")
    public R saveOrder(GoodsOrderInfoVo orderInfoVo) {
        return R.ok(orderInfoService.orderAdd(orderInfoVo, false));
    }

    /**
     * 订单付款
     *
     * @param orderCode 订单编号
     * @param staffCode 员工编号
     * @return 结果
     */
    @GetMapping("/payment")
    public R orderPaymentPlatform(@RequestParam("orderCode") String orderCode, @RequestParam(value = "staffCode", required = false) String staffCode) {
        orderInfoService.orderPaymentPlatform(orderCode, staffCode);
        return R.ok(true);
    }

    /**
     * 订单打印小票
     *
     * @param orderId 订单ID
     * @return 结果
     */
    @GetMapping("/receipt/export/{orderId}")
    public R receipt(@PathVariable("orderId") Integer orderId) {
        return R.ok(orderInfoService.receipt(orderId));
    }

    /**
     * 更新订单状态
     *
     * @param orderId 订单ID
     * @param status  状态
     * @return 结果
     */
    @GetMapping("/edit/status")
    public R setOrderStatus(@RequestParam("orderId") Integer orderId, @RequestParam("status") Integer status) {
        return R.ok(orderInfoService.update(Wrappers.<GoodsOrderInfo>lambdaUpdate().set(GoodsOrderInfo::getOrderStatus, status).eq(GoodsOrderInfo::getId, orderId)));
    }

    /**
     * 获取详情信息
     *
     * @param id id
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(orderInfoService.getById(id));
    }

    /**
     * 获取详情信息
     *
     * @param code code
     * @return 结果
     */
    @GetMapping("/detail/{code}")
    public R detail(@PathVariable("code") String code) {
        return R.ok(orderInfoService.getOne(Wrappers.<GoodsOrderInfo>lambdaQuery().eq(GoodsOrderInfo::getCode, code)));
    }

    /**
     * 获取信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(orderInfoService.list());
    }

    /**
     * 新增订单信息
     *
     * @param goodsOrderInfo 订单信息
     * @return 结果
     */
    @PostMapping
    public R save(GoodsOrderInfo goodsOrderInfo) {
        return R.ok(orderInfoService.save(goodsOrderInfo));
    }

    /**
     * 修改订单信息
     *
     * @param goodsOrderInfo 订单信息
     * @return 结果
     */
    @PutMapping
    public R edit(GoodsOrderInfo goodsOrderInfo) {
        return R.ok(orderInfoService.updateById(goodsOrderInfo));
    }

    /**
     * 删除订单信息
     *
     * @param ids ids
     * @return 订单信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(orderInfoService.removeByIds(ids));
    }

}
