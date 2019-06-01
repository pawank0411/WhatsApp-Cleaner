package com.example.pawan.whatsAppcleaner.tabs.Wallpaper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pawan.whatsAppcleaner.adapters.innerAdapeters.InnerDetailsAdapter_image;
import com.example.pawan.whatsAppcleaner.datas.FileDetails;
import com.example.pawan.whatsAppcleaner.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


public class wallpaper extends Fragment implements  InnerDetailsAdapter_image.OnCheckboxListener {


    RecyclerView recyclerView;
    Button button;
    private ImageView no_files;
    private InnerDetailsAdapter_image innerDetailsAdapterImage;
    private ArrayList<FileDetails> innerdatalist = new ArrayList<>();


    private double len;
    private String byteMake;
    private static final long GiB = 1024 * 1024 * 1024;
    private static final long MiB = 1024 * 1024;
    private static final long KiB = 1024;

    private ProgressDialog progressDialog;
    private ArrayList<FileDetails> filesToDelete = new ArrayList<>();

    private AdView mAdView;
    private TextView no_ads;

    private static final String AD_ID = "ca-app-pub-7255339257613393~8837303265";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView;
        String path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WallPaper";
        rootView = inflater.inflate(R.layout.image_activity, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view);
        button = rootView.findViewById(R.id.delete);
        no_files = rootView.findViewById(R.id.nofiles);

        progressDialog = new ProgressDialog(getContext());
        no_ads = rootView.findViewById(R.id.ads_not_loaded);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setMessage("Are you sure you want to delete selected files?")
                        .setCancelable(true)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int success = -1;
                                ArrayList<FileDetails> deletedFiles = new ArrayList<>();

                                for (FileDetails details : filesToDelete) {
                                    File file = new File(details.getPath());
                                    if (file.exists()) {
                                        if (file.delete()) {
                                            deletedFiles.add(details);
                                            if (success == 0) {
                                                return;
                                            }
                                            success = 1;
                                        } else {
                                            Log.e("TEST", "" + file.getName() + " delete failed");
                                            success = 0;
                                        }
                                    } else {
                                        Log.e("TEST", "" + file.getName() + " doesn't exists");
                                        success = 0;
                                    }
                                }

                                filesToDelete.clear();

                                for (FileDetails deletedFile : deletedFiles) {
                                    innerdatalist.remove(deletedFile);
                                }
                                innerDetailsAdapterImage.notifyDataSetChanged();
                                if (success == 0) {
                                    Toast.makeText(getContext(), "Couldn't delete some files", Toast.LENGTH_SHORT).show();
                                } else if (success == 1) {
                                    Toast.makeText(getContext(), "Deleted successfully", Toast.LENGTH_SHORT).show();
                                }
                                button.setText("Delete Selected Items (0B)");
                                button.setTextColor(Color.parseColor("#A9A9A9"));
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Network", 0);
        boolean status = sharedPreferences.getBoolean("Status", false);

