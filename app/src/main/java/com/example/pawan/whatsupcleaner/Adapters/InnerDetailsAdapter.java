package com.example.pawan.whatsupcleaner.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @NonNull
    @Override
    public InnerDetailsAdapter.InnerDataViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View view = layoutInflater.inflate(R.layout.inner_content, null);

        return new InnerDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerDetailsAdapter.InnerDataViewHolder innerDataViewHolder, int positions) {

        Details details = innerDataList.get(positions);

        innerDataViewHolder.image_name.setText(details.getTitle());
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class InnerDataViewHolder extends RecyclerView.ViewHolder {

        TextView image_name;

        public InnerDataViewHolder(View itemView) {
            super(itemView);

            image_name = itemView.findViewById(R.id.img_name);
        }
    }
}
