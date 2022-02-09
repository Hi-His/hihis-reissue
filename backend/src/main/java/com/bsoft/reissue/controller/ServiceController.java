package com.bsoft.reissue.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bsoft.reissue.dto.BasicResponse;
import com.bsoft.reissue.dto.Pagination;
import com.bsoft.reissue.entity.AnchorPointLog;
import com.bsoft.reissue.entity.AnchorPointReissue;
import com.bsoft.reissue.service.AnchorPointLogService;
import com.bsoft.reissue.service.AnchorPointReissueService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author: 何胜豪
 * @Title: TODO
 * @Package: com.bsoft.rooler.controller
 * @Description:
 * @date : 2021/11/27 23:15
 */
@RestController
@RequestMapping("/logs")
public class ServiceController {

    @Autowired
    AnchorPointLogService  anchorPointLogService;


    @Autowired
    AnchorPointReissueService anchorPointReissueService;


    @PostMapping("")
    public Page<AnchorPointLog> fetchLogs(@RequestBody Pagination page){
        Page<AnchorPointLog> logPage = new Page<>();
         logPage.setCurrent(page.getCurrent());
         logPage.setSize(Long.parseLong(page.getSize()));

        AnchorPointLog reissue = page.getAnchorPointLog();


        QueryWrapper<AnchorPointLog> wrapper = new QueryWrapper<AnchorPointLog>()
                .orderByDesc("insert_time");
        if(reissue != null){
            String cd = reissue.getCd();
            String beanName = reissue.getBeanName();
            String params = reissue.getParams();
            String method = reissue.getMethod();
            String result = reissue.getResult();
            if(cd != null){
                wrapper.eq("cd",cd);
            }
            if(StringUtils.hasText(beanName)){
                wrapper.like("bean_name",beanName);
            }
            if(StringUtils.hasText(params)){
                wrapper.like("params",params);
            }
            if(StringUtils.hasText(method)){
                wrapper.like("method",method);
            }
            if(StringUtils.hasText(result)){
                wrapper.like("result",result);
            }
        }

        Page<AnchorPointLog> logPage1 = anchorPointLogService.selectLogsPage(logPage, wrapper);
        System.out.println(logPage1);
        return logPage1;
    }


    @PostMapping("/sendReissue")
    public BasicResponse sendReissue(@RequestBody AnchorPointReissue reissue){
        String params = reissue.getParams();
        try {
            OkHttpClient client = new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS).build();
            Request request = new Request.Builder().url("http://172.27.0.7/hiHis/*.jsonRequest")
                    .addHeader("X-Service-Id",reissue.getBeanName())
                    .addHeader("X-Service-Method",reissue.getMethod())
                    .addHeader("Cookie",reissue.getTk())
                    .post(okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"),params)).build();
            System.out.println(params);
            Call call = client.newCall(request);
            try {
                Response response = call.execute();
                String string = response.body().string();
                System.out.println(string);

                if(200 == response.code() ){
                    return new BasicResponse(true,string);
                }else{
                    return new BasicResponse(false,string);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return new BasicResponse(false,"");
            }finally {

            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BasicResponse(false,"");
        }

    }


    @PostMapping("/sendPoint")
    public BasicResponse sendPoint(@RequestBody AnchorPointLog log){
        String params = log.getParams();
        try {
            OkHttpClient client = new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS).build();
            Request request = new Request.Builder().url("http://172.27.0.7/hiHis/*.jsonRequest")
                    .addHeader("X-Service-Id",log.getBeanName())
                    .addHeader("X-Service-Method",log.getMethod())
                    .addHeader("Cookie",log.getTk())
                    .post(okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"),params)).build();
            System.out.println(params);
            Call call = client.newCall(request);
            try {
                Response response = call.execute();
                String string = response.body().string();
                System.out.println(string);
                if(200 == response.code() ){
                    return new BasicResponse(true, string);
                }else{
                    return new BasicResponse(false,string);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return new BasicResponse(false,"");
            }finally {

            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BasicResponse(false,"");
        }


    }


    @PostMapping("/reissue")
    public Page<AnchorPointReissue> fetchReissueLogs(@RequestBody Pagination page){
        Page<AnchorPointReissue> logPage = new Page<>();
        logPage.setCurrent(page.getCurrent());
        logPage.setSize(Long.parseLong(page.getSize()));

        AnchorPointReissue reissue = page.getAnchorPointReissue();


        QueryWrapper<AnchorPointReissue> wrapper = new QueryWrapper<AnchorPointReissue>()
                .orderByDesc("insert_time");

        if(reissue != null){
            String cd = reissue.getCd();
            String beanName = reissue.getBeanName();
            String params = reissue.getParams();
            String method = reissue.getMethod();
            if(StringUtils.hasText(cd)){
                wrapper.eq("cd",cd);
            }
            if(StringUtils.hasText(beanName)){
                wrapper.like("bean_name",beanName);
            }
            if(StringUtils.hasText(params)){
                wrapper.like("params",params);
            }
            if(StringUtils.hasText(method)){
                wrapper.like("method",method);
            }
        }

        Page<AnchorPointReissue> logPage1 = anchorPointReissueService.selectLogsPage(logPage, wrapper);
        System.out.println(logPage1);
        return logPage1;
    }
}
