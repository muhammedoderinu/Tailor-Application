package com.hfad.tailorme;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Adapter.GalleryAdapter;
import Database.Gallery;
import Database.GalleryDatabase;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.BIND_AUTO_CREATE;


public class GalleryFragment extends Fragment
    implements ServiceConnection, DownloadGalleryService.Callback {
  private static final int PICKFILE_REQUEST_CODE = 1;
  ExtendedFloatingActionButton uploadButton;

  ArrayList<Bitmap> bitmaps;
  String pictureFile;
  RecyclerView galleryRecycler;
  GalleryAdapter adapter;
  List<Gallery> galleryLists;
  Gallery galleryList;
  Listener listener;
  ArrayList<String> imagesUrl = new ArrayList<String>();
  private DownloadGalleryService mService;
  Boolean onAppear;

  private static final String PATH = "User/Gallery/";

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

        View view =  inflater.inflate(R.layout.fragment_gallery, container, false);
        galleryRecycler = view.findViewById(R.id.galleryRecycler);
        uploadButton = view.findViewById(R.id.uploadFile);

         try {
             createImageFile(getContext());
         }catch(IOException e){

         }

                 if(!imagesUrl.isEmpty()){
                     imagesUrl.clear();
                 }
                 galleryLists = retrieveData();

                 if (galleryLists!=null) {
                     loadAdapter();
                     viewLargePicture();
             }




        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               selectImage(getContext());

            }
        });


        onAppear = false;
        callOnAppear(false);


        return view;
    }

    public void viewLargePicture(){
        adapter.setListener(new GalleryAdapter.Listener() {
            @Override
            public void onclick(int position) {
                Intent intent  = new Intent(getContext(),DetailedGallery.class);
                intent.putExtra(DetailedGallery.EXTRA_ID, position);
                Bundle b = new Bundle();
                b.putStringArrayList("EXTRA_IMAGE_TITLE", imagesUrl);
                intent.putExtras(b);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);

            }

            @Override
            public void onLongClick(int position) {


            }
        });

    }

    public void loadAdapter(){


            for (int i = 0; i < galleryLists.size(); i++) {
                galleryList = galleryLists.get(i);
                String imageUrl = galleryList.getFilepath();

                imagesUrl.add(imageUrl);
                Log.d("oooon", imageUrl);

            }
            initializeAdapter();
        }

        @Override
        public void onResume(){
           super.onResume();
           callOnAppear(true);
            if(!imagesUrl.isEmpty()){
                imagesUrl.clear();
            }
            galleryLists = retrieveData();

            if (galleryLists != null) {
                loadAdapter();
                viewLargePicture();
            }
            Intent bindIntent = new Intent(getContext(), DownloadGalleryService.class);
            getContext().bindService(bindIntent,this,BIND_AUTO_CREATE);

        }



    @Override
    public void onPause(){
        super.onPause();
        callOnAppear(false);
        if(mService!=null){
            mService.setCallBack(null); // to avoid memory leaks
            getContext().unbindService(this);
        }
    }
    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        // get gallery database
                         Gallery gallery = new Gallery();

                        // get the last gallery object from the database
                        // get the index of the the last gallery object
                        // po = index+1

                        Log.d("onActivityp", pictureFile);
                        if(pictureFile!=null) {
                            gallery.setFilePath(pictureFile);
                            GalleryDatabase galleryDatabase = GalleryDatabase.getInstance(getContext());
                            galleryDatabase.galleryDao().insertGallery(gallery);
                            storeImage(selectedImage, pictureFile);

                        }
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage =  data.getData();
                        String[] filePathColumns = {MediaStore.Images.ImageColumns.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getActivity().getContentResolver().query(
                                    selectedImage,filePathColumns,
                                    null,null,null);
                            if(cursor!=null){
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
                                String picturePath = cursor.getString(columnIndex);
                                Gallery gallery =new Gallery();
                                gallery.setFilePath(picturePath);
                                GalleryDatabase galleryDatabase = GalleryDatabase.getInstance(getContext());
                                galleryDatabase.galleryDao().insertGallery(gallery);



                            }




                            // call the gallery database
                            // insert file filepath to gallery object

                        }

                    }
                    break;
            }
        }
    }

    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }



    private File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        pictureFile = image.getAbsolutePath();
        return image;
    }

    private File createImageCloudFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "cloud_JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        pictureFile = image.getAbsolutePath();
        return image;
    }


    private void storeImage(Bitmap image, String pictureFile) {

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("fileNotFound", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("I0", "Error accessing file: " + e.getMessage());
        }
    }

    public void initializeAdapter(){
        adapter  = new GalleryAdapter(imagesUrl);
        galleryRecycler.setAdapter(adapter);
       RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
       galleryRecycler.setLayoutManager(layoutManager);

    }

    public List<Gallery> retrieveData(){
        GalleryDatabase appDb = GalleryDatabase.getInstance(getContext());
        try {
            return appDb.galleryDao().getGalleryList();
        }catch(Exception e){
            Toast.makeText(getContext(),"Empty Database",Toast.LENGTH_LONG).show();
        }


        return null;
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder iBinder) {
        mService = ((DownloadGalleryService.LocalBinder) iBinder).getService();


        // Once we have a reference to the service, we can update the UI
        // and enabled the button that should be somewhat disabled



        mService.setCallBack(this);
        Log.d("onServiceConnected: ","I am connected");

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        // Disable the button as we are losing the reference to the service

        mService = null;

    }

    @Override
    public void onOperationProgress(int progress) {
        // set progress to user interface

    }

    @Override
    public void onOperationCompleted() {


    }

    @Override
    public File onOperationStart() {
        try {
            File file = createImageCloudFile(getContext());
            return file;
        }catch(Exception e){

        }
        return null;
    }

    interface Listener{
        public void galleryAppearing(Boolean galleryAppeared );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (Listener) context;
    }

    public void callOnAppear(Boolean onAppear){
        listener.galleryAppearing(onAppear);
    }




}