package com.hfad.tailorme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.CustomerFragmentAdapter;
import Adapter.CustomerResultAdapter;
import Database.Customer;
import Database.CustomerDatabase;
import utils.VerticalSpaceItemDecoration;

public class CustomerDetails extends AppCompatActivity {
    List<Customer> customerList;
    CustomerResultAdapter adapter;
    RecyclerView customerDetails;
    Customer listing;
    TextInputEditText names, phoneNumbers, styles, backs;
    TextInputEditText busts, waists, hipss, fullLengthOfGowns;
    TextInputEditText lengthOfSkirts, lengthOfBlouses, sleeves, roundSleeves;
    ArrayList<String> data, keys;
    ExtendedFloatingActionButton cloudButton;
    public static final String GET_POS = "GET_POS";
    public static final int VERTICAL_ITEM_SPACE=40;

    String name;
    String phoneNumber;
    String style;
    String back;
    String bust;
    String waist;
    String hips;
    String fullLengthOfGown;
    String lengthOfSkirt;
    String lengthOfBlouse;
    String sleeve;
    String roundSleeve;
    CustomerInfo customerInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);
        //cloudButton = findViewById(R.id.cloudUploadButton);
        customerDetails = findViewById(R.id.recycler_result);

        int position = getIntent().getExtras().getInt(GET_POS);


        customerList = retrieveData();
        if(customerList!=null) {

                Customer listing = customerList.get(position);
                 name = listing.getCustomerName();
                 phoneNumber = listing.getPhoneNumber();
                 style = listing.getCustomerStyle();
                 back = listing.getBack();
                 bust = listing.getBust();
                 waist = listing.getWaist();
                 hips = listing.getCustomerHips();
                 fullLengthOfGown = listing.getFullLengthOfGown();
                 lengthOfSkirt = listing.getLengthOfSkirt();
                 lengthOfBlouse = listing.getLengthOfBlouse();
                 sleeve = listing.getSleeve();
                 roundSleeve = listing.getRoundSleeve();

                 data = new ArrayList<String>();
                 data.add(style);
                 data.add(back);
                 data.add(bust);
                 data.add(waist);
                 data.add(hips);
                 data.add(fullLengthOfGown);
                 data.add(lengthOfSkirt);
                 data.add(lengthOfBlouse);
                 data.add(sleeve);
                 data.add(roundSleeve);

                 keys = new ArrayList<String>();
                 keys.add("Style");
                 keys.add("Back");
                 keys.add("Bust");
                 keys.add("Waist");
                 keys.add("Hips");
                 keys.add("Full length of gown");
                 keys.add("Length of skirt");
                 keys.add("Length of blouse");
                 keys.add("Sleeve");
                 keys.add("Round Sleeve");



        }

         initializeAdapter(data,keys);




        cloudButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadCustomerToCloud(customerInfo);
            }
        });







    }

    public List<Customer> retrieveData(){
        CustomerDatabase appDb = CustomerDatabase.getInstance(this);
        try {
            return  appDb.customerDao().getCustomerList();
        }catch(Exception e){
            Toast.makeText(this,"Empty Database",Toast.LENGTH_LONG).show();
        }


        return null;

    }

    public void uploadCustomerToCloud(CustomerInfo customerInfo){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // get reference
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User/Customer Info");
        ref.child(user.getUid()+"/"+phoneNumber).setValue(customerInfo);

    }


    public void initializeAdapter(ArrayList<String> names, ArrayList<String> phoneNumbers){
        adapter = new CustomerResultAdapter(names,phoneNumbers);
        customerDetails.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        customerDetails.setLayoutManager(layoutManager);
        customerDetails.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));

    }

    public void getDataFromDatabase(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // get reference
        DatabaseReference ref =FirebaseDatabase.getInstance().getReference()
                .child(user.getUid());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                //final Profile p = dataSnapshot.getValue(Profile.class);



            }

            @Override
            public void onCancelled( DatabaseError error) {

            }
        });
    }


}