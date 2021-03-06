package com.example.krunal.OneSports.Scheduler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.example.krunal.OneSports.Database.DBHelper;
import com.example.krunal.OneSports.Database.DBUtils;
import com.example.krunal.OneSports.model.NBAData;
import com.example.krunal.OneSports.utilities.NetworkUtils;
import com.example.krunal.OneSports.utilities.parseJSON;
import com.firebase.jobdispatcher.JobParameters;

import java.util.ArrayList;


public class Refresher extends com.firebase.jobdispatcher.JobService {

    private static final String TAG="NewsService";
    @Override
    public boolean onStartJob(JobParameters job) {
        new AsyncTask() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected Object doInBackground(Object[] params) {

                newsrefresh(Refresher.this);

                return null;


            }
        }.execute();

        Toast.makeText(Refresher.this, "Scores updated from " + "server!",
                Toast.LENGTH_SHORT).show();


        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void newsrefresh(Context context) {
        try {
           // URL newsURL = NetworkUtils.buildURL();
            ArrayList<NBAData> nba= null;
            ArrayList<NBAData> nfl= null;

            String jsonNBA = NetworkUtils.getResponseFromHttpUrl();
            nba = parseJSON.parseJsonData(context , jsonNBA);


            String jsonNFL = NetworkUtils.getResponseFromHttpUrlNfl();
            nfl = parseJSON.parseJsonData(context , jsonNFL);


            ArrayList<NBAData> results=new ArrayList<>();

            results.addAll(nba);
            results.addAll(nfl);



            SQLiteDatabase db = new DBHelper(context).getWritableDatabase();
            DBUtils.insertnews(db, results);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
