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

    private static final String REQUEST_CODES_PARAM = "REQUEST_CODES_PARAM";

    // a: 题材(百合-12) b: 读者群(少年-1) c: 进度(连载-1) d: 地区(日本-1) e: 排序(人气-0) f: 页码-0
    int subjectCode = 0, groupCode = 0, statusCode = 0, regionCode = 0, sortCode = 0, pageCode = 0;

    // TODO: Rename and change types of parameters
    private int[] requestCodes;

    private boolean isLoadingNext = false;

    private SwipeRefreshLayout srlLayout;
    private RecyclerView rvCoverItems;
    private CoverListAdapter coverListAdapter;

    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshAction();
        }
    };

    private CoverListAdapter.OnItemClickListener mOnItemClickListener = new CoverListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            // TODO: 2019/4/19 goto detail activity
        }
    };


    public MainPageFragment() {
        // Required empty public constructor
    }

    public static MainPageFragment newInstance(int[] codes) {
        MainPageFragment fragment = new MainPageFragment();
        Bundle args = new Bundle();
        args.putIntArray(REQUEST_CODES_PARAM, codes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            requestCodes = getArguments().getIntArray(REQUEST_CODES_PARAM);
            if (requestCodes != null) {
                subjectCode = requestCodes[0];
                groupCode = requestCodes[1];
                statusCode = requestCodes[2];
                regionCode = requestCodes[3];
                sortCode = requestCodes[4];
                pageCode = requestCodes[5];
            }
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

        //load first page
        if (mCoverViewModel.getCoverItems().getValue() == null || mCoverViewModel.getCoverItems().getValue().size() == 0) {
//            loadCoverItems(subjectCode, groupCode, statusCode, regionCode, sortCode, pageCode);
            refreshAction();
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

        coverListAdapter.setOnItemClickListener(mOnItemClickListener);
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

    public void switchSortOrder(int order) {
        sortCode = order;
        refreshAction();
    }

    private void refreshAction() {
        pageCode = 0;
        coverListAdapter = new CoverListAdapter(getActivity());
        coverListAdapter.setOnItemClickListener(mOnItemClickListener);
        rvCoverItems.setAdapter(coverListAdapter);
        rvCoverItems.invalidate();
        mCoverViewModel.deleteAllItems();
        loadCoverItems(subjectCode, groupCode, statusCode, regionCode, sortCode, pageCode);
    }

    public void filterApplyAction(int[] requestCodes) {
        subjectCode = requestCodes[0];
        groupCode = requestCodes[1];
        statusCode = requestCodes[2];
        regionCode = requestCodes[3];
        sortCode = requestCodes[4];
        pageCode = requestCodes[5];
        refreshAction();
    }

}
