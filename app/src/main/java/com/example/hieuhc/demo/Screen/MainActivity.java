package com.example.hieuhc.demo.Screen;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.hieuhc.demo.Model.LocationModel;
import com.example.hieuhc.demo.Model.PlaceDetailModel;
import com.example.hieuhc.demo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DownloadFilesTask task = (DownloadFilesTask) new DownloadFilesTask().execute();
    }


    private class DownloadFilesTask extends AsyncTask<String, Integer, JSONObject> {
        String TAG = "DownloadFilesTask";
        String link = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyDNFGAYONj3HwOkg56cuaFOxdJs2qf1VmU&radius=500&pagetoken=CqQEHwIAAMwN8y-ypiZXUzgLaXZP46TXIrMVW2-S01J48NJI6SyuSqhCANlnooscz3Ue7-_P_Z03XHHhvwdhoY3rtQlQk_0DlGFT7GyibULCaT5-HyM8EWVrbGIctpAQsnhsI21ZPUY7pnG-riDzqbMZddiUvl1htCPm63BovY5pePHME5G4HM5R6360oppjZHpmp_QOOMe93IR23i4xXnilkYtTkgkp31m__jpgWwafRE9u9w33QsZTLaNeeBqhhLnqEoB9OOS4Ept2jAwePFt9zHjPbWLWemYoSYFmdqEDi4SrSBLstKuQqpgwou_TGyF0zVmE9JsIRIEoA-SnpSdxgjOudD-7pmBGJ5x96ASFS9zulw2uQ9v8MjtzIToInK9GMjLNSr273V7pL7qDQ5P2mtk4RwFJB2FkNXN2nSBIk46Me4IzWtx-Ti4o-sGHMfBW3gM0qiKm98RG9D8kFqghHc5AwhmpHpTS4eUwe59xt7zJq7UclmdK-hQ-ncfJfLTR_VzLn81skz2WIkPnP1inagYqXzZcsKThhFPx7QnFEo6rCGgM4R2fiZtas4mREhOgnXemNhpI7r-WIPPR3JgUxlzGtrbmr4NAIs4eXzaMQp57PGB_hD1udhazuZ9QKUTGOY7Kl-O_uXhGnAcIsJXFQe7bBgEv_x3SIk83QWROeewU6Ngj2yxiDL737c3Z87xjEvLc-pVYPcKCaYrkqLicZvIG0G4SEFg2ov1WfY0YstbCuOOcU3AaFNp0SL9b6LEypyg4KcPYFimkMYgy";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute: ");
        }

        protected JSONObject doInBackground(String... urls) {
            Log.d(TAG, "doInBackground: " + urls);
            // call api
            try {
                URL url = new URL(link);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                int statusCode = urlConnection.getResponseCode();
                if (statusCode ==  200) {
                    InputStream it = new BufferedInputStream(urlConnection.getInputStream());
                    InputStreamReader read = new InputStreamReader(it);
                    BufferedReader buff = new BufferedReader(read);
                    StringBuilder dta = new StringBuilder();
                    String chunks ;
                    while((chunks = buff.readLine()) != null) {
                        dta.append(chunks);
                    }
                    String dataString = dta.toString();
                    JSONObject jsonObject = new JSONObject(dataString);
                    Log.d(TAG, "doInBackground: ");

                    return jsonObject;

                }
                else {
                    return null;
                }

            } catch (Exception ex) {
                return null;
            }
        }

        protected void onProgressUpdate(Integer... progress) {
            Log.d(TAG, "onProgressUpdate: " + progress);
        }

        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject == null) {
                // show alert
                return;
            }

            List<PlaceDetailModel> placeList = new ArrayList<>();
            try {
                JSONArray resultArrObject = jsonObject.getJSONArray("results");
                for (int i = 0; i < resultArrObject.length(); i++) {
                    JSONObject resultObject = (JSONObject) resultArrObject.get(i);

                    PlaceDetailModel placeDetailModel = new PlaceDetailModel();
                    placeDetailModel.setId(resultObject.getString("id"));
                    placeDetailModel.setName(resultObject.getString("name"));
//                    placeDetailModel.setAdrAddress(resultObject.getString("adr_address"));
//                    placeDetailModel.setFormattedAddress(resultObject.getString("formatted_address"));
//                    placeDetailModel.setFormattedPhoneNumber(resultObject.getString("formatted_phone_number"));
                    placeDetailModel.setPlaceId(resultObject.getString("place_id"));
                    placeDetailModel.setIcon(resultObject.getString("icon"));
                    //placeDetailModel.setTypes(jsonObject.accumulate("types", ));
                    //placeDetailModel.setUrl(resultObject.getString("url"));
//                    placeDetailModel.setInternationalPhoneNumber(resultObject.getString("international_phone_number"));
                    placeDetailModel.setVicinity(resultObject.getString("vicinity"));
                    JSONObject geoObject= resultObject.getJSONObject("geometry");
                    JSONObject locObject= geoObject.getJSONObject("location");
                    double lat = locObject.getDouble("lat");
                    double lng = locObject.getDouble("lng");
                    LocationModel locationModel = new LocationModel();
                    locationModel.setLat(locObject.getDouble("lat"));
                    locationModel.setLng(locObject.getDouble("lng"));
                    placeDetailModel.setLocationModel(locationModel);

                    placeList.add(placeDetailModel);
                }

                //int x = 0;


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
