package com.example.pawan.whatsupcleaner.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pawan.whatsupcleaner.Datas.Details;
import com.example.pawan.whatsupcleaner.R;

import java.util.List;
import java.util.zip.Inflater;

public class InnerDetailsAdapter extends RecyclerView.Adapter<InnerDetailsAdapter.InnerDataViewHolder> {

    private Context ctx;
    List<Details> innerDataList;

    public InnerDetailsAdapter (Context ctx,List<Details> innerDataList){
        this.ctx = ctx;
        this.innerDataList = innerDataList;
    }

    @Override
    public InnerDetailsAdapter.InnerDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.inner_content, parent, false);

        return new InnerDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerDetailsAdapter.InnerDataViewHolder innerDataViewHolder, int positions) {

        Details details = innerDataList.get(positions);

        innerDataViewHolder.image_name.setText(details.getTitle());
        innerDataViewHolder.imageView.setImageResource(details.getImage());
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class InnerDataViewHolder extends RecyclerView.ViewHolder {

        TextView image_name;
        ImageView imageView;

        public InnerDataViewHolder(View itemView) {
            super(itemView);

            image_name = itemView.findViewById(R.id.img_name);
            imageView  = itemView.findViewById(R.id.image);
        }
    }
}
