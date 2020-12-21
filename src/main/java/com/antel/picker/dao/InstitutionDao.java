package com.antel.picker.dao;

import com.antel.picker.model.Institution;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface InstitutionDao {

    //@Select("select * from t_institution")
    List<Institution> selectAll(@Param("name") String name);

    //@Insert("insert into t_institution (name) VALUES(#{name})")
    void insert(Institution inst);

    void update(Institution inst);

    void softDel(Institution inst);

    void softDelBatch(List<String> list);

    //@Select("select * from t_institution")

    List<Institution> selectDrawList(@Param("projectId") Integer projectId);

    Integer getMaxProId();

    List<Institution> selectBiIds(List ids);

    void clearCfgFlag();

    void updateCfg(List list);

    List<Integer> getInstIdInCfgList();

    List<Institution> selectByName(String name);

    void updateCity(String cityName);

    String getCity();
}
