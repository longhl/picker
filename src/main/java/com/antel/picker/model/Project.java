package com.antel.picker.model;

import lombok.Data;

import java.util.List;

@Data
public class Project {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 科目
     */
    private Subject subject;

    /**
     * 摇号人
     */
    private String drawer;

    /**
     * 记录人
     */
    private String recorder;

    /**
     * 时间
     */
    private String date;

    /**
     * 中签数
     */
    private Integer drawNum;

    /**
     * 总数
     */
    private Integer amount;

    /**
     * 供方列表
     */
    private List<Institution> elements;

    /**
     * 数据源
     */
    private String source;

}
