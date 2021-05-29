package com.example.tn_46new.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tn_46new.FullProductActivity;
import com.example.tn_46new.Models.Products;
import com.example.tn_46new.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GroceryAdapterHome extends RecyclerView.Adapter<GroceryAdapterHome.GroceryViewHolderHome>{

    private List<Products> list;
    private Context mContext;
    private Activity activity;
    private String USER_TYPE="";

    public GroceryAdapterHome(List<Products> list, Context mContext, Activity activity, String USER_TYPE) {
        this.list = list;
        this.mContext = mContext;
        this.activity = activity;
        this.USER_TYPE = USER_TYPE;
    }

    @NonNull
    @Override
    public GroceryViewHolderHome onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.itemlayout_grocery_home,parent,false);

        return new GroceryViewHolderHome(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryViewHolderHome holder, int position) {
        final Products products=list.get(position);

        //Setting up the relevant data
        holder.name.setText(products.getName());
        holder.price.setText("Rs "+products.getPrice()+"/-");

        Picasso.get().load(products.getImageone()).placeholder(R.drawable.placeholder).into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (USER_TYPE.equals("buyer"))
                {
                    Intent i=new Intent(mContext, FullProductActivity.class);
                    i.putExtra("pid",products.getPid());
                    i.putExtra("user","buyer");
                    i.putExtra("category","g");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                }
                if (USER_TYPE.equals("seller"))
                {
                    Intent i=new Intent(mContext, FullProductActivity.class);
                    i.putExtra("pid",products.getPid());
                    i.putExtra("user","seller");
                    i.putExtra("category","g");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                }
                if (USER_TYPE.equals("admin"))
                {
                    Intent i=new Intent(mContext, FullProductActivity.class);
                    i.putExtra("pid",products.getPid());
                    i.putExtra("user","admin");
                    i.putExtra("category","g");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class GroceryViewHolderHome extends RecyclerView.ViewHolder
    {

        //Following are the items on the layout
        private ImageView image;
        private TextView name;
        private TextView price;

        public GroceryViewHolderHome(@NonNull View itemView) {
            super(itemView);

            image=itemView.findViewById(R.id.image_layout_groceryHome);
            name=itemView.findViewById(R.id.name_layout_groceryHome);
            price=itemView.findViewById(R.id.price_layout_groceryHome);
        }
    }
}
