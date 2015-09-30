package yesteam.code4pilar2015.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import yesteam.code4pilar2015.R;
import yesteam.code4pilar2015.adapters.OfrendasAdapter;
import yesteam.code4pilar2015.helpers.EmptyRecyclerView;
import yesteam.code4pilar2015.provider.DatabaseProvider;

public class OfrendasListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private OfrendasAdapter adapter;
    MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofrendas_list);

        /* BEGIN Search engine */

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Bundle extras = new Bundle();
                extras.putString("name", query);
                getSupportLoaderManager().restartLoader(0, extras, OfrendasListActivity.this);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Bundle extras = new Bundle();
                extras.putString("name", newText);
                getSupportLoaderManager().restartLoader(0, extras, OfrendasListActivity.this);
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {
            }
        });

        searchView.setVoiceSearch(true);

        /* END Search Engine */

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
    protected void onDestroy() {
        getSupportLoaderManager().destroyLoader(0);

        super.onDestroy();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String where = null;
        String name = null;

        if (args != null) {
            name = args.getString("name");
        }
        if (null != name && !"".equals(name)) {
            where = DatabaseProvider.OfrendaTable.COLUMN_NAME + " like '%" + name + "%'";
        }

        return new CursorLoader(OfrendasListActivity.this, DatabaseProvider.OfrendaTable.URI, null, where, null,
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_ofrenda_list, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
}
