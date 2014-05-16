package com.getuncorkedapp.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.getuncorkedapp.R;
import com.getuncorkedapp.models.Wine;
import com.getuncorkedapp.models.WineAdapter;
import com.getuncorkedapp.utils.ParseKeys;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQuery.CachePolicy;

public class WineListActivity extends Activity {
	
	private ListView wineList;
	private WineAdapter wineAdapter;
	public final static String WINE_ID_EXTRA = "com.getuncorkedapp.WINE_ID";
	public final String ALL_WINES = "allWines";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wine_list);
        
        Parse.initialize(this, ParseKeys.APPID, ParseKeys.CLIENTKEY);
        ParseAnalytics.trackAppOpened( getIntent() );
        ParseObject.registerSubclass( Wine.class );
        
        wineList = (ListView) findViewById(R.id.wineList);
        wineAdapter = new WineAdapter(this, new ArrayList<Wine>() );
        wineList.setAdapter(wineAdapter);
        
        wineList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Wine wine = wineAdapter.getItem(position);
				Intent intent = new Intent().setClass(parent.getContext(), ViewWineActivity.class);
                intent.putExtra( WINE_ID_EXTRA, wine.getObjectId() );
                Log.i("WineListActivity", wine.getObjectId() );
                startActivity(intent);
			}
        	
        });
        
        fetchData();
		
	}

	public void fetchData() {
		
        ParseQuery<Wine> query = ParseQuery.getQuery(Wine.class);
        query.setCachePolicy(CachePolicy.CACHE_THEN_NETWORK);
        query.findInBackground(new FindCallback<Wine>() {

        	@Override
            public void done( List<Wine> wines, ParseException error ) {
                if(wines != null){
                    wineAdapter.clear();
                    wineAdapter.addAll(wines);
                } else {
                	Log.e("WineListActivity", error.getLocalizedMessage() );
                	return;
                }
            }
        });
        
		 
		
    }
}
