package yesteam.code4pilar2015.adapters;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
    private SimpleDateFormat formatterOut, formatterSection;

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout section;
        public ImageView image;
        public TextView date, title, description, place, price, section_title;

        public ViewHolder(View rowView) {
            super(rowView);

            section = (LinearLayout) rowView.findViewById(R.id.section);
            section_title = (TextView) rowView.findViewById(R.id.section_title);

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

        formatterOut = new SimpleDateFormat("dd/MM", Locale.getDefault());

        String bestPattern;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bestPattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "MMMMd");
        } else {
            bestPattern = "d MMMM";
        }
        formatterSection = new SimpleDateFormat(bestPattern, Locale.getDefault());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_event, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolderCursor(ViewHolder holder, Cursor cursor) {
        if (cursor.getPosition() == 0) {
            holder.section.setVisibility(View.VISIBLE);

            long date = cursor.getLong(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_END_DATE));
            if (date > 0) {
                holder.section.setVisibility(View.VISIBLE);
                holder.section_title.setText(context.getString(R.string.date_until) + formatterSection.format(new Date(date)));

            } else {
                holder.section.setVisibility(View.GONE);
            }

        } else {
            cursor.moveToPrevious();
            long datePrev = cursor.getLong(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_END_DATE));
            cursor.moveToNext();

            long dateThis = cursor.getLong(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_END_DATE));
            if (datePrev != dateThis) {
                holder.section.setVisibility(View.VISIBLE);
                holder.section_title.setText(context.getString(R.string.date_until) + formatterSection.format(new Date(dateThis)));

            } else {
                holder.section.setVisibility(View.GONE);
            }
        }

        holder.title.setText(cursor.getString(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_TITLE)));
        holder.description.setText(cursor.getString(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_DESCRIPTION)));
        holder.place.setText(cursor.getString(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_PLACE_NAME)));

        if (!TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_PRICE)))) {
            holder.price.setText(cursor.getString(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_PRICE)));
        } else {
            holder.price.setText(R.string.price_free);
        }

        if (!TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_START_HOUR)))) {
            holder.date.setText(cursor.getString(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_START_HOUR)));

        } else {
            Date dateStart = null;
            Date dateEnd = null;

            long date = cursor.getLong(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_START_DATE));
            if (date > 0) {
                dateStart = new Date(date);
            }

            date = cursor.getLong(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_END_DATE));
            if (date > 0) {
                dateEnd = new Date(date);
            }

            if (dateStart != null) {
                if (dateEnd != null) {
                    holder.date.setText(formatterOut.format(dateStart) + " - " + formatterOut.format(dateEnd));
                } else {
                    holder.date.setText(formatterOut.format(dateStart));
                }

            } else {
                if (dateEnd != null) {
                    holder.date.setText(formatterOut.format(dateEnd));
                } else {
                    holder.date.setText("");
                }
            }
        }

        String photo = "http:" + cursor.getString(cursor.getColumnIndex(DatabaseProvider.EventsTable.COLUMN_IMAGE));
        Picasso.with(context).load(photo).placeholder(R.drawable.placeholder).noFade().into(holder.image);


        holder.date.setSelected(true);
        holder.place.setSelected(true);
        holder.price.setSelected(true);
    }

}
