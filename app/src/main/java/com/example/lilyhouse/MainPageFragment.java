package com.example.lilyhouse;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lilyhouse.adapters.CoverListAdapter;
import com.example.lilyhouse.apiservices.MangaCoverItemService;
import com.example.lilyhouse.models.MangaCoverItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainPageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "======MainPageFragment";
    private final String BASIC_JSON_URL = "https://m.dmzj.com/classify/";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // a: 题材(百合-12) b: 读者群(少年-0) c: 进度(连载-1) d: 地区(日本-1) e: 排序(人气-0) f: 页码-0
    int subjectCode = 12, groupCode = 0, statusCode = 0, regionCode = 1, sortCode = 0, pageCode = 0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView rvCoverItems;
    private CoverListAdapter coverListAdapter;


    public MainPageFragment() {
        // Required empty public constructor
    }

    public static MainPageFragment newInstance(String param1, String param2) {
        MainPageFragment fragment = new MainPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Log.d(TAG, "onCreateView: ");
        View rootView = inflater.inflate(R.layout.fragment_main_page, container, false);

        rvCoverItems = rootView.findViewById(R.id.main_recyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCoverItems.setLayoutManager(layoutManager);
        coverListAdapter = new CoverListAdapter(getActivity());
        rvCoverItems.setAdapter(coverListAdapter);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASIC_JSON_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MangaCoverItemService itemService = retrofit.create(MangaCoverItemService.class);

        String urlString = "12-0-0-1-0-0.json";
        Call<List<MangaCoverItem>> call = itemService.getCoverItems(urlString);

        call.enqueue(new Callback<List<MangaCoverItem>>() {
            @Override
            public void onResponse(Call<List<MangaCoverItem>> call, Response<List<MangaCoverItem>> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.code());
                    Snackbar.make(rvCoverItems, "Request Page Error!", Snackbar.LENGTH_SHORT).show();
                } else {
                    coverListAdapter.addItems(response.body());
                    Log.d(TAG, "onResponse: " + response.body().size());
                }
            }

            @Override
            public void onFailure(Call<List<MangaCoverItem>> call, Throwable t) {
                Snackbar.make(rvCoverItems, "Network Error!", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    // a: 题材(百合-12) b: 读者群(少年-0) c: 进度(连载-1) d: 地区(日本-1) e: 排序(人气-0) f: 页码-0
    private String getUrlString(int subjectCode, int groupCode, int statusCode, int regionCode,
                                int sortCode, int pageCode) {
        String urlString = subjectCode + "-" + groupCode + "-" + statusCode + "-" + regionCode +
                "-" + sortCode + "-" + pageCode + ".json";
        Log.d(TAG, "getUrlString: " + urlString);
        return urlString;
    }

}
