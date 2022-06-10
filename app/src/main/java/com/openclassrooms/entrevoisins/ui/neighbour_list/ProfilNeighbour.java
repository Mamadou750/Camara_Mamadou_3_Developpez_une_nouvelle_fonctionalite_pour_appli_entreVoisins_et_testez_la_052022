package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;



import com.bumptech.glide.Glide;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;


import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfilNeighbour extends AppCompatActivity {

    @BindView(R.id.profile_name)
    TextView profile_name;
    @BindView(R.id.profile_picture)
    ImageView picture;
    @BindView(R.id.info_name)
    TextView name;
    @BindView(R.id.info_fb)
    TextView social;
    @BindView(R.id.profile_favorite)
    ImageView fab_favorite;
    @BindView(R.id.profile_return)
    ImageView back;
    @BindView(R.id.info_location)
    TextView adresse;
    @BindView(R.id.info_tel)
    TextView phone;
    @BindView(R.id.describe)
    TextView about;

    protected int mNeighbourId;
    protected Neighbour mNeighbour;
    private NeighbourApiService mApiService;
    public static String NEIGHBOUR = "NEIGHBOUR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_neighbour);
        mNeighbourId = (int) getIntent().getLongExtra(ProfilNeighbour.NEIGHBOUR, 0);
        mApiService = DI.getNeighbourApiService();
        mNeighbour = mApiService.getNeighbour(mNeighbourId);
        ButterKnife.bind(this);

        //scroll describe text
        about.setMovementMethod( new ScrollingMovementMethod());

        //Get profile information
        profile_name.setText(mNeighbour.getName());
        name.setText(mNeighbour.getName());
        social.setText("www.facebook.com/" + mNeighbour.getName());
        adresse.setText(mNeighbour.getAddress());
        phone.setText(mNeighbour.getPhoneNumber());
        about.setText(mNeighbour.getAboutMe());
        Glide.with(this).load(mNeighbour.getAvatarUrl()).centerCrop().into(picture);


        fab_favorite.setOnClickListener(v -> setFabFavorite());

        back.setOnClickListener(v -> this.finish());
    }

    public void setFabFavorite() {
        if (mApiService.isFavorite(mNeighbour)) {
            mApiService.removeFavorite(mNeighbour);
        } else {
            mApiService.addFavorite(mNeighbour);
        }

    }
}