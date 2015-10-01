package yesteam.code4pilar2015.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import yesteam.code4pilar2015.R;

public class OfrendaLocationsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final LatLng Zaragoza = new LatLng(41.652788, -0.880048);

    private static final LatLng PlazaAragon = new LatLng(41.647658, -0.885351);
    private static final LatLng Canfranc = new LatLng(41.6487291, -0.8856754);
    private static final LatLng Albareda = new LatLng(41.6491042, -0.8845738);
    private static final LatLng CasaJimenez = new LatLng(41.6498965, -0.8842101);
    private static final LatLng CincoMarzo = new LatLng(41.65162, -0.88223);
    private static final LatLng Diputacion = new LatLng(41.6522325, -0.88162);
    private static final LatLng PlazaSas = new LatLng(41.6538813, -0.8808521);
    private static final LatLng PuentePiedra = new LatLng(41.65653, -0.875782);
    private static final LatLng StaEngracia = new LatLng(41.6494018, -0.8830691);
    private static final LatLng SanVicentePaul = new LatLng(41.6550119, -0.8736276);
    private static final float zoom = 15.0F;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofrenda_locations);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.locations_ofrenda_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(PlazaAragon)
                .title("Plaza Aragón")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconb1)));

        googleMap.addMarker(new MarkerOptions().position(Canfranc)
                .title("Calle Canfranc")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconb2)));

        googleMap.addMarker(new MarkerOptions().position(Albareda)
                .title("Calle Albareda")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconb3)));

        googleMap.addMarker(new MarkerOptions().position(CasaJimenez)
                .title("Calle Casa Jiménez")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconb4)));

        googleMap.addMarker(new MarkerOptions().position(CincoMarzo)
                .title("Calle Cinco de Marzo")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconb5)));

        googleMap.addMarker(new MarkerOptions().position(Diputacion)
                .title("Diputación Provincial")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconb6)));

        googleMap.addMarker(new MarkerOptions().position(PlazaSas)
                .title("Plaza Sas")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconb7)));

        googleMap.addMarker(new MarkerOptions().position(PuentePiedra)
                .title("Puente de Piedra")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconb8)));

        googleMap.addMarker(new MarkerOptions().position(StaEngracia)
                .title("Plaza Santa Engracia")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconb9)));

        googleMap.addMarker(new MarkerOptions().position(SanVicentePaul)
                .title("Calle San Vicente de Paul")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconb10)));

        final LatLngBounds accesos = LatLngBounds.builder()
                .include(Albareda)
                .include(Canfranc)
                .include(CasaJimenez)
                .include(CincoMarzo)
                .include(Diputacion)
                .include(PlazaAragon)
                .include(PlazaSas)
                .include(PuentePiedra)
                .include(SanVicentePaul)
                .include(StaEngracia)
                .build();

        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(accesos, 100));
            }
        });

        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (cameraPosition.zoom > 2) {
                    CameraPosition newCameraPosition = new CameraPosition.Builder(cameraPosition).bearing(35).build();
                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition));
                }
            }
        });
    }
}
