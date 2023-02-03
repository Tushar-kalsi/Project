package com.tushar.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.SharedPreferences;
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
import com.tushar.project.databinding.ActivityTitleSubmissionBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class TitleSubmission extends AppCompatActivity implements View.OnClickListener {
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    ActivityTitleSubmissionBinding binding;
    String enrollment_number , name ;
    CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_title_submission);

        requestQueue = Volley.newRequestQueue(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        customDialog=new CustomDialog(this, "Uplaoding , Please Wait ..... ");
        enrollment_number = sharedPreferences.getString("enrollment_number", " ");
        binding.enrollmentNumberInput.setText(enrollment_number);
        name = sharedPreferences.getString("name", " ");
        binding.nameInput.setText(name);

        binding.button4.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(binding.titleSubmissionInput.getText()!=null && binding.titleSubmissionInput.getText().toString().trim().length()>0){
            makeApiCall(binding.titleSubmissionInput.getText().toString());

        }else{
            binding.titleSubmissionInput.setError("Invalid Title");

        }


    }

    public void makeApiCall(String title){

        String url=getString(R.string.domain_url)+"title";


        customDialog.startDialog();


        try {

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("EN", enrollment_number);
            jsonBody.put("title", title);

            final String requestBody = jsonBody.toString();
            Log.d("request", requestBody+" form saving");


            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject myJsonObject = new JSONObject(response);

                        boolean success= myJsonObject.optBoolean("success");
                        String message =myJsonObject.optString("message ");
                        if(success){
                            Toast.makeText(TitleSubmission.this , "Success: "+message, Toast.LENGTH_SHORT).show();

                            finish();
                        }else{
                            Toast.makeText(TitleSubmission.this , "Error", Toast.LENGTH_LONG).show();

                        }

                    }
                    catch (Exception e){


                    }

                    customDialog.endDialog();



                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    customDialog.endDialog();


                    Toast.makeText(TitleSubmission.this , "Error:"+error.toString().trim(), Toast.LENGTH_LONG).show();


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