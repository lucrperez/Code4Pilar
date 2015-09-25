package yesteam.code4pilar2015.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import yesteam.code4pilar2015.R;
import yesteam.code4pilar2015.adapters.EventsAdapter;
import yesteam.code4pilar2015.provider.DatabaseProvider;
import yesteam.code4pilar2015.services.DownloadEvents;

public class EventsListActivity extends AppCompatActivity implements EventsAdapter.OnItemClickEventListener, LoaderManager.LoaderCallbacks<Cursor>, TabLayout.OnTabSelectedListener {

    private EventsAdapter adapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_all_title));
        tabLayout.setOnTabSelectedListener(this);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        adapter = new EventsAdapter(EventsListActivity.this, null, this);
        mRecyclerView.setAdapter(adapter);

        loadEvents();

        new CreateTabs().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected void onDestroy() {
        getSupportLoaderManager().destroyLoader(0);

        super.onDestroy();
    }

    private void loadEvents() {
        getSupportLoaderManager().restartLoader(0, null, this);

        startService(new Intent(EventsListActivity.this, DownloadEvents.class));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(EventsListActivity.this, DatabaseProvider.EventsTable.URI, null,
                null, null, DatabaseProvider.EventsTable.COLUMN_START_DATE + " ASC");
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

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    private class CreateTabs extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... params) {
            return getContentResolver().query(DatabaseProvider.CategoriesTable.URI, null, null, null, DatabaseProvider.CategoriesTable.COLUMN_TITLE + " ASC");
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);

            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex(DatabaseProvider.CategoriesTable.COLUMN_TITLE));
                int code = cursor.getInt(cursor.getColumnIndex(DatabaseProvider.CategoriesTable.COLUMN_CODE));
                tabLayout.addTab(tabLayout.newTab().setText(title).setTag(code));
            }
            cursor.close();
        }
    }
}
