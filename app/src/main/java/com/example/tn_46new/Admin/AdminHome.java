package com.example.tn_46new.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
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
import com.example.tn_46new.Buyer.BuyerHome;
import com.example.tn_46new.Buyer.BuyerLoginActivity;
import com.example.tn_46new.Models.Banners;
import com.example.tn_46new.Models.Products;
import com.example.tn_46new.NewsJobActivity;
import com.example.tn_46new.OrdersActivity;
import com.example.tn_46new.R;
import com.example.tn_46new.SearchActivity;
import com.example.tn_46new.SubcategoryActivity;
import com.example.tn_46new.seller.AddProduct;
import com.example.tn_46new.seller.SellerHome;
import com.example.tn_46new.seller.SellerLoginActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AdminHome extends AppCompatActivity {

    //Following are the objects on the xml file
    private ImageView hamburgerIcon;


    private EditText searchBar;

    private ImageView fashionCat;
    private ImageView groceryCat;
    private ImageView electronicCat;

    private ImageView banner;
    private ImageView sponseredAdd;

    //Following ar eth ethree see allbuttons
    private TextView seeall_elec;
    private TextView seeall_groc;
    private TextView seeall_fashion;

    public static final String MY_PREFS_NAME = "AdminCred";

    //Following are the objecst for teh meenu
    private NavigationView navMenu;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

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


    private int RequestCode = 438;

    private String downloadImageUrl1 = "null";
    private Uri ImageUri1;

    private String downloadImageUrl2 = "null";
    private Uri ImageUri2;

    private int picNumber=0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        //Folllowing is the code to set the status bar as color
        Window window= AdminHome.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(AdminHome.this, R.color.mainyellow));

        //Restricting the lanscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init();


        electronicCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), SubcategoryActivity.class);
                i.putExtra("user","admin");
                i.putExtra("category","e");
                startActivity(i);
            }
        });



        groceryCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), SubcategoryActivity.class);
                i.putExtra("user","admin");
                i.putExtra("category","g");
                startActivity(i);
            }
        });



        fashionCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), SubcategoryActivity.class);
                i.putExtra("user","admin");
                i.putExtra("category","f");
                startActivity(i);
            }
        });



        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(AdminHome.this);
                View dialog= LayoutInflater.from(AdminHome.this).inflate(R.layout.layout_search_cat,null);

                TextView e=dialog.findViewById(R.id.electronicItems_s);
                TextView g=dialog.findViewById(R.id.groceryItems_s);
                TextView f=dialog.findViewById(R.id.fashionItems_s);

                e.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(getApplicationContext(), SearchActivity.class);
                        i.putExtra("user","admin");
                        i.putExtra("type","e");
                        startActivity(i);
                        Objects.requireNonNull(AdminHome.this).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        bottomSheetDialog.cancel();
                    }
                });

                f.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(getApplicationContext(), SearchActivity.class);
                        i.putExtra("user","admin");
                        i.putExtra("type","f");
                        startActivity(i);
                        Objects.requireNonNull(AdminHome.this).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        bottomSheetDialog.cancel();
                    }
                });

                g.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(getApplicationContext(), SearchActivity.class);
                        i.putExtra("user","admin");
                        i.putExtra("type","g");
                        startActivity(i);
                        Objects.requireNonNull(AdminHome.this).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        bottomSheetDialog.cancel();
                    }
                });



                bottomSheetDialog.setContentView(dialog);
                bottomSheetDialog.show();
            }
        });


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

                if (menuItem.getItemId()== R.id.customproduct_admin)
                {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent i=new Intent(getApplicationContext(),RecievedCustomOrders.class);
                    startActivity(i);


                }
                else if (menuItem.getItemId()== R.id.recievedOrders_admin)
                {
                    Intent i=new Intent(getApplicationContext(), OrdersActivity.class);
                    i.putExtra("type","admin");
                    startActivity(i);

                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (menuItem.getItemId()== R.id.aboutus_admin)
                {

                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent i=new Intent(getApplicationContext(), AboutUs.class);
                    startActivity(i);
                }
                else if (menuItem.getItemId()== R.id.contactus_admin)
                {
                    final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(AdminHome.this);
                    View dialog= LayoutInflater.from(AdminHome.this).inflate(R.layout.layout_contactus,null);




                    bottomSheetDialog.setContentView(dialog);
                    bottomSheetDialog.show();


                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (menuItem.getItemId()== R.id.rateus_admin)
                {

                    Toast.makeText(AdminHome.this, "Rate Us", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (menuItem.getItemId()== R.id.news_admin)
                {

                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent i=new Intent(getApplicationContext(), NewsJobActivity.class);
                    i.putExtra("user","admin");
                    i.putExtra("type","news");
                    startActivity(i);
                }
                else if (menuItem.getItemId()== R.id.jobs_admin)
                {

                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent i=new Intent(getApplicationContext(), NewsJobActivity.class);
                    i.putExtra("user","admin");
                    i.putExtra("type","jobs");
                    startActivity(i);
                }
                else if (menuItem.getItemId()== R.id.logout_admin)
                {

                    drawerLayout.closeDrawer(GravityCompat.START);


                    Dialog dialog = new Dialog(AdminHome.this);
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

                            //Putting the shared pref
                            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString("loggedin?","No");

                            Intent i=new Intent(getApplicationContext(), AdminLogin.class);
                            startActivity(i);
                            finish();


                            editor.apply();


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

        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picNumber=1;
                pickImage();
            }
        });

        sponseredAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picNumber=2;
                pickImage();
            }
        });

        //////////////////////////////////////////////////////////////////////////////////////////
        //Here, we need to ask for read and write external storage permission
        if (ContextCompat.checkSelfPermission(AdminHome.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            //Here, it means that location permission is not granted yet and we need to get it
            ActivityCompat.requestPermissions(AdminHome.this,new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            },100);

        }

        //////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////
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

                                listFashion.add(products);


                        }

                        Collections.reverse(listFashion);
                        adapterFashion=new FashionAdapterHome(listFashion,getApplicationContext(), AdminHome.this,"admin");
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

                                listGroc.add(products);


                        }

                        Collections.reverse(listGroc);
                        adapterGroc=new GroceryAdapterHome(listGroc,getApplicationContext(),AdminHome.this,"admin");
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
                        adapterElect=new ElectronicHomeAdapter(listElec,getApplicationContext(),AdminHome.this,"admin");
                        electronicRecycler.setAdapter(adapterElect);
                        adapterElect.notifyDataSetChanged();



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void init() {
        hamburgerIcon=findViewById(R.id.hamburgerIcon_adminhome);

        searchBar=findViewById(R.id.searchBar_adminHome);

        fashionCat=findViewById(R.id.fashionCat_adminhome);
        groceryCat=findViewById(R.id.groceriesCat_adminhome);
        electronicCat=findViewById(R.id.electronicsCat_adminhome);

        banner=findViewById(R.id.banner_adminhome);
        sponseredAdd=findViewById(R.id.sponseredAdd_adminhome);

        electronicRecycler=findViewById(R.id.electronisRecycler_adminhome);
        groceryRecycler=findViewById(R.id.geroceryrecycler_adminhome);
        fashionRecycler=findViewById(R.id.fashionRecycler_adminhome);

        navMenu=findViewById(R.id.nav_admin);
        drawerLayout=findViewById(R.id.drawer_admin);

        seeall_elec=findViewById(R.id.seeall_admin_elec);
        seeall_groc=findViewById(R.id.seeall_admin_groc);
        seeall_elec=findViewById(R.id.seeall_admin_fash);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==100)
        {
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {

            }
            else
            {
                ActivityCompat.requestPermissions(AdminHome.this,new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE
                },100);
            }
        }


    }

    private void pickImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, RequestCode);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == RequestCode && resultCode == Activity.RESULT_OK && data.getData() != null) {

            if (picNumber==1)
            {
                ImageUri1 = data.getData();
                uploadImage(ImageUri1);
            }
            else
            {
                ImageUri2 = data.getData();
                uploadImage(ImageUri2);
            }






        } else {

            makeToast("Image Not Selected",0);
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadImage(Uri imageUri) {

        progressDialog = new ProgressDialog(AdminHome.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show(); //Starting the progress dialog
        progressDialog.setContentView(R.layout.layout_loading);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        final String productRandomKey = Calendar.getInstance().getTime().toString();


        final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("Item Images").child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                makeToast(uploadTask.getException().toString(), 1);
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();

                        }

                        if (picNumber == 1) {
                            downloadImageUrl1 = filePath.getDownloadUrl().toString();
                        }
                        if (picNumber == 2) {
                            downloadImageUrl2 = filePath.getDownloadUrl().toString();
                        }

                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (picNumber == 1) {
                            downloadImageUrl1 = task.getResult().toString();

                           DatabaseReference ref1= FirebaseDatabase.getInstance().getReference().child("Banners");
                            Map<String,Object>map=new HashMap<>();
                            map.put("Banner",downloadImageUrl1);

                            ref1.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        progressDialog.dismiss();
                                        makeToast("Banner Uploaded",0);
                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        makeToast(task.getException().toString(),0);
                                    }
                                }
                            });

                        }
                        if (picNumber == 2) {
                            downloadImageUrl2 = task.getResult().toString();


                            DatabaseReference ref1= FirebaseDatabase.getInstance().getReference().child("Banners");
                            Map<String,Object>map=new HashMap<>();
                            map.put("Add",downloadImageUrl2);

                            ref1.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        progressDialog.dismiss();
                                        makeToast("Sponsered advertisement Uploaded",0);
                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        makeToast(task.getException().toString(),0);
                                    }
                                }
                            });
                        }


                    }
                });
            }


        });
    }

    public void makeToast(String textToShow, int lenght)
    {
        LayoutInflater inflater = AdminHome.this.getLayoutInflater();
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