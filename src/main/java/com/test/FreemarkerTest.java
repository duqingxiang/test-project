package com.test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class FreemarkerTest {


    public static void main(String[] args) {
        Map<String,Object> templateData = Maps.newHashMap();
        templateData.put("summary","summary");
        templateData.put("operationId","operationId");
        templateData.put("tags","tags");
        templateData.put("url","xxxxxxxxxx");
        templateData.put("method","post");


        Map<String,Object> bodyMap = Maps.newHashMap();
        bodyMap.put("symbol","btc_usdt");
        bodyMap.put("r","ssssss");

//        templateData.put("body", JSON.toJSONString(bodyMap).replace("\"","'"));


        List<TestParam> paramList = Lists.newArrayList();

        paramList.add(new TestParam("param1","123",""));
        paramList.add(new TestParam("param22","789",""));

//        templateData.put("queryList",paramList);

        executeTemplate(templateData,"/Users/duqingxiang/IdeaProjects/test-project/src/main/resources","demo.ftl","/Users/duqingxiang/Documents/test_template/","test9.feature");

    }



    public static void executeTemplate(Map<String,Object> paramMap,String templateDirPath, String templateName,String outPath,String name){
        try {

            URL resource = FreemarkerTest.class.getClassLoader().getResource(templateName);
            File file11 = new File(resource.getFile());


            Configuration cfg = new Configuration();
            cfg.setDirectoryForTemplateLoading(file11);
            cfg.setObjectWrapper(new DefaultObjectWrapper());

            Template temp = cfg.getTemplate(file11.getName());
            String fileName = name;
            File file = new File(outPath + fileName);
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            temp.process(paramMap, bw);
            bw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
