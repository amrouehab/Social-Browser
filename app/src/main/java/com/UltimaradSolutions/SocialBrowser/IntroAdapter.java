package com.UltimaradSolutions.SocialBrowser;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


 class IntroAdapter extends BaseAdapter {
    private final int ModeID;
    WebViewActivity activity;
    int iconsIDs[]=new int[]{R.mipmap.fb_icon,R.mipmap.twitter_icon,R.mipmap.isnt_icon,R.mipmap.whatss_icon,R.mipmap.meassenger_icon
            ,R.mipmap.ic_launcher,R.mipmap.gpluss_icon,R.mipmap.lin_icon,R.drawable.search_drawable};
    String iconsNames[]=new String[]{"Facebook[http://www.fb.com.PNG"
            ,"Twitter[http://www.twitter.com.PNG"
            ,"Instagram[http://www.instagram.com.PNG"
            , "WhatsApp[https://web.whatsapp.com/.PNG"
            ,"FbChat[https://www.facebook.com/messages/.PNG"
            ,"YouTube[http://www.Youtube.com.PNG"
            ,"Google+[http://www.plusgoogle.com.PNG"
            ,"Linkedin[http://www.linkedin.com.PNG"
           };
    ArrayList<introView> Views=new ArrayList<>();
   File directory=null;

     IntroAdapter(WebViewActivity activity, int id) {
        this.activity=activity;
            createSdIConsDirectroty();
this.ModeID=id;
    }

    private void createSdIConsDirectroty() {
        directory =new File(activity.getFilesDir(),"Icons");
            if (!directory.exists()) {
                directory.mkdir();
            }
    }


     @Override
    public int getCount() {
        return Views.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        return Views.get(i);
    }


     introView CreateIntroView(String tabName,String Url, Drawable myDrawable, int i) {
        introView introView = (com.UltimaradSolutions.SocialBrowser.introView) activity.getLayoutInflater().inflate(R.layout.intro_view,null);
         introView.initlaizeView();
        introView.setIconAndTitle(tabName,Url, myDrawable,i);
         introView.check.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 int checkedTImes=0;
                 if (ModeID==R.id.Split_pages){
                     for (int i = 0; i < Views.size(); i++) {
                         if(Views.get(i).check.isChecked())checkedTImes++;
                     }
                     }
                     if(checkedTImes>2)((CheckBox)v).setChecked(false);
                 }
         });
        return introView;
    }


     void addIntroViewToList(introView introView) {
        Views.add(introView);
        notifyDataSetChanged();
    }

    public void delIntroViewFromList(introView introView) {
        Views.remove(introView);
        notifyDataSetChanged();
    }

     void saveTheNewPage( ImageView icon,String Name,String Url) throws IOException {
        Bitmap bm;
        bm=((BitmapDrawable)icon.getDrawable()).getBitmap();
        assert Name != null;
        File file = new File(directory,Name);
        FileOutputStream outStream = new FileOutputStream(file);
        bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
        outStream.flush();
        outStream.close();
        activity.IntroPagesDB.insertRow(Url,Name,"none");

    }


}

