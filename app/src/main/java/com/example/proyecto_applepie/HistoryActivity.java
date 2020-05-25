package com.example.proyecto_applepie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    ListView logList;
    ArrayList<JSONObject> jsonList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        logList = findViewById(R.id.logList);

        // getLogs request with volley *Stupid Volley*
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://34f3c2a3.ngrok.io/getlogs/",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++){
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = response.getJSONObject(i);
                                jsonList.add(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        logList.setAdapter(new LogAdapter(
                                HistoryActivity.this,
                                R.layout.layout_log,
                                jsonList));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Option to go to the settings screen
        if(item.getItemId() == R.id.mnSettings){
            Intent inSettings = new Intent(this, SettingsActivity.class);
            startActivity(inSettings);
            return true;
            // Option to launch onboard screen from the settings menu
        }else if(item.getItemId() == R.id.mnOnboard){
            Intent onBoardIntent = new Intent(this, OnBoarding.class);
            startActivity(onBoardIntent);
            return true;
        }else if(item.getItemId() == R.id.mnNewLog){
            Intent inNewLog = new Intent(this, NewLogActivity.class);
            startActivity(inNewLog);
            return true;
            // Option to logout
        }

        return super.onOptionsItemSelected(item);
    }
}
