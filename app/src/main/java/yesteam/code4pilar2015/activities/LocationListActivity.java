package yesteam.code4pilar2015.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import yesteam.code4pilar2015.R;
import yesteam.code4pilar2015.adapters.EventsAdapter;
import yesteam.code4pilar2015.helpers.EmptyRecyclerView;
import yesteam.code4pilar2015.provider.DatabaseProvider;

public class LocationListActivity extends AppCompatActivity implements EventsAdapter.OnItemClickEventListener, LoaderManager.LoaderCallbacks<Cursor> {

    String placeCode;

    private EventsAdapter adapter;
    TextView txtEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);

        Intent thisIntent = getIntent();
        placeCode = thisIntent.getStringExtra("place-code");
        String placeName = thisIntent.getStringExtra("place-name");

        setTitle(placeName);

        txtEmpty = (TextView) findViewById(R.id.location_empty);
        EmptyRecyclerView mRecyclerView = (EmptyRecyclerView) findViewById(R.id.location_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setEmptyView(txtEmpty);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        adapter = new EventsAdapter(LocationListActivity.this, null, this);
        mRecyclerView.setAdapter(adapter);
        getSupportLoaderManager().restartLoader(0, null, this);
    }

    @Override
    protected void onDestroy() {
        getSupportLoaderManager().destroyLoader(0);

        super.onDestroy();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String where = DatabaseProvider.EventsTable.COLUMN_PLACE_CODE + "=?";
        String[] whereArgs = new String[]{String.valueOf(placeCode)};

        return new CursorLoader(LocationListActivity.this, DatabaseProvider.EventsTable.URI, null,
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
        Intent iEvent = new Intent(LocationListActivity.this, DetailEventActivity.class);
        iEvent.putExtra("event-code", cursor.getInt(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_CODE)));
        startActivity(iEvent);
    }
}
