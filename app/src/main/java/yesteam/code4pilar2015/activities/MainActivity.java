package yesteam.code4pilar2015.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import yesteam.code4pilar2015.R;
import yesteam.code4pilar2015.adapters.EventsAdapter;

public class MainActivity extends AppCompatActivity {

    private EventsAdapter adapterLeft;
    private EventsAdapter adapterRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Button Ofrenda
        ImageView img1 = (ImageView) findViewById(R.id.main_img_ofrenda);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int_ofrenda = new Intent(MainActivity.this, OfrendaListActivity.class);
                startActivity(int_ofrenda);
            }
        });

        //Button Eventos
        ImageView img2 = (ImageView) findViewById(R.id.main_img_events);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int_events = new Intent(MainActivity.this, EventsListActivity.class);
                startActivity(int_events);
            }
        });

        //Button Locations
        ImageView img3 = (ImageView) findViewById(R.id.main_img_locations);
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int_locations = new Intent(MainActivity.this, LocationsActivity.class);
                startActivity(int_locations);
            }
        });

        //Button Artists
        ImageView img4 = (ImageView) findViewById(R.id.main_img_categories);
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int_categories = new Intent(MainActivity.this, CategoriesListActivity.class);
                startActivity(int_categories);
            }
        });
    }
}
