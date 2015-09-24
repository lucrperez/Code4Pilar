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
                //Intent int_ofrenda = new Intent(MainActivity.this, OfrendaListActivity.class);
                //startActivity(int_ofrenda);
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
                //Intent int_locations = new Intent(MainActivity.this, LocationsListActivity.class);
                //startActivity(int_locations);
            }
        });

        //Button Artists
        ImageView img4 = (ImageView) findViewById(R.id.main_img_artists);
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent int_artists = new Intent(MainActivity.this, ArtistsListActivity.class);
                //startActivity(int_artists);
            }
        });

/*
        RecyclerView mRecyclerViewLeft = (RecyclerView) findViewById(R.id.recycler_view_left);
        mRecyclerViewLeft.setHasFixedSize(true);

        LinearLayoutManager mLayoutManagerLeft = new LinearLayoutManager(this);
        mRecyclerViewLeft.setLayoutManager(mLayoutManagerLeft);

        adapterLeft = new EventsAdapter(MainActivity.this, null, this);
        mRecyclerViewLeft.setAdapter(adapterLeft);

        RecyclerView mRecyclerViewRight = (RecyclerView) findViewById(R.id.recycler_view_right);
        mRecyclerViewRight.setHasFixedSize(true);

        LinearLayoutManager mLayoutManagerRight = new LinearLayoutManager(this);
        mRecyclerViewRight.setLayoutManager(mLayoutManagerRight);

        adapterRight = new EventsAdapter(MainActivity.this, null, this);
        mRecyclerViewRight.setAdapter(adapterRight);
*/
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
