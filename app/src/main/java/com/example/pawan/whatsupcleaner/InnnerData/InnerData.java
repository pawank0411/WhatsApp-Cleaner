package com.example.pawan.whatsupcleaner.InnnerData;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.pawan.whatsupcleaner.Adapters.InnerDetailsAdapter;
import com.example.pawan.whatsupcleaner.Datas.Details;
import com.example.pawan.whatsupcleaner.MainActivity;
import com.example.pawan.whatsupcleaner.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InnerData extends AppCompatActivity {

    List<Details>  innerdatalist;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inner_activity);



        recyclerView = findViewById(R.id.card_view1);
        recyclerView.setHasFixedSize(true);

        innerdatalist = new ArrayList<>();

        innerdatalist.add(
                new Details(
                    "Image 1",
                        R.drawable.ic_launcher_background
                )
        );
        innerdatalist.add(
                new Details(
                        "Image 1",R.drawable.ic_launcher_background
                )
        );
        innerdatalist.add(
                new Details(
                        "Image 1",
                        R.drawable.ic_launcher_background
                )
        );
        innerdatalist.add(
                new Details(
                        "Image 1",R.drawable.ic_launcher_background
                )
        );
        innerdatalist.add(
                new Details(
                        "Image 1",R.drawable.ic_launcher_background
                )
        );

        InnerDetailsAdapter innerDetailsAdapter = new InnerDetailsAdapter(this,innerdatalist);
        recyclerView.setAdapter(innerDetailsAdapter);

    }
}
