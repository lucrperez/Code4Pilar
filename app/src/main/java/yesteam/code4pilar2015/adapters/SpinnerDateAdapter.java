package yesteam.code4pilar2015.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import yesteam.code4pilar2015.R;

/**
 * Created by Luis on 26/09/2015.
 */
public class SpinnerDateAdapter extends BaseAdapter {

    private Context context;
    private String[] mItems = new String[]{};

    public SpinnerDateAdapter(Context context, String[] mItems) {
        this.context = context;
        this.mItems = mItems;
    }

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public Object getItem(int position) {
        return mItems[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals("DROPDOWN")) {
            view = LayoutInflater.from(context).inflate(R.layout.toolbar_spinner_item_dropdown, parent, false);
            view.setTag("DROPDOWN");
        }

        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(getTitle(position));

        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null || !convertView.getTag().toString().equals("NON_DROPDOWN")) {
            convertView = LayoutInflater.from(context).inflate(R.layout.toolbar_spinner_item_actionbar, parent, false);
            convertView.setTag("NON_DROPDOWN");
        }
        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        textView.setText(getTitle(position));
        return convertView;
    }

    private String getTitle(int position) {
        return position >= 0 && position < mItems.length ? mItems[position] : "";
    }
}
