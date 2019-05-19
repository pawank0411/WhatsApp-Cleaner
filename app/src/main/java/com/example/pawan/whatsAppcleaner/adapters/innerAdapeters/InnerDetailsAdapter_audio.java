package com.example.pawan.whatsAppcleaner.adapters.innerAdapeters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.pawan.whatsAppcleaner.datas.FileDetails;
import com.example.pawan.whatsAppcleaner.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class InnerDetailsAdapter_audio extends RecyclerView.Adapter<InnerDetailsAdapter_audio.InnerDataViewHolder> {

    private Context ctx;
    ArrayList<FileDetails> innerDataList;

    private  OnCheckboxListener onCheckboxlistener;


    public InnerDetailsAdapter_audio(Context ctx, ArrayList<FileDetails> innerDataList , OnCheckboxListener onCheckboxlistener){
        this.ctx = ctx;
        this.innerDataList = innerDataList;
        this.onCheckboxlistener = onCheckboxlistener;
    }

    @Override
    public InnerDetailsAdapter_audio.InnerDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.doc_content, parent, false);

        return new InnerDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final InnerDetailsAdapter_audio.InnerDataViewHolder innerDataViewHolder, int positions) {

        final FileDetails details = innerDataList.get(positions);
        innerDataViewHolder.tittle_name.setText(details.getName());
        innerDataViewHolder.data.setText(String.valueOf(details.getSize()));
        innerDataViewHolder.data.setText(String.valueOf(details.getSize()));
        innerDataViewHolder.imageView.setCircleBackgroundColor(ContextCompat.getColor(innerDataViewHolder.imageView.getContext(),
                FileDetails.getColor()));
        innerDataViewHolder.imageView.setBorderColor(ContextCompat.getColor(innerDataViewHolder.imageView.getContext(),
                FileDetails.getColor()));
        innerDataViewHolder.imageView.setImageResource(details.getImage());

        final int pos = positions;


        innerDataViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                innerDataList.get(innerDataViewHolder.getAdapterPosition()).setSelected(isChecked);

                if (onCheckboxlistener != null) {
                    onCheckboxlistener.onCheckboxClicked(buttonView, innerDataList);
                }


            }
        });


        if (details.isSelected()) {
            innerDataViewHolder.checkBox.setChecked(true);
        } else {
            innerDataViewHolder.checkBox.setChecked(false);
        }

        innerDataViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File a = new File(details.getPath());
                Uri file = FileProvider.getUriForFile(ctx, ctx.getApplicationContext().getPackageName() +
                        ".my.package.name.provider",a);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(file, "audio/*");
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

        TextView tittle_name, data;
        CardView cardView;
        CheckBox checkBox;
        CircleImageView imageView;
        public InnerDataViewHolder(View itemView) {
            super(itemView);

            tittle_name = itemView.findViewById(R.id.title);
            data = itemView.findViewById(R.id.data);
            cardView = itemView.findViewById(R.id.card_view1);
            checkBox = itemView.findViewById(R.id.checkbox);
            imageView = itemView.findViewById(R.id.image);
        }
    }


    public interface OnCheckboxListener {
        void onCheckboxClicked(View view, List<FileDetails> updatedFiles);
    }
}
