package com.tushar.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

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
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tushar.project.databinding.ActivityNonqipRegistrationBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class NONQIP_registration extends AppCompatActivity {

    String enrollment_number;

    SharedPreferences preferences ;
    private ActivityNonqipRegistrationBinding binding;
    CustomDialog dialog;
    RequestQueue requestQueue ;
    int id ;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this , R.layout.activity_nonqip_registration);

        dialog = new CustomDialog(this, "Saving Data .....");
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        requestQueue= Volley.newRequestQueue(this);
        preferences= PreferenceManager.getDefaultSharedPreferences(getApplication());
        int type=preferences.getInt("type", 1);

        if(type==2 || type ==3){


            Intent intent=getIntent();

            username = intent.getStringExtra("username");
            getQIP();


            binding.firstNameInput.setEnabled(false);
            binding.deptRegInput.setEnabled(false);
            binding.enrollmentInout.setEnabled(false);
            binding.lastNameInpout.setEnabled(false);
            binding.fatherNameInput.setEnabled(false);
            binding.addressInput.setEnabled(false);
            binding.DOBinput.setEnabled(false);

            binding.titletext.setText("Student Information");
            binding.button4.setVisibility(View.GONE);



        }else {
            username = preferences.getString("username", " ");
            id = preferences.getInt("id", 1);
            enrollment_number=preferences.getString("enrollment_number", " ");
            binding.enrollmentInout.setText(enrollment_number);


            getQIP();
            SetDate setDate = new SetDate(binding.DOBinput, this);


            binding.button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (binding.deptRegInput.getText() == null || binding.deptRegInput.getText().toString().length() == 0) {

                        binding.deptRegLayout.setError("Invalid Dept. Number");
                    } else {
                        if (binding.enrollmentInout.getText() == null || binding.enrollmentInout.getText().toString().length() == 0) {

                            binding.enrollmentLayout.setError("Invalid Enroll. Number");
                        } else {


                            if (binding.firstNameInput.getText() == null || binding.firstNameInput.getText().toString().length() == 0) {
                                binding.firstNamelayout.setError("Invalid First Name");
                            } else {


                                if (binding.lastNameInpout.getText() == null || binding.lastNameInpout.getText().toString().length() == 0) {
                                    binding.lastNameInpout.setError("Invalid last Name");
                                } else {

                                    if (binding.fatherNameInput.getText() == null || binding.fatherNameInput.getText().toString().length() == 0) {

                                        binding.fatherNameLayout.setError("Invalid Father Name ");

                                    } else {

                                        if (binding.addressInput.getText() == null || binding.addressInput.getText().toString().length() == 0) {

                                            binding.adressLayout.setError("Invalid address");
                                        } else {

                                            if (binding.DOBinput.getText() == null || binding.DOBinput.getText().toString().length() == 0) {

                                                binding.doblayout.setError("Invalid DOB");

                                            } else {
                                                String address = binding.addressInput.getText().toString();
                                                String dob = binding.DOBinput.getText().toString();
                                                String fatherName = binding.fatherNameInput.getText().toString();
                                                String firstName = binding.firstNameInput.getText().toString();
                                                String lastName = binding.lastNameInpout.getText().toString();
                                                String deptName = binding.deptRegInput.getText().toString();
                                                String enrollName = binding.enrollmentInout.getText().toString();

                                                saveNonQIPSetting(fatherName, firstName, lastName, deptName, enrollName, dob, address);


                                            }

                                        }


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

        String url =getString(R.string.domain_url)+"nonqip?username="+username;
        Log.d("errorVolley", url);
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
                                binding.DOBinput.setText(data.optString("dob"));
                                binding.addressInput.setText(data.optString("address"));

                                binding.button4.setText("Modify");


                            }

                        }
                        catch (Exception e){


                        }

                        dialog.endDialog();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NONQIP_registration.this , "There is some error ", Toast.LENGTH_LONG).show();

                dialog.endDialog();


            }
        });

// Add the request to the RequestQueue.
        requestQueue.add(stringRequest);

    }

    public void saveNonQIPSetting(String fatherName , String firstName , String lastName , String deptNumber , String enrollNumber , String dob , String address){




        String URL=getString(R.string.domain_url)+"nonqip";
        try {

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("dob", dob);
            jsonBody.put("address", address);
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
                            Toast.makeText(NONQIP_registration.this , "Success: "+message, Toast.LENGTH_LONG).show();

                            finish();

                        }else{
                            Log.d("errorVolley", response);

                            Toast.makeText(NONQIP_registration.this , "Error", Toast.LENGTH_LONG).show();

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


                    Toast.makeText(NONQIP_registration.this , "Error:"+error.toString(), Toast.LENGTH_LONG).show();


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