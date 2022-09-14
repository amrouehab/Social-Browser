package com.UltimaradSolutions.SocialBrowser;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final FragmentManager manager;
        public List<WebFragment> mFragmentList = new ArrayList<>();
        public List<String> mFragmentTitleList = new ArrayList<>();
        WebViewActivity activity;
        // private long baseId = 0;
        @Override
        public int getItemPosition(Object object) {
           /* WebFragment fragment = (WebFragment)object;
            fragment.getId();
            int position = mFragmentList.indexOf(fragment);

            if (position >= 0) {
                return position;
            } else {*/
                return ViewPagerAdapter.POSITION_NONE;


        }
        public ViewPagerAdapter(FragmentManager manager,WebViewActivity activity) {
            super(manager);
            this.manager=manager;
this.activity=activity;
        }

        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(WebFragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        void removePage(int pos) {
            try {
               manager.beginTransaction().detach(mFragmentList.get(pos)).commit();
               manager.beginTransaction().remove(mFragmentList.get(pos)).commit();
                mFragmentList.remove(pos);
                mFragmentTitleList.remove(pos);
                notifyDataSetChanged();
            } catch (Exception e) {
                e.getMessage();
                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
            }
        }

        public void ClearAll() {
            mFragmentTitleList.clear();
            mFragmentList.clear();
            notifyDataSetChanged();

        }
    }

