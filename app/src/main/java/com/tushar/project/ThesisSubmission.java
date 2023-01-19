package com.tushar.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
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
import com.tushar.project.databinding.ActivityThesisSubmissionBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ThesisSubmission extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {
    public static final int PICK_FILE=1;
    PdfRenderer pdfRenderer;
    Uri bitmap;
    RequestQueue requestQueue ;

    ActivityThesisSubmissionBinding binding;
    String enrollment_number;
    SharedPreferences preferences;
    String selected_type_journal;
    CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding= DataBindingUtil.setContentView(this , R.layout.activity_thesis_submission);
        preferences= PreferenceManager.getDefaultSharedPreferences(getApplication());

        requestQueue=Volley.newRequestQueue(this);

        int type=preferences.getInt("type", 1) ;

        if(type==2 || type ==3){
            customDialog=new CustomDialog(this , "Loading, Please wait .... ");

            Intent intent=getIntent();

            String en=intent.getStringExtra("en");


            binding.typeSpinner.setVisibility(View.GONE);
            binding.typeTextView.setVisibility(View.VISIBLE);
            makeApiCallToGet(en);



            binding.titletext.setText("View Publication");
            binding.button4.setText("Back");
            binding.button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    finish();

                }
            });

        }else{

            binding.studentNmaeInput.setText(preferences.getString("name" ," "));

            customDialog=new CustomDialog(this , "Upload Image .... ");
        SetDate setDate=new SetDate(binding.dateofpublicatoinInput, this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_jornals, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        binding.typeSpinner.setAdapter(adapter);
        binding.typeSpinner.setOnItemSelectedListener(this);


        enrollment_number= preferences.getString("enrollment_number", "");

        binding.enrollmentInout.setText(enrollment_number);


        binding.uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                accessPdf();
            }
        });

        binding.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(binding.titleNameInput.getText()==null || binding.titleNameInput.getText().toString().trim().length()==0){
                    binding.deptRegLayout.setError("Invalid Title");
                }else{

                    if(binding.journalInput.getText()==null || binding.journalInput.getText().toString().trim().length()==0){
                        binding.firstNamelayout.setError("Invalid Journal");

                    }else{


                        if(selected_type_journal==null){
                            TextView txt= (TextView)( binding.typeSpinner.getSelectedView());
                            txt.setError("Type issue");
                            txt.setTextColor(Color.RED);


                        }else{

                            if(binding.dateofpublicatoinInput.getText()==null || binding.dateofpublicatoinInput.getText().toString().trim().length()==0){

                                binding.datepublicationLayout.setError("Invalid DOP");
                            }else{

                                if(bitmap==null){
                                    binding.uploadButton.setError("Invalid Document ");
                                }else{

                                    String title =binding.titleNameInput.getText().toString().trim();
                                    String journal=binding.journalInput.getText().toString().trim();
                                    String dop=binding.dateofpublicatoinInput.getText().toString().trim();

                                    uploadThesis(title, enrollment_number,journal, dop , selected_type_journal );


                                }
                            }
                        }
                    }

                }
            }
        });
        }

    }

    private void makeApiCallToGet(String enrollment_number){


        String url =getString(R.string.domain_url)+"publication?en="+enrollment_number;
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
                            JSONObject data =myJsonObject.optJSONObject("message");

                            if(success){

                                if(data!=null ){
                                    binding.studentNmaeInput.setText(data.optString("name"));
                                    binding.titleNameInput.setText(data.optString("title"));
                                    binding.enrollmentInout.setText (data.optString("EN"));
                                    binding.journalInput.setText (data.optString("journal"));
                                    binding.dateofpublicatoinInput.setText(data.optString("dop"));
                                    binding.typeTextView.setText( data.optString("type"));
                                    binding.enrollmentInout.setText(data.optString("en"));
                                    String doc= data.optString("doc");

                                    if(!doc.equals("null")){

                                        binding.uploadButton.setText("View Document");
                                        binding.uploadButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(doc));
                                                startActivity(browserIntent);


                                            }
                                        });
                                    }

                                }
                            }

                        }
                        catch (Exception e){


                        }

                        customDialog.endDialog();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ThesisSubmission.this , "There is some error ", Toast.LENGTH_LONG).show();

                customDialog.endDialog();


            }
        });

// Add the request to the RequestQueue.
        requestQueue.add(stringRequest);

    }

    private void uploadThesis(String title, String enrollmentNumber , String journal, String dop , String type ) {

        customDialog.startDialog();

//        Uri file = Uri.fromFile(new File(bitmap.toString()));
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://project-44332.appspot.com");
        StorageReference storageRef = storage.getReference();
        StorageReference riversRef = storageRef.child("thesis/"+enrollmentNumber+"_"+title+".pdf");


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

                    uploadThesisToSever(myurl , journal, enrollmentNumber, dop , type, title );
                } else {
                    customDialog.endDialog();
                    Toast.makeText(ThesisSubmission.this, "Uploaded Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    public void uploadThesisToSever(String downloadUrl, String journal, String enrollmentNumber, String dop, String type, String title){



        String URL=getString(R.string.domain_url)+"publication";
        try {

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("title", title);
            jsonBody.put("enrollment_number", enrollmentNumber);
            jsonBody.put("journal", journal);
            jsonBody.put("type", selected_type_journal);
            jsonBody.put("dop", dop);
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
                            Toast.makeText(ThesisSubmission.this , "Success: "+message, Toast.LENGTH_LONG).show();
                            finish();
                        }else{
                            Toast.makeText(ThesisSubmission.this , "Error", Toast.LENGTH_LONG).show();

                        }

                    }
                    catch (Exception e){
                        Toast.makeText(ThesisSubmission.this , "Error", Toast.LENGTH_LONG).show();


                    }





                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    customDialog.endDialog();


                    Toast.makeText(ThesisSubmission.this , "Error:"+error.toString(), Toast.LENGTH_LONG).show();


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


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String val = (String) adapterView.getItemAtPosition(i);
        selected_type_journal=val;

        Log.d("errorVolley", "Item selected");

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}