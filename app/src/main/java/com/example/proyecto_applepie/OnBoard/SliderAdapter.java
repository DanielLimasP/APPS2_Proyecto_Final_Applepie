package com.example.proyecto_applepie.OnBoard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.proyecto_applepie.R;

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
            R.drawable.abduction
    };

    public String[] slide_headings = {
            "EAT",
            "SLEEP",
            "CODE"
    };

    public String[] slide_descriptions = {
            "Lorem",
            "Ipsum",
            "Dolor"
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
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
        container.removeView((RelativeLayout) object);
    }
}
