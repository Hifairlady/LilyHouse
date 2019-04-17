package com.example.lilyhouse.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.lilyhouse.models.MangaCoverItem;
import com.example.lilyhouse.repositories.CoverItemRepo;

import java.util.List;

public class CoverItemViewModel extends AndroidViewModel {

    private CoverItemRepo mCoverItemRepo;
    private LiveData<List<MangaCoverItem>> coverItems;

    public CoverItemViewModel(@NonNull Application application) {
        super(application);
        mCoverItemRepo = new CoverItemRepo(application);
        coverItems = mCoverItemRepo.getCoverItems();
    }

    public LiveData<List<MangaCoverItem>> getCoverItems() {
        return coverItems;
    }

    public void insertItems(MangaCoverItem... items) {
        mCoverItemRepo.insertItems(items);
    }
}
