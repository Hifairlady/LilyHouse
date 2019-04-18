package com.example.lilyhouse.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

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

    public void deleteAllItems() {
        mCoverItemRepo.deleteAllItems();
    }
}
