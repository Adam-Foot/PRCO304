package com.example.prco.ui.sites;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SitesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SitesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Sites A-Z");
    }

    public LiveData<String> getText() {
        return mText;
    }
}