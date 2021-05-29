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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tn_46new.Adapters.FashionAdapterHome;
import com.example.tn_46new.Adapters.FullProductImagesAdapter;
import com.example.tn_46new.Admin.AdminHome;
import com.example.tn_46new.Buyer.BuyerHome;
import com.example.tn_46new.Models.Products;
import com.example.tn_46new.seller.AddProduct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class FullProductActivity extends AppCompatActivity {

    //Following are the objects
    private ImageView backBtn;
    private ImageView cartBtn;
    private ImageView delBtn;

    private RecyclerView imageRecycler;

    private TextView name;
    private TextView price;
    private TextView description;

    private TextView addToCart;

    private LinearLayout electronic_variant;
    private LinearLayout grocery_variant;
    private LinearLayout fashion_variant;

    //Follwing is to get the starting details
    private String pid="";
    private String USER_TYPE="";
    private String category="";

    //Following are the variables, to store the image links and pass them to the image display adapter
    private String i1="";
    private String i2="";
    private String i3="";
    private String i4="";

    private ProgressDialog progressDialog;

    List<String> imageList=null;
    private FullProductImagesAdapter adapterFullImage;

    //Following are the objects for the radiobutton
    private RadioButton gb2;
    private RadioButton gb3;
    private RadioButton gb4;
    private RadioButton gb6;
    private RadioButton gb8;
    private RadioButton others_electrical_radiobtn;

    //Following are the objects for the grocery varaint
    private RadioButton gram50;
    private RadioButton gram100;
    private RadioButton gram200;
    private RadioButton gram250;
    private RadioButton gram500;
    private RadioButton kg1;
    private RadioButton getOthers_grocery_radiobtn;

    //Following are the objects for the fashion radio button
    private RadioButton s;
    private RadioButton m;
    private RadioButton l;
    private RadioButton xl;
    private RadioButton xxl;
    private RadioButton freeSize;
    private RadioButton getOthers_fashion_radiobtn;

    //Following are the three radio buttons
    private RadioGroup electricRadGrp;
    private RadioGroup groceryRadGrp;
    private RadioGroup fashionRadGrp;

    //Followinga are the  variables to fetch and store the details sent to the cart
    private String nameC="";
    private String imageC="";
    private String priceC="";
    private String selleridC="";



    //Following ais the variabble to storre the order variant
    private String VARIANT="";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_product);

        Intent i=getIntent();
        pid=i.getStringExtra("pid");
        USER_TYPE=i.getStringExtra("user");
        category=i.getStringExtra("category");

        //Folllowing is the code to set the status bar as color
        Window window= FullProductActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(FullProductActivity.this, R.color.mainyellow));

        //Restricting the lanscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init();

        fetchAllTHeDeatailsOFTheProduct(); //Following is the method, that will fetch all the details of the respective product


        //Setting th etop right button, according to the user
        if (USER_TYPE.equals("buyer"))
        {
            cartBtn.setBackgroundResource(R.drawable.cart_ic);

        }
        else
        {
            cartBtn.setBackgroundResource(R.drawable.delete_ic);

        }


        if (USER_TYPE.equals("seller"))
        {
            addToCart.setText("Edit the product");
        }


        //Setting up the recycler view, for the image
        imageRecycler.setHasFixedSize(true);
        imageRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        imageList=new ArrayList<>();


        //Making all the radio buttons invivsilble
        gb2.setVisibility(View.GONE);
        gb3.setVisibility(View.GONE);
        gb4.setVisibility(View.GONE);
        gb6.setVisibility(View.GONE);
        gb8.setVisibility(View.GONE);
        others_electrical_radiobtn.setVisibility(View.GONE);

        //Making all the grocery invisible at initial
        gram50.setVisibility(View.GONE);
        gram100.setVisibility(View.GONE);
        gram200.setVisibility(View.GONE);
        gram250.setVisibility(View.GONE);
        gram500.setVisibility(View.GONE);
        kg1.setVisibility(View.GONE);
        getOthers_grocery_radiobtn.setVisibility(View.GONE);

        //Making the fashion variants layoout invivble
        s.setVisibility(View.GONE);
        m.setVisibility(View.GONE);
        l.setVisibility(View.GONE);
        xl.setVisibility(View.GONE);
        xxl.setVisibility(View.GONE);
        freeSize.setVisibility(View.GONE);
        getOthers_fashion_radiobtn.setVisibility(View.GONE);


        //Settting a check changed listener on the electric layout
        electricRadGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.gb2_btn)
                {
                    VARIANT="2 GB";
                }
                else if (checkedId==R.id.gb3_btn)
                {
                    VARIANT="3 GB";
                }

                else if (checkedId==R.id.gb4_btn)
                {
                    VARIANT="4 GB";
                }

                else if (checkedId==R.id.gb6_btn)
                {
                    VARIANT="6 GB";
                }

                else if (checkedId==R.id.gb8_btn)
                {
                    VARIANT="8 GB";
                }

                else if (checkedId==R.id.others_electrical_radtn)
                {
                    VARIANT="Others";
                }
            }
        });

        //Setting the check changed listenerr on the grocery radGrp
        groceryRadGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.gram50_radBtn)
                {
                    VARIANT="50 gram";
                }
                else if (checkedId==R.id.gram100_radBtn)
                {
                    VARIANT="100 gram";
                }

                else if (checkedId==R.id.gram200_radBtn)
                {
                    VARIANT="200 gram";
                }

                else if (checkedId==R.id.gram250_radBtn)
                {
                    VARIANT="250 gram";
                }

                else if (checkedId==R.id.gram500_radBtn)
                {
                    VARIANT="500 gram";
                }

                else if (checkedId==R.id.kg1_radBtn)
                {
                    VARIANT="1 kg";
                }

                else if (checkedId==R.id.others_grocery_radBtn)
                {
                    VARIANT="Others";
                }
            }
        });

        //Seeting a on check changes listener on  the fashion radGrp
        fashionRadGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.s_radBtn)
                {
                    VARIANT="Small";
                }
                else if (checkedId==R.id.m_radBtn)
                {
                    VARIANT="Medium";
                }

                else if (checkedId==R.id.l_radBtn)
                {
                    VARIANT="Large";
                }

                else if (checkedId==R.id.xl_radBtn)
                {
                    VARIANT="Extra Large";
                }

                else if (checkedId==R.id.xxl_radBtn)
                {
                    VARIANT="Extra Extra Large";
                }

                else if (checkedId==R.id.freesize_radBtn)
                {
                    VARIANT="Free Size";
                }
                else if (checkedId==R.id.others_fashion_radBtn)
                {
                    VARIANT="Others";
                }
            }
        });









        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (USER_TYPE.equals("buyer"))
            {
               if (VARIANT.equals(""))
               {
                   makeToast("Please select a variant",0);
               }
               else
               {
                    //Here,we have to add the product to the cart with qunatity as one
                //Now we have to add the current product in the cart with intial quantity 1

                progressDialog = new ProgressDialog(FullProductActivity.this);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show(); //Starting the progress dialog
                progressDialog.setContentView(R.layout.layout_loading);
                progressDialog.setCancelable(false);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                DatabaseReference cartRed=FirebaseDatabase.getInstance().getReference().child("Cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                String refC=cartRed.push().getKey().toString();

                final Map<String,Object> map= new HashMap<>();
                map.put("name",nameC);
                map.put("unitprice",priceC);
                map.put("image",imageC);

                map.put("variant",VARIANT);
                map.put("qty","1");
                map.put("totalprice",priceC);
                map.put("cid",refC);
                map.put("sellerid",selleridC);

                cartRed.child(refC).updateChildren(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    makeToast("Added to cart sucessfully",0);
                                    progressDialog.dismiss();
                                    finish();
                                }
                                else
                                {
                                    makeToast(task.getException().toString(),1);
                                    progressDialog.dismiss();

                                }
                            }
                        });
               }






            }
            else if (USER_TYPE.equals("seller"))
            {

                //Here, we have to start the editing activity for the product
                Intent i=new Intent(getApplicationContext(), AddProduct.class);
                if (category.equals("e"))
                {
                    i.putExtra("category","electronics");
                }
                if (category.equals("g"))
                {
                    i.putExtra("category","grocery");
                }
                if (category.equals("f"))
                {
                    i.putExtra("category","fashion");
                }

                i.putExtra("todo","edit");
                i.putExtra("pid",pid);

                startActivity(i);
                finish();




            }
            else if (USER_TYPE.equals("admin"))
            {
                makeToast("Function not avilable for admin",0);
            }
            }
        });

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (USER_TYPE.equals("buyer"))
                {

                    //Here, we need to finish the activity and open the cart


                }
                else
                {
                    //Here, we need to del the product, so we need to get a confirmation dialog
                    Dialog dialog=new Dialog(FullProductActivity.this);
                    dialog.setContentView(R.layout.layout_dialogbox);
                    dialog.setCancelable(true);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                    TextView text=dialog.findViewById(R.id.text_dialog);
                    TextView positive=dialog.findViewById(R.id.positive_dialog);
                    TextView negative=dialog.findViewById(R.id.negative_dialog);

                    text.setText("Are you sure you want to delete the product?");

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


                            progressDialog = new ProgressDialog(FullProductActivity.this);
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show(); //Starting the progress dialog
                            progressDialog.setContentView(R.layout.layout_loading);
                            progressDialog.setCancelable(false);
                            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                            DatabaseReference pRef=null;

                            if (category.equals("e"))
                            {
                                pRef= FirebaseDatabase.getInstance().getReference().child("Products").child("Electronics");
                                electronic_variant.setVisibility(View.VISIBLE);
                            }
                            else if (category.equals("g"))
                            {
                                pRef= FirebaseDatabase.getInstance().getReference().child("Products").child("Grocery");
                                grocery_variant.setVisibility(View.VISIBLE);
                            }
                            else if (category.equals("f"))
                            {
                                pRef= FirebaseDatabase.getInstance().getReference().child("Products").child("Fashion");
                                fashion_variant.setVisibility(View.VISIBLE);
                            }

                            pRef.child(pid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                               if (task.isSuccessful())
                               {
                                    makeToast("Item Deleted Sucessfully",0);
                                    finish();
                                    progressDialog.dismiss();
                               }
                               else
                               {
                                   makeToast(task.getException().toString(),0);
                                   progressDialog.dismiss();
                               }
                                }
                            });



                        }
                    });


                    dialog.show();
                }
            }
        });






        //Setting a on click listener on the backBtn
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    private void fetchAllTHeDeatailsOFTheProduct() {
        DatabaseReference pRef=null;

        if (category.equals("e"))
        {
            pRef= FirebaseDatabase.getInstance().getReference().child("Products").child("Electronics");
            electronic_variant.setVisibility(View.VISIBLE);
        }
        else if (category.equals("g"))
        {
            pRef= FirebaseDatabase.getInstance().getReference().child("Products").child("Grocery");
            grocery_variant.setVisibility(View.VISIBLE);
        }
        else if (category.equals("f"))
        {
            pRef= FirebaseDatabase.getInstance().getReference().child("Products").child("Fashion");
            fashion_variant.setVisibility(View.VISIBLE);
        }

        pRef.child(pid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    Products products=snapshot.getValue(Products.class);

                    //Gettting all the images one by one
                    i1=products.getImageone();
                    i2=products.getImagetwo();
                    i3=products.getImagethree();
                    i4=products.getImagefour();

                    if (!i1.equals("null"))
                    {
                        imageList.add(i1);
                    }

                    if (!i2.equals("null"))
                    {
                        imageList.add(i2);
                    }

                    if (!i3.equals("null"))
                    {
                        imageList.add(i3);
                    }

                    if (!i4.equals("null"))
                    {
                        imageList.add(i4);
                    }

                    adapterFullImage=new FullProductImagesAdapter(imageList,getApplicationContext(), FullProductActivity.this,"buyer");
                    imageRecycler.setAdapter(adapterFullImage);
                    adapterFullImage.notifyDataSetChanged();


                    name.setText(products.getName());
                    price.setText("Rs "+products.getPrice()+"/-");

                    description.setText(products.getDescri());


                    //setting the items to cart
                    nameC=products.getName();
                    priceC=products.getPrice();
                    imageC=products.getImageone();
                    selleridC=products.getSellerid();



                    //Making the avilable variants for the eelectrical variants option visible
                    if (products.getTwogb().equals("yes"))
                    {
                        gb2.setVisibility(View.VISIBLE);
                    }
                    if (products.getThreegb().equals("yes"))
                    {
                        gb3.setVisibility(View.VISIBLE);
                    }
                    if (products.getFourgb().equals("yes"))
                    {
                        gb4.setVisibility(View.VISIBLE);
                    }
                    if (products.getSixgb().equals("yes"))
                    {
                        gb6.setVisibility(View.VISIBLE);
                    }
                    if (products.getEightgb().equals("yes"))
                    {
                        gb8.setVisibility(View.VISIBLE);
                    }
                    if (products.getOtherselectrical().equals("yes"))
                    {
                        others_electrical_radiobtn.setVisibility(View.VISIBLE);
                    }

                    //MAking the avilable variants visible for grocery
                    if (products.getFifty().equals("yes"))
                    {
                        gram50.setVisibility(View.VISIBLE);
                    }
                    if (products.getHundred().equals("yes"))
                    {
                        gram100.setVisibility(View.VISIBLE);
                    }
                    if (products.getTwohundred().equals("yes"))
                    {
                        gram200.setVisibility(View.VISIBLE);
                    }
                    if (products.getTwofifty().equals("yes"))
                    {
                        gram250.setVisibility(View.VISIBLE);
                    }
                    if (products.getFivehundred().equals("yes"))
                    {
                        gram500.setVisibility(View.VISIBLE);
                    }
                    if (products.getOnekg().equals("yes"))
                    {
                        kg1.setVisibility(View.VISIBLE);
                    }
                    if (products.getOthersgrocery().equals("yes"))
                    {
                        others_electrical_radiobtn.setVisibility(View.VISIBLE);
                    }

                    //Making the avilable varaints VISIBLE FOR fashion category
                    if (products.getS().equals("yes"))
                    {
                        s.setVisibility(View.VISIBLE);
                    }
                    if (products.getM().equals("yes"))
                    {
                        m.setVisibility(View.VISIBLE);
                    }
                    if (products.getL().equals("yes"))
                    {
                        l.setVisibility(View.VISIBLE);
                    }
                    if (products.getXl().equals("yes"))
                    {
                        xl.setVisibility(View.VISIBLE);
                    }
                    if (products.getXxl().equals("yes"))
                    {
                        xxl.setVisibility(View.VISIBLE);
                    }
                    if (products.getFreesize().equals("yes"))
                    {
                        freeSize.setVisibility(View.VISIBLE);
                    }
                    if (products.getOthersfashion().equals("yes"))
                    {
                        getOthers_fashion_radiobtn.setVisibility(View.VISIBLE);
                    }






                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void init() {
        imageRecycler=findViewById(R.id.imageRecycler_foodProduct);

        backBtn=findViewById(R.id.backBtn_fullProduct);
        cartBtn=findViewById(R.id.cartic_foodProduct);

        name=findViewById(R.id.itemName_foodProduct);
        price=findViewById(R.id.itemPrice_foodProduct);

        description=findViewById(R.id.productDescription_foodProduct);

        addToCart=findViewById(R.id.addToCartBtn_foodProduct);

        //The variant layouts
        electronic_variant=findViewById(R.id.electrnicvariantlayout_foodProduct);
        grocery_variant=findViewById(R.id.groceryvariantLayout_foodProduct);
        fashion_variant=findViewById(R.id.fashionvariantlayout_foodProduct);


        //Asinging ids to the electrical  variants radio button
        gb2=findViewById(R.id.gb2_btn);
        gb3=findViewById(R.id.gb3_btn);
        gb4=findViewById(R.id.gb4_btn);
        gb6=findViewById(R.id.gb6_btn);
        gb8=findViewById(R.id.gb8_btn);
        others_electrical_radiobtn=findViewById(R.id.others_electrical_radtn);

        //Assinging ids to the grocery radio buttons
        gram50=findViewById(R.id.gram50_radBtn);
        gram100=findViewById(R.id.gram100_radBtn);
        gram200=findViewById(R.id.gram200_radBtn);
        gram250=findViewById(R.id.gram250_radBtn);
        gram500=findViewById(R.id.gram500_radBtn);
        kg1=findViewById(R.id.kg1_radBtn);
        getOthers_grocery_radiobtn=findViewById(R.id.others_grocery_radBtn);

        //Assinging ids to the fashion radio butons
        s=findViewById(R.id.s_radBtn);
        m=findViewById(R.id.m_radBtn);
        l=findViewById(R.id.l_radBtn);
        xl=findViewById(R.id.xl_radBtn);
        xxl=findViewById(R.id.xxl_radBtn);
        getOthers_fashion_radiobtn=findViewById(R.id.others_fashion_radBtn);
        freeSize=findViewById(R.id.freesize_radBtn);

        backBtn=findViewById(R.id.backBtn_fullProduct);

        electricRadGrp=findViewById(R.id.radGrp_electric);
        groceryRadGrp=findViewById(R.id.radGrp_grocery);
        fashionRadGrp=findViewById(R.id.radGrp_fashion);




    }
    public void makeToast(String textToShow, int lenght)
    {
        LayoutInflater inflater = FullProductActivity.this.getLayoutInflater();
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