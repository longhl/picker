package com.antel.picker.controller;

import com.antel.picker.service.RandomService;
import com.antel.picker.model.ElementModel;
import com.antel.picker.model.ElementPool;
import com.antel.picker.random.MyRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private RandomService randomService;

    @RequestMapping("/success")
    public String success(Map<String, Object> map) {
        map.put("id", "9527");
        return "success";
    }

    /**
     * 提交全部抽签对象
     *
     * @param file
     * @return
     */
    @RequestMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) {
        randomService.createElementPool(file);
        return "succ";
    }

    /**
     * 导出中签结果
     *
     * @param response
     */
    @RequestMapping("/download")
    public void download(HttpServletResponse response) {
        try {

            byte[] buffer = new byte[1024];

            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + "抽签结果.xlsx");
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());

            InputStream templateIs = this.getClass().getResourceAsStream("/templates/result.xlsx");
            randomService.download(templateIs,toClient);
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 抽签对象列表
     *
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public List<ElementModel> list() {
/*        List<ElementModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ElementModel model = new ElementModel();
            model.setCode(String.valueOf(i));
            model.setName("name_" + i);
            model.setStatus(String.valueOf(new Random().nextInt(2)));
            list.add(model);
        }
        return list;*/
        return ElementPool.pool;

    }

    @RequestMapping("drawLots")
    @ResponseBody
    public String drawLots() {
        List<ElementModel> pick = MyRandom.pick(ElementPool.pool, 5);
        for (ElementModel item : pick) {
            item.setStatus("1");
        }
        ElementPool.luckList = pick;
        ElementPool.pool.addAll(pick);
        return "success";
    }
}
