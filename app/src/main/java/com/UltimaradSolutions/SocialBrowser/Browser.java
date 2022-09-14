package com.UltimaradSolutions.SocialBrowser;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import im.delight.android.webview.AdvancedWebView;


@SuppressWarnings("WeakerAccess")
public class Browser implements AdvancedWebView.Listener {
    public EditText SearchTextBox;
    public AdvancedWebView web;
    public View backTabLayout;
    public LinearLayout linearLayout;
    public ImageButton but ;
    public View mCustomView;
    public View mContentView;
    public Browser.ChromeClient mWebChromeClient = null;
    public FrameLayout mCustomViewContainer;
    public WebChromeClient.CustomViewCallback mCustomViewCallback;
    ProgressBar Pbar;
    private BackgroundTabListView BackTabListHelper;
    private boolean ISInLoadingState =false;
    RecyclerViewAdapter.MyViewHolder ListItem;
    public Bitmap ICon;
    public PopupMenu menu;
    boolean loadingIsDone=false;
    WebViewActivity Activity;
    private String LastURL="";
    private SwipeRefreshLayout swipeLayout;


    public Browser(View Layout, WebViewActivity activity) {
        Activity=activity;
        assignViews(Layout,R.id.webView, R.id.progressBar2,R.id.SWLY);

    }


    public Browser(BackgroundTabListView BackTabListhelper,WebViewActivity activity) {
        final GestureDetector gestureDetector = new GestureDetector(Activity, new CustomeGestureDetector());

        this.BackTabListHelper = BackTabListhelper;
        Activity=activity;
        LayoutInflater layoutInflater = (LayoutInflater) Activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        backTabLayout = layoutInflater.inflate(R.layout.browser_layout, null);
        web =  backTabLayout.findViewById(R.id.webView);
        web.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        assignViews(backTabLayout,R.id.webView, R.id.progressBar2, R.id.SWLY);
        SearchTextBoxInitialziation();



    }

    public Browser(View webLayout, int webid, int PbarID, WebViewActivity activity) {
        Activity=activity;
        web =  webLayout.findViewById(webid);
        assignViews(webLayout,webid,PbarID, 0);



    }

    public void SearchTextBoxInitialziation() {
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, (float) 9.1);
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, (float) 0.9);
         linearLayout=new LinearLayout(Activity);
       // linearLayout.setLayoutParams(param);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.MATCH_PARENT));
        linearLayout.setWeightSum(10);
        but=new ImageButton(Activity);
        but.setLayoutParams(param1);
        linearLayout.setBackgroundColor(Color.WHITE);
        but.setBackgroundColor(Color.WHITE);
        but.setImageResource(R.drawable.ic_action_navigation_more_vert);
        SearchTextBox = new EditText(Activity);
        SearchTextBox.setLayoutParams(param2);
        SearchTextBox.setSingleLine(true);
        SearchTextBox.setImeOptions(EditorInfo.IME_ACTION_GO);
        SearchTextBox.clearFocus();
        backTabLayout.requestFocus();
        SearchTextBox.setText(web.getUrl());
        linearLayout.setTag("s");
        SearchTextBox.setBackgroundResource(R.drawable.search_drawable);
        SearchTextBox.setSelectAllOnFocus(true);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.addView(SearchTextBox);
        menuOncClick();
        linearLayout.addView(but);
        //but.setWidth(15);
        SearchTextBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if (arg1 == EditorInfo.IME_ACTION_GO) {
                    InputMethodManager imm = (InputMethodManager) Activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    web.loadUrl("https://www.google.com.eg/webhp?sourceid=chrome-instant&ion=1&espv=2&es_th=1&ie=UTF-8#es_th=1&q=" + SearchTextBox.getText().toString());
                    imm.hideSoftInputFromWindow(SearchTextBox.getWindowToken(), 0);
                  //  Activity.historyHelper.Adapter.getFilter().filter("");

                }
                return false;
            }

        });
