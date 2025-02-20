package cc.mrbird.febs.stock.service;

import cc.mrbird.febs.stock.entity.DrugInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IDrugInfoService extends IService<DrugInfo> {

    /**
     * 分页获取商品信息
     *
     * @param page     分页对象
     * @param drugInfo 商品信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectDrugPage(Page<DrugInfo> page, DrugInfo drugInfo);
}
