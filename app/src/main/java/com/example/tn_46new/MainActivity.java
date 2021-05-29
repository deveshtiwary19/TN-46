package com.example.tn_46new;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tn_46new.Admin.AdminHome;
import com.example.tn_46new.Admin.AdminLogin;
import com.example.tn_46new.Buyer.BuyerHome;
import com.example.tn_46new.Buyer.BuyerLoginActivity;
import com.example.tn_46new.seller.SellerHome;
import com.example.tn_46new.seller.SellerLoginActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private Button customerBtn;
    private Button sellerBtn;
    private TextView adminBtn;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Folllowing is the code to set the status bar as color
        Window window= MainActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.mainyellow));

        //Restricting the lanscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init(); //Folllowing is the method, to intialize all the object with  their respective ids

        linearLayout.setVisibility(View.INVISIBLE);
        linearLayout.setAlpha(0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                if (FirebaseAuth.getInstance().getCurrentUser() ==null)
                {
                    linearLayout.setVisibility(View.VISIBLE);
                    linearLayout.animate().alpha(1).setDuration(1500);
                }
                else
                {
                    //Checking the correct pannel and switchong  to the original home
                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists())
                                    {
                                        //Means the user is buyer
                                        Intent i=new Intent(getApplicationContext(),BuyerHome.class);
                                        startActivity(i);
                                        finish();
                                        makeToast("Welcome",0);
                                    }
                                    else
                                    {
                                        //Means the user is seller
                                        Intent i=new Intent(getApplicationContext(), SellerHome.class);
                                        startActivity(i);
                                        finish();
                                        makeToast("Welcome",0);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }





                //Getting the shared pref and checking for admin logged in or not
                SharedPreferences prefs = getSharedPreferences("AdminCred", MODE_PRIVATE);
                String name = prefs.getString("loggedin?", "No");

                if (name.equals("Yes"))
                {
                    Intent i=new Intent(getApplicationContext(), AdminHome.class);
                    startActivity(i);
                    finish();
                    makeToast("Welcome",0);
                }


            }
        },3000);

        FirebaseApp.initializeApp(getApplicationContext());




        //Now we will add on click listeners on all the three buttons
        customerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), BuyerLoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        sellerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), SellerLoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), AdminLogin.class);
                startActivity(i);
                finish();
            }
        });



    }

    private void init() {
        linearLayout=findViewById(R.id.l1_ss);
        adminBtn=findViewById(R.id.adminBtn_ss);
        customerBtn=findViewById(R.id.customerBtn_ss);
        sellerBtn=findViewById(R.id.sellerBtn_ss);
    }

    //Following is the method, to create a custom tost
    public void makeToast(String textToShow, int lenght)
    {
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
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