onTextCHanged(SearchTextBox);
    }
    //this method responsible of filtiring the listview when the search is on
    private void onTextCHanged(EditText view) {
        view.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text

            //    Activity.historyHelper.Adapter.getFilter().filter(cs);

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {


            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Activity.BackTabLayout.findViewWithTag("HL")!=Activity.historyHelper.List)
                {
                    SearchTextBox.setSelectAllOnFocus(true);
                   // ((ViewGroup)Activity.historyHelper.List.getParent()).removeView(Activity.historyHelper.List);
                   // Activity.BackTabLayout.addView(Activity.historyHelper.List);
                   // Activity.historyHelper.List.bringToFront();
                  //  Activity.BackTabLayout.invalidate();
                    Activity.fullScreen();


                }
            }
        });

    }

    private void menuOncClick() {
        final PopupMenu menu =new PopupMenu(Activity,but);

        menu.inflate(R.menu.popup_menu);


        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                menu.show();
            }
        });
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.action_copy: {
                        ClipboardManager clipboard = (ClipboardManager) Activity.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Url", web.getUrl());
                        clipboard.setPrimaryClip(clip);
                        return true;
                    }
                    case R.id.share: {
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBodyText = web.getUrl();
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Share Url");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                        Activity.startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
                        return true;

                    }
                    case R.id.bookmark: {
                         long id =Activity.BookmarksDB.insertRow(web.getUrl(),web.getTitle(),Activity.currentDate);
                        if(id!=-1)Toast.makeText(Activity,"Page has successfully been added to bookmarks ",Toast.LENGTH_SHORT).show();
                        else Toast.makeText(Activity,"Page is already at bookmarks",Toast.LENGTH_SHORT).show();
                        return true;

                    }
                }
                return true;
            }
        });
    }

    public void OpenLink(String link) {
        mWebChromeClient = new ChromeClient();
        web.setWebViewClient(new WebClient());
        web.setListener(Activity, this);
        web.setWebChromeClient(mWebChromeClient);
        web.getSettings().supportZoom();
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setDomStorageEnabled(true);
        web.getSettings().setSaveFormData(true);
        web.getSettings().setAllowContentAccess(true);
        web.getSettings().setAllowFileAccess(true);
        web.getSettings().setAllowFileAccessFromFileURLs(true);
        web.getSettings().setAllowUniversalAccessFromFileURLs(true);
        web.setClickable(true);
        web.getSettings().setBuiltInZoomControls(true);
        web.getSettings().setDisplayZoomControls(false);
        web.loadUrl(link);

         swipeToRefresh(swipeLayout);
    }

    public void assignViews(View layout, int webid, int pbarID, int swly) {
       if(web==null) web = layout.findViewById(webid);
        Pbar =  layout.findViewById(pbarID);
        web.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    WebView webView = (WebView) v;

                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            if (webView.canGoBack()&&webView.hasFocus()) {
                                webView.goBack();
                                return true;
                            }
                            else {
                               return backTabBack();

                            }


                    }
                }

                return false;
            }
        });
