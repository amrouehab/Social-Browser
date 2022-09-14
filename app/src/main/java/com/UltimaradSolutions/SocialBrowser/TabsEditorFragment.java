package com.UltimaradSolutions.SocialBrowser;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;


public class TabsEditorFragment extends SettingsFragment {
    private EditText SiteUrl;
    private EditText TabName;
    public int id;
    View Layout ;
    View ManualLy;
    GridView TabsList;
    IntroAdapter introAdapter;
    private Bitmap AddedViewIcon;
    private Button addbutton;
    WebViewActivity activity;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        id = getArguments().getInt("id");
        activity= (WebViewActivity) getActivity();
        startMenuLayout(inflater, R.layout.main_screen, null);
        ManualLy = Layout.findViewById(R.id.manualLy);
        TabsList = Layout.findViewById(R.id.gridview);
        introAdapter = new IntroAdapter((WebViewActivity) getActivity(), id);
        getDefaultPages();
        setIcongetterButtonOnClick();
        backOnclick(Layout);

        return Layout;

    }
    void getDefaultPages()  {
        introView introView;
        for (int i = 0; i < 8; i++) {
            String pagedetail=introAdapter.iconsNames[i];
            introView =introAdapter.CreateIntroView(pagedetail.substring(0,pagedetail.indexOf('[')),
                    pagedetail.substring(pagedetail.indexOf('[',0)+1,pagedetail.lastIndexOf('.')),introAdapter.activity.getResources().getDrawable(introAdapter.iconsIDs[i]),i);
            introAdapter.addIntroViewToList(introView);
            onclickIntroView(introView);
        }


    }

    public void setIcongetterButtonOnClick() {
        addUserAddedViewsFromSDCard();
        ManualLy.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
                                                                   @Override
                                                                   public void onClick(View view) {

                                                                       if(getUrlStartText(SiteUrl.getText().toString()).equals("")) Toast.makeText(activity, "please enter a valid url", Toast.LENGTH_SHORT).show();
                                                                       else {
                                                                           SearchOnlineForIcon(getUrlStartText(SiteUrl.getText().toString()));
                                                                           Toast.makeText(activity,"Checking url please waite...",Toast.LENGTH_LONG).show();
                                                                           addbutton.setVisibility(View.INVISIBLE);
                                                                       }
                                                                   }
                                                               }
        );

    }
    //setUp adapter views for main Screen
    private void addUserAddedViewsFromSDCard() {
        String TabName;
        Bitmap myBitmap = null;
        Drawable myDrawable = null;
        int i=0;
        if (introAdapter.directory.exists()&&introAdapter.directory.list().length>0) {
            String IconNamesList[]=introAdapter.directory.list();

            do {
                TabName = IconNamesList[i];
                Cursor cursor=(activity).IntroPagesDB.getRowByName(TabName);
                myBitmap = BitmapFactory.decodeFile(introAdapter.directory.getPath() + "/" + TabName);
                myDrawable = new BitmapDrawable(introAdapter.activity.getResources(), myBitmap);
                introAdapter.addIntroViewToList(introAdapter.CreateIntroView(TabName,cursor.getString(DBAdapter.COL_PageUrl),myDrawable,i));
                onclickIntroView(introAdapter.Views.get(i));
                i++;

            } while (i<IconNamesList.length);
            //add manual view

        }
        introView introView = introAdapter.CreateIntroView("Add","add",introAdapter.activity.addBut.getDrawable(),introAdapter.getCount()+i);
        introView.check.setVisibility(View.GONE);
        introAdapter.Views.add(introView);
        onclickIntroView(introView);
        TabsList.setAdapter(introAdapter);
    }

    private void onclickIntroView(introView introView) {
        introView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   if(((introView)view).check.isChecked()) introAdapter.checkedViewsIndexes.add(introAdapter.Views.indexOf(view));
                //manual is clicked
                if(!((introView)view).check.isShown()){
                    startManualLayout();

                }



            }
        });

    }



    public void startManualLayout() {
        changeLayoutsVisibilty(true);
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        SiteUrl=Layout.findViewById(R.id.urlTEXT);
        TabName=Layout.findViewById(R.id.nameText);
        SiteUrl.requestFocus();
        SiteUrl.callOnClick();
        SiteUrl.performClick();
        imm.showSoftInput(SiteUrl, InputMethodManager.SHOW_IMPLICIT);
    }

    private void changeLayoutsVisibilty(boolean ShowMaualLy) {
        if(ShowMaualLy) {
            ManualLy.setVisibility(View.VISIBLE);
            TabsList.setVisibility(View.GONE);
        }else
        {
            TabsList.setVisibility(View.VISIBLE);
            ManualLy.setVisibility(View.GONE);
        }


    }
