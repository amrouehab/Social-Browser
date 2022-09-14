package com.UltimaradSolutions.SocialBrowser;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;


public class BackGroundTab extends WebFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(FirstCreate) {
            setToolBar();
            browser=backGroundTabsHandler.Page;
            registerForContextMenu(browser.web);
            FirstCreate=false;

        }

        return backGroundTabsHandler.Page.backTabLayout;
    }




    public void setToolBar() {
       if(((WebViewActivity)getActivity()).BackgroundTab.toolbar.findViewWithTag("s")!=null) ((WebViewActivity)getActivity()).BackgroundTab.toolbar.removeView(((WebViewActivity)getActivity()).BackgroundTab.toolbar.findViewWithTag("s"));
        ((WebViewActivity)getActivity()).BackgroundTab.toolbar.addView(backGroundTabsHandler.Page.linearLayout);
       backGroundTabsHandler.Page.backTabLayout.requestFocus();



    }




}
