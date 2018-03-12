package com.hassan.masla7ty.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.hassan.masla7ty.R;
import com.hassan.masla7ty.pojo.Stations;
import com.hassan.masla7ty.pojo.Government;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class GovernStackWidget extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GovernViewsRemoteFactory(this.getApplicationContext(), intent);
    }
}

class GovernViewsRemoteFactory implements RemoteViewsService.RemoteViewsFactory {
    private ArrayList<Government> mGovernments;
    private Context mContext;

    public GovernViewsRemoteFactory(Context context, Intent intent) {
        mContext = context;
    }

    public void onCreate() {
    }

    public void onDestroy() {
        mGovernments.clear();
    }

    public int getCount() {
        if (mGovernments == null)
            return 0;

        return mGovernments.size();
    }

    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);

        Government government = mGovernments.get(position);

        rv.setTextViewText(R.id.widget_item_govern_name, government.getName());
        StringBuilder sb = new StringBuilder();
        String name;double quantity;
        for (Stations stations : government.getStations()) {

             name = stations.getSpecifications();
             quantity = stations.getArea();


            sb.append("\n");
            sb.append(name + " - " + quantity );
        }

        rv.setTextViewText(R.id.widget_item_stations,sb.toString());

        Bundle extras = new Bundle();
        extras.putParcelable(mContext.getString(R.string.extra_recipe), government);
        Intent fillIntent = new Intent();
        fillIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.recipe_widget_item,fillIntent);

        return rv;
    }

    public RemoteViews getLoadingView() {
        return null;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public long getItemId(int position) {
        return position;
    }

    public boolean hasStableIds() {
        return true;
    }

    public void onDataSetChanged() {
        if(mGovernments == null) {


            final String UDACITY_BASE_URL_MOVIE = "http://finalmasla7ty.esy.es/app/government.json";
            Uri builtUri = Uri.parse(UDACITY_BASE_URL_MOVIE);

            HttpURLConnection urlConnection = null;
            try {
                // get URL
                URL url = new URL(builtUri.toString());
                // open connection
                urlConnection = (HttpURLConnection) url.openConnection();

                // create an input stream reader
                InputStreamReader reader = new InputStreamReader(urlConnection.getInputStream());

                Government[] governmentArray = new Gson().fromJson(reader, Government[].class);
                mGovernments = new ArrayList<>(Arrays.asList(governmentArray));

            } catch (MalformedURLException e) {
                Log.e("MalformedURLException", e.getMessage());
            } catch (IOException e) {
                Log.e("IOException", e.getMessage());
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }
    }
}
