package com.antel.picker.service.impl;

import com.antel.picker.dao.DrawHisDao;
import com.antel.picker.dao.InstitutionDao;
import com.antel.picker.dao.ProjectDao;
import com.antel.picker.model.DrawHis;
import com.antel.picker.model.Institution;
import com.antel.picker.model.Project;
import com.antel.picker.model.Subject;
import com.antel.picker.service.UploadService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class UploadServiceImpl implements UploadService {

    private static final String PROJECT_SRC = "upload";

    private LocalDate baseDate = LocalDate.of(2018, 5, 28);

    private Long baseLong = 43248L;

    private static Subject subject;

    static {
        subject = new Subject();
        subject.setId(999999999);
    }

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private InstitutionDao institutionDao;

    @Autowired
    private DrawHisDao drawHisDao;

    @Override
    public void save(List<Object> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            for (int i = 1; i < list.size(); i++) {
                List next = (List) list.get(i);
                saveItem(next);
            }
        }
    }

    private void saveItem(List item) {

        Integer projectId = saveProject(item);
        String instIds = saveInst((String) item.get(1));
        saveDrawHis(projectId, instIds, instIds);
    }

    private void saveDrawHis(Integer projectId, String allIds, String targetIds) {

        List<DrawHis> list = drawHisDao.getDrawHisByPro(projectId);
        if (CollectionUtils.isEmpty(list)) {
            DrawHis his = new DrawHis();
            his.setProId(projectId);
            his.setAllElements(allIds);
            his.setTargetElements(targetIds);
            Integer version = drawHisDao.nextVersion(projectId);
            his.setVersion(version);
            drawHisDao.insert(his);
        }
    }

    private Integer saveProject(List item) {
        String str0 = (String) item.get(0);
        String str2 = (String) item.get(2);
        String str3 = (String) item.get(3);
        String str4 = (String) item.get(4);
        Project project;
        List<Project> list = projectDao.getByName(str0);
        if (CollectionUtils.isEmpty(list)) {
            LocalDate localDate = baseDate.plusDays(Long.valueOf(str4) - baseLong);
            // 落地项目对象
            project = new Project();
            project.setName(str0);
            project.setDate(localDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
            project.setDrawer(str2);
            project.setRecorder(str3);
            project.setSubject(subject);
            project.setSource(PROJECT_SRC);
            projectDao.insert(project);
        } else {
            project = list.get(0);
        }
        return project.getId();
    }

    private String saveInst(String nameStr) {
        List<Integer> ids = new ArrayList<>();
        if (StringUtils.isNotBlank(nameStr)) {
            String[] split = nameStr.split(",");
            for (int i = 0; i < split.length; i++) {
                // 不存在就添加一个新的对象
                Institution institution;
                List<Institution> list = institutionDao.selectByName(split[i]);
                if (CollectionUtils.isEmpty(list)) {
                    institution = new Institution();
                    institution.setName(split[i]);
                    institutionDao.insert(institution);
                } else {
                    institution = list.get(0);
                }
                ids.add(institution.getId());
            }
        }
        String s = ids.toString().replace("[", "").replace("]", "").replaceAll(" ", "");
        return s;
    }
}
