package com.example.pawan.whatsAppcleaner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import com.crashlytics.android.Crashlytics;
import com.example.pawan.whatsAppcleaner.adapters.DetailsAdapter;
import com.example.pawan.whatsAppcleaner.adapters.DetailsAdapterCustom;
import com.example.pawan.whatsAppcleaner.datas.Details;
import com.example.pawan.whatsAppcleaner.tabs.TabLayoutActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;

import io.fabric.sdk.android.Fabric;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DetailsAdapter.OnItemClickListener, DetailsAdapterCustom.OnItemClickListener {

    private static final int EXTERNAL_STORAGE_PERMISSION_CODE = 1002;
    private ArrayList<Details> dataList1 = new ArrayList<>();
    private ArrayList<Details> dataList = new ArrayList<>();
    private TextView total_data;
    private DetailsAdapterCustom detailsAdaptercustom;
    private DetailsAdapter detailsAdapter1;
    private ProgressDialog progressDialog;
    private AdView mAdView, mAdView1;

    private Boolean intenttoimages, intenttovideos, intenttoaudios, intenttodocuments, intenttogifs, intenttowall, intenttovoice;

    private InterstitialAd mInterstitialAd_doc, mInterstitialAd_images, mInterstitialAd_audio,
            mInterstitialAd_gif, mInterstitialAd_voice, mInterstitialAd_wall, mInterstitialAd_videos;


    @SuppressWarnings("FieldCanBeLocal")
    private String path;
    @SuppressWarnings("FieldCanBeLocal")
    private String data_img, data_doc, data_vid, data_aud, data_gif, data_wall, data_voice, tot_dat;
    @SuppressWarnings("FieldCanBeLocal")
    private long sum, size_img, size_doc, size_vid, size_wall, size_aud, size_gif, size_voice;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        total_data = findViewById(R.id.data);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerView recyclerView1 = findViewById(R.id.recycle);
        mAdView = findViewById(R.id.adView_small);
        mAdView1 = findViewById(R.id.adView_large);

        // Obtain the FirebaseAnalytics instance.
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(recyclerView));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, String.valueOf(recyclerView));
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "name");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Network", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("Status", isNetworkAvailable());
        editor.apply();


        /*Will Fix this in next Update*/
        String sent_wall = "Sent";
        String sent_voice = "Sent";
        String wallpaper = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WallPaper/" + sent_wall;
        String voice = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Voice Notes/" + sent_voice;

        File file = new File(wallpaper);
        File file1 = new File(voice);

        if (!file.exists()) {
            if (!file.mkdir()) {
                  Log.e("FIle", "Can't be created");
            } else {
                 Log.e("FIle", "created");
            }
        } else {
            Log.e("FIle", "Alreaddy Exists");
        }

        if (!file1.exists()) {
            if (!file.mkdir()) {
                Log.e("FIle", "Can't be created");
            } else {
                Log.e("FIle", "created");
            }
        } else {
            Log.e("FIle", "Alreaddy Exists");
        }

        if (isNetworkAvailable()) {
            if (height <= 1920 && height >= 1280) {

                mAdView.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7").addTestDevice("C07AF1687B80C3A74C718498EF9B938A").build());

                mAdView.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        if (!mAdView.isLoading()) {
                            mAdView.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7").addTestDevice("C07AF1687B80C3A74C718498EF9B938A").build());
                        }
                    }

                    @Override
                    public void onAdLoaded() {
                        Log.e("Banner", "Loaded");
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        Log.e("Bannercode", String.valueOf(i));
                    }
                });
            } else if (height > 1920) {

                mAdView1.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7").build());
                mAdView1.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        if (!mAdView1.isLoading()) {
                            mAdView1.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7").build());
                        }
                    }

                    @Override
                    public void onAdLoaded() {
                        Log.e("Banner", "Loaded");
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        Log.e("Bannercode", String.valueOf(i));
                    }
                });
            }
        }


        mInterstitialAd_doc = new InterstitialAd(this);
        mInterstitialAd_images = new InterstitialAd(this);
        mInterstitialAd_videos = new InterstitialAd(this);
        mInterstitialAd_audio = new InterstitialAd(this);
        mInterstitialAd_voice = new InterstitialAd(this);
        mInterstitialAd_wall = new InterstitialAd(this);
        mInterstitialAd_gif = new InterstitialAd(this);

        mInterstitialAd_doc.setAdUnitId("ca-app-pub-7255339257613393/6137674653");
        mInterstitialAd_doc.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7")
                .addTestDevice("C07AF1687B80C3A74C718498EF9B938A").addTestDevice("882530CA8147915F79DF99860BF2F5B0")
                .addTestDevice("D7397574F6CC21785588738256355351").build());

        mInterstitialAd_doc.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                if (intenttodocuments) {
                    intenttodocuments = false;
                    onIntenttoDoc();
                }
                if (!mInterstitialAd_doc.isLoaded() && !mInterstitialAd_doc.isLoading()) {
                    mInterstitialAd_doc.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7").addTestDevice("C07AF1687B80C3A74C718498EF9B938A").build());
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                Log.e("Interstitial_doc", String.valueOf(i));
            }

            @Override
            public void onAdLoaded() {
                Log.e("Interstitial_doc", "Loaded");
            }

        });

        mInterstitialAd_images.setAdUnitId("ca-app-pub-7255339257613393/6137674653");
        mInterstitialAd_images.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7")
                .addTestDevice("C07AF1687B80C3A74C718498EF9B938A").addTestDevice("882530CA8147915F79DF99860BF2F5B0")
                .addTestDevice("D7397574F6CC21785588738256355351").build());

        mInterstitialAd_images.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {

                if (intenttoimages) {
                    intenttoimages = false;
                    onIntenttoImages();
                }
                if (!mInterstitialAd_images.isLoaded() && !mInterstitialAd_images.isLoading()) {
                    mInterstitialAd_images.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7").addTestDevice("C07AF1687B80C3A74C718498EF9B938A").build());
                }

            }

            @Override
            public void onAdFailedToLoad(int i) {
                Log.e("Interstitial_images", String.valueOf(i));
            }

            @Override
            public void onAdLoaded() {
                Log.e("Interstitial_images", "Loaded");
            }
        });

        mInterstitialAd_videos.setAdUnitId("ca-app-pub-7255339257613393/6137674653");
        mInterstitialAd_videos.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7")
                .addTestDevice("C07AF1687B80C3A74C718498EF9B938A").addTestDevice("882530CA8147915F79DF99860BF2F5B0")
                .addTestDevice("D7397574F6CC21785588738256355351").build());

        mInterstitialAd_videos.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                if (intenttovideos) {
                    intenttovideos = false;
                    onIntenttoVideos();
                }
                if (!mInterstitialAd_videos.isLoaded() && !mInterstitialAd_videos.isLoading()) {
                    mInterstitialAd_videos.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7").addTestDevice("C07AF1687B80C3A74C718498EF9B938A").build());
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                Log.e("Interstitial_videos", String.valueOf(i));
            }

            @Override
            public void onAdLoaded() {
                Log.e("Interstitial_videos", "Loaded");
            }

        });

        mInterstitialAd_audio.setAdUnitId("ca-app-pub-7255339257613393/6137674653");
        mInterstitialAd_audio.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7")
                .addTestDevice("C07AF1687B80C3A74C718498EF9B938A").addTestDevice("882530CA8147915F79DF99860BF2F5B0")
                .addTestDevice("D7397574F6CC21785588738256355351").build());

        mInterstitialAd_audio.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                if (intenttoaudios) {
                    intenttoaudios = false;
                    onIntenttoAudio();
                }
                if (!mInterstitialAd_audio.isLoaded() && !mInterstitialAd_audio.isLoading()) {
                    mInterstitialAd_audio.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7").addTestDevice("C07AF1687B80C3A74C718498EF9B938A").build());
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                Log.e("Interstitial_audio", String.valueOf(i));
            }

            @Override
            public void onAdLoaded() {
                Log.e("Interstitial_audio", "Loaded");
            }

        });

        mInterstitialAd_voice.setAdUnitId("ca-app-pub-7255339257613393/6137674653");
        mInterstitialAd_voice.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7")
                .addTestDevice("C07AF1687B80C3A74C718498EF9B938A").addTestDevice("882530CA8147915F79DF99860BF2F5B0")
                .addTestDevice("D7397574F6CC21785588738256355351").build());

        mInterstitialAd_voice.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                if (intenttovoice) {
                    intenttovoice = false;
                    onIntenttoVoice();
                }
                if (!mInterstitialAd_voice.isLoaded() && !mInterstitialAd_voice.isLoading()) {
                    mInterstitialAd_doc.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7").addTestDevice("C07AF1687B80C3A74C718498EF9B938A").build());
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                Log.e("Interstitial_voice", String.valueOf(i));
            }

            @Override
            public void onAdLoaded() {
                Log.e("Interstitial_voice", "Loaded");
            }

        });

        mInterstitialAd_wall.setAdUnitId("ca-app-pub-7255339257613393/6137674653");
        mInterstitialAd_wall.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7")
                .addTestDevice("C07AF1687B80C3A74C718498EF9B938A").addTestDevice("882530CA8147915F79DF99860BF2F5B0")
                .addTestDevice("D7397574F6CC21785588738256355351").build());

        mInterstitialAd_wall.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                if (intenttowall) {
                    intenttowall = false;
                    onIntenttoWall();
                }
                if (!mInterstitialAd_wall.isLoaded() && !mInterstitialAd_wall.isLoading()) {
                    mInterstitialAd_wall.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7").addTestDevice("C07AF1687B80C3A74C718498EF9B938A").build());
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                Log.e("Interstitial_wall", String.valueOf(i));
            }

            @Override
            public void onAdLoaded() {
                Log.e("Interstitial_wall", "Loaded");
            }

        });

        mInterstitialAd_gif.setAdUnitId("ca-app-pub-7255339257613393/6137674653");
        mInterstitialAd_gif.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7")
                .addTestDevice("C07AF1687B80C3A74C718498EF9B938A").addTestDevice("882530CA8147915F79DF99860BF2F5B0")
                .addTestDevice("D7397574F6CC21785588738256355351").build());

        mInterstitialAd_gif.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                if (intenttogifs) {
                    intenttogifs = false;
                    onIntenttoGif();
                }
                if (!mInterstitialAd_gif.isLoaded() && !mInterstitialAd_gif.isLoading()) {
                    mInterstitialAd_gif.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7").addTestDevice("C07AF1687B80C3A74C718498EF9B938A").build());
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                Log.e("Interstitial_gif", String.valueOf(i));
            }

            @Override
            public void onAdLoaded() {
                Log.e("Interstitial_gif", "Loaded");
            }

        });

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

    @SuppressLint("WrongConstant")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.privacy:
                Uri uri = Uri.parse("https://whatsapp-cleaner.flycricket.io/privacy.html");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
                return (true);
            case R.id.about:
                Uri uri1 = Uri.parse("http://pawankumar.me/");
                Intent intent1 = new Intent(Intent.ACTION_VIEW,uri1);
                startActivity(intent1);
            return (true);
            case R.id.feedback:
                Uri uri2 = Uri.parse("https://forms.gle/gsxE4QaqvAM6D3xJ9");
                Intent intent2 = new Intent(Intent.ACTION_VIEW,uri2);
                startActivity(intent2);
                return (true);
        }
        return (super.onOptionsItemSelected(item));
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

        FetchFiles(MainActivity mainActivity) {
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

    public void onIntenttoImages() {
        Intent intent = new Intent(MainActivity.this, TabLayoutActivity.class);
        intent.putExtra("category", DataHolder.IMAGE);
        startActivity(intent);
    }

    public void onIntenttoVideos() {
        Intent intent = new Intent(MainActivity.this, TabLayoutActivity.class);
        intent.putExtra("category", DataHolder.VIDEO);
        startActivity(intent);
    }

    public void onIntenttoDoc() {
        Intent intent = new Intent(MainActivity.this, TabLayoutActivity.class);
        intent.putExtra("category", DataHolder.DOCUMENT);
        startActivity(intent);
    }

    public void onIntenttoAudio() {
        Intent intent = new Intent(MainActivity.this, TabLayoutActivity.class);
        intent.putExtra("category", DataHolder.AUDIO);
        startActivity(intent);
    }

    public void onIntenttoGif() {
        Intent intent = new Intent(MainActivity.this, TabLayoutActivity.class);
        intent.putExtra("category", DataHolder.GIF);
        startActivity(intent);
    }

    public void onIntenttoWall() {
        Intent intent = new Intent(MainActivity.this, TabLayoutActivity.class);
        intent.putExtra("category", DataHolder.WALLPAPER);
        startActivity(intent);
    }

    public void onIntenttoVoice() {
        Intent intent = new Intent(MainActivity.this, TabLayoutActivity.class);
        intent.putExtra("category", DataHolder.VOICE);
        startActivity(intent);
    }


    @Override
    public void onImagesClicked() {
        if (mInterstitialAd_images.isLoaded() && mInterstitialAd_images != null) {
            mInterstitialAd_images.show();
            intenttoimages = true;

        } else {
            Log.e("TAG", "Not loaded");
            if (hasPermission()) {
                onIntenttoImages();
            } else {
                askPermission();
            }

        }
    }

    @Override
    public void onDocumentsClicked() {
        if (mInterstitialAd_doc.isLoaded() && mInterstitialAd_doc != null) {
            mInterstitialAd_doc.show();
            intenttodocuments = true;

        } else {
            Log.e("Tag", "Not Loaded");
            if (hasPermission()) {
                onIntenttoDoc();
            } else {
                askPermission();
            }

        }
    }

    @Override
    public void onVideosClicked() {
        if (mInterstitialAd_videos.isLoaded() && mInterstitialAd_videos != null) {
            mInterstitialAd_videos.show();
            intenttovideos = true;

        } else {
            Log.e("TAG", "Not Loaded");
            if (hasPermission()) {
                onIntenttoVideos();
            } else {
                askPermission();
            }
        }
    }

    @Override
    public void onAudiosClicked() {
        if (mInterstitialAd_audio.isLoaded() && mInterstitialAd_audio != null) {
            mInterstitialAd_audio.show();
            intenttoaudios = true;

        } else {
            Log.e("TAG", "Not loaded");
            if (hasPermission()) {
                onIntenttoAudio();
            } else {
                askPermission();
            }
        }
    }

    @Override
    public void onGifsClicked() {
        if (mInterstitialAd_gif.isLoaded() && mInterstitialAd_gif != null) {
            mInterstitialAd_gif.show();
            intenttogifs = true;

        } else {
            Log.e("Tag", "NotLoaded");
            if (hasPermission()) {
                onIntenttoGif();
            } else {
                askPermission();
            }
        }
    }

    @Override
    public void onWallpapersClicked() {
        if (mInterstitialAd_wall.isLoaded() && mInterstitialAd_wall != null) {
            mInterstitialAd_wall.show();
            intenttowall = true;

        } else {
            Log.e("TAG", "Not loaded");
            if (hasPermission()) {
                onIntenttoWall();
            } else {
                askPermission();
            }
        }
    }

    @Override
    public void onVoiceClicked() {
        if (mInterstitialAd_voice.isLoaded() && mInterstitialAd_voice != null) {
            mInterstitialAd_voice.show();
            intenttovoice = true;

        } else {
            Log.e("TAG", "Not loaded");
            if (hasPermission()) {
                onIntenttoVoice();
            } else {
                askPermission();
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

