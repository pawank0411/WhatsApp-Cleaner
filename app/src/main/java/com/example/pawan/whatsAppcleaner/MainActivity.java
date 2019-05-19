package com.example.pawan.whatsAppcleaner;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.pawan.whatsAppcleaner.adapters.DetailsAdapter;
import com.example.pawan.whatsAppcleaner.adapters.DetailsAdapterCustom;
import com.example.pawan.whatsAppcleaner.datas.Details;
import com.example.pawan.whatsAppcleaner.tabs.Audio.AndroidTabLayoutActivity_aud;
import com.example.pawan.whatsAppcleaner.tabs.Documents.AndroidTabLayoutActivity_doc;

import com.example.pawan.whatsAppcleaner.tabs.Gifs.AndroidTabLayoutActivity_gifs;
import com.example.pawan.whatsAppcleaner.tabs.Images.AndroidTabLayoutActivity_img;
import com.example.pawan.whatsAppcleaner.tabs.Images.Images_rec;
import com.example.pawan.whatsAppcleaner.tabs.Videos.AndroidTabLayoutActivity_video;
import com.example.pawan.whatsAppcleaner.tabs.Wallpaper.wallpaper;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

// TODO: 1/13/19 We imoplement the interface here
public class MainActivity extends AppCompatActivity implements DetailsAdapter.OnItemClickListener, DetailsAdapterCustom.OnItemClickListener {

    private static final int EXTERNAL_STORAGE_PERMISSION_CODE = 1002;
    public static Toast transitionToast;
    List<Details> datalist1;
    List<Details> datalist;
    TextView total_data;
    RecyclerView recyclerView, recyclerView1;
    DetailsAdapterCustom detailsAdaptercustom;
    ProgressDialog pr;
    private double len;
    private String byteMake, path;
    private File directory;
    private String data_img, data_doc, data_vid, data_aud, data_gif, data_wall, data_voice, tot_dat;
    private long sum, size_img, size_doc, size_vid, size_wall, size_aud, size_gif, size_voice;
    private static final long GiB = 1024 * 1024 * 1024;
    private static final long MiB = 1024 * 1024;
    private static final long KiB = 1024;
    RelativeLayout loading;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loading = findViewById(R.id.loading);

        total_data = findViewById(R.id.data);

        boolean hasPermission =
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;

