package cc.mrbird.febs.stock.entity.vo;

import lombok.Data;

@Data
public class OrderDetailVo {

    /**
     * 所属用户
     */
    private Integer userId;

    /**
     * 购买商品信息
     */
    private String drugString;
}
