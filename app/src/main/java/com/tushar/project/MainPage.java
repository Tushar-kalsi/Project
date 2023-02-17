package com.tushar.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
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
import com.tushar.project.databinding.ActivityMainPageBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainPage extends AppCompatActivity {
    ActivityMainPageBinding binding;
    CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this , R.layout.activity_main_page);
        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();

        dialog=new CustomDialog(MainPage.this, "Signing Up ..... ");

        binding.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(binding.inputUsername.getText()==null || binding.inputUsername.getText().toString().length()<6){
                    binding.txtUsername.setError("Invalid Username ");

                }else{

                    if(binding.inputPassword.getText()==null || binding.inputPassword.getText().toString().trim().length()<6){
                        binding.txtPassword.setError("Invalid Password");

                    }else{

                        if(binding.inputName.getText()==null || binding.inputName.getText().toString().trim().length()<6){

                            binding.txtName.setError("Invalid Name");
                        }else{

                            if(binding.enrollmentInput.getText()==null || binding.enrollmentInput.getText().toString().trim().length()==0){
                                binding.enrollmentLayout.setError("Invalid Enrollment Number");
                            }else {
                                String enrollment_number=binding.enrollmentInput.getText().toString().trim();

                                String name = binding.inputName.getText().toString().trim();
                                String username = binding.inputUsername.getText().toString().trim();
                                String password = binding.inputPassword.getText().toString().trim();

                                registerUser(name, username, password, enrollment_number);
                            }
                        }
                    }

                }
            }
        });

    }
    void registerUser(String name , String username , String password , String enrollment_number){

        dialog.startDialog();
        String URL=getString(R.string.domain_url)+"createuser";
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", username);
            jsonBody.put("password", password);
            jsonBody.put("name", name);
            jsonBody.put("enrollment_number", enrollment_number);



            final String requestBody = jsonBody.toString();
            Log.d("request", requestBody+" form saving");


            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("errorVolley", response);

                    try {
                        JSONObject myJsonObject = new JSONObject(response);

                        boolean success = myJsonObject.optBoolean("success");

                        if(success){
                            Toast.makeText(MainPage.this, "Successfully registered , Please login" ,Toast.LENGTH_LONG).show();
                            Intent inten =new Intent(MainPage.this , MainActivity.class);
                            startActivity(inten);
                            finish();

                        }else{
                            Toast.makeText(MainPage.this, "Failed to register "+myJsonObject.optString("message") ,Toast.LENGTH_LONG).show();

                        }


                    }
                    catch (Exception e){


                    }

                    dialog.endDialog();



                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.endDialog();


                    Toast.makeText(MainPage.this , "Error:"+error.toString(), Toast.LENGTH_LONG).show();


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
}