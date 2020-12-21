package com.antel.picker.model;

import lombok.Data;

import java.util.List;

/**
 * 科目
 */
@Data
public class Subject {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 科目名称
     */
    private String name;

    /**
     * 供方列表
     */
    private List<Institution> instList;

}
