package com.example.pawan.whatsAppcleaner.tabs.Gifs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pawan.whatsAppcleaner.R;
import com.example.pawan.whatsAppcleaner.adapters.innerAdapeters.InnerDetailsAdapter_doc;
import com.example.pawan.whatsAppcleaner.adapters.innerAdapeters.InnerDetailsAdapter_image;
import com.example.pawan.whatsAppcleaner.datas.FileDetails;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


public class Gifs_sent extends AppCompatActivity implements InnerDetailsAdapter_image.OnCheckboxListener {

    //List<Details>  innerdatalist;
    RecyclerView recyclerView;
    Button button;
    InnerDetailsAdapter_image innerDetailsAdapterImage;
    ArrayList<FileDetails> innerdatalist = new ArrayList<>();

    boolean checkClick = false;
    double sizeChecked = 0;
    InnerDetailsAdapter_doc innerDetailsAdapterDoc;
    private static final long GiB = 1024 * 1024 * 1024;
    private static final long MiB = 1024 * 1024;
    private static final long KiB = 1024;
    private double len;
    private String byteMake;

    String path;
    int position = 0;
    private ArrayList<FileDetails> filesToDelete = new ArrayList<>();

    //    private String[] Path = new String[149];
//    private int [] Pos=new int[149];
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_wallpaper_activity);

        recyclerView = findViewById(R.id.card_view1);
        button = findViewById(R.id.delete);


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
                    innerdatalist.remove(deletedFile);
                }
                innerDetailsAdapterImage.notifyDataSetChanged();
                if (success == 0) {
                    Toast.makeText(Gifs_sent.this, "Couldn't delete some files", Toast.LENGTH_SHORT).show();
                } else if (success == 1){
                    Toast.makeText(Gifs_sent.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                }
                button.setText("Delete Selected Items (0B)");
                button.setTextColor(Color.parseColor("#A9A9A9"));

            }
        });


        Intent intent = getIntent();
        String b = intent.getStringExtra("Flag");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //Need to ask permission again or close the app
        } else {
            String path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Animated Gifs/Sent";


            File directory = new File(path);
            ArrayList<FileDetails> fileList1 = new ArrayList<>();
            File[] results = directory.listFiles();
            if (results != null) {
                for (File file : results) {
                    //Check if it is a file or a folder
                    if (file.isDirectory()) {
                        //For now we skip it
                        //getFileSize(file);
                    } else {
                        //Still verify if the file is an image in whatsapp preferred format(jpg)
                        if (file.getName().endsWith(".mp4")) {
                            FileDetails fileDetails = new FileDetails();
                            fileDetails.setName(file.getName());
                            fileDetails.setPath(file.getPath());
                            fileDetails.setSize("" + getFileSize(file));
                            fileList1.add(fileDetails);
                        }
                    }
                }
                Log.e("Files", "files found: " + fileList1.toString());
                innerdatalist = fileList1;
            } else {
                Log.e("Files", "No files found in " + directory.getName());
            }
        }
//        }
//        else {
//            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
//        }

        innerDetailsAdapterImage = new InnerDetailsAdapter_image(this, innerdatalist, this);
        recyclerView.setAdapter(innerDetailsAdapterImage);
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
            }

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

            String size = Formatter.formatShortFileSize(Gifs_sent.this, totalFileSize);
            button.setText("Delete Selected Items (" + size + ")");
            button.setTextColor(Color.parseColor("#C103A9F4"));
        } else {
            button.setText("Delete Selected Items (0B)");
            button.setTextColor(Color.parseColor("#A9A9A9"));
        }



    }

    private double sizeCal(double sizeChecked) {
        if (sizeChecked > GiB) {
            sizeChecked = sizeChecked / GiB;
            byteMake = "GB";
        } else if (sizeChecked > MiB) {
            sizeChecked = sizeChecked / MiB;
            byteMake = "MB";
        } else if (sizeChecked > KiB) {
            sizeChecked = sizeChecked / KiB;
            byteMake = "KB";
        }
        return sizeChecked;
    }
}