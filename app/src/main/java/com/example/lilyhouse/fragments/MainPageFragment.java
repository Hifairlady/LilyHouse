package com.example.lilyhouse.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lilyhouse.R;
import com.example.lilyhouse.adapters.CoverListAdapter;
import com.example.lilyhouse.apiservices.MangaCoverItemService;
import com.example.lilyhouse.clients.MangaCoverItemClient;
import com.example.lilyhouse.models.MangaCoverItem;
import com.example.lilyhouse.viewmodels.CoverItemViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPageFragment extends Fragment {
    private static final String TAG = "======MainPageFragment";
    private final String BASIC_JSON_URL = "https://m.dmzj.com/classify/";

    private CoverItemViewModel mCoverViewModel;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // a: 题材(百合-12) b: 读者群(少年-0) c: 进度(连载-1) d: 地区(日本-1) e: 排序(人气-0) f: 页码-0
    int subjectCode = 12, groupCode = 0, statusCode = 0, regionCode = 1, sortCode = 0, pageCode = 0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private boolean isLoadingNext = false;

    private SwipeRefreshLayout srlLayout;
    private RecyclerView rvCoverItems;
    private CoverListAdapter coverListAdapter;

    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            pageCode = 0;
            coverListAdapter = new CoverListAdapter(getActivity());
            rvCoverItems.setAdapter(coverListAdapter);
            rvCoverItems.invalidate();
            mCoverViewModel.deleteAllItems();
            loadCoverItems(12, 0, 0, 1, 0, 0);
        }
    };


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
        View rootView = inflater.inflate(R.layout.fragment_main_page, container, false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mCoverViewModel = ViewModelProviders.of(getActivity()).get(CoverItemViewModel.class);
        mCoverViewModel.getCoverItems().observe(getActivity(), new Observer<List<MangaCoverItem>>() {
            @Override
            public void onChanged(@Nullable List<MangaCoverItem> mangaCoverItems) {
                coverListAdapter.setItems(mangaCoverItems);
                coverListAdapter.addFooter();
                isLoadingNext = false;
            }
        });

        if (mCoverViewModel.getCoverItems().getValue() == null || mCoverViewModel.getCoverItems().getValue().size() == 0) {
            loadCoverItems(12, 0, 0, 1, 0, 0);
        }

    }

    private void initView(View rootView) {
        srlLayout = rootView.findViewById(R.id.main_refresh_layout);
        srlLayout.setColorSchemeColors(0xFFFFDBCF);
        srlLayout.setOnRefreshListener(mRefreshListener);
        rvCoverItems = rootView.findViewById(R.id.main_recyclerview);
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        rvCoverItems.setLayoutManager(layoutManager);
        coverListAdapter = new CoverListAdapter(getActivity());
        coverListAdapter.setHasStableIds(true);
        rvCoverItems.setAdapter(coverListAdapter);
        rvCoverItems.setItemAnimator(null);

        rvCoverItems.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager layoutManager1 = (GridLayoutManager) rvCoverItems.getLayoutManager();
                int lastPos = layoutManager1.findLastCompletelyVisibleItemPosition();
                if (lastPos == coverListAdapter.getItemCount() - 1 && !isLoadingNext) {
                    isLoadingNext = true;
                    loadNextPageItems();
                }
            }
        });
    }

    private void loadCoverItems(int subjectCode, int groupCode, int statusCode, int regionCode,
                                int sortCode, int pageCode) {
        MangaCoverItemService itemService =
                MangaCoverItemClient.getRetrofit().create(MangaCoverItemService.class);
        Call<List<MangaCoverItem>> call = itemService.getCoverItems(subjectCode, groupCode, statusCode,
                regionCode, sortCode, pageCode);
        call.enqueue(new Callback<List<MangaCoverItem>>() {
            @Override
            public void onResponse(Call<List<MangaCoverItem>> call, Response<List<MangaCoverItem>> response) {
                if (!response.isSuccessful()) {
                    Snackbar.make(rvCoverItems, "Request Page Error!", Snackbar.LENGTH_SHORT).show();
                } else {
                    MangaCoverItem[] items = new MangaCoverItem[response.body().size()];
                    response.body().toArray(items);
                    mCoverViewModel.insertItems(items);
                }
                srlLayout.setRefreshing(false);
                isLoadingNext = false;
            }

            @Override
            public void onFailure(Call<List<MangaCoverItem>> call, Throwable t) {
                Snackbar.make(rvCoverItems, "Network Error!", Snackbar.LENGTH_SHORT).show();
                coverListAdapter.addFooter();
                srlLayout.setRefreshing(false);
                isLoadingNext = false;
            }
        });
    }

    private void loadNextPageItems() {
        pageCode = pageCode + 1;
        coverListAdapter.removeFooter();
        loadCoverItems(subjectCode, groupCode, statusCode, regionCode, sortCode, pageCode);
    }

}
