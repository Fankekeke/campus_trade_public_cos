package cc.mrbird.febs.stock.entity.vo;

import cc.mrbird.febs.stock.entity.PharmacyInventory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 库存设置
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class InventoryVo {

    private Integer pharmacyId;

    private List<PharmacyInventory> pharmacyInventoryList;
}
