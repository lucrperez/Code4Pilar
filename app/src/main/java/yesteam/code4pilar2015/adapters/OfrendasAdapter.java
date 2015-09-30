package yesteam.code4pilar2015.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import yesteam.code4pilar2015.R;
import yesteam.code4pilar2015.helpers.CursorRecyclerAdapter;
import yesteam.code4pilar2015.provider.DatabaseProvider;

public class OfrendasAdapter extends CursorRecyclerAdapter<OfrendasAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, hour, access;
        public View topStroke, bottomStroke;

        public ViewHolder(View rowView) {
            super(rowView);

            name = (TextView) rowView.findViewById(R.id.pruebas_ofrenda_name);
            hour = (TextView) rowView.findViewById(R.id.pruebas_ofrenda_hour);
            access = (TextView) rowView.findViewById(R.id.pruebas_ofrenda_access);

            topStroke = rowView.findViewById(R.id.pruebas_ofrenda_top_stroke);
            bottomStroke = rowView.findViewById(R.id.pruebas_ofrenda_bottom_stroke);
        }
    }

    public OfrendasAdapter(Cursor cursor) {
        super(cursor);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ofrenda, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolderCursor(ViewHolder holder, Cursor cursor) {
        if (cursor.getPosition() == 0) {
            holder.topStroke.setVisibility(View.INVISIBLE);
        } else {
            holder.topStroke.setVisibility(View.VISIBLE);
        }

        if (cursor.getPosition() == cursor.getCount() - 1) {
            holder.bottomStroke.setVisibility(View.INVISIBLE);
        } else {
            holder.bottomStroke.setVisibility(View.VISIBLE);
        }

        holder.name.setText(cursor.getString(cursor.getColumnIndex(DatabaseProvider.OfrendaTable.COLUMN_NAME)));
        holder.hour.setText(cursor.getString(cursor.getColumnIndex(DatabaseProvider.OfrendaTable.COLUMN_TIME)));
        holder.access.setText(cursor.getString(cursor.getColumnIndex(DatabaseProvider.OfrendaTable.COLUMN_ACCESS)));
    }

}
