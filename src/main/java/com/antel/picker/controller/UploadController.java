package com.antel.picker.controller;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import com.antel.picker.dao.InstitutionDao;
import com.antel.picker.model.DrawHisDto;
import com.antel.picker.model.Institution;
import com.antel.picker.model.QueryParam;
import com.antel.picker.model.ResponseObject;
import com.antel.picker.service.DrawService;
import com.antel.picker.service.SubjectService;
import com.antel.picker.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

@Controller
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("upload")
    @ResponseBody
    public String upload(MultipartFile file) {
        try {
            List<Object> list = EasyExcelFactory.read(file.getInputStream(), new Sheet(0));
            uploadService.save(list);
            return "success";
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }
}
