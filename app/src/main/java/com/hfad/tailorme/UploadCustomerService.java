package com.hfad.tailorme;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import Database.Customer;
import Database.CustomerDatabase;

public class UploadCustomerService extends Service {
    private static final int NOTIFICATION_ID = 1001;
    private LocalBinder mLocalBinder = new LocalBinder();
    private Callback mCallback;
    String uid;

    public IBinder onBind(Intent intent){
        return mLocalBinder;
    }

    public void uploadCustomersToCloud(String path){
        new MyAsyncTask().execute(path);
    }

    public void setCallBack(Callback callback){
        mCallback = callback;
    }

    public class LocalBinder extends Binder {
        public UploadCustomerService getService(){
            return UploadCustomerService.this;
        }
    }

    public interface Callback{
        void onOperationProgress(int progress);
        void onOperationCompleted();
        List<Customer> onOperationStart();


    }

    private final class MyAsyncTask
            extends AsyncTask<String,Integer,String > {
        List<Customer> customersList;


        @Override
        protected void onPreExecute(){
            super.onPreExecute();
           // customers =  mCallback.onOperationStart();
            CustomerDatabase appDb = CustomerDatabase.getInstance(getApplicationContext());
            try {
                customersList = appDb.customerDao().getCustomerList();
            }catch(Exception e){
                Toast.makeText(getApplicationContext(),"Empty Database",Toast.LENGTH_LONG).show();
            }


        }

        @Override
        protected void onProgressUpdate(Integer... values){

        }

        @Override
        protected String doInBackground(String... path){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            //String phoneNumber = customers.get(1);

            // get reference
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child(user.getUid()+"/Customer Info").setValue(customersList);
            Log.d("xxupload", "doInBackground: ");

            return null;
        }

        @Override
        protected void onPostExecute(String result){

        }

        @Override
        protected void onCancelled(){

        }

    }

   }