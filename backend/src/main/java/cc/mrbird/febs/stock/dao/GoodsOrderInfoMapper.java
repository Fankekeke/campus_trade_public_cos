package cc.mrbird.febs.stock.dao;

import cc.mrbird.febs.stock.entity.GoodsOrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface GoodsOrderInfoMapper extends BaseMapper<GoodsOrderInfo> {

    /**
     * 分页获取订单信息
     *
     * @param page      分页对象
     * @param goodsOrderInfo 订单信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectOrderPage(Page<GoodsOrderInfo> page, @Param("goodsOrderInfo") GoodsOrderInfo goodsOrderInfo);

    /**
     * 查询总收益
     *
     * @return 结果
     */
    BigDecimal selectOrderPrice(@Param("pharmacyId") Integer pharmacyId);

    /**
     * 获取本月订单信息
     *
     * @return 结果
     */
    List<GoodsOrderInfo> selectOrderByMonth(@Param("pharmacyId") Integer pharmacyId);

    /**
     * 获取本年订单信息
     *
     * @return 结果
     */
    List<GoodsOrderInfo> selectOrderByYear(@Param("pharmacyId") Integer pharmacyId);

    /**
     * 十天内订单数量统计
     *
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectOrderNumWithinDays(@Param("pharmacyId") Integer pharmacyId);

    /**
     * 十天内订单收益统计
     *
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectOrderPriceWithinDays(@Param("pharmacyId") Integer pharmacyId);

    /**
     * 订单销售商品类别统计
     *
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectOrderDrugType(@Param("pharmacyId") Integer pharmacyId);

    /**
     * 获取用户订单物流
     *
     * @param userId 用户ID
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectOrderLogistics(@Param("userId") Integer userId);

    /**
     * 根据时间获取订单信息
     *
     * @param year  年度
     * @param month 月度
     * @return 结果
     */
    List<GoodsOrderInfo> selectOrderByCheckMonth(@Param("year") Integer year, @Param("month") Integer month);
}
