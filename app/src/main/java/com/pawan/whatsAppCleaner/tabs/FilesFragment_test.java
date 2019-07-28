package com.pawan.whatsAppCleaner.tabs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUriExposedException;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pawan.whatsAppCleaner.CheckRecentRun;
import com.pawan.whatsAppCleaner.DataHolder;
import com.pawan.whatsAppCleaner.MainActivity;
import com.pawan.whatsAppCleaner.R;
import com.pawan.whatsAppCleaner.adapters.innerAdapeters.InnerDetailsAdapter;
import com.pawan.whatsAppCleaner.datas.FileDetails;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class FilesFragment_test extends Fragment implements InnerDetailsAdapter.OnCheckboxListener {

    private Button button, date, name, size;
    private TextView no_ads;
    private ImageView no_files;
    private InnerDetailsAdapter innerDetailsAdapter;
    private ArrayList<FileDetails> innerDataList = new ArrayList<>();
    private ArrayList<FileDetails> filesToDelete = new ArrayList<>();
    private String path_tab = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/.Status Download";
    private ProgressDialog progressDialog;
    private AdView mAdView;
    @SuppressWarnings("FieldCanBeLocal")
    private CheckBox selectall;
    private boolean flag_d = true, flag_n = true, flag_s = true;
    private File source;
    private String statusdownload;
    private int position;
    @SuppressWarnings("FieldCanBeLocal")
    private SharedPreferences settings = null;
    @SuppressWarnings("FieldCanBeLocal")
    private SharedPreferences.Editor editor = null;
    private final static String TAG = "MainActivity";
    public final static String PREFS = "PrefsFile";
    @SuppressWarnings("FieldCanBeLocal")
    private FirebaseAnalytics mFirebaseAnalytics;

    public static FilesFragment_test newInstance(String category, String path) {
        FilesFragment_test filesFragment_test = new FilesFragment_test();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        bundle.putString("category", category);
        filesFragment_test.setArguments(bundle);
        return filesFragment_test;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView;
        String category = null;
        String path = null;

        if (getArguments() != null) {
            path = getArguments().getString("path");
            category = getArguments().getString("category");
        } else {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            if (getActivity() != null)
                getActivity().finish();
        }

        if (category == null) {
            if (getActivity() != null)
                getActivity().finish();
            return null;
        }

        RecyclerView recyclerView;
        int STATUS = 1;

        switch (category) {
            default:
            case DataHolder.IMAGE:
                rootView = inflater.inflate(R.layout.status_activity, container, false);
                recyclerView = rootView.findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                innerDetailsAdapter = new InnerDetailsAdapter(STATUS, getActivity(), innerDataList, this);
                break;
        }
        settings = getContext().getSharedPreferences(PREFS, MODE_PRIVATE);



        // First time running app?
        if (!settings.contains("lastRun"))
            enableNotification(null);
        else
            recordRunTime();

        Log.v(TAG, "Starting CheckRecentRun service...");
        getContext().startService(new Intent(getContext(), CheckRecentRun.class));
        //Switch toggle = rootView.findViewById(R.id.switch1);
        no_ads = rootView.findViewById(R.id.ads_not_loaded);

        button = rootView.findViewById(R.id.delete);
        date = rootView.findViewById(R.id.date);
        name = rootView.findViewById(R.id.name);
        size = rootView.findViewById(R.id.size);
        no_files = rootView.findViewById(R.id.nofiles);
        mAdView = rootView.findViewById(R.id.adView);
        selectall = rootView.findViewById(R.id.selectall);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(innerDetailsAdapter);

        SharedPreferences sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences("Network", 0);
        boolean status = sharedPreferences.getBoolean("Status", false);

        if (status) {
            no_ads.setVisibility(View.VISIBLE);
            no_ads.setText(R.string.no_ads);


            mAdView.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7").build());

            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    if (!mAdView.isLoading()) {
                        mAdView.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7").build());
                    }
                }

                @Override
                public void onAdLoaded() {
                    Log.e("Banner", "Loaded");
                    no_ads.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    Log.e("Bannercode", String.valueOf(i));
                }
            });
        } else {
            no_ads.setVisibility(View.VISIBLE);
            no_ads.setText(R.string.no_ads);
        }

        Log.e("TEST", "" + path);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        final Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(recyclerView));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, String.valueOf(recyclerView));
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(button));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, String.valueOf(button));
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "name");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        new FetchFiles(this, category, new FetchFiles.OnFilesFetched() {
            @Override
            public void onFilesFetched(List<FileDetails> fileDetails) {
                if (fileDetails != null && !fileDetails.isEmpty()) {
                    statusdownload = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/.Status Download";
                    File check = new File(statusdownload);
                    File[] list = check.listFiles();
                    if (list != null) {
                        for (File file : list) {
                            for (int j = 0; j < fileDetails.size(); j++) {
                                String s = file.getName();
                                String v = fileDetails.get(j).getName();
                                if (s.equals(v)) {
                                    Log.e("dup", fileDetails.get(j).getName());
                                    fileDetails.remove(j);
                                    break;
                                }
                            }
                        }
                    }
                    if (!fileDetails.isEmpty()) {
                        innerDataList.addAll(fileDetails);
                        innerDetailsAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                        no_files.setVisibility(View.INVISIBLE);
                    } else {
                        progressDialog.dismiss();
                        Log.e("Nofiles", "NO Files Found");
                        no_files.setVisibility(View.VISIBLE);
                        no_files.setImageResource(R.drawable.file);
                    }

                } else {
                    progressDialog.dismiss();
                    Log.e("Nofiles", "NO Files Found");
                    no_files.setVisibility(View.VISIBLE);
                    no_files.setImageResource(R.drawable.file);
                }
            }
        }).execute(path);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag_n || !flag_s) {
                    flag_n = true;
                    flag_s = true;
                    name.setTextColor(Color.parseColor("#FF161616"));
                    size.setTextColor(Color.parseColor("#FF161616"));
                }
                if (flag_d) {
//                    Toast.makeText(getContext(), "sorted", Toast.LENGTH_SHORT).show();
                    flag_d = false;
                    date.setTextColor(Color.parseColor("#C103A9F4"));
                    Collections.sort(innerDataList, new Comparator<FileDetails>() {
                        @Override
                        public int compare(FileDetails o1, FileDetails o2) {
                            return -o1.getMod().compareTo(o2.getMod());
                        }
                    });
                    innerDetailsAdapter.notifyDataSetChanged();
                } else {
//                    Toast.makeText(getContext(), "Unsorted", Toast.LENGTH_SHORT).show();
                    flag_d = true;
                    date.setTextColor(Color.parseColor("#FF161616"));
                    Collections.sort(innerDataList, new Comparator<FileDetails>() {
                        @Override
                        public int compare(FileDetails o1, FileDetails o2) {
                            return o1.getMod().compareTo(o2.getMod());
                        }
                    });
                    Log.e("State", "Disabled");
                    innerDetailsAdapter.notifyDataSetChanged();
                }
            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag_d || !flag_s) {
                    flag_d = true;
                    flag_s = true;
                    date.setTextColor(Color.parseColor("#FF161616"));
                    size.setTextColor(Color.parseColor("#FF161616"));
                }
                if (flag_n) {
//                    Toast.makeText(getContext(), "sorted", Toast.LENGTH_SHORT).show();
                    flag_n = false;
                    name.setTextColor(Color.parseColor("#C103A9F4"));
                    Collections.sort(innerDataList, new Comparator<FileDetails>() {
                        @Override
                        public int compare(FileDetails o1, FileDetails o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                    Log.e("State", "Toggled");
                    innerDetailsAdapter.notifyDataSetChanged();
                } else {
//                    Toast.makeText(getContext(), "Unsorted", Toast.LENGTH_SHORT).show();
                    flag_n = true;
                    name.setTextColor(Color.parseColor("#FF161616"));
                    Collections.sort(innerDataList, new Comparator<FileDetails>() {
                        @Override
                        public int compare(FileDetails o1, FileDetails o2) {
                            return -o1.getName().compareTo(o2.getName());
                        }
                    });
                    Log.e("State", "Disabled");
                    innerDetailsAdapter.notifyDataSetChanged();
                }
            }
        });
        size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag_d || !flag_n) {
                    flag_d = true;
                    flag_n = true;
                    date.setTextColor(Color.parseColor("#FF161616"));
                    name.setTextColor(Color.parseColor("#FF161616"));
                }
                if (flag_s) {
//                    Toast.makeText(getContext(), "sorted", Toast.LENGTH_SHORT).show();
                    flag_s = false;
                    size.setTextColor(Color.parseColor("#C103A9F4"));
                    Collections.sort(innerDataList, new Comparator<FileDetails>() {
                        @Override
                        public int compare(FileDetails o1, FileDetails o2) {
                            return -o1.getS().compareTo(o2.getS());
                        }
                    });
                    Log.e("State", "Toggled");
                    innerDetailsAdapter.notifyDataSetChanged();
                } else {
//                    Toast.makeText(getContext(), "Unsorted", Toast.LENGTH_SHORT).show();
                    flag_s = true;
                    size.setTextColor(Color.parseColor("#FF161616"));
                    Collections.sort(innerDataList, new Comparator<FileDetails>() {
                        @Override
                        public int compare(FileDetails o1, FileDetails o2) {
                            return o1.getS().compareTo(o2.getS());
                        }
                    });
                    Log.e("State", "Disabled");
                    innerDetailsAdapter.notifyDataSetChanged();
                }
            }
        });

        selectall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (int i = 0; i < innerDataList.size(); i++) {
                        innerDataList.get(i).setSelected(true);
                    }
                    innerDetailsAdapter.notifyDataSetChanged();
                } else {
                    for (int i = 0; i < innerDataList.size(); i++) {
                        innerDataList.get(i).setSelected(false);
                    }
                    innerDetailsAdapter.notifyDataSetChanged();
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!filesToDelete.isEmpty()) {
                    int success = -1;
                    ArrayList<FileDetails> deletedFiles = new ArrayList<>();
                    for (FileDetails details : filesToDelete) {
                        String soure = details.getPath();
                        statusdownload = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/.Status Download/" + details.getName();
                        source = new File(soure);
                        if (!source.exists()) {
                            if (!source.mkdir()) {
                                Log.e("FIle", "Can't be created");
                            } else {
                                Log.e("FIle", "created");
                            }
                        }
                        File dest = new File(statusdownload);
                        try {
                            FileUtils.copyFile(source, dest);
                            success = 1;
                            deletedFiles.add(details);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("exp", String.valueOf(e));
                            success = 0;
                        }
                    }
                    filesToDelete.clear();
                    if (selectall.isChecked()){
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                    }
                    for (FileDetails deletedFile : deletedFiles) {
                        innerDataList.remove(deletedFile);
                    }
                    innerDetailsAdapter.notifyDataSetChanged();
                    if (success == 0) {
                        Toast.makeText(getContext(), "Couldn't download some files", Toast.LENGTH_SHORT).show();
                    } else if (success == 1) {
                        Toast.makeText(getContext(), "Downloaded successfully", Toast.LENGTH_SHORT).show();
                    }
                    button.setText(R.string.download_selected_items_0b);
                    button.setTextColor(Color.parseColor("#A9A9A9"));
                }

            }
        });

        TabLayoutActivity_test.setmOnTabListener(new TabLayoutActivity_test.OnTabListener() {
            @Override
            public void ontablistener(TabLayout.Tab tab) {
                position = tab.getPosition();
                if (position == 1) {
                    new FetchFiles_tab(FilesFragment_test.this, new FetchFiles_tab.OnFilesFetched_tab() {
                        @Override
                        public void onFilesFetched_tab(List<FileDetails> fileDetails) {

                            if (fileDetails != null && !fileDetails.isEmpty()) {
                                innerDataList.clear();
                                innerDataList.addAll(fileDetails);
                                innerDetailsAdapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                                no_files.setVisibility(View.INVISIBLE);
                            } else {
                                progressDialog.dismiss();
                                Log.e("Nofiles", "NO Files Found");
                                no_files.setVisibility(View.VISIBLE);
                                no_files.setImageResource(R.drawable.file);
                            }
                        }
                    }).execute(path_tab);
                    button.setText(R.string.share_delete);
                    button.setTextColor(Color.parseColor("#A9A9A9"));

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog.Builder(getContext())
                                    .setMessage("Are you want to share or delete?")
                                    .setCancelable(true)
                                    .setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
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
                                            if (selectall.isChecked()){
                                                Intent intent = new Intent(getContext(), MainActivity.class);
                                                startActivity(intent);
                                            }
                                            for (FileDetails deletedFile : deletedFiles) {
                                                innerDataList.remove(deletedFile);
                                            }
                                            innerDetailsAdapter.notifyDataSetChanged();
                                            if (success == 0) {
                                                Toast.makeText(getContext(), "Couldn't delete some files", Toast.LENGTH_SHORT).show();
                                            } else if (success == 1) {
                                                Toast.makeText(getContext(), "Deleted successfully", Toast.LENGTH_SHORT).show();
                                            }
                                            button.setText(R.string.share_delete);
                                            button.setTextColor(Color.parseColor("#A9A9A9"));
                                        }
                                    })
                                    .setPositiveButton("SHARE", new DialogInterface.OnClickListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.N)
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent sendIntent = new Intent();
                                            try {
                                                ArrayList<Uri> files = new ArrayList<>();
                                                sendIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                                                sendIntent.setType("image/*");
                                                for (FileDetails details : filesToDelete) {
                                                    String soure = details.getPath();
                                                    File file = new File(soure);
                                                    Uri uri = FileProvider.getUriForFile(getContext(), Objects.requireNonNull(getContext()).getApplicationContext().getPackageName() +
                                                            ".my.package.name.provider", file);
                                                    files.add(uri);
                                                }
                                                sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                                                startActivity(sendIntent);
                                            } catch (FileUriExposedException e) {
                                                Log.e("err", String.valueOf(e));
                                                Toast.makeText(getContext(), "An Error occured", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).create().show();
                        }
                    });
                }
            }
        });

        return rootView;
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

    private static long getFileSize(File file) {
        if (file != null && file.isFile()) {
            return file.length();
        }
        return 0;
    }

    @Override
    public void oncheckboxlistener(View view, List<FileDetails> updatedFiles) {

        filesToDelete.clear();

        for (FileDetails details : updatedFiles) {
            if (details.isSelected()) {
                filesToDelete.add(details);
            }
        }
        if (position == 1) {
            if (filesToDelete.size() > 0) {

                long totalFileSize = 0;

                for (FileDetails details : filesToDelete) {
                    File file = new File(details.getPath());
                    totalFileSize += file.length();
                }

                String size = Formatter.formatShortFileSize(getActivity(), totalFileSize);
                button.setText("Share or Delete Selected Items (" + size + ")");
                button.setTextColor(Color.parseColor("#C103A9F4"));
            } else {
                button.setText(R.string.share_delete);
                button.setTextColor(Color.parseColor("#A9A9A9"));
            }
        } else {
            if (filesToDelete.size() > 0) {

                long totalFileSize = 0;

                for (FileDetails details : filesToDelete) {
                    File file = new File(details.getPath());
                    totalFileSize += file.length();
                }

                String size = Formatter.formatShortFileSize(getActivity(), totalFileSize);
                button.setText("Download Selected Items (" + size + ")");
                button.setTextColor(Color.parseColor("#C103A9F4"));
            } else {
                button.setText(R.string.download_selected_items_0b);
                button.setTextColor(Color.parseColor("#A9A9A9"));
            }
        }
    }

    private static class FetchFiles extends AsyncTask<String, Void, Object> {

        private String category;
        private OnFilesFetched onFilesFetched;
        private WeakReference<FilesFragment_test> filesFragmentWeakReference;

        FetchFiles(FilesFragment_test filesFragment_test, String category, OnFilesFetched onFilesFetched) {
            filesFragmentWeakReference = new WeakReference<>(filesFragment_test);
            this.category = category;
            this.onFilesFetched = onFilesFetched;
        }

        @Override
        protected void onPreExecute() {
            // display a progress dialog for good user experiance
            filesFragmentWeakReference.get().progressDialog = new ProgressDialog(filesFragmentWeakReference.get().getContext());
            filesFragmentWeakReference.get().progressDialog.setMessage("Please Wait");
            filesFragmentWeakReference.get().progressDialog.setCancelable(false);
            if (!filesFragmentWeakReference.get().progressDialog.isShowing()) {
                filesFragmentWeakReference.get().progressDialog.show();
            }
        }


        @Override
        protected Object doInBackground(String... strings) {

            String path = strings[0];
            ArrayList<FileDetails> files = new ArrayList<>();

            if (path != null) {
                File directory = new File(path);
                File[] results = directory.listFiles();

                if (results != null) {
                    //Still verify if the file is an image in whatsapp preferred format(jpg)
                    for (final File file : results) {
                        if (DataHolder.STATUS.equals(category)) {
                            if (file.isFile()) {
                                FileDetails fileDetails = new FileDetails();
                                fileDetails.setName(file.getName());
                                fileDetails.setPath(file.getPath());
                                fileDetails.setMod(file.lastModified());
//                                String mime = "*/*";
//                                File a = new File(file.getPath());
//                                Uri uri = FileProvider.getUriForFile(Objects.requireNonNull(filesFragmentWeakReference.get().getContext()),
//                                        Objects.requireNonNull(filesFragmentWeakReference.get().getContext()).getApplicationContext().getPackageName() +
//                                                ".my.package.name.provider", a);
//                                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
//                                if (mimeTypeMap.hasExtension(
//                                        MimeTypeMap.getFileExtensionFromUrl(uri.toString()))) {
//
//                                    mime = Objects.requireNonNull(mimeTypeMap.getMimeTypeFromExtension(
//                                            MimeTypeMap.getFileExtensionFromUrl(uri.toString()))).split("/")[0];
//                                }

                                fileDetails.setSize(Formatter.formatShortFileSize(filesFragmentWeakReference.get().
                                                getContext(),
                                        getFileSize(file)));
                                fileDetails.setS(getFileSize(file));
                                files.add(fileDetails);
                            }
                        }
                    }
                } else {
                    Log.e("Files", "No files found in " + directory.getName());
                }
                Collections.sort(files, new Comparator<FileDetails>() {
                    @Override
                    public int compare(FileDetails o1, FileDetails o2) {
                        filesFragmentWeakReference.get().flag_d = false;
                        return -o1.getMod().compareTo(o2.getMod());
                    }
                });
            } else {
                Log.e("Files", "Path is empty");
            }
            return files;
        }


        @Override
        protected void onPostExecute(Object o) {
            List<FileDetails> files = (List<FileDetails>) o;
            if (onFilesFetched != null) {
                onFilesFetched.onFilesFetched(files);
            }
        }

        public interface OnFilesFetched {
            void onFilesFetched(List<FileDetails> fileDetails);
        }

    }

    private static class FetchFiles_tab extends AsyncTask<String, Void, Object> {

        private OnFilesFetched_tab onFilesFetched;
        private WeakReference<FilesFragment_test> filesFragmentWeakReference_tab;

        FetchFiles_tab(FilesFragment_test filesFragment_test, OnFilesFetched_tab onFilesFetched) {
            filesFragmentWeakReference_tab = new WeakReference<>(filesFragment_test);
            this.onFilesFetched = onFilesFetched;
        }

        @Override
        protected void onPreExecute() {
            // display a progress dialog for good user experiance
            filesFragmentWeakReference_tab.get().progressDialog = new ProgressDialog(filesFragmentWeakReference_tab.get().getContext());
            filesFragmentWeakReference_tab.get().progressDialog.setMessage("Please Wait");
            filesFragmentWeakReference_tab.get().progressDialog.setCancelable(false);
            if (!filesFragmentWeakReference_tab.get().progressDialog.isShowing()) {
                filesFragmentWeakReference_tab.get().progressDialog.show();
            }
        }


        @Override
        protected Object doInBackground(String... strings) {

            String path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/.Status Download";
            ArrayList<FileDetails> files = new ArrayList<>();

            File directory = new File(path);
            File[] results = directory.listFiles();

            if (results != null) {
                //Still verify if the file is an image in whatsapp preferred format(jpg)
                for (File file : results) {
                        if (file.isFile()) {
                            FileDetails fileDetails = new FileDetails();
                            fileDetails.setName(file.getName());
                            fileDetails.setPath(file.getPath());
                            fileDetails.setMod(file.lastModified());
//                            String mime = "*/*";
//                            File a = new File(file.getPath());
//                            Uri uri = FileProvider.getUriForFile(Objects.requireNonNull(filesFragmentWeakReference_tab.get().getContext()),
//                                    Objects.requireNonNull(filesFragmentWeakReference_tab.get().getContext()).getApplicationContext().getPackageName() +
//                                            ".my.package.name.provider", a);
//                            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
//                            if (mimeTypeMap.hasExtension(
//                                    MimeTypeMap.getFileExtensionFromUrl(uri.toString()))) {
//
//                                mime = Objects.requireNonNull(mimeTypeMap.getMimeTypeFromExtension(
//                                        MimeTypeMap.getFileExtensionFromUrl(uri.toString()))).split("/")[0];
//                            }

                            fileDetails.setSize(Formatter.formatShortFileSize(filesFragmentWeakReference_tab.get().
                                            getContext(),
                                    getFileSize(file)));
                            fileDetails.setS(getFileSize(file));
                            files.add(fileDetails);
                        }
                }
            } else {
                Log.e("Files", "No files found in " + directory.getName());
            }
            Collections.sort(files, new Comparator<FileDetails>() {
                @Override
                public int compare(FileDetails o1, FileDetails o2) {
                    filesFragmentWeakReference_tab.get().flag_d = false;
                    return -o1.getMod().compareTo(o2.getMod());
                }
            });
            return files;
        }


        @Override
        protected void onPostExecute(Object o) {
            List<FileDetails> files = (List<FileDetails>) o;
            if (onFilesFetched != null) {
                onFilesFetched.onFilesFetched_tab(files);
            }
        }

        public interface OnFilesFetched_tab {
            void onFilesFetched_tab(List<FileDetails> fileDetails);
        }

    }
}
