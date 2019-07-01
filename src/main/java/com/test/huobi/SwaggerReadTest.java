package com.test.huobi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.test.FreemarkerTest;
import com.test.TestParam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SwaggerReadTest {

    static boolean EXECUTE_FREEMARKER_SWITCH = true;
    static boolean DEBUG_FLAG = true;

    public static void main(String[] args) {
        // swagger-ui 所获取到的json保存文件的路径
        String swaggerFilePath = "/Users/duqingxiang/Documents/swagger-api.txt";
        // 根据业务统计出来dw的接口文件
        String dwApiFilePath = "/Users/duqingxiang/Documents/dw-restful-api.txt";
        // 生成模板的文件夹路径
        String templateDirPath = "/Users/duqingxiang/IdeaProjects/test-project/src/main/resources";
        // 生成模板的文件名
        String templateName = "demo.ftl";

        execute(templateDirPath, templateName, swaggerFilePath, dwApiFilePath);

    }


    public static void execute(String templateDirPath, String templateName, String swaggerFilePath, String dwApiFilePath) {
        String swaggerContent = readFileContent(swaggerFilePath);

        // 处理json字符串中关于$ref循环调用导致value值异常
        swaggerContent = swaggerContent.replaceAll("\\$", "@*@");


        log(swaggerContent);

        List<String> dwApiList = getRestApiList(dwApiFilePath);
        if (dwApiList == null || dwApiList.size() <= 0) {
            return;
        }

        System.out.println();
        System.out.println("=========================");
        System.out.println();

        JSONObject swaggerJSON = JSON.parseObject(swaggerContent);

        // 这个是返回结果的对象集合
        JSONObject definitions = swaggerJSON.getJSONObject("definitions");

        // 这个是接口URL信息的对象集合
        JSONObject swaggerPaths = swaggerJSON.getJSONObject("paths");

        for (String url : dwApiList) {


            log("=======>" + url);
            JSONObject path = swaggerPaths.getJSONObject(url);
            log(path.toJSONString());

            buildTemplate(templateDirPath, templateName, url, path, definitions);

        }

    }


    public static void buildTemplate(String templateDirPath, String templateName, String url, JSONObject path, JSONObject definitions) {
        Map<String, Object> templateData = Maps.newHashMap();
        List<Map.Entry<String, Object>> list = Lists.newArrayList(path.entrySet());

        Map.Entry<String, Object> jsonEntry = list.get(0);
        String requestMethod = jsonEntry.getKey();
        JSONObject requestContent = (JSONObject) jsonEntry.getValue();

        String summary = requestContent.getString("summary");
        String operationId = requestContent.getString("operationId");
        String tags = requestContent.getString("tags");


        templateData.put("summary", summary);
        templateData.put("operationId", operationId);
        templateData.put("tags", convertName('-',tags));
        templateData.put("url", url);
        templateData.put("method", requestMethod);


        log("tags:" + tags + "  method:" + requestMethod + "  content:" + requestContent.toJSONString());

        String className = convertName('-', tags.replaceAll("\\[", "")
                .replaceAll("\\]", "")
                .replaceAll("\"", ""));


        String featureName = className
                + "^" +
                url.replaceAll("/", "_")
                + ".feature";


        /////////以下是生成 feature模板的
        List<TestParam> paramList = Lists.newArrayList();
        List<TestParam> queryList = Lists.newArrayList();
        List<TestParam> headerList = Lists.newArrayList();
        List<TestParam> pathList = Lists.newArrayList();
        boolean pathFlag = false;
        boolean bodyFlag = false;
        String pathUrl = "";
        JSONArray parameterArray = requestContent.getJSONArray("parameters");
        Map<String, JSONObject> pathMap = Maps.newHashMap();
        if (parameterArray != null) {
            log(" paramterArray ===> " + parameterArray.toJSONString());
            for (int i = 0; i < parameterArray.size(); i++) {
                JSONObject paramter = parameterArray.getJSONObject(i);
                String in = paramter.getString("in");






                if ("body".equals(in)) {
                    bodyFlag = true;
                    JSONObject schemaObj = paramter.getJSONObject("schema");
                    if (schemaObj != null) {
                        String schemaClassName = schemaObj.getString("@*@ref");
                        if (schemaClassName != null && schemaClassName.trim().length() > 0) {
                            schemaClassName = schemaClassName.replace("#/definitions/", "");
                        }


                        JSONObject schemaClassObj = definitions.getJSONObject(schemaClassName);
                        if (schemaClassObj != null) {
//                                log(" ====> schema::" + schemaClassName + " ::" + schemaClassObj.toJSONString());
                            System.out.println(" ====> schema::" + schemaClassName + " ::" + schemaClassObj.toJSONString());

                            JSONArray requiredArray = schemaClassObj.getJSONArray("required");
                            Map<String, Integer> requiredMap = Maps.newHashMap();
                            if (requiredArray != null && requiredArray.size() > 0) {
                                for (Object o : requiredArray) {
                                    requiredMap.put(o.toString(), 1);
                                }
                            }


                            JSONObject properties = schemaClassObj.getJSONObject("properties");
                            for (Map.Entry<String, Object> entry : properties.entrySet()) {
                                JSONObject prop = (JSONObject) entry.getValue();
                                String key = entry.getKey();

                                String required = requiredMap.get(key) == null ? "false" : "true";


                                String extra = "# name:"+key+" type:" + prop.getString("type") + " required:" + required;
                                String value = convertName('-',key);


                                paramList.add(new TestParam(key, value, extra));


                            }
                        }

                    }

                    log(" parameter: body :" + paramter.toJSONString());
                }

                String name = paramter.getString("name");
                String type = paramter.getString("type");
                String desc = paramter.getString("description");
                String required = paramter.getString("required");
                String extra = "# name:"+name+" desc:"+desc+" type:" + type + " required:" + required;
                String value = convertName('-',name);
                TestParam param = new TestParam(name, value, extra);
                if ("path".equals(in)) {

                    pathFlag = true;

                    pathList.add(param);
                    paramList.add(param);
                    log(" parameter: path :" + paramter.toJSONString());
                }

                if ("query".equals(in)) {

                    queryList.add(param);
                    paramList.add(param);
                    log(" parameter: query :" + paramter.toJSONString());
                }


                if ("header".equals(in)) {

                    headerList.add(param);
                    paramList.add(param);
                    log(" parameter: header :" + paramter.toJSONString());
                }



            }
        }




        if (pathFlag) {
            templateData.put("pathList",pathList);
        }

        if (queryList != null && queryList.size() > 0) {
            templateData.put("queryList", queryList);
        }
        if (headerList != null && headerList.size() > 0) {
            templateData.put("headerList", headerList);
        }
        if (paramList != null && paramList.size() > 0) {
            templateData.put("paramList", paramList);
        }


        templateData.put("pathFlag", pathFlag);
        templateData.put("bodyFlag", bodyFlag);
        templateData.put("featureName", featureName);




        JSONObject responseSchema = requestContent.getJSONObject("responses").getJSONObject("200").getJSONObject("schema");
        if (responseSchema != null) {

            String responseObjectKey = responseSchema.getString("@*@ref");
            Object o = getResponse(definitions, responseObjectKey);

            templateData.put("responseJSON", JSON.toJSONString(o));
        }




        if (EXECUTE_FREEMARKER_SWITCH) {
            FreemarkerTest.executeTemplate(templateData, templateDirPath, templateName, "/Users/duqingxiang/Documents/test_template/", featureName);
        }

        System.out.println("**************************" + featureName + "*****************************");

    }

    public static void addStatistics(Map<String, List<String>> map, String key, String value) {
        List<String> urlList = map.get(key);
        if (urlList == null || urlList.size() <= 0) {
            urlList = Lists.newArrayList();
        }
        urlList.add(value);
        map.put(key, urlList);
    }

    public static List<String> getRestApiList(String dwApiFilePath) {

        BufferedReader br = null;

        List<String> urlList = Lists.newArrayList();
        try {
            String str;
            br = new BufferedReader(new FileReader(dwApiFilePath));

            while ((str = br.readLine()) != null) {


                String[] array = str.split("\\|");
                String url = array[1];
                log("===>" + str);
                urlList.add(url);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return urlList;
    }

    public static String readFileContent(String filePath) {


        BufferedReader br = null;

        StringBuilder builder = new StringBuilder();
        try {
            String str;
            br = new BufferedReader(new FileReader(filePath));

            while ((str = br.readLine()) != null) {

                builder.append(str);

            }

            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }


    public static Object getResponse(JSONObject definitions, String key) {

        String newKey = key.replace("#/definitions/", "");

        if (newKey.startsWith("Collection")) {
            newKey = newKey.replaceAll("Collection«", "").replaceAll("»", "");

            return getResponseList(definitions, newKey);
        } else {
            return getResponseObject(definitions, newKey);
        }
    }


    public static List<Map<String, Object>> getResponseList(JSONObject definitions, String key) {
        List<Map<String, Object>> list = Lists.newArrayList();
        Map<String, Object> map = getResponseObject(definitions, key);
        if (map != null && map.size() > 0) {
            list.add(map);
        }
        return list;
    }

    public static Map<String, Object> getResponseObject(JSONObject definitions, String key) {

        Map<String, Object> result = Maps.newHashMap();
        JSONObject value = definitions.getJSONObject(key);
        if (value == null) {
            return null;
        }
        log("getResponseObject ====>" + key);
        log("getResponseObject ====>" + value.toJSONString());


        JSONObject properties = value.getJSONObject("properties");

        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            log("getResponseObject entry: " + entry.getKey() + " : " + entry.getValue());

            JSONObject object = (JSONObject) entry.getValue();
            String desc = object.getString("description");
            String type = object.getString("type");

            String refValue = object.getString("@*@ref");
            if (refValue != null && refValue.length() > 0) {
                Object o = getResponse(definitions, refValue);
                result.put(entry.getKey(), o);
            } else {

                StringBuilder vv = new StringBuilder();
                vv.append("type:").append(type);
                if (desc != null && desc.trim().length() > 0) {
                    vv.append(" desc:" + desc);
                }

                result.put(entry.getKey(), vv.toString());
            }

        }
        return result;
    }


    public static void log(String str) {
        if (DEBUG_FLAG) {
            System.out.println(str);
        }
    }

    public static String convertName(char split, String str) {
        StringBuilder sb = new StringBuilder();
        boolean upFlag = false;
        for (int i = 0; i < str.length(); i++) {

            char c = str.charAt(i);
            if (c == split) {
                upFlag = true;
                continue;
            }


            if (upFlag && Character.isLowerCase(c)) {
                c = (char) (c - 32);
                upFlag = false;
            }

            sb.append(c);
        }

        return sb.toString();
    }

}
