package com.wzyy.springbootinit.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONArray;

import java.io.InputStream;

/**
 * @description
 * @author: Wangzhaoyi
 * @create: 2023-12-23 10:57
 **/
public class CommonUtil {



    public String getProfileValue(String key){
        JSONObject jsonObject = getProfile();
        return jsonObject.getString(key);
    }


    public JSONObject getProfile(){
        String path="/Profile/profile.json";
        InputStream config = getClass().getResourceAsStream(path);
        JSONObject json = new JSONObject();
        if (config == null) {
            throw new RuntimeException("读取文件失败");
        } else {
            json = JSON.parseObject(config, JSONObject.class);
            System.out.println(json);
        }
        return json;
    }

}
