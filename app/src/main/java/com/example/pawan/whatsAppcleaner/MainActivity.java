package com.example.pawan.whatsAppcleaner;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.Formatter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.pawan.whatsAppcleaner.adapters.DetailsAdapter;
import com.example.pawan.whatsAppcleaner.adapters.DetailsAdapterCustom;
import com.example.pawan.whatsAppcleaner.datas.Details;
import com.example.pawan.whatsAppcleaner.tabs.TabLayoutActivity;
import com.example.pawan.whatsAppcleaner.tabs.Wallpaper.wallpaper;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;

// TODO: 1/13/19 We implement the interface here
public class MainActivity extends AppCompatActivity implements DetailsAdapter.OnItemClickListener, DetailsAdapterCustom.OnItemClickListener {

    private static final int EXTERNAL_STORAGE_PERMISSION_CODE = 1002;
    ArrayList<Details> dataList1 = new ArrayList<>();
    ArrayList<Details> dataList = new ArrayList<>();
    TextView total_data, files;
    RecyclerView recyclerView, recyclerView1;
    DetailsAdapterCustom detailsAdaptercustom;
    DetailsAdapter detailsAdapter1;

    @SuppressWarnings("FieldCanBeLocal")
    private String path;
    @SuppressWarnings("FieldCanBeLocal")
    private String data_img, data_doc, data_vid, data_aud, data_gif, data_wall, data_voice, tot_dat;
    @SuppressWarnings("FieldCanBeLocal")
    private long sum, size_img, size_doc, size_vid, size_wall, size_aud, size_gif, size_voice;
    RelativeLayout loading;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loading = findViewById(R.id.loading);
        total_data = findViewById(R.id.data);
        files = findViewById(R.id.files);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView1 = findViewById(R.id.recycle);


