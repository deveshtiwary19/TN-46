package com.example.tn_46new.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tn_46new.Models.Orders;
import com.example.tn_46new.Models.Products;
import com.example.tn_46new.OrderSummary;
import com.example.tn_46new.R;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {

    private List<Orders> list;
    private Context mContext;
    private Activity activity;
    private String USER_TYPE="";

    public OrdersAdapter(List<Orders> list, Context mContext, Activity activity, String USER_TYPE) {
        this.list = list;
        this.mContext = mContext;
        this.activity = activity;
        this.USER_TYPE = USER_TYPE;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.itemlayout_orders,parent,false);

        return new OrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {

        //Setting the data
        final Orders orders=list.get(position);

        holder.amount.setText("Rs "+orders.getTotal()+"/-");
        holder.orderid.setText(orders.getOrderid());

        if (orders.getStatus().equals("placed"))
             holder.orderStatus.setText("Placed");
        else if (orders.getStatus().equals("accepted"))
            holder.orderStatus.setText("Accepted");
        else if (orders.getStatus().equals("delivered"))
            holder.orderStatus.setText("Delivered");

        holder.dateAndTime.setText(orders.getTime()+" on "+orders.getDate());

        holder.amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext, OrderSummary.class);
                i.putExtra("user",USER_TYPE);
                i.putExtra("id",orders.getOrderid());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
            }
        });

        holder.orderid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext, OrderSummary.class);
                i.putExtra("user",USER_TYPE);
                i.putExtra("id",orders.getOrderid());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
            }
        });

        holder.orderStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext, OrderSummary.class);
                i.putExtra("user",USER_TYPE);
                i.putExtra("id",orders.getOrderid());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
            }
        });

        holder.dateAndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext, OrderSummary.class);
                i.putExtra("user",USER_TYPE);
                i.putExtra("id",orders.getOrderid());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OrdersViewHolder extends RecyclerView.ViewHolder
    {

        //Following are the objects on the itemlayout
        private TextView amount;
        private TextView orderid;
        private TextView orderStatus;
        private TextView dateAndTime;


        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            amount=itemView.findViewById(R.id.amount_orderItems);
            orderid=itemView.findViewById(R.id.orderid_orderItems);
            orderStatus=itemView.findViewById(R.id.orderStatus_orderItems);
            dateAndTime=itemView.findViewById(R.id.dateAndTime_orderItems);


        }
    }
}
