package com.hfad.tailorme;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Database.Customer;
import Database.CustomerDatabase;

public class DownloadCustomerService extends Service {
    private static final int NOTIFICATION_ID = 1001;
    private LocalBinder mLocalBinder = new LocalBinder();
    private Callback mCallback;
    public IBinder onBind(Intent intent){
        return mLocalBinder;
    }

    public void downloadCustomersToLocal(String path){
        new MyAsyncTask().execute(path);
    }

    public void setCallBack(Callback callback){
        mCallback = callback;
    }

    public class LocalBinder extends Binder {
        public DownloadCustomerService getService(){
            return DownloadCustomerService.this;
        }
    }
    public interface Callback{
        void onOperationProgress(int progress);
        void onOperationCompleted();
        List<Customer> onDownloadOperationStart();

    }
   int k=3;

    private final class MyAsyncTask
            extends AsyncTask<String,Integer,String > {
        List<Customer> customers;
        CustomerDatabase appDb;


        @Override
        protected void onPreExecute(){
            super.onPreExecute();
           // customers =  mCallback.onDownloadOperationStart();
           appDb = CustomerDatabase.getInstance(getApplicationContext());


        }

        @Override
        protected void onProgressUpdate(Integer... values){

        }

        @Override
        protected String doInBackground(String... path){

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference usersRef = rootRef.child(user.getUid()+"/Customer Info");
            ValueEventListener valueEventListener = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Customer> cloudLists = new ArrayList<>();
                    List<Customer> lists = new ArrayList<>();
                    for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                        Customer customer = userSnapshot.getValue(Customer.class);
                        Log.d("xxxxCustomer",customer.toString());
                        appDb.customerDao().insertCustomer(customer);
                    }

                   //List<Customer> customers = (List<Customer>) dataSnapshot.child("Customer Info").getValue();



                    //Do what you need to do with your list
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("", databaseError.getMessage()); //Don't ignore errors!
                }
            };
            usersRef.addListenerForSingleValueEvent(valueEventListener);

            return null;
        }

        @Override
        protected void onPostExecute(String result){
            mCallback.onOperationCompleted();

        }

        @Override
        protected void onCancelled(){

        }

    }


}