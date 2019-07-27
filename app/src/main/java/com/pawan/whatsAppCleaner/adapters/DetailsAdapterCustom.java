package com.pawan.whatsAppCleaner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.pawan.whatsAppCleaner.R;
import com.pawan.whatsAppCleaner.datas.Details;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailsAdapterCustom extends RecyclerView.Adapter<DetailsAdapterCustom.DetailsViewHolder> {
    private Context ctx;

    private List<Details> datalist;

    private OnItemClickListener listener;

    public DetailsAdapterCustom(Context ctx, List<Details> datalist, OnItemClickListener listener) {
        this.ctx = ctx;
        this.datalist = datalist;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.main_content2, parent, false);
        return new DetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsAdapterCustom.DetailsViewHolder detailsViewHolder, int positions) {

        Details details = datalist.get(positions);
        detailsViewHolder.title.setText(details.getTitle());
        detailsViewHolder.data.setText(String.valueOf(details.getData()));
        detailsViewHolder.image.setCircleBackgroundColor(ContextCompat.getColor(detailsViewHolder.image.getContext(), details.getColor()));
        detailsViewHolder.image.setBorderColor(ContextCompat.getColor(detailsViewHolder.image.getContext(), details.getColor()));
        detailsViewHolder.image.setImageResource(details.getImage());

        final int pos = positions;
//        detailsViewHolder.data.setText(details.getData());

        detailsViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent;
                switch (pos) {


                    case 0:
                        if (listener != null)
                            listener.onAudiosClicked();
                        break;

                    case 1:
                        if (listener != null)
                            listener.onVoiceClicked();
                        break;
                    case 2:
                        if (listener != null)
                            listener.onWallpapersClicked();
                        break;
                    case 3:
                        if (listener != null)
                            listener.onGifsClicked();
                        break;
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class DetailsViewHolder extends RecyclerView.ViewHolder {

        TextView title, data;
        CircleImageView image;
        CardView cardView;


        public DetailsViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            data = itemView.findViewById(R.id.data);
            image = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.recycler_view);
        }
    }

    // TODO: 1/13/19 So we create an interface that calls main Activity when the user clicks on any item
    public interface OnItemClickListener {

        void onAudiosClicked();

        void onGifsClicked();

        void onWallpapersClicked();

        void onVoiceClicked();

    }

}
