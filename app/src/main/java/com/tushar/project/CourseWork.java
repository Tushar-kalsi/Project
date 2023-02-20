package com.tushar.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
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
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tushar.project.databinding.ActivityCourseWorkBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class CourseWork extends AppCompatActivity {

    public static final int TEACHER_VIEW=2;

    PdfRenderer renderer;
    RequestQueue requestQueue ;
    int attempt=0;
    String enrollment_number;
    private static final int PICK_FILE = 1;
    boolean imageSelected=false;
    Uri pdfUri ;

    SharedPreferences preferences;
    StorageReference storageRef;
    CustomDialog dialog;
    ActivityCourseWorkBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=DataBindingUtil.setContentView(this , R.layout.activity_course_work);

        requestQueue= Volley.newRequestQueue(this);
        preferences= PreferenceManager.getDefaultSharedPreferences(getApplication());

        enrollment_number=preferences.getString("enrollment_number", " ");
        int type=preferences.getInt("type",1);

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://project-44332.appspot.com");
        storageRef = storage.getReference();
        dialog=new CustomDialog(this, "Loading ...." );


        if(type==TEACHER_VIEW || type==3){

            Intent intent=getIntent();
            String en =intent.getStringExtra("en");

            binding.titletext.setText("Course Work \nInformation");
            makeApiCall(en);
            binding.subjectNameInput.setEnabled(false);
            binding.subjectCodeInput.setEnabled(false);

            binding.button4.setText("Back");

            binding.button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });



        }else{



            Log.d("errorVolley", enrollment_number+" enrollment numnber ");


            String name =preferences.getString("name","");
            binding.nameInput.setText(name);
            getAllCourseWork();


            binding.button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    makePostRequest();
                }
            });

        }


    }
    private void makeApiCall(String en){


        String url =getString(R.string.domain_url)+"coursework?en="+en;
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


                            if(success){
                                JSONArray data =myJsonObject.optJSONArray("results");
                                if(data!=null && data.length()>0){


                                    attempt=data.length();
                                    String subjectName="";
                                    String subjectCode="";
                                    String document="";
                                    for(int i =0;i<data.length();i++){
                                        JSONObject jsonObject= (JSONObject) data.get(i);
                                        String naem =jsonObject.optString("name");

                                        subjectCode += jsonObject.optString("SubCode")+"\n";
                                        subjectName += jsonObject.optString("SubName")+"\n";

                                        String en=jsonObject.optString("EN");
                                         document= jsonObject.optString("Document");

                                        binding.enrollmentNumberInput.setText(en);
                                        binding.nameInput.setText(naem);

                                    }



                                    binding.subjectNameInput.setText(subjectName.trim());
                                    binding.subjectCodeInput.setText(subjectCode.trim());

                                    if(document.equals("null")){

                                        binding.uploadButton.setVisibility(View.GONE);

                                    }else{
                                        String finalDocument = document;
                                        binding.uploadButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalDocument));
                                                startActivity(browserIntent);
                                            }
                                        });



                                    }

                                }

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
                Toast.makeText(CourseWork.this , "There is some error ", Toast.LENGTH_LONG).show();

                dialog.endDialog();


            }
        });

// Add the request to the RequestQueue.
        requestQueue.add(stringRequest);

    }

    public void getAllCourseWork(){
        binding.enrollmentNumberInput.setText(enrollment_number+" ");
        String url =getString(R.string.domain_url)+"coursework?en="+enrollment_number;
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

                            Log.d("attemptCount", "boolean attempt "+success + "  "+response);
                            if(success){
                                JSONArray data =myJsonObject.optJSONArray("results");


                                if(data==null){
                                    attempt=0;

                                }else{

                                    attempt=data.length();

                                }
                                Log.d("attemptCount", "Counting attempt "+attempt);

                                binding.uploadButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Log.d("errorVolley", "clicked ");


                                        if(attempt<4) {
                                            Toast.makeText(CourseWork.this, "You can only Upload document on " + (4 - attempt) + " complete Course work registration ", Toast.LENGTH_LONG).show();
                                        }else{

                                            accessPdf();


                                        }

                                    }
                                });


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
                Toast.makeText(CourseWork.this , "There is some error ", Toast.LENGTH_LONG).show();

                dialog.endDialog();


            }
        });

