package com.antel.picker.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;

public interface RandomService {
    void createElementPool(MultipartFile file);

    void download(InputStream is, OutputStream os);
}
