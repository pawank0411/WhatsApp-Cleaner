package com.pawan.whatsAppCleaner.adapters.innerAdapeters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pawan.whatsAppCleaner.R;
import com.pawan.whatsAppCleaner.datas.FileDetails;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class InnerDetailsAdapter_image extends RecyclerView.Adapter<InnerDetailsAdapter_image.InnerDataViewHolder> {

    private Context ctx;
    private ArrayList<FileDetails> innerDataList;
    private OnCheckboxListener onCheckboxlistener;


    public InnerDetailsAdapter_image(Context ctx, ArrayList<FileDetails> innerDataList, OnCheckboxListener onCheckboxlistener) {
        this.ctx = ctx;
        this.innerDataList = innerDataList;
        this.onCheckboxlistener = onCheckboxlistener;
    }

    @NonNull
    @Override
    public InnerDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.status, parent, false);

        return new InnerDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final InnerDataViewHolder innerDataViewHolder, int positions) {

        final FileDetails details = innerDataList.get(positions);

        Glide.with(ctx)
                .load(details.getPath())
                .transition(withCrossFade())
                .thumbnail(
                        Glide.with(ctx)
                                .load(R.drawable.img))
                .into(innerDataViewHolder.imageView);

        innerDataViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (innerDataViewHolder.checkBox.isChecked()) {
                    innerDataViewHolder.checkBox.setChecked(false);
                    innerDataList.get(innerDataViewHolder.getAdapterPosition()).setSelected(false);
                } else {
                    innerDataViewHolder.checkBox.setChecked(true);
                    innerDataList.get(innerDataViewHolder.getAdapterPosition()).setSelected(true);
                }

                if (onCheckboxlistener != null) {
                    onCheckboxlistener.onCheckboxClicked(v, innerDataList);
                }
            }
        });
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

        innerDataViewHolder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                File a = new File(String.valueOf(Uri.parse(details.getPath())));

                Uri uri = FileProvider.getUriForFile(ctx, ctx.getApplicationContext().getPackageName() +
                        ".my.package.name.provider", a);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                String mime = "*/*";
                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                if (mimeTypeMap.hasExtension(
                        MimeTypeMap.getFileExtensionFromUrl(uri.toString()))) {

                    mime = mimeTypeMap.getMimeTypeFromExtension(
                            MimeTypeMap.getFileExtensionFromUrl(uri.toString()));

                }
                try {
                    Log.e("Mime", mime);
                    intent.setDataAndType(uri, mime);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    ctx.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(ctx, "Couldn't find app that open this file ", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return innerDataList.size();
    }


    public class InnerDataViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        CardView cardView;
        CheckBox checkBox;

        public InnerDataViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.recycler_view);
            checkBox = itemView.findViewById(R.id.checkbox);

        }
    }

    public interface OnCheckboxListener {
        void onCheckboxClicked(View view, List<FileDetails> updatedFiles);
    }
}