//we2fna l7d el manual click ht5fy el settings we t add intro view we notify el adapter

    public void OnaddClick() throws IOException {
        String url=getUrlStartText(SiteUrl.getText().toString());
        String tabName=TabName.getText().toString();
        if(url.equals("")||tabName.equals("")) Toast.makeText(activity, "please fill the requirements ", Toast.LENGTH_SHORT).show();
        else {

            introView introView =introAdapter.CreateIntroView(tabName,url,new BitmapDrawable(activity.getResources(),AddedViewIcon),introAdapter.getCount());
            introView addview=introAdapter.Views.get(introAdapter.Views.size()-1);
            introAdapter.delIntroViewFromList(addview);
            introAdapter.notifyDataSetChanged();
            introAdapter.Views.add(introView);
            introAdapter.notifyDataSetChanged();
            introAdapter.Views.add(addview);
            introAdapter.notifyDataSetChanged();
            introAdapter.saveTheNewPage(introView.icon,tabName,url);
            changeLayoutsVisibilty(false);
            //   CreateTabsCHosen();
            //  if (id != R.id.Split_pages) {
            //       EditingISDone = true;
            //       dismiss();
            //   }
        }
    }

    private void SearchOnlineForIcon(String Url) {
        WebView iconGetter=ManualLy.findViewById(R.id.iconGetter);
        iconGetter.setWebViewClient(new WebClient());
        iconGetter.setWebChromeClient(new ChromeClient());
        iconGetter.loadUrl(Url);
        ProgressBar pb =Layout.findViewById(R.id.pbar);
        pb.setVisibility(View.VISIBLE);
        addbutton.setVisibility(View.GONE);

    }
    public class ChromeClient extends WebChromeClient {

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            if(icon!=null)
            {
                AddedViewIcon=icon;
                ImageView iconn =ManualLy.findViewById(R.id.imageView3);
                iconn.setImageBitmap(icon);
                ProgressBar pb =Layout.findViewById(R.id.pbar);
                pb.setVisibility(View.GONE);
                addbutton.setVisibility(View.VISIBLE);

            }
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if(newProgress==100){
                ProgressBar pb =Layout.findViewById(R.id.pbar);
                pb.setVisibility(View.GONE);
                addbutton.setVisibility(View.VISIBLE);
            }
        }
    } public class WebClient extends WebViewClient {

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return super.shouldOverrideUrlLoading(view, url);

        }

    }

    public void StartClick(){
        addbutton =Layout.findViewById(R.id.Add);

        addbutton.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             if (!ManualLy.isShown())
                                                 CreateTabsCHosen();
                                             else try {
                                                 OnaddClick();
                                             } catch (IOException e) {
                                                 e.printStackTrace();
                                             }
                                         }
                                     }
                //  if (id != R.id.Split_pages) {
                //        EditingISDone = true;
                //         dismiss();
                //     }
                //  setupViewPager(viewPager);
                //   WebViewActivity.tabLayout.setupWithViewPager(viewPager);
                //   }

                // }

        );



    }
    public void startMenuLayout(LayoutInflater inflater, int main_screen, ViewGroup container) {
        if(id==R.id.Split_pages)Layout=null;
        if(Layout==null) Layout=inflater.inflate(main_screen,container,false);
        //  getTheme().applyStyle(R.style.MyMaterialTheme_Dark, true);
        StartClick();
        if (id==R.id.Split_pages){
            TextView header=(TextView)Layout.findViewById(R.id.textView);
            header.setText("Please choose two Pages for  'split Mode'");




        }
    }





    private void CreateTabsCHosen() {
        boolean aTabIsChoosen=false;
        ArrayList<String> SplitPagesDetails=new ArrayList<>();
        for (int i=0;i<introAdapter.Views.size();i++) {
            introView View= introAdapter.Views.get(i);
            if(View.check.isChecked()) {
                if(id==R.id.Split_pages) {
                    SplitPagesDetails.add(View.check.getText().toString());
                    SplitPagesDetails.add(View.Url);
                    if(SplitPagesDetails.size()==4){setupSplitPages(SplitPagesDetails);aTabIsChoosen=true;break;}

                }else
                    CreateTab(View.check.getText().toString(), View.Url);
                aTabIsChoosen=true;
            }
        }
        if (!aTabIsChoosen)
            Toast.makeText(activity, "Please choose at least one tab", Toast.LENGTH_SHORT).show();
        else removeThisFragment();
    }

    private String getUrlStartText(String s) {
        try {
            if (s.substring(0, 4).contains("http")) return s;
            else return "https://" + s;
        }catch (Exception e){

            return "https://" + s;
        }

    }


    private void CreateTab(String tabName, String url) {

        WebFragment fragInfo = new WebFragment();
        fragInfo.WebLayout = activity.getLayoutInflater().inflate(R.layout.browser_layout, null);
        fragInfo.browser = new Browser(fragInfo.WebLayout,(activity));
        AddingFragment(tabName, url,null, fragInfo);


    }

    private void setupSplitPages(ArrayList<String> SplitPageDetails) {
        String tabname=SplitPageDetails.get(0),tabname2=SplitPageDetails.get(2);
        if(tabname.length()!=1&&tabname2.length()!=1)tabname=tabname.substring(0,1)+"/"+tabname2.substring(0,1);
        else tabname=tabname.substring(0,0)+"/"+tabname2.substring(0,0);
        splitScreenFragment fragInfo = new splitScreenFragment();
        fragInfo.showLayout(activity.getLayoutInflater(),(activity));
         AddingFragment(tabname,SplitPageDetails.get(1),SplitPageDetails.get(3),fragInfo);







    }
    void AddingFragment(String tabName, String url, String url2, WebFragment fragInfo) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("url2", url2);
        fragInfo.setArguments(bundle);
        fragInfo.urll = url;
        ((WebViewActivity)getActivity()).adapter.addFragment(fragInfo, tabName);

    }

    @Override
    void removeThisFragment() {
        super.removeThisFragment();
        //check that this layout is the intro to enable the changes in the viewpager adapter
        if(TabsList!=null&&Layout.findViewById(R.id.Add)!=null)((WebViewActivity)getActivity()).OnTabs();
    }
}