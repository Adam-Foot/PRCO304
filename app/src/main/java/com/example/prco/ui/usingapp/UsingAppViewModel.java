package com.example.prco.ui.usingapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UsingAppViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UsingAppViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Using this App");
    }

    public LiveData<String> getText() {
        return mText;
    }
}