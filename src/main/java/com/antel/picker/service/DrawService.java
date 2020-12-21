package com.antel.picker.service;

import com.antel.picker.model.DrawHisDto;
import com.antel.picker.model.Institution;
import com.antel.picker.model.QueryParam;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface DrawService {

    String doDraw(Integer projectId, String drawFlag);

    List<DrawHisDto> getHis(QueryParam param);

    List<Institution> getHisDetail(Integer hisId);

    void updateCfg(String ids);

    void downloadHis(QueryParam param, InputStream inputStream, OutputStream outputStream);

    void deleteHis(Integer hisId);
}
