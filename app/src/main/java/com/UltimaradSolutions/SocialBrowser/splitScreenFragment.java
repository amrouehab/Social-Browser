package com.UltimaradSolutions.SocialBrowser;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;


public class splitScreenFragment extends WebFragment {


    public String url2;
    private int progress;
    private int width;
    public String FragmentType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(FirstCreate) {

            urll = this.getArguments().getString("url");
            url2 =this.getArguments().getString("url2");
            FragmentType="H";
            web1.OpenLink(urll);
            web2.OpenLink(url2);
            FirstCreate=false;

        }

        return WebLayout;
    }


    public void showLayout(LayoutInflater inflater,WebViewActivity Activity) {
        WebLayout=inflater.inflate(R.layout.test,null);
        seekbar= WebLayout.findViewById(R.id.WebSize);
        web1=new Browser(WebLayout,R.id.Web1,R.id.pBar1,Activity);
        web2=new Browser(WebLayout,R.id.web2,R.id.pBar2,Activity);
        browser=web1;
        browser.Activity=web1.Activity;
        FragmentType="s";
        setInitialHeights(Activity);
        registerForContextMenu(web1.web);
        registerForContextMenu(web2.web);
        web1.web.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(web1.web.isFocused()) web1.Activity.navigationTabHelper.notifyBrowserChanged(web1);
                if(web2.web.isFocused())web2.Activity.navigationTabHelper.currentBrowser=web2;
            }
        });

    }

    private void setInitialHeights(WebViewActivity activity) {
        int height = activity.viewPager.getHeight();
        width=activity.viewPager.getWidth();
      //  seekbar.setLayoutParams(new LinearLayout.LayoutParams(width,30));
        web1.web.setLayoutParams(new LinearLayout.LayoutParams(width,(height /2)));
        web2.web.setLayoutParams(new LinearLayout.LayoutParams(width,(height /2)));
        seekbar.setMax(height -20);
        seekbar.setProgress(height /2);
        progress= height /2;
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i>progress) {
                    web1.web.setLayoutParams(new LinearLayout.LayoutParams(width, i));
                    web2.web.setLayoutParams(new LinearLayout.LayoutParams(width,web2.web.getHeight()-i));
                    progress=i;
                }
                else {
                    web2.web.setLayoutParams(new LinearLayout.LayoutParams(width, web2.web.getHeight()+i));
                    web1.web.setLayoutParams(new LinearLayout.LayoutParams(width,i));
                    progress=i;

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        web1.web.loadUrl("about:blank");
        web2.web.loadUrl("about:blank");
    }

    public Browser  web1;
    public Browser  web2;
    public SeekBar seekbar;


}
