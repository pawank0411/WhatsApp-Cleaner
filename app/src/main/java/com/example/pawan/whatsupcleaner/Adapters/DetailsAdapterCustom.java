package com.example.pawan.whatsupcleaner.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pawan.whatsupcleaner.Datas.Details;
import com.example.pawan.whatsupcleaner.InnnerData.InnerData;
import com.example.pawan.whatsupcleaner.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailsAdapterCustom extends RecyclerView.Adapter<DetailsAdapterCustom.DetailsCustomViewHolder> {
    private Context ctx;

    private List<Details> datalist;

    public DetailsAdapterCustom(Context ctx, List<Details> datalist) {
        this.ctx = ctx;
        this.datalist = datalist;
    }

    @Override
    public DetailsCustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.content, null);
        return new DetailsCustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsAdapterCustom.DetailsCustomViewHolder detailsCustomViewHolder, int positions) {

        Details details = datalist.get(positions);

        detailsCustomViewHolder.title.setText(details.getTitle());
        detailsCustomViewHolder.data.setText(String.valueOf(details.getData() + "MB"));

        detailsCustomViewHolder.image.setCircleBackgroundColor(ContextCompat.getColor(detailsCustomViewHolder.image.getContext(), details.getColor()));
        detailsCustomViewHolder.image.setBorderColor(ContextCompat.getColor(detailsCustomViewHolder.image.getContext(), details.getColor()));
        detailsCustomViewHolder.image.setImageResource(details.getImage());

        final int pos = positions;
        //detailsCustomViewHolder.data.setText(details.getData());
    }


    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class DetailsCustomViewHolder extends RecyclerView.ViewHolder {

        TextView title, data;
        CircleImageView image;
        CardView cardView;

        public DetailsCustomViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            data = itemView.findViewById(R.id.data);
            image = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.card_view1);


        }
    }
}



