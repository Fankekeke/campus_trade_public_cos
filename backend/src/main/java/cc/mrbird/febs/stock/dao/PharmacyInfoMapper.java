package cc.mrbird.febs.stock.dao;

import cc.mrbird.febs.stock.entity.PharmacyInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface PharmacyInfoMapper extends BaseMapper<PharmacyInfo> {

    /**
     * 分页获取商家信息
     *
     * @param page     分页对象
     * @param pharmacyInfo 商家信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectPharmacyPage(Page<PharmacyInfo> page, @Param("pharmacyInfo") PharmacyInfo pharmacyInfo);
}
