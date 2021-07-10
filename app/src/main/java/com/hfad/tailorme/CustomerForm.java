package com.hfad.tailorme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;

import Adapter.CustomerFragmentAdapter;
import Database.Customer;
import Database.CustomerDatabase;
import Database.DatabaseOperation;

public class CustomerForm extends AppCompatActivity

{
    Customer customer;
    String name;
    String phoneNumber;
    String style, fullLengthOfGown;
    String back,  lengthOfSkirt;
    String bust,  lengthOfBlouse;
    String waist;
    String hips;
    String sleeve;
    String roundSleeve;
    TextInputEditText nameText, phoneNumberText, styleText,backText,bustText;
    TextInputEditText waistText, hipsText, sleeveText,roundSleeveText, fullLengthOfGownText;
    TextInputEditText lengthOfSkirtText, lengthOfBlouseText, date;
    Button addButton;
    Boolean inProgress;
    DatabaseOperation databaseOperation;
    CustomerFragmentAdapter adapter;
    MaterialDatePicker<Long> datePicker;
    String dateString;
    long chosenDate;
    String uniqueTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_form);

        addButton = findViewById(R.id.addButton);
        nameText = (TextInputEditText) findViewById(R.id.customerName);
        phoneNumberText = (TextInputEditText)findViewById(R.id.phoneNumber);
        styleText =(TextInputEditText) findViewById(R.id.customerStyle);
        backText = (TextInputEditText)findViewById(R.id.back);
        bustText = (TextInputEditText)findViewById(R.id.bust);
        waistText = (TextInputEditText)findViewById(R.id.waist);
        hipsText = (TextInputEditText)findViewById(R.id.hips);
        sleeveText = (TextInputEditText)findViewById(R.id.sleeve);
        roundSleeveText = (TextInputEditText)findViewById(R.id.sleeve);
        fullLengthOfGownText = (TextInputEditText)findViewById(R.id.fullLengthForGown);
        lengthOfBlouseText = (TextInputEditText)findViewById(R.id.lengthOfBlouse);
        lengthOfSkirtText = (TextInputEditText)findViewById(R.id.lengthOfSkirt);
        date = findViewById(R.id.date);
        date.setInputType(InputType.TYPE_NULL);
        inProgress = false;


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });

         uniqueTime = addTimeStamp();



           addButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   name = nameText.getText().toString();
                   phoneNumber = phoneNumberText.getText().toString();
                   style = styleText.getText().toString();
                   back = backText.getText().toString();
                   bust = bustText.getText().toString();
                   waist = waistText.getText().toString();
                   sleeve = sleeveText.getText().toString();
                   hips = hipsText.getText().toString();
                   roundSleeve = roundSleeveText.getText().toString();
                   fullLengthOfGown = fullLengthOfGownText.getText().toString();
                   lengthOfBlouse = lengthOfBlouseText.getText().toString();
                   lengthOfSkirt = lengthOfSkirtText.getText().toString();
                   customer = new Customer();
                   customer.setBack(back);
                   customer.setBust(bust);
                   customer.setCustomerHips(hips);
                   customer.setCustomerStyle(style);
                   customer.setCustomerName(name);
                   customer.setRoundSleeve(roundSleeve);
                   customer.setSleeve(sleeve);
                   customer.setWaist(waist);
                   customer.setPhoneNumber(phoneNumber);
                   customer.setFullLengthOfGown(fullLengthOfGown);
                   customer.setLengthOfBlouse(lengthOfBlouse);
                   customer.setLengthOfSkirt(lengthOfSkirt);
                   customer.setInProgress(inProgress);
                   customer.setDate(dateString);
                   customer.setTime(uniqueTime);
                   customer.setLongTime(chosenDate);


                   // Respond to positive button click.

                   if(dateString!=null&&!name.isEmpty()&&!phoneNumber.isEmpty())  {
                       try {
                           CustomerDatabase appDb = CustomerDatabase.getInstance(CustomerForm.this);
                           appDb.customerDao().insertCustomer(customer);
                           Intent intent = new Intent(CustomerForm.this, HomePage.class);
                           startActivity(intent);
                       }catch(SQLiteConstraintException e){

                           Intent intent = new Intent(CustomerForm.this, HomePage.class);
                           startActivity(intent);
                       }


                   }else{
                       Toast.makeText(CustomerForm.this, "Required Fields are empty", Toast.LENGTH_LONG).show();
                   }

               }

           });



    }



    public void pickDate(){
        datePicker =  MaterialDatePicker.Builder.datePicker()
               .setTitleText("Select Date")
               .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
               .build();

       datePicker.show(getSupportFragmentManager(),"1");

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                chosenDate = datePicker.getSelection();
                String Scd =  String.valueOf(chosenDate);
                dateString = DateFormat.format("MM/dd/yyyy", new Date(chosenDate)).toString();
                Log.d("pickDate", dateString);

            }
        });





    }

    public String addTimeStamp(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return timeStamp;
    }


}