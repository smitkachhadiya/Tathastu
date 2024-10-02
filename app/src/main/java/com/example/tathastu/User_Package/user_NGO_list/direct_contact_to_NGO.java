package com.example.tathastu.User_Package.user_NGO_list;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class direct_contact_to_NGO extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, PaymentResultListener {


    private RecyclerView recyclerView;
    private NGODataAdapter adapter;
    private List<NGOData> dataList;

    private  List<String> catList;
    private ConnectivityReceiver connectivityReceiver;

    MaterialAutoCompleteTextView dropcat;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_contact_to_ngo);


        FloatingActionButton BTN_back = findViewById(R.id.BTN_back);
        recyclerView = findViewById(R.id.recycle_ngo_detail);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataList = new ArrayList<>();
        adapter = new NGODataAdapter(dataList, this);
        recyclerView.setAdapter(adapter);

        dropcat=findViewById(R.id.dropcat);
        catList = new ArrayList<>();
        catList.add("All");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(direct_contact_to_NGO.this, android.R.layout.simple_list_item_1,catList);
        dropcat.setAdapter(arrayAdapter);
        dropcat.setText("All",false);

        fetchJsonData("All");


        // Check if it's triggered from the NGO dashboard
        if ("ngoDashboard".equals(getIntent().getStringExtra("source"))) {
            // Hide donate button
            adapter.setShowDonateButton(false);
        } else {
            // Show donate button (default behavior for user)
            adapter.setShowDonateButton(true);
        }

        dropcat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fetchJsonData(catList.get(position));
            }
        });

        // Fetch helpline numbers from the API
//        new FetchDataTask().execute("https://meetk1803.github.io/tathastu_quotes_api/ngo_list.json");

        // Initialize the ConnectivityReceiver
        connectivityReceiver = new ConnectivityReceiver();
        ConnectivityReceiver.connectivityReceiverListener = this;

        // Register the receiver to listen for connectivity changes
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        // BACK

        BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

                Animatoo.INSTANCE.animateSlideRight(direct_contact_to_NGO.this);
            }
        });
    }

    //----------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(this);
        finish();
    }
    @Override
    public void onPaymentSuccess(String s) {
        // Handle payment success
        showToast("Payment Successful");
    }

    @Override
    public void onPaymentError(int i, String s) {
        // Handle payment failure
        showToast("Payment Failed: " + s);
    }


    void fetchJsonData(String cat){

        dataList.clear();

        requestQueue = Volley.newRequestQueue(this);

        // URL of the JSON file on the server
        String url = "https://meetk1803.github.io/tathastu_quotes_api/ngo_list.json";

        // Make a request to fetch JSON data
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Handle the JSON array response
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                // Extract data from JSON object
                                String Name=jsonObject.getString("ngo_name");
                                String Number=jsonObject.getString("mob");
                                String Category=jsonObject.getString("category");
                                String Address=jsonObject.getString("address");
                                String Website=jsonObject.getString("website");
                                String Email=jsonObject.getString("email");
                                String Instagram=jsonObject.getString("instagram");
                                String LinkedIn=jsonObject.getString("linkedin");
                                String Facebook=jsonObject.getString("facebook");
                                String Twitter=jsonObject.getString("twitter");
                                String Youtube=jsonObject.getString("youtube");

                                if(cat.equals("All")) {
                                    NGOData ngoData = new NGOData(Name, Address, Category, Number, Website, Email, Instagram, LinkedIn, Facebook, Twitter, Youtube);
                                    dataList.add(ngoData);
                                }else if(Category.equals(cat)){
                                    NGOData ngoData = new NGOData(Name, Address, Category, Number, Website, Email, Instagram, LinkedIn, Facebook, Twitter, Youtube);
                                    dataList.add(ngoData);
                                }

                                if(!catList.contains(Category))
                                {
                                    catList.add(Category);
                                }
                            }
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Toast.makeText(direct_contact_to_NGO.this, "Error to Fetch Data!!", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the request queue
        requestQueue.add(jsonArrayRequest);
    }


//    private class FetchDataTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... urls) {
//            try {
//                return fetchDataFromApi(urls[0]);
//            } catch (IOException e) {
//                e.printStackTrace();
//                return "Error fetching data from API";
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            if (result != null && !result.startsWith("Error")) {
//                parseJsonData(result);
//            } else {
//                if (!isNetworkAvailable()) {
//                    showToast("No internet connection");
//                } else {
//                    showToast(result);
//                }
//            }
//        }
//
//        private boolean isNetworkAvailable() {
//            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//        }
//    }


//    private String fetchDataFromApi(String apiUrl) throws IOException {
//        HttpURLConnection urlConnection = null;
//        BufferedReader reader = null;
//        String jsonData = null;
//
//        try {
//            URL url = new URL(apiUrl);
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.connect();
//
//            InputStream inputStream = urlConnection.getInputStream();
//            StringBuilder builder = new StringBuilder();
//            if (inputStream == null) {
//                return null;
//            }
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                builder.append(line).append("\n");
//            }
//            if (builder.length() == 0) {
//                return null;
//            }
//            jsonData = builder.toString();
//        } finally {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        return jsonData;
//    }

//    private void parseJsonData(String jsonData) {
//        try {
//            JSONArray jsonArray = new JSONArray(jsonData);
//
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//              String Name=jsonObject.getString("ngo_name");
//                String Number=jsonObject.getString("mob");
//                String Category=jsonObject.getString("category");
//                String Address=jsonObject.getString("address");
//                String Website=jsonObject.getString("website");
//                String Email=jsonObject.getString("email");
//                String Instagram=jsonObject.getString("instagram");
//                String LinkedIn=jsonObject.getString("linkedin");
//                String Facebook=jsonObject.getString("facebook");
//                String Twitter=jsonObject.getString("twitter");
//                String Youtube=jsonObject.getString("youtube");
//
//                NGOData ngoData=new NGOData(Name,Address,Category,Number,Website,Email,Instagram,LinkedIn,Facebook,Twitter,Youtube);
//                dataList.add(ngoData);
//            }
//
//            adapter.notifyDataSetChanged();
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(direct_contact_to_NGO.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unregister the receiver to avoid memory leaks
        unregisterReceiver(connectivityReceiver);
    }

    //SNACKBAR
    private void showSnackbar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();

        // Inflate custom layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View customView = inflater.inflate(R.layout.custom_snackbar_layout, null);

        // Set text
        TextView textView = customView.findViewById(android.R.id.text1);
        textView.setText(message);

        // Add custom view to Snackbar
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbarView;
        snackbarLayout.removeAllViews(); // Remove all default views
        snackbarLayout.setPadding(1, 1, 1, 1);
        snackbarLayout.addView(customView, 0);

        snackbar.show();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
        }
    }
}
