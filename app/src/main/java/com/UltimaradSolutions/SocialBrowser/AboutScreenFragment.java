package com.UltimaradSolutions.SocialBrowser;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class AboutScreenFragment extends SettingsFragment{


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View Layout=inflater.inflate(R.layout.aboutscreen,null);
        backOnclick(Layout);
        return Layout;
    }
}
