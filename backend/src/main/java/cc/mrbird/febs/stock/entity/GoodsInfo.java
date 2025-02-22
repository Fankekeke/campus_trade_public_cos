package cc.mrbird.febs.stock.entity;

import java.math.BigDecimal;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 商品管理
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("drug_info")
public class GoodsInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 商品编号
     */
    private String code;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 所属品牌
     */
    private String brand;

    /**
     * 所属分类
     */
    private Integer category;

    /**
     * 商品类别
     */
    private Integer classification;

    /**
     * 通用名
     */
    private String commonName;

    /**
     * 型号
     */
    private String dosageForm;

    /**
     * 规格
     */
    private String usages;

    /**
     * 保存状态
     */
    private String applicableSymptoms;

    /**
     * 国产/进口
     */
    private String applicableDisease;

    /**
     * 包装清单
     */
    private String packingList;

    /**
     * 使用剂量
     */
    private String dosageUse;

    /**
     * 有效期
     */
    private Integer validityPeriod;

    /**
     * 批准文号
     */
    private String approvalNumber;

    /**
     * 生产企业
     */
    private String manufacturer;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 商品图片
     */
    private String images;

    /**
     * 所属商家
     */
    private Integer pharmacyId;


}
