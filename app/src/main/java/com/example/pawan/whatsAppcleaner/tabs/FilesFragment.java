package com.example.pawan.whatsAppcleaner.tabs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pawan.whatsAppcleaner.DataHolder;
import com.example.pawan.whatsAppcleaner.R;
import com.example.pawan.whatsAppcleaner.adapters.innerAdapeters.InnerDetailsAdapter;
import com.example.pawan.whatsAppcleaner.datas.FileDetails;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilesFragment extends Fragment implements InnerDetailsAdapter.OnCheckboxListener {

    private RecyclerView recyclerView;
    private Button button;
    private Switch toggle;
    private TextView no_ads;
    private ImageView no_files;
    private InnerDetailsAdapter innerDetailsAdapter;
    private ArrayList<FileDetails> innerDataList = new ArrayList<>();
    private ArrayList<FileDetails> filesToDelete = new ArrayList<>();
    private final int IMAGES = 1;
    private final int VIDEOS = 2;
    private final int AUDIOS = 3;
    private final int FILE = 4;
    private final int VOICE = 6;
    private ProgressDialog progressDialog;
    private AdView mAdView;

    private static final String AD_ID = "ca-app-pub-7255339257613393~8837303265";

    public static FilesFragment newInstance(String category, String path) {
        FilesFragment filesFragment = new FilesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        bundle.putString("category", category);
        filesFragment.setArguments(bundle);
        return filesFragment;
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

        switch (category) {
            default:
            case DataHolder.WALLPAPER:
            case DataHolder.IMAGE:
                rootView = inflater.inflate(R.layout.image_activity, container, false);
                recyclerView = rootView.findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                innerDetailsAdapter = new InnerDetailsAdapter(IMAGES, getActivity(), innerDataList, this);
                break;
            case DataHolder.GIF:
            case DataHolder.VIDEO:
                rootView = inflater.inflate(R.layout.image_activity, container, false);
                recyclerView = rootView.findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                innerDetailsAdapter = new InnerDetailsAdapter(VIDEOS, getActivity(), innerDataList, this);
                break;
            case DataHolder.VOICE:
                rootView = inflater.inflate(R.layout.doc_activity, container, false);
                recyclerView = rootView.findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                innerDetailsAdapter = new InnerDetailsAdapter(VOICE, getActivity(), innerDataList, this);
                break;
            case DataHolder.AUDIO:
                rootView = inflater.inflate(R.layout.doc_activity, container, false);
                recyclerView = rootView.findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                innerDetailsAdapter = new InnerDetailsAdapter(AUDIOS, getActivity(), innerDataList, this);
                break;
            case DataHolder.DOCUMENT:
                rootView = inflater.inflate(R.layout.doc_activity, container, false);
                recyclerView = rootView.findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                innerDetailsAdapter = new InnerDetailsAdapter(FILE, getActivity(), innerDataList, this);
                break;
        }

        toggle = rootView.findViewById(R.id.switch1);
        no_ads = rootView.findViewById(R.id.ads_not_loaded);

        button = rootView.findViewById(R.id.delete);
        no_files = rootView.findViewById(R.id.nofiles);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(innerDetailsAdapter);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Network",0);
        boolean status = sharedPreferences.getBoolean("Status",false);

        if (status){
            no_ads.setVisibility(View.VISIBLE);
            no_ads.setText("Sorry For Ads,  but as a Student it will fulfill my daily Bread Butter needs.");


            mAdView = new AdView(getContext());
            mAdView = rootView.findViewById(R.id.adView);
            mAdView.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7").build());

            mAdView.setAdListener(new AdListener(){
                @Override
                public void onAdClosed() {
                    if (!mAdView.isLoading()) {
                        mAdView.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7").build());
                    }
                }

                @Override
                public void onAdLoaded() {
                    Log.e("Banner","Loaded");
                    no_ads.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    Log.e("Bannercode", String.valueOf(i));
                }
            });
        }
        else {
            no_ads.setVisibility(View.VISIBLE);
            no_ads.setText("Sorry For Ads,  but as a Student it will fulfill my daily Bread Butter needs.");
        }

        Log.e("TEST", "" + path);

        new FetchFiles(this, category, new FetchFiles.OnFilesFetched() {
            @Override
            public void onFilesFetched(List<FileDetails> fileDetails) {
                if (fileDetails != null && !fileDetails.isEmpty()) {
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
        }).execute(path);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Collections.reverse(innerDataList);
                    Log.e("State","Toggled");
                    innerDetailsAdapter.notifyDataSetChanged();
                } else {
                    Collections.reverse(innerDataList);
                    Log.e("State","Disabled");
                    innerDetailsAdapter.notifyDataSetChanged();
                }
            }
        });

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
                                    innerDataList.remove(deletedFile);
                                }
                                innerDetailsAdapter.notifyDataSetChanged();
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

        return rootView;
    }

    private static long getFileSize(File file) {
        if (file != null && file.isFile()) {
            return file.length();
        }
        return 0;
    }

    @Override
    public void onCheckboxClicked(View view, List<FileDetails> updatedFiles) {
        filesToDelete.clear();

        for (FileDetails details : updatedFiles) {
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

            String size = Formatter.formatShortFileSize(getActivity(), totalFileSize);
            button.setText("Delete Selected Items (" + size + ")");
            button.setTextColor(Color.parseColor("#C103A9F4"));
        } else {
            button.setText("Delete Selected Items (0B)");
            button.setTextColor(Color.parseColor("#A9A9A9"));
        }
    }

    private static class FetchFiles extends AsyncTask<String, Void, Object> {

        private String category;
        private OnFilesFetched onFilesFetched;
        private WeakReference<FilesFragment> filesFragmentWeakReference;

        FetchFiles(FilesFragment filesFragment, String category, OnFilesFetched onFilesFetched) {
            filesFragmentWeakReference = new WeakReference<>(filesFragment);
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
                    for (final File file : results) {
                        //Still verify if the file is an image in whatsapp preferred format(jpg)
                        switch (category) {
                            case DataHolder.IMAGE:
                                if (file.isFile()) {
                                    if (!file.getName().endsWith(".nomedia")) {
                                        FileDetails fileDetails = new FileDetails();
                                        fileDetails.setName(file.getName());
                                        fileDetails.setPath(file.getPath());
                                        fileDetails.setSize(Formatter.formatShortFileSize(filesFragmentWeakReference.get().
                                                        getContext(),
                                                getFileSize(file)));
                                        files.add(fileDetails);
                                    }
                                }
                                break;
                            case DataHolder.DOCUMENT:
                                //Check if it is a file or a folder
                                if (file.isFile()) {
                                    if (file.getName().endsWith(".doc") || file.getName().endsWith(".pdf")
                                            || file.getName().endsWith(".docx") || file.getName().endsWith(".enc") || file.getName().endsWith(".java")) {
                                        FileDetails fileDetails = new FileDetails();
                                        fileDetails.setName(file.getName());
                                        fileDetails.setPath(file.getPath());
                                        fileDetails.setImage(R.drawable.ic_doc);
                                        fileDetails.setColor(R.color.red);
                                        fileDetails.setSize(Formatter.formatShortFileSize(filesFragmentWeakReference.get().getContext(), getFileSize(file)));
                                        files.add(fileDetails);
                                    }
                                }
                                break;
                            case DataHolder.VIDEO:
                                if (file.isFile()) {
                                    if (!file.getName().endsWith(".nomedia")) {
                                        FileDetails fileDetails = new FileDetails();
                                        fileDetails.setName(file.getName());
                                        fileDetails.setPath(file.getPath());
                                        fileDetails.setImage(R.drawable.video_play);
                                        fileDetails.setColor(R.color.transparent);
                                        fileDetails.setSize(Formatter.formatShortFileSize(filesFragmentWeakReference.get()
                                                        .getContext(),
                                                getFileSize(file)));
                                        files.add(fileDetails);
                                    }
                                }
                                break;
                            case DataHolder.AUDIO:
                                if (file.isFile()) {
                                    if (!file.getName().endsWith(".nomedia")) {
                                        FileDetails fileDetails = new FileDetails();
                                        fileDetails.setName(file.getName());
                                        fileDetails.setPath(file.getPath());
                                        fileDetails.setImage(R.drawable.ic_audio);
                                        fileDetails.setColor(R.color.blue);
                                        fileDetails.setSize(Formatter.formatShortFileSize(filesFragmentWeakReference.get()
                                                        .getContext(),
                                                getFileSize(file)));
                                        files.add(fileDetails);
                                    }
                                }
                                break;
                            case DataHolder.GIF:
                                if (file.isFile()) {
                                    if (!file.getName().endsWith(".nomedia")) {
                                        FileDetails fileDetails = new FileDetails();
                                        fileDetails.setName(file.getName());
                                        fileDetails.setPath(file.getPath());
                                        fileDetails.setImage(R.drawable.video_play);
                                        fileDetails.setColor(R.color.transparent);
                                        fileDetails.setSize(Formatter.formatShortFileSize(filesFragmentWeakReference.get().getContext(),
                                                getFileSize(file)));
                                        files.add(fileDetails);
                                    }
                                }
                                break;
                            case DataHolder.WALLPAPER:
                                if (file.isFile()) {
                                    if (!file.getName().endsWith(".nomedia")) {
                                        FileDetails fileDetails = new FileDetails();
                                        fileDetails.setName(file.getName());
                                        fileDetails.setPath(file.getPath());
                                        fileDetails.setSize(Formatter.formatShortFileSize(filesFragmentWeakReference.get().getContext(),
                                                getFileSize(file)));
                                        files.add(fileDetails);
                                    }
                                }
                                break;
                            case DataHolder.VOICE:
                                if (results != null) {
                                        if (file.isDirectory()) {
                                            File[] res = file.listFiles();
                                            Log.e("Files", String.valueOf(res.length));

                                            for (int j = 0; j < res.length; j++) {
                                                if (!res[j].getName().endsWith(".nomedia")) {
                                                    FileDetails fileDetails = new FileDetails();
                                                    fileDetails.setName(res[j].getName());
                                                    fileDetails.setPath(res[j].getPath());
                                                    fileDetails.setImage(R.drawable.voice);
                                                    fileDetails.setColor(R.color.orange);
                                                    fileDetails.setSize(Formatter.formatShortFileSize(filesFragmentWeakReference.get().getContext(),
                                                            getFileSize(res[j])));
                                                    files.add(fileDetails);
                                                }
                                            }
                                        }

                                }
                        }
                    }
                } else {
                    Log.e("Files", "No files found in " + directory.getName());
                }
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


}
