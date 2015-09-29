package yesteam.code4pilar2015.activities;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import yesteam.code4pilar2015.R;
import yesteam.code4pilar2015.helpers.SquareImageView;
import yesteam.code4pilar2015.provider.DatabaseProvider;

public class DetailEventActivity extends AppCompatActivity implements OnMapReadyCallback {

    private CollapsingToolbarLayout toolbarLayout;
    private SquareImageView image;
    private TextView textDescription, textCategory, textDate, textHour, textPrice, textLocationName, textLocationAddress, textLocationTelephone, textLocationEmail, textLocationAccessibility, textOtherWeb;
    private GoogleMap map;

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
        textHour = (TextView) findViewById(R.id.text_hour);
        textPrice = (TextView) findViewById(R.id.text_price);
        textLocationName = (TextView) findViewById(R.id.text_location_name);
        textLocationAddress = (TextView) findViewById(R.id.text_location_address);
        textLocationTelephone = (TextView) findViewById(R.id.text_location_telephone);
        textLocationEmail = (TextView) findViewById(R.id.text_location_email);
        textLocationAccessibility = (TextView) findViewById(R.id.text_location_accessibility);
        textOtherWeb = (TextView) findViewById(R.id.text_other_web);

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
        private SimpleDateFormat formatter;

        public GetEventData() {
            String bestPattern;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                bestPattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "MMMMd");
            } else {
                bestPattern = "d MMMM";
            }
            formatter = new SimpleDateFormat(bestPattern, Locale.getDefault());
        }

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

            String imagePath = "http:" + eventCursor.getString(eventCursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_IMAGE));
            Picasso.with(DetailEventActivity.this).load(imagePath).placeholder(R.drawable.placeholder).noFade().into(image);

            String description = eventCursor.getString(eventCursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_DESCRIPTION));
            textDescription.setText(description);

            String category = categoryCursor.getString(categoryCursor.getColumnIndex(DatabaseProvider.CategoriesTable.COLUMN_TITLE));
            textCategory.setText(getString(R.string.detail_text_category) + category);

            long startDate = eventCursor.getLong(eventCursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_START_DATE));
            long endDate = eventCursor.getLong(eventCursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_END_DATE));
            String hours = eventCursor.getString(eventCursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_START_HOUR));
            if (startDate > 0) {
                if (startDate - endDate == 0) {
                    textDate.setText(formatter.format(new Date(endDate)));
                } else {
                    textDate.setText(formatter.format(new Date(startDate)) + " - " + formatter.format(new Date(endDate)));
                }

            } else {
                textDate.setText(getString(R.string.date_until) + formatter.format(new Date(endDate)));
            }
            textHour.setText(getString(R.string.detail_text_hour) + hours);

            String price = eventCursor.getString(eventCursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_PRICE));
            if (!TextUtils.isEmpty(price)) {
                textPrice.setText(price);
            } else {
                textPrice.setText(R.string.price_free);
            }

            String placeName = placeCursor.getString(placeCursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_TITLE));
            textLocationName.setText(placeName);

            String placeAddress = placeCursor.getString(placeCursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_ADDRESS));
            if (!TextUtils.isEmpty(placeAddress)) {
                textLocationAddress.setText(placeAddress);
                textLocationAddress.setVisibility(View.VISIBLE);
            } else {
                textLocationAddress.setVisibility(View.GONE);
            }

            String placeTelephone = placeCursor.getString(placeCursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_TELEPHONE));
            if (!TextUtils.isEmpty(placeTelephone)) {
                textLocationTelephone.setText(getString(R.string.detail_text_location_telephone) + placeTelephone);
                textLocationTelephone.setVisibility(View.VISIBLE);
            } else {
                textLocationTelephone.setVisibility(View.GONE);
            }

            String placeEmail = placeCursor.getString(placeCursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_EMAIL));
            if (!TextUtils.isEmpty(placeEmail)) {
                textLocationEmail.setText(getString(R.string.detail_text_location_email) + placeEmail);
                textLocationEmail.setVisibility(View.VISIBLE);
            } else {
                textLocationEmail.setVisibility(View.GONE);
            }

            String placeAccessibility = placeCursor.getString(placeCursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_ACCESSIBILITY));
            if (!TextUtils.isEmpty(placeAccessibility)) {
                textLocationAccessibility.setText(placeAccessibility);
                textLocationAccessibility.setVisibility(View.VISIBLE);
            } else {
                textLocationAccessibility.setVisibility(View.GONE);
            }


            Double latitude = placeCursor.getDouble(placeCursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_LATITUDE));
            Double longitude = placeCursor.getDouble(placeCursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_LONGITUDE));
            LatLng location = new LatLng(latitude, longitude);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
            map.addMarker(new MarkerOptions().position(location).title(placeName)).showInfoWindow();

            String eventWeb = eventCursor.getString(eventCursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_WEB));
            if (!TextUtils.isEmpty(eventWeb)) {
                textOtherWeb.setText(getString(R.string.detail_text_other_web) + eventWeb);
                textOtherWeb.setVisibility(View.VISIBLE);
            } else {
                textOtherWeb.setVisibility(View.GONE);
            }


            eventCursor.close();
            placeCursor.close();
            categoryCursor.close();
        }
    }
}
