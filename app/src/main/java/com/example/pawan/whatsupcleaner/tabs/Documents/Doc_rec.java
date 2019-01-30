package com.example.pawan.whatsupcleaner.tabs.Documents;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.pawan.whatsupcleaner.adapters.innerAdapeters.InnerDetailsAdapter_doc;
import com.example.pawan.whatsupcleaner.datas.FileDetails;
import com.example.pawan.whatsupcleaner.R;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;


public class Doc_rec extends AppCompatActivity implements InnerDetailsAdapter_doc.OnCheckboxlistener {

    //List<Details>  innerdatalist;
    RecyclerView recyclerView;
    Button button;
    ArrayList<FileDetails> innerdatalist = new ArrayList<>();
    boolean checkClick=false;
    double sizeChecked=0;
    InnerDetailsAdapter_doc innerDetailsAdapterDoc;
    private static final long GiB = 1024 * 1024 * 1024;
    private static final long MiB = 1024 * 1024;
    private static final long KiB = 1024;
    private double len;
    private String byteMake;

    String path;
    int position = 0;

    //    private String[] Path = new String[149];
//    private int [] Pos=new int[149];
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_activity);

        recyclerView = findViewById(R.id.recycle1);
        button = findViewById(R.id.delete);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              checkClick=true;
              sizeChecked=0;
//          File a = new File(path);
//          if (a.exists()) {
////              for (int i = 0; i < innerDetailsAdapterDoc.getItemCount(); i++){
////                  if (innerdatalist.get(i).isclicked) {
////                      Toast.makeText(Documents.this, Path[i], Toast.LENGTH_SHORT).show();
////                      //innerdatalist.get(i).isclicked = false;
////                  }
//
//
//              if (a.delete()) {
//                  Toast.makeText(Documents.this, "Deleted", Toast.LENGTH_SHORT).show();
//                  innerdatalist.remove(position);
//                  innerDetailsAdapterDoc.notifyItemChanged(position);
//                  innerDetailsAdapterDoc.notifyItemRangeChanged(position, innerdatalist.size());
//
//              } else {
//                  Toast.makeText(Documents.this, "file not Deleted :", Toast.LENGTH_SHORT).show();
//              }
//          }
            }
        });


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String path = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/WhatsApp Documents";

        File directory = new File(path);
        ArrayList<FileDetails> fileList2 = new ArrayList<>();
        File[] results = directory.listFiles();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //Need to ask permission again or close the app
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //Need to ask permission again or close the app
            } else {

                if (results != null) {
                    for (File file : results) {
                        //Check if it is a file or a folder
                        if (file.isDirectory()) {
                            //For now we skip it
                        } else {
                            //Still verify if the file is an image in whatsapp preferred format(jpg)
                            if (file.getName().endsWith(".pdf") || file.getName().endsWith(".doc")) {
                                FileDetails fileDetails = new FileDetails();
                                fileDetails.setName(file.getName());
                                fileDetails.setPath(file.getPath());
                                fileDetails.setSize("" + getFileSize(file));
                                fileList2.add(fileDetails);
                            }
                        }
                    }
                    Log.e("Files", "files found: " + fileList2.toString());
                    innerdatalist = fileList2;
                } else {
                    Log.e("Files", "No files found in " + directory.getName());
                }
            }

        }


        innerDetailsAdapterDoc = new InnerDetailsAdapter_doc(this, innerdatalist, this);
        recyclerView.setAdapter(innerDetailsAdapterDoc);
    }

    private String getFileSize(File file) {
        NumberFormat format = new DecimalFormat("#.##");
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
        final double length = file.length();

        if (file.isFile()) {
            if (length > GiB) {
                len=length/GiB;
                byteMake="GB";
                return format.format(length / GiB) + " GB";
            } else if (length > MiB) {
                len=length/MiB;
                 byteMake="MB";
                return format.format(length / MiB) + " MB";
            } else if (length > KiB) {
                len=length/KiB;
                byteMake="KB";
                return format.format(length / KiB) + " KB";
            }

            return format.format(length) + " B";
        } else {
                len=0;
        }
        return "";
    }


    @Override
    public void onCheckboxClicked(View view, int pos) {
        boolean checkedBox[]=new boolean[innerdatalist.size()];
        boolean checked = ((CheckBox) view).isChecked();
        checkedBox[pos]=true;
        // CheckBox checkBox = (CheckBox)view;
        String  size;
        final FileDetails details = innerdatalist.get(pos);

        // FIXME: 1/26/19

        if (checked){
                File file = new File(details.getPath());
                size = getFileSize(file);
                sizeChecked = sizeChecked + len;
                Log.e("Deleted Amount",Double.toString(sizeChecked));
                sizeChecked=Math.floor(sizeChecked*100)/100;
                button.setText("Delete Selected Items" + " (" +sizeChecked+byteMake+ ")");
                button.setTextColor(Color.parseColor("#C103A9F4"));
         }
        else{
            button.setText("Delete Selected Items (0B)");
            button.setTextColor(Color.parseColor("#A9A9A9"));

        }



    }


}