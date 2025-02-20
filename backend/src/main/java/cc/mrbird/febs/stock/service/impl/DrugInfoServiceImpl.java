package cc.mrbird.febs.stock.service.impl;

import cc.mrbird.febs.stock.entity.DrugInfo;
import cc.mrbird.febs.stock.dao.DrugInfoMapper;
import cc.mrbird.febs.stock.service.IDrugInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class DrugInfoServiceImpl extends ServiceImpl<DrugInfoMapper, DrugInfo> implements IDrugInfoService {

    /**
     * 分页获取商品信息
     *
     * @param page     分页对象
     * @param drugInfo 商品信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectDrugPage(Page<DrugInfo> page, DrugInfo drugInfo) {
        return baseMapper.selectDrugPage(page, drugInfo);
    }
}
