package com.example.tn_46new.Buyer;

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
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tn_46new.Adapters.CartItemAdapter;
import com.example.tn_46new.Adapters.FashionAdapterHome;
import com.example.tn_46new.Adapters.OrdersAdapter;
import com.example.tn_46new.FullProductActivity;
import com.example.tn_46new.Models.Cart;
import com.example.tn_46new.Models.Orders;
import com.example.tn_46new.OrdersActivity;
import com.example.tn_46new.R;
import com.example.tn_46new.SubcategoryActivity;
import com.example.tn_46new.seller.AddProduct;
import com.example.tn_46new.seller.SellerHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CartActivity extends AppCompatActivity implements PaymentResultListener {

    //Following are the object on the cart screen
    private RecyclerView recyclerView;
    private List<Cart> list;
    private CartItemAdapter adapter;

    private ImageView backBtn;
    private EditText name;
    private EditText contact;
    private EditText adresss;

    private RadioGroup radGrp;
    private RadioButton onlineRad;
    private RadioButton cashOnDeliveryRad;

    private TextView tottalPrice;

    private Button placeOrder;

    //The textView for autofilling details
    private TextView autofill;
    //Following is the variable to catch if details found or not
    private boolean isFilled=false;

    double TOTAL=0;



    private ProgressDialog progressDialog;

    private String MODE=""; //Following is the variable that will  hold the payment method.



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Folllowing is the code to set the status bar as color
        Window window= CartActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(CartActivity.this, R.color.mainyellow));

        //Restricting the lanscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init(); //Following is the method, to initialize the objects with their ids

        //Setting up  the recylcer view
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        list=new ArrayList<>();


        fetchAllTheItemInTheCart(); //Following is the method, that fetches all the items in the users cart

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

       //First, we will build a bottom sheet dialog to set the proper adress
        adresss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(CartActivity.this);
                View dialog= LayoutInflater.from(CartActivity.this).inflate(R.layout.layout_adress_input,null);

                //Following are teh fields for getting adress
                EditText houseNo=dialog.findViewById(R.id.houseNo_adressBottom);
                EditText roadNo=dialog.findViewById(R.id.roadNo_adressBottom);
                EditText locality=dialog.findViewById(R.id.locality_adressBottom);
                EditText pin=dialog.findViewById(R.id.pincode_adressBottom);
                EditText city=dialog.findViewById(R.id.city_adressBottom);
                EditText landmark=dialog.findViewById(R.id.landmark_adressBottom);
                EditText state=dialog.findViewById(R.id.state_adressBottom);
                Button addAdress=dialog.findViewById(R.id.addAdress_adressBottom);

                addAdress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(houseNo.getText().toString()))
                        {
                            makeToast("House No. Empty",0);
                        }
                        else if (TextUtils.isEmpty(roadNo.getText().toString()))
                        {
                            makeToast("Road No. Empty",0);
                        }
                        else if (TextUtils.isEmpty(locality.getText().toString()))
                        {
                            makeToast("Locality Empty",0);
                        }
                        else if (TextUtils.isEmpty(pin.getText().toString()) && pin.getText().toString().length()<6)
                        {
                            makeToast("Invalid/Empty PIN",0);
                        }
                        else if (TextUtils.isEmpty(city.getText().toString()))
                        {
                            makeToast("City Empty",0);
                        }
                        else if (TextUtils.isEmpty(landmark.getText().toString()))
                        {
                            makeToast("Landmark Empty",0);
                        }
                        else if (TextUtils.isEmpty(state.getText().toString()))
                        {
                            makeToast("State Empty",0);
                        }
                        else
                        {
                            adresss.setText(houseNo.getText()+", Road No."+roadNo.getText()+", "+locality.getText()+",\n"+city.getText()+"-"+pin.getText()+", "+state.getText()+"\n"+"("+landmark.getText()+")");
                            bottomSheetDialog.cancel();
                        }
                    }
                });
                bottomSheetDialog.setContentView(dialog);
                bottomSheetDialog.show();
            }
        });

        //Setting the radioButton for the payment mode
        radGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.onlineBtn_cart)
                {
                    MODE="online";
                }
                else if (checkedId==R.id.codBtn_cart)
                {
                    MODE="cod";
                }
            }
        });

        //Now, setting a on click listener to place the order
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TOTAL==0)
                {
                    makeToast("No items to order in cart",0);
                }
                else if (TextUtils.isEmpty(name.getText().toString()))
                {
                    makeToast("Please provide name for delivery",0);
                }
                else if (TextUtils.isEmpty(contact.getText().toString()))
                {
                    makeToast("Please provide contact for delivery",0);
                }
                else if (TextUtils.isEmpty(adresss.getText().toString()))
                {
                    makeToast("Please provide adress for delivery",0);
                }
                else if (MODE.equals(""))
                {
                    makeToast("Please choose a payment method",0);
                }
                else
                {
                    if (MODE.equals("online"))
                    {
                        //Here, the user needs to pay online, hence the payment gateway
                        Checkout co=new Checkout();
                        try {

                            JSONObject options=new JSONObject();
                            //Putting the name
                            options.put("name","TN-46");
                            //Putting the description
                            options.put("description","Payment for your Order");
                            //putting the currency
                            options.put("currency","INR");
                            //Amount is always in paisa so doing converson
                            double amount= TOTAL*100;
                            //putting the Total Amount
                            options.put("amount",amount);


                            //Craeting a jSon Object for contact and Email
                            JSONObject prefill=new JSONObject();
                            prefill.put("email",FirebaseAuth.getInstance().getCurrentUser().getEmail().toString()); //putting the user email
                            prefill.put("contact",contact.getText()); //putting the user phone
                            /////////////////////////////////////////////////////////////////

                            //puttinng that prefill to options
                            options.put("prefill",prefill);

                            //Now opening the checkout for payment gateway
                            co.open(CartActivity.this,options);


                        }
                        catch (Exception e)
                        {
                            String msg=e.getMessage().toString();
                            makeToast(msg,1);
                        }
                    }
                    else
                    {
                       placeTheOrder("cod");
                    }
                }
            }
        });



        //Setting a on click listener on autofill option
        autofill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tryToFindThePreviousDetails(); //Following is the method, that tries to fetch the existing orders to get the last details



            }
        });








    }

    private void tryToFindThePreviousDetails() {
        FirebaseDatabase.getInstance().getReference().child("Orders")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            for (DataSnapshot dataSnapshot: snapshot.getChildren())
                            {
                                Orders orders=dataSnapshot.getValue(Orders.class);
                                if (orders.getCustomerid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                {

                                    isFilled=true;

                                   //Now, filling upthe subsequesn data
                                    name.setText(orders.getDelname());
                                    contact.setText(orders.getDelcontact());
                                    adresss.setText(orders.getDeladress());

                                    break;
                                }
                            }


                            if (isFilled)
                            {
                                makeToast("Details Fetched!! Please wait, we will fill it in a second.",1);
                            }
                            else
                            {
                                makeToast("No previous details found!! Fill the details manually",1);

                            }




                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void placeTheOrder(String cod) {

        progressDialog = new ProgressDialog(CartActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show(); //Starting the progress dialog
        progressDialog.setContentView(R.layout.layout_loading);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        DatabaseReference orderRef=FirebaseDatabase.getInstance().getReference().child("Orders");

        String orderid=orderRef.push().getKey().toString();

        final Map<String,Object> map=new HashMap<>();

        map.put("orderid",orderid);
        map.put("customerid",FirebaseAuth.getInstance().getCurrentUser().getUid());

        //Getting the current date
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        map.put("date",date);

        //Getting current time
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        map.put("time",currentTime);

        map.put("total",String.valueOf(TOTAL));
        map.put("status","placed");

        //Now, detecting the mode of payment
        if (cod.equals("cod"))
        {
            map.put("payment","COD");
        }
        else
        {
            map.put("payment","paid");
        }


        map.put("delname",name.getText().toString());
        map.put("delcontact",contact.getText().toString());
        map.put("deladress",adresss.getText().toString());

        orderRef.child(orderid).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful())
                {

                    for (int var=0;var<list.size();var++)
                    {
                        Cart cart=list.get(var);

                        Map<String,Object> map2=new HashMap<>();

                        map2.put("name",cart.getName());
                        map2.put("unitprice",cart.getUnitprice());
                        map2.put("image",cart.getImage());

                        map2.put("variant",cart.getVariant());
                        map2.put("qty",cart.getQty());
                        map2.put("totalprice",cart.getTotalprice());
                        map2.put("cid",cart.getCid());
                        map2.put("sellerid",cart.getSellerid());

                        FirebaseDatabase.getInstance().getReference().child("Order Items").child(orderid).child(cart.getCid()).updateChildren(map2)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            FirebaseDatabase.getInstance().getReference().child("Cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful())
                                                    {
                                                        if (!CartActivity.this.isFinishing() && progressDialog != null) {
                                                            progressDialog.dismiss();
                                                        }

                                                        makeToast("Order Placed",0);
                                                        finish();
                                                        Intent i=new Intent(getApplicationContext(),PostOrderPlacedActivity.class);
                                                        i.putExtra("id",orderid);
                                                        startActivity(i);


                                                    }
                                                    else
                                                    {
                                                        progressDialog.dismiss();
                                                        makeToast(task.getException().toString(),1);
                                                    }
                                                }
                                            });
                                        }
                                        else
                                        {
                                            progressDialog.dismiss();
                                            makeToast(task.getException().toString(),1);
                                        }
                                    }
                                });



                    }





                }
                else
                {
                    progressDialog.dismiss();
                    makeToast(task.getException().toString(),1);
                }




            }
        });




    }

    private void fetchAllTheItemInTheCart() {
        FirebaseDatabase.getInstance().getReference().child("Cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (list!=null)
                        {
                            list.clear();
                            TOTAL=0;
                        }

                        if (snapshot.exists())
                        {
                            for (DataSnapshot dataSnapshot: snapshot.getChildren())
                            {
                                Cart cart=dataSnapshot.getValue(Cart.class);
                                list.add(cart);

                                //Here, in the loop, we have to keep calculating teh items
                                double tP=Double.parseDouble(cart.getTotalprice());

                                TOTAL=TOTAL+tP;


                            }

                            Collections.reverse(list);
                            adapter=new CartItemAdapter(list,getApplicationContext(),CartActivity.this,"cart");
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();


                            tottalPrice.setText("Rs "+TOTAL+"/-");





                        }
                        else
                        {
                            //Means the cart of the user is empty
                            finish();
                            makeToast("Cart Empty",0);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void init() {
        recyclerView=findViewById(R.id.recycler_cart);
        backBtn=findViewById(R.id.backBtn_cart);
        name=findViewById(R.id.name_cart);
        contact=findViewById(R.id.contact_cart);
        adresss=findViewById(R.id.adress_cart);

        radGrp=findViewById(R.id.radGrp_cart);
        onlineRad=findViewById(R.id.onlineBtn_cart);
        cashOnDeliveryRad=findViewById(R.id.codBtn_cart);

        tottalPrice=findViewById(R.id.totalPrice_cart);

        placeOrder=findViewById(R.id.placeOrder_cart);

        autofill=findViewById(R.id.autofill_cart);
    }

    public void makeToast(String textToShow, int lenght)
    {
        LayoutInflater inflater = CartActivity.this.getLayoutInflater();
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
    public void onPaymentSuccess(String s) {
        placeTheOrder("online");
    }

    @Override
    public void onPaymentError(int i, String s) {
        makeToast(s,1);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}