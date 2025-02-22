package cc.mrbird.febs.stock.entity.vo;

import cc.mrbird.febs.stock.entity.GoodsOrderInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单管理
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GoodsOrderInfoVo extends GoodsOrderInfo {

    private String orderDetailList;

    private String staffCode;

}
