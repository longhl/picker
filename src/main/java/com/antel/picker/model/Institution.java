package com.antel.picker.model;

import lombok.Data;

@Data
public class Institution implements Comparable<Institution> {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 是否中签0：否，1：是
     */
    private String flag;

    /**
     * 状态0：已删除，1：生效中
     */
    private String status;

    /**
     * 状态0：已删除，1：生效中
     */
    private String cfgStatus;

    private String city;

    @Override
    public int compareTo(Institution o) {
        return this.id.compareTo(o.id);
    }
}