        if (status == true) {
            no_ads.setVisibility(View.INVISIBLE);

            MobileAds.initialize(getContext(),
                    AD_ID);
            mAdView = new AdView(getContext());
            mAdView.setAdSize(AdSize.BANNER);
            mAdView.setAdUnitId("ca-app-pub-7255339257613393/2279288290");

            mAdView = rootView.findViewById(R.id.adView);
            mAdView.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7").build());

            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    if (mAdView.isLoading()) {
                        no_ads.setVisibility(View.VISIBLE);
                        no_ads.setText("Soory For Ads, but as a Student it will fulfill my daily Bread Butter needs.");
                        mAdView.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7").build());

                    }

                }
            });

            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    Log.e("Loaded", "Loaded");
                }
            });

            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int i) {
                    Log.e("Bannercode", String.valueOf(i));
                    no_ads.setVisibility(View.VISIBLE);
                    no_ads.setText("Soory For Ads, but as a Student it will fulfill my daily Bread Butter needs.");
                }
            });

        } else {
            no_ads.setVisibility(View.VISIBLE);
            no_ads.setText("Soory For Ads, but as a Student it will fulfill my daily Bread Butter needs.");
        }

        new wallpaper.Fetchfiles(this, new wallpaper.Fetchfiles.OnFilesFetched() {
            @Override
            public void onFilesFetched(List<FileDetails> fileDetails) {
                if (fileDetails != null && !fileDetails.isEmpty()) {
                    innerdatalist.addAll(fileDetails);
                    innerDetailsAdapterImage.notifyDataSetChanged();
                    progressDialog.dismiss();
                    no_files.setVisibility(View.INVISIBLE);
                } else {
                    progressDialog.dismiss();
                    Log.e("Nofiles", "NO Files Found");
                    no_files.setVisibility(View.VISIBLE);
                    no_files.setImageResource(R.drawable.file);
                }
            }
        }).execute(path);

        MobileAds.initialize(getContext(),
                AD_ID);
        mAdView = new AdView(getContext());
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId("ca-app-pub-7255339257613393/2279288290");

        mAdView = rootView.findViewById(R.id.adView);
        mAdView.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7").build());

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                if (mAdView.isLoading()) {
                    mAdView.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7").build());
                }

            }
        });

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.e("Loaded", "Loaded");
            }
        });

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                Log.e("Bannercode", String.valueOf(i));
            }
        });

        return rootView;
    }

    private static class Fetchfiles extends AsyncTask<String, Void, Object>{

        private OnFilesFetched onFilesFetched;
        private WeakReference<wallpaper> wallpaperWeakReference;

        Fetchfiles(wallpaper wallpaper, OnFilesFetched onFilesFetched){
            wallpaperWeakReference = new WeakReference<>(wallpaper);
            this.onFilesFetched = onFilesFetched;
        }

        @Override
        protected void onPreExecute() {

            wallpaperWeakReference.get().progressDialog =   new ProgressDialog(wallpaperWeakReference.get().getContext());
            wallpaperWeakReference.get().progressDialog.setMessage("Please Wait");
            wallpaperWeakReference.get().progressDialog.setCancelable(false);
            if (!wallpaperWeakReference.get().progressDialog.isShowing()) {
                wallpaperWeakReference.get().progressDialog.show();
            }
        }
        @Override
        protected Object doInBackground(String... strings) {

            String path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WallPaper";

                        File directory = new File(path);

            ArrayList<FileDetails> fileList1 = new ArrayList<>();

            if (path != null) {

                File[] results = directory.listFiles();
                if (results != null) {
                    for (int i = 0; i < results.length; i++) {
                        if (results[i].isDirectory()) {
                        } else {
                            FileDetails fileDetails = new FileDetails();
                            fileDetails.setName(results[i].getName());
                            fileDetails.setPath(results[i].getPath());
                            fileDetails.setSize("" + wallpaperWeakReference.get().getFileSize(results[i]));
                            fileList1.add(fileDetails);
                        }
                    }
                    Log.e("Files", "files found: " + fileList1.toString());
                } else {
                    wallpaperWeakReference.get().no_files.setVisibility(View.VISIBLE);
                    wallpaperWeakReference.get().no_files.setImageResource(R.drawable.file);
                    Log.e("Files", "No files found in " + directory.getName());
                }

            } else {
                Log.e("Files", "No files found in " + directory.getName());
            }

          return fileList1;
        }

        public interface OnFilesFetched {
            void onFilesFetched(List<FileDetails> fileDetails);
        }
    }

   private String getFileSize(File file) {
        NumberFormat format = new DecimalFormat("#.##");
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
        final double length = file.length();

        if (file.isFile()) {
            if (length > GiB) {
                len = length / GiB;
                byteMake = "GB";
                return format.format(length / GiB) + " GB";
            } else if (length > MiB) {
                len = length / MiB;
                byteMake = "MB";
                return format.format(length / MiB) + " MB";
            } else if (length > KiB) {
                len = length / KiB;
                byteMake = "KB";
                return format.format(length / KiB) + " KB";
            }else
                return format.format(length) + " B";
        } else {
            len = 0;
        }
        return "";
    }


    @Override
    public void onCheckboxClicked(View view, List<FileDetails> pos) {
        filesToDelete.clear();

        for (FileDetails details : pos) {
            if (details.isSelected()) {
                filesToDelete.add(details);
            }
        }

        if (filesToDelete.size() > 0) {

            long totalFileSize = 0;

            for (FileDetails details : filesToDelete) {
                File file = new File(details.getPath());
                totalFileSize += file.length();
            }

            String size = Formatter.formatShortFileSize(getContext(), totalFileSize);
            button.setText("Delete Selected Items (" + size + ")");
            button.setTextColor(Color.parseColor("#C103A9F4"));
        } else {
            button.setText("Delete Selected Items (0B)");
            button.setTextColor(Color.parseColor("#A9A9A9"));
        }



    }
}