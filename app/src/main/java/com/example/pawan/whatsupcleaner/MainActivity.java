package com.example.pawan.whatsupcleaner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.pawan.whatsupcleaner.Adapters.DetailsAdapter;
import com.example.pawan.whatsupcleaner.Adapters.DetailsAdapterCustom;
import com.example.pawan.whatsupcleaner.Datas.Details;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Details> datalist1;
    List<Details> datalist;

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //For Images,documents and Videos
        recyclerView = findViewById(R.id.recycle1);
        recyclerView.setHasFixedSize(true);
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

        DetailsAdapter detailsAdapter1 = new DetailsAdapter(this,datalist1);
        recyclerView.setAdapter(detailsAdapter1);


        //for Audios,Voice,Wallpapers,Gifs
        recyclerView = findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        recyclerView.setLayoutManager(mGridLayoutManager);

        datalist = new ArrayList<>();

        datalist.add(
                new Details(
                        "Audio files",
                        400,
                        R.drawable.ic_image,
                        R.color.green
                )
        );
        datalist.add(
                new Details(
                        "Voice messages",
                        1,
                        R.drawable.ic_image,
                        R.color.green
                )
        );
        datalist.add(
                new Details(
                        "Wallpapers",
                        5,
                        R.drawable.ic_image,
                        R.color.green
                )
        );
        datalist.add(
                new Details(
                        "GIFs",
                        5,
                        R.drawable.ic_image,
                        R.color.green
                )
        );


        DetailsAdapterCustom detailsAdapter = new DetailsAdapterCustom(this,datalist);
        recyclerView.setAdapter(detailsAdapter);

    }
}
