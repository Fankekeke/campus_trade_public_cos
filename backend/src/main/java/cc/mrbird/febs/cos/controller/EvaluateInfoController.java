package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.CommodityInfo;
import cc.mrbird.febs.cos.entity.EvaluateInfo;
import cc.mrbird.febs.cos.entity.OrderInfo;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.ICommodityInfoService;
import cc.mrbird.febs.cos.service.IEvaluateInfoService;
import cc.mrbird.febs.cos.service.IOrderInfoService;
import cc.mrbird.febs.cos.service.IUserInfoService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/evaluate-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EvaluateInfoController {

    private final IEvaluateInfoService evaluateInfoService;

    private final IUserInfoService userInfoService;

    private final IOrderInfoService orderInfoService;

    /**
     * 分页获取订单评价信息
     *
     * @param page         分页对象
     * @param evaluateInfo 订单评价信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<EvaluateInfo> page, EvaluateInfo evaluateInfo) {
        return R.ok(evaluateInfoService.selectEvaluatePage(page, evaluateInfo));
    }

    /**
     * 获取订单评价信息
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(evaluateInfoService.list());
    }

    /**
     * 获取订单评价详细信息
     *
     * @param id ID
     * @return 结果
     */
    @GetMapping("/detail/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(evaluateInfoService.getById(id));
    }

    /**
     * 新增订单评价信息
     *
     * @param evaluateInfo 订单评价信息
     * @return 结果
     */
    @PostMapping
    public R save(EvaluateInfo evaluateInfo) {
        // 订单信息
        OrderInfo order = orderInfoService.getById(evaluateInfo.getOrderId());
        evaluateInfo.setUserId(order.getBuyUserId());
        evaluateInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        // 获取卖家信息
        UserInfo user = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, order.getSellUserId()));
        if (user != null) {
            // 设置卖家信用积分 评分5加5积分，评分4+3，评分3不加不减，评分2减1，评分1减3，评分0减5
            if (evaluateInfo.getScore().compareTo(new BigDecimal(5)) == 0) {
                user.setIntegral(user.getIntegral() + 5);
            } else if (evaluateInfo.getScore().compareTo(new BigDecimal(4)) == 0) {
                user.setIntegral(user.getIntegral() + 3);
            } else if (evaluateInfo.getScore().compareTo(new BigDecimal(3)) == 0) {
                // 不加不减
            } else if (evaluateInfo.getScore().compareTo(new BigDecimal(2)) == 0) {
                user.setIntegral(user.getIntegral() - 1);
            } else if (evaluateInfo.getScore().compareTo(new BigDecimal(1)) == 0) {
                user.setIntegral(user.getIntegral() - 3);
            } else if (evaluateInfo.getScore().compareTo(new BigDecimal(0)) == 0) {
                user.setIntegral(user.getIntegral() - 5);
            }
            userInfoService.updateById(user);
        }
        return R.ok(evaluateInfoService.save(evaluateInfo));
    }

    /**
     * 修改订单评价信息
     *
     * @param evaluateInfo 订单评价信息
     * @return 结果
     */
    @PutMapping
    public R edit(EvaluateInfo evaluateInfo) {
        return R.ok(evaluateInfoService.updateById(evaluateInfo));
    }

    /**
     * 删除订单评价信息
     *
     * @param ids 主键IDS
     * @return 结果
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(evaluateInfoService.removeByIds(ids));
    }
}
