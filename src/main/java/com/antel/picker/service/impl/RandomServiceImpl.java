package com.antel.picker.service.impl;

import com.antel.picker.service.RandomService;
import com.antel.picker.model.ElementModel;
import com.antel.picker.model.ElementPool;
import com.antel.picker.model.ExcelListener;
import com.antel.picker.utils.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Service
@Slf4j
public class RandomServiceImpl implements RandomService {

    @Override
    public void createElementPool(MultipartFile file) {
        try {
            ExcelListener listener = new ExcelListener<>();
            ExcelUtil.readExcel(file.getInputStream(), ElementModel.class, 1, listener);
            ElementPool.pool = listener.getData();
            log.info(ElementPool.pool.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void download(InputStream is, OutputStream os) {
        ExcelUtil.writeExcel(is, os, ElementModel.class, ElementPool.pool, 1);
    }

}
