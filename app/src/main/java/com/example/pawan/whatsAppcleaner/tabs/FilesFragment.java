package com.example.pawan.whatsAppcleaner.tabs;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pawan.whatsAppcleaner.DataHolder;
import com.example.pawan.whatsAppcleaner.R;
import com.example.pawan.whatsAppcleaner.adapters.innerAdapeters.InnerDetailsAdapter;
import com.example.pawan.whatsAppcleaner.datas.FileDetails;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FilesFragment extends Fragment implements InnerDetailsAdapter.OnCheckboxListener {

    private RecyclerView recyclerView;
    private Button button;
    private TextView no_files;
    private InnerDetailsAdapter innerDetailsAdapterImage;
    private ArrayList<FileDetails> innerDataList = new ArrayList<>();
    private ArrayList<FileDetails> filesToDelete = new ArrayList<>();
    private final int IMAGES = 1;
    private final int VIDEOS = 2;
    private final int AUDIOS = 3;
    private final int FILE = 4;
    ProgressDialog progressDialog;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

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
            case DataHolder.IMAGE:
                rootView = inflater.inflate(R.layout.image_wallpaper_activity, container, false);
                recyclerView = rootView.findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                innerDetailsAdapterImage = new InnerDetailsAdapter(IMAGES, getActivity(), innerDataList, this);
                break;
            case DataHolder.GIF:
            case DataHolder.VIDEO:
                rootView = inflater.inflate(R.layout.image_wallpaper_activity, container, false);
                recyclerView = rootView.findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                innerDetailsAdapterImage = new InnerDetailsAdapter(VIDEOS, getActivity(), innerDataList, this);
                break;
            case DataHolder.AUDIO:
                rootView = inflater.inflate(R.layout.doc_activity, container, false);
                recyclerView = rootView.findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                innerDetailsAdapterImage = new InnerDetailsAdapter(AUDIOS, getActivity(), innerDataList, this);
            case DataHolder.DOCUMENT:
                rootView = inflater.inflate(R.layout.doc_activity, container, false);
                recyclerView = rootView.findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                innerDetailsAdapterImage = new InnerDetailsAdapter(FILE, getActivity(), innerDataList, this);
        }

        button = rootView.findViewById(R.id.delete);


        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(innerDetailsAdapterImage);

        Log.e("TEST", "" + path);



