package com.example.pawan.whatsupcleaner.InnnerData;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.pawan.whatsupcleaner.Adapters.InnerDetailsAdapter;
import com.example.pawan.whatsupcleaner.Datas.Details;
import com.example.pawan.whatsupcleaner.R;

import java.util.ArrayList;
import java.util.List;

public class InnerData extends AppCompatActivity {

    List<Details>  datalist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inner_content);


        RecyclerView recyclerView = findViewById(R.id.card_view1);

        datalist = new ArrayList<>();

        datalist.add(
                new Details(
                    "Image 1",12
                )
        );


        InnerDetailsAdapter innerDetailsAdapter = new InnerDetailsAdapter(this,datalist);
        recyclerView.setAdapter(innerDetailsAdapter);
    }
}
