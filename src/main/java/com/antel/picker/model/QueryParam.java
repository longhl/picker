package com.antel.picker.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class QueryParam {
    Map<String, Object> sp = new HashMap<>();
}
