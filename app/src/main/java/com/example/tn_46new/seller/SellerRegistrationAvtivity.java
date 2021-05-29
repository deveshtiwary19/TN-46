package com.example.tn_46new.seller;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.tn_46new.Models.SellerDetails;
import com.example.tn_46new.Models.Sellers;
import com.example.tn_46new.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SellerRegistrationAvtivity extends AppCompatActivity implements PaymentResultListener {

    //Following are the objects on the screen
    private ImageView backBtn;
    private EditText name;
    private EditText shopName;
    private EditText contact;
    private EditText email;
    private EditText adress;

    private TextView contactAdvice;
    private TextView registerAdvice;

    private Button registerBtn;

    private boolean isRegistered=false;

    private ProgressDialog progressDialog;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration_avtivity);


        //Folllowing is the code to set the status bar as color
        Window window= SellerRegistrationAvtivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(SellerRegistrationAvtivity.this, R.color.mainyellow));

        //Restricting the lanscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init(); //Following is the method, that initializess all the objects wit their respective ids


        //First we will check if user is registered
        FirebaseDatabase.getInstance().getReference().child("Sellers").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            Sellers sellers=snapshot.getValue(Sellers.class);

                            if (sellers.getReg().equals("done"))
                            {
                                isRegistered=true;

                                contactAdvice.setVisibility(View.GONE);
                                registerAdvice.setVisibility(View.GONE);
                                registerBtn.setText("Update");

                                fetchTheExistingDetails(); //Following is the method, that will fetch the existing details for the registered seller
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        /////////////////////////////////////////////////////////////////////////////////////////
        //Now, we have to check if user is already registered or not, and perform the edit or registration operation respectively
        if (isRegistered)
        {

        }


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRegistered)
                {
                    if (TextUtils.isEmpty(name.getText().toString()))
                    {
                        makeToast("Please enter the name",0);
                    }
                    else if (TextUtils.isEmpty(shopName.getText().toString()))
                    {
                        makeToast("Please enter the shop name",0);
                    }
                    else if (TextUtils.isEmpty(contact.getText().toString())  || contact.getText().toString().length()<10)
                    {
                        makeToast("Invalid/Empty Contact number",0);
                    }
                    else if (TextUtils.isEmpty(email.getText().toString()))
                    {
                        makeToast("Please enter the email",0);
                    }
                    else if (TextUtils.isEmpty(adress.getText().toString()))
                    {
                        makeToast("Please enter the shop adress",0);
                    }
                    else
                    {

                        //WE will have to update the sellers registration details
                        getANdUpdateDetails();



                    }
                }
                else
                {
                    if (TextUtils.isEmpty(name.getText().toString()))
                    {
                        makeToast("Please enter the name",0);
                    }
                    else if (TextUtils.isEmpty(shopName.getText().toString()))
                    {
                        makeToast("Please enter the shop name",0);
                    }
                    else if (TextUtils.isEmpty(contact.getText().toString())  || contact.getText().toString().length()<10)
                    {
                        makeToast("Invalid/Empty Contact number",0);
                    }
                    else if (TextUtils.isEmpty(email.getText().toString()))
                    {
                        makeToast("Please enter the email",0);
                    }
                    else if (TextUtils.isEmpty(adress.getText().toString()))
                    {
                        makeToast("Please enter the shop adress",0);
                    }
                    else
                    {

                        //WE will have to take the user to the registeration page payment gateway
                        Checkout co=new Checkout();
                        try {

                            JSONObject options=new JSONObject();
                            //Putting the name
                            options.put("name","TN-46");
                            //Putting the description
                            options.put("description","Payment for seller Registration");
                            //putting the currency
                            options.put("currency","INR");
                            //Amount is always in paisa so doing converson
                            double amount= 50*100;
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
                            co.open(SellerRegistrationAvtivity.this,options);


                        }
                        catch (Exception e)
                        {
                            String msg=e.getMessage().toString();
                            makeToast(msg,1);
                        }



                    }
                }
            }
        });


















    }

    private void fetchTheExistingDetails() {
        FirebaseDatabase.getInstance().getReference().child("Seller Registration").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if (snapshot.exists())
                       {
                           SellerDetails sellerDetails=snapshot.getValue(SellerDetails.class);

                           //Setting the data accordingly
                           name.setText(sellerDetails.getName());
                           shopName.setText(sellerDetails.getShopname());
                           contact.setText(sellerDetails.getContact());
                           email.setText(sellerDetails.getEmail());
                           adress.setText(sellerDetails.getAdress());


                       }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getANdUpdateDetails() {

        progressDialog=new ProgressDialog(SellerRegistrationAvtivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show(); //Starting the progress dialog
        progressDialog.setContentView(R.layout.layout_loading);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        final Map<String,Object>map=new HashMap<>();

        map.put("name",name.getText().toString());
        map.put("shopname",shopName.getText().toString());
        map.put("contact",contact.getText().toString());
        map.put("email",email.getText().toString());
        map.put("adress",adress.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("Seller Registration").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    final Map<String,Object>map1=new HashMap<>();

                    map1.put("reg","done");

                    FirebaseDatabase.getInstance().getReference().child("Sellers").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .updateChildren(map1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                progressDialog.dismiss();
                    if (isRegistered)
                    {
                        makeToast("Details Updated",0);
                        finish();
                    }
                    else
                    {
                        makeToast("Registration sucessfull",0);
                        finish();
                    }
                            }
                            else
                            {
                                makeToast(task.getException().toString(),0);
                                progressDialog.dismiss();
                            }

                        }
                    });


                }
                else
                {
                    makeToast(task.getException().toString(),0);
                    progressDialog.dismiss();
                }
            }
        });




    }

    private void init() {

        backBtn=findViewById(R.id.backBtn_sellerReg);

        name=findViewById(R.id.name_sellerReg);
        shopName=findViewById(R.id.shopName_sellerReg);
        contact=findViewById(R.id.contact_sellerReg);
        email=findViewById(R.id.email_sellerReg);
        adress=findViewById(R.id.shopAdress_sellerReg);

        contactAdvice=findViewById(R.id.contactAdvice_sellerReg);
        registerAdvice=findViewById(R.id.registerAdvice_sellerReg);

        registerBtn=findViewById(R.id.registerBtn_sellerReg);


    }


    public void makeToast(String textToShow, int lenght)
    {
        LayoutInflater inflater = SellerRegistrationAvtivity.this.getLayoutInflater();
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
        getANdUpdateDetails();
    }

    @Override
    public void onPaymentError(int i, String s) {

        makeToast(s,1);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}