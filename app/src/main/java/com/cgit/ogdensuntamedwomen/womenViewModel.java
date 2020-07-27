package com.cgit.ogdensuntamedwomen;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cgit.ogdensuntamedwomen.model.CSVFile;
import com.cgit.ogdensuntamedwomen.model.PlaceContent;
import com.cgit.ogdensuntamedwomen.model.Places;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class womenViewModel extends ViewModel {

    MutableLiveData<ArrayList<PlaceContent>> mutableLiveData;

    public womenViewModel(Context context, Places places) {
        mutableLiveData = new MutableLiveData<>();
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open("LocationContent.csv");
            CSVFile csvFile = new CSVFile(inputStream);
            ArrayList<PlaceContent> myList = csvFile.readPlacesContent(places.getId());
            mutableLiveData.setValue(myList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MutableLiveData<ArrayList<PlaceContent>> getMutableLiveData() {
        return mutableLiveData;
    }
}
