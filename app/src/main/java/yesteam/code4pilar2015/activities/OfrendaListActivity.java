package yesteam.code4pilar2015.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import yesteam.code4pilar2015.R;

public class OfrendaListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofrenda_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //MySimpleAdapter adapter = new MySimpleAdapter(getApplicationContext(), generateData());

        ListView lv = (ListView) findViewById(R.id.groups_list);
        AutoCompleteTextView tvGroup = (AutoCompleteTextView) findViewById(R.id.groups_search);
    }


}
