package com.antel.picker.service;

import com.antel.picker.model.Institution;
import com.antel.picker.model.SubInstLevel;
import com.antel.picker.model.Subject;

import java.util.List;

public interface SubjectService {
    List<Subject> getAllSubject();

    void insert(Subject subject);

    void insertSubjectInstRel(Subject subject);

    List<Institution> getInstList(Integer subId);

    List<Institution> getInstList2(Integer subId, Integer levelId);

    void updateSubject(Subject subject);

    void delete(Subject subject);

    void removeInst(Integer subId, Integer instId);

    Subject getSubject(Integer id);

    List<SubInstLevel> getSubInstLevel(Integer subId);
}
