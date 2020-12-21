package com.antel.picker.utils;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.List;

/**
 * @description:
 * @author: chenmingjian
 * @date: 19-3-18 16:16
 */
@Slf4j
public class ExcelUtil {

    /**
     * @param is            输入文件的流对象
     * @param clazz         每行数据转换成的对象类
     * @param sheetNo       第几个sheet
     * @param excelListener excel的监听类
     * @return 是否成功, 如果成功可以再监听类里面取数据
     */
    public static Boolean readExcel(InputStream is, Class<? extends BaseRowModel> clazz, int sheetNo, AnalysisEventListener excelListener) {
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(is);
            // 解析每行结果在listener中处理
            ExcelReader excelReader = EasyExcelFactory.getReader(bis, excelListener);
            excelReader.read(new Sheet(sheetNo, 1, clazz));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * @param os    文件输出流
     * @param clazz Excel实体映射类
     * @param data  导出数据
     * @return
     */
    public static Boolean writeExcel(InputStream templateIs, OutputStream os, Class<? extends BaseRowModel> clazz, List<? extends BaseRowModel> data, int sheetNo) {
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(os);
            ExcelWriter writer = new ExcelWriter(templateIs, bos, ExcelTypeEnum.XLSX, false);
            //写第一个sheet, sheet1  数据全是List<String> 无模型映射关系
            Sheet sheet1 = new Sheet(sheetNo, 0, clazz);
            writer.write(data, sheet1);
            writer.finish();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }


}
