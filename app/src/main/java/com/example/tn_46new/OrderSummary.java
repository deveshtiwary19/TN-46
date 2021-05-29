package com.example.tn_46new;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tn_46new.Adapters.GroceryAdapterHome;
import com.example.tn_46new.Adapters.OrderItemsAdapter;
import com.example.tn_46new.Buyer.BuyerHome;
import com.example.tn_46new.Models.Cart;
import com.example.tn_46new.Models.OrderItems;
import com.example.tn_46new.Models.Orders;
import com.example.tn_46new.Models.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderSummary extends AppCompatActivity {

    //Following are the objects on the screen
    private ImageView backBtn;
    private TextView orderID;
    private RecyclerView recyclerView;
    private TextView netAmount;

    private TextView itemsTxt;


    private TextView deliveryDetails;
    private TextView deliveryDateTime;
    private TextView paymentMode;

    private LinearLayout adminLayout;
    private RadioGroup ragGrp;
    private RadioButton radBtn_palced;
    private RadioButton radBtn_accepted;
    private RadioButton radBtn_delivered;

    private Button updateBtn;

    private ImageView orderStatusCustomer;

    private boolean t=false;


    //Follwoing are the var to get from the intent
    private String  USER_TYPE="";
    private String  idOrder="";

    //Folllowing is the variable, that will hold the order status for updating process
    private String status="";

    private ProgressDialog progressDialog;

    private List<OrderItems> list;
    private OrderItemsAdapter adapter;




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        //Folllowing is the code to set the status bar as color
        Window window= OrderSummary.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(OrderSummary.this, R.color.mainyellow));

        //Restricting the lanscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init(); //Following is the method, to  initialize all the objects with their respective ids

        //Getting the values from intent
        Intent i=getIntent();
        USER_TYPE=i.getStringExtra("user");
        idOrder=i.getStringExtra("id");







        if (USER_TYPE.equals("admin"))
        {
            //The user is admin
            adminLayout.setVisibility(View.VISIBLE);
            orderStatusCustomer.setVisibility(View.GONE);
        }
        else
        {
        //The user is customer
            adminLayout.setVisibility(View.GONE);
            orderStatusCustomer.setVisibility(View.VISIBLE);
        }

        fetchandSetAllTheRelevantDataForOrder(); //Follwoing is the method that fetches all the data for

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        list=new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("Order Items").child(idOrder)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (list!=null)
                        {
                            list.clear();
                        }
                        if (snapshot.exists())
                        {
                            for (DataSnapshot dataSnapshot:snapshot.getChildren())
                            {
                                OrderItems orderItems=dataSnapshot.getValue(OrderItems.class);
                                list.add(orderItems);
                            }

                            Collections.reverse(list);
                            adapter=new OrderItemsAdapter(list,getApplicationContext(),OrderSummary.this,USER_TYPE);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




        ragGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.plaedRadBtn_os)
                {
                    status="p";
                }
                else if (checkedId==R.id.acceptedRadBtn_os)
                {
                    status="a";
                }
                else if (checkedId==R.id.deliveredRadBtn_os)
                {
                    status="d";
                }
            }
        });









        //Now, a click listener for the update status button
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                   if (status.equals("p"))
                   {

                       progressDialog = new ProgressDialog(OrderSummary.this);
                       progressDialog.setCanceledOnTouchOutside(false);
                       progressDialog.show(); //Starting the progress dialog
                       progressDialog.setContentView(R.layout.layout_loading);
                       progressDialog.setCancelable(false);
                       progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                       //The admin is trying to mark the order as placed

                       Dialog dialog = new Dialog(OrderSummary.this);
                       dialog.setContentView(R.layout.layout_dialogbox);
                       dialog.setCancelable(false);
                       dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                       TextView text = dialog.findViewById(R.id.text_dialog);
                       TextView positive = dialog.findViewById(R.id.positive_dialog);
                       TextView negative = dialog.findViewById(R.id.negative_dialog);

                       text.setText("You are attempting to mark the order as PLACED. Are you sure to change the order status to Placed?");

                       positive.setText("Yes");
                       negative.setText("No");

                       negative.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               dialog.cancel();
                               progressDialog.dismiss();
                           }
                       });

                       positive.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {

                               dialog.cancel();

                               final Map<String,Object> map=new HashMap<>();
                               map.put("status","placed");
                               FirebaseDatabase.getInstance().getReference().child("Orders").child(idOrder).updateChildren(map)
                                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {
                                               if (task.isSuccessful())
                                               {
                                                   progressDialog.dismiss();
                                                   makeToast("Order status updated",0);
                                               }
                                               else
                                               {
                                                   progressDialog.dismiss();
                                                   makeToast(task.getException().toString(),1);
                                               }
                                           }
                                       });






                           }
                       });


                       dialog.show();


                   }

                   else if (status.equals("a"))
                   {

                       progressDialog = new ProgressDialog(OrderSummary.this);
                       progressDialog.setCanceledOnTouchOutside(false);
                       progressDialog.show(); //Starting the progress dialog
                       progressDialog.setContentView(R.layout.layout_loading);
                       progressDialog.setCancelable(false);
                       progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                       //The admin is trying to mark the order as placed

                       Dialog dialog = new Dialog(OrderSummary.this);
                       dialog.setContentView(R.layout.layout_dialogbox);
                       dialog.setCancelable(false);
                       dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                       TextView text = dialog.findViewById(R.id.text_dialog);
                       TextView positive = dialog.findViewById(R.id.positive_dialog);
                       TextView negative = dialog.findViewById(R.id.negative_dialog);

                       text.setText("You are attempting to mark the order as ACCEPTED. Are you sure to change the order status to Accepted?");

                       positive.setText("Yes");
                       negative.setText("No");

                       negative.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               dialog.cancel();
                               progressDialog.dismiss();
                           }
                       });

                       positive.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {

                               dialog.cancel();

                               final Map<String,Object> map=new HashMap<>();
                               map.put("status","accepted");
                               FirebaseDatabase.getInstance().getReference().child("Orders").child(idOrder).updateChildren(map)
                                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {
                                               if (task.isSuccessful())
                                               {
                                                   progressDialog.dismiss();
                                                   makeToast("Order status updated",0);
                                               }
                                               else
                                               {
                                                   progressDialog.dismiss();
                                                   makeToast(task.getException().toString(),1);
                                               }
                                           }
                                       });






                           }
                       });


                       dialog.show();


                   }

                   else if (status.equals("d"))
                   {

                       progressDialog = new ProgressDialog(OrderSummary.this);
                       progressDialog.setCanceledOnTouchOutside(false);
                       progressDialog.show(); //Starting the progress dialog
                       progressDialog.setContentView(R.layout.layout_loading);
                       progressDialog.setCancelable(false);
                       progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                       //The admin is trying to mark the order as placed

                       Dialog dialog = new Dialog(OrderSummary.this);
                       dialog.setContentView(R.layout.layout_dialogbox);
                       dialog.setCancelable(false);
                       dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                       TextView text = dialog.findViewById(R.id.text_dialog);
                       TextView positive = dialog.findViewById(R.id.positive_dialog);
                       TextView negative = dialog.findViewById(R.id.negative_dialog);

                       text.setText("You are attempting to mark the order as DELIVERED. Are you sure to change the order status to Delivered?");

                       positive.setText("Yes");
                       negative.setText("No");

                       negative.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               dialog.cancel();
                               progressDialog.dismiss();
                           }
                       });

                       positive.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {

                               dialog.cancel();

                               final Map<String,Object> map=new HashMap<>();
                               map.put("status","delivered");
                               FirebaseDatabase.getInstance().getReference().child("Orders").child(idOrder).updateChildren(map)
                                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {
                                               if (task.isSuccessful())
                                               {
                                                   progressDialog.dismiss();
                                                   makeToast("Order status updated",0);
                                               }
                                               else
                                               {
                                                   progressDialog.dismiss();
                                                   makeToast(task.getException().toString(),1);
                                               }
                                           }
                                       });






                           }
                       });


                       dialog.show();


                   }











            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }



    private void fetchandSetAllTheRelevantDataForOrder() {
        FirebaseDatabase.getInstance().getReference().child("Orders").child(idOrder)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if (snapshot.exists())
                       {
                           Orders orders=snapshot.getValue(Orders.class);

                           //Setting up the relevant data for the specific order
                           orderID.setText(idOrder);
                           netAmount.setText("Rs "+orders.getTotal()+"/-");

                           //Now, setting the delivery details
                           deliveryDetails.setText(orders.getDelname()+"\n"+orders.getDeladress()+"\n"+orders.getDelcontact());

                           //Setting up the date and time
                           deliveryDateTime.setText("Date: "+orders.getDate()+"\n"+"Time: "+orders.getTime());

                           if (orders.getPayment().equals("paid"))
                           {
                               paymentMode.setText("Paid Online");
                           }
                           else
                           {
                               paymentMode.setText("Cash On Delivery");
                           }


                           //Now, setting the track status
                           if (orders.getStatus().equals("placed"))
                           {
                               Picasso.get().load(R.drawable.placed_status).into(orderStatusCustomer);
                               radBtn_palced.setChecked(true);
                               status="p";
                           }
                           else if (orders.getStatus().equals("accepted"))
                           {
                               Picasso.get().load(R.drawable.accepted_status).into(orderStatusCustomer);
                               radBtn_accepted.setChecked(true);
                               status="a";
                           }
                           else if (orders.getStatus().equals("delivered"))
                           {
                               Picasso.get().load(R.drawable.delivered_status).into(orderStatusCustomer);
                               radBtn_delivered.setChecked(true);
                               status="d";
                           }



                       }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void init() {
        backBtn=findViewById(R.id.backBtn_os);
        orderID=findViewById(R.id.orderid_os);
        recyclerView=findViewById(R.id.recycler_os);
        netAmount=findViewById(R.id.amount_os);


        deliveryDetails=findViewById(R.id.deliveryDetails_os);
        deliveryDateTime=findViewById(R.id.dateTime_os);
        paymentMode=findViewById(R.id.paymentmode_os);

        adminLayout=findViewById(R.id.adminLayout_os);
        ragGrp=findViewById(R.id.radGrp_os);
        radBtn_palced=findViewById(R.id.plaedRadBtn_os);
        radBtn_accepted=findViewById(R.id.acceptedRadBtn_os);
        radBtn_delivered=findViewById(R.id.deliveredRadBtn_os);

       updateBtn=findViewById(R.id.updateBtn_os);

        orderStatusCustomer=findViewById(R.id.customerLayout_os);
    }
    public void makeToast(String textToShow, int lenght)
    {
        LayoutInflater inflater = OrderSummary.this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.layout_toast,
                null);


        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(textToShow);

        Toast toast = new Toast(getApplicationContext());
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