// Add the request to the RequestQueue.
        requestQueue.add(stringRequest);
    }
    public void makePostRequest(){

        if(binding.enrollmentNumberInput.getText()==null || binding.enrollmentNumberInput.getText().toString().trim().trim().length()==0){

            binding.enrollmentLayout.setError("Invalid enrollment");
        }else{

            if(binding.subjectCodeInput.getText() ==null || binding.subjectCodeInput.getText().toString().trim().trim().length()==0){

                binding.subjectCodeLayout.setError("Invalid SubjectCode");

            }else{

                if(binding.subjectNameInput.getText()==null || binding.subjectNameInput.getText().toString().trim().trim().length()==0){

                    binding.subjectNameLayout.setError("Invalid subject Error");


                }else{

                    String subJectCode=binding.subjectCodeInput.getText().toString().trim().trim();
                    String subjectName =binding.subjectNameInput.getText().toString().trim().trim();
                    String enrollmentNumber =binding.enrollmentNumberInput.getText().toString().trim().trim();

                    storeCourseWork(subJectCode, enrollmentNumber, subjectName, null);
                }
            }
        }


    }

    public void storeCourseWork(String subjectCode , String enrollNumber, String subjectNaem , String docuemnt){

        dialog.startDialog();

        if(attempt>=4){

            if(pdfUri==null){
                binding.uploadButton.setError("Document Required ");
                return;
            }
            StorageReference riversRef = storageRef.child("coursework/"+enrollment_number+".pdf");

            riversRef.putFile(pdfUri).continueWithTask(new Continuation() {
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

                        uploadToAWS(myurl , subjectCode, enrollNumber, subjectNaem );
                    } else {
                        dialog.endDialog();
                        Toast.makeText(CourseWork.this, "Uploaded Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }else{

            uploadToAWS(null , subjectCode, enrollNumber, subjectNaem);

        }


    }

    public void uploadToAWS (String document ,String subjectCode, String enrollNumber, String subjectNaem ){
        String URL=getString(R.string.domain_url)+"coursework";
        try {

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("subject_code", subjectCode);
            jsonBody.put("enrollment_number", enrollNumber);
            jsonBody.put("subject_name", subjectNaem);
            jsonBody.put("document", document);


            final String requestBody = jsonBody.toString().trim();
            Log.d("request", requestBody+" form saving");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject myJsonObject = new JSONObject(response);

                        boolean success= myJsonObject.optBoolean("success");
                        String message =myJsonObject.optString("message ");
                        if(success){
                            Toast.makeText(CourseWork.this , "Success: "+message, Toast.LENGTH_LONG).show();
                            finish();
                        }else{
                            Toast.makeText(CourseWork.this , "Error", Toast.LENGTH_LONG).show();

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


                    Toast.makeText(CourseWork.this , "Error:"+error.toString().trim(), Toast.LENGTH_LONG).show();


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
                pdfUri=uri;

                Log.d("errorVolley", "uri data "+uri.toString());

                try {
                    ParcelFileDescriptor parcelFileDescriptor = getContentResolver()
                            .openFileDescriptor(uri, "r");
                    renderer = new PdfRenderer(parcelFileDescriptor);
                    int total_pages = renderer.getPageCount();
                   int display_page = 0;
                    _display(display_page);
                } catch (FileNotFoundException fnfe){

                } catch (IOException e){

                }
            }
        }

    }
    private void _display(int _n) {
        if (renderer != null) {
            PdfRenderer.Page page = renderer.openPage(_n);
            Bitmap mBitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
            page.render(mBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
           // imageview1.setImageBitmap(mBitmap);
            page.close();
            //textview1.setText((_n + 1) + "/" + total_pages);
        }
    }

}