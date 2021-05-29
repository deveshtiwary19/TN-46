package com.example.tn_46new;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.tn_46new.Adapters.ProductsAdapter;
import com.example.tn_46new.Models.Banners;
import com.example.tn_46new.Models.Products;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    //Following are objects on the screen
    private ImageView backBtn;
    private EditText searchEditTxt;
    private ImageView sponseredAdd;
    private RecyclerView recyclerView;

    private String C_TO_SEND="";

    private String USer_Type="";

    private List<Products> list;
    private ProductsAdapter adapter;

    private RadioGroup radGrp;
    private RadioButton radBtnElec;

    private String categoryToSearch;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent i=getIntent();
        USer_Type=i.getStringExtra("user");
        categoryToSearch=i.getStringExtra("type");

        //Folllowing is the code to set the status bar as color
        Window window= SearchActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(SearchActivity.this, R.color.mainyellow));

        //Restricting the landscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init(); //Following is the method, to assign all the objects wit their respective ids

        radBtnElec.setChecked(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        list=new ArrayList<>();


        radGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.radBtn_electronics_search)
                {
                    categoryToSearch="e";
                    fetchAllItemsAndSet();
                }
                else if (checkedId==R.id.radBtn_fashion_search)
                {
                    categoryToSearch="f";
                    fetchAllItemsAndSet();
                }
                else if (checkedId==R.id.radBtn_grocery_search)
                {
                    categoryToSearch="g";
                    fetchAllItemsAndSet();
                }
            }
        });


        //Getting the focus on the serach bar at starting
        searchEditTxt.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchEditTxt, InputMethodManager.SHOW_IMPLICIT);

        searchEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchFor(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        fetchAllItemsAndSet();




        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fetchTheSponseredAdd(); //Following is the method, to fetch the sponsered add



    }

    private void fetchAllItemsAndSet() {
        Query ref=null;

        if (categoryToSearch.equals("e"))
        {
            ref= FirebaseDatabase.getInstance().getReference().child("Products").child("Electronics");
            C_TO_SEND="electronics";

        }
        else if (categoryToSearch.equals("g"))
        {
            ref= FirebaseDatabase.getInstance().getReference().child("Products").child("Grocery").orderByChild("search");
            C_TO_SEND="grocery";

        }
        else if (categoryToSearch.equals("f"))
        {
            ref= FirebaseDatabase.getInstance().getReference().child("Products").child("Fashion").orderByChild("search");
            C_TO_SEND="fashion";

        }

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(list!=null)
                {
                    list.clear();
                }

              if (snapshot.exists())
              {
                  for (DataSnapshot dataSnapshot:snapshot.getChildren())
                  {
                      Products products=dataSnapshot.getValue(Products.class);
                      list.add(products);

                  }


                  Collections.reverse(list);
                  adapter=new ProductsAdapter(list,getApplicationContext(), SearchActivity.this,USer_Type,C_TO_SEND);
                  recyclerView.setAdapter(adapter);
                  adapter.notifyDataSetChanged();


              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searchFor(String toLowerCase) {

        Query searchQuery=null;

        if (categoryToSearch.equals("e"))
        {
             searchQuery= FirebaseDatabase.getInstance().getReference().child("Products").child("Electronics").orderByChild("search").startAt(toLowerCase).endAt(toLowerCase+"\uf8ff");
             C_TO_SEND="electronics";

        }
        else if (categoryToSearch.equals("g"))
        {
            searchQuery= FirebaseDatabase.getInstance().getReference().child("Products").child("Grocery").orderByChild("search").startAt(toLowerCase).endAt(toLowerCase+"\uf8ff");
            C_TO_SEND="grocery";

        }
        else if (categoryToSearch.equals("f"))
        {
            searchQuery= FirebaseDatabase.getInstance().getReference().child("Products").child("Fashion").orderByChild("search").startAt(toLowerCase).endAt(toLowerCase+"\uf8ff");
            C_TO_SEND="fashion";

        }

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
                        Products products=dataSnapshot.getValue(Products.class);
                        list.add(products);
                    }

                    Collections.reverse(list);
                    adapter=new ProductsAdapter(list,getApplicationContext(), SearchActivity.this,USer_Type,C_TO_SEND);
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

    private void fetchTheSponseredAdd() {
        FirebaseDatabase.getInstance().getReference().child("Banners")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            Banners banners=snapshot.getValue(Banners.class);

                            Picasso.get().load(banners.getAdd()).placeholder(R.drawable.placeholder).into(sponseredAdd);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void init() {
        backBtn=findViewById(R.id.backBtn_searchProduct);
        searchEditTxt=findViewById(R.id.searchBox_searchProduct);
        sponseredAdd=findViewById(R.id.sponseredAdd_searchActivity);
        recyclerView=findViewById(R.id.recycler_searchActivity);

        radGrp=findViewById(R.id.radGrp_searchActivity);
        radBtnElec=findViewById(R.id.radBtn_electronics_search);
    }
}