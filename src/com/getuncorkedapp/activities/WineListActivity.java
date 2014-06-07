package com.getuncorkedapp.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.getuncorkedapp.R;
import com.getuncorkedapp.application.ParseApp;
import com.getuncorkedapp.models.Wine;
import com.getuncorkedapp.models.WineAdapter;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class WineListActivity extends Activity
{

	private ListView wineList;
	private WineAdapter wineAdapter;
	public final static String WINE_ID_EXTRA = "com.getuncorkedapp.WINE_ID";
	public final String ALL_WINES = "AllWines";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wine_list);

		ParseAnalytics.trackAppOpened(getIntent());
		// ParseObject.registerSubclass( Wine.class );

		wineList = (ListView) findViewById(R.id.wineList);
		wineAdapter = new WineAdapter(this, new ArrayList<Wine>());
		wineList.setAdapter(wineAdapter);

		wineList.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
				int position, long id)
			{
				Wine wine = wineAdapter.getItem(position);
				Intent intent = new Intent().setClass(parent.getContext(),
					ViewWineActivity.class);
				intent.putExtra(WINE_ID_EXTRA, wine.getObjectId());
				Log.i("WineListActivity", wine.getObjectId());
				startActivity(intent);
			}

		});

		fetchData();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.wine_list_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle presses on the action bar items
		switch (item.getItemId())
		{
		case R.id.action_add:
			Intent intent = new Intent().setClass(getApplicationContext(),
				NewEntryActivity.class);
			startActivity(intent);
			return true;
		case R.id.userdisplay:
			ParseApp app = (ParseApp) getApplication();
			app.setUser(null);
			SharedPreferences userInfo = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
			Editor edit = userInfo.edit();
			edit.clear();
			edit.commit();
			Intent intent2 = new Intent().setClass(getApplicationContext(),
				LoginActivity.class);
			startActivity(intent2);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void fetchData()
	{

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Wine");
		query.findInBackground(new FindCallback<ParseObject>()
		{

			@Override
			public void done(List<ParseObject> wines, ParseException error)
			{
				if (wines != null)
				{
					wineAdapter.clear();
					for (ParseObject wine : wines)
					{
						wineAdapter.add((Wine) wine);
						Log.i("WineListActivity", wine.getString("name"));
					}
					// remove previously stored wines
					ParseObject.unpinAllInBackground(ALL_WINES);
					// store local cache
					ParseObject.pinAllInBackground(ALL_WINES, wines);
				}
				else
				{
					Log.e("WineListActivity", error.getLocalizedMessage());
					return;
				}
			}
		});

	}
}
