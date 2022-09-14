package com.UltimaradSolutions.SocialBrowser;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

class NavigationTabHelper {
    private WebViewActivity webViewActivity;
    Browser currentBrowser;
    private LinearLayout navTab;
    private ImageButton backBut;
  private ImageButton forwardBut;
    private ImageButton refreshkBut;
    TextView backTabsBut;
    public PopupMenu menu;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
     ImageButton NavmenuBut;


    NavigationTabHelper(WebViewActivity main, ViewPagerAdapter adapter, ViewPager viewPager) {
         this.webViewActivity=main;
         this.adapter=adapter;
         this.viewPager=viewPager;
         init();
         onMenuButCLick();
        backTabsBut= navTab.findViewById(R.id.BackTabButt);

    }

    private void init() {
        navTab=(LinearLayout) webViewActivity.findViewById(R.id.NavTab);
        backBut= navTab.findViewById(R.id.backkbutt);
        forwardBut= navTab.findViewById(R.id.forwardBut);
        refreshkBut= navTab.findViewById(R.id.refreshBut);
        NavmenuBut=navTab.findViewById(R.id.NavmenuBut);
    }

    private void onMenuButCLick() {
        ImageButton menuButton =navTab.findViewById(R.id.menuBut);
        menu = new PopupMenu(webViewActivity, menuButton);
        OnMenuItemClick();
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentBrowser!=null) {
                    if (currentBrowser.menu == null) currentBrowser.menu = menu;

                }
                else {
                    menu.getMenu().clear();
                    menu.inflate(R.menu.menu_main_dark);
                }
                    menu.show();
            }

        });
    }

    void whatMenuToShow() {
        if (theTabisBackground()) {
            menu.getMenu().clear();
            menu.inflate(R.menu.back_tab_menu);
            backGroundTabsHandler.Page.menu = menu;
            currentBrowser=backGroundTabsHandler.Page;
        }
        else {
            menu.getMenu().clear();
           menu.inflate(R.menu.menu_main);
            if(adapter.mFragmentList.size()!=0) {//to know if there is no tab opened
                adapter.mFragmentList.get(webViewActivity.GetTabPosition).browser.menu = menu;
                currentBrowser = adapter.mFragmentList.get(webViewActivity.GetTabPosition).browser;
                webViewActivity.BackgroundTab.notifyBackTabsCountChanged();
                notifyMenuItemChanged(0,true);
            }


        }


    }

    private void OnMenuItemClick(){
        setOnButtonSClick(R.id.backkbutt);
        setOnButtonSClick(R.id.refreshBut);
        setOnButtonSClick(R.id.forwardBut);
        setOnButtonSClick(R.id.NavmenuBut);
        setOnButtonSClick(R.id.BackTabButt);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.action_about: {
                        AboutScreenFragment aboutScreenFragment=new AboutScreenFragment();
                        showAnOptionLy(aboutScreenFragment);
                        return true;
                    }
                    case R.id.Action_AddTab: {
                        TabsEditorFragment tabsEditorFragment =new TabsEditorFragment();
                        Bundle b=new Bundle();
                        b.putInt("id",R.id.Action_AddTab);
                        tabsEditorFragment.setArguments(b);
                        showAnOptionLy(tabsEditorFragment);
                        return true;

                    }
                    case R.id.Action_RemoveTab: {
                        int x = webViewActivity.GetTabPosition;
                        adapter.mFragmentList.get(x).browser.finish();
                        if (adapter.mFragmentList.size() == 1) {
                            TabsEditorFragment tabsEditorFragment =new TabsEditorFragment();
                            Bundle b=new Bundle();
                            b.putInt("id",R.id.Action_AddTab);
                            tabsEditorFragment.setArguments(b);
                            showAnOptionLy(tabsEditorFragment);
                            currentBrowser = null;
                        }
                        adapter.removePage(x);
                        if(x!=0&&x!=adapter.mFragmentList.size()) webViewActivity.GetTabPosition--;
                         if(x==0&&!(adapter.mFragmentList.size()<=1))webViewActivity.GetTabPosition++;
                        viewPager.setCurrentItem(webViewActivity.GetTabPosition,true);
                       if(adapter.mFragmentTitleList.size()!=0) notifyBrowserChanged(adapter.mFragmentList.get(webViewActivity.GetTabPosition).browser);
                        webViewActivity.editTabLayoutStyle();

                        return true;
                    }
                    case R.id.action_Theme: {
                        //StartTabCHooserScreen(R.id.action_Theme);
                        webViewActivity.startThemeChangeLayout();
                        return true;

                    }

                    case R.id.action_fullscreen: {
                        //hideToolbar();
                      //  webViewActivity.fullScreen(item);

                        return true;
                    }
                    case R.id.Bookmarks: {
                        BrowsingDataFragment browsingDataFragment=new BrowsingDataFragment();
                        Bundle b=new Bundle();
                        b.putInt("fragment",1);
                        browsingDataFragment.setArguments(b);
                        showAnOptionLy(browsingDataFragment);
                        return true;
                    }
                    case R.id.save_tabs: {
                        webViewActivity.Alert();
                        return true;
                    }
                    case R.id.Split_pages: {
                        TabsEditorFragment tabsEditorFragment =new TabsEditorFragment();
                        Bundle b=new Bundle();
                        b.putInt("id",R.id.Split_pages);
                        tabsEditorFragment.setArguments(b);
                        showAnOptionLy(tabsEditorFragment);

                        return true;
                    }
                    case R.id.history: {
                        BrowsingDataFragment browsingDataFragment=new BrowsingDataFragment();
                        Bundle b=new Bundle();
                        b.putInt("fragment",0);
                        browsingDataFragment.setArguments(b);
                        showAnOptionLy(browsingDataFragment);

                        return true;
                    }
                }

                return true;
            }
        });


}

     void showAnOptionLy(SettingsFragment settingsFragment) {

        android.support.v4.app.FragmentTransaction ft = webViewActivity.getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.enter_r,R.anim.exit_r,R.anim.enter,R.anim.exit);
        ft.add(R.id.histContainer, settingsFragment);
        ft.commit();

    }


    private void setOnButtonSClick(final int ButID) {
        View Button=navTab.findViewById(ButID);
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.refreshBut:{
                        if(currentBrowser!=null) {
                            if (currentBrowser.web.getProgress() == 100) {
                                currentBrowser.web.reload();

                            } else {

                                currentBrowser.web.stopLoading();
                            }
                            break;
                        }
                    }
                    case R.id.forwardBut:{
                        if(currentBrowser!=null&&currentBrowser.web.canGoForward()) currentBrowser.web.goForward();
                        break;
                    }
                    case R.id.BackTabButt:{
webViewActivity.BackgroundTab.backgroundTabListView.mDrawerLayout.openDrawer(Gravity.START);
                        webViewActivity.showBackTabsAlert();
                        break;
                    }
                    case R.id.backkbutt:{
                        if(currentBrowser!=null&&currentBrowser.web.canGoBack())currentBrowser.web.goBack();

                        break;
                    }
                    case R.id.NavmenuBut:{
                        showTabsly();


                        break;
                    }
                }
            }
        });
    }
