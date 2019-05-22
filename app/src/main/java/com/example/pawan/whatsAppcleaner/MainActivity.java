package com.example.pawan.whatsAppcleaner;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.pawan.whatsAppcleaner.adapters.DetailsAdapter;
import com.example.pawan.whatsAppcleaner.adapters.DetailsAdapterCustom;
import com.example.pawan.whatsAppcleaner.datas.Details;
import com.example.pawan.whatsAppcleaner.tabs.TabLayoutActivity;
import com.example.pawan.whatsAppcleaner.tabs.Voice.voice;
import com.example.pawan.whatsAppcleaner.tabs.Wallpaper.wallpaper;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.dynamic.IFragmentWrapper;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

// TODO: 1/13/19 We implement the interface here
public class MainActivity extends AppCompatActivity implements DetailsAdapter.OnItemClickListener, DetailsAdapterCustom.OnItemClickListener {

    private static final int EXTERNAL_STORAGE_PERMISSION_CODE = 1002;
    private ArrayList<Details> dataList1 = new ArrayList<>();
    private ArrayList<Details> dataList = new ArrayList<>();
    private TextView total_data, files;
    private ImageView logo;
    private RecyclerView recyclerView, recyclerView1;
    private DetailsAdapterCustom detailsAdaptercustom;
    private DetailsAdapter detailsAdapter1;
    private ProgressDialog progressDialog;

    Boolean intenttoimages, intenttovideos, intenttoaudios, intenttodocuments, intenttogifs, intenttowall, intenttovoice;

    private InterstitialAd mInterstitialAd;
    private static final String AD_ID = "ca-app-pub-7255339257613393~8837303265";

    @SuppressWarnings("FieldCanBeLocal")
    private String path;
    @SuppressWarnings("FieldCanBeLocal")
    private String data_img, data_doc, data_vid, data_aud, data_gif, data_wall, data_voice, tot_dat;
    @SuppressWarnings("FieldCanBeLocal")
    private long sum, size_img, size_doc, size_vid, size_wall, size_aud, size_gif, size_voice;
    private RelativeLayout loading;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loading = findViewById(R.id.loading);
        total_data = findViewById(R.id.data);
        files = findViewById(R.id.files);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView1 = findViewById(R.id.recycle);
        logo = findViewById(R.id.logo);

        /** Initializing*/


