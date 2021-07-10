package com.hfad.tailorme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import Adapter.CustomerFragmentAdapter;
import Database.Customer;
import Database.CustomerDatabase;
import utils.VerticalSpaceItemDecoration;


public class CustomerFragment
        extends Fragment implements DownloadCustomerService.Callback{
    RecyclerView customerCard;
    View view;
    CustomerFragmentAdapter adapter, searchAdapter;
    Button searchButton;
    List<Customer> customerList;
    ArrayList<String> searchName;
    ArrayList<String> searchPhoneNumber;
    ArrayList<String> searchDate;
    ArrayList<String> names, phoneNumbers, dates, backs;
    List<Customer> listings;
    LinearLayout cloudLoaderLayout;
    private static final int VERTICAL_ITEM_SPACE = 0;
    String[] filter = {"Customer Name","Phone Number", "Date" };
    MaterialDatePicker<Long> datePicker;
    Spinner spin;
    EditText searchBox;
    Listener listener;
    boolean isCustomerName;
    boolean isPhoneNumber;
    boolean isDate;
    long chosenStartDate;
    long chosenEndDate;
    Boolean onAppear;






    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_customer, container, false);
        customerCard= view.findViewById(R.id.customerCard);
        spin  = (Spinner) view.findViewById(R.id.spinner);
        cloudLoaderLayout = view.findViewById(R.id.cloudLoaderLayout);
        searchBox = view.findViewById(R.id.searchBox);

        isCustomerName = true;
        isPhoneNumber = false;
        isDate = true;
        names = new ArrayList<String>();
        phoneNumbers = new ArrayList<String>();
        dates = new ArrayList<String>();
        onAppear = false;


        getAppear(false);
        commitDatabase();

        initializeSearchFilter();




          searchButton = view.findViewById(R.id.searchButton);
          searchButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  if(isDate) {
                      searchName = new ArrayList<>();
                      searchPhoneNumber = new ArrayList<>();
                      searchDate = new ArrayList<>();
                      Toast.makeText(getContext(), "i am on",Toast.LENGTH_LONG).show();

                      CustomerDatabase appDb = CustomerDatabase.getInstance(getContext());
                      List<Customer> customers = appDb.customerDao().
                              getDateSearch(chosenStartDate, chosenEndDate);

                      for (int i = 0; i < customers.size(); i++) {
                          Customer customer = customers.get(i);
                          String name = customer.getCustomerName();
                          String phoneNumber = customer.getPhoneNumber();
                          String date = customer.getDate();
                          searchName.add(name);
                          searchPhoneNumber.add(phoneNumber);
                          searchDate.add(date);

                      }
                      initializeAdapter(searchName,searchPhoneNumber,searchDate);

                      //initializeSearchAdapter(searchName,searchPhoneNumber,searchDate);
                  }

                  if(isPhoneNumber){
                      searchName = new ArrayList<>();
                      searchPhoneNumber = new ArrayList<>();
                      searchDate = new ArrayList<>();
                      Toast.makeText(getContext(), "i am phone",Toast.LENGTH_LONG).show();

                      String search = searchBox.getText().toString();
                      CustomerDatabase appDb = CustomerDatabase.getInstance(getContext());
                      List<Customer> customers = appDb.customerDao().getPhone(search);
                      for (int i = 0; i < customers.size(); i++) {
                          Customer customer = customers.get(i);
                          String name = customer.getCustomerName();
                          String phoneNumber = customer.getPhoneNumber();
                          String date = customer.getDate();
                          searchName.add(name);
                          searchPhoneNumber.add(phoneNumber);
                          searchDate.add(date);

                      }
                      initializeAdapter(searchName,searchPhoneNumber,searchDate);
                      initializeSearchFilter();


                  }
                  if(isCustomerName){
                      searchName = new ArrayList<>();
                      searchPhoneNumber = new ArrayList<>();
                      searchDate = new ArrayList<>();
                      Toast.makeText(getContext(), "i am name",Toast.LENGTH_LONG).show();

                      String search = searchBox.getText().toString();
                      CustomerDatabase appDb = CustomerDatabase.getInstance(getContext());
                      List<Customer> customers = appDb.customerDao().getNameSearch(search);
                      for (int i = 0; i < customers.size(); i++) {
                          Customer customer = customers.get(i);
                          String name = customer.getCustomerName();
                          String phoneNumber = customer.getPhoneNumber();
                          String date = customer.getDate();
                          searchName.add(name);
                          searchPhoneNumber.add(phoneNumber);
                          searchDate.add(date);

                      }
                      initializeAdapter(searchName,searchPhoneNumber,searchDate);
                      initializeSearchFilter();


                  }
              }
          });
        return view;
    }

    public List<Customer> retrieveData(){
        CustomerDatabase appDb = CustomerDatabase.getInstance(getContext());
        try {
            customerList = appDb.customerDao().getCustomerList();
            return customerList;
        }catch(Exception e){
            Toast.makeText(getContext(),"Empty Database",Toast.LENGTH_LONG).show();
        }
        return null;
    }

    public void commitAdapter(){
        adapter.setListener(new CustomerFragmentAdapter.Listener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(getContext(),CustomerDetails.class);
                intent.putExtra(CustomerDetails.GET_POS, position );
                startActivity(intent);

            }
            @Override
            public void onDelete(int position) {
                Customer customer = customerList.get(position);
                CustomerDatabase appDb = CustomerDatabase.getInstance(getContext());
                appDb.customerDao().deleteCustomer(customer);
                adapter.notifyItemRemoved(position);


            }
        });
    }
    public void initializeSearchFilter(){
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==1){
                    isPhoneNumber = true;
                    isCustomerName = false;
                    isDate = false;
                    initializeSearchFilter();

                }
                if(position==0){
                    isCustomerName = true;
                    isPhoneNumber = false;
                    isDate = false;
                    initializeSearchFilter();
                }

                if(position==2) {
                    isDate = true;
                    isPhoneNumber = false;
                    isCustomerName = false;
                    pickStartDate();
                    initializeSearchFilter();
                }

                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter aa = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,filter);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

    }

    public void initializeSearchAdapter(ArrayList<String> searchName,
                                        ArrayList<String> searchPhoneNumber,
                                        ArrayList<String> searchDate){
        searchAdapter = new CustomerFragmentAdapter(searchName,searchPhoneNumber,searchDate);
        customerCard.setAdapter(searchAdapter);

        LinearLayoutManager  layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        customerCard.setLayoutManager(layoutManager);
        customerCard.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));

    }
    @Override
    public void onResume(){
        super.onResume();
        commitDatabase();
        initializeAdapter(names, phoneNumbers, dates);
        commitAdapter();
        getAppear(true);
        Log.d("oaf oaf",  onAppear.toString());

    }

    @Override
    public void onPause(){
        super.onPause();
        adapter=null;
        isDate = false;
        getAppear(false);

        searchAdapter = null;

    }


    public void initializeAdapter(ArrayList<String> names, ArrayList<String>
            phoneNumbers, ArrayList<String> dates){
        adapter = new CustomerFragmentAdapter(names,phoneNumbers,dates);
        customerCard.setAdapter(adapter);

        LinearLayoutManager  layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        customerCard.setLayoutManager(layoutManager);
        customerCard.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));


    }

    public void addCustomersToArrayList(List<Customer> listings){
        for(int i=0;i<listings.size();i++) {
            Customer listing = listings.get(i);
            String name = listing.getCustomerName();
            String phoneNumber = listing.getPhoneNumber();
            String date = listing.getDate();
            names.add(name);
            phoneNumbers.add(phoneNumber);
            dates.add(date);
        }



    }

    @Override
    public void onOperationProgress(int progress) {

    }

    @Override
    public void onOperationCompleted() {
        commitDatabase();
    }

    @Override
    public List<Customer> onDownloadOperationStart() {
        return null;
    }


    public interface Listener{
        public void lonAppear(Boolean onAppear);
        void backUp();
    }

    public void setListener(Listener listener){

        this.listener = listener;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (Listener) context;
    }


    public void getAppear(Boolean onAppear){
        listener.lonAppear(onAppear);
    }


    public void pickStartDate(){
        datePicker =  MaterialDatePicker.Builder.datePicker()
                .setTitleText("FROM")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        datePicker.show(getFragmentManager(),"1");

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                chosenStartDate = datePicker.getSelection();
                pickEndDate();


            }
        });





    }

    public void commitDatabase(){
        listings = retrieveData();
        if(!listings.isEmpty()){
            addCustomersToArrayList(listings);
            // initializeAdapter(names,phoneNumbers, dates);

        }
    }


    public void pickEndDate(){
        datePicker =  MaterialDatePicker.Builder.datePicker()
                .setTitleText("TO")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        datePicker.show(getFragmentManager(),"1");

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                chosenEndDate = datePicker.getSelection();


            }
        });





    }




}