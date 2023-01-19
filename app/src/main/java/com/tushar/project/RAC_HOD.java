package com.tushar.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.tushar.project.databinding.ActivityRacHodBinding;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.Map;

public class RAC_HOD extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    CustomDialog customDialog;
    String[] superVisorStringArray, coSuperVisorStringArray;
    private String selectedSuperVisior="", selectedCosuperVisor="";
    RequestQueue referenceQueue;
    ActivityRacHodBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= DataBindingUtil.setContentView(this , R.layout.activity_rac_hod);
        customDialog=new CustomDialog(this, "Fectching, Please Wait ......");
        referenceQueue= Volley.newRequestQueue(this);

        superVisorStringArray= getResources().getStringArray(R.array.array_supervisor);
        coSuperVisorStringArray=getResources().getStringArray(R.array.array_cosupervisor);
        Intent intent=getIntent();
        String en =intent.getStringExtra("en");
        makeRacCall(en);

        binding.coSuperVisorSpinnerTextview.setVisibility(View.GONE);
        binding.superVisorTextView.setVisibility(View.GONE);
        binding.coSuperVisorSpinner.setVisibility(View.VISIBLE);
        binding.superVisorSpinner.setVisibility(View.VISIBLE);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, superVisorStringArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.superVisorSpinner.setAdapter(arrayAdapter);


        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, coSuperVisorStringArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.coSuperVisorSpinner.setAdapter(arrayAdapter1);


        binding.coSuperVisorSpinner.setOnItemSelectedListener(this);
        binding.superVisorSpinner.setOnItemSelectedListener(this);


        binding.uploadButton.setText("View Document");

        binding.button4.setText("Modify");
        binding.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateSuperVisor(en);

            }
        });




    }

    private void updateSuperVisor(String enNumnber){

        String url =getString(R.string.domain_url)+"rac/modify?en="+enNumnber;
        Log.d("errorVolley", url);
        customDialog.startDialog();


        try {

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("supervisor", selectedSuperVisior);
            jsonBody.put("cosupervisor", selectedCosuperVisor);


            final String requestBody = jsonBody.toString();
            Log.d("request", requestBody+" form saving");


            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    customDialog.endDialog();

                    try {
                        JSONObject myJsonObject = new JSONObject(response);

                        boolean success= myJsonObject.optBoolean("success");
                        String message =myJsonObject.optString("message ");
                        if(success){
                            Toast.makeText(RAC_HOD.this , "Success: "+message, Toast.LENGTH_LONG).show();
                            finish();
                        }else{
                            Toast.makeText(RAC_HOD.this , "Error", Toast.LENGTH_LONG).show();

                        }

                    }
                    catch (Exception e){

                        Toast.makeText(RAC_HOD.this , "Someting Went wrong", Toast.LENGTH_LONG).show();


                    }





                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    customDialog.endDialog();


                    Toast.makeText(RAC_HOD.this , "Error:"+error.toString(), Toast.LENGTH_LONG).show();


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

            referenceQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }




    }




    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if(view.getId()==binding.superVisorSpinner.getId()){

            if(i==0){
                selectedSuperVisior="";

            }else {
                selectedSuperVisior=superVisorStringArray[i-1];

            }

        }else{

            if(i==0){
                selectedCosuperVisor="";

            }else {

                selectedCosuperVisor=coSuperVisorStringArray[i-1];

            }

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void makeRacCall(String enNumnber) {

        String url =getString(R.string.domain_url)+"rac?en="+enNumnber;
        Log.d("errorVolley", url);
        customDialog.startDialog();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        try {
                            JSONObject myJsonObject = new JSONObject(response);

                            boolean success= myJsonObject.optBoolean("success");
                            JSONArray data =myJsonObject.optJSONArray("results1");

                            if(success){


                                for(int i=0;i<data.length();i++){

                                    JSONObject jsonObject=data.getJSONObject(i);

                                    binding.nameInput.setText(jsonObject.optString("name"));
                                    binding.enrollmentNumberInput.setText(jsonObject.optString("EN"));
                                    binding.dorinput.setText(jsonObject.optString("DOR"));
                                    binding.batchInput.setText(jsonObject.optString("Batch"));
                                    String supervisor=jsonObject.optString("SuperVisor");
                                    String coSuperVisor=jsonObject.optString("CoSupervisor");
                                    String documentLink=jsonObject.optString("Document");



                                    binding.dorinput.setText(jsonObject.optString("DOR"));


                                    binding.uploadButton.setText("View Document");

                                    binding.uploadButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(documentLink));
                                            startActivity(browserIntent);
                                        }
                                    });


                                }


                            }

                        }
                        catch (Exception e){

                            Log.d("errorVolley", "Inside exeption "+e.toString());
                        }

                        customDialog.endDialog();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RAC_HOD.this , "There is some error ", Toast.LENGTH_LONG).show();

                customDialog.endDialog();


            }
        });

// Add the request to the RequestQueue.
        referenceQueue.add(stringRequest);
    }

}