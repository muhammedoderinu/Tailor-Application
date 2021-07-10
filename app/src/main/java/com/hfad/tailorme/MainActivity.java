package com.hfad.tailorme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText usernameText, passwordText, confirm_passwordText;
    TextView username_error,password_error,confirm_password_error;
    Button submitBtn;
    String username, password, confirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameText = findViewById(R.id.username);
        passwordText = findViewById(R.id.password);
        confirm_passwordText = findViewById(R.id.confirmPassword);

        username_error = findViewById(R.id.username_error);
        password_error = findViewById(R.id.password_error);
        confirm_password_error = findViewById(R.id.confirm_password_error);

        submitBtn = findViewById(R.id.submit);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameText.getText().toString();
                password = passwordText.getText().toString();
                confirm_password = confirm_passwordText.getText().toString();
                registerUsers(username,password,confirm_password);


            }



        });


    }

     private void registerUsers(String username, String password, String confirm_password) {
         // Instantiate the RequestQueue.
         RequestQueue queue = Volley.newRequestQueue(this);
         String url = "http://192.168.0.100/tregister.php";
          StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                 Log.e("TAG","RESPONSE IS"+ response);
                 try{
                     JSONObject jsonObject = new JSONObject(response);
                     //Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                     Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                     

                 }  catch(JSONException e) {
                     e.printStackTrace();
                 }

                
              }
          }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 Toast.makeText(MainActivity.this,"Connection Failed", Toast.LENGTH_LONG) .show();
                 String str_error = error.toString();
                 Log.d("volley_err",  str_error);
             }

         }) {
              @Override
              public String getBodyContentType(){
                  return "application/x-www-form-urlencoded; charset=UTF-8";
              }

              @Override
              protected Map<String, String> getParams (){
                  Map<String, String> params = new HashMap<String, String>();

                  params.put("username", username) ;
                  params.put("password",password) ;
                  params.put("confirm_password",confirm_password);

                  return params;
              }
          };
          queue.add(request);

     }
}