package com.example.krunal.OneSports;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.krunal.OneSports.Scheduler.SchedulerUtils;
import com.example.krunal.OneSports.model.ScheduleModel;
import com.example.krunal.OneSports.utilities.NBAAdapter;
import com.example.krunal.OneSports.utilities.NetworkUtils;
import com.example.krunal.OneSports.utilities.ScheduleAdapter;
import com.example.krunal.OneSports.utilities.parseJSON;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ScheduleGames_Nfl extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private RecyclerView rv;
    private final String TAG = "schedulegames_NFL";
    private NBAAdapter nbaAdapter2;
    private static final int LOADER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = (RecyclerView) findViewById(R.id.nba_response_result);

        rv.setAdapter(nbaAdapter2);
        rv.setLayoutManager(new LinearLayoutManager(this));


        Log.d(TAG, "INSIDE ONCREATE");

        ScheduleGames_Nfl.fetchSchedule task = new fetchSchedule();
        task.execute();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }




    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        SchedulerUtils.stopScheduledNewsLoad(this);
    }

    public class fetchSchedule extends AsyncTask<URL, Void, ArrayList<ScheduleModel>> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected ArrayList<ScheduleModel> doInBackground(URL... params) {
            Log.d(TAG, "INSIDE DOINBACKGROUND");
            ArrayList<ScheduleModel> resultSchedule = null;
            String scheduleResponse = "";
            URL url = null;
            try {
                url = new URL("https://api.mysportsfeeds.com/v1.2/pull/nhl/2017-2018-regular/full_game_schedule.json");
                Log.d(TAG, "URL is>>>>>>>>>>"+url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try{
                scheduleResponse = NetworkUtils.getScheduleNBA(url);

            }catch (Exception e){

                e.printStackTrace();
            }
            try {
                resultSchedule = parseJSON.parseNBAScheduleJSON(scheduleResponse);

                Log.d(TAG, "$$$$Getting prase dataN$$$$$$");
            }catch (Exception e){

                e.printStackTrace();
            }

            return resultSchedule;
        }

        @Override
        protected void onPostExecute(final ArrayList<ScheduleModel> scheduleModels) {

            super.onPostExecute(scheduleModels);


            if(scheduleModels!=null){
                ScheduleAdapter adapter = new ScheduleAdapter(scheduleModels, new ScheduleAdapter.ItemClickListener() {

                    @Override
                    public void onItemClick(int clickedItemIndex) {
                        String date = scheduleModels.get(clickedItemIndex).getGameDate();
                        Toast.makeText(ScheduleGames_Nfl.this,"Showing schedule for:"+date,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ScheduleGames_Nfl.this, ScheduleDetails.class);
                        intent.putExtra("homeTeam", scheduleModels.get(clickedItemIndex).getHomeTeam());
                        intent.putExtra("awayTeam", scheduleModels.get(clickedItemIndex).getAwayTeam());
                        intent.putExtra("gameDate", scheduleModels.get(clickedItemIndex).getGameDate());
                        intent.putExtra("location", scheduleModels.get(clickedItemIndex).getGameLocation());
                        startActivity(intent);
                    }
                });
                rv.setAdapter(adapter);
            }
        }


    }

    @Override
    public void onBackPressed() {


        super.onBackPressed();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_nba) {
            Intent intent = new Intent(this, NbaActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            //String gameName = null;
            intent.putExtra("gameName", "nba");
            startActivity(intent);

        }
        if(id==R.id.nav_nfl){
            Intent intent = new Intent(this, NbaActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            //String gameName = null;
            intent.putExtra("gameName", "nfl");
            startActivity(intent);

        }
        if(id==R.id.nav_all){
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            //String gameName = null;
            intent.putExtra("gameName", "all");
            startActivity(intent);
        }
        if(id == R.id.nav_schedule_nba){
            Intent intent = new Intent(this, ScheduleGames_Nba.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            //String gameName = null;7
//            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//            drawer.closeDrawer(GravityCompat.START);
            intent.putExtra("gameName", "nba_schedule");
            startActivity(intent);
        }
        if(id == R.id.nav_schedule_nfl){
            Intent intent = new Intent(this, ScheduleGames_Nfl.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//            drawer.closeDrawer(GravityCompat.START);
            intent.putExtra("gameName", "nfl_schedule");
            startActivity(intent);
        }
        if(id == R.id.nav_nba_standings){
            Intent intent = new Intent(this, NbaSchedule.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;

    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return true;

    }
}
