package yesteam.code4pilar2015.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.TextView;

import yesteam.code4pilar2015.R;
import yesteam.code4pilar2015.adapters.EventsAdapter;
import yesteam.code4pilar2015.helpers.EmptyRecyclerView;
import yesteam.code4pilar2015.provider.DatabaseProvider;

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
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_all_title).setTag(-1));
        tabLayout.setOnTabSelectedListener(this);

        TextView txtEmpty = (TextView) findViewById(R.id.empty);
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
    }

    @Override
    protected void onDestroy() {
        getSupportLoaderManager().destroyLoader(0);

        super.onDestroy();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String where = null;
        String[] whereArgs = null;

        int code = args.getInt("category", -1);

        if (code >= 0) {
            where = DatabaseProvider.EventsTable.COLUMN_CATEGORY_CODE + "=?";
            whereArgs = new String[]{String.valueOf(code)};
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
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
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

            int pos = 0;
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex(DatabaseProvider.CategoriesTable.COLUMN_TITLE));
                int code = cursor.getInt(cursor.getColumnIndex(DatabaseProvider.CategoriesTable.COLUMN_CODE));
                tabLayout.addTab(tabLayout.newTab().setText(title).setTag(code));

                if (code == categorySelected) {
                    pos = cursor.getPosition() + 1;
                }
            }

            tabLayout.getTabAt(pos).select();

            final int finalPos = pos;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int left = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(finalPos).getLeft();
                    tabLayout.scrollTo(left, 0);
                }
            }, 100);

            cursor.close();
        }
    }
}
