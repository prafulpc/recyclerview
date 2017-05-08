package com.pratap.endlessrecyclerview;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    ProgressDialog pDialog;
    private TextView tvEmptyView;
    private RecyclerView mRecyclerView;
    private DataAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<GalleryPojo> galleryList = new ArrayList<>();
    String urlGetImage = "http://182.56.133.128:9001/ca/services/rest/api/v1/pictures/1";
    private List<GalleryPojo> studentList;


    protected Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvEmptyView = (TextView) findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        studentList = new ArrayList<GalleryPojo>();
        handler = new Handler();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Android Students");

        }

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading Images.. Please Wait..!");
        pDialog.setCancelable(false);
        Log.i("main ", "called");

        loadImages();
//       loadData();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new DataAdapter(studentList, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        if (studentList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);

        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }

        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                studentList.add(null);
                mAdapter.notifyItemInserted(studentList.size() - 1);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        studentList.remove(studentList.size() - 1);
                        mAdapter.notifyItemRemoved(studentList.size());
                        //add items one by one
                        int start = studentList.size();
                        int end = start + 20;

                        for (int i = start + 1; i <= end; i++) {
                            studentList.add(new GalleryPojo("Student " + i, "AndroidStudent" + i + "@gmail.com"));
                           // studentList.add(new GalleryPojo(galleryList.get(i).getImageComment(),galleryList.get(i).getImage(),galleryList.get(i).getDate_time(),galleryList.get(i).getUploaded_by()));
                            mAdapter.notifyItemInserted(studentList.size());
                        }
                        mAdapter.setLoaded();
                        //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                    }
                }, 2000);

            }
        });

    }

    private void showProgressDialog() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing()) {
            pDialog.hide();
        }
    }

    private void loadImages() {
        Log.i("volley ", "called");
        JsonArrayRequest jsonObjectRequest = null;
        showProgressDialog();
        jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, urlGetImage, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i <= response.length() - 1; i++) {
                            JSONObject jsonObject = response.optJSONObject(i);

                            String uploaded_by = jsonObject.optString("uploaded_by");
                            String imageComment = jsonObject.optString("comment");
                            String image = jsonObject.optString("picture");
                            String date_time = jsonObject.optString("date");

                            GalleryPojo galleryPojo = new GalleryPojo();
                            galleryPojo.setImage(image);
                            galleryPojo.setImageComment(imageComment);
                            galleryPojo.setUploaded_by(uploaded_by);
                            galleryPojo.setDate_time(date_time);
                            galleryList.add(galleryPojo);

                            Log.i("Image length", String.valueOf(galleryList.size()));
                        }

                        create(galleryList);
//                        hideProgressDialog();
                        // pb_vertical.setVisibility(View.GONE);
//                        mAdapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse networkResponse = error.networkResponse;

                if (networkResponse != null) {

                    Log.i("Volley", "Error. HTTP Status Code:" + networkResponse.statusCode);

                    // hideProgressDialog();
                }
                if (error instanceof TimeoutError) {
                    Log.i("Volley", "TimeoutError");
                } else if (error instanceof NoConnectionError) {
                    Log.i("Volley", "NoConnectionError");

                   /* View parentLayout = findViewById(R.id.activity_gallery_view);
                    Snackbar snackbar;
                    snackbar = Snackbar.make(parentLayout, "Cannot connect to network. Please try again later", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackbar.setAction("Action", null);
                    snackBarView.setBackgroundColor(Color.parseColor("#FF0000"));
                    snackbar.show();*/
                } else if (error instanceof AuthFailureError) {
                    Log.i("Volley", "AuthFailureError");
                } else if (error instanceof ServerError) {
                    Log.i("Volley", "ServerError");
                } else if (error instanceof NetworkError) {
                    Log.i("Volley", "NetworkError");
                } else if (error instanceof ParseError) {
                    Log.i("Volley", "ParseError");
                }

                  hideProgressDialog();
            }
        });
        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    // load initial data
    private void loadData() {

        for (int i = 1; i <= 20; i++) {
            studentList.add(new GalleryPojo("Student " + i, "androidstudent" + i));

        }
    }

    private void create(List<GalleryPojo> galleryList) {

        int i=0;
        //for (int i = 1; i <= galleryList.size()-1; i++)
        {
            //studentList.add(new GalleryPojo("Student " + i, "androidstudent" + i + "@gmail.com"));
            studentList.add(new GalleryPojo(galleryList.get(i).getImageComment(),galleryList.get(i).getImage(),galleryList.get(i).getDate_time(),galleryList.get(i).getUploaded_by()));

        }

        mAdapter.notifyDataSetChanged();
    }


}
