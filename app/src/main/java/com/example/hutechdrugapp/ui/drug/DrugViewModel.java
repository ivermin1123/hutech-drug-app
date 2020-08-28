package com.example.hutechdrugapp.ui.drug;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DrugViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DrugViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is drug fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}