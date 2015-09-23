package yesteam.code4pilar2015.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

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
