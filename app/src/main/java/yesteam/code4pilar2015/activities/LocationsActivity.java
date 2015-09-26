package yesteam.code4pilar2015.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import yesteam.code4pilar2015.R;
import yesteam.code4pilar2015.adapters.SpinnerDateAdapter;

public class LocationsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int ROWS = 24;
    private static int endRow = 24;

    private GoogleMap map;
    private Spinner spinner;

    private static final String tenth = "10/10/2015";
    private static final String eleventh = "11/10/2015";
    private static final String twelfth = "12/10/2015";
    private static final String thirteenth = "13/10/2015";
    private static final String fourteenth = "14/10/2015";
    private static final String fifteenth = "15/10/2015";
    private static final String sixteenth = "16/10/2015";
    private static final String seventeenth = "17/10/2015";
    private static final String eighteenth = "18/10/2015";
    private static final String nineteeenth = "19/10/2015";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        List<String> listDates = new ArrayList<>();

        try {
            Date compareDate = dateFormat.parse(tenth);
            compareDate = dateFormat.parse(eleventh);
            if (new Date().before(compareDate)) {
                listDates.add(tenth);
            }
            compareDate = dateFormat.parse(twelfth);
            if (new Date().before(compareDate)) {
                listDates.add(eleventh);
            }
            compareDate = dateFormat.parse(thirteenth);
            if (new Date().before(compareDate)) {
                listDates.add(twelfth);
            }
            compareDate = dateFormat.parse(fourteenth);
            if (new Date().before(compareDate)) {
                listDates.add(thirteenth);
            }
            compareDate = dateFormat.parse(fifteenth);
            if (new Date().before(compareDate)) {
                listDates.add(fourteenth);
            }
            compareDate = dateFormat.parse(sixteenth);
            if (new Date().before(compareDate)) {
                listDates.add(fifteenth);
            }
            compareDate = dateFormat.parse(seventeenth);
            if (new Date().before(compareDate)) {
                listDates.add(sixteenth);
            }
            compareDate = dateFormat.parse(eighteenth);
            if (new Date().before(compareDate)) {
                listDates.add(seventeenth);
            }
            compareDate = dateFormat.parse(nineteeenth);
            if (new Date().before(compareDate)) {
                listDates.add(eighteenth);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String[] dates = new String[listDates.size()];
        listDates.toArray(dates);

        //String[] dates = new String[]{"10 de Octubre", "11 de Octubre", "12 de Octubre", "13 de Octubre", "14 de Octubre", "15 de Octubre", "16 de Octubre", "17 de Octubre", "18 de Octubre"};
        SpinnerDateAdapter spinnerAdapter = new SpinnerDateAdapter(this, dates);

        spinner = (Spinner) findViewById(R.id.spnDate);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.locations_map)).getMap();
        map.setMyLocationEnabled(true);
        LatLng ll = new LatLng(41.6532341,-0.8870108);
        float zoom = (float) 12.5;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, zoom));


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spnDate:
                endRow = ROWS;
                //new DownloadPets().execute();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
