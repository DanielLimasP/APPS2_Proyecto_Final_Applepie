package com.example.proyecto_applepie;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;

public class LogAdapter extends ArrayAdapter<JSONObject> {

    Context context;
    int resource;
    List<JSONObject> objects;

    public LogAdapter(@NonNull Context context, int resource, @NonNull List<JSONObject> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
            convertView = layoutInflater.inflate(resource, parent, false);
        }

        TextView concept, amount, paypalme;
        ImageView image;

        concept = convertView.findViewById(R.id.logConcept);
        amount = convertView.findViewById(R.id.logAmount);
        paypalme = convertView.findViewById(R.id.logPaypalme);
        image = convertView.findViewById(R.id.logImg);

        Random r = new Random();
        int imageInt = r.nextInt(10);

        switch(imageInt){
            case 1:
                image.setImageResource(R.drawable.user);
                break;
            case 2:
                image.setImageResource(R.drawable.user2);
                break;
            case 3:
                image.setImageResource(R.drawable.user3);
                break;
            case 4:
                image.setImageResource(R.drawable.user4);
                break;
            case 5:
                image.setImageResource(R.drawable.user5);
                break;
            case 6:
                image.setImageResource(R.drawable.user6);
                break;
            case 7:
                image.setImageResource(R.drawable.user7);
                break;
            case 8:
                image.setImageResource(R.drawable.user8);
                break;
            case 9:
                image.setImageResource(R.drawable.user9);
                break;
            case 10:
                image.setImageResource(R.drawable.user10);
                break;
            case 11:
                image.setImageResource(R.drawable.user11);
                break;
        }

        try {
            concept.setText(objects.get(position).getString("concept") +".");
            amount.setText(objects.get(position).getString("amount")+ " pesos MXN.");
            paypalme.setText(objects.get(position).getString("paypalme")+ "." );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;


    }
}
