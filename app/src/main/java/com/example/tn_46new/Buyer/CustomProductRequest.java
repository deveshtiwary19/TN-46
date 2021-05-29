package com.example.tn_46new.Buyer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.tn_46new.Admin.AdminHome;
import com.example.tn_46new.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CustomProductRequest extends AppCompatActivity {

    //Following are the objects on the screen
    private ImageView backBtn;
    private ImageView imageView;
    private TextView imageAdvice;
    private EditText productName;
    private EditText productDescri;
    private EditText productLink;
    private EditText contact;
    private Button reqquestBtn;

    //Following is the object for the progress dialog
    private ProgressDialog progressDialog;

    //Var to get operating decision values from the intent
    private String USER_TYPE="";
    private String id="";

    //Following are the variables for the image picking function
    private int RequestCode = 438;

    private String downloadImageUrl = "null";
    private Uri ImageUri=null;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_product_request);

        //Folllowing is the code to set the status bar as color
        Window window= CustomProductRequest.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(CustomProductRequest.this, R.color.mainyellow));

        //Restricting the lanscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init(); //Following is the method, to initialize all the objects with their respective ids

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //////////////////////////////////////////////////////////////////////////////////////////
        //Here, we need to ask for read and write external storage permission
        if (ContextCompat.checkSelfPermission(CustomProductRequest.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            //Here, it means that location permission is not granted yet and we need to get it
            ActivityCompat.requestPermissions(CustomProductRequest.this,new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            },100);

        }

        //////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        //Setting a click listener for place request button
        reqquestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(productName.getText().toString()))
                {
                    makeToast("Provide Product name",0);
                }
                else if (TextUtils.isEmpty(productDescri.getText().toString()))
                {
                    makeToast("Provide Product description",0);
                }

                else if (TextUtils.isEmpty(contact.getText().toString()))
                {
                    makeToast("Please give contact number to reach you",0);
                }
                else if (TextUtils.isEmpty(productLink.getText().toString()))
                {
                    makeToast("Provide a link from internet",0);
                }
                else
                {
                    placeTheRequest(); //Following is the method, that places the request
                }
            }
        });





    }

    private void placeTheRequest() {
        progressDialog = new ProgressDialog(CustomProductRequest.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show(); //Starting the progress dialog
        progressDialog.setContentView(R.layout.layout_loading);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        if (ImageUri==null)
        {
            //No, need to upload image, directly place request
            DatabaseReference cRef=FirebaseDatabase.getInstance().getReference().child("Custom Orders");

            String uid=cRef.push().getKey();

            final Map<String,Object>map=new HashMap<>();
            map.put("image","null");
            map.put("name",productName.getText().toString());
            map.put("link",productLink.getText().toString());
            map.put("descri",productDescri.getText().toString());
            map.put("contact",contact.getText().toString());
            map.put("orderid",uid);
            map.put("status","placed");
            map.put("customerid", FirebaseAuth.getInstance().getCurrentUser().getUid());

            cRef.child(uid).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        makeToast("Custom Order Request Placed",1);

                        finish();
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
            //Upload the image, then place request
            final String productRandomKey = Calendar.getInstance().getTime().toString();


            final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("Custom Product Images").child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

            final UploadTask uploadTask = filePath.putFile(ImageUri);

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


                                downloadImageUrl = filePath.getDownloadUrl().toString();

                                return filePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                                downloadImageUrl = task.getResult().toString();

                                //Now, the image is uplaoded and the link is stored, Now place the order(custom)

                            DatabaseReference cRef=FirebaseDatabase.getInstance().getReference().child("Custom Orders");

                            String uid=cRef.push().getKey();

                            final Map<String,Object>map=new HashMap<>();
                            map.put("image",downloadImageUrl);
                            map.put("name",productName.getText().toString());
                            map.put("link",productLink.getText().toString());
                            map.put("descri",productDescri.getText().toString());
                            map.put("contact",contact.getText().toString());
                            map.put("orderid",uid);
                            map.put("customerid", FirebaseAuth.getInstance().getCurrentUser().getUid());

                            cRef.child(uid).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        progressDialog.dismiss();
                                        makeToast("Custom Order Request Placed",1);

                                        finish();
                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        makeToast(task.getException().toString(),1);
                                    }
                                }
                            });





                        }
                    });
                }


            });
        }
    }

    private void init() {
        backBtn=findViewById(R.id.backBtn_customProduct);
        imageAdvice=findViewById(R.id.imageAdvice_customProduct);
        imageView=findViewById(R.id.image_customProduct);
        productName=findViewById(R.id.name_customProduct);
        productDescri=findViewById(R.id.descri_customProduct);
        productLink=findViewById(R.id.link_customProduct);
        contact=findViewById(R.id.contact_customProduct);
        reqquestBtn=findViewById(R.id.requestBtn_customProduct);

    }
    public void makeToast(String textToShow, int lenght)
    {
        LayoutInflater inflater = CustomProductRequest.this.getLayoutInflater();
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

    //Following are the functions, to get the image from the gallery
    private void pickImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, RequestCode);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == RequestCode && resultCode == Activity.RESULT_OK && data.getData() != null) {


                ImageUri = data.getData();

                //Setting up the selected image, in the imageview
                 Picasso.get().load(ImageUri).into(imageView);

        } else {

            makeToast("Image Not Selected",0);
        }


        super.onActivityResult(requestCode, resultCode, data);
    }


}