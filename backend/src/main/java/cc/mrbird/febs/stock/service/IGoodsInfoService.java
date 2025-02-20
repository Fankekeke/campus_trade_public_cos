package cc.mrbird.febs.stock.service;

import cc.mrbird.febs.stock.entity.GoodsInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IGoodsInfoService extends IService<GoodsInfo> {

    /**
     * 分页获取商品信息
     *
     * @param page     分页对象
     * @param goodsInfo 商品信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectDrugPage(Page<GoodsInfo> page, GoodsInfo goodsInfo);
}
