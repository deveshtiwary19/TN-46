package com.example.tn_46new.seller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tn_46new.Admin.AdminHome;
import com.example.tn_46new.MainActivity;
import com.example.tn_46new.Models.Products;
import com.example.tn_46new.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddProduct extends AppCompatActivity {

    //Following are the  objects
    private LinearLayout electronicVariants;
    private LinearLayout groceryVariants;
    private LinearLayout fashionVariants;

    private LinearLayout electronicSubcategory;
    private LinearLayout grocerySubcategory;
    private LinearLayout fashionSubcategory;

    private TextView title;
    private ImageView backBtn;

    private Button addProductBtn;

    private ImageView imageOne;
    private ImageView imagetwo;
    private ImageView  imagethree;
    private ImageView imagefour;

    private EditText nameEtxt;
    private EditText priceEtxt;
    private EditText descriEtxt;

    private ProgressDialog progressDialog;

    //Now, we need variables for all the subcategory
    private String fashion_sub="";
    private String electrical_sub="";
    private String grocery_sub="";

    //Following aree the three radGroups
    private RadioGroup electroniRadGrp;
    private RadioGroup groceryradGrp;
    private RadioGroup fashionRadGrp;

    //Now following are all the variables required to store the avilable variants

    //For the electrical category
    private String twoGb="no";
    private String threeGb="no";
    private String fourGb="no";
    private String sixGb="no";
    private String eightGb="no";
    private String othersElec="no";

    //For the grocery category
    private String fifty="no";
    private String hundred="no";
    private String twohundred="no";
    private String twofifty="no";
    private String fivehundred="no";
    private String onekg="no";
    private String othersGroc="no";

    //For the fashion category
    private String s="no";
    private String m="no";
    private String l="no";
    private String xl="no";
    private String xxl="no";
    private String freesize="no";
    private String othersFashion="no";


    //Following are the buttons to choose the variants avilable for the products according to the category
    //For the electrical category
    private Button twoGbBtn;
    private Button threeGbBtn;
    private Button fourGbBtn;
    private Button sixGbBtn;
    private Button eightGbBtn;
    private Button othersElecBtn;

    //For the grocery category
    private Button fiftyBtn;
    private Button hundredBtn;
    private Button twohundredBtn;
    private Button twofiftyBtn;
    private Button fivehundredBtn;
    private Button onekgBtn;
    private Button othersGrocBtn;

    //For the fashion category
    private Button sBtn;
    private Button mBtn;
    private Button lBtn;
    private Button xlBtn;
    private Button xxlBtn;
    private Button freesizeBtn;
    private Button othersFashionBtn;

    private int RequestCode = 438;

    private String downloadImageUrl1 = "null";
    private Uri ImageUri1;

    private String downloadImageUrl2 = "null";
    private Uri ImageUri2;

    private String downloadImageUrl3 = "null";
    private Uri ImageUri3;

    private String downloadImageUrl4 = "null";
    private Uri ImageUri4;


    private int picNumber=0; //Following is the variable that stores the picture number clicked to choose





    private String CATEGORY_TO_ADD="none";
    private String TO_DO="none";
    private String pid="none";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        //Folllowing is the code to set the status bar as color
        Window window= AddProduct.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(AddProduct.this, R.color.mainyellow));

        //Restricting the lanscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //GEttig the product category and to do opertion from intent
        Intent i=getIntent();
        CATEGORY_TO_ADD=i.getStringExtra("category");
        TO_DO=i.getStringExtra("todo");
        pid=i.getStringExtra("pid");

        init(); //FOllowing is teh method,  that assigns all the objets with their respective ids

        //////////////////////////////////////////////////////////////////////////////////////////
        //Here, we need to ask for read and write external storage permission
        if (ContextCompat.checkSelfPermission(AddProduct.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            //Here, it means that location permission is not granted yet and we need to get it
            ActivityCompat.requestPermissions(AddProduct.this,new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            },100);

        }

        //////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////

        //Making the layouts visible according to the given category
        if (CATEGORY_TO_ADD.equals("electronics"))
        {
            electronicSubcategory.setVisibility(View.VISIBLE);
            electronicVariants.setVisibility(View.VISIBLE);
            title.setText("You are adding a product under\nElectronics Category");
        }
        else if (CATEGORY_TO_ADD.equals("grocery"))
        {
            grocerySubcategory.setVisibility(View.VISIBLE);
            groceryVariants.setVisibility(View.VISIBLE);
            title.setText("You are adding a product under\nGrocery Category");
        }
        else if (CATEGORY_TO_ADD.equals("fashion"))
        {
            fashionSubcategory.setVisibility(View.VISIBLE);
            fashionVariants.setVisibility(View.VISIBLE);
            title.setText("You are adding a product under\nFashion Category");
        }

        /////////////////////////////////////////////////////////////////////
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ////////////////////////////////////////////////////////////////////


        //TEh radGrp for the electronic sub category selection//////////////
        electroniRadGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id=checkedId;

                if (id==R.id.mobilePhone_radBtn)
                {
                    electrical_sub="mobiles";
                }
                else if (id==R.id.pcandlaptop_radBtn)
                {
                    electrical_sub="pandl";
                }
                else if (id==R.id.homeAppliance_radBtn)
                {
                    electrical_sub="homeappliance";
                }
                else if (id==R.id.accesories_radBtn)
                {
                    electrical_sub="accesories";
                }
                else if (id==R.id.electrical_radBtn)
                {
                    electrical_sub="electrical";
                }
                else if (id==R.id.furniture_radBtn)
                {
                    electrical_sub="furniture";
                }
                else if (id==R.id.refurbished_radBtn)
                {
                    electrical_sub="refurbished";

                }else if (id==R.id.electricothers_radBtn)
                {
                    electrical_sub="others";
                }
            }
        });
        //////////////////////////////////////////////////////////////////////////



        //TEh radGrp for the grocery sub category selection//////////////
        groceryradGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id=checkedId;

                if (id==R.id.comsetics_radBtn)
                {
                    grocery_sub="cosmetics";
                }
                else if (id==R.id.masalas_radBtn)
                {
                    grocery_sub="masalas";
                }
                else if (id==R.id.flours_radBtn)
                {
                    grocery_sub="flours";
                }
                else if (id==R.id.rice_radBtn)
                {
                    grocery_sub="rice";
                }
                else if (id==R.id.healthProduct_radBtn)
                {
                    grocery_sub="healthproduct";
                }
                else if (id==R.id.genralAndCleaning_radBtn)
                {
                    grocery_sub="genralandcleaninng";
                }
                else if (id==R.id.mensandwomensbeauty_radBtn)
                {
                    grocery_sub="menswomensbeauty";

                }
            }
        });
        //////////////////////////////////////////////////////////////////////////

        //TEh radGrp for the fashion sub category selection//////////////
        fashionRadGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id=checkedId;

                if (id==R.id.menswear_radBtn)
                {
                    fashion_sub="menswear";
                }
                else if (id==R.id.womenswear_radBtn)
                {
                    fashion_sub="womenswear";
                }
                else if (id==R.id.sportswear_radBtn)
                {
                    fashion_sub="sportswear";
                }
                else if (id==R.id.footwear_radBtn)
                {
                    fashion_sub="footwear";
                }
                else if (id==R.id.bandl_radBtn)
                {
                    fashion_sub="bandl";
                }
                else if (id==R.id.gifts_radBtn)
                {
                    fashion_sub="gifts";
                }
                else if (id==R.id.jwellers_radBtn)
                {
                    fashion_sub="jwellers";

                }
                else if (id==R.id.tailorwear_radBtn)
                {
                    fashion_sub="tailorwear";

                }
                else if (id==R.id.fashionothers_radBtn)
                {
                    fashion_sub="others";

                }

            }
        });
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////
        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CATEGORY_TO_ADD.equals("electronics"))
                {
                    if (downloadImageUrl1.equals("null"))
                    {
                        makeToast("First image is mandatory",0);
                    }
                    else if (TextUtils.isEmpty(nameEtxt.getText().toString()))
                    {
                        makeToast("Name can not be empty",0);
                    }
                    else if (TextUtils.isEmpty(descriEtxt.getText().toString()))
                    {
                        makeToast("Description can not be empty",0);
                    }
                    else if (TextUtils.isEmpty(priceEtxt.getText().toString()))
                    {
                        makeToast("Price can not be empty",0);
                    }
                    else if (electrical_sub.equals(""))
                    {
                        makeToast("Select a subcategory",0);
                    }
                    else if (twoGb.equals("no") && threeGb.equals("no") && fourGb.equals("no") && sixGb.equals("no")  && eightGb.equals("no") && othersElec.equals("no"))
                    {
                        makeToast("Select at least one variant",0);
                    }
                    else
                    {
                        if (TO_DO.equals("edit"))
                        {
                            updateProduct("e");
                        }
                        else
                        {
                            placeOrder("e");
                        }
                    }
                }
                else if (CATEGORY_TO_ADD.equals("grocery"))
                {
                    if (downloadImageUrl1.equals("null"))
                    {
                        makeToast("First image is mandatory",0);
                    }
                    else if (TextUtils.isEmpty(nameEtxt.getText().toString()))
                    {
                        makeToast("Name can not be empty",0);
                    }
                    else if (TextUtils.isEmpty(descriEtxt.getText().toString()))
                    {
                        makeToast("Description can not be empty",0);
                    }
                    else if (TextUtils.isEmpty(priceEtxt.getText().toString()))
                    {
                        makeToast("Price can not be empty",0);
                    }
                    else if (grocery_sub.equals(""))
                    {
                        makeToast("Select a subcategory",0);
                    }
                    else if (fifty.equals("no") && hundred.equals("no") && twohundred.equals("no") && twofifty.equals("no")  && fivehundred.equals("no") && onekg.equals("no")&& othersGroc.equals("no"))
                    {
                        makeToast("Select at least one variant",0);
                    }
                    else
                    {
                        if (TO_DO.equals("edit"))
                        {
                            updateProduct("g");
                        }
                        else
                        {
                            placeOrder("g");
                        }

                    }
                }
                else if (CATEGORY_TO_ADD.equals("fashion"))
                {
                    if (downloadImageUrl1.equals("null"))
                    {
                        makeToast("First image is mandatory",0);
                    }
                    else if (TextUtils.isEmpty(nameEtxt.getText().toString()))
                    {
                        makeToast("Name can not be empty",0);
                    }
                    else if (TextUtils.isEmpty(descriEtxt.getText().toString()))
                    {
                        makeToast("Description can not be empty",0);
                    }
                    else if (TextUtils.isEmpty(priceEtxt.getText().toString()))
                    {
                        makeToast("Price can not be empty",0);
                    }
                    else if (fashion_sub.equals(""))
                    {
                        makeToast("Select a subcategory",0);
                    }
                    else if (s.equals("no") && m.equals("no") && l.equals("no") && xl.equals("no")  && xxl.equals("no") && freesize.equals("no")&& othersFashion.equals("no"))
                    {
                        makeToast("Select at least one variant",0);
                    }
                    else
                    {
                        if (TO_DO.equals("edit"))
                        {
                            updateProduct("f");
                        }
                        else
                        {
                            placeOrder("f");
                        }
                    }
                }

            }
        });
        //////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////



        //////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////
        //Following will be the operation, if the activity is opened for editing
        if (TO_DO.equals("edit"))
        {
            addProductBtn.setText("Update the product");

            fetchTheProductDetailsandSetToAllTheVariables();


        }









        //////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////
        //Setting a on click listener for all the variant selecting options

        //The electrical categories
        twoGbBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (twoGb.equals("no"))
                {
                    twoGbBtn.setBackgroundColor(R.color.mainyellow);
                    twoGb="yes";
                }
                else
                {
                    twoGbBtn.setBackgroundColor(Color.parseColor("#A9A9A9"));
                    twoGb="no";
                }
            }
        });

        threeGbBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (threeGb.equals("no"))
                {
                    threeGbBtn.setBackgroundColor(R.color.mainyellow);
                    threeGb="yes";
                }
                else
                {
                    threeGbBtn.setBackgroundColor(Color.parseColor("#A9A9A9"));
                    threeGb="no";
                }
            }
        });


        fourGbBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (fourGb.equals("no"))
                {
                    fourGbBtn.setBackgroundColor(R.color.mainyellow);
                    fourGb="yes";
                }
                else
                {
                    fourGbBtn.setBackgroundColor(Color.parseColor("#A9A9A9"));
                    fourGb="no";
                }
            }
        });


        sixGbBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (sixGb.equals("no"))
                {
                    sixGbBtn.setBackgroundColor(R.color.mainyellow);
                    sixGb="yes";
                }
                else
                {
                    sixGbBtn.setBackgroundColor(Color.parseColor("#A9A9A9"));
                    sixGb="no";
                }
            }
        });


        eightGbBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (eightGb.equals("no"))
                {
                    eightGbBtn.setBackgroundColor(R.color.mainyellow);
                    eightGb="yes";
                }
                else
                {
                    eightGbBtn.setBackgroundColor(Color.parseColor("#A9A9A9"));
                    eightGb="no";
                }
            }
        });


        othersElecBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (othersElec.equals("no"))
                {
                    othersElecBtn.setBackgroundColor(R.color.mainyellow);
                    othersElec="yes";
                }
                else
                {
                    othersElecBtn.setBackgroundColor(Color.parseColor("#A9A9A9"));
                    othersElec="no";
                }
            }
        });


        //The fasshion category
        sBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (s.equals("no"))
                {
                    sBtn.setBackgroundColor(R.color.mainyellow);
                    s="yes";
                }
                else
                {
                    sBtn.setBackgroundColor(Color.parseColor("#A9A9A9"));
                    s="no";
                }
            }
        });

        mBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (m.equals("no"))
                {
                    mBtn.setBackgroundColor(R.color.mainyellow);
                    m="yes";
                }
                else
                {
                    mBtn.setBackgroundColor(Color.parseColor("#A9A9A9"));
                    m="no";
                }
            }
        });


        lBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (l.equals("no"))
                {
                    lBtn.setBackgroundColor(R.color.mainyellow);
                    l="yes";
                }
                else
                {
                    lBtn.setBackgroundColor(Color.parseColor("#A9A9A9"));
                    l="no";
                }
            }
        });


        xlBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (xl.equals("no"))
                {
                    xlBtn.setBackgroundColor(R.color.mainyellow);
                    xl="yes";
                }
                else
                {
                    xlBtn.setBackgroundColor(Color.parseColor("#A9A9A9"));
                    xl="no";
                }
            }
        });


        xxlBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (xxl.equals("no"))
                {
                    xxlBtn.setBackgroundColor(R.color.mainyellow);
                    xxl="yes";
                }
                else
                {
                    xxlBtn.setBackgroundColor(Color.parseColor("#A9A9A9"));
                    xxl="no";
                }
            }
        });

        freesizeBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (freesize.equals("no"))
                {
                    freesizeBtn.setBackgroundColor(R.color.mainyellow);
                    freesize="yes";
                }
                else
                {
                    freesizeBtn.setBackgroundColor(Color.parseColor("#A9A9A9"));
                    freesize="no";
                }
            }
        });


        othersFashionBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (othersFashion.equals("no"))
                {
                    othersFashionBtn.setBackgroundColor(R.color.mainyellow);
                    othersFashion="yes";
                }
                else
                {
                    othersFashionBtn.setBackgroundColor(Color.parseColor("#A9A9A9"));
                    othersFashion="no";
                }
            }
        });


        //The grocery category
        fiftyBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (fifty.equals("no"))
                {
                    fiftyBtn.setBackgroundColor(R.color.mainyellow);
                    fifty="yes";
                }
                else
                {
                    fiftyBtn.setBackgroundColor(Color.parseColor("#A9A9A9"));
                    fifty="no";
                }
            }
        });

        hundredBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (hundred.equals("no"))
                {
                    hundredBtn.setBackgroundColor(R.color.mainyellow);
                    hundred="yes";
                }
                else
                {
                    hundredBtn.setBackgroundColor(Color.parseColor("#A9A9A9"));
                    hundred="no";
                }
            }
        });


        twohundredBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (twohundred.equals("no"))
                {
                    twohundredBtn.setBackgroundColor(R.color.mainyellow);
                    twohundred="yes";
                }
                else
                {
                    twohundredBtn.setBackgroundColor(Color.parseColor("#A9A9A9"));
                    twohundred="no";
                }
            }
        });


        twofiftyBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (twofifty.equals("no"))
                {
                    twofiftyBtn.setBackgroundColor(R.color.mainyellow);
                    twofifty="yes";
                }
                else
                {
                    twofiftyBtn.setBackgroundColor(Color.parseColor("#A9A9A9"));
                    twofifty="no";
                }
            }
        });


        fivehundredBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (fivehundred.equals("no"))
                {
                    fivehundredBtn.setBackgroundColor(R.color.mainyellow);
                    fivehundred="yes";
                }
                else
                {
                    fivehundredBtn.setBackgroundColor(Color.parseColor("#A9A9A9"));
                    fivehundred="no";
                }
            }
        });

        onekgBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (onekg.equals("no"))
                {
                    onekgBtn.setBackgroundColor(R.color.mainyellow);
                    onekg="yes";
                }
                else
                {
                    onekgBtn.setBackgroundColor(Color.parseColor("#A9A9A9"));
                    onekg="no";
                }
            }
        });


        othersGrocBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (othersGroc.equals("no"))
                {
                    othersGrocBtn.setBackgroundColor(R.color.mainyellow);
                    othersGroc="yes";
                }
                else
                {
                    othersGrocBtn.setBackgroundColor(Color.parseColor("#A9A9A9"));
                    othersGroc="no";
                }
            }
        });

        //////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////


        imageOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
                picNumber=1;
            }
        });


        imagetwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
                picNumber=2;
            }
        });


        imagethree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
                picNumber=3;
            }
        });


        imagefour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
                picNumber=4;
            }
        });


    }

    private void fetchTheProductDetailsandSetToAllTheVariables() {
        DatabaseReference pRef=null;

        if (CATEGORY_TO_ADD.equals("electronics"))
        {
            pRef= FirebaseDatabase.getInstance().getReference().child("Products").child("Electronics");

        }
        else if (CATEGORY_TO_ADD.equals("grocery"))
        {
            pRef= FirebaseDatabase.getInstance().getReference().child("Products").child("Grocery");

        }
        else if (CATEGORY_TO_ADD.equals("fashion"))
        {
            pRef= FirebaseDatabase.getInstance().getReference().child("Products").child("Fashion");

        }

        pRef.child(pid).addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    Products products=snapshot.getValue(Products.class);

                    //Now, we need to set the relevant data
                    downloadImageUrl1=products.getImageone();
                    downloadImageUrl2=products.getImagetwo();
                    downloadImageUrl3=products.getImagethree();
                    downloadImageUrl4=products.getImagefour();

                    //Now displaying the images
                    Picasso.get().load(downloadImageUrl1).into(imageOne);
                    Picasso.get().load(downloadImageUrl2).into(imagetwo);
                    Picasso.get().load(downloadImageUrl3).into(imagethree);
                    Picasso.get().load(downloadImageUrl4).into(imagefour);

                    //Setting the name price and description
                    nameEtxt.setText(products.getName());
                    priceEtxt.setText(products.getPrice());
                    descriEtxt.setText(products.getDescri());

                    if (CATEGORY_TO_ADD.equals("electronics"))
                    {
                        electrical_sub=products.getSubcategory();
                    }
                    else if (CATEGORY_TO_ADD.equals("grocery"))
                    {
                        grocery_sub=products.getSubcategory();
                    }
                    else if (CATEGORY_TO_ADD.equals("fashion"))
                    {
                        fashion_sub=products.getSubcategory();
                    }

                    electronicSubcategory.setVisibility(View.GONE);
                    grocerySubcategory.setVisibility(View.GONE);
                    fashionSubcategory.setVisibility(View.GONE);

                    //Now, setting the variants for all one by one
                    s=products.getS();
                    m=products.getM();
                    l=products.getL();
                    xl=products.getXl();
                    xxl=products.getXxl();
                    freesize=products.getFreesize();
                    othersFashion=products.getOthersfashion();

                    fifty=products.getFifty();
                    hundred=products.getHundred();
                    twohundred=products.getTwohundred();
                    twofifty=products.getTwofifty();
                    fivehundred=products.getFivehundred();
                    onekg=products.getOnekg();
                    othersGroc=products.getOthersgrocery();

                    twoGb=products.getTwogb();
                    threeGb=products.getThreegb();
                    fourGb=products.getFourgb();
                    sixGb=products.getSixgb();
                    eightGb=products.getEightgb();
                    othersElec=products.getOtherselectrical();

                    //Now, we need to highlight the button foor all the yes variables
                    if (products.getS().equals("yes"))
                    {
                        sBtn.setBackgroundColor(R.color.mainyellow);
                    }
                     if (products.getM().equals("yes"))
                    {
                        mBtn.setBackgroundColor(R.color.mainyellow);
                    }
                     if (products.getL().equals("yes"))
                    {
                        lBtn.setBackgroundColor(R.color.mainyellow);
                    }
                     if (products.getXl().equals("yes"))
                    {
                        xlBtn.setBackgroundColor(R.color.mainyellow);
                    }
                     if (products.getXxl().equals("yes"))
                    {
                        xxlBtn.setBackgroundColor(R.color.mainyellow);
                    }
                     if (products.getFreesize().equals("yes"))
                    {
                        freesizeBtn.setBackgroundColor(R.color.mainyellow);
                    }
                     if (products.getOthersfashion().equals("yes"))
                    {
                        othersElecBtn.setBackgroundColor(R.color.mainyellow);
                    }




                    if (products.getFifty().equals("yes"))
                    {
                        fiftyBtn.setBackgroundColor(R.color.mainyellow);
                    }
                     if (products.getHundred().equals("yes"))
                    {
                        hundredBtn.setBackgroundColor(R.color.mainyellow);
                    }
                     if (products.getTwohundred().equals("yes"))
                    {
                        twohundredBtn.setBackgroundColor(R.color.mainyellow);
                    }
                     if (products.getTwofifty().equals("yes"))
                    {
                        twofiftyBtn.setBackgroundColor(R.color.mainyellow);
                    }
                     if (products.getFivehundred().equals("yes"))
                    {
                        fivehundredBtn.setBackgroundColor(R.color.mainyellow);
                    }

                     if (products.getOnekg().equals("yes"))
                    {
                        onekgBtn.setBackgroundColor(R.color.mainyellow);
                    }
                     if (products.getOtherselectrical().equals("yes"))
                    {
                        othersGrocBtn.setBackgroundColor(R.color.mainyellow);
                    }


                    if (products.getTwogb().equals("yes"))
                    {
                        twoGbBtn.setBackgroundColor(R.color.mainyellow);
                    }

                     if (products.getFourgb().equals("yes"))
                    {
                        fourGbBtn.setBackgroundColor(R.color.mainyellow);
                    }
                     if (products.getSixgb().equals("yes"))
                    {
                        sixGbBtn.setBackgroundColor(R.color.mainyellow);
                    }
                     if (products.getEightgb().equals("yes"))
                    {
                        eightGbBtn.setBackgroundColor(R.color.mainyellow);
                    }
                     if (products.getOtherselectrical().equals("yes"))
                    {
                        othersElecBtn.setBackgroundColor(R.color.mainyellow);
                    }
                     if (products.getThreegb().equals("yes"))
                    {
                        threeGbBtn.setBackgroundColor(R.color.mainyellow);
                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    public void makeToast(String textToShow, int lenght)
    {
        LayoutInflater inflater = AddProduct.this.getLayoutInflater();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==100)
        {
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {

            }
            else
            {
                ActivityCompat.requestPermissions(AddProduct.this,new String[]{
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
                Picasso.get().load(ImageUri1).into(imageOne);

                uploadImage(ImageUri1);
            }
            else if (picNumber==2)
            {
                ImageUri2 = data.getData();
                Picasso.get().load(ImageUri2).into(imagetwo);

                uploadImage(ImageUri2);
            }
            else if (picNumber==3)
            {
                ImageUri3 = data.getData();
                Picasso.get().load(ImageUri3).into(imagethree);

                uploadImage(ImageUri3);
            }
            else if (picNumber==4)
            {
                ImageUri4 = data.getData();
                Picasso.get().load(ImageUri4).into(imagefour);


                uploadImage(ImageUri4);
            }





        } else {

            makeToast("Image Not Selected",0);
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadImage(Uri imageUri) {

        progressDialog = new ProgressDialog(AddProduct.this);
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
                        if (picNumber == 3) {
                            downloadImageUrl3 = filePath.getDownloadUrl().toString();
                        }
                        if (picNumber == 4) {
                            downloadImageUrl4 = filePath.getDownloadUrl().toString();
                        }

                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (picNumber == 1) {
                            downloadImageUrl1 = task.getResult().toString();
                        }
                        if (picNumber == 2) {
                            downloadImageUrl2 = task.getResult().toString();
                        }
                        if (picNumber == 3) {
                            downloadImageUrl3 = task.getResult().toString();
                        }
                        if (picNumber == 4) {
                            downloadImageUrl4 = task.getResult().toString();
                        }

                        progressDialog.dismiss();
                    }
                });
            }


        });
    }


    private void init() {

        electronicVariants=findViewById(R.id.layout_variantschoice_electronics);
        groceryVariants=findViewById(R.id.layout_variantschoice_grocery);
        fashionVariants=findViewById(R.id.layout_variantschoice_fashion);

        electronicSubcategory=findViewById(R.id.electronics_subcategories_layout_addProduct);
        grocerySubcategory=findViewById(R.id.grocery_subcategories_layout_addProduct);
        fashionSubcategory=findViewById(R.id.fashion_subcategories_layout_addProduct);

        title=findViewById(R.id.title_addProduct);
        backBtn=findViewById(R.id.backBtn_addProduct);

        imageOne=findViewById(R.id.image1_addProduct);
        imagetwo=findViewById(R.id.image2_addProduct);
        imagethree=findViewById(R.id.image3_addProduct);
        imagefour=findViewById(R.id.image4_addProduct);

        nameEtxt=findViewById(R.id.name_addProduct);
        priceEtxt=findViewById(R.id.price_addProduct);
        descriEtxt=findViewById(R.id.descri_addProduct);

        electroniRadGrp=findViewById(R.id.electrnics_subcategories_radGrp_addProduct);
        groceryradGrp=findViewById(R.id.grocery_subcategories_radGrp_addProduct);
        fashionRadGrp=findViewById(R.id.fashion_subcategories_radGrp_addProduct);

        addProductBtn=findViewById(R.id.addProductBtn_addProduct);

        //Assinging the variant avilable buttons
        //For the electrical category
        twoGbBtn=findViewById(R.id.twogb_btn);
        threeGbBtn=findViewById(R.id.threegb_btn);
        fourGbBtn=findViewById(R.id.fourgb_btn);
        sixGbBtn=findViewById(R.id.sixgb_btn);
        eightGbBtn=findViewById(R.id.eightgb_btn);
        othersElecBtn=findViewById(R.id.othreselectrical_btn);

        //For the grocery category
        fiftyBtn=findViewById(R.id.fifty_btn);
        hundredBtn=findViewById(R.id.hundred_btn);
        twohundredBtn=findViewById(R.id.twohundred_btn);
        twofiftyBtn=findViewById(R.id.twofifty_btn);
        fivehundredBtn=findViewById(R.id.fivehundred_btn);
        onekgBtn=findViewById(R.id.onekg_btn);
        othersGrocBtn=findViewById(R.id.othresgrocery_btn);

        //For the fashion category
        sBtn=findViewById(R.id.s_btn);
        mBtn=findViewById(R.id.m_btn);
        lBtn=findViewById(R.id.l_btn);
        xlBtn=findViewById(R.id.xl_btn);
        xxlBtn=findViewById(R.id.xxl_btn);
        freesizeBtn=findViewById(R.id.freesize_btn);
        othersFashionBtn=findViewById(R.id.othersfashion_btn);


    }

    private void updateProduct(String test)
    {
        //Her we need to start a progress dialog
        progressDialog = new ProgressDialog(AddProduct.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show(); //Starting the progress dialog
        progressDialog.setContentView(R.layout.layout_loading);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        DatabaseReference electronicRef=null;

        if (test.equals("e"))
        {
            electronicRef=FirebaseDatabase.getInstance().getReference().child("Products").child("Electronics");
        }

        if (test.equals("g"))
        {
            electronicRef=FirebaseDatabase.getInstance().getReference().child("Products").child("Grocery");
        }

        if (test.equals("f"))
        {
            electronicRef=FirebaseDatabase.getInstance().getReference().child("Products").child("Fashion");
        }



        final Map<String,Object>map=new HashMap<>();
        //The fashion variants
        map.put("s",s);
        map.put("m",m);
        map.put("l",l);
        map.put("xl",xl);
        map.put("xxl",xxl);
        map.put("freesize",freesize);
        map.put("othersfashion",othersFashion);

        //The grocery variants
        map.put("fifty",fifty);
        map.put("hundred",hundred);
        map.put("twohundred",twohundred);
        map.put("twofifty",twofifty);
        map.put("fivehundred",fivehundred);
        map.put("onekg",onekg);
        map.put("othersgrocery",othersGroc);

        //The electric ones
        map.put("twogb",twoGb);
        map.put("threegb",threeGb);
        map.put("fourgb",fourGb);
        map.put("sixgb",sixGb);
        map.put("eightgb",eightGb);
        map.put("otherselectrical",othersElec);

        //The rest other fields
        map.put("imageone",downloadImageUrl1);
        map.put("imagetwo",downloadImageUrl2);
        map.put("imagethree",downloadImageUrl3);
        map.put("imagefour",downloadImageUrl4);
        map.put("name",nameEtxt.getText().toString());
        map.put("price",priceEtxt.getText().toString());
        map.put("descri",descriEtxt.getText().toString());
        if (test.equals("e"))
        {
            map.put("subcategory",electrical_sub);
        }

        if (test.equals("g"))
        {
            map.put("subcategory",grocery_sub);
        }

        if (test.equals("f"))
        {
            map.put("subcategory",fashion_sub);
        }



        electronicRef.child(pid).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    makeToast("Product Updated",0);
                    progressDialog.dismiss();
                    finish();
                }
                else
                {
                    makeToast(task.getException().toString(),1);
                    progressDialog.dismiss();
                    finish();
                }
            }
        });

    }
    


    private void placeOrder(String test)
    {
        //Her we need to start a progress dialog
        progressDialog = new ProgressDialog(AddProduct.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show(); //Starting the progress dialog
        progressDialog.setContentView(R.layout.layout_loading);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        DatabaseReference electronicRef=null;

        if (test.equals("e"))
        {
             electronicRef=FirebaseDatabase.getInstance().getReference().child("Products").child("Electronics");
        }

        if (test.equals("g"))
        {
             electronicRef=FirebaseDatabase.getInstance().getReference().child("Products").child("Grocery");
        }

        if (test.equals("f"))
        {
             electronicRef=FirebaseDatabase.getInstance().getReference().child("Products").child("Fashion");
        }

        String pid=electronicRef.push().getKey().toString();

        final Map<String,Object>map=new HashMap<>();
        //The fashion variants
        map.put("s",s);
        map.put("m",m);
        map.put("l",l);
        map.put("xl",xl);
        map.put("xxl",xxl);
        map.put("freesize",freesize);
        map.put("othersfashion",othersFashion);

        //The grocery variants
        map.put("fifty",fifty);
        map.put("hundred",hundred);
        map.put("twohundred",twohundred);
        map.put("twofifty",twofifty);
        map.put("fivehundred",fivehundred);
        map.put("onekg",onekg);
        map.put("othersgrocery",othersGroc);

        //The electric ones
        map.put("twogb",twoGb);
        map.put("threegb",threeGb);
        map.put("fourgb",fourGb);
        map.put("sixgb",sixGb);
        map.put("eightgb",eightGb);
        map.put("otherselectrical",othersElec);

        //The rest other fields
        map.put("imageone",downloadImageUrl1);
        map.put("imagetwo",downloadImageUrl2);
        map.put("imagethree",downloadImageUrl3);
        map.put("imagefour",downloadImageUrl4);
        map.put("name",nameEtxt.getText().toString());
        map.put("price",priceEtxt.getText().toString());
        map.put("descri",descriEtxt.getText().toString());
        if (test.equals("e"))
        {
            map.put("subcategory",electrical_sub);
        }

        if (test.equals("g"))
        {
            map.put("subcategory",grocery_sub);
        }

        if (test.equals("f"))
        {
            map.put("subcategory",fashion_sub);
        }

        map.put("sellerid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        map.put("pid",pid);
        map.put("search",nameEtxt.getText().toString().toLowerCase());

        electronicRef.child(pid).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    makeToast("Product Uploaded",0);
                    progressDialog.dismiss();
                    finish();
                }
                else
                {
                    makeToast(task.getException().toString(),1);
                    progressDialog.dismiss();
                    finish();
                }
            }
        });

    }
}
