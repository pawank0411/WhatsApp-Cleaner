package com.example.pawan.whatsAppcleaner;

import android.Manifest;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.crashlytics.android.Crashlytics;
import com.example.pawan.whatsAppcleaner.adapters.DetailsAdapter;
import com.example.pawan.whatsAppcleaner.adapters.DetailsAdapterCustom;
import com.example.pawan.whatsAppcleaner.datas.Details;
import com.example.pawan.whatsAppcleaner.tabs.TabLayoutActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import io.fabric.sdk.android.Fabric;

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
    private TextView no_ads;
    private RecyclerView recyclerView, recyclerView1;
    private DetailsAdapterCustom detailsAdaptercustom;
    private DetailsAdapter detailsAdapter1;
    private ProgressDialog progressDialog;
    private AdView mAdView, mAdView1;

    private Boolean intenttoimages, intenttovideos, intenttoaudios, intenttodocuments, intenttogifs, intenttowall, intenttovoice;

    private InterstitialAd mInterstitialAd;


    private FirebaseAnalytics mFirebaseAnalytics;
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
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        total_data = findViewById(R.id.data);
        files = findViewById(R.id.files);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView1 = findViewById(R.id.recycle);
        no_ads = findViewById(R.id.ads_not_loaded);
        mAdView = findViewById(R.id.adView_small);
        mAdView1 = findViewById(R.id.adView_large);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Network", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("Status", isNetworkAvailable());
        editor.commit();


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


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7255339257613393/6137674653");
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7")
                .addTestDevice("C07AF1687B80C3A74C718498EF9B938A").addTestDevice("882530CA8147915F79DF99860BF2F5B0")
                .addTestDevice("D7397574F6CC21785588738256355351").build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {

                if (intenttodocuments) {
                    intenttodocuments = false;
                    onIntenttoDoc();
                } else if (intenttoimages) {
                    intenttoimages = false;

                    try {
                        onIntenttoImages();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, String.valueOf(e), Toast.LENGTH_LONG).show();
                    }
                } else if (intenttoaudios) {
                    intenttoaudios = false;
                    try {
                        onIntenttoAudio();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, String.valueOf(e), Toast.LENGTH_LONG).show();
                    }
                } else if (intenttovideos) {
                    intenttovideos = false;
                    try {
                        onIntenttoVideos();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, String.valueOf(e), Toast.LENGTH_LONG).show();
                    }
                } else if (intenttowall) {
                    intenttowall = false;
                    try {
                        onIntenttoWall();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, String.valueOf(e), Toast.LENGTH_LONG).show();
                    }
                } else if (intenttogifs) {
                    intenttogifs = false;
                    try {
                        onIntenttoGif();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, String.valueOf(e), Toast.LENGTH_LONG).show();
                    }
                } else if (intenttovoice) {
                    intenttovoice = false;
                    try {
                        onIntenttoVoice();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, String.valueOf(e), Toast.LENGTH_LONG).show();
                    }
                }

                if (!mInterstitialAd.isLoaded() && !mInterstitialAd.isLoading()) {
                    mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7").addTestDevice("C07AF1687B80C3A74C718498EF9B938A").build());
                }

            }

            @Override
            public void onAdFailedToLoad(int i) {
                Log.e("error code", String.valueOf(i));
            }

            @Override
            public void onAdLoaded() {
                Log.e("Interstitial", "Loaded");
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
        if (mInterstitialAd.isLoaded() && mInterstitialAd != null) {
            mInterstitialAd.show();
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
        if (mInterstitialAd.isLoaded() && mInterstitialAd != null) {
            mInterstitialAd.show();
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
        if (mInterstitialAd.isLoaded() && mInterstitialAd != null) {
            mInterstitialAd.show();
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
        if (mInterstitialAd.isLoaded() && mInterstitialAd != null) {
            mInterstitialAd.show();
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
        if (mInterstitialAd.isLoaded() && mInterstitialAd != null) {
            mInterstitialAd.show();
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
        if (mInterstitialAd.isLoaded() && mInterstitialAd != null) {
            mInterstitialAd.show();
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
        if (mInterstitialAd.isLoaded() && mInterstitialAd != null) {
            mInterstitialAd.show();
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

