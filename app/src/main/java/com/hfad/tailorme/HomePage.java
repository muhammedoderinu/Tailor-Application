package com.hfad.tailorme;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import Database.Customer;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class HomePage extends AppCompatActivity implements
          NavigationView.OnNavigationItemSelectedListener,
        CustomerFragment.Listener,GalleryFragment.Listener
        , UploadCustomerService.Callback, DownloadCustomerService.Callback {
    private ShareActionProvider shareActionProvider;
    ImageView imageView;
    View header;
    CustomerFragment callBack;
    Boolean appeared;
    Boolean galleryAppeared;
    UploadCustomerService mService;
    DownloadCustomerService dService;
    Listener listener;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        callBack = new CustomerFragment();
        callBack.setListener(this);






     Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        }catch(Exception e){

            }











        Fragment fragment = new MenuFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_frame,fragment);
        ft.commit();


        // connect the drawer layer to the navigation graph
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer, toolbar,
                R.string.nav_open_drawer,
                R.string.nav_close_drawer);

        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        header = navigationView.getHeaderView(0);

        ExtendedFloatingActionButton fab = findViewById(R.id.addCustomer);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(HomePage.this,CustomerForm.class);
               startActivity(intent);
            }
        });




    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();
        Fragment fragment = null;
        Intent intent = null;

        switch (id) {
            case R.id.nav_edit:
                fragment= new MessageFragment();
                break;
            case R.id.nav_account:
                fragment = new DraftFragment ();
                break;
            case R.id.nav_feedback:
                intent = new Intent(this, LogOutActivity.class);
                break;
            default:
                fragment= new AboutUsFragment();


        }
        if(fragment !=null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();

        }else {
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else{
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         //respond to menu item selection

        switch (item.getItemId()) {
            case R.id.customerBackUp:
                Toast.makeText(this,"CustomerBackup",Toast.LENGTH_LONG).show();
               // callBack.doBackUp();
                doBackUp();

                return true;
            case R.id.customerDownload:
                Toast.makeText(this,"CustomerDownload",Toast.LENGTH_LONG).show();
                downloadCustomerBackup();

                return true;

            case R.id.mediaDownload:
                Toast.makeText(this,"MediaDownload",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {


            if(appeared!=null) {

                if (!appeared) {
                    menu.getItem(0).setVisible(false);
                    menu.getItem(0).setEnabled(false);

                    Log.d("opom", "onPrepareOptionsMenu: ");
                } else if(appeared) {
                    menu.getItem(0).setVisible(true);
                    menu.getItem(0).setEnabled(true);

                }


            }



            // You can also use something like:
            // menu.findItem(R.id.example_foobar).setEnabled(false);

        return true;
    }


    @Override
    public void lonAppear(Boolean onAppear) {
        this.appeared = onAppear;
    }

    @Override
    public void backUp() {

    }

    @Override
    public void galleryAppearing(Boolean galleryAppeared) {
        this.galleryAppeared = galleryAppeared;
    }




    @Override
    public void onOperationProgress(int progress) {

    }

    @Override
    public void onOperationCompleted() {

    }

    @Override
    public List<Customer> onDownloadOperationStart() {


        return null;
    }

    @Override
    public List<Customer> onOperationStart() {


        return null;
    }

    @Override
    public void onPause(){
        super.onPause();
        if(mService!=null){
            Log.d("dnulll", "donServicenull: ");
            mService.setCallBack(null); // to avoid memory leaks
            unbindService(mConnection);
        }

        if(dService!=null){
            Log.d("dnulll", "donServicenull: ");
            dService.setCallBack(null); // to avoid memory leaks
            unbindService(dConnection);
        }


    }


    @Override
    public void onResume(){

        super.onResume();

        Intent bindIntent = new Intent(this,UploadCustomerService.class);
        bindService(bindIntent,mConnection, Context.BIND_AUTO_CREATE);

        Intent dBindIntent = new Intent(this,DownloadCustomerService.class);
        bindService(dBindIntent,dConnection, Context.BIND_AUTO_CREATE);


    }

    public void doBackUp(){
        mService.uploadCustomersToCloud("path");
        Log.d("uploadCustomer", "doBackUp: ");
    }
    public void downloadCustomerBackup(){
        dService.downloadCustomersToLocal("path");
    }


    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            mService = ((UploadCustomerService.LocalBinder)service).getService();
            mService.setCallBack(HomePage.this);
            Log.d("jogger", "onServiceConnected: ");



        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

    private ServiceConnection dConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            dService = ((DownloadCustomerService.LocalBinder)service).getService();
            dService.setCallBack(HomePage.this);
            Log.d("djogger", "donServiceConnected: ");



        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

    public interface Listener{
        void onClick();

    }

    public void setListener(Listener listener){
        this.listener = listener;
    }


}