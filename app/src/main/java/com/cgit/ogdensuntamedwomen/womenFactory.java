package com.cgit.ogdensuntamedwomen;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cgit.ogdensuntamedwomen.model.Places;

public class womenFactory implements ViewModelProvider.Factory {
    Context context;
    Places places;

    public womenFactory(Context context, Places places) {
        this.context = context;
        this.places = places;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new womenViewModel(context,places);
    }
}
