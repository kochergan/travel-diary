package com.traveldiary.controller;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TravelDiary extends ListActivity {
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;

	private static final int TAKE_LOCATION_ID = Menu.FIRST;
	private static final int DELETE_LOCATION_ID = Menu.FIRST + 1;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, TAKE_LOCATION_ID, 0, R.string.menu_take);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO react on selected menu items
		return super.onOptionsItemSelected(item);
	}

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case TAKE_LOCATION_ID:
                takeLocation();
                return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }

	private void takeLocation() {
		Intent i = new Intent(this, LocationEdit.class);
		startActivityForResult(i, ACTIVITY_CREATE);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.locations_list);
		String[] locations = getResources().getStringArray(
				R.array.locations_array);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.location_item,
				locations));
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		registerForContextMenu(lv);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_LOCATION_ID, 0, R.string.menu_delete);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DELETE_LOCATION_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			// TODO delete location
			return true;
		}
		return super.onContextItemSelected(item);
	}

	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, LocationEdit.class);
        //TODO store information in intent to show location details
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Bundle extras = intent.getExtras();
        switch(requestCode) {
            case ACTIVITY_CREATE:
            	//TODO create new location
                break;
            case ACTIVITY_EDIT:
                //TODO update location with new notes and picture, then refresh
                break;
        }
    }
}