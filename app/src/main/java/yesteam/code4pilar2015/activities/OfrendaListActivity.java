package yesteam.code4pilar2015.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import yesteam.code4pilar2015.R;
import yesteam.code4pilar2015.adapters.ESArrayAdapter;
import yesteam.code4pilar2015.adapters.OfrendaAdapter;
import yesteam.code4pilar2015.items.OfrendaItem;
import yesteam.code4pilar2015.provider.DatabaseProvider;

public class OfrendaListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofrenda_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        OfrendaAdapter adapter = new OfrendaAdapter(getApplicationContext(), generateData());

        final ListView lv = (ListView) findViewById(R.id.groups_list);
        final AutoCompleteTextView tvGroup = (AutoCompleteTextView) findViewById(R.id.groups_search);

        final ESArrayAdapter autoAdapter = new ESArrayAdapter(getBaseContext(), R.layout.autocomplete_item, getAllGroupNames());
        tvGroup.setAdapter(autoAdapter);

        lv.setAdapter(adapter);

        ImageButton btnSearch = (ImageButton) findViewById(R.id.groups_btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filter = tvGroup.getText().toString();
                OfrendaAdapter adapter;
                if (filter.equals("")) {
                    adapter = new OfrendaAdapter(getApplicationContext(), generateData());
                } else {
                    adapter = new OfrendaAdapter(getApplicationContext(), generateData(filter));
                }
                lv.setAdapter(adapter);
            }
        });
    }

    private ArrayList<OfrendaItem> generateData() {

        String[] projection = new String[] {DatabaseProvider.OfrendaTable.COLUMN_ID, DatabaseProvider.OfrendaTable.COLUMN_NAME, DatabaseProvider.OfrendaTable.COLUMN_TIME, DatabaseProvider.OfrendaTable.COLUMN_ACCESS};
        Cursor cursor = getContentResolver().query(DatabaseProvider.OfrendaTable.URI, projection, null, null, null);

        ArrayList<OfrendaItem> items = new ArrayList<>();

        OfrendaItem ofrendaItem = new OfrendaItem(-1, "Nombre", "Hora", -1);
        items.add(ofrendaItem);

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseProvider.OfrendaTable.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(DatabaseProvider.OfrendaTable.COLUMN_NAME));
            String meet_hour = cursor.getString(cursor.getColumnIndex(DatabaseProvider.OfrendaTable.COLUMN_TIME));
            int access = cursor.getInt(cursor.getColumnIndex(DatabaseProvider.OfrendaTable.COLUMN_ACCESS));

            ofrendaItem = new OfrendaItem(id, name, meet_hour, access);
            items.add(ofrendaItem);
        }
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseProvider.OfrendaTable.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(DatabaseProvider.OfrendaTable.COLUMN_NAME));
            String meet_hour = cursor.getString(cursor.getColumnIndex(DatabaseProvider.OfrendaTable.COLUMN_TIME));
            int access = cursor.getInt(cursor.getColumnIndex(DatabaseProvider.OfrendaTable.COLUMN_ACCESS));

            ofrendaItem = new OfrendaItem(id, name, meet_hour, access);
            items.add(ofrendaItem);
        }
        return items;
    }

    private ArrayList<OfrendaItem> generateData(String search) {

        String filter = DatabaseProvider.OfrendaTable.COLUMN_NAME + " LIKE '%" + search + "%'";

        String[] projection = new String[] {DatabaseProvider.OfrendaTable.COLUMN_ID, DatabaseProvider.OfrendaTable.COLUMN_NAME, DatabaseProvider.OfrendaTable.COLUMN_TIME, DatabaseProvider.OfrendaTable.COLUMN_ACCESS};
        Cursor cursor = getContentResolver().query(DatabaseProvider.OfrendaTable.URI, projection, filter, null, DatabaseProvider.OfrendaTable.COLUMN_NAME);

        ArrayList<OfrendaItem> items = new ArrayList<>();

        OfrendaItem ofrendaItem = new OfrendaItem(-1, "Nombre", "Hora", -1);
        items.add(ofrendaItem);

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseProvider.OfrendaTable.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(DatabaseProvider.OfrendaTable.COLUMN_NAME));
            String meet_hour = cursor.getString(cursor.getColumnIndex(DatabaseProvider.OfrendaTable.COLUMN_TIME));
            int access = cursor.getInt(cursor.getColumnIndex(DatabaseProvider.OfrendaTable.COLUMN_ACCESS));

            ofrendaItem = new OfrendaItem(id, name, meet_hour, access);
            items.add(ofrendaItem);
        }
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseProvider.OfrendaTable.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(DatabaseProvider.OfrendaTable.COLUMN_NAME));
            String meet_hour = cursor.getString(cursor.getColumnIndex(DatabaseProvider.OfrendaTable.COLUMN_TIME));
            int access = cursor.getInt(cursor.getColumnIndex(DatabaseProvider.OfrendaTable.COLUMN_ACCESS));

            ofrendaItem = new OfrendaItem(id, name, meet_hour, access);
            items.add(ofrendaItem);
        }

        return items;
    }

    private String[] getAllGroupNames() {
        String[] projection = new String[] {DatabaseProvider.OfrendaTable.COLUMN_ID, DatabaseProvider.OfrendaTable.COLUMN_NAME};
        Cursor cursor = getContentResolver().query(DatabaseProvider.OfrendaTable.URI, projection, null, null, null);

        ArrayList<String> items = new ArrayList<>();

        if (cursor.moveToFirst()) {
            items.add(cursor.getString(cursor.getColumnIndex(DatabaseProvider.OfrendaTable.COLUMN_NAME)));
        }
        while (cursor.moveToNext()) {
            items.add(cursor.getString(cursor.getColumnIndex(DatabaseProvider.OfrendaTable.COLUMN_NAME)));
        }
        String[] strList = new String[items.size()];
        strList = items.toArray(strList);
        return strList;
    }
}
