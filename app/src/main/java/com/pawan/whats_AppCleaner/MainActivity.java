package com.pawan.whats_AppCleaner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pawan.whats_AppCleaner.adapters.DetailsAdapter;
import com.pawan.whats_AppCleaner.adapters.DetailsAdapterCustom;
import com.pawan.whats_AppCleaner.datas.Details;
import com.pawan.whats_AppCleaner.tabs.TabLayoutActivity;
import com.pawan.whats_AppCleaner.tabs.TabLayoutActivity_test;

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
    private Boolean intenttoimages, intenttovideos, intenttoaudios, intenttodocuments, intenttogifs, intenttowall, intenttovoice, intenttostatus;

    private InterstitialAd mInterstitialAd_doc, mInterstitialAd_images, mInterstitialAd_audio,
            mInterstitialAd_gif, mInterstitialAd_voice, mInterstitialAd_wall, mInterstitialAd_videos, mInterstitialAd_status;
    private String sent = "Sent";

    private final static String TAG = "MainActivity";
    public final static String PREFS = "PrefsFile";
    @SuppressWarnings("FieldCanBeLocal")
    private SharedPreferences settings = null;
    @SuppressWarnings("FieldCanBeLocal")
    private SharedPreferences.Editor editor = null;

    @SuppressWarnings("FieldCanBeLocal")
    private String path;
    @SuppressWarnings("FieldCanBeLocal")
    private String data_img, data_doc, data_vid, data_aud, data_gif, data_wall, data_voice, data_status, tot_dat;
    @SuppressWarnings("FieldCanBeLocal")
    private long sum, size_img, size_doc, size_vid, size_wall, size_aud, size_gif, size_voice, size_status;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        total_data = findViewById(R.id.data);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerView recyclerView1 = findViewById(R.id.recycle);
        mAdView = findViewById(R.id.adView_small);
        mAdView1 = findViewById(R.id.adView_large);

        AppRater.app_launched(MainActivity.this);

        settings = getSharedPreferences(PREFS, MODE_PRIVATE);


        // First time running app?
        if (!settings.contains("lastRun"))
            enableNotification(null);
        else
            recordRunTime();

        Log.v(TAG, "Starting CheckRecentRun service...");
        startService(new Intent(this, CheckRecentRun.class));

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

        if (isNetworkAvailable()) {
            if (height <= 1920 && height > 1280) {

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
        mInterstitialAd_status = new InterstitialAd(this);

        mInterstitialAd_doc.setAdUnitId("ca-app-pub-7255339257613393/6990771456");
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

        mInterstitialAd_images.setAdUnitId("ca-app-pub-7255339257613393/6990771456");
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

        mInterstitialAd_videos.setAdUnitId("ca-app-pub-7255339257613393/6990771456");
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

        mInterstitialAd_audio.setAdUnitId("ca-app-pub-7255339257613393/6990771456");
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

        mInterstitialAd_voice.setAdUnitId("ca-app-pub-7255339257613393/6990771456");
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

        mInterstitialAd_wall.setAdUnitId("ca-app-pub-7255339257613393/6990771456");
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

        mInterstitialAd_gif.setAdUnitId("ca-app-pub-7255339257613393/6990771456");
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

        mInterstitialAd_status.setAdUnitId("ca-app-pub-7255339257613393/6990771456");
        mInterstitialAd_status.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7")
                .addTestDevice("C07AF1687B80C3A74C718498EF9B938A").addTestDevice("882530CA8147915F79DF99860BF2F5B0")
                .addTestDevice("D7397574F6CC21785588738256355351").build());

        mInterstitialAd_status.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                if (intenttostatus) {
                    intenttostatus = false;
                    onIntenttostatus();
                }
                if (!mInterstitialAd_status.isLoaded() && !mInterstitialAd_status.isLoading()) {
                    mInterstitialAd_status.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7").addTestDevice("C07AF1687B80C3A74C718498EF9B938A").build());
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

        String main_path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media";
        File main = new File(main_path);

        if (!main.exists()) {
            Intent intent = new Intent(MainActivity.this, Noapp.class);
            startActivity(intent);
        } else {
            askPermission();

            progressDialog = new ProgressDialog(this);
            //The line of code below will set the progress to look like the Material Design version
            progressDialog.setProgressStyle(R.style.Widget_AppCompat_ProgressBar_Horizontal/*style/Widget.ProgressBar.Horizontal*/);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
            fetchFiles();

        }

    }

    public void recordRunTime() {
        editor = settings.edit();
        editor.putLong("lastRun", System.currentTimeMillis());
        editor.apply();
    }

    public void enableNotification(View v) {
        editor = settings.edit();
        editor.putLong("lastRun", System.currentTimeMillis());
        editor.putBoolean("enabled", true);
        editor.apply();
        Log.v(TAG, "Notifications enabled");
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        MenuCompat.setGroupDividerEnabled(menu, true);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.privacy:
                Uri uri = Uri.parse("https://whats-app-cleaner.flycricket.io/privacy.html");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return (true);
            case R.id.about:
                Uri uri1 = Uri.parse("http://pawankumar.me/");
                Intent intent1 = new Intent(Intent.ACTION_VIEW, uri1);
                startActivity(intent1);
                return (true);
            case R.id.feedback:
                Uri uri2 = Uri.parse("https://forms.gle/gsxE4QaqvAM6D3xJ9");
                Intent intent2 = new Intent(Intent.ACTION_VIEW, uri2);
                startActivity(intent2);
                return (true);
            case R.id.logo:
                Uri uri3 = Uri.parse("https://www.behance.net/vizdash1998");
                Intent intent3 = new Intent(Intent.ACTION_VIEW, uri3);
                startActivity(intent3);
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
                        .setPositiveButton("OK", (dialog, which) -> {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CODE);
                            }
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            dialog.dismiss();
                            finish();
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
                            .setPositiveButton("OK", (dialog, which) -> {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent, EXTERNAL_STORAGE_PERMISSION_CODE);
                            })
                            .setNegativeButton("Cancel", (dialog, which) -> {
                                dialog.dismiss();
                                finish();
                            }).create().show();


                } else {

                    new AlertDialog.Builder(this)
                            .setTitle("Permission needed")
                            .setMessage("Storage permission is needed to read WhatsApp Media")
                            .setCancelable(false)
                            .setPositiveButton("OK", (dialog, which) -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                            Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CODE);
                                }
                            })
                            .setNegativeButton("Cancel", (dialog, which) -> {
                                dialog.dismiss();
                                finish();
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
            mainActivityWeakReference.get().path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Images";

            File img = new File(mainActivityWeakReference.get().path);
            String img_sent = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Images/" + mainActivityWeakReference.get().sent;
            File imgs = new File(img_sent);

            if (!img.exists()) {
                String imgg = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Images";
                File image = new File(imgg);
                if (!image.exists()) {
                    if (!image.mkdir()) {
                        Log.e("FIle", "Can't be created");
                    } else {
                        Log.e("FIle", "created");
                        if (!imgs.exists()) {
                            if (!imgs.mkdir()) {
                                Log.e("sent", "Can't be created");
                            } else {
                                Log.e("sent", "created");
                            }
                        } else {
                            Log.e("sent", "Alreaddy Exists");
                        }
                    }
                } else {
                    Log.e("FIle", "Alreaddy Exists");
                }
            } else {
                if (!imgs.exists()) {
                    if (!imgs.mkdir()) {
                        Log.e("sent", "Can't be created");
                    } else {
                        Log.e("sent", "created");
                    }
                } else {
                    Log.e("sent", "Alreaddy Exists");
                }
                mainActivityWeakReference.get().size_img = FileUtils.sizeOfDirectory(new File(DataHolder.imagesReceivedPath));
                mainActivityWeakReference.get().data_img = Formatter.formatShortFileSize(mainActivityWeakReference.get(), mainActivityWeakReference.get().size_img);
            }
            /*Size for Documents folder*/
            mainActivityWeakReference.get().path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Documents";
            File doc = new File(mainActivityWeakReference.get().path);

            String doc_sent = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Documents/" + mainActivityWeakReference.get().sent;
            File dc = new File(doc_sent);

            if (!doc.exists()) {
                String docc = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Documents";
                File docs = new File(docc);
                if (!docs.exists()) {
                    if (!docs.mkdir()) {
                        Log.e("FIle", "Can't be created");
                    } else {
                        Log.e("FIle", "created");
                        if (!dc.exists()) {
                            if (!dc.mkdir()) {
                                Log.e("sent", "Can't be created");
                            } else {
                                Log.e("sent", "created");
                            }
                        } else {
                            Log.e("sent", "Alreaddy Exists");
                        }
                    }
                } else {
                    Log.e("FIle", "Alreaddy Exists");
                }
            } else {
                if (!dc.exists()) {
                    if (!dc.mkdir()) {
                        Log.e("sent", "Can't be created");
                    } else {
                        Log.e("sent", "created");
                    }
                } else {
                    Log.e("sent", "Alreaddy Exists");
                }
                mainActivityWeakReference.get().size_doc = FileUtils.sizeOfDirectory(new File(DataHolder.documentsReceivedPath));
                mainActivityWeakReference.get().data_doc = Formatter.formatShortFileSize(mainActivityWeakReference.get(), mainActivityWeakReference.get().size_doc);
            }

            /*Size for Videos folder*/
            mainActivityWeakReference.get().path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Video";
            File vid = new File(mainActivityWeakReference.get().path);
            String vid_sent = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Video/" + mainActivityWeakReference.get().sent;
            File vidd = new File(vid_sent);

            if (!vid.exists()) {
                String vi = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Video";
                File video = new File(vi);
                if (!video.exists()) {
                    if (!video.mkdir()) {
                        Log.e("FIle", "Can't be created");
                    } else {
                        Log.e("FIle", "created");
                        if (!vidd.exists()) {
                            if (!vidd.mkdir()) {
                                Log.e("sent", "Can't be created");
                            } else {
                                Log.e("sent", "created");
                            }
                        } else {
                            Log.e("sent", "Alreaddy Exists");
                        }
                    }
                } else {
                    Log.e("FIle", "Alreaddy Exists");
                }
            } else {
                if (!vidd.exists()) {
                    if (!vidd.mkdir()) {
                        Log.e("sent", "Can't be created");
                    } else {
                        Log.e("sent", "created");
                    }
                } else {
                    Log.e("sent", "Alreaddy Exists");
                }
                mainActivityWeakReference.get().size_vid = FileUtils.sizeOfDirectory(new File(DataHolder.videosReceivedPath));
                mainActivityWeakReference.get().data_vid = Formatter.formatShortFileSize(mainActivityWeakReference.get(), mainActivityWeakReference.get().size_vid);
            }
            /*Size for Audios folder*/
            mainActivityWeakReference.get().path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Audio";
            File aud = new File(mainActivityWeakReference.get().path);
            String aud_sent = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Audio/" + mainActivityWeakReference.get().sent;
            File audd = new File(aud_sent);

            if (!aud.exists()) {
                String audi = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Audio";
                File audio = new File(audi);
                if (!audio.exists()) {
                    if (!audio.mkdir()) {
                        Log.e("FIle", "Can't be created");
                    } else {
                        Log.e("FIle", "created");
                        if (!audd.exists()) {
                            if (!audd.mkdir()) {
                                Log.e("sent", "Can't be created");
                            } else {
                                Log.e("sent", "created");
                            }
                        } else {
                            Log.e("sent", "Alreaddy Exists");
                        }
                    }
                } else {
                    Log.e("FIle", "Alreaddy Exists");
                }
            } else {
                if (!audd.exists()) {
                    if (!audd.mkdir()) {
                        Log.e("sent", "Can't be created");
                    } else {
                        Log.e("sent", "created");
                    }
                } else {
                    Log.e("sent", "Alreaddy Exists");
                }
                mainActivityWeakReference.get().size_aud = FileUtils.sizeOfDirectory(new File(DataHolder.audiosReceivedPath));
                mainActivityWeakReference.get().data_aud = Formatter.formatShortFileSize(mainActivityWeakReference.get(), mainActivityWeakReference.get().size_aud);
            }

            /*Size for Wallpaper folder*/
            mainActivityWeakReference.get().path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WallPaper";
            File wall = new File(mainActivityWeakReference.get().path);
            String wallpaper_sent = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WallPaper/" + mainActivityWeakReference.get().sent;
            File file = new File(wallpaper_sent);

            if (!wall.exists()) {
                String wallp = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/" + "WallPaper";
                File walls = new File(wallp);
                if (!walls.exists()) {
                    if (!walls.mkdir()) {
                        Log.e("FIle", "Can't be created");
                    } else {
                        Log.e("FIle", "created");
                        if (!file.exists()) {
                            if (!file.mkdir()) {
                                Log.e("sent", "Can't be created");
                            } else {
                                Log.e("sent", "created");
                            }
                        } else {
                            Log.e("FIle", "Alreaddy Exists");
                        }
                    }
                } else {
                    Log.e("FIle", "Alreaddy Exists");
                }
            } else {
                if (!file.exists()) {
                    if (!file.mkdir()) {
                        Log.e("sent", "Can't be created");
                    } else {
                        Log.e("sent", "created");
                    }
                } else {
                    Log.e("sent", "Alreaddy Exists");
                }
                mainActivityWeakReference.get().size_wall = FileUtils.sizeOfDirectory(new File(mainActivityWeakReference.get().path));
                mainActivityWeakReference.get().data_wall = Formatter.formatShortFileSize(mainActivityWeakReference.get(), mainActivityWeakReference.get().size_wall);
            }
            /*Size for Gifs folder*/
            mainActivityWeakReference.get().path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Animated Gifs";
            File gif = new File(mainActivityWeakReference.get().path);
            String gi_sent = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Animated Gifs/" + mainActivityWeakReference.get().sent;
            File gii = new File(gi_sent);

            if (!gif.exists()) {
                String gifs = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/" + "WhatsApp Animated Gifs";
                File gi = new File(gifs);
                if (!gi.exists()) {
                    if (!gi.mkdir()) {
                        Log.e("FIle", "Can't be created");
                    } else {
                        Log.e("FIle", "created");
                        if (!gii.exists()) {
                            if (!gii.mkdir()) {
                                Log.e("sent", "Can't be created");
                            } else {
                                Log.e("sent", "created");
                            }
                        } else {
                            Log.e("sent", "Alreaddy Exists");
                        }
                    }
                } else {
                    Log.e("FIle", "Alreaddy Exists");
                }
            } else {
                if (!gii.exists()) {
                    if (!gii.mkdir()) {
                        Log.e("sent", "Can't be created");
                    } else {
                        Log.e("sent", "created");
                    }
                } else {
                    Log.e("sent", "Alreaddy Exists");
                }
                mainActivityWeakReference.get().size_gif = FileUtils.sizeOfDirectory(new File(DataHolder.gifReceivedPath));
                mainActivityWeakReference.get().data_gif = Formatter.formatShortFileSize(mainActivityWeakReference.get(), mainActivityWeakReference.get().size_gif);
            }
            /*Size for Voice Notes folder*/
            mainActivityWeakReference.get().path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Voice Notes";
            File vo = new File(mainActivityWeakReference.get().path);

            String voice_sent = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Voice Notes/" + mainActivityWeakReference.get().sent;
            File file1 = new File(voice_sent);

            if (!vo.exists()) {
                String voi = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/WhatsApp Voice Notes";
                File voic = new File(voi);
                if (!voic.exists()) {
                    if (!voic.mkdir()) {
                        Log.e("FIle", "Can't be created");
                    } else {
                        Log.e("FIle", "created");
                        if (!file1.exists()) {
                            if (!file1.mkdir()) {
                                Log.e("sent", "Can't be created");
                            } else {
                                Log.e("sent", "created");
                            }
                        } else {
                            Log.e("sent", "Alreaddy Exists");
                        }
                    }
                } else {
                    Log.e("FIle", "Alreaddy Exists");
                }
            } else {
                if (!file1.exists()) {
                    if (!file1.mkdir()) {
                        Log.e("sent", "Can't be created");
                    } else {
                        Log.e("sent", "created");
                    }
                } else {
                    Log.e("sent", "Alreaddy Exists");
                }
                mainActivityWeakReference.get().size_voice = FileUtils.sizeOfDirectory(new File(mainActivityWeakReference.get().path));
                mainActivityWeakReference.get().data_voice = Formatter.formatShortFileSize(mainActivityWeakReference.get(), mainActivityWeakReference.get().size_voice);
            }
            /*Size for status*/
            mainActivityWeakReference.get().path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/.Status Download";
            File status = new File(mainActivityWeakReference.get().path);
            if (!status.exists()) {
                if (!status.mkdir()) {
                    Log.e("FIle", "Can't be created");
                } else {
                    Log.e("FIle", "created");
                }
            } else {
                mainActivityWeakReference.get().size_status = FileUtils.sizeOfDirectory(new File(mainActivityWeakReference.get().path));
                mainActivityWeakReference.get().data_status = Formatter.formatShortFileSize(mainActivityWeakReference.get(), mainActivityWeakReference.get().size_status);
            }

            mainActivityWeakReference.get().sum = mainActivityWeakReference.get().size_img + mainActivityWeakReference.get().size_doc +
                    mainActivityWeakReference.get().size_vid + mainActivityWeakReference.get().size_voice + mainActivityWeakReference.get().size_gif +
                    mainActivityWeakReference.get().size_wall + mainActivityWeakReference.get().size_aud + mainActivityWeakReference.get().size_status;
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
            mainActivityWeakReference.get().dataList1.add(new Details(
                    "Statuses",
                    mainActivityWeakReference.get().data_status,
                    R.drawable.ic_status,
                    R.color.orange
            ));

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
                    R.drawable.camera_iris,
                    R.color.green));
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

    public void onIntenttostatus() {
        Intent intent = new Intent(MainActivity.this, TabLayoutActivity_test.class);
        intent.putExtra("category", DataHolder.STATUS);
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

    @Override
    public void onStatusClicked() {
        if (mInterstitialAd_status.isLoaded() && mInterstitialAd_status != null) {
            mInterstitialAd_status.show();
            intenttostatus = true;

        } else {
            Log.e("TAG", "Not loaded");
            if (hasPermission()) {
                onIntenttostatus();
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

