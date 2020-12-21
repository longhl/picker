package com.antel.picker.controller;

import com.antel.picker.dao.InstitutionDao;
import com.antel.picker.model.Institution;
import com.antel.picker.model.ResponseObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/inst")
public class InstitutionController {

    @Autowired
    private InstitutionDao institutionDao;

    @RequestMapping(value = "listAll", method = RequestMethod.GET)
    @ResponseBody
    public List<Institution> getLeftList(@RequestParam(required = false) String name) {

        List<Institution> list = institutionDao.selectAll(name);
        return list;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResponseObject add(Institution inst) {
        institutionDao.insert(inst);
        return ResponseObject.SUCCESS();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseObject update(Institution inst) {
        institutionDao.update(inst);
        return ResponseObject.SUCCESS();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseObject delete(String ids) {

        if (StringUtils.isNotBlank(ids)) {
            String[] idArr = ids.split(",");
            List<String> list = Arrays.asList(idArr);
            institutionDao.softDelBatch(list);
        }

        return ResponseObject.SUCCESS();
    }

    @RequestMapping("/drawList")
    @ResponseBody
    public List<Institution> getDrawList(@RequestParam(value = "projectId", required = false) Integer projectId) {
        if (projectId == null) {
            projectId = institutionDao.getMaxProId();
        }
        if (projectId == null) {
            return new ArrayList<>();
        }
        return institutionDao.selectDrawList(projectId);
    }

    @RequestMapping(value = "/city/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResponseObject updateCity(String cityName) {
        institutionDao.updateCity(cityName);
        return ResponseObject.SUCCESS();
    }

    @RequestMapping(value = "/city/get", method = RequestMethod.GET)
    @ResponseBody
    public ResponseObject getCity() {
        String city = institutionDao.getCity();
        return ResponseObject.SUCCESS(city);
    }

    @RequestMapping(value = "/city/combobox", method = RequestMethod.GET)
    @ResponseBody
    public List<CityVo> getCombobox() {
        String city = institutionDao.getCity();

        if (StringUtils.isBlank(city)) {
            return new ArrayList<>();
        } else {
            String replace = city.trim().replace("，", ",");
            String[] strings = StringUtils.split(replace, ",");
            List<CityVo> list = new ArrayList<>();
            for (String name : strings) {
                list.add(new CityVo(name));
            }

            return list;
        }
    }

    public class CityVo {
        private String cityName;

        public CityVo() {
        }

        public CityVo(String cityName) {
            this.cityName = cityName;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }
    }
}
