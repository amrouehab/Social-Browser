package com.UltimaradSolutions.SocialBrowser;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;


//el nzam da mo2akt b3d keda h3ml class da h3dlo we hinhirt meno el history we el bookmarks we hyb2a gowaa el helper
public class SettingsFragment extends Fragment {



     void backOnclick(View v) {
        Button back=v.findViewById(R.id.backHistory);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeThisFragment();

            }
        });
    }



    void removeThisFragment(){
        FragmentManager fm= getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft =fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.enter_r,R.anim.exit_r);
        ft.detach(SettingsFragment.this);
        ft.remove(SettingsFragment.this);
        ft.commit();


    }




}
