package yesteam.code4pilar2015.activities;

import android.content.Intent;
import android.database.Cursor;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import yesteam.code4pilar2015.R;
import yesteam.code4pilar2015.adapters.EventsAdapter;
import yesteam.code4pilar2015.helpers.EmptyRecyclerView;
import yesteam.code4pilar2015.provider.DatabaseProvider;

public class EventsListActivity extends AppCompatActivity implements EventsAdapter.OnItemClickEventListener, LoaderManager.LoaderCallbacks<Cursor>, TabLayout.OnTabSelectedListener, GestureOverlayView.OnGesturePerformedListener {

    private EventsAdapter adapter;
    private TabLayout tabLayout;

    private TextView txtEmpty;

    private MaterialSearchView searchView;

    private GestureLibrary gestureLibrary;

    private static final String GESTURE_RIGHT = "right";
    private static final String GESTURE_LEFT = "left";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Bundle extras = new Bundle();
                extras.putString("name", query);
                extras.putInt("category", (int) tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getTag());
                getSupportLoaderManager().restartLoader(0, extras, EventsListActivity.this);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Bundle extras = new Bundle();
                extras.putString("name", newText);
                extras.putInt("category", (int) tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getTag());
                getSupportLoaderManager().restartLoader(0, extras, EventsListActivity.this);
                return false;
            }
        });
        searchView.setVoiceSearch(true);

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_favorites_title).setTag(-99));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_all_title).setTag(-1));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_childhood_title).setTag(-2));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_youth_title).setTag(-3));
        tabLayout.setOnTabSelectedListener(this);

        txtEmpty = (TextView) findViewById(R.id.empty);
        EmptyRecyclerView mRecyclerView = (EmptyRecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setEmptyView(txtEmpty);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        adapter = new EventsAdapter(EventsListActivity.this, null, this);
        mRecyclerView.setAdapter(adapter);

        int code = getIntent().getIntExtra("category_code", 0);
        new CreateTabs(code).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        Bundle extras = new Bundle();
        extras.putInt("category", -1);
        getSupportLoaderManager().restartLoader(0, extras, this);

        GestureOverlayView gestureOverlayView = (GestureOverlayView) findViewById(R.id.gestures);

        gestureLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
        gestureLibrary.load();

        gestureOverlayView.addOnGesturePerformedListener(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_events_list, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                EventsListActivity.this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        getSupportLoaderManager().destroyLoader(0);

        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String where = null;
        String[] whereArgs = null;

        int category = args.getInt("category", -1);
        String name = args.getString("name", "");


        if ((category >= 0) && (!TextUtils.isEmpty(name))) {
            where = DatabaseProvider.EventsTable.COLUMN_CATEGORY_CODE + "=? AND " + DatabaseProvider.EventsTable.COLUMN_TITLE + " like ?";
            whereArgs = new String[]{String.valueOf(category), "%" + name + "%"};

        } else if ((category == -99) && (!TextUtils.isEmpty(name))) {
            where = DatabaseProvider.EventsTable.COLUMN_FAVORITE + "=? AND " + DatabaseProvider.EventsTable.COLUMN_TITLE + " like ?";
            whereArgs = new String[]{"1", "%" + name + "%"};

        } else if ((category < -1) && (!TextUtils.isEmpty(name))) {
            where = DatabaseProvider.EventsTable.COLUMN_POPULATION_TYPE + "=? AND " + DatabaseProvider.EventsTable.COLUMN_TITLE + " like ?";
            whereArgs = new String[]{String.valueOf(Math.abs(category + 1)), "%" + name + "%"};

        } else if (category >= 0) {
            where = DatabaseProvider.EventsTable.COLUMN_CATEGORY_CODE + "=?";
            whereArgs = new String[]{String.valueOf(category)};

        } else if (category == -99) {
            where = DatabaseProvider.EventsTable.COLUMN_FAVORITE + "=?";
            whereArgs = new String[]{"1"};

        } else if (category < -1) {
            where = DatabaseProvider.EventsTable.COLUMN_POPULATION_TYPE + "=?";
            whereArgs = new String[]{String.valueOf(Math.abs(category + 1))};

        } else if (!TextUtils.isEmpty(name)) {
            where = DatabaseProvider.EventsTable.COLUMN_TITLE + " like ?";
            whereArgs = new String[]{"%" + name + "%"};
        }

        return new CursorLoader(EventsListActivity.this, DatabaseProvider.EventsTable.URI, null,
                where, whereArgs,
                DatabaseProvider.EventsTable.COLUMN_END_DATE + " ASC, " + DatabaseProvider.EventsTable.COLUMN_START_HOUR + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor newCursor) {
        adapter.changeCursor(newCursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.changeCursor(null);
    }

    @Override
    public void onItemClickEvent(Cursor cursor) {
        Intent iEvent = new Intent(EventsListActivity.this, DetailEventActivity.class);
        iEvent.putExtra("event-code", cursor.getInt(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_CODE)));
        startActivity(iEvent);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Bundle extras = new Bundle();
        extras.putInt("category", (int) tab.getTag());
        getSupportLoaderManager().restartLoader(0, extras, this);

        if (tab.getPosition() == 0) {
            txtEmpty.setText(R.string.empty_favorites);
        } else {
            txtEmpty.setText(R.string.empty);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        ArrayList<Prediction> predictions = gestureLibrary.recognize(gesture);
        if (predictions.size() > 0) {
            Prediction prediction = predictions.get(0);
            if (prediction.score > 1.0) {
                if (GESTURE_RIGHT.equalsIgnoreCase(prediction.name)) {
                    int selectedTab = tabLayout.getSelectedTabPosition();
                    if (selectedTab > 0) {
                        tabLayout.getTabAt(selectedTab - 1).select();
                    }
                } else if (GESTURE_LEFT.equalsIgnoreCase(prediction.name)) {
                    int selectedTab = tabLayout.getSelectedTabPosition();
                    if (selectedTab < tabLayout.getTabCount() - 1) {
                        tabLayout.getTabAt(selectedTab + 1).select();
                    }
                }
            }
        }
    }

    private class CreateTabs extends AsyncTask<Void, Void, Cursor> {

        private int categorySelected;

        public CreateTabs(int categorySelected) {
            this.categorySelected = categorySelected;
        }

        @Override
        protected Cursor doInBackground(Void... params) {
            return getContentResolver().query(DatabaseProvider.CategoriesTable.URI, null, null, null, DatabaseProvider.CategoriesTable.COLUMN_TITLE + " ASC");
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);

            int pos = 1;
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex(DatabaseProvider.CategoriesTable.COLUMN_TITLE));
                int code = cursor.getInt(cursor.getColumnIndex(DatabaseProvider.CategoriesTable.COLUMN_CODE));
                tabLayout.addTab(tabLayout.newTab().setText(title).setTag(code));

                if (code == categorySelected) {
                    pos = cursor.getPosition() + 4;
                }
            }
            cursor.close();

            if (tabLayout.getTabCount() > 4) {
                tabLayout.getTabAt(pos).select();

                final int finalPos = pos;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int left = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(finalPos).getLeft();
                        int offset = tabLayout.getWidth() / 4;
                        tabLayout.scrollTo(left - offset, 0);
                    }
                }, 100);

            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new CreateTabs(categorySelected).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }, 1000);
            }
        }
    }
}
