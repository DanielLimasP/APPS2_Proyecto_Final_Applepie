package com.example.proyecto_applepie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context = context;
    }

    // Arrays or something
    public int[] slide_images = {
            R.drawable.andromeda,
            R.drawable.comet,
            R.drawable.abduction,
            R.drawable.alien,
            R.drawable.planets,
            R.drawable.pplogobig
    };

    public String[] slide_headings = {
            "Bienvenido a  PayPalQR",
            "¿Cómo configuro PayPal.Me?",
            "Busca PayPal.Me dentro de la configuración de tu cuenta",
            "Sigue los pasos dentro de PayPal",
            "Tu link dentro de la aplicación",
            "Genera y recibe, escanea y paga"

    };

    public String[] slide_descriptions = {
            "La aplicación que te ayudara a realizar pagos con PayPal en establecimientos pequeños",
            "Para empezar la configuración de la aplicación, empieza yendo a tu cuenta de PayPal",
            "Ahora entra en 'configuración' y busca la sección de PayPal.Me",
            "Sigue los pasos y configura tu cuenta personal de PayPal.Me",
            "Una vez terminada la configuración de tu cuenta, dentro de PayPalQR, entra en la sección de configuración y pega tu link",
            "Genera tu código QR y has o recibe pagos por PayPal de una manera más sencilla."
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    public Object instantiateItem(ViewGroup container, int position){
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView imgView = (ImageView) view.findViewById(R.id.slideImg);
        TextView txtView1 = (TextView) view.findViewById(R.id.slideHead);
        TextView txtView2 = (TextView) view.findViewById(R.id.slideText);

        imgView.setImageResource(slide_images[position]);
        txtView1.setText(slide_headings[position]);
        txtView2.setText(slide_descriptions[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
