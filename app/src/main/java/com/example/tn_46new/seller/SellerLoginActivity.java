package com.example.tn_46new.seller;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tn_46new.Buyer.BuyerHome;
import com.example.tn_46new.Buyer.BuyerLoginActivity;
import com.example.tn_46new.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SellerLoginActivity extends AppCompatActivity {


    private TextView tile;
    private EditText email;
    private EditText password;
    private EditText confirmPass;
    private TextView forgotPassword;
    private Button loginBtn;
    private TextView login_register_link;

    private ProgressDialog progressDialog;

    private String MODE="login"; //Following is the variableto keep a check on the login or register function of the page

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);


        //Folllowing is the code to set the status bar as color
        Window window= SellerLoginActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(SellerLoginActivity.this, R.color.mainyellow));

        //Restricting the lanscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init();

        confirmPass.setVisibility(View.GONE);

        login_register_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MODE.equals("login"))
                {
                    MODE="register";
                    tile.setText("Sign Up");
                    loginBtn.setText("Create Account");
                    confirmPass.setVisibility(View.VISIBLE);
                    forgotPassword.setVisibility(View.GONE);
                    login_register_link.setText("Already a user?Login");

                }
                else
                {
                    MODE="login";
                    tile.setText("Sign In");
                    loginBtn.setText("Login");
                    confirmPass.setVisibility(View.GONE);
                    forgotPassword.setVisibility(View.VISIBLE);
                    login_register_link.setText("Not a user?Register");
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MODE.equals("login"))
                {
                    loginTheUser();
                }
                else
                {
                    registerTheUser();
                }
            }
        });

        //Teh forget passwod setup
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(SellerLoginActivity.this);
                View dialog= LayoutInflater.from(SellerLoginActivity.this).inflate(R.layout.layout_forget_password,null);

                EditText email=dialog.findViewById(R.id.email_forgetPassword);
                Button send=dialog.findViewById(R.id.sendLink_forgetPassword);

                //Now, setting up it
               send.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       if (TextUtils.isEmpty(email.getText().toString()))
                       {
                           makeToast("Email is empty",0);
                       }
                       else
                       {
                           progressDialog=new ProgressDialog(SellerLoginActivity.this);
                           progressDialog.setCanceledOnTouchOutside(false);
                           progressDialog.show(); //Starting the progress dialog
                           progressDialog.setContentView(R.layout.layout_loading);
                           progressDialog.setCancelable(false);
                           progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                           FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString())
                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           if (task.isSuccessful())
                                           {
                                               progressDialog.dismiss();
                                               bottomSheetDialog.cancel();
                                               makeToast("Link sent sucessfully",0);
                                           }
                                           else
                                           {
                                               progressDialog.dismiss();
                                               bottomSheetDialog.cancel();
                                               makeToast(task.getException().toString(),1);
                                           }
                                       }
                                   });
                       }
                   }
               });



                bottomSheetDialog.setContentView(dialog);
                bottomSheetDialog.show();
            }
        });


    }

    private void loginTheUser() {
        if (TextUtils.isEmpty(email.getText().toString()))
        {
            makeToast("Email is empty",0);
        }
        else if (TextUtils.isEmpty(password.getText().toString()))
        {
            makeToast("Password is empty",0);
        }
        else
        {
            progressDialog=new ProgressDialog(SellerLoginActivity.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show(); //Starting the progress dialog
            progressDialog.setContentView(R.layout.layout_loading);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {

                                FirebaseDatabase.getInstance().getReference().child("Sellers").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists())
                                                {
                                                    makeToast("Welcome Back",1);
                                                    Intent i=new Intent(getApplicationContext(),SellerHome.class);
                                                    startActivity(i);
                                                    finish();
                                                    progressDialog.dismiss();
                                                }
                                                else
                                                {
                                                    makeToast("Incorrect Pannel! Switch to customer pannel",1);
                                                    progressDialog.dismiss();
                                                    email.setText("");
                                                    password.setText("");
                                                    confirmPass.setText("");
                                                    FirebaseAuth.getInstance().signOut();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                            }
                            else
                            {
                                makeToast(task.getException().toString(),1);
                                progressDialog.dismiss();
                                email.setText("");
                                password.setText("");
                                confirmPass.setText("");
                            }
                        }
                    });
        }
    }

    private void registerTheUser() {
        if (TextUtils.isEmpty(email.getText().toString()))
        {
            makeToast("Email is empty",0);
        }
        else if (TextUtils.isEmpty(password.getText().toString()))
        {
            makeToast("Password is empty",0);
        }
        else if (TextUtils.isEmpty(confirmPass.getText().toString()))
        {
            makeToast("Please confirm Password",0);
        }
        else if (!(password.getText().toString()).equals(confirmPass.getText().toString()))
        {
            makeToast("Password mismatch",0);
        }
        else
        {
            progressDialog=new ProgressDialog(SellerLoginActivity.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show(); //Starting the progress dialog
            progressDialog.setContentView(R.layout.layout_loading);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(),confirmPass.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {

                                DatabaseReference userRef= FirebaseDatabase.getInstance().getReference().child("Sellers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                final Map<String,Object> map=new HashMap<>();
                                map.put("email",FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                map.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                map.put("reg","pending");

                                userRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            makeToast("Account Created Sucessfully",1);
                                            Intent i=new Intent(getApplicationContext(),SellerHome.class);
                                            startActivity(i);
                                            finish();
                                            progressDialog.dismiss();
                                        }
                                        else
                                        {
                                            makeToast(task.getException().toString(),1);
                                            progressDialog.dismiss();
                                            email.setText("");
                                            password.setText("");
                                            confirmPass.setText("");
                                        }
                                    }
                                });




                            }
                            else
                            {
                                makeToast(task.getException().toString(),1);
                                progressDialog.dismiss();
                                email.setText("");
                                password.setText("");
                                confirmPass.setText("");
                            }
                        }
                    });
        }
    }

    private void init() {
        tile=findViewById(R.id.title_sellerLogin);
        email=findViewById(R.id.email_sellerlogin);
        password=findViewById(R.id.password_sellerlogin);
        confirmPass=findViewById(R.id.confirmPassword_sellerlogin);
        forgotPassword=findViewById(R.id.forgetPassword_sellerlogin);
        loginBtn=findViewById(R.id.loginBtn_sellerlogin);
        login_register_link=findViewById(R.id.register_loginLink_sellerlogin);
    }

    public void makeToast(String textToShow, int lenght)
    {
        LayoutInflater inflater = SellerLoginActivity.this.getLayoutInflater();
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