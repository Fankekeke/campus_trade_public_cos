package cc.mrbird.febs.stock.dao;

import cc.mrbird.febs.stock.entity.GoodsInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface GoodsInfoMapper extends BaseMapper<GoodsInfo> {

    /**
     * 分页获取商品信息
     *
     * @param page     分页对象
     * @param goodsInfo 商品信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectDrugPage(Page<GoodsInfo> page, @Param("goodsInfo") GoodsInfo goodsInfo);
}
