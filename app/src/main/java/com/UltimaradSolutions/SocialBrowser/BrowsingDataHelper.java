package com.UltimaradSolutions.SocialBrowser;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import static com.UltimaradSolutions.SocialBrowser.DBAdapter.COL_PageUrl;
import static com.UltimaradSolutions.SocialBrowser.DBAdapter.COL_Time;
import static com.UltimaradSolutions.SocialBrowser.DBAdapter.KEY_PAGENAME;
import static com.UltimaradSolutions.SocialBrowser.DBAdapter.KEY_PAGEURL;
import static com.UltimaradSolutions.SocialBrowser.DBAdapter.KEY_Time;

class BrowsingDataHelper {
     ListAdapter Adapter;
    private WebViewActivity mainActivity;
    ListView List;
    private ListAdapter.PlanetFilter planetFilter;
    private TextView loadMore;
    private ArrayList<HashMap<String, String>> LinksList;
    private DBAdapter Db;
    private int historyCounts;

    BrowsingDataHelper(WebViewActivity mainActivity,DBAdapter db) {
        LinksList = new ArrayList<>();
        this.mainActivity = mainActivity;
Db=db;


    }

    private void setOnLoadMoreClick(TextView loadMore) {
        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FillListFromDB(historyCounts-12);
            }
        });
    }



    private void UpdateAdapter() {

        Adapter = new ListAdapter(
                mainActivity,
                R.layout.history_list_item, FillListFromDB(0));
    }
// 3shan ye3ml load all momken long click
     private ArrayList<HashMap<String, String>> FillListFromDB(int pos) {
        HashMap<String, String> Link;
        String PageName, PageURL,date;
        Cursor cursor = Db.getAllRows();
        if(historyCounts==0) historyCounts=cursor.getCount();
        if(pos==0){
            cursor.moveToLast();
            pos=cursor.getPosition();
        }
         int i=0;
        if (cursor.moveToPosition(pos)) {

            do {

                PageName = cursor.getString(DBAdapter.COL_PAGEName);
                PageURL = cursor.getString(COL_PageUrl);
                date = cursor.getString(COL_Time);
                Link = fillTheHashMab(PageName, PageURL, date);
                if(!PageURL.equals("b")) LinksList.add(Link);
                else{
                    if(date.equals(mainActivity.currentDate)) LinksList.add(0,Link);
                    else LinksList.add(Link);
                }

                boolean isThereAnotherData=cursor.moveToPrevious();
                if(i==12&&isThereAnotherData) {loadMore.setVisibility(View.VISIBLE);break;}
                if(!isThereAnotherData)loadMore.setVisibility(View.GONE);
                cursor.moveToNext();
                i++;
            } while (cursor.moveToPrevious());
        }
        cursor.close();
         historyCounts=historyCounts-12;
if(Adapter !=null)  Adapter.notifyDataSetChanged();
        return LinksList;
    }

    private HashMap<String, String> fillTheHashMab(String pageName, String pageURL, String date) {
        HashMap<String, String> Link = new HashMap<>();
        Link.put("name", pageName);
        Link.put("url", pageURL);
        Link.put("date", date);
        return Link;
    }

    private void FillListView(final ListView historyList) {
        historyList.setAdapter(Adapter);
        historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = ((TextView) view.findViewById(R.id.LinkUrl)).getText().toString();
                    RecyclerViewAdapter adapter = mainActivity.BackgroundTab.backgroundTabListView.adapter;
                    mainActivity.BackgroundTab.createBackroundTab(url);
                    mainActivity.BackTabLayout.setVisibility(View.VISIBLE);
                    adapter.position = backGroundTabsHandler.BackGroundTabsList.size() - 1;
                    mainActivity.ContainerLayout.getChildAt(0).findViewById(R.id.backHistory).callOnClick();
                    mainActivity.BackgroundTab.OpenBackgroundTab(adapter.position, true);


            }
        });


    }



    private void resetAdapter() {
        Adapter.getFilter().filter("");
    }

   void initialiseListView(View v) {
       List =v.findViewById(R.id.ListMenu);
      Close();
        List.setVisibility(View.VISIBLE);
       loadMore=new TextView(mainActivity);
        loadMore.setText(R.string.moree);
        loadMore.setTextSize(30);
        loadMore.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        loadMore.setGravity(Gravity.CENTER);
        List.addFooterView(loadMore);
        UpdateAdapter();
        FillListView(List);
       Adapter.notifyDataSetChanged();
        setOnLoadMoreClick(loadMore);
    }

    void Close() {
        LinksList.clear();

    }

    private class ListAdapter extends ArrayAdapter<HashMap<String, String>> implements Filterable {

        ArrayList<HashMap<String, String>> ListItems;
        ArrayList<HashMap<String, String>> OriginalhistoryListItems;


        private ListAdapter(Context context, int resource, ArrayList<HashMap<String, String>> ListItems) {
            super(context, resource, ListItems);
            this.ListItems = ListItems;
            this.OriginalhistoryListItems = ListItems;


        }



        public int getCount() {
            return ListItems.size();
        }

        public HashMap<String, String> getItem(int position) {
            return ListItems.get(position);
        }


        public long getItemId(int position) {
            return ListItems.get(position).hashCode();
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View v;

            HashMap<String, String> p = ListItems.get(position);

            if(p.get("name").equals("a,m,r")){
                v=new TextView(mainActivity);
                v.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50));
                v.setBackgroundColor(Color.GRAY);
                ((TextView)v).setTextColor(Color.BLACK);
                ((TextView)v).setText(p.get("date"));
            }


           else  {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.history_list_item, null);
                TextView NAme =  v.findViewById(R.id.LinkNAme);
                TextView Url =   v.findViewById(R.id.LinkUrl);


                if (NAme != null) {
                    NAme.setText(p.get("name"));
                }

                if (Url != null) {
                    Url.setText(p.get("url"));
                }

            }
            mainActivity.currentDate=p.get("date");


