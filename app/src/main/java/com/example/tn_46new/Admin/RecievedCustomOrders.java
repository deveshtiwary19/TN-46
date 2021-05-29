package com.example.tn_46new.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.tn_46new.Adapters.CustomOrdersAdapter;
import com.example.tn_46new.Adapters.ElectronicHomeAdapter;
import com.example.tn_46new.Adapters.OrdersAdapter;
import com.example.tn_46new.Models.CustomOrders;
import com.example.tn_46new.Models.Orders;
import com.example.tn_46new.OrdersActivity;
import com.example.tn_46new.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecievedCustomOrders extends AppCompatActivity {

    //Following are the objects on the screen
    private ImageView backBtn;
    private EditText searchBar;
    private RecyclerView recyclerView;

    //Following are the objects for the recycler view
    private List<CustomOrders> list;
    private CustomOrdersAdapter adapter;

    private ProgressDialog progressDialog;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieved_custom_orders);

        //Folllowing is the code to set the status bar as color
        Window window= RecievedCustomOrders.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(RecievedCustomOrders.this, R.color.mainyellow));

        //Restricting the lanscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init(); //Following is the method, to initialize all the objects with their respective ids

        //Setting up the recyler view
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        list=new ArrayList<>();

        fetchAllTheCustomOrders(); //Following is the method, that fetches all the 

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Now, following is the code to set up the searchBar
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            search(s.toString()); //Folllowing is the method, that will search for the resppective custom order
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void search(String toString) {

        Query searchQuery= FirebaseDatabase.getInstance().getReference().child("Custom Orders").orderByChild("orderid").startAt(toString).endAt(toString+"\uf8ff");

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
                        CustomOrders orders=dataSnapshot.getValue(CustomOrders.class);

                        list.add(orders);





                    }

                    Collections.reverse(list);
                    adapter=new CustomOrdersAdapter(list,getApplicationContext(), RecievedCustomOrders.this);
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

    private void fetchAllTheCustomOrders() {
        progressDialog = new ProgressDialog(RecievedCustomOrders.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show(); //Starting the progress dialog
        progressDialog.setContentView(R.layout.layout_loading);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        FirebaseDatabase.getInstance().getReference().child("Custom Orders")
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
                                CustomOrders customOrders=dataSnapshot.getValue(CustomOrders.class);
                                list.add(customOrders);
                            }
                        }


                        Collections.reverse(list);
                        adapter=new CustomOrdersAdapter(list,getApplicationContext(),RecievedCustomOrders.this);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        progressDialog.dismiss();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void init() {
        backBtn=findViewById(R.id.backBtn_customorders);
        searchBar=findViewById(R.id.searchBar_customorders);
        recyclerView=findViewById(R.id.recycler_customorders);
    }
}