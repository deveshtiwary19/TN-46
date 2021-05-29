package com.example.tn_46new.seller;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
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
import com.example.tn_46new.Buyer.BuyerHome;
import com.example.tn_46new.Buyer.BuyerLoginActivity;
import com.example.tn_46new.MainActivity;
import com.example.tn_46new.Models.Banners;
import com.example.tn_46new.Models.Products;
import com.example.tn_46new.Models.Sellers;
import com.example.tn_46new.NewsJobActivity;
import com.example.tn_46new.R;
import com.example.tn_46new.SearchActivity;
import com.example.tn_46new.SubcategoryActivity;
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

public class SellerHome extends AppCompatActivity {

    //Following are the objects on the xml file
    private ImageView hamburgerIcon;
    private ImageView addProductBtn;

    private EditText searchBar;

    private ImageView fashionCat;
    private ImageView groceryCat;
    private ImageView electronicCat;

    private NavigationView navMenu;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    private ImageView banner;
    private ImageView sponseredAdd;

    private RecyclerView electronicRecycler;
    private RecyclerView groceryRecycler;
    private RecyclerView fashionRecycler;

    //Following atre the objcet sfor the elect recycler
    private List<Products> listElec;
    private ElectronicHomeAdapter adapterElect;


    private List<Products> listGroc;
    private GroceryAdapterHome adapterGroc;

    private List<Products> listFashion;
    private FashionAdapterHome adapterFashion;

    
    private boolean SELLER_PERMISSION=false;

