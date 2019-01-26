package com.example.pawan.whatsupcleaner;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.example.pawan.whatsupcleaner.adapters.DetailsAdapter;
import com.example.pawan.whatsupcleaner.adapters.DetailsAdapterCustom;
import com.example.pawan.whatsupcleaner.datas.Details;
import com.example.pawan.whatsupcleaner.innerdata.Audio;
import com.example.pawan.whatsupcleaner.tabs.Documents.AndroidTabLayoutActivity_doc;

import com.example.pawan.whatsupcleaner.tabs.Images.AndroidTabLayoutActivity_img;
import com.example.pawan.whatsupcleaner.tabs.Videos.AndroidTabLayoutActivity_video;

import java.util.ArrayList;
import java.util.List;

// TODO: 1/13/19 We imoplement the interface here
public class MainActivity extends AppCompatActivity implements DetailsAdapter.OnItemClickListener, DetailsAdapterCustom.OnItemClickListener {

    private static final int READ_EXTERNAL_STORAGE_PERMISSION_CODE = 1002;

    List<Details> datalist1;
    List<Details> datalist;
    RecyclerView recyclerView,recyclerView1;

    DetailsAdapterCustom detailsAdapter;

    ProgressDialog pr;
    public static Toast transitionToast;

    RelativeLayout loading;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loading = findViewById(R.id.loading);


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            //ask for permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_CODE);
        }
        //For Images,documents and Videos
        recyclerView = findViewById(R.id.recycle1);
        recyclerView.setHasFixedSize(true);

        recyclerView1 = findViewById(R.id.recycle);
        recyclerView1.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        datalist1 = new ArrayList<>();


        datalist1.add(
                new Details(
                        "Images",
                        400,
                        R.drawable.ic_image,
                        R.color.green
                )
        );
        datalist1.add(
                new Details(
                        "Documents",
                        1,
                        R.drawable.ic_folder,
                        R.color.orange
                )
        );
        datalist1.add(
                new Details(
                        "Videos",
                        5,
                        R.drawable.ic_video,
                        R.color.blue
                )
        );

        //Add implemented listener to constructor
        DetailsAdapter detailsAdapter1 = new DetailsAdapter(this, datalist1, this);
        recyclerView.setAdapter(detailsAdapter1);


        GridLayoutManager mGridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        recyclerView1.setLayoutManager(mGridLayoutManager);

        datalist = new ArrayList<>();

        datalist.add(
                new Details(
                        "Audio files",
                        400,
                        R.drawable.ic_library_music_black,
                        R.color.purple
                )
        );
        datalist.add(
                new Details(
                        "Voice files",
                        112,
                        R.drawable.ic_queue_music_black,
                        R.color.lightblue
                )
        );
        datalist.add(
                new Details(
                        "Wallpapers",
                        5,
                        R.drawable.ic_image,
                        R.color.maroon
                )
        );
        datalist.add(
                new Details(
                        "GIFs",
                        5,
                        R.drawable.ic_image,
                        R.color.lightpink
                )
        );

        detailsAdapter = new DetailsAdapterCustom(this, datalist, this);
        recyclerView1.setAdapter(detailsAdapter);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We will come back to this after
            }
        }
    }
    @Override
    public void onImagesClicked() {
        // TODO: 1/13/19 Let's try this on a device with whatsapp images
        //We still need to verify permission here too
        Intent intent = new Intent(MainActivity.this, AndroidTabLayoutActivity_img.class);
        startActivity(intent);

    }

    @Override
    public void onDocumentsClicked() {

        Intent intent = new Intent(MainActivity.this, AndroidTabLayoutActivity_doc.class);
        startActivity(intent);
    }

    @Override
    public void onVideosClicked() {

        Intent intent = new Intent(MainActivity.this, AndroidTabLayoutActivity_video.class);
        startActivity(intent);
    }
    @Override
    public void onAudiosClicked() {

        Intent intent = new Intent(MainActivity.this, Audio.class);
        startActivity(intent);
    }

    @Override
    public void onGifsClicked() {

        Intent intent = new Intent(MainActivity.this, AndroidTabLayoutActivity_video.class);
        startActivity(intent);
    }

    @Override
    public void onWallpapersClicked() {

        Intent intent = new Intent(MainActivity.this, AndroidTabLayoutActivity_img.class);
        startActivity(intent);
    }

    @Override
    public void onVoiceClicked() {

        Toast.makeText(this, "Need to be Implemented", Toast.LENGTH_SHORT).show();
    }

}