if(swly!=0)swipeLayout=layout.findViewById(swly);
    }

    private boolean backTabBack() {
        boolean back=false;
        if (backTabLayout != null) {
            back=true;
            BackTabListHelper.adapter.delete(BackTabListHelper.adapter.position);
            if(backGroundTabsHandler.BackGroundTabsList.size()==0) {
                BackTabListHelper.adapter.position = 99;
                Activity.BackgroundTab.showHomeFragment(true);
            }else {

               BackTabListHelper.adapter.position=backGroundTabsHandler.BackGroundTabsList.size()-1;
          Activity.BackgroundTab.OpenBackgroundTab(BackTabListHelper.adapter.position,true);
            }
        }
        return back;
    }    //swipeLayout = (SwipeRefreshLayout) layout.findViewById(R.id.webLy);
    //   Pbar= (ProgressBar) layout.findViewById(fbpb);


    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        if (!loadingIsDone) {
            if (SearchTextBox != null) {
                SearchTextBox.setText(url);
            }
            if (backTabLayout != null && !ISInLoadingState) {
                BackTabListHelper.adapter.UpdateListItem(true, web, ListItem, Browser.this);
                ISInLoadingState = true;

            }
        }
        if(web.getProgress()>10)loadingIsDone=true;
    }

    @Override
    public void onPageFinished(String url) {
        if ((loadingIsDone||web.getProgress()==100)&& ISInLoadingState) {
            if (backTabLayout != null ) {
                BackTabListHelper.adapter.UpdateListItem(false,  web, ListItem, Browser.this);
                ISInLoadingState = false;

            }


        }
        if(web.getProgress()==100)loadingIsDone=false;


    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {
        if (AdvancedWebView.handleDownload(Activity, url, suggestedFilename)) {
            Toast.makeText(Activity, "DOWNLOADING...", Toast.LENGTH_SHORT).show();
        } else {
            // download couldn't be handled because user has disabled download manager app on the device
            Toast.makeText(Activity, "please enable download manager app", Toast.LENGTH_SHORT).show();
            // TODO show some notice to the user
        }
    }

    @Override
    public void onExternalPageRequest(String url) {





    }

    private boolean checkURL(String url) {
        if (web.hasFocus()) {
            if (url.substring(0, 5).equals("https")) {
                return isUrlsMatch(url, 8, 12);
            } else {


                return isUrlsMatch(url, 7, 11);
            }


        }else return true;
    }

    private boolean isUrlsMatch(String url, int from, int to) {

        if(url.substring(from,to).contains("www.")||url.substring(from,to).contains("WWW.")){
            if(url.charAt(to)=='h'||url.charAt(to)=='m')to=to+2;
            return web.getUrl().contains(url.substring(to, url.indexOf(".", to)));

        }
        else {
if(url.charAt(from)=='h'||url.charAt(from)=='m')from=from+2;
            return web.getUrl().contains(url.substring(from, url.indexOf(".", from)));
        }
    }

    public void Refresh() {
        web.reload();


    }

    public void swipeToRefresh(SwipeRefreshLayout slayout) {
       if(slayout!=null) {
           slayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue,R.color.red);

           slayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
               @Override
               public void onRefresh() {
                   Refresh();


               }
           });

       }
    }

    public void assignListItem(RecyclerViewAdapter.MyViewHolder holder) {
        if(ListItem==null)this.ListItem=holder;
    }
    void finish() {
        web.destroy();
        web=null;
        backTabLayout=null;
        ICon=null;
        ListItem=null;
        Pbar=null;
    }

    public class ChromeClient extends WebChromeClient {


        FrameLayout.LayoutParams LayoutParameters = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            Pbar.setProgress(newProgress);
            //loadingTitle.setProgress(newProgress);
            // hide the progress bar if the loading is complete

if(newProgress==10&&(web.getId() != (R.id.web2) || web.getId() != (R.id.Web1)))Activity.navigationTabHelper.notifyMenuItemChanged(0, true);
            if (newProgress == 100) {
                Pbar.setVisibility(View.GONE);
               if(swipeLayout!=null) swipeLayout.setRefreshing(false);
                AddToHestory(view);
                if (web.getId() != (R.id.web2) || web.getId() != (R.id.Web1))
                    Activity.navigationTabHelper.notifyMenuItemChanged(0, true);


            } else {
                Pbar.setVisibility(View.VISIBLE);

            }
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
           if(icon!=null) scaleAndSaveIcon(icon);
            if(Activity.navigationTabHelper.currentBrowser!=null&&Activity.navigationTabHelper.currentBrowser.equals(Browser.this))
         Activity.navigationTabHelper.setIconNavBar(ICon);


        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            // if a view already exists then immediately terminate the new one
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mContentView = Activity.MainScreen;
            mContentView.setVisibility(View.GONE);
            mCustomViewContainer = new FrameLayout(Activity);
            mCustomViewContainer.setLayoutParams(LayoutParameters);
            mCustomViewContainer.setBackgroundResource(android.R.color.black);
            view.setLayoutParams(LayoutParameters);
            mCustomViewContainer.addView(view);
            mCustomView = view;
            mCustomView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        WebView webView = (WebView) v;

                        switch (keyCode) {
                            case KeyEvent.KEYCODE_BACK:
                                if (mCustomViewContainer != null) {
                                    mWebChromeClient.onHideCustomView();
                                  Activity.fullscreenISOn = true;
                                    Activity.fullScreen();
                                    return true;
                                }
                        }
                    }
                    return false;
                }
            });
            mCustomViewCallback = callback;
           mCustomViewContainer.setVisibility(View.VISIBLE);
            Activity.setContentView(mCustomViewContainer);
         Activity.fullScreen();

        }

        @Override
        public void onHideCustomView() {
            if (mCustomView == null) {
                return;
            } else {
                // Hide the custom view.
                mCustomView.setVisibility(View.GONE);
                // Remove the custom view from its container.
              mCustomViewContainer.removeView(mCustomView);
                mCustomView = null;
               mCustomViewContainer.setVisibility(View.GONE);
                mCustomViewCallback.onCustomViewHidden();
                // Show the content view.
                mContentView.setVisibility(View.VISIBLE);
                Activity.setContentView(mContentView);
            }
        }
    }

    private void scaleAndSaveIcon(Bitmap icon) {
        if(!Activity.BackgroundTab.toolbar.isShown()){

            if(ICon==null)ICon= Activity.BackgroundTab.backgroundTabListView.adapter.getResizedBitmap(icon,Activity.navigationTabHelper.NavmenuBut.getHeight(),
                    Activity.navigationTabHelper.NavmenuBut.getHeight());
        }else ICon= Activity.BackgroundTab.backgroundTabListView.adapter.getResizedBitmap(icon,Activity.navigationTabHelper.NavmenuBut.getHeight(),
                Activity.navigationTabHelper.NavmenuBut.getHeight());
           

    }

    private void AddToHestory(WebView view) {
        if(Activity.HistoryIsEnabled) {
            if (view.getTitle() != null && !view.getTitle().equals("") && !view.getUrl().equals(LastURL)) {
                DateFormat df = new SimpleDateFormat(("EEE, d MMM yyyy"));//("EEE, d MMM yyyy, HH:mm");
                String date = df.format(Calendar.getInstance().getTime());
                if (!Activity.currentDate.equals(date)) {//ana 7atet el conition da 3ashan mesh kol sheywa hycheck fel db
                    Activity.historyHelper.AddDateRowToDb(date);

                }
                long id = Activity.HisrotyDb.insertRow(view.getUrl(), view.getTitle(), date);
                LastURL = view.getUrl();


            }
        }
    }

    public class WebClient extends WebViewClient {

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(backTabLayout==null&&!checkURL(url)){
                RecyclerViewAdapter adapter=Activity.BackgroundTab.backgroundTabListView.adapter;
                Activity.BackgroundTab.createBackroundTab(url);
                Activity.BackTabLayout.setVisibility(View.VISIBLE);
                adapter.position=backGroundTabsHandler.BackGroundTabsList.size()-1;
               Activity.BackgroundTab.OpenBackgroundTab(adapter.position,true);

                return true;

            }
            else {

                return super.shouldOverrideUrlLoading(view, url);

            }

        }
    }
    private class CustomeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1 == null || e2 == null) return false;
            if(e1.getPointerCount() > 1 || e2.getPointerCount() > 1) return false;
            else {
                try {
                    if(e1.getY() - e2.getY() > 20 ) {
                        // Hide Actionbar
                       Activity.abbarLayout.setExpanded(false);
                   web.invalidate();
                        return false;
                    }
                    else if (e2.getY() - e1.getY() > 20 ) {
                        // Show Actionbar
                        Activity.abbarLayout.setExpanded(true);
                      web.invalidate();
                        return false;
                    }

                } catch (Exception e) {
                    web.invalidate();
                }
                return false;
            }


        }
    }
}





