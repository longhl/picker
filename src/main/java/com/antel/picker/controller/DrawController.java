package com.antel.picker.controller;

import com.antel.picker.dao.InstitutionDao;
import com.antel.picker.model.DrawHisDto;
import com.antel.picker.model.Institution;
import com.antel.picker.model.QueryParam;
import com.antel.picker.model.ResponseObject;
import com.antel.picker.service.DrawService;
import com.antel.picker.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

@Controller
@RequestMapping("/draw")
public class DrawController {

    @Autowired
    private DrawService drawService;
    @Autowired
    private InstitutionDao institutionDao;
    @Autowired
    private SubjectService subjectService;

    @GetMapping("/cfg")
    public String drawConfigView() {
        return "drawConfigView";
    }

    @RequestMapping(value = "/act", method = RequestMethod.POST)
    @ResponseBody
    public ResponseObject actDraw(@RequestParam("pId") Integer pId, @RequestParam("drawFlag") String drawFlag) {
        String ids = drawService.doDraw(pId, drawFlag);
        return ResponseObject.SUCCESS(ids);
    }

    @RequestMapping(value = "/his", method = RequestMethod.GET)
    @ResponseBody
    public List<DrawHisDto> getDrawHis(QueryParam param) {
        return drawService.getHis(param);
    }

    @RequestMapping(value = "/hisDetail", method = RequestMethod.GET)
    @ResponseBody
    public List<Institution> getHisDetail(Integer id) {
        return drawService.getHisDetail(id);
    }

    @PostMapping("updateCfg")
    @ResponseBody
    public ResponseObject updateCfg(@RequestParam("ids") String ids) {
        drawService.updateCfg(ids);
        return ResponseObject.SUCCESS();
    }

    @RequestMapping(value = "listCfg", method = RequestMethod.GET)
    @ResponseBody
    public List<Institution> cfgList(@RequestParam(name = "subjectId", required = false) Integer subjectId) {
        if (subjectId == null) {
            List<Institution> list = institutionDao.selectAll("");
            return list;
        } else {
            List<Institution> list = subjectService.getInstList(subjectId);
            return list;
        }
    }

    /**
     * @param response
     */
    @RequestMapping("/download")
    public void download(HttpServletResponse response, QueryParam param) {
        try {

            byte[] buffer = new byte[1024];

            response.reset();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Type", "video/x-msvideo");

            String fileName = URLEncoder.encode("抽签结果.xlsx", "utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());

            InputStream inputStream = this.getClass().getResourceAsStream("/templates/drawhistemp.xlsx");
            drawService.downloadHis(param, inputStream, outputStream);
            response.setContentType("application/octet-stream");
            outputStream.write(buffer);
            outputStream.flush();
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("del")
    @ResponseBody
    public ResponseObject delete(@RequestParam("hisId") Integer hisId) {
        System.out.println(hisId);
        drawService.deleteHis(hisId);
        return ResponseObject.SUCCESS();
    }
}
