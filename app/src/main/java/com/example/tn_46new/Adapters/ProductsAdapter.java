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

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {

    private List<Products> list;
    private Context mContext;
    private Activity activity;
    private String USER_TYPE="";
    private String Category="";

    public ProductsAdapter(List<Products> list, Context mContext, Activity activity, String USER_TYPE, String category) {
        this.list = list;
        this.mContext = mContext;
        this.activity = activity;
        this.USER_TYPE = USER_TYPE;
        Category = category;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.itemlayout_products_adapter,parent,false);

        return new ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {

        final Products products=list.get(position);

        //Now, setting the relevant data from th list of products
        Picasso.get().load(products.getImageone()).placeholder(R.drawable.placeholder).into(holder.imageView);
        holder.name.setText(products.getName());
        holder.price.setText("Rs "+products.getPrice()+"/-");
        holder.descri.setText(products.getDescri());


        holder.viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (USER_TYPE.equals("buyer"))
                {
                    Intent i=new Intent(mContext, FullProductActivity.class);
                    i.putExtra("pid",products.getPid());
                    i.putExtra("user","buyer");
                    if (Category.equals("fashion"))
                    {
                        i.putExtra("category","f");
                    }
                    else if (Category.equals("grocery"))
                    {
                        i.putExtra("category","g");
                    }
                    else if (Category.equals("electronics"))
                    {
                        i.putExtra("category","e");
                    }
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                }
                if (USER_TYPE.equals("seller"))
                {
                    Intent i=new Intent(mContext, FullProductActivity.class);
                    i.putExtra("pid",products.getPid());
                    i.putExtra("user","seller");
                    if (Category.equals("fashion"))
                    {
                        i.putExtra("category","f");
                    }
                    else if (Category.equals("grocery"))
                    {
                        i.putExtra("category","g");
                    }
                    else if (Category.equals("electronics"))
                    {
                        i.putExtra("category","e");
                    }
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                }
                if (USER_TYPE.equals("admin"))
                {
                    Intent i=new Intent(mContext, FullProductActivity.class);
                    i.putExtra("pid",products.getPid());
                    i.putExtra("user","admin");
                    if (Category.equals("fashion"))
                    {
                        i.putExtra("category","f");
                    }
                    else if (Category.equals("grocery"))
                    {
                        i.putExtra("category","g");
                    }
                    else if (Category.equals("electronics"))
                    {
                        i.putExtra("category","e");
                    }
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                }
            }
        });


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (USER_TYPE.equals("buyer"))
                {
                    Intent i=new Intent(mContext, FullProductActivity.class);
                    i.putExtra("pid",products.getPid());
                    i.putExtra("user","buyer");
                    if (Category.equals("fashion"))
                    {
                        i.putExtra("category","f");
                    }
                    else if (Category.equals("grocery"))
                    {
                        i.putExtra("category","g");
                    }
                    else if (Category.equals("electronics"))
                    {
                        i.putExtra("category","e");
                    }
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                }
                if (USER_TYPE.equals("seller"))
                {
                    Intent i=new Intent(mContext, FullProductActivity.class);
                    i.putExtra("pid",products.getPid());
                    i.putExtra("user","seller");
                    if (Category.equals("fashion"))
                    {
                        i.putExtra("category","f");
                    }
                    else if (Category.equals("grocery"))
                    {
                        i.putExtra("category","g");
                    }
                    else if (Category.equals("electronics"))
                    {
                        i.putExtra("category","e");
                    }
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                }
                if (USER_TYPE.equals("admin"))
                {
                    Intent i=new Intent(mContext, FullProductActivity.class);
                    i.putExtra("pid",products.getPid());
                    i.putExtra("user","admin");
                    if (Category.equals("fashion"))
                    {
                        i.putExtra("category","f");
                    }
                    else if (Category.equals("grocery"))
                    {
                        i.putExtra("category","g");
                    }
                    else if (Category.equals("electronics"))
                    {
                        i.putExtra("category","e");
                    }
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                }
            }
        });


        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (USER_TYPE.equals("buyer"))
                {
                    Intent i=new Intent(mContext, FullProductActivity.class);
                    i.putExtra("pid",products.getPid());
                    i.putExtra("user","buyer");
                    if (Category.equals("fashion"))
                    {
                        i.putExtra("category","f");
                    }
                    else if (Category.equals("grocery"))
                    {
                        i.putExtra("category","g");
                    }
                    else if (Category.equals("electronics"))
                    {
                        i.putExtra("category","e");
                    }
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                }
                if (USER_TYPE.equals("seller"))
                {
                    Intent i=new Intent(mContext, FullProductActivity.class);
                    i.putExtra("pid",products.getPid());
                    i.putExtra("user","seller");
                    if (Category.equals("fashion"))
                    {
                        i.putExtra("category","f");
                    }
                    else if (Category.equals("grocery"))
                    {
                        i.putExtra("category","g");
                    }
                    else if (Category.equals("electronics"))
                    {
                        i.putExtra("category","e");
                    }
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                }
                if (USER_TYPE.equals("admin"))
                {
                    Intent i=new Intent(mContext, FullProductActivity.class);
                    i.putExtra("pid",products.getPid());
                    i.putExtra("user","admin");
                    if (Category.equals("fashion"))
                    {
                        i.putExtra("category","f");
                    }
                    else if (Category.equals("grocery"))
                    {
                        i.putExtra("category","g");
                    }
                    else if (Category.equals("electronics"))
                    {
                        i.putExtra("category","e");
                    }
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                }
            }
        });


        holder.price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (USER_TYPE.equals("buyer"))
                {
                    Intent i=new Intent(mContext, FullProductActivity.class);
                    i.putExtra("pid",products.getPid());
                    i.putExtra("user","buyer");
                    if (Category.equals("fashion"))
                    {
                        i.putExtra("category","f");
                    }
                    else if (Category.equals("grocery"))
                    {
                        i.putExtra("category","g");
                    }
                    else if (Category.equals("electronics"))
                    {
                        i.putExtra("category","e");
                    }
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                }
                if (USER_TYPE.equals("seller"))
                {
                    Intent i=new Intent(mContext, FullProductActivity.class);
                    i.putExtra("pid",products.getPid());
                    i.putExtra("user","seller");
                    if (Category.equals("fashion"))
                    {
                        i.putExtra("category","f");
                    }
                    else if (Category.equals("grocery"))
                    {
                        i.putExtra("category","g");
                    }
                    else if (Category.equals("electronics"))
                    {
                        i.putExtra("category","e");
                    }
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                }
                if (USER_TYPE.equals("admin"))
                {
                    Intent i=new Intent(mContext, FullProductActivity.class);
                    i.putExtra("pid",products.getPid());
                    i.putExtra("user","admin");
                    if (Category.equals("fashion"))
                    {
                        i.putExtra("category","f");
                    }
                    else if (Category.equals("grocery"))
                    {
                        i.putExtra("category","g");
                    }
                    else if (Category.equals("electronics"))
                    {
                        i.putExtra("category","e");
                    }
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(i);
                }
            }
        });

        holder.descri.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (USER_TYPE.equals("buyer"))
                        {
                            Intent i=new Intent(mContext, FullProductActivity.class);
                            i.putExtra("pid",products.getPid());
                            i.putExtra("user","buyer");
                            if (Category.equals("fashion"))
                            {
                                i.putExtra("category","f");
                            }
                            else if (Category.equals("grocery"))
                            {
                                i.putExtra("category","g");
                            }
                            else if (Category.equals("electronics"))
                            {
                                i.putExtra("category","e");
                            }
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(i);
                        }
                        if (USER_TYPE.equals("seller"))
                        {
                            Intent i=new Intent(mContext, FullProductActivity.class);
                            i.putExtra("pid",products.getPid());
                            i.putExtra("user","seller");
                            if (Category.equals("fashion"))
                            {
                                i.putExtra("category","f");
                            }
                            else if (Category.equals("grocery"))
                            {
                                i.putExtra("category","g");
                            }
                            else if (Category.equals("electronics"))
                            {
                                i.putExtra("category","e");
                            }
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(i);
                        }
                        if (USER_TYPE.equals("admin"))
                        {
                            Intent i=new Intent(mContext, FullProductActivity.class);
                            i.putExtra("pid",products.getPid());
                            i.putExtra("user","admin");
                            if (Category.equals("fashion"))
                            {
                                i.putExtra("category","f");
                            }
                            else if (Category.equals("grocery"))
                            {
                                i.putExtra("category","g");
                            }
                            else if (Category.equals("electronics"))
                            {
                                i.putExtra("category","e");
                            }
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(i);
                        }
                    }
                }
        );




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ProductsViewHolder extends RecyclerView.ViewHolder
    {
        //Following are the objects on the item layout
        private ImageView imageView;
        private TextView name;
        private TextView price;
        private TextView descri;
        private TextView viewBtn;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            //Assinging ids to respective objects on the itemLayout
            imageView=itemView.findViewById(R.id.image_item_productsAdapter);
            name=itemView.findViewById(R.id.name_item_productsAdapter);
            price=itemView.findViewById(R.id.price_item_productsAdapter);
            descri=itemView.findViewById(R.id.descri_item_productsAdapter);
            viewBtn=itemView.findViewById(R.id.view_item_productsAdapter);

        }
    }
}
