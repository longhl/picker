package com.antel.picker.controller;

import com.alibaba.fastjson.JSON;
import com.antel.picker.model.Institution;
import com.antel.picker.model.ResponseObject;
import com.antel.picker.model.SubInstLevel;
import com.antel.picker.model.Subject;
import com.antel.picker.service.SubjectService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.expression.Lists;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @RequestMapping("/view")
    public String subjectView() {
        return "subjectView";
    }

    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    @ResponseBody
    public List<Subject> getSubjectList() {
        List<Subject> list = subjectService.getAllSubject();
        return list;
    }

    @RequestMapping(value = "/level")
    @ResponseBody
    public List<SubInstLevel> getSubInstLevel(Integer subId) {
        if (subId == null) {
            return new ArrayList<>();
        }
        List<SubInstLevel> level = subjectService.getSubInstLevel(subId);
        return level;
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @ResponseBody
    public List<Institution> getDetail(Integer subId, Integer levelId) {
        List<Institution> list2 = subjectService.getInstList2(subId, levelId);
        return list2;
    }

    /**
     * 新增项目
     *
     * @param subject
     * @param list
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResponseObject addProject(Subject subject, @RequestParam("list") String list) {
        List<Institution> array = new ArrayList<>();
        if (StringUtils.isNotBlank(list)) {
            list = list.substring(1, list.length() - 1);
            array = JSON.parseArray(list, Institution.class);
        }
        subject.setInstList(array);
        subjectService.insert(subject);
        subjectService.insertSubjectInstRel(subject);
        return ResponseObject.SUCCESS(subject);
    }

    @RequestMapping("/getCurrentInstList")
    @ResponseBody
    public List<Institution> getCurrentInstList(@RequestParam(value = "id", required = false) Integer id, @RequestParam(value = "levelId", required = false) Integer levelId) {
        if (id == null) {
            return new ArrayList<>();
        } else if (levelId != null) {
            return subjectService.getInstList2(id, levelId);
        } else {
            return subjectService.getInstList(id);
        }
    }

    @RequestMapping("/update")
    @ResponseBody
    public ResponseObject update(Subject subject, @RequestParam("list") String list, HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        List<Institution> array = new ArrayList<>();
        if (StringUtils.isNotBlank(list)) {
            list = list.substring(1, list.length() - 1);
            array = JSON.parseArray(list, Institution.class);
        }
        subject.setInstList(array);
        subjectService.updateSubject(subject);
        return ResponseObject.SUCCESS();
    }

    @RequestMapping("/delete")
    @ResponseBody
    public ResponseObject delete(Subject subject) {
        subjectService.delete(subject);
        return ResponseObject.SUCCESS();
    }

    @RequestMapping("/removeInst")
    @ResponseBody
    public ResponseObject removeInst(@RequestParam("subId") Integer subId, @RequestParam("instId") Integer instId) {
        subjectService.removeInst(subId, instId);
        return ResponseObject.SUCCESS();
    }
}
