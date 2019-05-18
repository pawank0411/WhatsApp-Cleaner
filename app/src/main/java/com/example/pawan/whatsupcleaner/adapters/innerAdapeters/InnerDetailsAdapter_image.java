package com.example.pawan.whatsupcleaner.adapters.innerAdapeters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pawan.whatsupcleaner.datas.FileDetails;
import com.example.pawan.whatsupcleaner.R;

import java.io.File;
import java.util.ArrayList;


public class InnerDetailsAdapter_image extends RecyclerView.Adapter<InnerDetailsAdapter_image.InnerDataViewHolder> {

    private Context ctx;
    ArrayList<FileDetails> innerDataList;
    private OnCheckboxlistener onCheckboxlistener;

    private static final int PICKFILE_RESULT_CODE = 8778;

    public InnerDetailsAdapter_image(Context ctx, ArrayList<FileDetails> innerDataList, OnCheckboxlistener onCheckboxlistener){
        this.ctx = ctx;
        this.innerDataList = innerDataList;
        this.onCheckboxlistener = onCheckboxlistener;
    }

    @Override
    public InnerDetailsAdapter_image.InnerDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.image_wallpaper_content, parent, false);

        return new InnerDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final InnerDetailsAdapter_image.InnerDataViewHolder innerDataViewHolder, int positions) {

        final FileDetails details = innerDataList.get(positions);

        Glide.with(ctx).load(details.getPath()).into(innerDataViewHolder.imageView );

        //Log.e("size ", "Size" + details.getSize());

        final int pos = positions;

        // FIXME: 1/26/19

        innerDataViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                details.setSelected(isChecked);

                if (onCheckboxlistener != null) {
                    onCheckboxlistener.onCheckboxClicked(buttonView,
                            innerDataViewHolder.getAdapterPosition());
                }


            }
        });

        if (details.isclicked) {
            innerDataViewHolder.checkBox.setChecked(true);
        } else {
            innerDataViewHolder.checkBox.setChecked(false);
        }

        innerDataViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                File a = new File (String.valueOf(Uri.parse(details.getPath())));
                Uri file = FileProvider.getUriForFile(ctx, ctx.getApplicationContext().getPackageName() +
                        ".my.package.name.provider",a);
                intent.setDataAndType(file, "image/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                ctx.startActivity(intent);

            }
        });


    }



    @Override
    public int getItemCount() {
        return innerDataList.size();
    }


    public class InnerDataViewHolder extends RecyclerView.ViewHolder {

        TextView image_name;
        ImageView imageView;
        CardView cardView;
        CheckBox checkBox;

        public InnerDataViewHolder(View itemView) {
            super(itemView);

//            image_name = itemView.findViewById(R.id.img_name);
            imageView  = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.card_view1);
            checkBox = itemView.findViewById(R.id.checkbox);

        }
    }
    public interface OnCheckboxlistener{
        void onCheckboxClicked(View view, int pos);
    }
}
