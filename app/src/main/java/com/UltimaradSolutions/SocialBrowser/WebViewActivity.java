package com.UltimaradSolutions.SocialBrowser;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.ScaleInOutTransformer;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.UltimaradSolutions.SocialBrowser.DBAdapter.KEY_ROWID;


public class WebViewActivity extends AppCompatActivity  {

    public AppBarLayout abbarLayout;
    public ViewPager viewPager;
    public  TabLayout tabLayout;
    public  ViewPagerAdapter adapter;
    public ImageButton addBut;
    public Activity main;
    private boolean tabsAreCHosen = false;
    private FragmentManager fm;
     DBAdapter TabsDB;
    public FrameLayout BackTabLayout;
    private String color;
    public  View MainScreen;
    private TextView Vieww;
    private String statusbarColor;
    private View ColorLayout;
    private View decorView;
    public boolean fullscreenISOn = false;
    public NavigationTabHelper navigationTabHelper;
    public DBAdapter HisrotyDb,BookmarksDB,IntroPagesDB;
    public BrowsingDataHelper historyHelper;
    public FrameLayout ContainerLayout;
    public String currentDate;
    public boolean HistoryIsEnabled;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            DateFormat df = new SimpleDateFormat(("EEE, d MMM yyyy"));//("EEE, d MMM yyyy, HH:mm");
            currentDate = df.format(Calendar.getInstance().getTime());
            startWebLayout();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        //WhatLayoutToStart();
        // AdView mAdView = (AdView) findViewById(R.id.adView);
        // / AdRequest adRequest = new AdRequest.Builder().build();
        // mAdView.loadAd(adRequest);

    }

    private void WhatLayoutToStart() throws SQLException {
        if (!tabsAreCHosen) {
            ////  startMenuLayout();
            //  StartClick();
            tabsAreCHosen = true;
        } else startWebLayout();

    }


    private void startWebLayout() throws SQLException {
        openDb();
        MainScreen=getLayoutInflater().inflate(R.layout.activity_main,null);

        setContentView(MainScreen);
        decorView = getWindow().getDecorView();
        main = WebViewActivity.this;
        fm = getSupportFragmentManager();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                fullScreen();
            }
        });
        fullScreen();
        initializeingObjects();
        SetupToolBar();
        OnPageChange(viewPager);
        OnTabSelected(tabLayout);
        //fullScreen(null);
        getSavedTabs();
        navigationTabHelper.whatMenuToShow();

    }



    private void initializeingObjects() {
        //BackTabsListLayout =(RelativeLayout)findViewById(R.id.bt) ;
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),this);
        addBut = (ImageButton) findViewById(R.id.adddd);
        BackTabLayout = (FrameLayout) findViewById(R.id.container);
        ContainerLayout=(FrameLayout)findViewById(R.id.histContainer);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        HistoryIsEnabled = settings.getBoolean("history", false);



    }


    private void SetupToolBar() {
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true, new ScaleInOutTransformer()); //set the animation
        tabLayout = (TabLayout) findViewById(R.id.tably);
        tabLayout.setupWithViewPager(viewPager);
        abbarLayout= (AppBarLayout) findViewById(R.id.view);
        BackgroundTab = new backGroundTabsHandler(fm, main);
        navigationTabHelper=new NavigationTabHelper((WebViewActivity) main,adapter,viewPager);
        BackgroundTab.notifyBackTabsCountChanged();
        historyHelper=new BrowsingDataHelper(WebViewActivity.this,HisrotyDb);
       historyHelper.AddDateRowToDb(currentDate);

