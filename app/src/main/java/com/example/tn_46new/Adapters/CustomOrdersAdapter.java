package com.example.tn_46new.Adapters;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tn_46new.Admin.AdminHome;
import com.example.tn_46new.Models.CustomOrders;
import com.example.tn_46new.Models.OrderItems;
import com.example.tn_46new.Models.Products;
import com.example.tn_46new.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomOrdersAdapter extends RecyclerView.Adapter<CustomOrdersAdapter.CustomOrdersViewHolder> {

    private List<CustomOrders> list;
    private Context mContext;
    private Activity activity;

    public CustomOrdersAdapter(List<CustomOrders> list, Context mContext, Activity activity) {
        this.list = list;
        this.mContext = mContext;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CustomOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.itemlayout_custom_orders,parent,false);

        return new CustomOrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomOrdersViewHolder holder, int position) {

        final CustomOrders customOrders=list.get(position);

        //NOw, setting up the data
        holder.itemName.setText(customOrders.getName());
        holder.itemDescri.setText(customOrders.getDescri());
        holder.orderid.setText(customOrders.getOrderid());

        if (customOrders.getStatus().equals("placed"))
        {
            holder.relativeLayout.setBackgroundColor(Color.parseColor("#ffde05"));
        }
        else
        {
            holder.relativeLayout.setBackgroundColor(Color.parseColor("#A9A9A9"));
        }

        //Now, we have to build a bottom sheet dialog with the
        holder.orderid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                markSeen(customOrders.getOrderid());

                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(activity);
                View dialog= LayoutInflater.from(activity).inflate(R.layout.layout_custom_order_full,null);

                //Following are the objects on the bottom sheet dialog
                ImageView imageView=dialog.findViewById(R.id.image_coBottom);
                TextView productName=dialog.findViewById(R.id.productname_coBottom);
                TextView productDescri=dialog.findViewById(R.id.productdescri_coBottom);
                TextView contact=dialog.findViewById(R.id.contact_coBottom);
                TextView link=dialog.findViewById(R.id.link_coBottom);
                TextView copyLink=dialog.findViewById(R.id.copyLink_coBottom);

                //NOw, setting all the relevant data
                if (customOrders.getImage().equals("null"))
                {
                    imageView.setVisibility(View.GONE);
                }
                else
                {
                    Picasso.get().load(customOrders.getImage()).placeholder(R.drawable.placeholder).into(imageView);
                }

                productName.setText(customOrders.getName());
                productDescri.setText(customOrders.getDescri());
                contact.setText(customOrders.getContact());
                link.setText(customOrders.getLink());


                copyLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", link.getText().toString());
                            clipboard.setPrimaryClip(clip);

                            makeToast("Link Copied",0);
                        }
                        catch (Exception e)
                        {
                            makeToast(e.toString(),1);
                        }
                    }
                });








                bottomSheetDialog.setContentView(dialog);
                bottomSheetDialog.show();
            }
        });



        holder.itemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                markSeen(customOrders.getOrderid()); //Following is the method, that marks the item as seen

                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(activity);
                View dialog= LayoutInflater.from(activity).inflate(R.layout.layout_custom_order_full,null);

                //Following are the objects on the bottom sheet dialog
                ImageView imageView=dialog.findViewById(R.id.image_coBottom);
                TextView productName=dialog.findViewById(R.id.productname_coBottom);
                TextView productDescri=dialog.findViewById(R.id.productdescri_coBottom);
                TextView contact=dialog.findViewById(R.id.contact_coBottom);
                TextView link=dialog.findViewById(R.id.link_coBottom);
                TextView copyLink=dialog.findViewById(R.id.copyLink_coBottom);

                //NOw, setting all the relevant data
                if (customOrders.getImage().equals("null"))
                {
                    imageView.setVisibility(View.GONE);
                }
                else
                {
                    Picasso.get().load(customOrders.getImage()).placeholder(R.drawable.placeholder).into(imageView);
                }

                productName.setText(customOrders.getName());
                productDescri.setText(customOrders.getDescri());
                contact.setText(customOrders.getContact());
                link.setText(customOrders.getLink());


                copyLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", link.getText().toString());
                            clipboard.setPrimaryClip(clip);

                            makeToast("Link Copied",0);
                        }
                        catch (Exception e)
                        {
                            makeToast(e.toString(),1);
                        }
                    }
                });








                bottomSheetDialog.setContentView(dialog);
                bottomSheetDialog.show();
            }
        });


        holder.itemDescri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                markSeen(customOrders.getOrderid());

                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(activity);
                View dialog= LayoutInflater.from(activity).inflate(R.layout.layout_custom_order_full,null);

                //Following are the objects on the bottom sheet dialog
                ImageView imageView=dialog.findViewById(R.id.image_coBottom);
                TextView productName=dialog.findViewById(R.id.productname_coBottom);
                TextView productDescri=dialog.findViewById(R.id.productdescri_coBottom);
                TextView contact=dialog.findViewById(R.id.contact_coBottom);
                TextView link=dialog.findViewById(R.id.link_coBottom);
                TextView copyLink=dialog.findViewById(R.id.copyLink_coBottom);

                //NOw, setting all the relevant data
                if (customOrders.getImage().equals("null"))
                {
                    imageView.setVisibility(View.GONE);
                }
                else
                {
                    Picasso.get().load(customOrders.getImage()).placeholder(R.drawable.placeholder).into(imageView);
                }

                productName.setText(customOrders.getName());
                productDescri.setText(customOrders.getDescri());
                contact.setText(customOrders.getContact());
                link.setText(customOrders.getLink());


                copyLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", link.getText().toString());
                            clipboard.setPrimaryClip(clip);

                            makeToast("Link Copied",0);
                        }
                        catch (Exception e)
                        {
                            makeToast(e.toString(),1);
                        }
                    }
                });








                bottomSheetDialog.setContentView(dialog);
                bottomSheetDialog.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CustomOrdersViewHolder extends RecyclerView.ViewHolder
    {
        //Following are the objects on the screen
        TextView itemName;
        TextView itemDescri;
        TextView orderid;
        RelativeLayout relativeLayout;

        public CustomOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName=itemView.findViewById(R.id.itemName_customorderItem);
            itemDescri=itemView.findViewById(R.id.descri_customorderItem);
            relativeLayout=itemView.findViewById(R.id.main_customOrderItem);
            orderid=itemView.findViewById(R.id.orderid_customorderItem);

        }
    }

    public void makeToast(String textToShow, int lenght)
    {
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.layout_toast,
                null);


        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(textToShow);

        Toast toast = new Toast(mContext);
        toast.setGravity(Gravity.BOTTOM, 0, 60);
        if (lenght==0)
        {
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        else
        {
            toast.setDuration(Toast.LENGTH_LONG);

        }
        toast.setView(layout);
        toast.show();
    }



    private void markSeen(String orderid) {

        final Map<String,Object> map=new HashMap<>();
        map.put("status","seen");

        FirebaseDatabase.getInstance().getReference().child("Custom Orders").child(orderid).updateChildren(map);





    }

}
