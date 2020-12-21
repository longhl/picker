package com.antel.picker.service.impl;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.antel.picker.dao.DrawHisDao;
import com.antel.picker.dao.InstitutionDao;
import com.antel.picker.dao.ProjectDao;
import com.antel.picker.model.*;
import com.antel.picker.random.MyRandom;
import com.antel.picker.service.DrawService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@Service
public class DrawServiceImpl implements DrawService {

    @Autowired
    private InstitutionDao institutionDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private DrawHisDao drawHisDao;

    @Override
    public String doDraw(Integer projectId, String flag) {
        Project project = projectDao.getById(projectId);
        // 获取样本池
        List<Institution> list = institutionDao.selectDrawList(projectId);
        List<Institution> resultList = new ArrayList<>();

        if (StringUtils.equals(flag, "1")) {
            List<Integer> cfgIds = institutionDao.getInstIdInCfgList();
            // 配置列表不为空
            if (CollectionUtils.isNotEmpty(cfgIds)) {
                // 分组
                List<Institution> g1 = new ArrayList<>();
                List<Institution> g2 = new ArrayList<>();
                for (Institution inst : list) {
                    if (cfgIds.contains(inst.getId())) {
                        g1.add(inst);
                    } else {
                        g2.add(inst);
                    }
                }

                // 抽签池和配置的供方列表没有交集
                if (CollectionUtils.isEmpty(g1)) {
                    resultList = MyRandom.pick(list, project.getDrawNum());
                } else {
                    if (g1.size() == project.getDrawNum()) {
                        resultList.addAll(g1);
                    } else if (g1.size() < project.getDrawNum()) {
                        int dt = project.getDrawNum() - g1.size();
                        List<Institution> dtList = MyRandom.pick(g2, dt);
                        g1.addAll(dtList);
                        resultList.addAll(g1);
                    } else {
                        int dt = g1.size() - project.getDrawNum();
                        List<Institution> dtList = MyRandom.pick(g1, dt);
                        g1.removeAll(dtList);
                        resultList.addAll(g1);
                    }
                }
            } else {
                resultList = MyRandom.pick(list, project.getDrawNum());
            }
        } else {
            resultList = MyRandom.pick(list, project.getDrawNum());
        }

        // 按照id排序
        Collections.sort(list);
        Collections.sort(resultList);

        String idStr1 = getIdStr(resultList);
        String idStr2 = getIdStr(list);

        DrawHis his = new DrawHis();
        his.setProId(projectId);
        his.setAllElements(idStr2);
        his.setTargetElements(idStr1);

        Integer version = drawHisDao.nextVersion(projectId);
        his.setVersion(version);
        drawHisDao.insert(his);

        return idStr1;
    }

    @Override
    public List<DrawHisDto> getHis(QueryParam param) {
        List<DrawHisDto> hisList = drawHisDao.getDrawHis(param);
        return hisList;
    }

    @Override
    public List<Institution> getHisDetail(Integer hisId) {
        DrawHis hisObj = drawHisDao.getDrawHisById(hisId);
        List<Institution> list = getInstitutions(hisObj);
        return list;
    }

    @Override
    public void updateCfg(String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] split = ids.split(",");
            List list = Arrays.asList(split);
            if (CollectionUtils.isNotEmpty(list)) {
                institutionDao.clearCfgFlag();
                institutionDao.updateCfg(list);
            }
        }
    }

    @Override
    public void downloadHis(QueryParam param, InputStream inputStream, OutputStream outputStream) {

        List<DrawHisDto> result = new ArrayList<>();
        List<DrawHisDto> hisList = drawHisDao.getDrawHis(param);
        try {
            for (DrawHisDto drawHisDto : hisList) {
                String idsStr = drawHisDto.getTargetIds();
                if (StringUtils.isNotBlank(idsStr)) {
                    String[] split = StringUtils.split(idsStr, ",");
                    List<Institution> instList = institutionDao.selectBiIds(Arrays.asList(split));
                    for (Institution inst : instList) {
                        DrawHisDto clone = drawHisDto.clone();
                        clone.setInstName(inst.getName());
                        result.add(clone);
                    }
                }
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        // 导出
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(outputStream);
            ExcelWriter writer = new ExcelWriter(inputStream, bos, ExcelTypeEnum.XLSX, false);
            Sheet sheet1 = new Sheet(1, 0, DrawHisDto.class);
            writer.write(result, sheet1);
            mergeRow(result, writer);
            writer.finish();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void deleteHis(Integer id) {
        drawHisDao.delete(id);
    }

    private void mergeRow(List<DrawHisDto> result, ExcelWriter writer) {

        if (result.size() <= 1) {
            return;
        }

        if (result.size() == 2 && result.get(0).getId() == result.get(1).getId()) {
            writer.merge(1, 2, 0, 0);
            writer.merge(1, 2, 2, 2);
            //writer.merge(1, 2, 1, 1);
            //writer.merge(1, 2, 3, 3);
            return;
        }

        int startRow = 0;
        int endRow = 0;
        for (int i = 0; i < result.size() - 1; i++) {
            if (result.get(i).getId() == result.get(i + 1).getId()) {
                endRow++;
            } else {
                if (startRow != endRow) {
                    writer.merge(startRow + 1, endRow + 1, 0, 0);
                    writer.merge(startRow + 1, endRow + 1, 2, 2);
                    //writer.merge(startRow + 1, endRow + 1, 1, 1);
                    //writer.merge(startRow + 1, endRow + 1, 3, 3);
                }
                startRow = i + 1;
                endRow = i + 1;
            }

            // 最后一行
            if (i == result.size() - 2 && startRow != endRow) {
                writer.merge(startRow + 1, endRow + 1, 0, 0);
                writer.merge(startRow + 1, endRow + 1, 2, 2);
                //writer.merge(startRow + 1, endRow + 1, 1, 1);
                //writer.merge(startRow + 1, endRow + 1, 3, 3);
            }
        }
    }

    private List<Institution> getInstitutions(DrawHis drawHis) {
        List<Institution> list = new ArrayList<>();
        String allInstIds = drawHis.getAllElements();
        String targetInstIds = drawHis.getTargetElements();
        if (StringUtils.isNotBlank(allInstIds)) {
            String[] split = StringUtils.split(allInstIds, ",");
            list = institutionDao.selectBiIds(Arrays.asList(split));
            if (StringUtils.isNotBlank(targetInstIds)) {
                String[] split1 = StringUtils.split(targetInstIds, ",");
                List<String> list1 = Arrays.asList(split1);
                for (Institution item : list) {
                    if (list1.contains(item.getId().toString())) {
                        item.setFlag("1");
                    } else {
                        item.setFlag("0");
                    }
                }
            }
        }

        // 删除未中签
        Iterator<Institution> iterator = list.iterator();
        while (iterator.hasNext()) {
            Institution next = iterator.next();
            if (!"1".equals(next.getFlag())) {
                iterator.remove();
            }
        }
        return list;
    }

    private String getIdStr(List<Institution> list) {
        StringBuilder builder = new StringBuilder("");
        for (Institution inst : list) {
            builder.append(inst.getId());
            builder.append(",");
        }
        String isStr = builder.toString();
        return isStr.substring(0, isStr.length() - 1);
    }
}
