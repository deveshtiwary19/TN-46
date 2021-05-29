package com.example.tn_46new;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.L;
import com.example.tn_46new.Adapters.FashionAdapterHome;
import com.example.tn_46new.Adapters.OrdersAdapter;
import com.example.tn_46new.Adapters.ProductsAdapter;
import com.example.tn_46new.Buyer.BuyerHome;
import com.example.tn_46new.Models.Orders;
import com.example.tn_46new.Models.Products;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    private String TYPE="";

    //Following are the objects on the screen
    private ImageView backBtn;
    private TextView heading;
    private EditText searchBar;
    private RecyclerView recyclerView;

    private List<Orders>list;

    private OrdersAdapter adapter;

    private ProgressDialog progressDialog;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        //Folllowing is the code to set the status bar as color
        Window window= OrdersActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(OrdersActivity.this, R.color.mainyellow));

        //Restricting the lanscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Getting the user type, to send to the adapter
        Intent i=getIntent();
        TYPE=i.getStringExtra("type");

        init(); //Following is the method, to assign the objects with their respective ids

        //Seting up the recycler view
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        list=new ArrayList<>();



        if (TYPE.equals("admin"))
        {
            heading.setText("Recieved Orders");
            fetchAllTheOrders();
        }
        else if (TYPE.equals("buyer"))
        {
            heading.setText("My Orders");
            fetchOnlyTheCurrentUserOrders();
        }


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchForOrder(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }

    private void searchForOrder(String toString) {

         Query searchQuery= FirebaseDatabase.getInstance().getReference().child("Orders").orderByChild("orderid").startAt(toString).endAt(toString+"\uf8ff");

        searchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(list!=null)
                {
                    list.clear();
                }

                if (snapshot.exists())
                {


                    for (DataSnapshot dataSnapshot: snapshot.getChildren())
                    {
                       Orders orders=dataSnapshot.getValue(Orders.class);

                       if (TYPE.equals("admin"))
                       {
                           list.add(orders);
                       }
                       else
                       {
                           if (orders.getCustomerid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                           {
                               list.add(orders);
                           }
                       }





                    }

                    Collections.reverse(list);
                    adapter=new OrdersAdapter(list,getApplicationContext(), OrdersActivity.this,TYPE);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                }
                else
                {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void fetchAllTheOrders() {

        progressDialog = new ProgressDialog(OrdersActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show(); //Starting the progress dialog
        progressDialog.setContentView(R.layout.layout_loading);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        FirebaseDatabase.getInstance().getReference().child("Orders")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (list!=null)
                        {
                            list.clear();
                        }
                        if (snapshot.exists())
                        {
                            for (DataSnapshot dataSnapshot: snapshot.getChildren())
                            {
                                Orders orders=dataSnapshot.getValue(Orders.class);

                                    list.add(orders);

                            }


                            Collections.reverse(list);
                            adapter=new OrdersAdapter(list,getApplicationContext(), OrdersActivity.this,TYPE);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();


                            progressDialog.dismiss();





                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void fetchOnlyTheCurrentUserOrders() {

        progressDialog = new ProgressDialog(OrdersActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show(); //Starting the progress dialog
        progressDialog.setContentView(R.layout.layout_loading);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        FirebaseDatabase.getInstance().getReference().child("Orders")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (list!=null)
                        {
                            list.clear();
                        }
                        if (snapshot.exists())
                        {
                            for (DataSnapshot dataSnapshot: snapshot.getChildren())
                            {
                                Orders orders=dataSnapshot.getValue(Orders.class);
                                if (orders.getCustomerid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                {
                                    list.add(orders);
                                }
                            }


                            Collections.reverse(list);
                            adapter=new OrdersAdapter(list,getApplicationContext(), OrdersActivity.this,TYPE);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();


                            progressDialog.dismiss();





                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void init() {
        backBtn=findViewById(R.id.backBtn_orders);
        heading=findViewById(R.id.title_orders);
        searchBar=findViewById(R.id.searchBar_orders);
        recyclerView=findViewById(R.id.recycler_orders);

    }
}