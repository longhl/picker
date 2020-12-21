package com.antel.picker.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Data
@ToString
@Slf4j
public class ElementModel extends BaseRowModel {

    @ExcelProperty(index = 0)
    private String code;

    @ExcelProperty(index = 1)
    private String name;

    private String status;

    @ExcelProperty(index = 2)
    private String statusName;

    public String getStatusName() {

        if (StringUtils.isBlank(status)) {
            return "";
        }
        switch (status) {
            case "0":
                return "未中签";
            case "1":
                return "中签";
            default:
                return "";
        }
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
