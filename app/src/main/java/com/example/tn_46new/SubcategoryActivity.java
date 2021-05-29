package com.example.tn_46new;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tn_46new.Adapters.ProductsAdapter;
import com.example.tn_46new.Models.Products;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubcategoryActivity extends AppCompatActivity {

    private ImageView backBtn;
    private TextView storeTitle;
    private TextView subcategory;
    private RecyclerView recyclerView;

    //Following are the adapters and list
    private List<Products> list;
    private ProductsAdapter adapter;

    private String user="";
    private String category="";


    private String C_TO_SEND="";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory);

        //Folllowing is the code to set the status bar as color
        Window window= SubcategoryActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(SubcategoryActivity.this, R.color.mainyellow));

        //Restricting the lanscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent i=getIntent();
        user=i.getStringExtra("user");
        category=i.getStringExtra("category");

        init(); //Following is the method to initialize all the objects with their respective ids

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Setting up the list and the recycler view
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setNestedScrollingEnabled(false);
        list=new ArrayList<>();

        if (category.equals("e"))
        {
            storeTitle.setText("Electronics Store");
        }
        else if (category.equals("g"))
        {
            storeTitle.setText("Grocery Store");
        }
        else if (category.equals("f"))
        {
            storeTitle.setText("Fashion Store");
        }

        fetchAllTheItems(); //Following is the method, that will fetch all the categories under the respective category


        //Now we have to set a bottom sheet dialog, which will show all the subcategories and fetch the data accordingly
        subcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (category.equals("e"))
                {

                    //Here, it means that the category is open with electronics, so we have to show and fetch all the electrnic subcategory
                    final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(SubcategoryActivity.this);
                    View dialog= LayoutInflater.from(SubcategoryActivity.this).inflate(R.layout.layout_subcategory_electronics,null);

                    TextView mobiles=dialog.findViewById(R.id.moblie_elecSubBottom);
                    TextView pcandlaptops=dialog.findViewById(R.id.pcandlaptop_elecSubBottom);
                    TextView homeAppliances=dialog.findViewById(R.id.homeAppliance_elecSubBottom);
                    TextView accesories=dialog.findViewById(R.id.accesories_elecSubBottom);
                    TextView electrical=dialog.findViewById(R.id.electrical_elecSubBottom);
                    TextView furniture=dialog.findViewById(R.id.furniture_elecSubBottom);
                    TextView refurbished=dialog.findViewById(R.id.refurbished_elecSubBottom);
                    TextView others=dialog.findViewById(R.id.others_elecSubBottom);


                    mobiles.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.cancel();
                            fetchUnderElectronics("mobiles"); //Following is the method, which will fetch only passed subcayegory and set to the recylcer view
                            subcategory.setText("Mobiles");
                        }
                    });

                    pcandlaptops.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.cancel();
                            fetchUnderElectronics("pandl"); //Following is the method, which will fetch only passed subcayegory and set to the recylcer view
                            subcategory.setText("PC and Laptops");
                        }
                    });

                    homeAppliances.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.cancel();
                            fetchUnderElectronics("homeappliance"); //Following is the method, which will fetch only passed subcayegory and set to the recylcer view
                            subcategory.setText("Home Appliances");
                        }
                    });

                    accesories.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.cancel();
                            fetchUnderElectronics("accesories"); //Following is the method, which will fetch only passed subcayegory and set to the recylcer view
                            subcategory.setText("Accesories");
                        }
                    });

                    electrical.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.cancel();
                            fetchUnderElectronics("electrical"); //Following is the method, which will fetch only passed subcayegory and set to the recylcer view
                            subcategory.setText("Electrical");
                        }
                    });

                    furniture.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.cancel();
                            fetchUnderElectronics("furniture"); //Following is the method, which will fetch only passed subcayegory and set to the recylcer view
                            subcategory.setText("Furnitures");
                        }
                    });

                    refurbished.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.cancel();
                            fetchUnderElectronics("refurbished"); //Following is the method, which will fetch only passed subcayegory and set to the recylcer view
                            subcategory.setText("Refurbished");
                        }
                    });

                    others.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.cancel();
                            fetchUnderElectronics("others"); //Following is the method, which will fetch only passed subcayegory and set to the recylcer view
                            subcategory.setText("Others");
                        }
                    });



                    bottomSheetDialog.setContentView(dialog);
                    bottomSheetDialog.show();
                }


                if (category.equals("g"))
                {
                    //Here, it means that the category is open with electronics, so we have to show and fetch all the electrnic subcategory
                    final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(SubcategoryActivity.this);
                    View dialog= LayoutInflater.from(SubcategoryActivity.this).inflate(R.layout.layout_subcategory_grocery,null);

                    TextView cosmetics=dialog.findViewById(R.id.cosmetic_grocSubBottom);
                    TextView masala=dialog.findViewById(R.id.masala_grocSubBottom);
                    TextView flour=dialog.findViewById(R.id.flour_grocSubBottom);
                    TextView rice=dialog.findViewById(R.id.rice_grocSubBottom);
                    TextView healthProduct=dialog.findViewById(R.id.healthProduct_grocSubBottom);
                    TextView genralCleaning=dialog.findViewById(R.id.genralCleaning_grocSubBottom);
                    TextView mensWomensBeauty=dialog.findViewById(R.id.mensAndWomensBeauty_grocSubBottom);


                    cosmetics.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.cancel();
                            fetchUnderGrocery("cosmetics"); //Following is the method, which will fetch only passed subcayegory and set to the recylcer view
                            subcategory.setText("Cosmetics");
                        }
                    });

                    masala.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.cancel();
                            fetchUnderGrocery("masalas"); //Following is the method, which will fetch only passed subcayegory and set to the recylcer view
                            subcategory.setText("Masalas");
                        }
                    });

                    flour.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.cancel();
                            fetchUnderGrocery("flours"); //Following is the method, which will fetch only passed subcayegory and set to the recylcer view
                            subcategory.setText("Flours");
                        }
                    });

                    rice.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.cancel();
                            fetchUnderGrocery("rice"); //Following is the method, which will fetch only passed subcayegory and set to the recylcer view
                            subcategory.setText("Rice");
                        }
                    });

                    healthProduct.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.cancel();
                            fetchUnderGrocery("healthproduct"); //Following is the method, which will fetch only passed subcayegory and set to the recylcer view
                            subcategory.setText("Health Product");
                        }
                    });

                    genralCleaning.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.cancel();
                            fetchUnderGrocery("genralandcleaninng"); //Following is the method, which will fetch only passed subcayegory and set to the recylcer view
                            subcategory.setText("Genral and Cleaning");
                        }
                    });

                    mensWomensBeauty.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.cancel();
                            fetchUnderGrocery("menswomensbeauty"); //Following is the method, which will fetch only passed subcayegory and set to the recylcer view
                            subcategory.setText("Men's and Women's Beauty");
                        }
                    });




                    bottomSheetDialog.setContentView(dialog);
                    bottomSheetDialog.show();

                }

                if (category.equals("f"))
                {
                    //Here, it means that the category is open with electronics, so we have to show and fetch all the electrnic subcategory
                    final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(SubcategoryActivity.this);
                    View dialog= LayoutInflater.from(SubcategoryActivity.this).inflate(R.layout.layout_subcategory_fashion,null);

                    TextView menswear=dialog.findViewById(R.id.menswear_fashionSubBottom);
                    TextView womenswear=dialog.findViewById(R.id.womenswear_fashionSubBottom);
                    TextView sportswear=dialog.findViewById(R.id.sportswear_fashionSubBottom);
                    TextView footwear=dialog.findViewById(R.id.footwear_fashionSubBottom);
                    TextView bandl=dialog.findViewById(R.id.bandl_fashionSubBottom);
                    TextView gifts=dialog.findViewById(R.id.gifts_fashionSubBottom);
                    TextView jwellers=dialog.findViewById(R.id.jwellers_fashionSubBottom);
                    TextView tailorwear=dialog.findViewById(R.id.tailorwears_fashionSubBottom);
                    TextView others=dialog.findViewById(R.id.others_fashionSubBottom);

                    menswear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.cancel();
                            fetchUnderFashion("menswear"); //Following is the method, which will fetch only passed subcayegory and set to the recylcer view
                            subcategory.setText("Menswear");
                        }
                    });

                    womenswear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.cancel();
                            fetchUnderFashion("womenswear"); //Following is the method, which will fetch only passed subcayegory and set to the recylcer view
                            subcategory.setText("Womens Wear");
                        }
                    });

                    sportswear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.cancel();
                            fetchUnderFashion("sportswear"); //Following is the method, which will fetch only passed subcayegory and set to the recylcer view
                            subcategory.setText("Sports Wear");
                        }
                    });

                    footwear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.cancel();
                            fetchUnderFashion("footwear"); //Following is the method, which will fetch only passed subcayegory and set to the recylcer view
                            subcategory.setText("Foot Wear");
                        }
                    });

                    bandl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.cancel();
                            fetchUnderFashion("bandl"); //Following is the method, which will fetch only passed subcayegory and set to the recylcer view
                            subcategory.setText("Bags and Luggages");
                        }
                    });

                    gifts.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.cancel();
                            fetchUnderFashion("gifts"); //Following is the method, which will fetch only passed subcayegory and set to the recylcer view
                            subcategory.setText("Gifts");
                        }
                    });

                    jwellers.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.cancel();
                            fetchUnderFashion("jwellers"); //Following is the method, which will fetch only passed subcayegory and set to the recylcer view
                            subcategory.setText("Jwellers");
                        }
                    });

                    tailorwear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.cancel();
                            fetchUnderFashion("tailorwear"); //Following is the method, which will fetch only passed subcayegory and set to the recylcer view
                            subcategory.setText("Tailor Wear");
                        }
                    });

                    others.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.cancel();
                            fetchUnderFashion("others"); //Following is the method, which will fetch only passed subcayegory and set to the recylcer view
                            subcategory.setText("Others");
                        }
                    });





                    bottomSheetDialog.setContentView(dialog);
                    bottomSheetDialog.show();

                }

            }
        });




















    }

    private void fetchUnderFashion(String sub) {
        FirebaseDatabase.getInstance().getReference().child("Products").child("Fashion")
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
                                Products products=dataSnapshot.getValue(Products.class);
                                if (products.getSubcategory().equals(sub))
                                {
                                    list.add(products);
                                }
                            }

                            Collections.reverse(list);
                            adapter=new ProductsAdapter(list,getApplicationContext(), SubcategoryActivity.this,user,C_TO_SEND);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void fetchUnderGrocery(String sub) {
        FirebaseDatabase.getInstance().getReference().child("Products").child("Grocery")
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
                                Products products=dataSnapshot.getValue(Products.class);
                                if (products.getSubcategory().equals(sub))
                                {
                                    list.add(products);
                                }
                            }

                            Collections.reverse(list);
                            adapter=new ProductsAdapter(list,getApplicationContext(), SubcategoryActivity.this,user,C_TO_SEND);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void fetchUnderElectronics(String sub) {

        FirebaseDatabase.getInstance().getReference().child("Products").child("Electronics")
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
                                Products products=dataSnapshot.getValue(Products.class);
                                if (products.getSubcategory().equals(sub))
                                {
                                    list.add(products);
                                }
                            }

                            Collections.reverse(list);
                            adapter=new ProductsAdapter(list,getApplicationContext(), SubcategoryActivity.this,user,C_TO_SEND);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void fetchAllTheItems() {
        DatabaseReference reference=null;
        if (category.equals("e"))
        {
           reference= FirebaseDatabase.getInstance().getReference().child("Products").child("Electronics");
           C_TO_SEND="electronics";
        }
        else if (category.equals("g"))
        {
            reference= FirebaseDatabase.getInstance().getReference().child("Products").child("Grocery");
            C_TO_SEND="grocery";
        }
        else if (category.equals("f"))
        {
            reference= FirebaseDatabase.getInstance().getReference().child("Products").child("Fashion");
            C_TO_SEND="fashion";
        }

        reference.addValueEventListener(new ValueEventListener() {
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
                        Products products=dataSnapshot.getValue(Products.class);
                        list.add(products);
                    }

                    Collections.reverse(list);
                    adapter=new ProductsAdapter(list,getApplicationContext(), SubcategoryActivity.this,user,C_TO_SEND);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void init() {
        backBtn=findViewById(R.id.backBtn_subCategoryActivity);
        storeTitle=findViewById(R.id.storeTitle_subCategoryActivity);
        subcategory=findViewById(R.id.subcategory_subCategoryActivity);
        recyclerView=findViewById(R.id.recycler_subCategoryActivity);
    }
}