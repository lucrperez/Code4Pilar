package yesteam.code4pilar2015.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import yesteam.code4pilar2015.R;
import yesteam.code4pilar2015.helpers.CursorRecyclerAdapter;
import yesteam.code4pilar2015.provider.DatabaseProvider;

public class EventsAdapter extends CursorRecyclerAdapter<EventsAdapter.ViewHolder> {

    private OnItemClickEventListener mListener;

    public interface OnItemClickEventListener {
        public void onItemClickEvent(Cursor cursor);
    }

    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView date, title, description, place, price;

        public ViewHolder(View rowView) {
            super(rowView);

            image = (ImageView) rowView.findViewById(R.id.row_event_image);
            date = (TextView) rowView.findViewById(R.id.row_event_date);
            title = (TextView) rowView.findViewById(R.id.row_event_title);
            description = (TextView) rowView.findViewById(R.id.row_event_description);
            place = (TextView) rowView.findViewById(R.id.row_event_place);
            price = (TextView) rowView.findViewById(R.id.row_event_price);

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCursor().moveToPosition(getAdapterPosition());
                    mListener.onItemClickEvent(getCursor());
                }
            });
        }
    }

    public EventsAdapter(Context context, Cursor cursor, OnItemClickEventListener listener) {
        super(cursor);

        this.context = context;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_event, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolderCursor(ViewHolder holder, Cursor cursor) {
        holder.title.setText(cursor.getString(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_TITLE)));
        holder.description.setText(cursor.getString(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_DESCRIPTION)));
        holder.place.setText(cursor.getString(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_PLACE_CODE)));
        holder.price.setText(cursor.getString(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_PRICE)));
        holder.date.setText(cursor.getString(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_START_DATE)));

        String photo = "http:"+cursor.getString(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_IMAGE));
        Picasso.with(context).load(photo).noFade().into(holder.image);
    }

}
