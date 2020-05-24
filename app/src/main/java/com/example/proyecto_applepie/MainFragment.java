package com.example.proyecto_applepie;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private View.OnClickListener clickListener;
    TextView pName;
    TextView pEmail;
    TextView pPaypal;
    ImageView pImage;
    Button pButton;
    String ppMe = MainScreen.paypalMe;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.fragment_main, container, false);
        pName = ll.findViewById(R.id.name);
        pEmail = ll.findViewById(R.id.email);
        pPaypal = ll.findViewById(R.id.paypal);
        pImage = ll.findViewById(R.id.photo);
        pButton = ll.findViewById(R.id.button);
        pButton.setOnClickListener(clickListener);

        if (MainScreen.acct != null) {
            String personName = MainScreen.acct.getDisplayName();
            String personEmail = MainScreen.acct.getEmail();

            pName.setText(personName);
            pEmail.setText(personEmail);
            // URI to Image glide implementation (Not working anymore)
            //Glide.with(this).load(profileImg).into(profileImageView);
        }
        pPaypal.setText(ppMe);
        return ll;
    }

    public View.OnClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

}