//        new FetchFiles(category).execute(path);

        if (path != null) {
            File directory = new File(path);
            File[] results = directory.listFiles();
            if (results != null) {
                    switch (category) {
                            case DataHolder.IMAGE:
                                for (int i = 0; i < results.length; i++){
                                    if (results[i].isFile()) {
                                        FileDetails fileDetails = new FileDetails();
                                        fileDetails.setName(results[i].getName());
                                        fileDetails.setPath(results[i].getPath());
                                        fileDetails.setSize(Formatter.formatShortFileSize(getActivity(), getFileSize(results[i])));
                                        innerDataList.add(fileDetails);
                                    }
                                    Log.e("Files", "files found: " + innerDataList.toString());
                                    innerDetailsAdapterImage.notifyDataSetChanged();
                                }
                                break;
                            case DataHolder.DOCUMENT:
                                for (File file : results) {
                                    //Check if it is a file or a folder
                                    if (file.isFile()) {
                                        //Still verify if the file is an image in whatsapp preferred format(jpg)
                                        if (file.getName().endsWith(".doc") || file.getName().endsWith(".pdf")
                                                || file.getName().endsWith(".docx") || file.getName().endsWith(".enc") || file.getName().endsWith(".java")) {
                                            FileDetails fileDetails = new FileDetails();
                                            fileDetails.setName(file.getName());
                                            fileDetails.setPath(file.getPath());
                                            fileDetails.setImage(R.drawable.ic_doc);
                                            fileDetails.setColor(R.color.red);
                                            fileDetails.setSize(Formatter.formatShortFileSize(getActivity(), getFileSize(file)));
                                            innerDataList.add(fileDetails);
                                        }
                                    }else {
                                        Log.e("Files", "No files found in " + directory.getName());
                                    }
                                    Log.e("Files", "files found: " + innerDataList.toString());
                                    innerDetailsAdapterImage.notifyDataSetChanged();
                                }
                                break;
                            case DataHolder.VIDEO:
                                for (int i = 0; i < results.length; i++){

                                    if (results[i].isFile()) {
                                        FileDetails fileDetails = new FileDetails();
                                        fileDetails.setName(results[i].getName());
                                        fileDetails.setPath(results[i].getPath());
                                        fileDetails.setSize(Formatter.formatShortFileSize(getActivity(), getFileSize(results[i])));
                                        innerDataList.add(fileDetails);
                                    }
                                    Log.e("Files", "files found: " + innerDataList.toString());
                                    innerDetailsAdapterImage.notifyDataSetChanged();
                                }
                                break;
                            case DataHolder.AUDIO:
                                for (int i = 0; i < results.length; i++){

                                    if (results[i].isFile()) {
                                        FileDetails fileDetails = new FileDetails();
                                        fileDetails.setName(results[i].getName());
                                        fileDetails.setPath(results[i].getPath());
                                        fileDetails.setImage(R.drawable.ic_audio);
                                        fileDetails.setColor(R.color.blue);
                                        fileDetails.setSize(Formatter.formatShortFileSize(getActivity(), getFileSize(results[i])));
                                        innerDataList.add(fileDetails);
                                    }
                                    Log.e("Files", "files found: " + innerDataList.toString());
                                    innerDetailsAdapterImage.notifyDataSetChanged();
                                }
                                break;
                            case DataHolder.GIF:
                                for (int i = 0; i < results.length; i++){
                                    if (results[i].isFile()) {
                                        FileDetails fileDetails = new FileDetails();
                                        fileDetails.setName(results[i].getName());
                                        fileDetails.setPath(results[i].getPath());
                                        fileDetails.setSize(Formatter.formatShortFileSize(getActivity(), getFileSize(results[i])));
                                        innerDataList.add(fileDetails);
                                    }
                                    Log.e("Files", "files found: " + innerDataList.toString());
                                    innerDetailsAdapterImage.notifyDataSetChanged();
                                }
                                break;
                        }

                    } else {
                Log.e("Files", "No files found in " + directory.getName());
            }
        }
        else {
                Log.e("Files", "Path is empty");
        }




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                innerDetailsAdapterImage.notifyDataSetChanged();
                if (success == 0) {
                    Toast.makeText(getContext(), "Couldn't delete some files", Toast.LENGTH_SHORT).show();
                } else if (success == 1) {
                    Toast.makeText(getContext(), "Deleted successfully", Toast.LENGTH_SHORT).show();
                }
                button.setText("Delete Selected Items (0B)");
                button.setTextColor(Color.parseColor("#A9A9A9"));

            }
        });

        return rootView;
    }

    private long getFileSize(File file) {
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

//    private class FetchFiles extends AsyncTask<String, Void, Object>{
//
//        private String category;
//
//        public FetchFiles (String category){
//            this.category = category;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // display a progress dialog for good user experiance
////         progressDialog = new ProgressDialog(FilesFragment.this);
////            progressDialog.setMessage("Please Wait");
////            progressDialog.setCancelable(false);
////            progressDialog.show();
//        }
//        @Override
//        protected Object doInBackground(String... strings) {
//
//            String path = strings[0];
//
//            ArrayList<FileDetails> files = new ArrayList<>();
//
//            if (path != null) {
//                File directory = new File(path);
//                File[] results = directory.listFiles();
//                if (results != null) {
//                    for (File file : results) {
//                        //Check if it is a file or a folder
//                        if (file.isFile()) {
//                            //Still verify if the file is an image in whatsapp preferred format(jpg)
//                            switch (category) {
//                                case DataHolder.IMAGE:
//                                    if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
//                                        FileDetails fileDetails = new FileDetails();
//                                        fileDetails.setName(file.getName());
//                                        fileDetails.setPath(file.getPath());
//                                        fileDetails.setSize(Formatter.formatShortFileSize(getActivity(), getFileSize(file)));
//                                        files.add(fileDetails);
//                                    }
//                                    break;
//                                case DataHolder.DOCUMENT:
//                                    if (file.getName().endsWith(".doc") || file.getName().endsWith(".pdf")
//                                            || file.getName().endsWith(".docx") || file.getName().endsWith(".enc") || file.getName().endsWith(".java")) {
//                                        FileDetails fileDetails = new FileDetails();
//                                        fileDetails.setName(file.getName());
//                                        fileDetails.setPath(file.getPath());
//                                        fileDetails.setImage(R.drawable.ic_doc);
//                                        fileDetails.setColor(R.color.red);
//                                        fileDetails.setSize(Formatter.formatShortFileSize(getActivity(), getFileSize(file)));
//                                        files.add(fileDetails);
//                                    }
//                                    break;
//                                case DataHolder.VIDEO:
//                                    if (file.getName().endsWith(".mp4")) {
//                                        FileDetails fileDetails = new FileDetails();
//                                        fileDetails.setName(file.getName());
//                                        fileDetails.setPath(file.getPath());
//                                        fileDetails.setSize(Formatter.formatShortFileSize(getActivity(), getFileSize(file)));
//                                        files.add(fileDetails);
//                                    }
//                                    break;
//                                case DataHolder.AUDIO:
//                                    if (file.getName().endsWith(".mp3") || file.getName().endsWith(".wav")) {
//                                        FileDetails fileDetails = new FileDetails();
//                                        fileDetails.setName(file.getName());
//                                        fileDetails.setPath(file.getPath());
//                                        fileDetails.setImage(R.drawable.ic_audio);
//                                        fileDetails.setColor(R.color.blue);
//                                        fileDetails.setSize(Formatter.formatShortFileSize(getActivity(), getFileSize(file)));
//                                        files.add(fileDetails);
//                                    }
//                                    break;
//                                case DataHolder.GIF:
//                                    if (file.getName().endsWith(".mp4")) {
//                                        FileDetails fileDetails = new FileDetails();
//                                        fileDetails.setName(file.getName());
//                                        fileDetails.setPath(file.getPath());
//                                        fileDetails.setSize(Formatter.formatShortFileSize(getActivity(), getFileSize(file)));
//                                        files.add(fileDetails);
//                                    }
//                                    break;
//                            }
//
//                        } else {
//                            //For now we skip it
//                            //getFileSize(file);
//                        }
//                    }
//                } else {
//                    Log.e("Files", "No files found in " + directory.getName());
//                }
//            } else {
//                Log.e("Files", "Path is empty");
//            }
//
//
//            return files;
//        }
//
//        @Override
//        protected void onPostExecute(Object o) {
//
//            List<FileDetails> files;
//            files = (List<FileDetails>) o;
//        }
//
//
//    }
}
