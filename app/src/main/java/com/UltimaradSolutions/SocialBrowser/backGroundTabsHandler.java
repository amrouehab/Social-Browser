package com.UltimaradSolutions.SocialBrowser;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

 class backGroundTabsHandler  {


     RecyclerView slidingLayout;
    private WebViewActivity mainActivity;
    public BackgroundTabListView backgroundTabListView;
    private  FragmentManager fm;
    public BackGroundTab backTab;
     Toolbar toolbar;
    private boolean FirstAdd=true;
    public static Browser Page;
    TextView alert;





    public backGroundTabsHandler(FragmentManager Fm, Activity main) {
        fm=Fm;
        mainActivity= (WebViewActivity) main;
        this.slidingLayout= (RecyclerView) mainActivity.findViewById(R.id.drawerListt);
        backgroundTabListView=new BackgroundTabListView(slidingLayout,mainActivity);
        backgroundTabListView.setup((DrawerLayout) mainActivity.findViewById(R.id.drawer_layout));
        toolbar= (Toolbar) mainActivity.findViewById(R.id.toolbar);
initiazeBackTabListLayoutButtons(backgroundTabListView.mDrawerLayout);
alert= (TextView) mainActivity.findViewById(R.id.backTabAlert);

    }



    private void initiazeBackTabListLayoutButtons(DrawerLayout backTabsListLayout) {
       ImageButton HomeClick = (ImageButton) backTabsListLayout.findViewById(R.id.homeClick);
        Button AddClick = (Button) backTabsListLayout.findViewById(R.id.add_backtab);
        ImageButton del=(ImageButton)backTabsListLayout.findViewById(R.id.delBut);
      //  HomeClick.setBackgroundColor(0x000000);
       // del.setBackgroundColor(0x000000);
        //AddClick.setBackgroundColor(0x000000);

        AddClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.BackgroundTab.createBackroundTab("http://www.google.com");
                backgroundTabListView.adapter.position=backGroundTabsHandler.BackGroundTabsList.size()-1;
           mainActivity.BackgroundTab.OpenBackgroundTab(backgroundTabListView.adapter.position,false);


            }
        });
        HomeClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundTabListView.adapter.position=99;
                mainActivity.BackgroundTab.onHomeClick();

            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundTabListView.adapter.position=99;
                mainActivity.BackgroundTab.deletAlltabs();

            }
        });
    }

    //hagrb asheel el fragment we a7oto tany
     void createBackroundTab(String url) {
        Browser BackTabPage=new Browser(backgroundTabListView,mainActivity);
            BackGroundTabsList.add(BackTabPage);
            backgroundTabListView.addTBackTabListItem(BackTabPage);
        BackTabPage.OpenLink(url);
        notifyBackTabsCountChanged();


    }

    void notifyBackTabsCountChanged() {
            TextView tv = mainActivity.navigationTabHelper.backTabsBut;
            tv.setText("  " + BackGroundTabsList.size() + "  ");
            tv.setTextColor(Color.GRAY);
            tv.setTypeface(null, Typeface.BOLD);
            tv.setTextSize(16);
            tv.setBackgroundResource(R.drawable.rect_shape);
        if(BackGroundTabsList.size()!=0)alert.setVisibility(View.INVISIBLE);
        else alert.setVisibility(View.VISIBLE);




    }

    private void createFragment() {
        removeFragment(false);
        backTab = new BackGroundTab();

    }

    private void removeFragment(boolean EnableAnimation) {
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        if(fm.findFragmentByTag("t")==backTab&&backTab!=null){
            if(EnableAnimation)ft.setCustomAnimations(R.anim.enter_r,R.anim.exit_r,R.anim.enter,R.anim.exit);
            ft.detach(backTab);
            ft.remove(backTab);
            ft.commit();
        }

    }


    public void OpenBackgroundTab(int pos,boolean EnableAnimation) {
        Page=  BackGroundTabsList.get(pos);
        createFragment();
        mainActivity.tabLayout.setVisibility(View.GONE);
        mainActivity.viewPager.setVisibility(View.INVISIBLE);
        toolbar.setVisibility(View.VISIBLE);
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        if(EnableAnimation)ft.setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.enter_r,R.anim.exit_r);
        ft.add(R.id.container, backTab,"t");
        if(FirstAdd){
            FirstAdd=false;
        }
        ft.commit();

        mainActivity.navigationTabHelper.whatMenuToShow();
       backgroundTabListView.mDrawerLayout.closeDrawer(Gravity.LEFT);


    }


public void onHomeClick(){
showHomeFragment(false);

}
    public void showHomeFragment(boolean EnableAnimation) {
        removeFragment(EnableAnimation);
        toolbar.removeView(toolbar.findViewWithTag("s"));
            FirstAdd=true;
        mainActivity.viewPager.setVisibility(View.VISIBLE);
        mainActivity.abbarLayout.setExpanded(false);

        backgroundTabListView.adapter.position=99;//reset the position to  know that the opened layout is home
        backTab=null;
        mainActivity.navigationTabHelper.whatMenuToShow();
        backgroundTabListView.mDrawerLayout.closeDrawer(Gravity.LEFT);


        }


    public void delTab(int pos){

        if(BackGroundTabsList.size()==0){
            backgroundTabListView.adapter.position=99;
            onHomeClick();
        }
        else {
if(backgroundTabListView.adapter.position==pos) removeFragment(false);
            backgroundTabListView.adapter.position = pos != 0 ? pos -1 : pos;

}
notifyBackTabsCountChanged();
        }


    public static  ArrayList<Browser> BackGroundTabsList=new ArrayList<>();

    public void deletAlltabs() {
        int i = BackGroundTabsList.size();
        if (i != 0) {
            while (i != 0) {
                BackGroundTabsList.remove(i - 1);
                i--;
            }
            backgroundTabListView.adapter.notifyDataSetChanged();
            notifyBackTabsCountChanged();
            onHomeClick();
        }else {

            Toast.makeText(mainActivity,"no opened backTabs",Toast.LENGTH_SHORT).show();
        }
    }

}
