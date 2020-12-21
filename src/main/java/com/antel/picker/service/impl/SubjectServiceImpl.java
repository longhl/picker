package com.antel.picker.service.impl;

import com.antel.picker.dao.SubjectDao;
import com.antel.picker.model.Institution;
import com.antel.picker.model.SubInstLevel;
import com.antel.picker.model.Subject;
import com.antel.picker.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectDao subjectDao;

    @Override
    public List<Subject> getAllSubject() {
        return subjectDao.selectAll();
    }

    @Override
    public void insert(Subject subject) {
        subjectDao.insert(subject);
    }

    @Override
    public void insertSubjectInstRel(Subject subject) {
        subjectDao.insertSubjectInstRel(subject);
    }

    @Override
    public List<Institution> getInstList(Integer subId) {
        return subjectDao.getInstList(subId);
    }

    @Override
    public List<Institution> getInstList2(Integer subId, Integer levelId) {
        return subjectDao.getInstList2(subId, levelId);
    }

    @Override
    public void updateSubject(Subject subject) {
        subjectDao.updateSubject(subject);

        // 删除列表中不存在的供方
        subjectDao.delSubInstRel2(subject);
        // merge
        subjectDao.mergeSubjectInstRel(subject);
    }

    @Override
    public void delete(Subject subject) {
        subjectDao.delSubInstRel(subject);
        subjectDao.delSubject(subject);
    }

    @Override
    public void removeInst(Integer subId, Integer instId) {
        subjectDao.delSingleSubInstRel(subId, instId);
    }

    @Override
    public Subject getSubject(Integer id) {
        return subjectDao.select(id);
    }

    @Override
    public List<SubInstLevel> getSubInstLevel(Integer subId) {
        return subjectDao.selectSubInstLevel(subId);
    }
}