        if (!hasPermission) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(this.findViewById(android.R.id.content), "Please Grant Permissions to read WhatsApp Media files in" +
                        " Setting", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CODE);
                        }
                    }
                }).show();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CODE);
                }
            }

        }

        /*Size for Images folder*/
        path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Images";

       size_img =  FileUtils.sizeOfDirectory(new File(path));
       data_img = Formatter.formatShortFileSize(MainActivity.this, size_img);

        /*Size for Documents folder*/

        path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Documents";

        directory = new File(path);


        size_doc =  FileUtils.sizeOfDirectory(new File(path));
        data_doc = Formatter.formatShortFileSize(MainActivity.this, size_doc);


        /*Size for Videos folder*/
        path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Video";

        size_vid =  FileUtils.sizeOfDirectory(new File(path));
        data_vid = Formatter.formatShortFileSize(MainActivity.this, size_vid);

        /*Size for Audios folder*/
        path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Audio";


        size_aud =  FileUtils.sizeOfDirectory(new File(path));
        data_aud = Formatter.formatShortFileSize(MainActivity.this, size_aud);


        /*Size for Wallpaper folder*/
        path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WallPaper";


        size_wall =  FileUtils.sizeOfDirectory(new File(path));
        data_wall = Formatter.formatShortFileSize(MainActivity.this, size_wall);

        /*Size for Gifs folder*/
        path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Animated Gifs";


        size_gif =  FileUtils.sizeOfDirectory(new File(path));
        data_gif = Formatter.formatShortFileSize(MainActivity.this, size_gif);

        /*Size for Voice Notes folder*/
        path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Voice Notes";


        size_voice =  FileUtils.sizeOfDirectory(new File(path));
        data_voice = Formatter.formatShortFileSize(MainActivity.this, size_voice);


       sum = size_img + size_doc + size_vid + size_voice + size_gif + size_wall + size_aud;
        tot_dat = Formatter.formatShortFileSize(MainActivity.this, sum);


        total_data.setText(tot_dat);
        //For Images,documents and Videos
        recyclerView = findViewById(R.id.recycle1);
        recyclerView.setHasFixedSize(true);

        recyclerView1 = findViewById(R.id.recycle);
        recyclerView1.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        datalist1 = new ArrayList<>();


        datalist1.add(new Details(
                "Images",
                data_img,
                R.drawable.ic_image,
                R.color.green));
        datalist1.add(new Details(
                "Documents",
                data_doc,
                R.drawable.ic_folder,
                R.color.orange));
        datalist1.add(new Details(
                "Videos",
                data_vid,
                R.drawable.ic_video,
                R.color.blue));

        //Add implemented listener to constructor
        DetailsAdapter detailsAdapter1 = new DetailsAdapter(this, datalist1, this);
        recyclerView.setAdapter(detailsAdapter1);


        GridLayoutManager mGridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        recyclerView1.setLayoutManager(mGridLayoutManager);

        datalist = new ArrayList<>();

        datalist.add(new Details(
                "Audio files",
                data_aud,
                R.drawable.ic_library_music_black,
                R.color.purple));
        datalist.add(new Details(
                "Voice files",
                data_voice,
                R.drawable.ic_queue_music_black,
                R.color.lightblue));
        datalist.add(new Details(
                "Wallpapers",
                data_wall,
                R.drawable.ic_image,
                R.color.maroon));
        datalist.add(new Details(
                "GIFs",
                data_gif,
                R.drawable.ic_image,
                R.color.lightpink));

        detailsAdaptercustom = new DetailsAdapterCustom(this, datalist, this);
        recyclerView1.setAdapter(detailsAdaptercustom);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //We have permission
                //We will come back to this after
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Snackbar.make(this.findViewById(android.R.id.content), "Please Grant Permissions to read WhatsApp Media Files", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CODE);
                            }
                        }
                    }).show();
                }
            }
        }
    }
    public static long getFileFolderSize(File dir) {
        long size = 0;
        File[] results = dir.listFiles();
        if (results != null) {
            if (dir.isDirectory()) {
                for (File file : results) {
                    if (file.isFile()) {
                        size += file.length();
                    } else
                        size += getFileFolderSize(file);
                }
            }
            else if (dir.isFile()) {
                size += dir.length();
            }
        }
        return size;
    }


    @Override
    public void onImagesClicked() {
        // TODO: 1/13/19 Let's try this on a device with whatsapp images
        //We still need to verify permission here too
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(this.findViewById(android.R.id.content), "Please Grant Permissions to read WhatsApp images", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CODE);
                        }
                    }
                }).show();
            }
        }else {

            Intent intent = new Intent(MainActivity.this, AndroidTabLayoutActivity_img.class);
            startActivity(intent);
        }
    }

    @Override
    public void onDocumentsClicked() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(this.findViewById(android.R.id.content), "Please Grant Permissions to read WhatsApp documents", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CODE);
                        }
                    }
                }).show();
            }
        } else {
            Intent intent = new Intent(MainActivity.this, AndroidTabLayoutActivity_doc.class);
            startActivity(intent);
        }
    }

    @Override
    public void onVideosClicked() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(this.findViewById(android.R.id.content), "Please Grant Permissions to read WhatsApp videos", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CODE);
                        }
                    }
                }).show();
            }
        } else {
            Intent intent = new Intent(MainActivity.this, AndroidTabLayoutActivity_video.class);
            startActivity(intent);
        }
    }

    @Override
    public void onAudiosClicked() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(this.findViewById(android.R.id.content), "Please Grant Permissions to read WhatsApp audio", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CODE);
                        }
                    }
                }).show();
            }
        } else {
            Intent intent = new Intent(MainActivity.this, AndroidTabLayoutActivity_aud.class);
            startActivity(intent);
        }
    }

    @Override
    public void onGifsClicked() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(this.findViewById(android.R.id.content), "Please Grant Permissions to read WhatsApp Gifs ", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CODE);
                        }
                    }
                }).show();
            }
        } else {
            Intent intent = new Intent(MainActivity.this, AndroidTabLayoutActivity_gifs.class);
            startActivity(intent);
        }
    }

    @Override
    public void onWallpapersClicked() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(this.findViewById(android.R.id.content), "Please Grant Permissions to read WhatsApp Wallpaper", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CODE);
                        }
                    }
                }).show();
            }
        } else {
            Intent intent = new Intent(MainActivity.this, wallpaper.class);
            startActivity(intent);
        }
    }

    @Override
    public void onVoiceClicked() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(this.findViewById(android.R.id.content), "Please Grant Permissions to read WhatsApp Voice", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CODE);
                        }
                    }
                }).show();
            }
        } else {
            Toast.makeText(this, "Need to be Implemented", Toast.LENGTH_SHORT).show();
        }
    }
}

