package com.antel.picker.dao;

import com.antel.picker.model.DrawHis;
import com.antel.picker.model.DrawHisDto;
import com.antel.picker.model.QueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface DrawHisDao {

    void insert(DrawHis his);

    Integer nextVersion(@Param("proId") Integer proId);

    List<DrawHisDto> getDrawHis(QueryParam param);

    DrawHis getDrawHisById(Integer id);

    void delete(Integer id);

    List<DrawHis> getDrawHisByPro(Integer projectId);
}