if(v.findViewById(R.id.Xbut)!=null)setOnRemoveItemClick(v.findViewById(R.id.Xbut));
            return v;
        }
        private void setOnRemoveItemClick(View DelBut) {
            DelBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(List.getPositionForView((View) v.getParent())!=AdapterView.INVALID_POSITION)

                    removeItem(List,getPosition(ListItems.get(List.getPositionForView((View) v.getParent()))));

                 //   else  removeItem(MainHistoryList,getPosition(ListItems.get(MainHistoryList.getPositionForView((View) v.getParent()))));



                    notifyDataSetChanged();
                }
            });

        }
        private void removeItem(ListView List,int position) {
            View item = List.getChildAt(position);
            TextView urlText= item.findViewById(R.id.LinkUrl);
            remove(ListItems.get(position));
           Cursor c = Db.getRow(urlText.getText().toString());
            if (c!=null) {
                c.moveToFirst();
                Db.deleteRow(c.getLong(0));
            }
        }

        @NonNull
        @Override
        public Filter getFilter() {
            if (planetFilter == null)
                planetFilter = new PlanetFilter();

            return planetFilter;
        }


        private class PlanetFilter extends Filter {


            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                // We implement here the filter logic
                if (constraint == null || constraint.length() == 0) {
                    // No filter implemented we return all the list
                    results.values = OriginalhistoryListItems;
                    results.count = OriginalhistoryListItems.size();
                } else {
                    // We perform filtering operation
                    ArrayList<HashMap<String, String>> nHList = new ArrayList<>();

                    for (HashMap<String, String> h : ListItems) {
                        if (h.get("name").toUpperCase().startsWith(constraint.toString().toUpperCase()))
                            nHList.add(h);
                    }

                    results.values = nHList;
                    results.count = nHList.size();

                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                // Now we have to inform the adapter about the new list filtered
                if (results.count == 0)
                    notifyDataSetInvalidated();
                else {
                    ListItems = (ArrayList<HashMap<String, String>>) results.values;
                    notifyDataSetChanged();
                }

            }

        }

    }

    boolean AddDateRowToDb(String date) {
        long id = 0;
        if (mainActivity.HistoryIsEnabled || Db == mainActivity.BookmarksDB) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(KEY_PAGEURL, "b");
            initialValues.put(KEY_PAGENAME, "a,m,r");
            initialValues.put(KEY_Time, date);
            if (!Db.checkExistance("a,m,r", "b", date)) {
                id = Db.inseserttoDb(initialValues);
            }

        }
        return id != -1;

    }
}
