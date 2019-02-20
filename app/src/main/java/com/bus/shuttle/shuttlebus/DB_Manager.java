package com.bus.shuttle.shuttlebus;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DB_Manager {
    private String urlPath;
    private String str_user_id;
    private String str_datetime;
    private String str_latitude;
    private String str_longitude;
    private ArrayList<String> results;

    private final String riding_information_UrlPath = "http://222.120.142.178/user_riding/riding_user_information.php";
    public ArrayList<String> riding_user_information(String str_user_id, String str_datetime,
                                                     String str_latitude, String str_longitude){
        urlPath = riding_information_UrlPath;
        this.str_user_id = str_user_id;
        this.str_datetime = str_datetime;
        this.str_latitude = str_latitude;
        this.str_longitude = str_longitude;

        try{
            results = new RidingPostUser().execute().get();

        } catch (InterruptedException e){
            e.printStackTrace();
        } catch (ExecutionException e){
            e.printStackTrace();
        }
        return results;
    }
    class RidingPostUser extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected  ArrayList<String> doInBackground(Void... voids)  {


            try {
                URL url = new URL(urlPath);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setUseCaches(false);
                con.setRequestProperty("Accept-Charset", "UTF-8");
                con.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8");

                String param = "str_user_id=" + str_user_id + "&str_datetime=" + str_datetime + "&str_latitude=" + str_latitude + "&str_longitude=" + str_longitude;
                Log.e(" abc",param);

                OutputStream outputStream = con.getOutputStream();
                outputStream.write(param.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                BufferedReader rd = null;
                ArrayList<String> qResults = new ArrayList<>();

                rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                String line = "";
                while ((line = rd.readLine()) != null) {
                Log.d("BufferedReader",line);
                    if (line != null) {
                        qResults.add(line);
                    }
                }

                return qResults;

            } catch(UnsupportedEncodingException e) {
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;

        }
        @Override
        protected  void onPostExecute(ArrayList<String> qResults){
            super.onPostExecute(qResults);
        }


    }


}
