package cc.mrbird.febs.stock.entity.vo;

import cc.mrbird.febs.stock.entity.OrderDetail;
import cc.mrbird.febs.stock.entity.OrderInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 订单管理
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderInfoVo extends OrderInfo {

    private String orderDetailList;

    private String staffCode;

}