reguestPermission();

    }



    public void OnTabSelected(TabLayout tabLayout) {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                GetTabPosition = tab.getPosition();
                viewPager.setCurrentItem(tab.getPosition());


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void OnPageChange(ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                GetTabPosition = position;
                navigationTabHelper.notifyBrowserChanged(adapter.mFragmentList.get(GetTabPosition).browser);

                if(navigationTabHelper.currentBrowser.web.hasFocus())Log.d("WebViewFocus","HasFocus");
                else Log.d("WebViewFocus","NoFocus");
                }




            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    //this function called when the activity closes
    protected void onDestroy() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("iconState",true);
        editor.apply();
        TabsDB.close();//Close the db on Destroying the activity
        HisrotyDb.close();
        super.onDestroy();
        //      stopService();

    }


    //this method responsible of opening the db
    private void openDb() throws SQLException {
        HisrotyDb=new DBAdapter(this,"HistoryDb","HistoryTable",getDbSQLCOde("HistoryTable"));
        BookmarksDB=new DBAdapter(this,"BookmarksDb","BookmarksTable",getDbSQLCOde("BookmarksTable"));
        IntroPagesDB=new DBAdapter(this,"IntroPagesDb","IntroPagesTable",getDbSQLCOde("IntroPagesTable"));
        TabsDB=new DBAdapter(this);
        BookmarksDB.open();
        IntroPagesDB.open();
        TabsDB.open();
        HisrotyDb.open();
    }

    private String getDbSQLCOde(String tableNAme) {
        String createDbSQL;
        createDbSQL = "create table " + tableNAme
                + " (" + KEY_ROWID + " integer primary key autoincrement, "
                + "PageName" + " string not null, "
                + "PageUrl" + " string not null, "
                + "Time" + " string not null "
                + ");";
        return createDbSQL;
    }

    private void SaveSettings() {
        TabsDB.deleteAll();
        for (int x = 0; x < adapter.mFragmentList.size(); x++) {
            WebFragment wF = adapter.mFragmentList.get(x);
            String tabName = adapter.mFragmentTitleList.get(x);
            String TabUrl;
            if (wF.browser == null) {
                TabUrl = wF.urll;
            } else {
                TabUrl = wF.browser.web.getUrl();
            }
            TabsDB.insertRow(TabUrl, tabName, null);

        }
    }

    public void getSavedTabs() {

        Cursor cursor = TabsDB.getAllRows();
        String TabName;
        String TabUrl;
        if (cursor.moveToFirst()) {
            do {
                TabName = cursor.getString(DBAdapter.COL_PAGEName);
                TabUrl = cursor.getString(DBAdapter.COL_PageUrl);
                CreateTab(TabName, TabUrl);

            } while (cursor.moveToNext());
            cursor.close();
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        }else
        {
            TabsEditorFragment tabsEditorFragment =new TabsEditorFragment();
            Bundle b=new Bundle();
            b.putInt("id",R.id.Action_AddTab);
            tabsEditorFragment.setArguments(b);
            navigationTabHelper.showAnOptionLy(tabsEditorFragment);
        }
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        statusbarColor = settings.getString("sColor", null);
        color = settings.getString("Color", null);
        if (statusbarColor != null) ApplyTheme();
            color = "#FF2b2a2a";
            statusbarColor = "#FF232222";
            ApplyTheme();
        }



    private void CreateTab(String tabName, String url) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        //bundle.putString("TT","H");
        WebFragment fragInfo = new WebFragment();
       fragInfo.WebLayout = getLayoutInflater().inflate(R.layout.browser_layout, null);
        fragInfo.browser = new Browser(fragInfo.WebLayout,this);
        fragInfo.setArguments(bundle);
        fragInfo.urll = url;
        if(adapter.mFragmentList.size()==0) navigationTabHelper.currentBrowser =fragInfo.browser;
        adapter.addFragment(fragInfo, tabName);
        editTabLayoutStyle();

    }


    public void Alert() {

        new AlertDialog.Builder(WebViewActivity.this)
                .setTitle("Save Tabs")
                .setMessage("Are you sure you want to save this tabs ?" + "\n\n by clicking yes this tabs will be saved and opend " +
                        "automatically as soon as the app had started")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        SaveSettings();

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }

                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }


    @Override
    //this method handles on back key click to exit the app
    public void onBackPressed() {
        if (!isFirstBackPress) {
            onDestroy();
            System.exit(0);
        } else {
            if (viewPager.getTranslationY() != 0) {
                showLastScreen();
            } else {
                Toast.makeText(WebViewActivity.this, "Press again to Exit", Toast.LENGTH_SHORT).show();
                isFirstBackPress = false;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isFirstBackPress = true;

                    }
                }, 3 * 1000);

            }
        }
    }

    private void showLastScreen() {
        if (BackgroundTab.backTab!=null) {
            BackgroundTab.OpenBackgroundTab(BackgroundTab.backgroundTabListView.adapter.position,true);
        }

    }




    public void editTabLayoutStyle() {
        if(300*(adapter.getCount())>tabLayout.getWidth()&&tabLayout.getWidth()!=0)
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        else tabLayout.setTabMode(TabLayout.MODE_FIXED);



    }

