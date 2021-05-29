package com.example.tn_46new.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tn_46new.FullProductActivity;
import com.example.tn_46new.Models.Cart;
import com.example.tn_46new.Models.Products;
import com.example.tn_46new.R;
import com.example.tn_46new.seller.AddProduct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartAdapterViewHolder> {

    private List<Cart> list;
    private Context mContext;
    private Activity activity;
    private String showing;

    private ProgressDialog progressDialog;


    //The constructor


    public CartItemAdapter(List<Cart> list, Context mContext, Activity activity, String showing) {
        this.list = list;
        this.mContext = mContext;
        this.activity = activity;
        this.showing = showing;

    }

    @NonNull
    @Override
    public CartAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.itemlayout_cart_items,parent,false);

        return new CartAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapterViewHolder holder, int position) {

        //Now, setting the item according to the list recieved
         final Cart cart=list.get(position);

        Picasso.get().load(cart.getImage()).placeholder(R.drawable.placeholder).into(holder.imageView);
        holder.name.setText(cart.getName());
        holder.variant.setText("Variant: "+cart.getVariant());

        holder.price.setText("Rs "+cart.getTotalprice()+"/-");

        holder.qty.setText(cart.getQty());


        //Setting a click listeneer on minus Btn
        holder.minnusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(activity);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show(); //Starting the progress dialog
                progressDialog.setContentView(R.layout.layout_loading);
                progressDialog.setCancelable(false);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


                double qty=Double.parseDouble(cart.getQty());
                double uP=Double.parseDouble(cart.getUnitprice());
                double tP=Double.parseDouble(cart.getTotalprice());

                if (qty==1)
                {
                    //Remove the product
                    FirebaseDatabase.getInstance().getReference().child("Cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(cart.getCid()).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        progressDialog.dismiss();
                                        makeToast("Item Removed",0);
                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        makeToast(task.getException().toString(),1);
                                    }
                                }
                            });

                }
                else
                {

                    qty--;


                    Double newP=uP*qty;

                    int q=(int)qty;

                    final Map<String,Object>map=new HashMap<>();
                    map.put("qty",String.valueOf(q));
                    map.put("totalprice",String.valueOf(newP));

                    FirebaseDatabase.getInstance().getReference().child("Cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(cart.getCid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                progressDialog.dismiss();
                            }
                            else {
                                progressDialog.dismiss();
                                makeToast(task.getException().toString(),1);
                            }
                        }
                    });



                }

                if (showing.equals("order"))
                {
                    holder.qtyC.setVisibility(View.GONE);
                }










            }
        });



        //Setting a click listeneer on plus Btn
        holder.plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(activity);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show(); //Starting the progress dialog
                progressDialog.setContentView(R.layout.layout_loading);
                progressDialog.setCancelable(false);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


                double qty=Double.parseDouble(cart.getQty());
                double uP=Double.parseDouble(cart.getUnitprice());
                double tP=Double.parseDouble(cart.getTotalprice());

                if (qty==10)
                {
                    makeToast("Maximum product limit is 10",1);
                    progressDialog.dismiss();

                }
                else
                {

                    qty++;

                    Double newP=uP*qty;

                    int q=(int)qty;

                    final Map<String,Object>map=new HashMap<>();
                    map.put("qty",String.valueOf(q));
                    map.put("totalprice",String.valueOf(newP));

                    FirebaseDatabase.getInstance().getReference().child("Cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(cart.getCid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                progressDialog.dismiss();
                            }
                            else {
                                progressDialog.dismiss();
                                makeToast(task.getException().toString(),1);
                            }
                        }
                    });



                }










            }
        });

























    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CartAdapterViewHolder extends RecyclerView.ViewHolder{

        //Following are the items on the cart item layout
        private ImageView imageView;
        private TextView name;
        private TextView variant;
        private TextView price;
        private TextView qty;
        private ImageView plusBtn;
        private ImageView minnusBtn;
        private CardView qtyC;

        public CartAdapterViewHolder(@NonNull View itemView) {
            super(itemView);


            imageView=itemView.findViewById(R.id.image_cartItem);
            name=itemView.findViewById(R.id.name_cartItem);
            variant=itemView.findViewById(R.id.variant_cartItem);
            price=itemView.findViewById(R.id.totalPrice_cartItem);
            qty=itemView.findViewById(R.id.qty_cartItem);
            plusBtn=itemView.findViewById(R.id.addBtn_cartItem);
            minnusBtn=itemView.findViewById(R.id.minusBtn_cartItem);
            qtyC=itemView.findViewById(R.id.qtyControl_cartItem);


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
}
