package com.tushar.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
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
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tushar.project.databinding.ActivityStudentDocumentUplaodBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentDocumentUplaod extends AppCompatActivity implements View.OnClickListener{

    ActivityStudentDocumentUplaodBinding binding;
    private static final int PICK_FILE = 1;
    PdfRenderer pdfRenderer;
    int type =0;
    public static final int PREDEFENCE_LETTER=7;
    public static final int MARKSHEET=2;
    public static final int THESISSUBMITTED=3;
    public static final int SYNOPSIS=4;
    public static final int THESISAWARDED=5;
    public static final int RDCUPLOAD=6;
    List<Integer> completedUpload;
    String enrollment_number, name;
    SharedPreferences preferences;
    HashMap<Integer, String> hashMap=new HashMap<>();
    RequestQueue requestQueue;
    CustomDialog customDialog;
    Uri marksheetUri , theisSubmitted , synopsisUri , thesisAwardedUri , rdcUploadUri,preDefenceLetter;
    List<Integer> selectedFiles;
    StorageReference storageRef;
    int viewType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this , R.layout.activity_student_document_uplaod);

        customDialog=CustomDialog.getInstance(this , "Upload , Please Wait .... ");

        requestQueue= Volley.newRequestQueue(this);

        Intent intent=getIntent();

       viewType=intent.getIntExtra("type", 1);

        if(viewType==1) {
            selectedFiles = new ArrayList<>();
            FirebaseStorage storage = FirebaseStorage.getInstance("gs://project-44332.appspot.com");
            storageRef = storage.getReference();

            completedUpload = new ArrayList<>();
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            name = preferences.getString("name", "");
            enrollment_number = preferences.getString("enrollment_number", "");
            binding.marksheetUploadButton.setOnClickListener(this);
            binding.thesisAwardedUploadButton.setOnClickListener(this);
            binding.rdcLetterUploadButton.setOnClickListener(this);
            binding.PreDefenceLetterUplaodButton.setOnClickListener(this);
            binding.synopsisAwardedButton.setOnClickListener(this);
            binding.thesisSubmittedLetterUploadButton.setOnClickListener(this);
            binding.enrollmentNumberInput.setText(enrollment_number);
            binding.nameInput.setText(name);



            binding.button4.setOnClickListener(this);

            makeApiCall(enrollment_number);

        }else{

            String enrollment_number= intent.getStringExtra("en");

            makeApiCall(enrollment_number);
            binding.button4.setVisibility(View.GONE);




        }
    }


    public void makeApiCall(String enrollment_number){

        String url = getString(R.string.domain_url) + "upload?param="+enrollment_number;

        customDialog.startDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        try {
                            JSONObject myJsonObject = new JSONObject(response);

                            boolean success = myJsonObject.optBoolean("success");
                            JSONArray data = myJsonObject.optJSONArray("results1");

                            if (success && data != null) {


                                    if(viewType==1 && data.length()>0 ){

                                        binding.marksheetUploadButton.setText("Completed");
                                        binding.marksheetUploadButton.setBackgroundColor(Color.GREEN);

                                        binding.thesisAwardedUploadButton.setText("Completed");
                                        binding.thesisAwardedUploadButton.setBackgroundColor(Color.GREEN);



                                        binding.thesisSubmittedLetterUploadButton.setText("Completed");
                                        binding.thesisSubmittedLetterUploadButton.setBackgroundColor(Color.GREEN);


                                        binding.PreDefenceLetterUplaodButton.setText("Completed");
                                        binding.PreDefenceLetterUplaodButton.setBackgroundColor(Color.GREEN);


                                        binding.synopsisAwardedButton.setText("Completed");
                                        binding.synopsisAwardedButton.setBackgroundColor(Color.GREEN);


                                        binding.rdcLetterUploadButton.setText("Completed");
                                        binding.rdcLetterUploadButton.setBackgroundColor(Color.GREEN);

                                        binding.button4.setVisibility(View.GONE);

                                    } else {

                                        for (int i = 0; i < data.length(); i++) {

                                            JSONObject obj = data.getJSONObject(i);
                                            StudentModel statusModel = new StudentModel();

//                                    statusModel.setCoursework(obj.optInt("coursework"));
//                                    statusModel.setPublication(obj.optInt("publication"));
//                                    statusModel.setRac(obj.optInt("rac"));
                                            statusModel.setEN(obj.optString("EN"));
                                            statusModel.setFullName(obj.optString("name"));
                                            statusModel.setRdcUrl(obj.optString("rdc"));
                                            statusModel.setMarksheetUrl(obj.optString("marksheet"));
                                            statusModel.setPdlUrl(obj.optString("pdl"));
                                            statusModel.setThesisub(obj.optString("thesisub"));
                                            statusModel.setThesisawa(obj.optString("thesisawa"));
                                            statusModel.setSynopsis(obj.optString("synopsis"));
                                            statusModel.setTitle(obj.optString("title"));

                                            setDatainUi(statusModel);


                                        }


                                    }
                            }

                        } catch (Exception e) {


                        }


                        customDialog.endDialog();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorVolley", error.toString().trim());
                Toast.makeText(StudentDocumentUplaod.this, "There is some error ", Toast.LENGTH_LONG).show();

                customDialog.endDialog();


            }
        });

        requestQueue.add(stringRequest);



    }
    public void setDatainUi(StudentModel studentModel){

        binding.titletext.setText("Document Detials");
        binding.enrollmentNumberInput.setText(studentModel.getEN());
        binding.nameInput.setText(studentModel.getFullName());

        binding.PreDefenceLetterUplaodButton.setText("View Doc.");
        binding.PreDefenceLetterUplaodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            try{
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(studentModel.getPdlUrl()));
                startActivity(browserIntent);

            }catch (Exception e){
                Toast.makeText(StudentDocumentUplaod.this , "Invalid document ", Toast.LENGTH_LONG).show();

            }
            }
        });
        binding.thesisSubmittedLetterUploadButton.setText("View Doc.");
        binding.thesisSubmittedLetterUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(studentModel.getThesisub()));
                    startActivity(browserIntent);
                }catch (Exception e){
                    Toast.makeText(StudentDocumentUplaod.this , "Invalid document ", Toast.LENGTH_LONG).show();

                }
            }
        });

        binding.thesisAwardedUploadButton.setText("View Doc.");
        binding.thesisAwardedUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            try{
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(studentModel.getThesisawa()));
                startActivity(browserIntent);

            }catch (Exception e){
                Toast.makeText(StudentDocumentUplaod.this , "Invalid document ", Toast.LENGTH_LONG).show();

            }
            }
        });

        binding.rdcLetterUploadButton.setText("View Doc.");
        binding.rdcLetterUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            try{
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(studentModel.getRdcUrl()));
                startActivity(browserIntent);

            }catch (Exception e){
                Toast.makeText(StudentDocumentUplaod.this , "Invalid document ", Toast.LENGTH_LONG).show();

            }

            }
        });

        binding.synopsisAwardedButton.setText("View Doc.");
        binding.synopsisAwardedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(studentModel.getSynopsis()));
                startActivity(browserIntent);

            }catch (Exception e){
                Toast.makeText(StudentDocumentUplaod.this , "Invalid document ", Toast.LENGTH_LONG).show();

            }

            }
        });

        binding.marksheetUploadButton.setText("View Doc.");
        binding.marksheetUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{


                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(studentModel.getMarksheetUrl()));
                    startActivity(browserIntent);
                }
                catch (Exception e){
                    Toast.makeText(StudentDocumentUplaod.this, "Invalid Link", Toast.LENGTH_LONG).show();
                }
            }
        });


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

               if(type==MARKSHEET){

                   marksheetUri=uri;

               }else if(type==THESISAWARDED){

                   thesisAwardedUri=uri;


               }else if (type==THESISSUBMITTED){

                    theisSubmitted=uri;

               }else if (type==SYNOPSIS){

                   synopsisUri=uri;


               }else if (type==RDCUPLOAD){

                   rdcUploadUri=uri;

               }else if(type==PREDEFENCE_LETTER){
                   preDefenceLetter=uri;


               }

                try {
                    ParcelFileDescriptor parcelFileDescriptor = getContentResolver()
                            .openFileDescriptor(uri, "r");
                    pdfRenderer = new PdfRenderer(parcelFileDescriptor);
                    int total_pages = pdfRenderer.getPageCount();
                    int display_page = 0;
                    _display(display_page);

                } catch (FileNotFoundException fnfe){

                } catch (IOException e){

                }
            }
        }

    }
    private void _display(int _n) {
        if (pdfRenderer != null) {
            PdfRenderer.Page page = pdfRenderer.openPage(_n);
            Bitmap mBitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
            page.render(mBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            // imageview1.setImageBitmap(mBitmap);
            page.close();
            //textview1.setText((_n + 1) + "/" + total_pages);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {



        if (view.getId()==binding.marksheetUploadButton.getId()){

            binding.marksheetUploadButton.setText("Selected");
            binding.marksheetUploadButton.setBackgroundColor(getColor(R.color.orange));
            type=MARKSHEET;
            accessPdf();
            selectedFiles.add(MARKSHEET);


        }
        else if (view.getId()==binding.thesisAwardedUploadButton.getId()){


            binding.thesisAwardedUploadButton.setText("Selected");
            binding.thesisAwardedUploadButton.setBackgroundColor(getColor(R.color.orange));
            type=THESISAWARDED;

            accessPdf();
            selectedFiles.add(THESISAWARDED);

        }else if (view.getId()==binding.rdcLetterUploadButton.getId()){

            binding.rdcLetterUploadButton.setText("Selected");
            binding.rdcLetterUploadButton.setBackgroundColor(getColor(R.color.orange));

            type=RDCUPLOAD;
            accessPdf();
            selectedFiles.add(RDCUPLOAD);


        }else if (view.getId()==binding.synopsisAwardedButton.getId()){


            binding.synopsisAwardedButton.setText("Selected");
            binding.synopsisAwardedButton.setBackgroundColor(getColor(R.color.orange));

            type=SYNOPSIS;
            accessPdf();
            selectedFiles.add(SYNOPSIS);


        }else if(view.getId()==binding.thesisSubmittedLetterUploadButton.getId()){

            binding.thesisSubmittedLetterUploadButton.setText("Selected");
            binding.thesisSubmittedLetterUploadButton.setBackgroundColor(getColor(R.color.orange));

            type=THESISSUBMITTED;
            accessPdf();
            selectedFiles.add(THESISSUBMITTED);

        }else if (view.getId()==binding.PreDefenceLetterUplaodButton.getId()) {

            binding.PreDefenceLetterUplaodButton.setText("Selected");
            binding.PreDefenceLetterUplaodButton.setBackgroundColor(getColor(R.color.orange));

            type = PREDEFENCE_LETTER;
            accessPdf();
            selectedFiles.add(PREDEFENCE_LETTER);
        }

        else{

            startUploading();


        }





    }
    public void startUploading(){
        String type="";


        if(selectedFiles.size()<6){
            Toast.makeText(this , "Please select all files", Toast.LENGTH_LONG ).show();

            return ;
        }

        for(int i: selectedFiles){
            customDialog.startDialog();
            if(i==MARKSHEET) {
                type = "marksheet";
            }else if(i==THESISAWARDED){
                type="awaited";
            }
            else if(i==THESISSUBMITTED){
                type="submitted";
            }else if(i==SYNOPSIS){
                type="synopsis";
            }else if (i==RDCUPLOAD){
                type="rdcupload";
            }else if (i==PREDEFENCE_LETTER){
                type="predefence";

            }


            StorageReference riversRef = storageRef.child("uploads/"+type+"/"+enrollment_number+".pdf");


           riversRef.putFile(marksheetUri).continueWithTask(new Continuation() {
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
                        if(customDialog!=null)
                            customDialog.endDialog();
                        Uri uri = task.getResult();
                        String myurl;
                        myurl = uri.toString();

                        if(i==MARKSHEET){

                            binding.marksheetUploadButton.setText("Completed");
                            binding.marksheetUploadButton.setBackgroundColor(Color.GREEN);

                            hashMap.put(MARKSHEET, myurl);

                        } else if(i==THESISAWARDED){
                            binding.thesisAwardedUploadButton.setText("Completed");
                            binding.thesisAwardedUploadButton.setBackgroundColor(Color.GREEN);

                            hashMap.put(THESISAWARDED, myurl);

                        }
                        else if(i==THESISSUBMITTED){
                            binding.thesisSubmittedLetterUploadButton.setText("Completed");
                            binding.thesisSubmittedLetterUploadButton.setBackgroundColor(Color.GREEN);

                            hashMap.put(THESISSUBMITTED, myurl);
                        }else if(i==SYNOPSIS){

                            binding.synopsisAwardedButton.setText("Completed");
                            binding.synopsisAwardedButton.setBackgroundColor(Color.GREEN);

                            hashMap.put(SYNOPSIS, myurl);

                        }else if (i==RDCUPLOAD){
                            binding.rdcLetterUploadButton.setText("Completed");
                            binding.rdcLetterUploadButton.setBackgroundColor(Color.GREEN);

                            hashMap.put(RDCUPLOAD, myurl);

                        }else if (i==PREDEFENCE_LETTER){


                            binding.PreDefenceLetterUplaodButton.setText("Completed");
                            binding.PreDefenceLetterUplaodButton.setBackgroundColor(Color.GREEN);

                            hashMap.put(PREDEFENCE_LETTER, myurl);
                        }

                        if(hashMap.size()==6){


                            sendToServer();

                        }

                    } else {
                        customDialog.endDialog();
                        Toast.makeText(StudentDocumentUplaod.this, "UploadedFailed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }


    }

    public void sendToServer(){

        customDialog.startDialog();

        String ur=getString(R.string.domain_url)+"upload";

        try {

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("name", name);
            jsonBody.put("EN", enrollment_number);
            jsonBody.put("rdc", hashMap.get(RDCUPLOAD));
            jsonBody.put("marksheet", hashMap.get(MARKSHEET));
            jsonBody.put("pdl", hashMap.get(PREDEFENCE_LETTER));
            jsonBody.put("thesisub", hashMap.get(THESISSUBMITTED));
            jsonBody.put("thesisawa", hashMap.get(THESISAWARDED));
            jsonBody.put("synopsis", hashMap.get(SYNOPSIS));


            final String requestBody = jsonBody.toString();
            Log.d("request", requestBody+" form saving");


            StringRequest stringRequest = new StringRequest(Request.Method.POST, ur, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    customDialog.endDialog();

                    Log.d("errorupload", response);
                    try {
                        JSONObject myJsonObject = new JSONObject(response);

                        boolean success= myJsonObject.optBoolean("success");
                        String message =myJsonObject.optString("message");
                        if(success){
                            Toast.makeText(StudentDocumentUplaod.this , "Success: "+message, Toast.LENGTH_SHORT).show();

                            finish();
                        }else{
                            Toast.makeText(StudentDocumentUplaod.this , "Error", Toast.LENGTH_LONG).show();

                        }

                    }
                    catch (Exception e){


                    }





                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    customDialog.endDialog();

                    Log.d("errorupload", error.toString());
                    Toast.makeText(StudentDocumentUplaod.this , "Error:"+error.toString().trim(), Toast.LENGTH_LONG).show();


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