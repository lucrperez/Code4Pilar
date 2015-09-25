package yesteam.code4pilar2015.services;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import yesteam.code4pilar2015.provider.DatabaseProvider;

public class DownloadCategories extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new AsyncDownload().execute();
        return Service.START_FLAG_REDELIVERY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class AsyncDownload extends AsyncTask<Void, Void, Void> {

        private static final String URL_CATEGORIES = "http://www.zaragoza.es/api/recurso/cultura-ocio/evento-zaragoza.json?fl=temas&rows=1000&q=programa%3D%3DFiestas%20del%20Pilar";

        @Override
        protected Void doInBackground(Void... params) {
            String strJson = null;
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(URL_CATEGORIES).build();
                Response response = client.newCall(request).execute();
                strJson = response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONArray categories = new JSONObject(strJson).getJSONArray("result");

                for (int i = 0; i < categories.length(); i++) {
                    JSONObject category = categories.getJSONObject(i).getJSONArray("temas").getJSONObject(0);

                    ContentValues values = new ContentValues();
                    values.put(DatabaseProvider.CategoriesTable.COLUMN_CODE, category.getInt("id"));
                    values.put(DatabaseProvider.CategoriesTable.COLUMN_TITLE, category.getString("title"));
                    if (!category.isNull("image")) {
                        values.put(DatabaseProvider.CategoriesTable.COLUMN_IMAGE, category.getString("image"));
                    }

                    getContentResolver().insert(DatabaseProvider.CategoriesTable.URI, values);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
