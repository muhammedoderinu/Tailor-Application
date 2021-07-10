package com.hfad.tailorme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

public class DetailedGallery
        extends AppCompatActivity {
    public static final String EXTRA_ID = "extra_id";
    ImageView largePicture;
    ArrayList<String> images = new ArrayList<String>();
    Button galleryToCloud;
    File imgFile;
    String downloadPath;
    String position;
    String timeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_gallery);
        largePicture = findViewById(R.id.largePicture);
        Bundle b = this.getIntent().getExtras();
        images = b.getStringArrayList("EXTRA_IMAGE_TITLE");
        galleryToCloud = findViewById(R.id.galleryToCloud);



        int imageId = (Integer) getIntent().getExtras().get(EXTRA_ID);
         position = String.valueOf(imageId);
          timeStamp = images.get(imageId);

        imgFile = new File(timeStamp);

        if(imgFile.exists()) {
            Log.d("onBindViewHolder", "I exist: ");
            Picasso.get()
                    .load(imgFile)
                    .resize(750,1000)
                    .centerCrop()
                    .into(largePicture);

            //Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            //largePicture.setImageBitmap(myBitmap);

        }

        galleryToCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imgFile!=null) {
                    Uri file = Uri.fromFile(imgFile);
                    uploadGalleryToCloud(file);
                }

            }
        });


    }

    public void uploadGalleryToCloud(Uri file){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference ref = storageRef.child(user.getUid()+"/Gallery/"+position);
       UploadTask uploadTask = ref.putFile(file);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                   Uri downloadUri = task.getResult();
                    DetailedGallery.this.downloadPath =  downloadUri.toString();
                    saveDownloadPathToDatabase(downloadPath);

                    Log.d("the download",downloadPath);
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    public void saveDownloadPathToDatabase(String downloadPath){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // get reference
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User/Gallery/"+timeStamp);
        ref.child(user.getUid()).setValue(downloadPath);
    }



}