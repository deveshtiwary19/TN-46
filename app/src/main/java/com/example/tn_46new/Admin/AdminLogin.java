package com.example.tn_46new.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.tn_46new.Buyer.BuyerLoginActivity;
import com.example.tn_46new.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminLogin extends AppCompatActivity {

    //Following are the objects on the scrren
    private EditText email;
    private EditText password;
    private Button loginBtn;

    //The variables to fetch and store the credentials
    private String emailC="";
    private String passC="";

    public static final String MY_PREFS_NAME = "AdminCred";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        //Folllowing is the code to set the status bar as color
        Window window= AdminLogin.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(AdminLogin.this, R.color.mainyellow));

        //Restricting the lanscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        email=findViewById(R.id.email_adminLogin);
        password=findViewById(R.id.password_adminLogin);
        loginBtn=findViewById(R.id.loginBtn_adminLogin);

        FirebaseDatabase.getInstance().getReference().child("Admin").child("email")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            emailC=snapshot.getValue().toString();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        FirebaseDatabase.getInstance().getReference().child("Admin").child("password")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            passC=snapshot.getValue().toString();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    if (email.getText().toString().equals("tn46.database@gmail.com") && password.getText().toString().equals("asdf@1234"))
                    {
                        makeToast("Welcome",0);
                        Intent i=new Intent(getApplicationContext(),AdminHome.class);
                        startActivity(i);
                        finish();
                        FirebaseAuth.getInstance().signOut();

                        //Putting the shared pref
                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("loggedin?","Yes");

                        editor.apply();

                    }
                    else
                    {
                        makeToast("Incorrect Credentials",0);
                    }
                }
            }
        });

    }

    public void makeToast(String textToShow, int lenght)
    {
        LayoutInflater inflater = AdminLogin.this.getLayoutInflater();
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