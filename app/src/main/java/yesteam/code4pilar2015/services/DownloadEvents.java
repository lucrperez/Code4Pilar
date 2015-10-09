package yesteam.code4pilar2015.services;

import android.app.AlarmManager;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.Html;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import yesteam.code4pilar2015.provider.DatabaseProvider;

public class DownloadEvents extends Service {

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

        private static final String URL_EVENTS = "http://www.zaragoza.es/api/recurso/cultura-ocio/evento-zaragoza.json?srsname=wgs84&sort=startDate%20asc&rows=1000&q=programa==Fiestas%20del%20Pilar";

        @Override
        protected Void doInBackground(Void... params) {
            String strJson = null;
            try {
                OkHttpClient client = new OkHttpClient();
                client.setConnectTimeout(15, TimeUnit.SECONDS);
                client.setReadTimeout(15, TimeUnit.SECONDS);

                Request request = new Request.Builder().url(URL_EVENTS).build();
                Response response = client.newCall(request).execute();
                strJson = response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            try {
                JSONObject jsonData = new JSONObject(strJson);
                int total = jsonData.getInt("totalCount");

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(DownloadEvents.this);
                boolean newData = !(total == prefs.getInt("total_data", 0));
                boolean newDbVersion = prefs.getInt("db_version", 0) < 2;
                boolean dataTooOld = (System.currentTimeMillis() - prefs.getLong("last_update", 0)) > (3 * AlarmManager.INTERVAL_HOUR);

                if (!(newDbVersion || dataTooOld || newData)) {
                    return null;

                } else {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("total_data", total);
                    editor.putInt("db_version", 2);
                    editor.putLong("last_update", System.currentTimeMillis());
                    editor.apply();

                    getContentResolver().delete(DatabaseProvider.EventsTable.URI, null, null);
                    getContentResolver().delete(DatabaseProvider.CategoriesTable.URI, null, null);
                    getContentResolver().delete(DatabaseProvider.PlacesTable.URI, null, null);
                }

                JSONArray events = jsonData.getJSONArray("result");
                SimpleDateFormat formatterIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());

                ArrayList<ContentValues> arrayEvents = new ArrayList<>();
                ArrayList<ContentValues> arrayCategories = new ArrayList<>();
                ArrayList<ContentValues> arrayPlaces = new ArrayList<>();

                for (int i = 0; i < events.length(); i++) {
                    JSONObject event = events.getJSONObject(i);

                    ContentValues eventValues = new ContentValues();

                    eventValues.put(DatabaseProvider.EventsTable.COLUMN_CODE, event.getInt("id"));
                    eventValues.put(DatabaseProvider.EventsTable.COLUMN_TITLE, event.getString("title").trim());
                    if (!event.isNull("description")) {
                        eventValues.put(DatabaseProvider.EventsTable.COLUMN_DESCRIPTION, stripHtml(event.getString("description")).trim());
                    }
                    if (!event.isNull("startDate")) {
                        eventValues.put(DatabaseProvider.EventsTable.COLUMN_START_DATE, formatterIn.parse(event.getString("startDate")).getTime());
                    }
                    if (!event.isNull("endDate")) {
                        eventValues.put(DatabaseProvider.EventsTable.COLUMN_END_DATE, formatterIn.parse(event.getString("endDate")).getTime());
                    }
                    if (!event.isNull("web")) {
                        eventValues.put(DatabaseProvider.EventsTable.COLUMN_WEB, event.getString("web"));
                    }
                    if (!event.isNull("image")) {
                        eventValues.put(DatabaseProvider.EventsTable.COLUMN_IMAGE, event.getString("image"));
                    }

                    if (!event.isNull("price")) {
                        JSONObject price = event.getJSONArray("price").getJSONObject(0);
                        eventValues.put(DatabaseProvider.EventsTable.COLUMN_PRICE, price.getString("hasCurrencyValue") + " " + price.getString("hasCurrency"));

                    } else if (!event.isNull("precioEntrada")) {
                        eventValues.put(DatabaseProvider.EventsTable.COLUMN_PRICE, stripHtml(event.getString("precioEntrada")).trim());
                    }

                    if (!event.isNull("poblacion")) {
                        JSONObject poblacion = event.getJSONArray("poblacion").getJSONObject(0);
                        eventValues.put(DatabaseProvider.EventsTable.COLUMN_POPULATION_TYPE, poblacion.getString("id"));
                    }

                    if (!event.isNull("temas")) {
                        JSONObject tema = event.getJSONArray("temas").getJSONObject(0);
                        eventValues.put(DatabaseProvider.EventsTable.COLUMN_CATEGORY_CODE, tema.getString("id"));

                        ContentValues categoryValues = new ContentValues();
                        categoryValues.put(DatabaseProvider.CategoriesTable.COLUMN_CODE, tema.getInt("id"));
                        categoryValues.put(DatabaseProvider.CategoriesTable.COLUMN_TITLE, tema.getString("title"));
                        if (!tema.isNull("image")) {
                            categoryValues.put(DatabaseProvider.CategoriesTable.COLUMN_IMAGE, tema.getString("image"));
                        }

                        if (!arrayCategories.contains(categoryValues)) {
                            arrayCategories.add(categoryValues);
                        }
                    }

                    if (!event.isNull("subEvent")) {
                        JSONObject subEvent = event.getJSONArray("subEvent").getJSONObject(0);

                        if (!subEvent.isNull("horaInicio")) {
                            eventValues.put(DatabaseProvider.EventsTable.COLUMN_START_HOUR, subEvent.getString("horaInicio"));

                        } else if (!subEvent.isNull("horario")) {
                            eventValues.put(DatabaseProvider.EventsTable.COLUMN_START_HOUR, stripHtml(subEvent.getString("horario")).trim());
                        }

                        JSONObject place = subEvent.getJSONObject("lugar");
                        eventValues.put(DatabaseProvider.EventsTable.COLUMN_PLACE_CODE, place.getString("id"));
                        eventValues.put(DatabaseProvider.EventsTable.COLUMN_PLACE_NAME, place.getString("title"));

                        ContentValues placeValues = new ContentValues();
                        placeValues.put(DatabaseProvider.PlacesTable.COLUMN_CODE, place.getString("id"));
                        placeValues.put(DatabaseProvider.PlacesTable.COLUMN_TITLE, place.getString("title"));
                        if (!place.isNull("direccion")) {
                            placeValues.put(DatabaseProvider.PlacesTable.COLUMN_ADDRESS, place.getString("direccion").trim());
                        }
                        if (!place.isNull("cp")) {
                            placeValues.put(DatabaseProvider.PlacesTable.COLUMN_CP, place.getString("cp"));
                        }
                        if (!place.isNull("telefono")) {
                            placeValues.put(DatabaseProvider.PlacesTable.COLUMN_TELEPHONE, place.getString("telefono"));
                        }
                        if (!place.isNull("mail")) {
                            placeValues.put(DatabaseProvider.PlacesTable.COLUMN_EMAIL, place.getString("mail"));
                        }
                        if (!place.isNull("accesibilidad")) {
                            placeValues.put(DatabaseProvider.PlacesTable.COLUMN_ACCESSIBILITY, stripHtml(place.getString("accesibilidad")).trim());
                        }

                        if (!place.isNull("geometry")) {
                            JSONArray coordinates = place.getJSONObject("geometry").getJSONArray("coordinates");
                            placeValues.put(DatabaseProvider.PlacesTable.COLUMN_LATITUDE, coordinates.getString(1));
                            placeValues.put(DatabaseProvider.PlacesTable.COLUMN_LONGITUDE, coordinates.getString(0));
                        }

                        if (!arrayPlaces.contains(placeValues)) {
                            arrayPlaces.add(placeValues);
                        }
                    }

                    if (!arrayEvents.contains(eventValues)) {
                        arrayEvents.add(eventValues);
                    }
                }

                getContentResolver().bulkInsert(DatabaseProvider.EventsTable.URI, arrayEvents.toArray(new ContentValues[arrayEvents.size()]));
                getContentResolver().bulkInsert(DatabaseProvider.CategoriesTable.URI, arrayCategories.toArray(new ContentValues[arrayCategories.size()]));
                getContentResolver().bulkInsert(DatabaseProvider.PlacesTable.URI, arrayPlaces.toArray(new ContentValues[arrayPlaces.size()]));

            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
                return null;

            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }

            return null;
        }

        public String stripHtml(String html) {
            return Html.fromHtml(html).toString();
        }
    }
}