        MobileAds.initialize(this,
                AD_ID);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7255339257613393/6137674653");
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("7341F6CF29732E7EF535478585141848").build());

        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                if (mInterstitialAd.isLoaded() && mInterstitialAd.isLoading()){
                    mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("7341F6CF29732E7EF535478585141848").build());

                }
                if (intenttoimages){
                    intenttoimages = false;
                    onImagesClicked();
                }else if (intenttodocuments){
                    intenttodocuments = false;
                    onDocumentsClicked();
                }else if (intenttoaudios){
                    intenttoaudios = false;
                    onAudiosClicked();
                }else if (intenttovideos){
                    intenttovoice = false;
                    onVoiceClicked();
                }else if (intenttowall){
                    intenttowall = false;
                    onWallpapersClicked();
                }else if (intenttogifs){
                    intenttogifs = false;
                    onGifsClicked();
                }else if (intenttovoice){
                    intenttovoice = false;
                    onVoiceClicked();
                }
            }
        });

        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int i) {
                    Log.e("error code", String.valueOf(i));
            }
        });


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

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

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
        new FetchFiles(this).execute();
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

    private static class FetchFiles extends AsyncTask<Void, Void, String> {

        private WeakReference<MainActivity> mainActivityWeakReference;

        public FetchFiles(MainActivity mainActivity) {
            this.mainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        protected String doInBackground(Void... voids) {
            /*Size for Images folder*/

            
            mainActivityWeakReference.get().size_img = FileUtils.sizeOfDirectory(new File(DataHolder.imagesReceivedPath));
            mainActivityWeakReference.get().data_img = Formatter.formatShortFileSize(mainActivityWeakReference.get(), mainActivityWeakReference.get().size_img);

            /*Size for Documents folder*/

            mainActivityWeakReference.get().size_doc = FileUtils.sizeOfDirectory(new File(DataHolder.documentsReceivedPath));
            mainActivityWeakReference.get().data_doc = Formatter.formatShortFileSize(mainActivityWeakReference.get(), mainActivityWeakReference.get().size_doc);


            /*Size for Videos folder*/

            mainActivityWeakReference.get().size_vid = FileUtils.sizeOfDirectory(new File(DataHolder.videosReceivedPath));
            mainActivityWeakReference.get().data_vid = Formatter.formatShortFileSize(mainActivityWeakReference.get(), mainActivityWeakReference.get().size_vid);

            /*Size for Audios folder*/

            mainActivityWeakReference.get().size_aud = FileUtils.sizeOfDirectory(new File(DataHolder.audiosReceivedPath));
            mainActivityWeakReference.get().data_aud = Formatter.formatShortFileSize(mainActivityWeakReference.get(), mainActivityWeakReference.get().size_aud);


            /*Size for Wallpaper folder*/
            mainActivityWeakReference.get().path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WallPaper";


            mainActivityWeakReference.get().size_wall = FileUtils.sizeOfDirectory(new File(mainActivityWeakReference.get().path));
            mainActivityWeakReference.get().data_wall = Formatter.formatShortFileSize(mainActivityWeakReference.get(), mainActivityWeakReference.get().size_wall);

            /*Size for Gifs folder*/

            mainActivityWeakReference.get().size_gif = FileUtils.sizeOfDirectory(new File(DataHolder.gifReceivedPath));
            mainActivityWeakReference.get().data_gif = Formatter.formatShortFileSize(mainActivityWeakReference.get(), mainActivityWeakReference.get().size_gif);

            /*Size for Voice Notes folder*/
            mainActivityWeakReference.get().path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Voice Notes";


            mainActivityWeakReference.get().size_voice = FileUtils.sizeOfDirectory(new File(mainActivityWeakReference.get().path));
            mainActivityWeakReference.get().data_voice = Formatter.formatShortFileSize(mainActivityWeakReference.get(), mainActivityWeakReference.get().size_voice);


            mainActivityWeakReference.get().sum = mainActivityWeakReference.get().size_img + mainActivityWeakReference.get().size_doc +
                    mainActivityWeakReference.get().size_vid + mainActivityWeakReference.get().size_voice + mainActivityWeakReference.get().size_gif +
                    mainActivityWeakReference.get().size_wall + mainActivityWeakReference.get().size_aud;
            mainActivityWeakReference.get().tot_dat = Formatter.formatShortFileSize(mainActivityWeakReference.get(), mainActivityWeakReference.get().sum);


            //For Images,documents and Videos
            mainActivityWeakReference.get().dataList1.clear();
            mainActivityWeakReference.get().dataList1.add(new Details(
                    "Images",
                    mainActivityWeakReference.get().data_img,
                    R.drawable.ic_image,
                    R.color.green));
            mainActivityWeakReference.get().dataList1.add(new Details(
                    "Documents",
                    mainActivityWeakReference.get().data_doc,
                    R.drawable.ic_folder,
                    R.color.orange));
            mainActivityWeakReference.get().dataList1.add(new Details(
                    "Videos",
                    mainActivityWeakReference.get().data_vid,
                    R.drawable.ic_video,
                    R.color.blue));

            mainActivityWeakReference.get().dataList.clear();
            mainActivityWeakReference.get().dataList.add(new Details(
                    "Audio files",
                    mainActivityWeakReference.get().data_aud,
                    R.drawable.ic_library_music_black,
                    R.color.purple));
            mainActivityWeakReference.get().dataList.add(new Details(
                    "Voice files",
                    mainActivityWeakReference.get().data_voice,
                    R.drawable.ic_queue_music_black,
                    R.color.lightblue));
            mainActivityWeakReference.get().dataList.add(new Details(
                    "Wallpapers",
                    mainActivityWeakReference.get().data_wall,
                    R.drawable.ic_image,
                    R.color.maroon));
            mainActivityWeakReference.get().dataList.add(new Details(
                    "GIFs",
                    mainActivityWeakReference.get().data_gif,
                    R.drawable.ic_image,
                    R.color.lightpink));

            return mainActivityWeakReference.get().tot_dat;
        }

        @Override
        protected void onPostExecute(String s) {
            mainActivityWeakReference.get().total_data.setText(s);
            mainActivityWeakReference.get().detailsAdapter1.notifyDataSetChanged();
            mainActivityWeakReference.get().detailsAdaptercustom.notifyDataSetChanged();
            mainActivityWeakReference.get().progressDialog.dismiss();
        }
    }

    @Override
    public void onImagesClicked() {
        if (mInterstitialAd.isLoaded()){
            intenttoimages = true;
            mInterstitialAd.show();
        }else{
            Log.e("TAG","Not loaded");
            if (hasPermission()) {
                Intent intent = new Intent(MainActivity.this, TabLayoutActivity.class);
                intent.putExtra("category", DataHolder.IMAGE);
                startActivity(intent);
            } else {
                askPermission();
            }

        }
    }

    @Override
    public void onDocumentsClicked() {
        if (mInterstitialAd.isLoaded()){
            intenttodocuments = true;
            mInterstitialAd.show();
        }else{
            Log.e("Tag", "Not Loaded");
            if (hasPermission()) {
                Intent intent = new Intent(MainActivity.this, TabLayoutActivity.class);
                intent.putExtra("category", DataHolder.DOCUMENT);
                startActivity(intent);
            } else {
                askPermission();
            }

        }
    }
/**Use AdRequest.Builder.addTestDevice("7341F6CF29732E7EF535478585141848")*/
    @Override
    public void onVideosClicked() {
        if (mInterstitialAd.isLoaded()){
            intenttovideos = true;
            mInterstitialAd.show();
        }else
            Log.e("TAG","Not Loaded");
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
        if (mInterstitialAd.isLoaded()){
            intenttoaudios = true;
            mInterstitialAd.show();
        }else
            Log.e("TAG", "Not loaded");
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
        if (mInterstitialAd.isLoaded()){
            intenttogifs = true;
            mInterstitialAd.show();
        }else
            Log.e("Tag", "NotLoaded");
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
        if (mInterstitialAd.isLoaded()){
            intenttowall = true;
            mInterstitialAd.show();
        }else
            Log.e("TAG","Not loaded");
        if (hasPermission()) {
            Intent intent = new Intent(MainActivity.this, wallpaper.class);
            startActivity(intent);
//            Toast.makeText(this, "Need to be implemented", Toast.LENGTH_SHORT).show();
        } else {
            askPermission();
        }
    }

    @Override
    public void onVoiceClicked() {
        if (mInterstitialAd.isLoaded()){
            intenttovoice = true;
            mInterstitialAd.show();
        }else
            Log.e("TAG", "Not loaded");
        if (hasPermission()) {
          Intent intent = new Intent(MainActivity.this, voice.class);
          startActivity(intent);
//            Toast.makeText(this, "Need to be implemented", Toast.LENGTH_SHORT).show();
        } else {
            askPermission();
        }
    }
}

