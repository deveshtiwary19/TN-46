package com.example.tn_46new.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tn_46new.Models.OrderItems;
import com.example.tn_46new.Models.Products;
import com.example.tn_46new.Models.SellerDetails;
import com.example.tn_46new.Models.Sellers;
import com.example.tn_46new.R;
import com.example.tn_46new.seller.SellerHome;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.OrderItemsViewHolder>
{
    private List<OrderItems> list;
    private Context mContext;
    private Activity activity;
    private String USER_TYPE="";

    public OrderItemsAdapter(List<OrderItems> list, Context mContext, Activity activity, String USER_TYPE) {
        this.list = list;
        this.mContext = mContext;
        this.activity = activity;
        this.USER_TYPE = USER_TYPE;
    }

    @NonNull
    @Override
    public OrderItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.itemlayout_ordered_items,parent,false);

        return new OrderItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemsViewHolder holder, int position) {

        OrderItems orderItems=list.get(position);

        holder.name.setText(orderItems.getName());
        holder.qty.setText("x"+orderItems.getQty());
        holder.variant.setText("Variant: "+orderItems.getVariant());
        holder.total.setText("Rs "+orderItems.getTotalprice()+"/-");

        Picasso.get().load(orderItems.getImage()).placeholder(R.drawable.placeholder).into(holder.image);

        //Now, we need to build a bottom sheet dialog that will show, the detail of the seller for the respective product
        holder.seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(activity);
                View dialog= LayoutInflater.from(activity).inflate(R.layout.layout_bottomsheet_sellerprofile,null);

                TextView shopname=dialog.findViewById(R.id.shopName_sellerProfile);
                TextView sellerName=dialog.findViewById(R.id.sellerName_sellerProfile);
                TextView adress=dialog.findViewById(R.id.adress_sellerProfile);
                TextView email=dialog.findViewById(R.id.email_sellerProfile);
                TextView contact=dialog.findViewById(R.id.contact_sellerProfile);

                //Now, fetching the data with the seller id in the item
                FirebaseDatabase.getInstance().getReference().child("Seller Registration").child(orderItems.getSellerid())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists())
                                {
                                    SellerDetails sellers=snapshot.getValue(SellerDetails.class);
                                    
                                    shopname.setText(sellers.getShopname());
                                    sellerName.setText(sellers.getName());
                                    adress.setText(sellers.getAdress());
                                    email.setText(sellers.getEmail());
                                    contact.setText(sellers.getContact());

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });






                bottomSheetDialog.setContentView(dialog);
                bottomSheetDialog.show();

            }
        });


        if (USER_TYPE.equals("admin"))
        {
            holder.seller.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.seller.setVisibility(View.GONE);
        }



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OrderItemsViewHolder extends RecyclerView.ViewHolder
    {
        TextView name;
        TextView qty;
        TextView variant;
        TextView total;

        ImageView image;
        ImageView seller;

        public OrderItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name_ordereItems);
            qty=itemView.findViewById(R.id.qty_ordereItems);
            variant=itemView.findViewById(R.id.variant_ordereItems);
            total=itemView.findViewById(R.id.total_ordereItems);
            image=itemView.findViewById(R.id.image_ordereItems);
            seller=itemView.findViewById(R.id.seller_ordereItems);

        }
    }

}
