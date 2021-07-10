package com.hfad.tailorme;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DownloadGalleryService extends Service {
    private static final int NOTIFICATION_ID = 1001;
    private LocalBinder mLocalBinder = new LocalBinder();
    private Callback mCallback;
    String uid;

    public IBinder onBind(Intent intent){
        return mLocalBinder;
    }

    public void downloadImagesFromCloud(String path){
        new MyAsyncTask().execute(path);
    }

    public void setCallBack(Callback callback){
        mCallback = callback;
    }

    public class LocalBinder extends Binder {
        public DownloadGalleryService getService(){
            return DownloadGalleryService.this;
        }
    }

    public interface Callback{
        void onOperationProgress(int progress);
        void onOperationCompleted();
        File onOperationStart();


    }

    private final class MyAsyncTask
            extends AsyncTask<String,Integer,String > {
        File filePath;


        @Override
        protected void onPreExecute(){
            super.onPreExecute();
           filePath =  mCallback.onOperationStart();


        }

        @Override
        protected void onProgressUpdate(Integer... values){

        }

        @Override
        protected String doInBackground(String... path){
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference usersRef = rootRef.child(path[0]);

            ValueEventListener valueEventListener = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<String> list = new ArrayList<>();
                    Map<String, Object> map = new HashMap<>();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                         map = (Map) ds.getValue();
                         Object value = map.get(user.getUid());
                         String uid = value.toString();

                         list.add(uid);
                         Log.d("doInbackground",uid);
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference httpsReference = storage.getReferenceFromUrl(uid);
                        httpsReference.getFile(filePath).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Log.d("onSuccess", "cloud file created ");

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });



                    }

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

        }

        @Override
        protected void onCancelled(){

        }

    }

    public void customConnection(){
        int count;
        try{
            URL url = new URL("path[0]");
            URLConnection connection = url.openConnection();
            connection.connect();
            // show progress bar
            int lengthOfFile = connection.getContentLength();

            //download file
            InputStream input = new BufferedInputStream(url.openStream(),8192);

            // output stream
            //File file = createImageFile(getContext());
            File file = new File("str");
            String filePath = file.getAbsolutePath();
            OutputStream output = new FileOutputStream(filePath);

            byte data[] = new byte[1024];
            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                //publishProgress( (int) ((total * 100) / lengthOfFile));

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();





                // writing data to file
                output.write(data, 0, count);
            }

        }catch(Exception e){
            Log.e("Error: ", e.getMessage());

        }
    }
}