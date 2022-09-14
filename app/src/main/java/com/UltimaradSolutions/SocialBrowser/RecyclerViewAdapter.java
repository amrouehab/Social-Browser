package com.UltimaradSolutions.SocialBrowser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import im.delight.android.webview.AdvancedWebView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private final RecyclerView recyclerView;
    private LayoutInflater inflater;
    public int position=99;
    WebViewActivity activity;


    public RecyclerViewAdapter(WebViewActivity context, RecyclerView recyclerView) {
        inflater = LayoutInflater.from(context);
        this.recyclerView=recyclerView;
        activity=context;
    }
//bug
    public void delete(int position) {
      //  MyViewHolder vh = (MyViewHolder) backTabList.findViewHolderForLayoutPosition(position);
      //  resetViewHolder(vh);
        backGroundTabsHandler.BackGroundTabsList.get(position).finish();
        backGroundTabsHandler.BackGroundTabsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, backGroundTabsHandler.BackGroundTabsList.size());



    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        MyViewHolder vh= backGroundTabsHandler.BackGroundTabsList.get(viewType).ListItem;
            if(vh==null) {
                view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
                vh = new MyViewHolder(view);
                backGroundTabsHandler.BackGroundTabsList.get(viewType).assignListItem(vh);
            }
            return vh;
        }
    @Override
    public int getItemViewType(int position) {

        return position;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
            MyViewHolder BrowserViewHolder=backGroundTabsHandler.BackGroundTabsList.get(position).ListItem;
            if(holder!=BrowserViewHolder) {
                backGroundTabsHandler.BackGroundTabsList.get(position).ListItem= holder;
                BrowserViewHolder=holder;
            }
            AdvancedWebView wv=  backGroundTabsHandler.BackGroundTabsList.get(position).web;
            UpdateListItem(false,wv,BrowserViewHolder,backGroundTabsHandler.BackGroundTabsList.get(position));


    }

    @Override
    public int getItemCount() {
        return backGroundTabsHandler.BackGroundTabsList.size();
    }

    public void addBackTabItem(Browser listItem) {
        notifyItemInserted(backGroundTabsHandler.BackGroundTabsList.indexOf(listItem));
    }

    public void setPosition(int pos) {
        position=pos;
    }

    public void UpdateListItem(boolean loading, AdvancedWebView view, MyViewHolder vh, Browser browser) {
        try {
           // MyViewHolder vh = (MyViewHolder) backTabList.findViewHolderForLayoutPosition(position);
            if(loading){
                vh.title.setText("Loading...");
                vh.imgv.setImageResource(R.drawable.ic_launcher);

            }else {
                Bitmap icon=browser.ICon;
                String title=view.getTitle();
                vh.title.setText(title);
                vh.imgv.setImageBitmap(icon);
                if(icon==null)vh.imgv.setImageResource(R.drawable.ic_launcher);



            }
            int width =activity.BackTabLayout.getWidth();
            int height =activity.BackTabLayout.getHeight();
            vh.TabImage.setImageBitmap(getScreenShot(view,height,width,vh.TabImage));
        }
        catch (Exception e)

        {e.getMessage();}

    }

    private Bitmap getScreenShot(WebView view, int height, int width, ImageView tabImage) {
        Bitmap tabImagee;
        view.setDrawingCacheEnabled(true);
        // this is the important code :)
// Without it the view will have a dimension of 0,0 and the bitmap will be null
        if(activity.BackTabLayout.getChildAt(0)==null||view.getHeight()==0) {
            view.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            tabImagee = Bitmap.createBitmap(view.getWidth(),
                    view.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(tabImagee);
            view.draw(canvas);

        }
        else {
            tabImagee = Bitmap.createBitmap(activity.BackTabLayout.getWidth(),
                    activity.BackTabLayout.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(tabImagee);
            activity.BackTabLayout.draw(canvas);


        }

       tabImagee = getResizedBitmap(tabImagee,tabImage.getHeight(),tabImage.getWidth());
        view.setDrawingCacheEnabled(false);
        return tabImagee;
    }
    public  Bitmap getResizedBitmap(Bitmap image, int newHeight, int newWidth) {
        int width = image.getWidth();
        int height = image.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(image, 0, 0, width, height,
                matrix, false);
        return resizedBitmap;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imgv;
        ImageView TabImage;



        public MyViewHolder(View itemView) {
            super(itemView);
            title =itemView.findViewById(R.id.title);
            imgv=itemView.findViewById(R.id.imvv);
            TabImage=itemView.findViewById(R.id.imageView2);
            // Container.getSettings().setJavaScriptEnabled(true);

        }

    }



}



