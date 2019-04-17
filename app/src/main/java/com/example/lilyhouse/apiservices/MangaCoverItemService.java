package com.example.lilyhouse.apiservices;

import com.example.lilyhouse.models.MangaCoverItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MangaCoverItemService {

    @GET("{urlString}")
    Call<List<MangaCoverItem>> getCoverItems(@Path("urlString") String urlString);

    //     a: 题材(百合-12) b: 读者群(少年-0) c: 进度(连载-1) d: 地区(日本-1) e: 排序(人气-0) f: 页码-0
//    int subjectCode = 12, groupCode = 0, statusCode = 0, regionCode = 1, sortCode = 0, pageCode = 0;
    @GET("{subjectCode}-{groupCode}-{statusCode}-{regionCode}-{sortCode}-{pageCode}.json")
    Call<List<MangaCoverItem>> getCoverItems(@Path("subjectCode") int subjectCode, @Path("groupCode")
            int groupCode, @Path("statusCode") int statusCode, @Path("regionCode") int regionCode,
                                             @Path("sortCode") int sortCode, @Path("pageCode") int pageCode);

}
