package com.antel.picker.model;

import lombok.Data;

import java.util.List;

@Data
public class DrawHis {
    private Integer id;
    private Integer proId;
    private Project project;
    private List<Institution> instList;
    private String targetElements;
    private String allElements;
    private Integer version;
    private String createTime;
}
