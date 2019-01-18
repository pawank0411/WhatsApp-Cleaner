package com.example.pawan.whatsupcleaner;

import android.Manifest;
import android.content.ContentProvider;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import com.example.pawan.whatsupcleaner.Adapters.DetailsAdapter;
import com.example.pawan.whatsupcleaner.Adapters.DetailsAdapterCustom;
import com.example.pawan.whatsupcleaner.Datas.Details;
import com.example.pawan.whatsupcleaner.InnnerData.InnerData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// TODO: 1/13/19 We imoplement the interface here
public class MainActivity extends AppCompatActivity implements DetailsAdapter.OnItemClickListener {

    private static final int READ_EXTERNAL_STORAGE_PERMISSION_CODE = 1002;
    List<Details> datalist1;
    List<Details> datalist;
    RecyclerView recyclerView,recyclerView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            //ask for permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_CODE);
        }
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

        //Add implemented listener to constructor
        DetailsAdapter detailsAdapter1 = new DetailsAdapter(this,datalist1, this);
        recyclerView.setAdapter(detailsAdapter1);


        //for Audios,Voice,Wallpapers,Gifs
        recyclerView1 = findViewById(R.id.recycle);
        recyclerView1.setHasFixedSize(true);

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
                        "Voice messages",
                        1,
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


//check for
        DetailsAdapterCustom detailsAdapter = new DetailsAdapterCustom(this,datalist);
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



        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //Need to ask permission again or close the app
        } else {
            String path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Images";


            File directory = new File(path);
            ArrayList<File> fileList = new ArrayList<>();
            File[] results = directory.listFiles();
            if (results != null) {
                for(File file : results) {
                    //Check if it is a file or a folder
                    if (file.isDirectory()) {
                        //For now we skip it
                    } else {
                        //Still verify if the file is an image in whatsapp preferred format(jpg)
                        if (file.getName().endsWith(".jpg"))
                            fileList.add(file);
                    }
                }
                Log.e("Files", "files found: " + fileList.toString());
            } else {
                Log.e("Files", "No files found in " + directory.getName());
            }
        }
        Intent intent = new Intent(this,InnerData.class);
        //Doubt
        intent.putExtra("Images",fileList());
        startActivity(intent);
    }

    @Override
    public void onDocumentsClicked() {

    }

    @Override
    public void onVideosClicked() {

    }
}
