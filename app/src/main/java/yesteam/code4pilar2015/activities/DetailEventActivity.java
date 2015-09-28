package yesteam.code4pilar2015.activities;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import yesteam.code4pilar2015.R;
import yesteam.code4pilar2015.helpers.SquareImageView;
import yesteam.code4pilar2015.provider.DatabaseProvider;

public class DetailEventActivity extends AppCompatActivity implements OnMapReadyCallback {

    CollapsingToolbarLayout toolbarLayout;
    SquareImageView image;
    TextView textDescription, textCategory, textDate, textPrice, textLocation, textOther;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        image = (SquareImageView) findViewById(R.id.image);
        textDescription = (TextView) findViewById(R.id.text_description);
        textCategory = (TextView) findViewById(R.id.text_category);
        textDate = (TextView) findViewById(R.id.text_date);
        textPrice = (TextView) findViewById(R.id.text_price);
        textLocation = (TextView) findViewById(R.id.text_location);
        textOther = (TextView) findViewById(R.id.text_other);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        int code = getIntent().getIntExtra("event-code", 0);
        new GetEventData().execute(code);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

    private class GetEventData extends AsyncTask<Integer, Void, Cursor[]> {

        @Override
        protected Cursor[] doInBackground(Integer... params) {
            Cursor eventCursor = getContentResolver().query(DatabaseProvider.EventsTable.URI, null, DatabaseProvider.EventsTable.COLUMN_CODE + "=?", new String[]{String.valueOf(params[0])}, null);

            eventCursor.moveToFirst();
            String placeCode = eventCursor.getString(eventCursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_PLACE_CODE));
            int categoryCode = eventCursor.getInt(eventCursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_CATEGORY_CODE));

            Cursor placeCursor = getContentResolver().query(DatabaseProvider.PlacesTable.URI, null, DatabaseProvider.PlacesTable.COLUMN_CODE + "=?", new String[]{placeCode}, null);
            Cursor categoryCursor = getContentResolver().query(DatabaseProvider.CategoriesTable.URI, null, DatabaseProvider.CategoriesTable.COLUMN_CODE + "=?", new String[]{String.valueOf(categoryCode)}, null);

            return new Cursor[]{eventCursor, placeCursor, categoryCursor};
        }

        @Override
        protected void onPostExecute(Cursor[] cursors) {
            super.onPostExecute(cursors);

            Cursor eventCursor = cursors[0];
            Cursor placeCursor = cursors[1];
            Cursor categoryCursor = cursors[2];

            eventCursor.moveToFirst();
            placeCursor.moveToFirst();
            categoryCursor.moveToFirst();

            String title = eventCursor.getString(eventCursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_TITLE));
            toolbarLayout.setTitle(title);
            getSupportActionBar().setTitle(title);

            String imagePath = "http:" + eventCursor.getString(eventCursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_IMAGE));
            Picasso.with(DetailEventActivity.this).load(imagePath).placeholder(R.drawable.placeholder).noFade().into(image);

            String description = eventCursor.getString(eventCursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_DESCRIPTION));
            textDescription.setText(description);

            String category = categoryCursor.getString(categoryCursor.getColumnIndex(DatabaseProvider.CategoriesTable.COLUMN_TITLE));
            textCategory.setText(category);

            String startDate = eventCursor.getString(eventCursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_START_DATE));
            String endDate = eventCursor.getString(eventCursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_END_DATE));
            String hours = eventCursor.getString(eventCursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_START_HOUR));
            textDate.setText(startDate + " - " + endDate + "\n" + hours);

            String price = eventCursor.getString(eventCursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_PRICE));
            textPrice.setText(price);

            String placeName = placeCursor.getString(placeCursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_TITLE));
            textLocation.setText(placeName);

            Double latitude = placeCursor.getDouble(placeCursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_LATITUDE));
            Double longitude = placeCursor.getDouble(placeCursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_LONGITUDE));
            LatLng location = new LatLng(latitude, longitude);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));


            eventCursor.close();
            placeCursor.close();
            categoryCursor.close();

        }
    }
}
