package com.example.tn_46new;

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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tn_46new.Adapters.NewsJobAdapter;
import com.example.tn_46new.Adapters.ProductsAdapter;
import com.example.tn_46new.Admin.AdminHome;
import com.example.tn_46new.Buyer.CartActivity;
import com.example.tn_46new.Models.NewsJobs;
import com.example.tn_46new.Models.Products;
import com.example.tn_46new.seller.AddProduct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class NewsJobActivity extends AppCompatActivity {

    //Following are the objects on the screen
    private ImageView backBtn;
    private TextView heading;
    private EditText searchBar;
    private RecyclerView recyclerView;
    private FloatingActionButton addBtn;

    private ProgressDialog progressDialog;
    private ProgressDialog progressDialog2;

    //Following are the objects for the recycler view operation
    private List<NewsJobs> list;
    private NewsJobAdapter adapter;

    //Following are the variables that the activit will need from the intent
    private String USER_TYPE="";
    private String type="";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_job);

        //Folllowing is the code to set the status bar as color
        Window window= NewsJobActivity.this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(NewsJobActivity.this, R.color.mainyellow));

        //Restricting the lanscape orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init(); //Following is the methpd, that initializes all the objects with their respective ids

        //Getting the required values from the intent
        Intent i=getIntent();
        USER_TYPE=i.getStringExtra("user");
        type=i.getStringExtra("type");

        //Setting the heading
        if (type.equals("news"))
        {
            heading.setText("News");
        }
        else
        {
            heading.setText("Jobs");
        }

        //Now, setting up the add btn
        if (USER_TYPE.equals("admin"))
        {
            addBtn.setVisibility(View.VISIBLE);
        }
        else
        {
            addBtn.setVisibility(View.GONE);
        }

        //The back btn
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Now, we have to display a bottom sheet for adding news/Job
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(NewsJobActivity.this);
                View dialog= LayoutInflater.from(NewsJobActivity.this).inflate(R.layout.layout_add_news_jobs,null);

                //Following are the objects on the bottom sheet dialog
                ImageView backBtn=dialog.findViewById(R.id.backBtn_newsJobs_bs);
                TextView heading=dialog.findViewById(R.id.heading_newsJobs_bs);
                EditText title=dialog.findViewById(R.id.title_newsJobs_bs);
                EditText descri=dialog.findViewById(R.id.descri_newsJobs_bs);
                Button postBtn=dialog.findViewById(R.id.postBtn_newsJobs_bs);

                if (type.equals("news"))
                {
                    heading.setText("Add News");
                }
                else
                {
                    heading.setText("Add Jobs");
                }

                postBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(title.getText().toString()))
                        {
                            makeToast("Title is empty",0);
                        }
                        else if (TextUtils.isEmpty(descri.getText().toString()))
                        {
                            makeToast("Description is empty",0);
                        }
                        else
                        {
                            progressDialog = new ProgressDialog(NewsJobActivity.this);
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show(); //Starting the progress dialog
                            progressDialog.setContentView(R.layout.layout_loading);
                            progressDialog.setCancelable(false);
                            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                            DatabaseReference njRef= FirebaseDatabase.getInstance().getReference().child("News-Jobs");
                            String uid=njRef.push().getKey();

                            final Map<String,Object> map=new HashMap<>();
                            map.put("title",title.getText().toString());
                            map.put("descri",descri.getText().toString());
                            map.put("type",type);
                            map.put("uid",uid);
                            //Getting the current date
                            String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                            map.put("date",date);

                            //Getting current time
                            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                            map.put("time",currentTime);
                            map.put("search",title.getText().toString().toLowerCase());

                            njRef.child(uid).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        progressDialog.dismiss();
                                        bottomSheetDialog.cancel();
                                        makeToast("Posting Sucessful",0);
                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        bottomSheetDialog.cancel();
                                        makeToast(task.getException().toString(),0);
                                    }
                                }
                            });




                        }
                    }
                });

                backBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.cancel();
                    }
                });








                bottomSheetDialog.setContentView(dialog);
                bottomSheetDialog.show();
            }
        });


        //Setting up the recycler view
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        list=new ArrayList<>();

        fetchItemsAsRequired(); //Following is the method, that fetches all the

        //Now, we need to set up the search box
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchForDesired(s.toString().toLowerCase()); //Following is the method, that adds the serach operation
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });







    }

    private void searchForDesired(String toLowerCase) {

        Query searchQuery= FirebaseDatabase.getInstance().getReference().child("News-Jobs").orderByChild("search").startAt(toLowerCase).endAt(toLowerCase+"\uf8ff");
        searchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(list!=null)
                {
                    list.clear();
                }

                if (snapshot.exists())
                {


                    for (DataSnapshot dataSnapshot:snapshot.getChildren())
                    {
                        NewsJobs newsJobs=dataSnapshot.getValue(NewsJobs.class);
                        if (type.equals("news"))
                        {
                            if (newsJobs.getType().equals("news"))
                            {
                                list.add(newsJobs);
                            }
                        }
                        else
                        {
                            if (newsJobs.getType().equals("jobs"))
                            {
                                list.add(newsJobs);
                            }
                        }
                    }

                    Collections.reverse(list);
                    adapter=new NewsJobAdapter(list,getApplicationContext(),NewsJobActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    progressDialog2.dismiss();


                }
                else
                {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void fetchItemsAsRequired() {
        progressDialog2 = new ProgressDialog(NewsJobActivity.this);
        progressDialog2.setCanceledOnTouchOutside(false);
        progressDialog2.show(); //Starting the progress dialog
        progressDialog2.setContentView(R.layout.layout_loading);
        progressDialog2.setCancelable(false);
        progressDialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        FirebaseDatabase.getInstance().getReference().child("News-Jobs")
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
                                NewsJobs newsJobs=dataSnapshot.getValue(NewsJobs.class);
                                if (type.equals("news"))
                                {
                                    if (newsJobs.getType().equals("news"))
                                    {
                                        list.add(newsJobs);
                                    }
                                }
                                else
                                {
                                    if (newsJobs.getType().equals("jobs"))
                                    {
                                        list.add(newsJobs);
                                    }
                                }
                            }

                            Collections.reverse(list);
                            adapter=new NewsJobAdapter(list,getApplicationContext(),NewsJobActivity.this);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            progressDialog2.dismiss();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



    }

    private void init() {
        backBtn=findViewById(R.id.backBtn_newsJobs);
        heading=findViewById(R.id.title_newsJobs);
        searchBar=findViewById(R.id.searchBar_newsJobs);
        recyclerView=findViewById(R.id.recycler_newsJobs);
        addBtn=findViewById(R.id.addBtn_newsJobs);
    }


    public void makeToast(String textToShow, int lenght)
    {
        LayoutInflater inflater = NewsJobActivity.this.getLayoutInflater();
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