package yesteam.code4pilar2015.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import yesteam.code4pilar2015.R;
import yesteam.code4pilar2015.items.LocationItem;
import yesteam.code4pilar2015.provider.DatabaseProvider;

public class LocationsActivity extends AppCompatActivity {

    private GoogleMap map;
    ArrayList<LocationItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.locations_map)).getMap();
        LatLng ll = new LatLng(41.6532341,-0.8870108);
        float zoom = (float) 12.5;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, zoom));

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                for (LocationItem it : items) {
                    LatLng ll = new LatLng(it.getLat(), it.getLng());
                    if (marker.getPosition().latitude == ll.latitude && marker.getPosition().longitude == ll.longitude) {


                        Intent intent = new Intent(LocationsActivity.this, LocationListActivity.class);
                        intent.putExtra("place-code", it.getCode());
                        intent.putExtra("place-name", it.getName());
                        startActivity(intent);

                        break;


                        /*String[] projection = new String[]{DatabaseProvider.EventsTable.COLUMN_TITLE, DatabaseProvider.EventsTable.COLUMN_CODE};
                        String where = DatabaseProvider.EventsTable.COLUMN_PLACE_CODE + "='" + it.getCode() + "'";
                        final Cursor cursor = getContentResolver().query(DatabaseProvider.EventsTable.URI, projection, where, null, null);

                        ArrayList<String> items = new ArrayList<String>();

                        if (cursor.moveToFirst()) {
                            items.add(cursor.getString(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_TITLE)));
                        }
                        while (cursor.moveToNext()) {
                            items.add(cursor.getString(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_TITLE)));
                        }

                        CharSequence[] cs = items.toArray(new CharSequence[items.size()]);

                        final AlertDialog.Builder builder = new AlertDialog.Builder(LocationsActivity.this);

                        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                        builder.setTitle(R.string.main_events)
                                .setItems(cs, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        cursor.moveToPosition(which);
                                        Intent intent = new Intent(getApplicationContext(), DetailEventActivity.class);
                                        intent.putExtra("event-code", cursor.getInt(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_CODE)));
                                        startActivity(intent);
                                    }
                                });

                        builder.create();
                        builder.show();*/
                    }
                }
            }
        });

            new

            LoadLocations()

            .

            execute();
        }


        private class LoadLocations extends AsyncTask<Void, Void, ArrayList<LocationItem>> {

        @Override
        protected ArrayList<LocationItem> doInBackground(Void... params) {
            String[] projection = new String[] { DatabaseProvider.PlacesTable.COLUMN_ID,
                    DatabaseProvider.PlacesTable.COLUMN_CODE,
                    DatabaseProvider.PlacesTable.COLUMN_LATITUDE,
                    DatabaseProvider.PlacesTable.COLUMN_LONGITUDE,
                    DatabaseProvider.PlacesTable.COLUMN_TITLE};
            Cursor cursor = getContentResolver().query(DatabaseProvider.PlacesTable.URI, projection, null, null, null);

            items = new ArrayList<>();

            LocationItem loc = new LocationItem();

            if (cursor.moveToFirst()) {
                loc.set_id(cursor.getInt(cursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_ID)));
                loc.setCode(cursor.getString(cursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_CODE)));
                loc.setName(cursor.getString(cursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_TITLE)));
                loc.setLat(cursor.getDouble(cursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_LATITUDE)));
                loc.setLng(cursor.getDouble(cursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_LONGITUDE)));
                items.add(loc);
            }
            while (cursor.moveToNext()) {
                loc = new LocationItem();
                loc.set_id(cursor.getInt(cursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_ID)));
                loc.setCode(cursor.getString(cursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_CODE)));
                loc.setName(cursor.getString(cursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_TITLE)));
                loc.setLat(cursor.getDouble(cursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_LATITUDE)));
                loc.setLng(cursor.getDouble(cursor.getColumnIndex(DatabaseProvider.PlacesTable.COLUMN_LONGITUDE)));
                items.add(loc);
            }
            cursor.close();
            return items;
        }

        @Override
        protected void onPostExecute(ArrayList<LocationItem> locationItems) {
            super.onPostExecute(locationItems);

            if (locationItems != null) {
                for (LocationItem it : locationItems) {
                    LatLng ll = new LatLng(it.getLat(), it.getLng());
                    map.addMarker(new MarkerOptions()
                            .position(ll)
                            .draggable(false)
                            .visible(true)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            .title(it.getName())
                            .snippet(getResources().getString(R.string.places_details_text)));
                }
            }

        }
    }
}
