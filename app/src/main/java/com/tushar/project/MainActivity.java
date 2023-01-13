package com.tushar.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tushar.project.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_main);
        dialog = new CustomDialog(MainActivity.this, "Authenticating, Please Wait .....");
        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean token = preferences.getBoolean("logged", false);

        if(token){
            Log.d("errorVolley", preferences.getString("username"," ")+" from shared pref login in ");
            startActivity(new Intent(MainActivity.this , home.class));
            finish();

        }
        binding.textView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this , MainPage.class);
                startActivity(intent);

            }
        });
        binding.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if( binding.inputUsername.getText()==null || binding.inputPassword.getText().toString().trim().length()<6 ){
                   binding.txtUsername.setError("Username Invalid!");

               }else{


                   if(binding.inputPassword.getText()==null || binding.inputPassword.getText().toString().trim().length()<6){

                       binding.txtPassword.setError("Password invalid ");
                   }else{

                       tryLoggingIn(binding.inputUsername.getText().toString().trim(), binding.inputPassword.getText().toString().trim());


                   }


               }

            }
        });


    }
    public void tryLoggingIn(String username , String password ){
        dialog.startDialog();

        String URL=getString(R.string.domain_url)+"login";
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", username);
            jsonBody.put("password", password);


            final String requestBody = jsonBody.toString().trim();
            Log.d("request", requestBody+" form saving");


            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject myJsonObject = new JSONObject(response);

                        boolean success = myJsonObject.optBoolean("success");
                        dialog.endDialog();

                        if(success){
                            JSONObject userData =myJsonObject.optJSONObject("data");
                            Toast.makeText(MainActivity.this  ,"Login Successfull", Toast.LENGTH_LONG).show();

                            String userName= userData.optString("username");
                            String name =userData.optString("name");
                            int id= userData.optInt("id");
                            int type =userData.optInt("type");

                            String enrollmentNumber=userData.optString("enrollment_number");


                            storeLocally(name , id , userName, enrollmentNumber, type);


                        }


                    }
                    catch (Exception e){


                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.endDialog();


                    Toast.makeText(MainActivity.this , "Error:"+error.toString().trim(), Toast.LENGTH_LONG).show();


                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> params = new HashMap<>();

                    return params;
                }
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);

                        Log.d("response code",responseString);
                        // can get more details such as response.headers
                    }
                    return super.parseNetworkResponse(response);
                }

            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                    2,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            requestQueue.add(stringRequest);
        } catch (JSONException e) {

            e.printStackTrace();

        }


    }

    public void storeLocally(String name , int id , String username, String enrollmentNumber, int type){

        Log.d("errorVolley", "username "+username +" id "+id+" name "+name );
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("logged",true);
        editor.putString("name",name);
        editor.putInt("id",id);
        editor.putString("username",username);
        editor.putString("enrollment_number", enrollmentNumber);
        editor.putInt("type", type);
        editor.apply();

        Log.d("errorVolley", preferences.getString("username", "")+" from shred pref");



        Intent inten =new Intent(MainActivity.this , home.class);
        startActivity(inten);
        finish();

    }

}