package yesteam.code4pilar2015.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import yesteam.code4pilar2015.R;
import yesteam.code4pilar2015.items.OfrendaItem;

/**
 * Created by Luis on 28/09/2015.
 */
public class OfrendaAdapter extends ArrayAdapter<OfrendaItem> {

    private final Context context;
    private final ArrayList<OfrendaItem> itemArrayList;

    public OfrendaAdapter(Context context, ArrayList<OfrendaItem> itemArrayList) {
        super(context, R.layout.row_ofrenda, itemArrayList);

        this.context = context;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.row_ofrenda, parent, false);

        //3. Get the views from layout
        TextView idView = (TextView) rowView.findViewById(R.id.row_ofrenda_id);
        TextView titleView = (TextView) rowView.findViewById(R.id.row_ofrenda_name);
        TextView hourView = (TextView) rowView.findViewById(R.id.row_ofrenda_hour);
        TextView accessView = (TextView) rowView.findViewById(R.id.row_ofrenda_access);

        //4. Set the text for the View
        idView.setText(Integer.toString(itemArrayList.get(position).getID()));
        titleView.setText(itemArrayList.get(position).getName());
        hourView.setText(itemArrayList.get(position).getMeet_hour());
        int access = itemArrayList.get(position).getAccess();
        if (access == 0) {
            accessView.setText("ND");
        } else if (access == -1) {
            accessView.setText("Acceso");
        } else {
            accessView.setText(Integer.toString(access));
        }

        return rowView;
    }
}
