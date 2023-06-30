package com.mybatiseasy.test.entity;

import com.mybatiseasy.annotation.Table;
import com.mybatiseasy.annotation.TableField;
import com.mybatiseasy.annotation.TableId;
import com.mybatiseasy.emums.TableIdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serial;
import java.io.Serializable;

/**
 * 全国省市区表
 *
 * @author 
 * @since 2023-06-30
 */
@Table("tx_sys_area")
@ApiModel(value = "SysArea对象", description = "全国省市区表")
public class SysArea implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    @TableId(type = TableIdType.AUTO)
    @TableField(column = "id")
    private Long id;


    @ApiModelProperty("省Id")
    @TableField(column = "province_id")
    private Long provinceId;


    @ApiModelProperty("市Id")
    @TableField(column = "city_id")
    private Long cityId;


    @ApiModelProperty("县区Id")
    @TableField(column = "county_id")
    private Long countyId;


    @ApiModelProperty("村镇Id")
    @TableField(column = "village_id")
    private Long villageId;


    @ApiModelProperty("首字母")
    @TableField(column = "initial")
    private String initial;


    @ApiModelProperty("主键ID")
    @TableId(type = TableIdType.AUTO)
    @TableField(column = "id_o")
    private Long idO;


    @ApiModelProperty("父ID")
    @TableField(column = "parent_id_o")
    private String parentIdO;


    @ApiModelProperty("父ID串")
    @TableField(column = "parent_ids")
    private String parentIds;


    @ApiModelProperty("全称")
    @TableField(column = "full_name")
    private String fullName;


    @ApiModelProperty("简称")
    @TableField(column = "short_name")
    private String shortName;


    @ApiModelProperty("总称")
    @TableField(column = "merger_name")
    private String mergerName;


    @ApiModelProperty("级别")
    @TableField(column = "region_type")
    private Integer regionType;


    @ApiModelProperty("父名称")
    @TableField(column = "parent_name")
    private String parentName;


    @TableField(column = "id_oo")
    private Long idOo;


    @ApiModelProperty("父Id")
    @TableField(column = "parent_id")
    private Long parentId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getCountyId() {
        return countyId;
    }

    public void setCountyId(Long countyId) {
        this.countyId = countyId;
    }

    public Long getVillageId() {
        return villageId;
    }

    public void setVillageId(Long villageId) {
        this.villageId = villageId;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public Long getIdO() {
        return idO;
    }

    public void setIdO(Long idO) {
        this.idO = idO;
    }

    public String getParentIdO() {
        return parentIdO;
    }

    public void setParentIdO(String parentIdO) {
        this.parentIdO = parentIdO;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getMergerName() {
        return mergerName;
    }

    public void setMergerName(String mergerName) {
        this.mergerName = mergerName;
    }

    public Integer getRegionType() {
        return regionType;
    }

    public void setRegionType(Integer regionType) {
        this.regionType = regionType;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Long getIdOo() {
        return idOo;
    }

    public void setIdOo(Long idOo) {
        this.idOo = idOo;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }



    @Override
    public String toString() {
    return "sysArea{" +
            "id=" + id +
            ", provinceId=" + provinceId +
            ", cityId=" + cityId +
            ", countyId=" + countyId +
            ", villageId=" + villageId +
            ", initial=" + initial +
            ", idO=" + idO +
            ", parentIdO=" + parentIdO +
            ", parentIds=" + parentIds +
            ", fullName=" + fullName +
            ", shortName=" + shortName +
            ", mergerName=" + mergerName +
            ", regionType=" + regionType +
            ", parentName=" + parentName +
            ", idOo=" + idOo +
            ", parentId=" + parentId +
    "}";
    }
}
