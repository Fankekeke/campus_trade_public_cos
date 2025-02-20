package cc.mrbird.febs.stock.service.impl;

import cc.mrbird.febs.stock.entity.GoodsInfo;
import cc.mrbird.febs.stock.dao.GoodsInfoMapper;
import cc.mrbird.febs.stock.service.IGoodsInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class GoodsInfoServiceImpl extends ServiceImpl<GoodsInfoMapper, GoodsInfo> implements IGoodsInfoService {

    /**
     * 分页获取商品信息
     *
     * @param page     分页对象
     * @param goodsInfo 商品信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectDrugPage(Page<GoodsInfo> page, GoodsInfo goodsInfo) {
        return baseMapper.selectDrugPage(page, goodsInfo);
    }
}
