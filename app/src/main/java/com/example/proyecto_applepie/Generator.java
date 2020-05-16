package com.example.proyecto_applepie;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class Generator extends Fragment {

    private View.OnClickListener clickListener;
    EditText qrEdtTxt;
    Button btnGenerate;
    ImageView imgQrDisplay;

    public Generator() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.fragment_generator, container, false);

        qrEdtTxt = ll.findViewById(R.id.qrEdtTxt);
        btnGenerate = ll.findViewById(R.id.btnGenerate);
        imgQrDisplay = ll.findViewById(R.id.imgQrDisplay);;
        btnGenerate = ll.findViewById(R.id.btnGenerate);
        btnGenerate.setOnClickListener(clickListener);

        return ll;
    }

    public View.OnClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
