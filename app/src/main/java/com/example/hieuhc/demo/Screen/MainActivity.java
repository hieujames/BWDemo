package com.example.hieuhc.demo.Screen;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.hieuhc.demo.Model.PlaceDetailModel;
import com.example.hieuhc.demo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DownloadFilesTask task = (DownloadFilesTask) new DownloadFilesTask().execute();
    }


    private class DownloadFilesTask extends AsyncTask<String, Integer, JSONObject> {
        String TAG = "DownloadFilesTask";
        String link = "https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJ7QIVsOqNGGARjr9Pym3agbs&key=AIzaSyDNFGAYONj3HwOkg56cuaFOxdJs2qf1VmU";

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

            PlaceDetailModel placeDetailModel = new PlaceDetailModel();
            try {
                placeDetailModel.setId(jsonObject.getString("id"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}