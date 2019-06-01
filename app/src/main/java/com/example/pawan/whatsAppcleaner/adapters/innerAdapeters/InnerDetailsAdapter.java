package com.example.pawan.whatsAppcleaner.adapters.innerAdapeters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pawan.whatsAppcleaner.datas.FileDetails;
import com.example.pawan.whatsAppcleaner.R;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class InnerDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context ctx;
    ArrayList<FileDetails> innerDataList;
    private OnCheckboxListener onCheckboxlistener;
    private int type;
    private final int IMAGES = 1;
    private final int VIDEOS = 2;
    private final int AUDIOS = 3;
    private final int FILE = 4;
    private final int VOICE = 6;

    public InnerDetailsAdapter(int type, Context ctx, ArrayList<FileDetails> innerDataList, OnCheckboxListener onCheckboxlistener){
        this.type = type;
        this.ctx = ctx;
        this.innerDataList = innerDataList;
        this.onCheckboxlistener = onCheckboxlistener;
    }

    @Override
    public int getItemViewType(int position) {
        switch (type) {
            default:
            case IMAGES:
                return IMAGES;
            case VIDEOS:
                return VIDEOS;
            case AUDIOS:
                return AUDIOS;
            case FILE:
                return FILE;
            case VOICE:
                return VOICE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == IMAGES) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.image_content, parent, false);
            return new InnerDataViewHolderMultimedia(view);
        } else if (viewType == VIDEOS) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.videos_content, parent, false);
            return new InnerDataViewHolderVideos(view);
        } else if (viewType == VOICE) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.doc_content, parent, false);
            return new InnerDataViewHolderDoc(view);
        } else {
            View view = LayoutInflater.from(ctx).inflate(R.layout.doc_content, parent, false);
            return new InnerDataViewHolderDoc(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int positions) {

        if (getItemViewType(positions) == IMAGES) {
            final InnerDataViewHolderMultimedia innerDataViewHolderMultimedia = (InnerDataViewHolderMultimedia) viewHolder;

            final FileDetails details = innerDataList.get(positions);

            Glide.with(ctx)
                    .load(details.getPath())
                    .transition(withCrossFade())
                    .thumbnail(
                            Glide.with(ctx)
                                    .load(R.drawable.img))
                    .into(innerDataViewHolderMultimedia.imageView);


            innerDataViewHolderMultimedia.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    innerDataList.get(innerDataViewHolderMultimedia.getAdapterPosition()).setSelected(isChecked);

                    if (onCheckboxlistener != null) {
                        onCheckboxlistener.onCheckboxClicked(buttonView, innerDataList);
                    }


                }
            });

            if (details.isSelected()) {
                innerDataViewHolderMultimedia.checkBox.setChecked(true);
            } else {
                innerDataViewHolderMultimedia.checkBox.setChecked(false);
            }

            innerDataViewHolderMultimedia.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    File a = new File(String.valueOf(Uri.parse(details.getPath())));
                    Uri file = FileProvider.getUriForFile(ctx, ctx.getApplicationContext().getPackageName() +
                            ".my.package.name.provider", a);
                    intent.setDataAndType(file, "image/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    ctx.startActivity(intent);

                }
            });
        }
        else if (getItemViewType(positions) == VIDEOS) {
            final InnerDataViewHolderVideos innerDataViewHolderVideos = (InnerDataViewHolderVideos) viewHolder;

            final FileDetails details = innerDataList.get(positions);

            innerDataViewHolderVideos.circleImageView.setCircleBackgroundColor(ContextCompat.getColor(innerDataViewHolderVideos.circleImageView.getContext(),
                    details.getColor()));
            innerDataViewHolderVideos.circleImageView.setBorderColor(ContextCompat.getColor(innerDataViewHolderVideos.circleImageView.getContext(),
                    details.getColor()));
            innerDataViewHolderVideos.circleImageView.setImageResource(details.getImage());

            Glide.with(ctx)
                    .load(details.getPath())
                    .transition(withCrossFade())
                    .thumbnail(
                            Glide.with(ctx)
                                    .load(R.drawable.video))
                    .into(innerDataViewHolderVideos.imageView);

            innerDataViewHolderVideos.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    innerDataList.get(innerDataViewHolderVideos.getAdapterPosition()).setSelected(isChecked);

                    if (onCheckboxlistener != null) {
                        onCheckboxlistener.onCheckboxClicked(buttonView, innerDataList);
                    }

                }
            });

            if (details.isSelected()) {
                innerDataViewHolderVideos.checkBox.setChecked(true);
            } else {
                innerDataViewHolderVideos.checkBox.setChecked(false);
            }

            innerDataViewHolderVideos.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    File a = new File(String.valueOf(Uri.parse(details.getPath())));
                    Uri file = FileProvider.getUriForFile(ctx, ctx.getApplicationContext().getPackageName() +
                            ".my.package.name.provider", a);
                    intent.setDataAndType(file, "video/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    ctx.startActivity(intent);

                }
            });
        }
        else if (getItemViewType(positions) == AUDIOS) {
            final InnerDataViewHolderDoc innerDataViewHolder = (InnerDataViewHolderDoc) viewHolder;
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


            innerDataViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    File a = new File (String.valueOf(Uri.parse(details.getPath())));
                    Uri file = FileProvider.getUriForFile(ctx, ctx.getApplicationContext().getPackageName() +
                            ".my.package.name.provider",a);
                    intent.setDataAndType(file, "audio/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    ctx.startActivity(intent);

                }
            });
        }
        else if (getItemViewType(positions) == VOICE) {
            final InnerDataViewHolderDoc innerDataViewHolder = (InnerDataViewHolderDoc) viewHolder;
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
        else if (getItemViewType(positions) == FILE){
            final InnerDataViewHolderDoc innerDataViewHolder = (InnerDataViewHolderDoc) viewHolder;
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

            innerDataViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    File a = new File (String.valueOf(Uri.parse(details.getPath())));
                    Uri file = FileProvider.getUriForFile(ctx, ctx.getApplicationContext().getPackageName() +
                            ".my.package.name.provider",a);
                    intent.setDataAndType(file, "application/pdf");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    ctx.startActivity(intent);

                }
            });
        }


    }



    @Override
    public int getItemCount() {
        return innerDataList.size();
    }


    public class InnerDataViewHolderMultimedia extends RecyclerView.ViewHolder {

        TextView image_name;
        ImageView imageView;
        CardView cardView;
        CheckBox checkBox;

        public InnerDataViewHolderMultimedia(View itemView) {
            super(itemView);

            imageView  = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.recycler_view);
            checkBox = itemView.findViewById(R.id.checkbox);

        }
    }

    public class InnerDataViewHolderVideos extends RecyclerView.ViewHolder {

        ImageView imageView;
        CardView cardView;
        CheckBox checkBox;
        CircleImageView circleImageView;

        public InnerDataViewHolderVideos(View itemView) {
            super(itemView);

            imageView  = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.recycler_view);
            checkBox = itemView.findViewById(R.id.checkbox);
            circleImageView = itemView.findViewById(R.id.play);

        }
    }

    public class InnerDataViewHolderDoc extends RecyclerView.ViewHolder  {

        TextView tittle_name, data;
        CardView cardView;
        CheckBox checkBox;
        CircleImageView imageView;

        public InnerDataViewHolderDoc(View itemView) {
            super(itemView);

            tittle_name = itemView.findViewById(R.id.title);
            data = itemView.findViewById(R.id.data);
            cardView = itemView.findViewById(R.id.recycler_view);
            checkBox = itemView.findViewById(R.id.checkbox);
            imageView = itemView.findViewById(R.id.image);
        }

    }

    public interface OnCheckboxListener {
        void onCheckboxClicked(View view, List<FileDetails> updatedFiles);
    }
}
