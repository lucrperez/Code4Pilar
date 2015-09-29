package yesteam.code4pilar2015.activities;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private CardView cardDescription, cardDate, cardPrice, cardLocation, cardOther;

    private Intent shareIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cardDescription = (CardView) findViewById(R.id.card_description);
        cardDate = (CardView) findViewById(R.id.card_date);
        cardPrice = (CardView) findViewById(R.id.card_price);
        cardLocation = (CardView) findViewById(R.id.card_location);
        cardOther = (CardView) findViewById(R.id.card_other);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_detail_event, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem actionShare = menu.findItem(R.id.action_share);

        if (shareIntent == null) {
            actionShare.setVisible(false);
        } else {
            actionShare.setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_chooser_title)));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void CreateShareIntent(int eventCode, String eventName, String placeCode) {
        shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Fiestas del Pilar 2015 - " + eventName +
                " http://www.zaragoza.es/ciudad/fiestaspilar/detalle_Agenda?id=" + eventCode + "&lugar=" + placeCode.substring(placeCode.indexOf("-") + 1));
        shareIntent.setType("text/plain");
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

            Cursor placeCursor = null;
            if (placeCode != null) {
                placeCursor = getContentResolver().query(DatabaseProvider.PlacesTable.URI, null, DatabaseProvider.PlacesTable.COLUMN_CODE + "=?", new String[]{placeCode}, null);
            }
            Cursor categoryCursor = getContentResolver().query(DatabaseProvider.CategoriesTable.URI, null, DatabaseProvider.CategoriesTable.COLUMN_CODE + "=?", new String[]{String.valueOf(categoryCode)}, null);

            return new Cursor[]{eventCursor, placeCursor, categoryCursor};
        }

        @Override
        protected void onPostExecute(Cursor[] cursors) {
            super.onPostExecute(cursors);

            Cursor eventCursor = cursors[0];
            Cursor placeCursor = cursors[1];
            Cursor categoryCursor = cursors[2];

            int eventCode;
            String eventName, placeCode;
            if (eventCursor != null) {
                eventCursor.moveToFirst();

                eventCode = eventCursor.getInt(eventCursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_CODE));
                eventName = eventCursor.getString(eventCursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_TITLE));
                placeCode = eventCursor.getString(eventCursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_PLACE_CODE));
                CreateShareIntent(eventCode, eventName, placeCode);
                supportInvalidateOptionsMenu();

                String title = eventCursor.getString(eventCursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_TITLE));
                toolbarLayout.setTitle(title);

                String imagePath = "http:" + eventCursor.getString(eventCursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_IMAGE));
                Picasso.with(DetailEventActivity.this).load(imagePath).placeholder(R.drawable.placeholder).noFade().into(image);

                String description = eventCursor.getString(eventCursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_DESCRIPTION));
                textDescription.setText(description);

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
                if (hours != null) {
                    textHour.setText(getString(R.string.detail_text_hour) + hours);
                } else {
                    textHour.setVisibility(View.GONE);
                }

                String price = eventCursor.getString(eventCursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_PRICE));
                if (!TextUtils.isEmpty(price)) {
                    textPrice.setText(price);
                } else {
                    textPrice.setText(R.string.price_free);
                }

                String eventWeb = eventCursor.getString(eventCursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_WEB));
                if (!TextUtils.isEmpty(eventWeb)) {
                    textOtherWeb.setText(getString(R.string.detail_text_other_web) + eventWeb);
                } else {
                    cardOther.setVisibility(View.GONE);
                }

                eventCursor.close();
            }


            if (categoryCursor != null) {
                categoryCursor.moveToFirst();

                String category = categoryCursor.getString(categoryCursor.getColumnIndex(DatabaseProvider.CategoriesTable.COLUMN_TITLE));
                textCategory.setText(getString(R.string.detail_text_category) + category);

                categoryCursor.close();
            }


            if (placeCursor != null) {
                placeCursor.moveToFirst();

                String placeName = placeCursor.getString(placeCursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_TITLE));
                textLocationName.setText(placeName);

                String placeAddress = placeCursor.getString(placeCursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_ADDRESS));
                if (!TextUtils.isEmpty(placeAddress)) {
                    textLocationAddress.setText(placeAddress);
                } else {
                    textLocationAddress.setVisibility(View.GONE);
                }

                String placeTelephone = placeCursor.getString(placeCursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_TELEPHONE));
                if (!TextUtils.isEmpty(placeTelephone)) {
                    textLocationTelephone.setText(getString(R.string.detail_text_location_telephone) + placeTelephone);
                } else {
                    textLocationTelephone.setVisibility(View.GONE);
                }

                String placeEmail = placeCursor.getString(placeCursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_EMAIL));
                if (!TextUtils.isEmpty(placeEmail)) {
                    textLocationEmail.setText(getString(R.string.detail_text_location_email) + placeEmail);
                } else {
                    textLocationEmail.setVisibility(View.GONE);
                }

                String placeAccessibility = placeCursor.getString(placeCursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_ACCESSIBILITY));
                if (!TextUtils.isEmpty(placeAccessibility)) {
                    textLocationAccessibility.setText(placeAccessibility);
                } else {
                    textLocationAccessibility.setVisibility(View.GONE);
                }

                double latitude = placeCursor.getDouble(placeCursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_LATITUDE));
                double longitude = placeCursor.getDouble(placeCursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_LONGITUDE));
                if ((latitude != 0) && (longitude != 0)) {
                    LatLng location = new LatLng(latitude, longitude);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
                    map.addMarker(new MarkerOptions().position(location).title(placeName)).showInfoWindow();

                } else {
                    Fragment mFragment = getFragmentManager().findFragmentById(R.id.map);
                    getFragmentManager().beginTransaction().hide(mFragment).commit();
                }

                placeCursor.close();

            } else {
                cardLocation.setVisibility(View.GONE);
            }

        }
    }
}