@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
       fullScreen();
}

    public void fullScreen() {

    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
    {
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }

    else

    {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                                        // hide status bar
        );


    }


    }

    public void startThemeChangeLayout() {
         if(ColorLayout==null)ColorLayout=getLayoutInflater().inflate(R.layout.theme_chooser,null);
        else ((ViewGroup)ColorLayout.getParent()).removeView(ColorLayout);

        new AlertDialog.Builder(WebViewActivity.this)
                .setTitle("Theme")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       ApplyTheme();

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                })
                .setIcon(R.drawable.ic_launcher)
                .setView(ColorLayout)
                .show();


    }
    public void getColor(View v){
    if(Vieww!=null){
        Vieww.setTextColor(Color.parseColor(color));
      Vieww.setText(String.valueOf(color));

    }
     Vieww=(TextView) v;
    statusbarColor= (String)Vieww.getTag();
    color=Vieww.getText().toString();
    Vieww.setText("✓");
    Vieww.setTextColor(Color.WHITE);
}







     void showBackTabsAlert() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = settings.edit();
        if(settings.getInt("BTN",0)==0){
            LinearLayout ly=new LinearLayout(this);
            final CheckBox check=new CheckBox(this);
            check.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            check.setText(R.string.dontshowthisagain);
            ly.addView(check);
            new AlertDialog.Builder(WebViewActivity.this)
                    .setTitle("BackTabs")
                    .setMessage("You have just discovered 'BackTabs' which allow you to open a browser tab.\n to go to main screen just press the Home icon")
                    .setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if(check.isChecked()){
                                editor.putInt("BTN",1);
                                editor.apply();
                            }
                            dialog.dismiss();

                        }
                    })
                    .setIcon(R.drawable.ic_launcher)
                    .setView(ly)
                    .show();




        }
    }







    private void ApplyTheme() {
        RelativeLayout drawerHEADER= (RelativeLayout) findViewById(R.id.nav_header_container);
        RelativeLayout drawerly1= (RelativeLayout) findViewById(R.id.bt);
        RelativeLayout drawerly2= (RelativeLayout) findViewById(R.id.drawerly);
  drawerly1.setBackgroundColor(Color.parseColor(color));
        drawerly2.setBackgroundColor(Color.parseColor(color));
        tabLayout.setBackgroundColor(Color.parseColor(color));
        drawerHEADER.setBackgroundColor(Color.parseColor(statusbarColor));
      //  BackgroundTab.toolbar.setBackgroundColor(Color.parseColor(color));
        BackgroundTab.slidingLayout.setBackgroundColor(Color.parseColor(color));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor(statusbarColor));

        }
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("sColor",statusbarColor);
        editor.putString("Color",color);
        editor.apply();
    }


   public void OnTabs() {

            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
            addBut.setVisibility(View.GONE);
            navigationTabHelper.whatMenuToShow();
          //  editTabLayoutStyle();


    }



void reguestPermission() {
    if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

        } else {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    12);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }
}
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 12: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    public  int GetTabPosition=0;
    private boolean isFirstBackPress = true;

    public backGroundTabsHandler BackgroundTab ;


    public void notifyHistoryIsEnabled(boolean isChecked) {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = settings.edit();
        if (isChecked) {
            editor.putBoolean("history", true);
            HistoryIsEnabled=true;
        }
        else  {
            editor.putBoolean("history",false);
            HistoryIsEnabled=true;
        }
            editor.apply();

    }
}


