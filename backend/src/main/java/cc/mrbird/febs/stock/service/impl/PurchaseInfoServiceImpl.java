package cc.mrbird.febs.stock.service.impl;

import cc.mrbird.febs.stock.entity.GoodsInfo;
import cc.mrbird.febs.stock.entity.PharmacyInventory;
import cc.mrbird.febs.stock.entity.PurchaseInfo;
import cc.mrbird.febs.stock.dao.PurchaseInfoMapper;
import cc.mrbird.febs.stock.service.IGoodsInfoService;
import cc.mrbird.febs.stock.service.IPharmacyInventoryService;
import cc.mrbird.febs.stock.service.IPurchaseInfoService;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PurchaseInfoServiceImpl extends ServiceImpl<PurchaseInfoMapper, PurchaseInfo> implements IPurchaseInfoService {

    private final IGoodsInfoService drugInfoService;

    private final IPharmacyInventoryService inventoryService;

    /**
     * 分页获取商品采购信息
     *
     * @param page 分页对象
     * @param purchaseInfo 商品采购信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectPurchasePage(Page<PurchaseInfo> page, PurchaseInfo purchaseInfo) {
        return baseMapper.selectPurchasePage(page, purchaseInfo);
    }

    /**
     * 收货
     *
     * @param id 采购ID
     * @return 结果
     */
    @Override
    public boolean receipt(Integer id) throws Exception {
        // 获取采购信息
        PurchaseInfo purchase = this.getById(id);
        purchase.setStatus(2);
        inventoryService.batchPutInventory(purchase.getPharmacyId(), purchase.getPurchaseDrug());

        return this.updateById(purchase);
    }

    /**
     * 新增商品采购信息
     *
     * @param purchaseInfo 商品采购信息
     * @return 结果
     */
    @Override
    public boolean purchaseAdd(PurchaseInfo purchaseInfo) {
        List<PharmacyInventory> inventoryList = JSONUtil.toList(purchaseInfo.getPurchaseDrug(), PharmacyInventory.class);
        BigDecimal totalPrice = BigDecimal.ZERO;
        int amount = 0;

        // 计算采购数量与总价格
        for (PharmacyInventory item : inventoryList) {
            amount += item.getReserve();
            BigDecimal itemPrice = NumberUtil.mul(item.getReserve(), item.getUnitPrice());
            totalPrice = totalPrice.add(itemPrice);
        }

        purchaseInfo.setAmount(amount);
        purchaseInfo.setTotalPrice(totalPrice);

        return this.save(purchaseInfo);
    }

    /**
     * 采购单详情-商品物流
     *
     * @param id 采购ID
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> detailPurchase(String id) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("drug", null);
            }
        };

        // 采购单信息
        PurchaseInfo purchase = this.getById(id);

        List<PharmacyInventory> inventoryList = JSONUtil.toList(purchase.getPurchaseDrug(), PharmacyInventory.class);
        List<Integer> drugIds = inventoryList.stream().map(PharmacyInventory::getDrugId).collect(Collectors.toList());
        List<GoodsInfo> goodsInfoList = (List<GoodsInfo>) drugInfoService.listByIds(drugIds);

        Map<Integer, GoodsInfo> drugMap = goodsInfoList.stream().collect(Collectors.toMap(GoodsInfo::getId, e -> e));
        for (PharmacyInventory pharmacy : inventoryList) {
            GoodsInfo goodsInfo = drugMap.get(pharmacy.getDrugId());
            if (null == goodsInfo) {
                continue;
            }
            pharmacy.setDrugName(goodsInfo.getName());
            pharmacy.setImages(goodsInfo.getImages());
        }


        result.put("drug", inventoryList);

        return result;
    }
}
