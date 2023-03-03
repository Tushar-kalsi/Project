package com.tushar.project;

import static com.tushar.project.ThesisSubmission.PICK_FILE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
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
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.tushar.project.databinding.ActivityRacHodBinding;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.Map;

public class RAC_HOD extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    CustomDialog customDialog;
    String[] superVisorStringArray, coSuperVisorStringArray, departmentStringArray;
    private String selectedSuperVisior="", selectedCosuperVisor="", slectedDepartment="";
    RequestQueue referenceQueue;
    ActivityRacHodBinding binding;
    Uri hodUploadingFile;
    String ty;
    StorageReference storageRef;
    public static final String KEY_DATA="data_key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= DataBindingUtil.setContentView(this , R.layout.activity_rac_hod);
        customDialog=new CustomDialog(this, "Fectching, Please Wait ......");
        referenceQueue= Volley.newRequestQueue(this);

        superVisorStringArray= getResources().getStringArray(R.array.array_supervisor);
        coSuperVisorStringArray=getResources().getStringArray(R.array.array_cosupervisor);
        departmentStringArray=getResources().getStringArray(R.array.department_array);

        Intent intent=getIntent();

        String en =intent.getStringExtra("en");
        ty=intent.getStringExtra("ty");

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://project-44332.appspot.com");
        storageRef = storage.getReference();



        String json=intent.getStringExtra(KEY_DATA);
        RacDataModel racDataModel=new Gson().fromJson(json , RacDataModel.class);

        makeRacCall(racDataModel);

        if(ty.equals("2")){

            binding.coSuperVisorSpinnerTextview.setVisibility(View.GONE);
            binding.superVisorTextView.setVisibility(View.GONE);
            binding.coSuperVisorSpinner.setVisibility(View.VISIBLE);
            binding.superVisorSpinner.setVisibility(View.VISIBLE);
            binding.departmentSpinnerTextView.setVisibility(View.GONE);

            binding.button4.setText("Modify");
            binding.button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Log.d("selectedURI", "Uri slected "+(hodUploadingFile==null));

                    if(hodUploadingFile!=null){


                        uploadHodFileToFireStore(en);

                    }else{
                        updateSuperVisor(en , " ",0);

                    }


                }
            });

            binding.uploadDocumentHOD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    accessPdf();
                }
            });

        }else{
            binding.coSuperVisorSpinnerTextview.setVisibility(View.VISIBLE);
            binding.superVisorTextView.setVisibility(View.VISIBLE);
            binding.coSuperVisorSpinner.setVisibility(View.GONE);
            binding.superVisorSpinner.setVisibility(View.GONE);


            binding.button4.setText("Back");
            binding.button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    finish();


                }
            });


        }


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, superVisorStringArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.superVisorSpinner.setAdapter(arrayAdapter);

        binding.superVisorSpinner.setOnItemSelectedListener(this);


        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, coSuperVisorStringArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.coSuperVisorSpinner.setAdapter(arrayAdapter1);


        binding.coSuperVisorSpinner.setOnItemSelectedListener(this);


        arrayAdapter1 = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, departmentStringArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.departmentSpinner.setAdapter(arrayAdapter1);


        binding.departmentSpinner.setOnItemSelectedListener(this);



        binding.uploadButton.setText("View Document");


    }
    private void uploadHodFileToFireStore(String enrollmentNumber){


        customDialog.startDialog();

//        Uri file = Uri.fromFile(new File(bitmap.toString()));

        StorageReference riversRef = storageRef.child("racUpload/"+enrollmentNumber+"_hod"+".pdf");

        riversRef.putFile(hodUploadingFile).continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // After uploading is done it progress
                    // dialog box will be dismissed

                    Uri uri = task.getResult();
                    String myurl;
                    myurl = uri.toString();

                    updateSuperVisor(enrollmentNumber, myurl, 1);


                } else {
                    customDialog.endDialog();

                    Toast.makeText(RAC_HOD.this, "UploadedFailed ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void updateSuperVisor(String enNumnber, String myUrl, int uploaded ){



        String url =getString(R.string.domain_url)+"rac/modify";
        Log.d("errorVolley", url);
        customDialog.startDialog();


        try {

            Log.d("errorVolley",selectedSuperVisior+"  "+selectedCosuperVisor+" are selected data ");
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("enrollment_number", enNumnber);
            jsonBody.put("supervisor", selectedSuperVisior);
            jsonBody.put("cosupervisor", selectedCosuperVisor);
            jsonBody.put("DepartmentName",slectedDepartment );


            if(uploaded==1){

                jsonBody.put("HODDocument", myUrl);
                jsonBody.put("HodDocumentInserted",1);


            }else{

                jsonBody.put("HODDocument", " ");
                jsonBody.put("HodDocumentInserted",0);


            }


            final String requestBody = jsonBody.toString();
            Log.d("request", requestBody+" form saving");


            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    customDialog.endDialog();

                    try {
                        JSONObject myJsonObject = new JSONObject(response);

                        boolean success= myJsonObject.optBoolean("success");
                        String message =myJsonObject.optString("message");
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

        if(adapterView.getId()==binding.superVisorSpinner.getId()){

            if(i==0){

                selectedSuperVisior="";
                Log.d("errorVolley", "supervisor none selected ");

            }else {
                Log.d("errorVolley", "supervisor selected "+superVisorStringArray[i]);
                selectedSuperVisior=superVisorStringArray[i];

            }

        }else if(adapterView.getId()==binding.coSuperVisorSpinner.getId()){


            if(i==0){
                selectedCosuperVisor="";
                Log.d("errorVolley", "co-supervisor none selected ");

            }else {

                Log.d("errorVolley", "co-supervisor selected "+superVisorStringArray[i]);
                selectedCosuperVisor=coSuperVisorStringArray[i];

            }

        }else if (adapterView.getId()==binding.departmentSpinner.getId()){


            if(i==0){
                slectedDepartment="";
                Log.d("errorVolley", "co-supervisor none selected ");

            }else {

                Log.d("errorVolley", "co-supervisor selected "+departmentStringArray[i]);
                slectedDepartment=departmentStringArray[i];

            }


        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void makeRacCall(RacDataModel racDataModel) {


        if(racDataModel==null)
            return;

        binding.nameInput.setText(racDataModel.getName());
        binding.enrollmentNumberInput.setText(racDataModel.getEnrollment_number());
        binding.dorinput.setText(racDataModel.dorDate);
        binding.batchInput.setText(racDataModel.batch);
        String supervisor= racDataModel.supervisor;
        String coSuperVisor= racDataModel.coSuperVisor;
        String documentLink= racDataModel.getDocumentLink();


        binding.coSuperVisorSpinnerTextview.setText(coSuperVisor);
        binding.superVisorTextView.setText(supervisor);

        binding.dorinput.setText(racDataModel.dorDate);


        binding.uploadButton.setText("View Document");

        binding.uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(documentLink));
                startActivity(browserIntent);
            }
        });







// Add the request to the RequestQueue.

    }


    public void accessPdf(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        startActivityForResult(intent, PICK_FILE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE && resultCode == RESULT_OK){
            if (data != null){
                Uri uri = data.getData();
                Log.d("selectedURI", "uri data "+uri.toString());
                hodUploadingFile=uri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    binding.uploadDocumentHOD.setBackgroundColor(getColor(R.color.orange));
                }

            }
        }

    }


}