package com.tushar.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
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
import com.tushar.project.databinding.ActivityQipRegistrationBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;

public class QIP_registration extends AppCompatActivity {

    ActivityQipRegistrationBinding binding;
    SharedPreferences preferences ;
    CustomDialog dialog;
    RequestQueue requestQueue ;
    String username ;
    int id;
    String enrollment_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= DataBindingUtil.setContentView(this , R.layout.activity_qip_registration);

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        dialog = new CustomDialog(this, "Saving data .....");
        requestQueue = Volley.newRequestQueue(this);

        preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        int type =preferences.getInt("type", 1);
        if(type==2 || type ==3){

            Intent intent=getIntent();

            username = intent.getStringExtra("username");

            getQIP();

            binding.button4.setVisibility(View.GONE);
            binding.firstNameInput.setEnabled(false);
            binding.deptRegInput.setEnabled(false);
            binding.enrollmentInout.setEnabled(false);
            binding.lastNameInpout.setEnabled(false);
            binding.fatherNameInput.setEnabled(false);

            binding.titletext.setText("Student Information");
            binding.button4.setText("Back");
            binding.button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    finish();
                }
            });
        }else {

            username = preferences.getString("username", " ");
            id = preferences.getInt("id ", 1);
            enrollment_number = preferences.getString("enrollment_number", " ");


            Log.d("errorVolley", username);


            binding.enrollmentInout.setText(enrollment_number);

            getQIP();


            binding.button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (binding.deptRegInput.getText() == null || binding.deptRegInput.getText().toString().trim().length() == 0) {

                        binding.deptRegLayout.setError("Invalid Dept. Number");
                    } else {
                        if (binding.enrollmentInout.getText() == null || binding.enrollmentInout.getText().toString().trim().length() == 0) {

                            binding.enrollmentLayout.setError("Invalid Enroll. Number");
                        } else {


                            if (binding.firstNameInput.getText() == null || binding.firstNameInput.getText().toString().trim().length() == 0) {
                                binding.firstNamelayout.setError("Invalid First Name");
                            } else {


                                if (binding.lastNameInpout.getText() == null || binding.lastNameInpout.getText().toString().trim().length() == 0) {
                                    binding.lastNameInpout.setError("Invalid last Name");
                                } else {

                                    if (binding.fatherNameInput.getText() == null || binding.fatherNameInput.getText().toString().trim().length() == 0) {

                                        binding.fatherNameLayout.setError("Invalid Father Name ");

                                    } else {


                                        String fatherName = binding.fatherNameInput.getText().toString().trim();
                                        String firstName = binding.firstNameInput.getText().toString().trim();
                                        String lastName = binding.lastNameInpout.getText().toString().trim();
                                        String deptName = binding.deptRegInput.getText().toString().trim();
                                        String enrollName = binding.enrollmentInout.getText().toString().trim().trim();
                                        saveQIPSetting(fatherName, firstName, lastName, deptName, enrollName);


                                    }
                                }

                            }
                        }
                    }
                }
            });
        }
    }

    public void getQIP(){


        String url =getString(R.string.domain_url)+"qip?username="+username;
        Log.d("errorVolley", url +"  url " );

        dialog.startDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        try {
                            JSONObject myJsonObject = new JSONObject(response);

                            boolean success= myJsonObject.optBoolean("success");
                            JSONObject data =myJsonObject.optJSONObject("message");

                            if(success){
                                data.optInt("id");
                                binding.enrollmentInout.setText(data.optString("enrollment_number"));
                                data.optString("username");
                                binding.lastNameInpout.setText( data.optString("lastname"));
                                binding.firstNameInput.setText(data.optString("firstname"));
                                binding.deptRegInput.setText(data.optString("dept_number"));
                                binding.fatherNameInput.setText(data.optString("fathername"));



                            }

                        }
                        catch (Exception e){


                        }

                        dialog.endDialog();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorVolley", error.toString().trim());
                Toast.makeText(QIP_registration.this , "There is some error ", Toast.LENGTH_LONG).show();

                dialog.endDialog();


            }
        });

// Add the request to the RequestQueue.
        requestQueue.add(stringRequest);

    }

    public void saveQIPSetting(String fatherName , String firstName , String lastName , String deptNumber , String enrollNumber ){




        String URL=getString(R.string.domain_url)+"qip";
        try {

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("dept_registration", deptNumber);
            jsonBody.put("enrollement_number", enrollNumber);
            jsonBody.put("first_name", firstName);
            jsonBody.put("last_name", lastName);
            jsonBody.put("father_name", fatherName);
            jsonBody.put("id", id);
            jsonBody.put("username", username);



            final String requestBody = jsonBody.toString();
            Log.d("request", requestBody+" form saving");


            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject myJsonObject = new JSONObject(response);

                        boolean success= myJsonObject.optBoolean("success");
                        String message =myJsonObject.optString("message ");
                        if(success){
                            Toast.makeText(QIP_registration.this , "Success: "+message, Toast.LENGTH_LONG).show();

                            finish();
                        }else{
                            Toast.makeText(QIP_registration.this , "Error", Toast.LENGTH_LONG).show();

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


                    Toast.makeText(QIP_registration.this , "Error:"+error.toString().trim(), Toast.LENGTH_LONG).show();


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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }



}