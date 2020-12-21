package com.antel.picker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseObject {
    private Object data;
    private String code;
    private String msg;

    public static ResponseObject SUCCESS() {
        return new ResponseObject(null, "success", "成功");
    }

    public static ResponseObject SUCCESS(Object data) {
        return new ResponseObject(data, "success", "成功");
    }

}
