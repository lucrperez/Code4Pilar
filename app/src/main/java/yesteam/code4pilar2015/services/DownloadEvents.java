package yesteam.code4pilar2015.services;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.Html;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
                Request request = new Request.Builder().url(URL_EVENTS).build();
                Response response = client.newCall(request).execute();
                strJson = response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONArray events = new JSONObject(strJson).getJSONArray("result");

                for (int i = 0; i < events.length(); i++) {
                    JSONObject event = events.getJSONObject(i);

                    ContentValues eventValues = new ContentValues();

                    eventValues.put(DatabaseProvider.EventsTable.COLUMN_CODE, event.getInt("id"));
                    eventValues.put(DatabaseProvider.EventsTable.COLUMN_TITLE, event.getString("title"));
                    if (!event.isNull("description")) {
                        eventValues.put(DatabaseProvider.EventsTable.COLUMN_DESCRIPTION, stripHtml(event.getString("description")));
                    }
                    if (!event.isNull("startDate")) {
                        eventValues.put(DatabaseProvider.EventsTable.COLUMN_START_DATE, event.getString("startDate"));
                    }
                    if (!event.isNull("endDate")) {
                        eventValues.put(DatabaseProvider.EventsTable.COLUMN_END_DATE, event.getString("endDate"));
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
                        eventValues.put(DatabaseProvider.EventsTable.COLUMN_PRICE, stripHtml(event.getString("precioEntrada")));
                    }

                    if (!event.isNull("temas")) {
                        JSONObject tema = event.getJSONArray("temas").getJSONObject(0);
                        eventValues.put(DatabaseProvider.EventsTable.COLUMN_CATEGORY_CODE, tema.getString("id"));
                    }

                    if (!event.isNull("subEvent")) {
                        JSONObject subEvent = event.getJSONArray("subEvent").getJSONObject(0);

                        if (!subEvent.isNull("horaInicio")) {
                            eventValues.put(DatabaseProvider.EventsTable.COLUMN_START_HOUR, subEvent.getString("horaInicio"));

                        } else if (!subEvent.isNull("horario")) {
                            eventValues.put(DatabaseProvider.EventsTable.COLUMN_START_HOUR, stripHtml(subEvent.getString("horario")));
                        }

                        JSONObject place = subEvent.getJSONObject("lugar");
                        eventValues.put(DatabaseProvider.EventsTable.COLUMN_PLACE_CODE, place.getString("id"));

                        ContentValues placeValues = new ContentValues();
                        placeValues.put(DatabaseProvider.PlacesTable.COLUMN_CODE, place.getString("id"));
                        placeValues.put(DatabaseProvider.PlacesTable.COLUMN_TITLE, place.getString("title"));
                        if (!place.isNull("direccion")) {
                            placeValues.put(DatabaseProvider.PlacesTable.COLUMN_ADDRESS, place.getString("direccion"));
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
                            placeValues.put(DatabaseProvider.PlacesTable.COLUMN_ACCESSIBILITY, stripHtml(place.getString("accesibilidad")));
                        }

                        if (!place.isNull("geometry")) {
                            JSONArray coordinates = place.getJSONObject("geometry").getJSONArray("coordinates");
                            placeValues.put(DatabaseProvider.PlacesTable.COLUMN_LATITUDE, coordinates.getString(1));
                            placeValues.put(DatabaseProvider.PlacesTable.COLUMN_LONGITUDE, coordinates.getString(0));
                        }

                        getContentResolver().insert(DatabaseProvider.PlacesTable.URI, placeValues);
                    }

                    getContentResolver().insert(DatabaseProvider.EventsTable.URI, eventValues);
                }

            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }

            return null;
        }

        public String stripHtml(String html) {
            return Html.fromHtml(html).toString();
        }
    }
}
