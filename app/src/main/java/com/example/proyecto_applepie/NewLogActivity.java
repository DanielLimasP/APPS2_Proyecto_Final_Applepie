package com.example.proyecto_applepie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class NewLogActivity extends AppCompatActivity {

    EditText concept, amount, paypalme;
    Button up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_log);

        concept = findViewById(R.id.edtConcept);
        amount = findViewById(R.id.edtAmount);
        paypalme = findViewById(R.id.edtPaypal);
        up = findViewById(R.id.btnUpload);

        if(ReaderActivity.paypalmeLink != null){
            paypalme.setText(ReaderActivity.paypalmeLink);
        }

    }

    public void uploadLog(View view){

        up.setBackground(ContextCompat.getDrawable(NewLogActivity.this, R.drawable.roundbtnselected));
        up.setTextColor(Color.WHITE);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userEmail", MainScreen.personEmail);
            jsonObject.put("concept", concept.getText().toString());
            jsonObject.put("amount", Integer.parseInt(amount.getText().toString()));
            if(ReaderActivity.paypalmeLink != null){
                jsonObject.put("paypalme", ReaderActivity.paypalmeLink);
            }else{
                jsonObject.put("paypalme", paypalme.getText().toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                "http://34f3c2a3.ngrok.io/uploadlog",
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(NewLogActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        concept.setText("");
        amount.setText("");
        paypalme.setText("");

        Toast.makeText(NewLogActivity.this, "Log succesfully created", Toast.LENGTH_LONG).show();

        finish();

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

}
