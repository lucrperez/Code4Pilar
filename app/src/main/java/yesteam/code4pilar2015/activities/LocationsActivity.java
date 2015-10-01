package yesteam.code4pilar2015.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

import yesteam.code4pilar2015.R;
import yesteam.code4pilar2015.provider.DatabaseProvider;

public class LocationsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final LatLng Zaragoza = new LatLng(41.6532341, -0.8870108);
    private static final Float zoom = 12.5F;

    private GoogleMap map;
    private HashMap<String, String[]> eventMarkerMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        eventMarkerMap = new HashMap<>();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                LocationsActivity.this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(Zaragoza, zoom));
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String[] placeData = eventMarkerMap.get(marker.getId());

                Intent intent = new Intent(LocationsActivity.this, LocationListActivity.class);
                intent.putExtra("place-code", placeData[0]);
                intent.putExtra("place-name", placeData[1]);
                startActivity(intent);
            }
        });

        new LoadLocations().execute();
    }

    private class LoadLocations extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... params) {
            return getContentResolver().query(DatabaseProvider.PlacesTable.URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);

            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_TITLE));
                String code = cursor.getString(cursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_CODE));
                double latitude = cursor.getDouble(cursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_LATITUDE));
                double longitude = cursor.getDouble(cursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_LONGITUDE));

                if ((latitude != 0) && (longitude != 0)) {
                    LatLng position = new LatLng(latitude, longitude);
                    Marker marker = map.addMarker(new MarkerOptions()
                            .position(position)
                            .draggable(false)
                            .visible(true)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            .title(name)
                            .snippet(getString(R.string.places_details_text)));

                    eventMarkerMap.put(marker.getId(), new String[]{code, name});
                }
            }
        }
    }
}
