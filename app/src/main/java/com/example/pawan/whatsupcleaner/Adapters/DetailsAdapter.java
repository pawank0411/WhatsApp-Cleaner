package com.example.pawan.whatsupcleaner.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pawan.whatsupcleaner.Datas.Details;
import com.example.pawan.whatsupcleaner.InnnerData.InnerData;
import com.example.pawan.whatsupcleaner.R;

import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailsViewHolder> {
    private Context ctx;

    private List<Details> datalist;

    public DetailsAdapter(Context ctx,List<Details> datalist){
        this.ctx = ctx;
        this.datalist = datalist;
    }

    @Override
    public DetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.content2, null);
        return new DetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsAdapter.DetailsViewHolder detailsViewHolder,final int positions) {

        Details details =  datalist.get(positions);
        detailsViewHolder.title.setText(details.getTitle());
//        detailsViewHolder.data.setText(details.getData());

        detailsViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent;
                switch (positions){
                    case 0:
                        //differnt activites for differnt cards
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                                + "/WhatsApp/Media/WhatsApp Images");
                        intent.setDataAndType(uri, "image/jpg");
                        ctx.startActivity(Intent.createChooser(intent, "Open folder"));
                        Toast.makeText(ctx, "Activity 1 for images", Toast.LENGTH_SHORT).show();


                        /*Need help*/

                        // grabed  images from whatsup media folder . /**show it in recycler view**/
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class DetailsViewHolder extends RecyclerView.ViewHolder {

        TextView title,data;
        CardView cardView;


        public DetailsViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
           data = itemView.findViewById(R.id.data);
           cardView = itemView.findViewById(R.id.card_view1);
           }
    }


}
