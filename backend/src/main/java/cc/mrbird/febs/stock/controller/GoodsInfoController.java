package cc.mrbird.febs.stock.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.stock.entity.GoodsInfo;
import cc.mrbird.febs.stock.entity.PharmacyInfo;
import cc.mrbird.febs.stock.service.IGoodsInfoService;
import cc.mrbird.febs.stock.service.IPharmacyInfoService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/stock/drug-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsInfoController {

    private final IGoodsInfoService drugInfoService;

    private final IPharmacyInfoService pharmacyInfoService;

    /**
     * 分页获取商品信息
     *
     * @param page      分页对象
     * @param goodsInfo 商品信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<GoodsInfo> page, GoodsInfo goodsInfo) {
        return R.ok(drugInfoService.selectDrugPage(page, goodsInfo));
    }

    /**
     * 获取详情信息
     *
     * @param id id
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(drugInfoService.getById(id));
    }

    @GetMapping("/selectDrugDetail")
    public R selectDrugDetail(@RequestParam("drugId") Integer drugId, @RequestParam("pharmacyId") Integer pharmacyId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("drug", drugInfoService.getById(drugId));
        result.put("pharmacy", pharmacyInfoService.getById(pharmacyId));
        return R.ok(result);
    }

    /**
     * 获取信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(drugInfoService.list());
    }

    /**
     * 新增商品信息
     *
     * @param goodsInfo 商品信息
     * @return 结果
     */
    @PostMapping
    public R save(GoodsInfo goodsInfo) {
        goodsInfo.setCode("DG-" + System.currentTimeMillis());
        goodsInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        // 设置商家信息
        PharmacyInfo pharmacyInfo = pharmacyInfoService.getOne(Wrappers.<PharmacyInfo>lambdaQuery().eq(PharmacyInfo::getUserId, goodsInfo.getPharmacyId()));
        if (pharmacyInfo != null) {
            goodsInfo.setPharmacyId(pharmacyInfo.getId());
        }
        return R.ok(drugInfoService.save(goodsInfo));
    }

    /**
     * 修改商品信息
     *
     * @param goodsInfo 商品信息
     * @return 结果
     */
    @PutMapping
    public R edit(GoodsInfo goodsInfo) {
        return R.ok(drugInfoService.updateById(goodsInfo));
    }

    /**
     * 删除商品信息
     *
     * @param ids ids
     * @return 商品信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(drugInfoService.removeByIds(ids));
    }
}
