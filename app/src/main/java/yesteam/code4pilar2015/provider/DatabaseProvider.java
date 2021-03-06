package yesteam.code4pilar2015.provider;

import android.net.Uri;

import novoda.lib.sqliteprovider.provider.SQLiteContentProviderImpl;
import yesteam.code4pilar2015.BuildConfig;

public class DatabaseProvider extends SQLiteContentProviderImpl {

    public static final String AUTHORITY = "content://" + BuildConfig.APPLICATION_ID + ".provider";

    public static class CategoriesTable {
        public static final String TABLE_NAME = "categories";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_CODE = "code";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_IMAGE = "image";

        public static final Uri URI = Uri.parse(AUTHORITY).buildUpon().appendPath(TABLE_NAME).build();
    }

    public static class PlacesTable {
        public static final String TABLE_NAME = "places";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_CODE = "code";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_CP = "cp";
        public static final String COLUMN_TELEPHONE = "telephone";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_ACCESSIBILITY = "accessibility";

        public static final Uri URI = Uri.parse(AUTHORITY).buildUpon().appendPath(TABLE_NAME).build();
    }

    public static class EventsTable {
        public static final String TABLE_NAME = "events";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_CODE = "code";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_PLACE_CODE = "place_code";
        public static final String COLUMN_PLACE_NAME = "place_name";
        public static final String COLUMN_START_DATE = "start_date";
        public static final String COLUMN_END_DATE = "end_date";
        public static final String COLUMN_START_HOUR = "start_hour";
        public static final String COLUMN_CATEGORY_CODE = "category_code";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_WEB = "web";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_POPULATION_TYPE = "population_type";
        public static final String COLUMN_FAVORITE = "favorite";

        public static final Uri URI = Uri.parse(AUTHORITY).buildUpon().appendPath(TABLE_NAME).build();
    }

    public static class OfrendaTable {
        public static final String TABLE_NAME = "ofrenda";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TIME = "meet_time";
        public static final String COLUMN_ACCESS = "meet_access";

        public static final Uri URI = Uri.parse(AUTHORITY).buildUpon().appendPath(TABLE_NAME).build();
    }
}
