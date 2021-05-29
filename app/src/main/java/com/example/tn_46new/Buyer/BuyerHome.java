package com.example.tn_46new.Buyer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tn_46new.AboutUs;
import com.example.tn_46new.Adapters.ElectronicHomeAdapter;
import com.example.tn_46new.Adapters.FashionAdapterHome;
import com.example.tn_46new.Adapters.GroceryAdapterHome;
import com.example.tn_46new.Admin.AdminHome;
import com.example.tn_46new.MainActivity;
import com.example.tn_46new.Models.Banners;
import com.example.tn_46new.Models.Products;
import com.example.tn_46new.NewsJobActivity;
import com.example.tn_46new.OrdersActivity;
import com.example.tn_46new.R;
import com.example.tn_46new.SearchActivity;
import com.example.tn_46new.SubcategoryActivity;
import com.example.tn_46new.seller.SellerHome;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BuyerHome extends AppCompatActivity {

    //Following are the objects on the xml file
    private ImageView hamburgerIcon;
    private ImageView cartBtn;

    private EditText searchBar;

    //Following are the objecst for teh meenu
    private NavigationView navMenu;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    private ImageView fashionCat;
    private ImageView groceryCat;
    private ImageView electronicCat;

    private ImageView banner;
    private ImageView sponseredAdd;

    private RecyclerView electronicRecycler;
    private RecyclerView groceryRecycler;
    private RecyclerView fashionRecycler;


    private List<Products> listElec;
    private ElectronicHomeAdapter adapterElect;

    private ProgressDialog progressDialog;

    private List<Products> listGroc;
    private GroceryAdapterHome adapterGroc;

    private List<Products> listFashion;
    private FashionAdapterHome adapterFashion;

    //Following ar eth ethree see allbuttons
    private TextView seeall_elec;
    private TextView seeall_groc;
    private TextView seeall_fashion;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_home);

        //Folllowing is the code to set the status bar as color
        Window window= BuyerHome.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(BuyerHome.this, R.color.mainyellow));

        //Restricting the lanscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init();//Following is the method, to intialize all the objects with their respective is


         //Setting up the electronic recycler for home
        electronicRecycler.setHasFixedSize(true);
        electronicRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        listElec=new ArrayList<>();
        fetchAllTheElectrnocProducts(); //Following is the method, to getch all the electronic products
        ////////////////////////////////////////////////////
        ///////////////////////////////////////////////////


        //Setting up the grocery recycler for home
        groceryRecycler.setHasFixedSize(true);
        groceryRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        listGroc=new ArrayList<>();
        fetchAllTheGroceryProducts(); //Following is the method, to getch all the gricery products
        ////////////////////////////////////////////////////
        ///////////////////////////////////////////////////


        //Setting up the grocery recycler for home
        fashionRecycler.setHasFixedSize(true);
        fashionRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        listFashion=new ArrayList<>();
        fetchAllTheFashionProducts(); //Following is the method, to getch all the fashion products
        ////////////////////////////////////////////////////
        ///////////////////////////////////////////////////


        fetchBannerAndAdd();



        //Setting up teh menu
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout,
                R.string.nav_open, R.string.nav_close
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.customproduct_sellerMenu) {
                   Intent i=new Intent(getApplicationContext(),CustomProductRequest.class);
                   startActivity(i);

                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (menuItem.getItemId() == R.id.myOrders_sellerMenu) {

                    Intent i=new Intent(getApplicationContext(), OrdersActivity.class);
                    i.putExtra("type","buyer");
                    startActivity(i);

                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (menuItem.getItemId() == R.id.aboutus_sellerMenu) {

                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent i=new Intent(getApplicationContext(), AboutUs.class);
                    startActivity(i);
                } else if (menuItem.getItemId() == R.id.contactus_sellerMenu) {
                    final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(BuyerHome.this);
                    View dialog= LayoutInflater.from(BuyerHome.this).inflate(R.layout.layout_contactus,null);




                    bottomSheetDialog.setContentView(dialog);
                    bottomSheetDialog.show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (menuItem.getItemId() == R.id.rateus_sellerMenu) {

                    Toast.makeText(BuyerHome.this, "Rate Us", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (menuItem.getItemId() == R.id.news_sellerMenu) {

                    drawerLayout.closeDrawer(GravityCompat.START);

                    Intent i=new Intent(getApplicationContext(), NewsJobActivity.class);
                    i.putExtra("user","buyer");
                    i.putExtra("type","news");
                    startActivity(i);

                } else if (menuItem.getItemId() == R.id.jobs_sellerMenu) {


                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent i=new Intent(getApplicationContext(), NewsJobActivity.class);
                    i.putExtra("user","buyer");
                    i.putExtra("type","jobs");
                    startActivity(i);

                } else if (menuItem.getItemId() == R.id.logout_sellerMenu) {

                    drawerLayout.closeDrawer(GravityCompat.START);


                    Dialog dialog = new Dialog(BuyerHome.this);
                    dialog.setContentView(R.layout.layout_dialogbox);
                    dialog.setCancelable(true);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                    TextView text = dialog.findViewById(R.id.text_dialog);
                    TextView positive = dialog.findViewById(R.id.positive_dialog);
                    TextView negative = dialog.findViewById(R.id.negative_dialog);

                    text.setText("Are you sure you want to logout?");

                    positive.setText("Yes");
                    negative.setText("No");

                    negative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });

                    positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            FirebaseAuth.getInstance().signOut();

                            Intent i=new Intent(getApplicationContext(),BuyerLoginActivity.class);
                            startActivity(i);
                            finish();


                        }
                    });


                    dialog.show();
                }












                return true;
            }
        });


        hamburgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });







        electronicCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), SubcategoryActivity.class);
                i.putExtra("user","buyer");
                i.putExtra("category","e");
                startActivity(i);
            }
        });



        groceryCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), SubcategoryActivity.class);
                i.putExtra("user","buyer");
                i.putExtra("category","g");
                startActivity(i);
            }
        });



        fashionCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), SubcategoryActivity.class);
                i.putExtra("user","buyer");
                i.putExtra("category","f");
                startActivity(i);
            }
        });




        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(BuyerHome.this);
                View dialog= LayoutInflater.from(BuyerHome.this).inflate(R.layout.layout_search_cat,null);

                TextView e=dialog.findViewById(R.id.electronicItems_s);
                TextView g=dialog.findViewById(R.id.groceryItems_s);
                TextView f=dialog.findViewById(R.id.fashionItems_s);

                e.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(getApplicationContext(), SearchActivity.class);
                        i.putExtra("user","buyer");
                        i.putExtra("type","e");
                        startActivity(i);
                        Objects.requireNonNull(BuyerHome.this).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        bottomSheetDialog.cancel();
                    }
                });

                f.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(getApplicationContext(), SearchActivity.class);
                        i.putExtra("user","buyer");
                        i.putExtra("type","f");
                        startActivity(i);
                        Objects.requireNonNull(BuyerHome.this).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        bottomSheetDialog.cancel();
                    }
                });

                g.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(getApplicationContext(), SearchActivity.class);
                        i.putExtra("user","buyer");
                        i.putExtra("type","g");
                        startActivity(i);
                        Objects.requireNonNull(BuyerHome.this).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        bottomSheetDialog.cancel();
                    }
                });



                bottomSheetDialog.setContentView(dialog);
                bottomSheetDialog.show();



            }
        });


        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),CartActivity.class);
                startActivity(i);
            }
        });


    }

    private void fetchAllTheFashionProducts() {

        progressDialog = new ProgressDialog(BuyerHome.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show(); //Starting the progress dialog
        progressDialog.setContentView(R.layout.layout_loading);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        FirebaseDatabase.getInstance().getReference().child("Products").child("Fashion")
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {




                        if (listFashion!=null)
                        {
                            listFashion.clear();
                        }

                        for (DataSnapshot dataSnapshot:snapshot.getChildren())
                        {
                            Products products=dataSnapshot.getValue(Products.class);

                            listFashion.add(products);


                        }

                        Collections.reverse(listFashion);
                        adapterFashion=new FashionAdapterHome(listFashion,getApplicationContext(), BuyerHome.this,"buyer");
                        fashionRecycler.setAdapter(adapterFashion);
                        adapterFashion.notifyDataSetChanged();

                        progressDialog.dismiss();



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void fetchAllTheGroceryProducts() {

        FirebaseDatabase.getInstance().getReference().child("Products").child("Grocery")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (listGroc!=null)
                        {
                            listGroc.clear();
                        }

                        for (DataSnapshot dataSnapshot:snapshot.getChildren())
                        {
                            Products products=dataSnapshot.getValue(Products.class);

                            listGroc.add(products);


                        }

                        Collections.reverse(listGroc);
                        adapterGroc=new GroceryAdapterHome(listGroc,getApplicationContext(),BuyerHome.this,"buyer");
                        groceryRecycler.setAdapter(adapterGroc);
                        adapterGroc.notifyDataSetChanged();



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void fetchAllTheElectrnocProducts() {
        FirebaseDatabase.getInstance().getReference().child("Products").child("Electronics")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (listElec!=null)
                        {
                            listElec.clear();
                        }

                        for (DataSnapshot dataSnapshot:snapshot.getChildren())
                        {
                            Products products=dataSnapshot.getValue(Products.class);

                            listElec.add(products);


                        }

                        Collections.reverse(listElec);
                        adapterElect=new ElectronicHomeAdapter(listElec,getApplicationContext(),BuyerHome.this,"buyer");
                        electronicRecycler.setAdapter(adapterElect);
                        adapterElect.notifyDataSetChanged();



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void fetchBannerAndAdd() {
        FirebaseDatabase.getInstance().getReference().child("Banners")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            Banners banners=snapshot.getValue(Banners.class);

                            Picasso.get().load(banners.getBanner()).placeholder(R.drawable.placeholder).into(banner);
                            Picasso.get().load(banners.getAdd()).placeholder(R.drawable.placeholder).into(sponseredAdd);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void init() {
        hamburgerIcon=findViewById(R.id.hamburgericon_buyer);
        cartBtn=findViewById(R.id.cart_buyer);

        searchBar=findViewById(R.id.searchBar_buyer);

        fashionCat=findViewById(R.id.fashionCat_buyer);
        groceryCat=findViewById(R.id.geroceryCat_buyer);
        electronicCat=findViewById(R.id.electroonicCat_buyer);

        banner=findViewById(R.id.banner_buyer);
        sponseredAdd=findViewById(R.id.sponseredAdd_buyer);

        electronicRecycler=findViewById(R.id.electronicrecycler_buyer);
        groceryRecycler=findViewById(R.id.geroceryRecycler_buyer);
        fashionRecycler=findViewById(R.id.fashionrecycler_buyer);

        navMenu=findViewById(R.id.nav_buyer);
        drawerLayout=findViewById(R.id.drawer_buyer);


        seeall_elec=findViewById(R.id.seeall_buyer_elec);
        seeall_groc=findViewById(R.id.seeall_buyer_groc);
        seeall_elec=findViewById(R.id.seeall_buyer_fashion);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }


    }


}