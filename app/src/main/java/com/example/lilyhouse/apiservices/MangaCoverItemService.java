package com.example.lilyhouse.apiservices;

import com.example.lilyhouse.models.MangaCoverItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MangaCoverItemService {

    @GET("{urlString}")
    Call<List<MangaCoverItem>> getCoverItems(@Path("urlString") String urlString);

}
