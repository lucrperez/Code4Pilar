package yesteam.code4pilar2015.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import yesteam.code4pilar2015.R;
import yesteam.code4pilar2015.adapters.CategoriesAdapter;
import yesteam.code4pilar2015.helpers.EmptyRecyclerView;
import yesteam.code4pilar2015.provider.DatabaseProvider;

public class CategoriesListActivity extends AppCompatActivity implements CategoriesAdapter.OnItemClickEventListener, LoaderManager.LoaderCallbacks<Cursor> {

    private CategoriesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView txtEmpty = (TextView) findViewById(R.id.empty);
        EmptyRecyclerView mRecyclerView = (EmptyRecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setEmptyView(txtEmpty);

        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(mLayoutManager);

        adapter = new CategoriesAdapter(CategoriesListActivity.this, null, this);
        mRecyclerView.setAdapter(adapter);

        getSupportLoaderManager().restartLoader(0, null, this);
    }

    @Override
    protected void onDestroy() {
        getSupportLoaderManager().destroyLoader(0);

        super.onDestroy();
    }

    @Override
    public void onItemClickEvent(Cursor cursor) {
        Intent iListCategory = new Intent(CategoriesListActivity.this, EventsListActivity.class);
        iListCategory.putExtra("category_code", cursor.getInt(cursor.getColumnIndex(DatabaseProvider.CategoriesTable.COLUMN_CODE)));
        startActivity(iListCategory);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(CategoriesListActivity.this, DatabaseProvider.CategoriesTable.URI, null,
                null, null, DatabaseProvider.CategoriesTable.COLUMN_TITLE + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.changeCursor(null);
    }

}
