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

public class CategoriesAdapter extends CursorRecyclerAdapter<CategoriesAdapter.ViewHolder> {

    private OnItemClickEventListener mListener;

    public interface OnItemClickEventListener {
        void onItemClickEvent(Cursor cursor);
    }

    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;

        public ViewHolder(View rowView) {
            super(rowView);

            image = (ImageView) rowView.findViewById(R.id.grid_category_image);
            title = (TextView) rowView.findViewById(R.id.grid_category_title);

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCursor().moveToPosition(getAdapterPosition());
                    mListener.onItemClickEvent(getCursor());
                }
            });
        }
    }

    public CategoriesAdapter(Context context, Cursor cursor, OnItemClickEventListener listener) {
        super(cursor);

        this.mListener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_category, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolderCursor(ViewHolder holder, Cursor cursor) {
        holder.title.setText(cursor.getString(cursor.getColumnIndex(DatabaseProvider.CategoriesTable.COLUMN_TITLE)));

        String photo = "http://www.zaragoza.es/cont/paginas/actividades/" + cursor.getString(cursor.getColumnIndex(DatabaseProvider.CategoriesTable.COLUMN_IMAGE));
        Picasso.with(context).load(photo).placeholder(R.drawable.placeholder).noFade().into(holder.image);

        holder.title.setSelected(true);
    }
}
