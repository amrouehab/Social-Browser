package com.UltimaradSolutions.SocialBrowser;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class introView extends LinearLayout {
    ImageView icon;
    CheckBox check;
    String Url;

    public introView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

     void initlaizeView() {
        icon=this.findViewById(R.id.imageView4);
        check=this.findViewById(R.id.checkBox2);


    }

    public introView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public introView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);


    }

    public introView(Context context) {
        super(context);


    }


    public void setIconAndTitle(String tabName, String url, Drawable myDrawable, int pos) {
        check.setText(tabName);
       icon.setImageDrawable(myDrawable);
        icon.setTag(pos+1);
        check.setTag(pos);
       Url=url;
    }


}
