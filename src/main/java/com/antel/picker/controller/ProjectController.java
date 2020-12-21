package com.antel.picker.controller;

import com.alibaba.fastjson.JSON;
import com.antel.picker.dao.InstitutionDao;
import com.antel.picker.dao.ProjectDao;
import com.antel.picker.model.Institution;
import com.antel.picker.model.Project;
import com.antel.picker.model.ResponseObject;
import com.antel.picker.model.Subject;
import com.antel.picker.service.SubjectService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private InstitutionDao institutionDao;

    @Autowired
    private SubjectService subjectService;

    /**
     * 新增项目
     *
     * @param project
     * @param list
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResponseObject addProject(Project project, @RequestParam("list") String list) {
        List<Institution> array = new ArrayList<>();
        if (StringUtils.isNotBlank(list)) {
            list = list.substring(1, list.length() - 1);
            array = JSON.parseArray(list, Institution.class);
        }
        project.setElements(array);
        project.setAmount(array.size());
        project.setSource("normal");
        projectDao.insert(project);
        projectDao.insertProjectInstRel(project);

        Subject subject = project.getSubject();
        if (subject != null && subject.getId() != null) {
            subject = subjectService.getSubject(subject.getId());
            project.setSubject(subject);
        }
        return ResponseObject.SUCCESS(project);
    }

    /**
     * 根据id删除项目
     *
     * @param id
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    public ResponseObject delProject(@RequestParam("id") Integer id) {
        projectDao.delProjectById(id);
        return ResponseObject.SUCCESS();
    }

    /**
     * 查询所有项目
     *
     * @return
     */
    @RequestMapping("/listAll")
    @ResponseBody
    public List<Project> listAll() {
        List<Project> list = projectDao.selectAll();
        return list;
    }

    @RequestMapping("/get")
    @ResponseBody
    public Project getProject(@RequestParam("projectId") Integer projectId) {
        Project project = projectDao.getById(projectId);
        List<Institution> institutions = institutionDao.selectDrawList(projectId);
        project.setElements(institutions);
        return project;
    }

    @RequestMapping("/ajaxGetProjectInfo")
    @ResponseBody
    public ResponseObject ajaxGetProjectInfo() {
        Project project = projectDao.getLatestProject();
        return ResponseObject.SUCCESS(project);
    }
}
