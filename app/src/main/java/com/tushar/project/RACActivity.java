package com.tushar.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tushar.project.databinding.ActivityRacactivityBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RACActivity extends AppCompatActivity {

    private static final int PICK_FILE = 1;
    PdfRenderer pdfRenderer;
    ActivityRacactivityBinding binding;
    RequestQueue requestQueue ;
    Uri bitmap;
    boolean imageSelected=false;
    SharedPreferences preferences;
    StorageReference storageRef;
    CustomDialog customDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this , R.layout.activity_racactivity);

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://project-44332.appspot.com");
        storageRef = storage.getReference();
        customDialog=new CustomDialog(this, "Uploading Image ...." );
        requestQueue= Volley.newRequestQueue(this);
        preferences= PreferenceManager.getDefaultSharedPreferences(getApplication());
        SetDate setDate=new SetDate(binding.dorinput , this);
        String enrollment_number =preferences.getString("enrollment_number","");
        binding.enrollmentNumberInput.setText(enrollment_number);
        binding.uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                accessPdf();



            }
        });


        binding.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(binding.enrollmentNumberInput.getText()==null || binding.enrollmentNumberInput.getText().toString().trim().length()==0){

                    binding.enrollmentLayout.setError("Invalid Enrollment Number");
                }else{

                    if(binding.dorinput.getText()==null || binding.dorinput.getText().toString().trim().length()==0){
                        binding.dorlayout.setError("Invalid DOR ");

                    }
                    else{

                      if(binding.batchInput.getText()==null || binding.batchInput.getText().toString().trim().length()==0){
                          binding.batchLayout.setError("Invalid batch");
                      }else{

                          if(bitmap==null ){
                              binding.uploadButton.setError("Upload Image");

                          }else{
                              String batch=binding.batchInput.getText().toString().trim();
                              String enrollmentNumber =binding.enrollmentNumberInput.getText().toString().trim();
                              String DOR =binding.dorinput.getText().toString().trim();

                              uploadRac(batch, enrollmentNumber, DOR);


                          }

                      }
                    }
                }
            }
        });
    }




    private void uploadRac(String batch, String enrollmentNumber , String DOR) {

        customDialog.startDialog();

//        Uri file = Uri.fromFile(new File(bitmap.toString()));

        StorageReference riversRef = storageRef.child("racUpload/"+enrollmentNumber+".pdf");


        riversRef.putFile(bitmap).continueWithTask(new Continuation() {
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

                    uploadRACToServer(myurl , batch, enrollmentNumber, DOR );
                } else {
                    customDialog.endDialog();
                    Toast.makeText(RACActivity.this, "UploadedFailed", Toast.LENGTH_SHORT).show();
                }
            }
        });





    }


    public void uploadRACToServer(String downloadUrl, String batch, String enrollmentNumber, String DOR){



        String URL=getString(R.string.domain_url)+"rac ";
        try {

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("DOR", DOR);
            jsonBody.put("enrollment_number", enrollmentNumber);
            jsonBody.put("batch", batch);
            jsonBody.put("document", downloadUrl);




            final String requestBody = jsonBody.toString();
            Log.d("request", requestBody+" form saving");


            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    customDialog.endDialog();

                    try {
                        JSONObject myJsonObject = new JSONObject(response);

                        boolean success= myJsonObject.optBoolean("success");
                        String message =myJsonObject.optString("message ");
                        if(success){
                            Toast.makeText(RACActivity.this , "Success: "+message, Toast.LENGTH_LONG).show();
                            finish();
                        }else{
                            Toast.makeText(RACActivity.this , "Error", Toast.LENGTH_LONG).show();

                        }

                    }
                    catch (Exception e){


                    }





                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    customDialog.endDialog();


                    Toast.makeText(RACActivity.this , "Error:"+error.toString(), Toast.LENGTH_LONG).show();


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
                Log.d("errorVolley", "uri data "+uri.toString());
                bitmap=uri;

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


}