void setIconNavBar(Bitmap icon){
    if(icon!=null) NavmenuBut.setImageBitmap(icon);
    else NavmenuBut.setImageResource(R.drawable.ic_launcher);
}
    private void showTabsly() {
        if (webViewActivity.viewPager.isShown()) {
            if (webViewActivity.tabLayout.isShown())
                webViewActivity.tabLayout.setVisibility(View.GONE);
            else webViewActivity.tabLayout.setVisibility(View.VISIBLE);
        }
    }
    void notifyMenuItemChanged(int notifyItemID,boolean notifyAll) {
        if (notifyAll&&currentBrowser!=null) {
            if(currentBrowser.menu==null)currentBrowser.menu=menu;
            editMenuItemWorkingMode(R.id.backkbutt);
            editMenuItemWorkingMode(R.id.forwardBut);
            editMenuItemWorkingMode(R.id.refreshBut);
        }
        else editMenuItemWorkingMode(notifyItemID);
    }

     private void editMenuItemWorkingMode(int ItemId) {
         if(currentBrowser!=null){
     //   SpannableString s=null;
     //   if(menu.getMenu().findItem(ItemId)!=null){ s = new SpannableString(menu.getMenu().findItem(ItemId).getTitle().toString());}
        switch (ItemId){
            case R.id.backkbutt :{
                if(currentBrowser.web.canGoBack()) editNavBarItem(Color.BLACK,backBut);
                else  editNavBarItem(Color.GRAY,backBut);
                break;
            }
            case R.id.forwardBut :{
                if(currentBrowser.web.canGoForward()) editNavBarItem(Color.BLACK,forwardBut);
                else  editNavBarItem(Color.GRAY,forwardBut);
                break;
            }

            case R.id.refreshBut:{
                if(currentBrowser.web.getProgress()==100) refreshkBut.setImageResource(R.drawable.ic_action_refresh);
                else refreshkBut.setImageResource(android.R.drawable.ic_delete);
                break;
            }





        }
    }}

    private void editNavBarItem(int color,ImageButton but) {
        Drawable mDrawable = but.getDrawable();
        mDrawable.setColorFilter(new
                PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
        but.setImageDrawable(mDrawable);
    }

    private void editMenuItem(SpannableString s, boolean b, int itemID, int color) {
        MenuItem item=menu.getMenu().findItem(itemID);
        s.setSpan(new ForegroundColorSpan(color), 0, s.length(), 0);
        item.setEnabled(b);
        item.setTitle(s);
    }
    private boolean theTabisBackground() {
        return webViewActivity.BackgroundTab.backTab!=null;
    }

    public void notifyBrowserChanged(Browser web) {
        if (currentBrowser != null) {
            currentBrowser = web;
currentBrowser.web.requestFocus();
            currentBrowser.web.invalidate();
            currentBrowser.web.bringToFront();

            if ( currentBrowser.menu == null) currentBrowser.menu = menu;

            setIconNavBar(currentBrowser != null ? currentBrowser.ICon : null);
            notifyMenuItemChanged(0, true);
        }
    }
}

