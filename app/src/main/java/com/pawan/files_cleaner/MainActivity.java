package com.pawan.files_cleaner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.format.Formatter;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pawan.files_cleaner.adapters.DetailsAdapter;
import com.pawan.files_cleaner.adapters.DetailsAdapterCustom;
import com.pawan.files_cleaner.datas.Details;
import com.pawan.files_cleaner.tabs.TabLayoutActivity;
import com.pawan.files_cleaner.tabs.TabLayoutActivity_test;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DetailsAdapter.OnItemClickListener, DetailsAdapterCustom.OnItemClickListener {

    public final static String PREFS = "PrefsFile";
    private final static String TAG = "MainActivity";
    private static final int EXTERNAL_STORAGE_PERMISSION_CODE = 1002;
    private final String sent = "Sent";
    public ProgressBar progressBar;
    private SharedPreferences settings = null;
    private SharedPreferences.Editor editor = null;
    private TextView total_data;
    private ArrayList<Details> dataList = new ArrayList<>();
    private ArrayList<File> defaultList = new ArrayList<>();
    private ArrayList<Details> dataList1 = new ArrayList<>();
    private DetailsAdapter detailsAdapter1;
    private DetailsAdapterCustom detailsAdaptercustom;
    private Boolean intentToImages, intentToVideos, intentToAudios, intentToDocuments, intentToGifs,
            intentToWall, intentToVoice, intentToStatus;

    private InterstitialAd mInterstitialAd_doc, mInterstitialAd_images, mInterstitialAd_audio,
            mInterstitialAd_gif, mInterstitialAd_voice, mInterstitialAd_wall, mInterstitialAd_videos,
            mInterstitialAd_status, mInterstitialAd_nondefault;

    private long sum = 0, size_img, size_doc, size_vid, size_wall, size_aud, size_gif, size_voice, size_status;
    private String path, data_img, data_doc, data_vid, data_aud, data_gif, data_wall, data_voice, data_status, tot_dat;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create list of filepath of default folders
        defaultList.add(new File(DataHolder.imagesReceivedPath));
        defaultList.add(new File(DataHolder.documentsReceivedPath));
        defaultList.add(new File(DataHolder.videosReceivedPath));
        defaultList.add(new File(DataHolder.statuscache));
        defaultList.add(new File(DataHolder.statusdownload));
        defaultList.add(new File(DataHolder.audiosReceivedPath));
        defaultList.add(new File(DataHolder.voiceReceivedPath));
        defaultList.add(new File(DataHolder.wallReceivedPath));
        defaultList.add(new File(DataHolder.gifReceivedPath));

        total_data = findViewById(R.id.data);
        progressBar = findViewById(R.id.progressBar);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        RecyclerView recyclerView1 = findViewById(R.id.recycle);
        AppRater.app_launched(MainActivity.this);

        settings = getSharedPreferences(PREFS, MODE_PRIVATE);

        // First time running app?
        if (!settings.contains("lastRun"))
            enableNotification(null);
        else
            recordRunTime();

        checkDarkMode();
        initNavigationDrawer();

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

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Network", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("Status", isNetworkAvailable());
        editor.apply();

        mInterstitialAd_doc = new InterstitialAd(this);
        mInterstitialAd_images = new InterstitialAd(this);
        mInterstitialAd_videos = new InterstitialAd(this);
        mInterstitialAd_audio = new InterstitialAd(this);
        mInterstitialAd_voice = new InterstitialAd(this);
        mInterstitialAd_wall = new InterstitialAd(this);
        mInterstitialAd_gif = new InterstitialAd(this);
        mInterstitialAd_status = new InterstitialAd(this);
        mInterstitialAd_nondefault = new InterstitialAd(this);

        mInterstitialAd_doc.setAdUnitId("ca-app-pub-7255339257613393/8699351909");
        mInterstitialAd_doc.loadAd(new AdRequest.Builder()
//                .addTestDevice("623B1B7759D51209294A77125459D9B7")
//                .addTestDevice("C07AF1687B80C3A74C718498EF9B938A").addTestDevice("882530CA8147915F79DF99860BF2F5B0")
//                .addTestDevice("D7397574F6CC21785588738256355351")
                .build());

        mInterstitialAd_doc.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                if (intentToDocuments) {
                    intentToDocuments = false;
                    onIntentToDoc();
                }
                if (!mInterstitialAd_doc.isLoaded() && !mInterstitialAd_doc.isLoading()) {
                    mInterstitialAd_doc.loadAd(new AdRequest.Builder()
//                            .addTestDevice("623B1B7759D51209294A77125459D9B7").addTestDevice("C07AF1687B80C3A74C718498EF9B938A")
                            .build());
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

        mInterstitialAd_images.setAdUnitId("ca-app-pub-7255339257613393/8699351909");
        mInterstitialAd_images.loadAd(new AdRequest.Builder()
//                .addTestDevice("623B1B7759D51209294A77125459D9B7")
//                .addTestDevice("C07AF1687B80C3A74C718498EF9B938A").addTestDevice("882530CA8147915F79DF99860BF2F5B0")
//                .addTestDevice("D7397574F6CC21785588738256355351")
                .build());

        mInterstitialAd_images.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {

                if (intentToImages) {
                    intentToImages = false;
                    onIntentToImages();
                }
                if (!mInterstitialAd_images.isLoaded() && !mInterstitialAd_images.isLoading()) {
                    mInterstitialAd_images.loadAd(new AdRequest.Builder()
//                            .addTestDevice("623B1B7759D51209294A77125459D9B7").addTestDevice("C07AF1687B80C3A74C718498EF9B938A")
                            .build());
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

        mInterstitialAd_videos.setAdUnitId("ca-app-pub-7255339257613393/8699351909");
        mInterstitialAd_videos.loadAd(new AdRequest.Builder()
//                .addTestDevice("623B1B7759D51209294A77125459D9B7")
//                .addTestDevice("C07AF1687B80C3A74C718498EF9B938A").addTestDevice("882530CA8147915F79DF99860BF2F5B0")
//                .addTestDevice("D7397574F6CC21785588738256355351")
                .build());

        mInterstitialAd_videos.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                if (intentToVideos) {
                    intentToVideos = false;
                    onIntentToVideos();
                }
                if (!mInterstitialAd_videos.isLoaded() && !mInterstitialAd_videos.isLoading()) {
                    mInterstitialAd_videos.loadAd(new AdRequest.Builder()
//                            .addTestDevice("623B1B7759D51209294A77125459D9B7").addTestDevice("C07AF1687B80C3A74C718498EF9B938A")
                            .build());
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

        mInterstitialAd_audio.setAdUnitId("ca-app-pub-7255339257613393/8699351909");
        mInterstitialAd_audio.loadAd(new AdRequest.Builder()
//                .addTestDevice("623B1B7759D51209294A77125459D9B7")
//                .addTestDevice("C07AF1687B80C3A74C718498EF9B938A").addTestDevice("882530CA8147915F79DF99860BF2F5B0")
//                .addTestDevice("D7397574F6CC21785588738256355351")
                .build());

        mInterstitialAd_audio.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                if (intentToAudios) {
                    intentToAudios = false;
                    onIntentToAudio();
                }
                if (!mInterstitialAd_audio.isLoaded() && !mInterstitialAd_audio.isLoading()) {
                    mInterstitialAd_audio.loadAd(new AdRequest.Builder()
//                            .addTestDevice("623B1B7759D51209294A77125459D9B7").addTestDevice("C07AF1687B80C3A74C718498EF9B938A")
                            .build());
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

        mInterstitialAd_voice.setAdUnitId("ca-app-pub-7255339257613393/8699351909");
        mInterstitialAd_voice.loadAd(new AdRequest.Builder()
//                .addTestDevice("623B1B7759D51209294A77125459D9B7")
//                .addTestDevice("C07AF1687B80C3A74C718498EF9B938A").addTestDevice("882530CA8147915F79DF99860BF2F5B0")
//                .addTestDevice("D7397574F6CC21785588738256355351")
                .build());

        mInterstitialAd_voice.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                if (intentToVoice) {
                    intentToVoice = false;
                    onIntentToVoice();
                }
                if (!mInterstitialAd_voice.isLoaded() && !mInterstitialAd_voice.isLoading()) {
                    mInterstitialAd_doc.loadAd(new AdRequest.Builder()
//                            .addTestDevice("623B1B7759D51209294A77125459D9B7").addTestDevice("C07AF1687B80C3A74C718498EF9B938A")
                            .build());
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

        mInterstitialAd_wall.setAdUnitId("ca-app-pub-7255339257613393/8699351909");
        mInterstitialAd_wall.loadAd(new AdRequest.Builder()
//                .addTestDevice("623B1B7759D51209294A77125459D9B7")
//                .addTestDevice("C07AF1687B80C3A74C718498EF9B938A").addTestDevice("882530CA8147915F79DF99860BF2F5B0")
//                .addTestDevice("D7397574F6CC21785588738256355351")
                .build());

        mInterstitialAd_wall.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                if (intentToWall) {
                    intentToWall = false;
                    onIntentToWall();
                }
                if (!mInterstitialAd_wall.isLoaded() && !mInterstitialAd_wall.isLoading()) {
                    mInterstitialAd_wall.loadAd(new AdRequest.Builder()
//                            .addTestDevice("623B1B7759D51209294A77125459D9B7").addTestDevice("C07AF1687B80C3A74C718498EF9B938A")
                            .build());
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

        mInterstitialAd_gif.setAdUnitId("ca-app-pub-7255339257613393/8699351909");
        mInterstitialAd_gif.loadAd(new AdRequest.Builder()
//                .addTestDevice("623B1B7759D51209294A77125459D9B7")
//                .addTestDevice("C07AF1687B80C3A74C718498EF9B938A").addTestDevice("882530CA8147915F79DF99860BF2F5B0")
//                .addTestDevice("D7397574F6CC21785588738256355351")
                .build());

        mInterstitialAd_gif.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                if (intentToGifs) {
                    intentToGifs = false;
                    onIntentToGif();
                }
                if (!mInterstitialAd_gif.isLoaded() && !mInterstitialAd_gif.isLoading()) {
                    mInterstitialAd_gif.loadAd(new AdRequest.Builder()
//                            .addTestDevice("623B1B7759D51209294A77125459D9B7").addTestDevice("C07AF1687B80C3A74C718498EF9B938A")
                            .build());
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

        mInterstitialAd_status.setAdUnitId("ca-app-pub-7255339257613393/8699351909");
        mInterstitialAd_status.loadAd(new AdRequest.Builder()
//                .addTestDevice("623B1B7759D51209294A77125459D9B7")
//                .addTestDevice("C07AF1687B80C3A74C718498EF9B938A").addTestDevice("882530CA8147915F79DF99860BF2F5B0")
//                .addTestDevice("D7397574F6CC21785588738256355351")
                .build());

        mInterstitialAd_status.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                if (intentToStatus) {
                    intentToStatus = false;
                    onIntentToStatus();
                }
                if (!mInterstitialAd_status.isLoaded() && !mInterstitialAd_status.isLoading()) {
                    mInterstitialAd_status.loadAd(new AdRequest.Builder()
//                            .addTestDevice("623B1B7759D51209294A77125459D9B7").addTestDevice("C07AF1687B80C3A74C718498EF9B938A")
                            .build());
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

        mInterstitialAd_nondefault.setAdUnitId("ca-app-pub-7255339257613393/8699351909");
        mInterstitialAd_nondefault.loadAd(new AdRequest.Builder()
//                .addTestDevice("623B1B7759D51209294A77125459D9B7")
//                .addTestDevice("C07AF1687B80C3A74C718498EF9B938A").addTestDevice("882530CA8147915F79DF99860BF2F5B0")
//                .addTestDevice("D7397574F6CC21785588738256355351")
                .build());

        mInterstitialAd_nondefault.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                if (intentToStatus) {
                    intentToStatus = false;
                    onIntentToStatus();
                }
                if (!mInterstitialAd_nondefault.isLoaded() && !mInterstitialAd_nondefault.isLoading()) {
                    mInterstitialAd_nondefault.loadAd(new AdRequest.Builder()
//                            .addTestDevice("623B1B7759D51209294A77125459D9B7").addTestDevice("C07AF1687B80C3A74C718498EF9B938A")
                            .build());
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
            fetchFiles();
        }
    }

    private void checkDarkMode() {
        if (settings.getBoolean("isNightMode", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

    private void initNavigationDrawer() {
        setSupportActionBar(findViewById(R.id.toolbar));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            drawerLayout.closeDrawers();
            if (id == R.id.nav_home) {
                if (getSupportFragmentManager().getBackStackEntryCount() != 0)
                    onBackPressed();
                return (true);
            } else if (id == R.id.nav_about) {
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    getSupportFragmentManager().beginTransaction().add(R.id.mainLayout, AboutFragment.newInstance()).addToBackStack(null).commit();
                    Objects.requireNonNull(getSupportActionBar()).hide();
                    return (true);
                }
            } else if (id == R.id.nav_switch) {
                ((SwitchCompat) item.getActionView()).toggle();
                return (true);
            }
            return true;
        });

        SwitchCompat drawerSwitch = (SwitchCompat) (navigationView.getMenu().findItem(R.id.nav_switch).getActionView());

        drawerSwitch.setChecked(settings.getBoolean("isNightMode", false));

        drawerSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            drawerLayout.closeDrawers();
            Toast.makeText(this, "Applying Changes...", Toast.LENGTH_SHORT).show();
            if (isChecked) {
                editor.putBoolean("isNightMode", true).apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                editor.putBoolean("isNightMode", false).apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
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

    @Override
    public void onBackPressed() {

        if (!Objects.requireNonNull(getSupportActionBar()).isShowing())
            Objects.requireNonNull(getSupportActionBar()).show();

        super.onBackPressed();
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

    public void onIntentToImages() {
        Intent intent = new Intent(MainActivity.this, TabLayoutActivity.class);
        intent.putExtra("category", DataHolder.IMAGE);
        startActivity(intent);
    }

    public void onIntentToVideos() {
        Intent intent = new Intent(MainActivity.this, TabLayoutActivity.class);
        intent.putExtra("category", DataHolder.VIDEO);
        startActivity(intent);
    }

    public void onIntentToDoc() {
        Intent intent = new Intent(MainActivity.this, TabLayoutActivity.class);
        intent.putExtra("category", DataHolder.DOCUMENT);
        startActivity(intent);
    }

    public void onIntentToAudio() {
        Intent intent = new Intent(MainActivity.this, TabLayoutActivity.class);
        intent.putExtra("category", DataHolder.AUDIO);
        startActivity(intent);
    }

    public void onIntentToGif() {
        Intent intent = new Intent(MainActivity.this, TabLayoutActivity.class);
        intent.putExtra("category", DataHolder.GIF);
        startActivity(intent);
    }

    public void onIntentToWall() {
        Intent intent = new Intent(MainActivity.this, TabLayoutActivity.class);
        intent.putExtra("category", DataHolder.WALLPAPER);
        startActivity(intent);
    }

    public void onIntentToVoice() {
        Intent intent = new Intent(MainActivity.this, TabLayoutActivity.class);
        intent.putExtra("category", DataHolder.VOICE);
        startActivity(intent);
    }

    public void onIntentToStatus() {
        Intent intent = new Intent(MainActivity.this, TabLayoutActivity_test.class);
        intent.putExtra("category", DataHolder.STATUS);
        startActivity(intent);
    }

    public void onIntentToNonDefault(String path) {
        Intent intent = new Intent(MainActivity.this, TabLayoutActivity.class);
        intent.putExtra("category", DataHolder.NONDEFAULT);
        intent.putExtra("pathname", path);
        startActivity(intent);
    }

    @Override
    public void onImagesClicked() {
        if (mInterstitialAd_images.isLoaded() && mInterstitialAd_images != null) {
            mInterstitialAd_images.show();
            intentToImages = true;
        } else {
            Log.e("TAG", "Not loaded");
            if (hasPermission()) {
                onIntentToImages();
            } else {
                askPermission();
            }
        }
    }

    @Override
    public void onDocumentsClicked() {
        if (mInterstitialAd_doc.isLoaded() && mInterstitialAd_doc != null) {
            mInterstitialAd_doc.show();
            intentToDocuments = true;
        } else {
            Log.e("Tag", "Not Loaded");
            if (hasPermission()) {
                onIntentToDoc();
            } else {
                askPermission();
            }
        }
    }

    @Override
    public void onVideosClicked() {
        if (mInterstitialAd_videos.isLoaded() && mInterstitialAd_videos != null) {
            mInterstitialAd_videos.show();
            intentToVideos = true;
        } else {
            Log.e("TAG", "Not Loaded");
            if (hasPermission()) {
                onIntentToVideos();
            } else {
                askPermission();
            }
        }
    }

    @Override
    public void onAudiosClicked() {
        if (mInterstitialAd_audio.isLoaded() && mInterstitialAd_audio != null) {
            mInterstitialAd_audio.show();
            intentToAudios = true;
        } else {
            Log.e("TAG", "Not loaded");
            if (hasPermission()) {
                onIntentToAudio();
            } else {
                askPermission();
            }
        }
    }

    @Override
    public void onGifsClicked() {
        if (mInterstitialAd_gif.isLoaded() && mInterstitialAd_gif != null) {
            mInterstitialAd_gif.show();
            intentToGifs = true;
        } else {
            Log.e("Tag", "NotLoaded");
            if (hasPermission()) {
                onIntentToGif();
            } else {
                askPermission();
            }
        }
    }

    @Override
    public void onWallpapersClicked() {
        if (mInterstitialAd_wall.isLoaded() && mInterstitialAd_wall != null) {
            mInterstitialAd_wall.show();
            intentToWall = true;
        } else {
            Log.e("TAG", "Not loaded");
            if (hasPermission()) {
                onIntentToWall();
            } else {
                askPermission();
            }
        }
    }

    @Override
    public void onVoiceClicked() {
        if (mInterstitialAd_voice.isLoaded() && mInterstitialAd_voice != null) {
            mInterstitialAd_voice.show();
            intentToVoice = true;
        } else {
            Log.e("TAG", "Not loaded");
            if (hasPermission()) {
                onIntentToVoice();
            } else {
                askPermission();
            }
        }
    }

    @Override
    public void onStatusClicked() {
        if (mInterstitialAd_status.isLoaded() && mInterstitialAd_status != null) {
            mInterstitialAd_status.show();
            intentToStatus = true;
        } else {
            Log.e("TAG", "Not loaded");
            if (hasPermission()) {
                onIntentToStatus();
            } else {
                askPermission();
            }
        }
    }

    @Override
    public void onNonDefaultClicked(String path) {
        if (mInterstitialAd_nondefault.isLoaded() && mInterstitialAd_nondefault != null) {
            mInterstitialAd_nondefault.show();
        } else {
            Log.e("TAG", "Not loaded");
            if (hasPermission()) {
                onIntentToNonDefault(path);
            } else {
                askPermission();
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private static class FetchFiles extends AsyncTask<Void, Void, String> {

        private final WeakReference<MainActivity> mainActivityWeakReference;

        FetchFiles(MainActivity mainActivity) {
            this.mainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        protected String doInBackground(Void... voids) {

            File root = new File(Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/");

            File[] listOfFiles = root.listFiles();
            long tot_size = 0;
            if (listOfFiles != null) {
                for (File listOfFile : listOfFiles) {
                    long size = FileUtils.sizeOfDirectory(listOfFile);
                    Log.d(TAG, listOfFile.getPath());
                    tot_size += size;
                }
            }
            mainActivityWeakReference.get().sum = tot_size;

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
            mainActivityWeakReference.get().path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/.Statuses";
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

            mainActivityWeakReference.get().tot_dat = Formatter.formatShortFileSize(mainActivityWeakReference.get(), mainActivityWeakReference.get().sum);

            //For Images,documents and Videos
            mainActivityWeakReference.get().dataList1.clear();
            mainActivityWeakReference.get().dataList1.add(new Details(
                    "Images",
                    mainActivityWeakReference.get().data_img, DataHolder.imagesReceivedPath,
                    R.drawable.ic_image,
                    R.color.green));
            mainActivityWeakReference.get().dataList1.add(new Details(
                    "Documents",
                    mainActivityWeakReference.get().data_doc, DataHolder.documentsReceivedPath,
                    R.drawable.ic_folder,
                    R.color.orange));
            mainActivityWeakReference.get().dataList1.add(new Details(
                    "Videos",
                    mainActivityWeakReference.get().data_vid, DataHolder.videosReceivedPath,
                    R.drawable.ic_video,
                    R.color.blue));
            mainActivityWeakReference.get().dataList1.add(new Details(
                    "Statuses",
                    mainActivityWeakReference.get().data_status, DataHolder.statuscache,
                    R.drawable.ic_status,
                    R.color.orange
            ));

            mainActivityWeakReference.get().dataList.clear();
            mainActivityWeakReference.get().dataList.add(new Details(
                    "Audio files",
                    mainActivityWeakReference.get().data_aud, DataHolder.audiosReceivedPath,
                    R.drawable.ic_library_music_black,
                    R.color.purple));
            mainActivityWeakReference.get().dataList.add(new Details(
                    "Voice files",
                    mainActivityWeakReference.get().data_voice, DataHolder.voiceReceivedPath,
                    R.drawable.ic_queue_music_black,
                    R.color.lightblue));
            mainActivityWeakReference.get().dataList.add(new Details(
                    "Wallpapers",
                    mainActivityWeakReference.get().data_wall, DataHolder.wallReceivedPath,
                    R.drawable.ic_image,
                    R.color.maroon));
            mainActivityWeakReference.get().dataList.add(new Details(
                    "GIFs",
                    mainActivityWeakReference.get().data_gif, DataHolder.gifReceivedPath,
                    R.drawable.camera_iris,
                    R.color.orange));

            //Adding the files not present in the default files list
            if (listOfFiles != null) {
                for (File listOfFile : listOfFiles) {
                    if (!mainActivityWeakReference.get().defaultList.contains(listOfFile)) {
                        long size = FileUtils.sizeOfDirectory(listOfFile);
                        String pathName = listOfFile.getPath();
                        String folderName = pathName.substring(pathName.indexOf("a/") + 2);

                        if (folderName.startsWith("WhatsApp ")) {
                            folderName = folderName.substring(9);
                        }

                        String data = Formatter.formatShortFileSize(mainActivityWeakReference.get(), size);
                        mainActivityWeakReference.get().dataList.add(new Details(
                                folderName,
                                data, pathName,
                                R.drawable.ic_folder,
                                R.color.black));
                    }
                }
            }

            return mainActivityWeakReference.get().tot_dat;
        }

        @Override
        protected void onPostExecute(String s) {

            setUpPieChart();

            mainActivityWeakReference.get().total_data.setText(String.format("%s Files", s));
            mainActivityWeakReference.get().detailsAdapter1.notifyDataSetChanged();
            mainActivityWeakReference.get().detailsAdaptercustom.notifyDataSetChanged();
            mainActivityWeakReference.get().progressBar.setVisibility(View.INVISIBLE);
        }

        private void setUpPieChart() {
            ArrayList<PieEntry> pieEntries = new ArrayList<>();

            int power = 1000000;

            if ((mainActivityWeakReference.get().size_status / power) != 0)
                pieEntries.add(new PieEntry(mainActivityWeakReference.get().size_status / power, "Status", R.drawable.ic_status));
            if ((mainActivityWeakReference.get().size_voice / power) != 0)
                pieEntries.add(new PieEntry(mainActivityWeakReference.get().size_voice / power, "Voice", R.drawable.ic_queue_music_black));
            if ((mainActivityWeakReference.get().size_gif / power) != 0)
                pieEntries.add(new PieEntry(mainActivityWeakReference.get().size_gif / power, "GIF", R.drawable.camera_iris));
            if ((mainActivityWeakReference.get().size_wall / power) != 0)
                pieEntries.add(new PieEntry(mainActivityWeakReference.get().size_wall / power, "Wallpaper", R.drawable.ic_image));
            if ((mainActivityWeakReference.get().size_aud / power) != 0)
                pieEntries.add(new PieEntry(mainActivityWeakReference.get().size_aud / power, "Audio", R.drawable.ic_library_music_black));
            if ((mainActivityWeakReference.get().size_vid / power) != 0)
                pieEntries.add(new PieEntry(mainActivityWeakReference.get().size_vid / power, "Video", R.drawable.video));
            if ((mainActivityWeakReference.get().size_doc / power) != 0)
                pieEntries.add(new PieEntry(mainActivityWeakReference.get().size_doc / power, "Document", R.drawable.ic_folder));
            if ((mainActivityWeakReference.get().size_img / power) != 0)
                pieEntries.add(new PieEntry(mainActivityWeakReference.get().size_img / power, "Image", R.drawable.ic_image));

            PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
            pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            pieDataSet.setValueTextColor(Color.BLACK);
            pieDataSet.setValueTextSize(10f);

            PieData data = new PieData(pieDataSet);
            PieChart pieChart = mainActivityWeakReference.get().findViewById(R.id.chart);

            pieChart.setEntryLabelColor(Color.TRANSPARENT);
            pieChart.setData(data);
            pieChart.setVisibility(View.VISIBLE);
            pieChart.getLegend().setEnabled(false);

            SpannableString s = new SpannableString("In MB");
            s.setSpan(new RelativeSizeSpan(2f), 0, s.length(), 0);
            s.setSpan(new StyleSpan(Typeface.BOLD), 0, s.length(), 0);

            pieChart.setCenterText(s);
            pieChart.getDescription().setEnabled(false);
            pieChart.animateY(1000);
            pieChart.setEntryLabelColor(Color.BLACK);
        }
    }
}

