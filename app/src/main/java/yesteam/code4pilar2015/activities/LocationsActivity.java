package yesteam.code4pilar2015.activities;

import android.content.Context;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import yesteam.code4pilar2015.R;
import yesteam.code4pilar2015.items.ClusterMarker;
import yesteam.code4pilar2015.provider.DatabaseProvider;

public class LocationsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final LatLng Zaragoza = new LatLng(41.6532341, -0.8870108);
    private static final Float zoom = 12.5F;

    private ClusterManager<ClusterMarker> mClusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Zaragoza, zoom));

        mClusterManager = new ClusterManager<>(this, googleMap);
        googleMap.setOnCameraChangeListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
        googleMap.setOnInfoWindowClickListener(mClusterManager);

        googleMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());

        final MyClusterRenderer clusterRenderer = new MyClusterRenderer(this, googleMap, mClusterManager);
        mClusterManager.setRenderer(clusterRenderer);
        clusterRenderer.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<ClusterMarker>() {
            @Override
            public void onClusterItemInfoWindowClick(ClusterMarker clusterMarker) {
                Intent intent = new Intent(LocationsActivity.this, LocationListActivity.class);
                intent.putExtra("place-code", clusterMarker.getCode());
                intent.putExtra("place-name", clusterMarker.getName());
                startActivity(intent);
            }
        });

        new LoadLocations().execute();
    }

    private class MyClusterRenderer extends DefaultClusterRenderer<ClusterMarker> {

        public MyClusterRenderer(Context context, GoogleMap map, ClusterManager<ClusterMarker> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(ClusterMarker item, MarkerOptions markerOptions) {
            markerOptions.title(item.getName());
            markerOptions.snippet(getString(R.string.places_details_text));
        }
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
                    ClusterMarker marker = new ClusterMarker(latitude, longitude, name, code);
                    mClusterManager.addItem(marker);
                }
            }
        }
    }
}
