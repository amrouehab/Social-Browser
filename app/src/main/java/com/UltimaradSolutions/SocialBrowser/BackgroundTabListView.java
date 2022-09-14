package com.UltimaradSolutions.SocialBrowser;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class BackgroundTabListView {

    public RecyclerView backTabList;
    public RecyclerViewAdapter adapter;
    private View layout;
    WebViewActivity Activity;
    private static String TAG = BackgroundTabListView.class.getSimpleName();
    public DrawerLayout mDrawerLayout;
    public View containerView;
    private ActionBarDrawerToggle mDrawerToggle;



    public BackgroundTabListView(RecyclerView slidingLayout,WebViewActivity activity) {
        Activity=activity;
        backTabList =slidingLayout;
        layout = Activity.findViewById(R.id.drawerly);

    }







    //this method added a title to the titles  array list which is taken by an adapter to show urls at the nav drawer
    //return true if adding successes
    public void addTBackTabListItem(Browser backTabImage){
        adapter.addBackTabItem(backTabImage);
        backTabList.scrollToPosition(backGroundTabsHandler.BackGroundTabsList.size()-1);



    }


public void setup(DrawerLayout drawerLayout){
    itemTouchHelper.attachToRecyclerView(backTabList);
    adapter = new RecyclerViewAdapter(Activity, backTabList);
    LinearLayoutManager verticalLayoutManagaer
            = new LinearLayoutManager(Activity, LinearLayoutManager.VERTICAL, false);
    backTabList.setLayoutManager(verticalLayoutManagaer);
    backTabList.addItemDecoration(new SpaceDicoration(0));
    backTabList.setAdapter(adapter);
    backTabList.addOnItemTouchListener(new RecyclerTouchListener(Activity, backTabList, new ClickListener() {
        @Override
        public void onClick(View view, int position) {
    adapter.setPosition(position);
    Activity.BackgroundTab.OpenBackgroundTab(position,false);




        }

        @Override
        public void onLongClick(View view, int position) {

        }
    }));
    containerView = Activity.findViewById(R.id.nav_view);
    mDrawerLayout = drawerLayout;







}



    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT| ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            //Remove swiped item from list and notify the RecyclerView
                adapter.delete(viewHolder.getLayoutPosition());
                Activity.BackgroundTab.delTab(viewHolder.getLayoutPosition());



        }


    };

    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }


    }
    public class SpaceDicoration extends RecyclerView.ItemDecoration {

        private  int space;

        public SpaceDicoration(int mVerticalSpaceHeight) {

            this.space=mVerticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            if(space==0) {
                float resultPix = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,380,Activity.getResources().getDisplayMetrics());
                space = (int) ((parent.getHeight()-resultPix) / 2);
            }
           // outRect.top = space;
           outRect.right=10;
            outRect.left =10;

            //outRect.top=space;
        }
    }


    public void returnToHomeLayout(){

        if(backTabList.getAdapter().getItemCount()!=0)
            Activity.BackgroundTab.showHomeFragment(false);
        else mDrawerLayout.closeDrawer(containerView);

    }


}