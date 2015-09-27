package yesteam.code4pilar2015.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import yesteam.code4pilar2015.R;

public class LocationsActivity extends AppCompatActivity {

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.locations_map)).getMap();
        map.setMyLocationEnabled(true);
        LatLng ll = new LatLng(41.6532341,-0.8870108);
        float zoom = (float) 12.5;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, zoom));


    }
}
