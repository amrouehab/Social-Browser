package com.UltimaradSolutions.SocialBrowser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;


public class BrowsingDataFragment extends SettingsFragment {

    int fragmentToShow;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        fragmentToShow = getArguments().getInt("fragment");
        View Layout=null;
        switch (fragmentToShow) {
            case 0: {
                Layout = inflater.inflate(R.layout.history_layout, null);
                Switch sWitch = Layout.findViewById(R.id.switch3);//NC
                if (((WebViewActivity) getActivity()).HistoryIsEnabled) sWitch.setChecked(true);
                setupHistoryLyCompnents(sWitch.isChecked(),Layout);
                final View finalLayout = Layout;
                sWitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                      @Override
                                                      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                          ((WebViewActivity) getActivity()).notifyHistoryIsEnabled(isChecked);
                                                          setupHistoryLyCompnents(isChecked, finalLayout);

                                                      }
                                                  }
                );
                break;
            }
            case 1: {
                BrowsingDataHelper BookmrksHelper;
                Layout = inflater.inflate(R.layout.bookmarks_ly, null);
                BookmrksHelper = new BrowsingDataHelper(((WebViewActivity) getActivity()), ((WebViewActivity) getActivity()).BookmarksDB);
                BookmrksHelper.AddDateRowToDb(((WebViewActivity) getActivity()).currentDate);
                BookmrksHelper.initialiseListView(Layout);
                break;
            }
        }
        backOnclick(Layout);
    return Layout;
    }



    private void setupHistoryLyCompnents(boolean isChecked, View Layout) {
        ListView list=Layout.findViewById(R.id.ListMenu);
        EditText historySeach = Layout.findViewById(R.id.SearchText);
        TextView notifier = Layout.findViewById(R.id.textView21);
        if ((isChecked)) {
            historySeach.setVisibility(View.VISIBLE);
            notifier.setVisibility(View.GONE);
            ((WebViewActivity) getActivity()).historyHelper.initialiseListView(Layout);
        } else {
            historySeach.setVisibility(View.GONE);
            notifier.setVisibility(View.VISIBLE);
            notifier.bringToFront();
            ((WebViewActivity) getActivity()).historyHelper.Close();
            if(list!=null)list.setVisibility(View.GONE);

        }

    }

}