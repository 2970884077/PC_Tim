package com.ticktockx;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ReqImpl {


    public String getReqSign(Map<String,String> params,String appkey){
        List<String> strs = new LinkedList<>();
        try {
            params.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEachOrdered(e -> {
                        if (!e.getValue().equals("")) {
                            try {
                                strs.add( e.getKey() + "=" + URLEncoder.encode(e.getValue(), "utf-8") + "&");
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
            StringBuilder builder = new StringBuilder();
            for(String str:strs){
                builder.append(str);
            }
            String get = builder.toString();
            MessageDigest digest = MessageDigest.getInstance("MD5");
            get += "app_key=" + appkey;

            return new String((digest.digest(get.getBytes())), "utf-8").toUpperCase();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
