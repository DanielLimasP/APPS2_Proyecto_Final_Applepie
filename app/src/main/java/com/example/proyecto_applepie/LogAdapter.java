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
        amount = convertView.findViewById(R.id.logPaypalme);
        image = convertView.findViewById(R.id.logImg);

        try {
            name.setText(objects.get(position).getString("first_name"));
            lastname.setText(objects.get(position).getString("last_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;


    }
}
