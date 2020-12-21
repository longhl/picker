package com.antel.picker.dao;

import com.antel.picker.model.Institution;
import com.antel.picker.model.SubInstLevel;
import com.antel.picker.model.Subject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface SubjectDao {
    List<Subject> selectAll();

    void insert(Subject subject);

    void insertSubjectInstRel(Subject subject);

    void mergeSubjectInstRel(Subject subject);

    List<Institution> getInstList(@Param("subId") Integer subId);

    List<Institution> getInstList2(@Param("subId") Integer subId, @Param("levelId") Integer levelId);

    void updateSubject(Subject subject);

    void delSubInstRel(Subject subject);

    void delSubInstRel2(Subject subject);

    void delSubject(Subject subject);

    void delSingleSubInstRel(@Param("subId") Integer subId, @Param("instId") Integer instId);

    Subject select(Integer id);

    List<SubInstLevel> selectSubInstLevel(Integer subId);
}