    //Following ar eth ethree see allbuttons
    private TextView seeall_elec;
    private TextView seeall_groc;
    private TextView seeall_fashion;
    
    

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);

        //Folllowing is the code to set the status bar as color
        Window window= SellerHome.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(SellerHome.this, R.color.mainyellow));

        //Restricting the lanscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init(); //Following is the method, that intializes all the objects with their respective ids

        checkForSellerRegistered();
        
        
        //Now we will show a bottom sheet dialog to choose the caategory under which
        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SELLER_PERMISSION)
                {
                    final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(SellerHome.this);
                    View dialog= LayoutInflater.from(SellerHome.this).inflate(R.layout.layout_bottomsheet_uploadcategory,null);

                    TextView elect=dialog.findViewById(R.id.electronicItems_choodeUploadCategory);
                    TextView grocery=dialog.findViewById(R.id.groceryItems_choodeUploadCategory);
                    TextView fashion=dialog.findViewById(R.id.fashionItems_choodeUploadCategory);

                    elect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.cancel();
                            Intent i=new Intent(getApplicationContext(),AddProduct.class);
                            i.putExtra("category","electronics");
                            i.putExtra("todo","add");
                            i.putExtra("pid","null");
                            startActivity(i);

                        }
                    });

                    grocery.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.cancel();
                            Intent i=new Intent(getApplicationContext(),AddProduct.class);
                            i.putExtra("category","grocery");
                            i.putExtra("todo","add");
                            i.putExtra("pid","null");
                            startActivity(i);
                        }
                    });

                    fashion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.cancel();
                            Intent i=new Intent(getApplicationContext(),AddProduct.class);
                            i.putExtra("category","fashion");
                            i.putExtra("todo","add");
                            i.putExtra("pid","null");
                            startActivity(i);
                        }
                    });



                    bottomSheetDialog.setContentView(dialog);
                    bottomSheetDialog.show();
                }
                else
                {
                    Dialog dialog = new Dialog(SellerHome.this);
                    dialog.setContentView(R.layout.layout_dialogbox);
                    dialog.setCancelable(false);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                    TextView text = dialog.findViewById(R.id.text_dialog);
                    TextView positive = dialog.findViewById(R.id.positive_dialog);
                    TextView negative = dialog.findViewById(R.id.negative_dialog);

                    text.setText("You are not registered as a seller yet. Please complete your registration to add products on TN-46.");

                    positive.setText("Ok");
                    negative.setText("Later");

                    negative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });

                    positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent i=new Intent(getApplicationContext(),SellerRegistrationAvtivity.class);
                            startActivity(i);

                            dialog.cancel();



                        }
                    });


                    dialog.show();





                }
            }
        });

        electronicCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), SubcategoryActivity.class);
                i.putExtra("user","seller");
                i.putExtra("category","e");
                startActivity(i);
            }
        });



        groceryCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), SubcategoryActivity.class);
                i.putExtra("user","seller");
                i.putExtra("category","g");
                startActivity(i);
            }
        });



        fashionCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), SubcategoryActivity.class);
                i.putExtra("user","seller");
                i.putExtra("category","f");
                startActivity(i);
            }
        });


        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(SellerHome.this);
                View dialog= LayoutInflater.from(SellerHome.this).inflate(R.layout.layout_search_cat,null);

                TextView e=dialog.findViewById(R.id.electronicItems_s);
                TextView g=dialog.findViewById(R.id.groceryItems_s);
                TextView f=dialog.findViewById(R.id.fashionItems_s);

                e.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(getApplicationContext(), SearchActivity.class);
                        i.putExtra("user","seller");
                        i.putExtra("type","e");
                        startActivity(i);
                        Objects.requireNonNull(SellerHome.this).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        bottomSheetDialog.cancel();
                    }
                });

                f.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(getApplicationContext(), SearchActivity.class);
                        i.putExtra("user","seller");
                        i.putExtra("type","f");
                        startActivity(i);
                        Objects.requireNonNull(SellerHome.this).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        bottomSheetDialog.cancel();
                    }
                });

                g.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(getApplicationContext(), SearchActivity.class);
                        i.putExtra("user","seller");
                        i.putExtra("type","g");
                        startActivity(i);
                        Objects.requireNonNull(SellerHome.this).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        bottomSheetDialog.cancel();
                    }
                });



                bottomSheetDialog.setContentView(dialog);
                bottomSheetDialog.show();

            }
        });

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

                if (menuItem.getItemId()== R.id.myRegistration_seller)
                {
                   Intent i=new Intent(getApplicationContext(),SellerRegistrationAvtivity.class);
                   startActivity(i);

                    drawerLayout.closeDrawer(GravityCompat.START);
                }

                else if (menuItem.getItemId()== R.id.aboutus_seller)
                {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent i=new Intent(getApplicationContext(), AboutUs.class);
                    startActivity(i);
                }
                else if (menuItem.getItemId()== R.id.contactus_seller)
                {
                    final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(SellerHome.this);
                    View dialog= LayoutInflater.from(SellerHome.this).inflate(R.layout.layout_contactus,null);




                    bottomSheetDialog.setContentView(dialog);
                    bottomSheetDialog.show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (menuItem.getItemId()== R.id.rateus_seller)
                {

                    Toast.makeText(SellerHome.this, "Rate Us", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (menuItem.getItemId()== R.id.news_seller)
                {

                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent i=new Intent(getApplicationContext(), NewsJobActivity.class);
                    i.putExtra("user","seller");
                    i.putExtra("type","news");
                    startActivity(i);
                }
                else if (menuItem.getItemId()== R.id.jobs_seller)
                {

                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent i=new Intent(getApplicationContext(), NewsJobActivity.class);
                    i.putExtra("user","seller");
                    i.putExtra("type","jobs");
                    startActivity(i);
                }
                else if (menuItem.getItemId()== R.id.logout_seller)
                {
                    drawerLayout.closeDrawer(GravityCompat.START);


                    Dialog dialog = new Dialog(SellerHome.this);
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

                            Intent i=new Intent(getApplicationContext(), SellerLoginActivity.class);
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



    }

    private void checkForSellerRegistered() {
        FirebaseDatabase.getInstance().getReference().child("Sellers").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            Sellers sellers=snapshot.getValue(Sellers.class);

                            if (sellers.getReg().equals("done"))
                            {
                                SELLER_PERMISSION=true;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void fetchAllTheFashionProducts() {

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
                            if (products.getSellerid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                            {
                                listFashion.add(products);
                            }

                        }

                        Collections.reverse(listFashion);
                        adapterFashion=new FashionAdapterHome(listFashion,getApplicationContext(),SellerHome.this,"seller");
                        fashionRecycler.setAdapter(adapterFashion);
                        adapterFashion.notifyDataSetChanged();



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
                            if (products.getSellerid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                            {
                                listGroc.add(products);
                            }

                        }

                        Collections.reverse(listGroc);
                        adapterGroc=new GroceryAdapterHome(listGroc,getApplicationContext(),SellerHome.this,"seller");
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
                            if (products.getSellerid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                            {
                                listElec.add(products);
                            }

                        }

                        Collections.reverse(listElec);
                        adapterElect=new ElectronicHomeAdapter(listElec,getApplicationContext(),SellerHome.this,"seller");
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
        hamburgerIcon=findViewById(R.id.hamburgericon_sellerHome);
        addProductBtn=findViewById(R.id.addProduct_sellerHome);

        searchBar=findViewById(R.id.searchBar_sellerHome);

        fashionCat=findViewById(R.id.fashioncat_sellerHome);
       groceryCat=findViewById(R.id.geroceriescat_sellerHome);
        electronicCat=findViewById(R.id.electronicscat_sellerHome);

        banner=findViewById(R.id.banner_sellerhome);
        sponseredAdd=findViewById(R.id.sponseredadd_sellerhome);

         electronicRecycler=findViewById(R.id.electronicrecycler_sellerhome);
         groceryRecycler=findViewById(R.id.groceriesrecycler_sellerhome);
         fashionRecycler=findViewById(R.id.fashionrecyclr_sellerhome);

         navMenu=findViewById(R.id.nav_seller);
         drawerLayout=findViewById(R.id.drawer_seller);

        seeall_elec=findViewById(R.id.seeall_seller_elec);
        seeall_groc=findViewById(R.id.seeall_seller_groc);
        seeall_elec=findViewById(R.id.seeall_seller_fash);
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