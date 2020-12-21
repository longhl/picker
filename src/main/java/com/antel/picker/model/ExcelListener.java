package com.antel.picker.model;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExcelListener<T> extends AnalysisEventListener {

    private List<T> data = new ArrayList();

    public List<T> getData() {
        return data;
    }

    @Override
    public void invoke(Object o, AnalysisContext analysisContext) {
        data.add((T) o);
        if (data.size() >= 100) {
            data = new ArrayList<>();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
