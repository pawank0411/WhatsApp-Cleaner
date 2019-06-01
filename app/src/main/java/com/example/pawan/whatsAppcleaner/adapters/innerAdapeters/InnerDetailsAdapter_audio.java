package com.example.pawan.whatsAppcleaner.adapters.innerAdapeters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.pawan.whatsAppcleaner.datas.FileDetails;
import com.example.pawan.whatsAppcleaner.R;

import java.util.ArrayList;

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
    public InnerDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.doc_content, parent, false);

        return new InnerDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final InnerDataViewHolder innerDataViewHolder, final int positions) {

        final FileDetails details = innerDataList.get(positions);

        innerDataViewHolder.tittle_name.setText(details.getName());
        innerDataViewHolder.data.setText(String.valueOf(details.getSize()));
        innerDataViewHolder.imageView.setCircleBackgroundColor(ContextCompat.getColor(innerDataViewHolder.imageView.getContext(),
                details.getColor()));
        innerDataViewHolder.imageView.setBorderColor(ContextCompat.getColor(innerDataViewHolder.imageView.getContext(),
                details.getColor()));
        innerDataViewHolder.imageView.setImageResource(details.getImage());

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
            imageView = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.recycler_view);
            checkBox = itemView.findViewById(R.id.checkbox);

        }
    }


    public interface OnCheckboxListener{
        void onCheckboxClicked(View view, ArrayList<FileDetails> updatedFiles);
    }
}
