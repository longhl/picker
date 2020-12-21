package com.antel.picker.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

@Data
public class DrawHisDto extends BaseRowModel implements Cloneable {
    private String id;

    private String targetIds;

    private String projectId;

    @ExcelProperty(index = 0)
    private String projectName;

    private String subjectId;
    //@ExcelProperty(index = 1)
    private String subjectName;

    private String drawer;
    private String recorder;

    //@ExcelProperty(index = 2)
    @ExcelProperty(index = 1)
    private String instName;

    //@ExcelProperty(index = 3)
    @ExcelProperty(index = 2)
    private String date;

    @Override
    public DrawHisDto clone() throws CloneNotSupportedException {
        return (DrawHisDto) super.clone();
    }
}
