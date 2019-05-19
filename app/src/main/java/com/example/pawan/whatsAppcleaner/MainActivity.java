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
import com.example.pawan.whatsAppcleaner.tabs.Videos.AndroidTabLayoutActivity_video;
import com.example.pawan.whatsAppcleaner.tabs.Wallpaper.wallpaper;

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
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        boolean hasPermission =
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;

        if (!hasPermission) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(this.findViewById(android.R.id.content), "Please Grant Permissions to read WhatsApp images", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CODE);
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
        NumberFormat format_img = new DecimalFormat("#.##");
        format_img.setMaximumFractionDigits(2);
        format_img.setMinimumFractionDigits(2);
        path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Images";

        directory = new File(path);

        size_img = getFileFolderSize(directory);

//        double size_img = (double) size / 1024 / 1024 / 1024;
//        String s = "GB";
//        if (size_img < 1) {
//            size_img = (double) size / 1024 /1024;
//            s = " MB";
//        }if (size_img < 1){
//            size_img = (double) size / 1024;
//            s = "KB" ;
//        }

        if (size_img > GiB) {
            len = size_img / GiB;
            byteMake = "GB";
             data_img = format_img.format(size_img / GiB) + " GB";
        } else if (size_img > MiB) {
            len = size_img / MiB;
            byteMake = "MB";
            data_img  = format_img.format(size_img / MiB) + " MB";
        } else if (size_img > KiB) {
            len = size_img/ KiB;
            byteMake = "KB";
            data_img = format_img.format(size_img / KiB) + " KB";
        }

        /*Size for Documents folder*/
        NumberFormat format_doc = new DecimalFormat("#.##");
        format_doc.setMaximumFractionDigits(2);
        format_doc.setMinimumFractionDigits(2);
        path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Documents";

        directory = new File(path);

        size_doc = getFileFolderSize(directory);

        if (size_doc > GiB) {
            len = size_doc / GiB;
            byteMake = "GB";
            data_doc = format_img.format(size_doc / GiB) + " GB";
        } else if (size_doc > MiB) {
            len = size_doc / MiB;
            byteMake = "MB";
            data_doc  = format_img.format(size_doc / MiB) + " MB";
        } else if (size_doc > KiB) {
            len = size_doc / KiB;
            byteMake = "KB";
            data_doc = format_img.format(size_doc / KiB) + " KB";
        }

        /*Size for Videos folder*/
        NumberFormat format_vid = new DecimalFormat("#.##");
        format_vid.setMaximumFractionDigits(2);
        format_vid.setMinimumFractionDigits(2);
        path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Video";

        directory = new File(path);

        size_vid = getFileFolderSize(directory);

        if (size_vid > GiB) {
            len = size_vid / GiB;
            byteMake = "GB";
            data_vid = format_img.format(size_vid / GiB) + " GB";
        } else if (size_vid > MiB) {
            len = size_vid / MiB;
            byteMake = "MB";
            data_vid  = format_img.format(size_vid / MiB) + " MB";
        } else if (size_vid > KiB) {
            len = size_vid / KiB;
            byteMake = "KB";
            data_vid = format_img.format(size_vid / KiB) + " KB";
        }

        /*Size for Audios folder*/
        NumberFormat format_aud = new DecimalFormat("#.##");
        format_aud.setMaximumFractionDigits(2);
        format_aud.setMinimumFractionDigits(2);
        path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Audio";

        directory = new File(path);

        size_aud = getFileFolderSize(directory);

        if (size_aud > GiB) {
            len = size_aud / GiB;
            byteMake = "GB";
            data_aud = format_img.format(size_aud / GiB) + " GB";
        } else if (size_aud > MiB) {
            len = size_aud / MiB;
            byteMake = "MB";
            data_aud  = format_img.format(size_aud / MiB) + " MB";
        } else if (size_aud > KiB) {
            len = size_aud / KiB;
            byteMake = "KB";
            data_aud = format_img.format(size_aud / KiB) + " KB";
        }

        /*Size for Wallpaper folder*/
        NumberFormat format_wal = new DecimalFormat("#.##");
        format_wal.setMaximumFractionDigits(2);
        format_wal.setMinimumFractionDigits(2);
        path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WallPaper";

        directory = new File(path);

        size_wall = getFileFolderSize(directory);

        if (size_wall > GiB) {
            len = size_wall / GiB;
            byteMake = "GB";
            data_wall = format_img.format(size_wall / GiB) + " GB";
        } else if (size_wall > MiB) {
            len = size_wall / MiB;
            byteMake = "MB";
            data_wall  = format_img.format(size_wall / MiB) + " MB";
        } else if (size_wall > KiB) {
            len = size_wall / KiB;
            byteMake = "KB";
            data_wall = format_img.format(size_wall / KiB) + " KB";
        }

        /*Size for Gifs folder*/
        NumberFormat format_gif = new DecimalFormat("#.##");
        format_gif.setMaximumFractionDigits(2);
        format_gif.setMinimumFractionDigits(2);
        path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Animated Gifs";

        directory = new File(path);

        size_gif = getFileFolderSize(directory);

        if (size_gif > GiB) {
            len = size_gif / GiB;
            byteMake = "GB";
            data_gif = format_img.format(size_gif / GiB) + " GB";
        } else if (size_gif > MiB) {
            len = size_gif / MiB;
            byteMake = "MB";
            data_gif  = format_img.format(size_gif / MiB) + " MB";
        } else if (size_gif > KiB) {
            len = size_gif / KiB;
            byteMake = "KB";
            data_gif = format_img.format(size_gif / KiB) + " KB";
        }

        /*Size for Voice Notes folder*/
        NumberFormat format_voi = new DecimalFormat("#.##");
        format_voi.setMaximumFractionDigits(2);
        format_voi.setMinimumFractionDigits(2);
        path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Voice Notes";

        directory = new File(path);

        size_voice = getFileFolderSize(directory);

        if (size_voice > GiB) {
            len = size_voice / GiB;
            byteMake = "GB";
            data_voice = format_img.format(size_voice / GiB) + " GB";
        } else if (size_voice > MiB) {
            len = size_voice / MiB;
            byteMake = "MB";
            data_voice  = format_img.format(size_voice / MiB) + " MB";
        } else if (size_voice > KiB) {
            len = size_voice / KiB;
            byteMake = "KB";
            data_voice = format_img.format(size_voice / KiB) + " KB";
        }

        sum = size_img + size_doc + size_vid + size_voice + size_gif + size_wall + size_aud;

        if (sum > GiB) {
            len = sum / GiB;
            byteMake = "GB";
            tot_dat = format_img.format(sum / GiB) + " GB";
        } else if (sum > MiB) {
            len = sum / MiB;
            byteMake = "MB";
            tot_dat  = format_img.format(sum / MiB) + " MB";
        } else if (sum > KiB) {
            len = sum / KiB;
            byteMake = "KB";
            tot_dat = format_img.format(sum / KiB) + " KB";
        }

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
                    Snackbar.make(this.findViewById(android.R.id.content), "Please Grant Permissions to read WhatsApp images in Settings", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    }).show();
                }
            }
        }
    }
    public static long getFileFolderSize(File dir) {
        long size = 0;
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isFile()) {
                    size += file.length();
                } else
                    size += getFileFolderSize(file);
            }
        } else if (dir.isFile()) {
            size += dir.length();
        }
        return size;
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

        Intent intent = new Intent(MainActivity.this, AndroidTabLayoutActivity_aud.class);
        startActivity(intent);
    }

    @Override
    public void onGifsClicked() {

        Intent intent = new Intent(MainActivity.this, AndroidTabLayoutActivity_gifs.class);
        startActivity(intent);
    }

    @Override
    public void onWallpapersClicked() {

        Intent intent = new Intent(MainActivity.this, wallpaper.class);
        startActivity(intent);
    }

    @Override
    public void onVoiceClicked() {

        Toast.makeText(this, "Need to be Implemented", Toast.LENGTH_SHORT).show();
    }

}

