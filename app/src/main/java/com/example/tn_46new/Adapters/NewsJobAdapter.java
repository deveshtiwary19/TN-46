package com.example.tn_46new.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tn_46new.Models.NewsJobs;
import com.example.tn_46new.R;

import java.util.List;

public class NewsJobAdapter extends RecyclerView.Adapter<NewsJobAdapter.NewsJobViewHolder> {

    private List<NewsJobs> list;
    private Context mContext;
    private Activity activity;

    public NewsJobAdapter(List<NewsJobs> list, Context mContext, Activity activity) {
        this.list = list;
        this.mContext = mContext;
        this.activity = activity;
    }

    @NonNull
    @Override
    public NewsJobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.itemlayout_news_jobs,parent,false);

        return new NewsJobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsJobViewHolder holder, int position) {

        final NewsJobs newsJobs=list.get(position);

        //Setting up the relevant data
        holder.title.setText(newsJobs.getTitle());
        holder.descri.setText(newsJobs.getDescri());

        holder.dateTime.setText(newsJobs.getDate()+" at "+newsJobs.getTime());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NewsJobViewHolder extends RecyclerView.ViewHolder
    {
        //Following are the objects on the itemview
        TextView title;
        TextView descri;
        TextView dateTime;

        public NewsJobViewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.title_newsjob_item);
            descri=itemView.findViewById(R.id.descri_newsjob_item);
            dateTime=itemView.findViewById(R.id.dateTime_newsjob_item);

        }
    }
}
