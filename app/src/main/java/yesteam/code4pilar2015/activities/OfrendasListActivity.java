package yesteam.code4pilar2015.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import yesteam.code4pilar2015.R;
import yesteam.code4pilar2015.adapters.OfrendasAdapter;
import yesteam.code4pilar2015.helpers.EmptyRecyclerView;
import yesteam.code4pilar2015.provider.DatabaseProvider;

public class OfrendasListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private OfrendasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofrendas_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView txtEmpty = (TextView) findViewById(R.id.empty);
        EmptyRecyclerView mRecyclerView = (EmptyRecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setEmptyView(txtEmpty);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        adapter = new OfrendasAdapter(null);
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
        return new CursorLoader(OfrendasListActivity.this, DatabaseProvider.OfrendaTable.URI, null, null, null,
                DatabaseProvider.OfrendaTable.COLUMN_ACCESS + " ASC, " + DatabaseProvider.OfrendaTable.COLUMN_TIME + " ASC, " + DatabaseProvider.OfrendaTable.COLUMN_NAME + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.changeCursor(null);
    }
}
