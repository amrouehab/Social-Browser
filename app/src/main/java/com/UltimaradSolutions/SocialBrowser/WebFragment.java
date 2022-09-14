package com.UltimaradSolutions.SocialBrowser;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;

public class WebFragment extends Fragment {
    public Browser browser;
    public boolean FirstCreate=true;
    public String Url;
    public String FragmentType="B";
    public LinearLayout toolbar;
    public  View WebLayout;
    public String urll;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(FirstCreate) {
            if(((WebViewActivity)getActivity()).adapter.mFragmentList.size()==0)((WebViewActivity)getActivity()).navigationTabHelper.currentBrowser=browser;
            urll = this.getArguments().getString("url");
            FragmentType="H";
            registerForContextMenu(browser.web);
            browser.OpenLink(urll);
            FirstCreate=false;
        }
        return WebLayout;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        try {
            WebView Wv= (WebView) v;
            final WebView.HitTestResult hr = Wv.getHitTestResult();
            if (hr.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                    hr.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                super.onCreateContextMenu(menu, v, menuInfo);
                menu.setHeaderTitle(hr.getExtra());
                menu.add(0, v.getId(), 0, "Open");
                menu.add(0, v.getId(), 0, "Open in new background Tab");
                menu.add(0, v.getId(), 0, "Copy link");
                Url=hr.getExtra();

            } else if (hr.getType() == WebView.HitTestResult.ANCHOR_TYPE ||
                    hr.getType() == WebView.HitTestResult.SRC_ANCHOR_TYPE) {
                super.onCreateContextMenu(menu, v, menuInfo);
                menu.setHeaderTitle(hr.getExtra());
                menu.add(0, v.getId(), 0, "Open");
                menu.add(0, v.getId(), 0, "Open in new background Tab");
                menu.add(0, v.getId(), 0, "Copy link");
                Url=hr.getExtra();

            }
        } catch (Exception e) {


            e.getMessage();
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int pos=((WebViewActivity) getActivity()).GetTabPosition;
        WebFragment wf=this;
       if(browser.Activity.adapter.mFragmentList.size()!=0) wf=(WebFragment) browser.Activity.adapter.getItem(pos);
        if ((wf.browser==browser&&Url!=null&&FragmentType=="H"&&!IsBackTab())){

itemOnClick(item);
            return true;
        }
        else {
            if(FragmentType=="B"&&backGroundTabsHandler.Page.web!=null&&Url!=null){


                itemOnClick(item);
                return true;

            }



        }
        return false;

}

    private boolean IsBackTab() {
        BackGroundTab backGroundTab=((WebViewActivity)getActivity()).BackgroundTab.backTab;
        return backGroundTab != null && backGroundTab.isAdded();

    }

    private void itemOnClick(MenuItem item) {
        if (item.getTitle() == "Open") {
            browser.web.loadUrl(Url);

        } else if (item.getTitle() == "Open in new background Tab") {

            ((WebViewActivity) getActivity()).BackgroundTab.createBackroundTab(Url);


        } else if (item.getTitle() == "Copy link") {
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(WebViewActivity.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", Url);
            clipboard.setPrimaryClip(clip);


        }
    }


}

