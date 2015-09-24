package yesteam.code4pilar2015.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import yesteam.code4pilar2015.R;
import yesteam.code4pilar2015.helpers.CursorRecyclerAdapter;
import yesteam.code4pilar2015.provider.DatabaseProvider;

public class EventsAdapter extends CursorRecyclerAdapter<EventsAdapter.ViewHolder> {

    private OnItemClickEventListener mListener;

    public interface OnItemClickEventListener {
        void onItemClickEvent(Cursor cursor);
    }

    private Context context;
    private SimpleDateFormat formatterIn, formatterOut;

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
        formatterIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        formatterOut = new SimpleDateFormat("dd/MM", Locale.getDefault());
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

        if (!TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_PRICE)))) {
            holder.price.setText(cursor.getString(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_PRICE)));
        } else {
            holder.price.setText(R.string.price_free);
        }

        if (!TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_START_HOUR)))) {
            holder.date.setText(cursor.getString(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_START_HOUR)));

        } else {
            try {
                Date dateStart = null;
                Date dateEnd = null;

                String date = cursor.getString(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_START_DATE));
                if (!TextUtils.isEmpty(date)) {
                    dateStart = formatterIn.parse(date);
                }

                date = cursor.getString(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_END_DATE));
                if (!TextUtils.isEmpty(date)) {
                    dateEnd = formatterIn.parse(date);
                }

                if ((dateStart != null) && (dateEnd != null)) {
                    holder.date.setText(formatterOut.format(dateStart) + " - " + formatterOut.format(dateEnd));
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        String photo = "http:" + cursor.getString(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_IMAGE));
        Picasso.with(context).load(photo).placeholder(R.drawable.placeholder).noFade().into(holder.image);


        holder.date.setSelected(true);
        holder.place.setSelected(true);
        holder.price.setSelected(true);
    }

}
