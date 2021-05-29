package com.example.tn_46new.Buyer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Placeholder;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tn_46new.OrderSummary;
import com.example.tn_46new.R;

public class PostOrderPlacedActivity extends AppCompatActivity {

    private ImageView backBtn;
    private TextView orderId;
    private TextView viewBtn;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_order_placed);

        Intent i=getIntent();
        String orderid=i.getStringExtra("id");

        //Folllowing is the code to set the status bar as color
        Window window= PostOrderPlacedActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(PostOrderPlacedActivity.this, R.color.mainyellow));

        //Restricting the lanscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init(); //FOllowing is the method, to initialize all the objects with their respective ids

        orderId.setText("Order id: "+orderid);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), OrderSummary.class);
                i.putExtra("user","buyer");
                i.putExtra("id",orderid);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });


    }

    private void init() {
        backBtn=findViewById(R.id.backBtn_postO);
        viewBtn=findViewById(R.id.viewBtn_postO);
        orderId=findViewById(R.id.orderID_postO);
    }
}