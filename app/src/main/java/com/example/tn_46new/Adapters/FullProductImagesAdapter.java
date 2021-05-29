package com.example.tn_46new.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tn_46new.Models.Products;
import com.example.tn_46new.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FullProductImagesAdapter extends RecyclerView.Adapter<FullProductImagesAdapter.FullProductImagesViewHolder> {

    private List<String> list;
    private Context mContext;
    private Activity activity;
    private String USER_TYPE="";

    public FullProductImagesAdapter(List<String> list, Context mContext, Activity activity, String USER_TYPE) {
        this.list = list;
        this.mContext = mContext;
        this.activity = activity;
        this.USER_TYPE = USER_TYPE;
    }

    @NonNull
    @Override
    public FullProductImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.itemlayout_fullproduct_images,parent,false);

        return new FullProductImagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FullProductImagesViewHolder holder, int position) {

        Picasso.get().load(list.get(position)).placeholder(R.drawable.placeholder).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FullProductImagesViewHolder extends RecyclerView.ViewHolder
    {

        private ImageView  imageView;
        public FullProductImagesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image_fullProductRecyler);
        }
    }
}
