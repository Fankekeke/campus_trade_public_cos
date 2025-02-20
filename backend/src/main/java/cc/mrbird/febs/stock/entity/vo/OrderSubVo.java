package cc.mrbird.febs.stock.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderSubVo {

    /**
     * 所属商家
     */
    private Integer pharmacyId;

    /**
     * 商品ID
     */
    private Integer drugId;

    /**
     * 购买数量
     */
    private BigDecimal total;

    /**
     * 单价
     */
    private BigDecimal unitPrice;
}