        files.setText(Html.fromHtml("<sub><small>Files</small></sub>"));
        detailsAdapter1 = new DetailsAdapter(this, dataList1, this);
        detailsAdaptercustom = new DetailsAdapterCustom(this, dataList, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(detailsAdapter1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        recyclerView1.setHasFixedSize(true);
        recyclerView1.setAdapter(detailsAdaptercustom);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        recyclerView1.setLayoutManager(mGridLayoutManager);

        askPermission();

        fetchFiles();
    }

    private void askPermission() {
        if (!hasPermission()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(this)
                        .setTitle("Permission needed")
                        .setMessage("Storage permission is needed to read WhatsApp Media")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                            Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CODE);
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        }).create().show();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CODE);
                }
            }

        }
    }

    private void fetchFiles() {
        /*Size for Images folder*/
        path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Images";

        size_img = FileUtils.sizeOfDirectory(new File(path));
        data_img = Formatter.formatShortFileSize(MainActivity.this, size_img);

        /*Size for Documents folder*/

        path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Documents";

        size_doc = FileUtils.sizeOfDirectory(new File(path));
        data_doc = Formatter.formatShortFileSize(MainActivity.this, size_doc);


        /*Size for Videos folder*/
        path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Video";

        size_vid = FileUtils.sizeOfDirectory(new File(path));
        data_vid = Formatter.formatShortFileSize(MainActivity.this, size_vid);

        /*Size for Audios folder*/
        path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Audio";


        size_aud = FileUtils.sizeOfDirectory(new File(path));
        data_aud = Formatter.formatShortFileSize(MainActivity.this, size_aud);


        /*Size for Wallpaper folder*/
        path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WallPaper";


        size_wall = FileUtils.sizeOfDirectory(new File(path));
        data_wall = Formatter.formatShortFileSize(MainActivity.this, size_wall);

        /*Size for Gifs folder*/
        path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Animated Gifs";


        size_gif = FileUtils.sizeOfDirectory(new File(path));
        data_gif = Formatter.formatShortFileSize(MainActivity.this, size_gif);

        /*Size for Voice Notes folder*/
        path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Voice Notes";


        size_voice = FileUtils.sizeOfDirectory(new File(path));
        data_voice = Formatter.formatShortFileSize(MainActivity.this, size_voice);


        sum = size_img + size_doc + size_vid + size_voice + size_gif + size_wall + size_aud;
        tot_dat = Formatter.formatShortFileSize(MainActivity.this, sum);


        total_data.setText(tot_dat);
        //For Images,documents and Videos

        dataList1.clear();
        dataList1.add(new Details(
                "Images",
                data_img,
                R.drawable.ic_image,
                R.color.green));
        dataList1.add(new Details(
                "Documents",
                data_doc,
                R.drawable.ic_folder,
                R.color.orange));
        dataList1.add(new Details(
                "Videos",
                data_vid,
                R.drawable.ic_video,
                R.color.blue));

        dataList.clear();
        dataList.add(new Details(
                "Audio files",
                data_aud,
                R.drawable.ic_library_music_black,
                R.color.purple));
        dataList.add(new Details(
                "Voice files",
                data_voice,
                R.drawable.ic_queue_music_black,
                R.color.lightblue));
        dataList.add(new Details(
                "Wallpapers",
                data_wall,
                R.drawable.ic_image,
                R.color.maroon));
        dataList.add(new Details(
                "GIFs",
                data_gif,
                R.drawable.ic_image,
                R.color.lightpink));

        detailsAdapter1.notifyDataSetChanged();
        detailsAdaptercustom.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                fetchFiles();
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    new AlertDialog.Builder(this)
                            .setTitle("Permission needed")
                            .setMessage("Storage permission is needed to read WhatsApp Media. Press OK to enable in settings.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivityForResult(intent, EXTERNAL_STORAGE_PERMISSION_CODE);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }).create().show();


                } else {

                    new AlertDialog.Builder(this)
                            .setTitle("Permission needed")
                            .setMessage("Storage permission is needed to read WhatsApp Media")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CODE);
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }).create().show();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasPermission()) {
            fetchFiles();
        }
    }

    private boolean hasPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onImagesClicked() {
        if (hasPermission()) {
            Intent intent = new Intent(MainActivity.this, TabLayoutActivity.class);
            intent.putExtra("category", DataHolder.IMAGE);
            startActivity(intent);
        } else {
            askPermission();
        }
    }

    @Override
    public void onDocumentsClicked() {
        if (hasPermission()) {
            Intent intent = new Intent(MainActivity.this, TabLayoutActivity.class);
            intent.putExtra("category", DataHolder.DOCUMENT);
            startActivity(intent);
        } else {
            askPermission();
        }
    }

    @Override
    public void onVideosClicked() {
        if (hasPermission()) {
            Intent intent = new Intent(MainActivity.this, TabLayoutActivity.class);
            intent.putExtra("category", DataHolder.VIDEO);
            startActivity(intent);
        } else {
            askPermission();
        }
    }

    @Override
    public void onAudiosClicked() {
        if (hasPermission()) {
            Intent intent = new Intent(MainActivity.this, TabLayoutActivity.class);
            intent.putExtra("category", DataHolder.AUDIO);
            startActivity(intent);
        } else {
            askPermission();
        }
    }

    @Override
    public void onGifsClicked() {
        if (hasPermission()) {
            Intent intent = new Intent(MainActivity.this, TabLayoutActivity.class);
            intent.putExtra("category", DataHolder.GIF);
            startActivity(intent);
        } else {
            askPermission();
        }
    }

    @Override
    public void onWallpapersClicked() {
        if (hasPermission()) {
            Intent intent = new Intent(MainActivity.this, wallpaper.class);
            startActivity(intent);
        } else {
            askPermission();
        }
    }

    @Override
    public void onVoiceClicked() {
        if (hasPermission()) {
            Toast.makeText(this, "Need to be Implemented", Toast.LENGTH_SHORT).show();
        } else {
            askPermission();
        }
    }
}

