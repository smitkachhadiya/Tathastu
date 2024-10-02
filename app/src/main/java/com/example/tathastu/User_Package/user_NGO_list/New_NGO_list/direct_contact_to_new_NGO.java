package com.example.tathastu.User_Package.user_NGO_list.New_NGO_list;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.NGO_Package.NGO_Profile.NGO_Profile_Model;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.example.tathastu.User_Package.user_NGO_list.NGOData;
import com.example.tathastu.User_Package.user_NGO_list.NGODataAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.PaymentResultListener;

import java.util.ArrayList;
import java.util.List;


public class direct_contact_to_new_NGO extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, PaymentResultListener {


    private RecyclerView recyclerView;
    private NGODataAdapter adapter;
    private List<NGOData> dataList;

    private  List<String> catList;
    private ConnectivityReceiver connectivityReceiver;

    MaterialAutoCompleteTextView dropcat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_contact_to_newngo);
        dataList = new ArrayList<>();

        FloatingActionButton BTN_back = findViewById(R.id.BTN_back);
        recyclerView = findViewById(R.id.recycle_ngo_detail);


        dropcat=findViewById(R.id.dropcat);
        catList = new ArrayList<>();
        catList.add("All");

        fetchNGOData("All");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NGODataAdapter(dataList, this);
        recyclerView.setAdapter(adapter);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(direct_contact_to_new_NGO.this, android.R.layout.simple_list_item_1,catList);
        dropcat.setAdapter(arrayAdapter);
        dropcat.setText("All",false);




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
                fetchNGOData(catList.get(position));
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

                Animatoo.INSTANCE.animateSlideRight(direct_contact_to_new_NGO.this);
            }
        });
    }

    //----------------------------------------------------------------------

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


    void fetchNGOData(String cat){

        dataList.clear();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ngo");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    for (DataSnapshot snapshot1 : snapshot.getChildren()){

                        NGO_Profile_Model data = snapshot1.getValue(NGO_Profile_Model.class);
                        String fname = data.getFname();
                        String address = data.getAddress();
                        String category=data.getType();
                        String mno=data.getMobile();
                        String email=data.getEmail();
                        String website=data.getWebsite();
                        String insta=data.getInstagram();
                        String linkedin=data.getLinkedin();
                        String facebook=data.getFacebook();
                        String twitter=data.getTwitter();
                        String youtube=data.getYoutube();

                        if(cat.equals(category))
                        {
                            NGOData data1 = new NGOData(fname,address,category,mno,email,website,insta,linkedin,facebook,twitter,youtube);
                            dataList.add(data1);
                        }

                        if("All".equals(cat))
                        {
                            NGOData data1 = new NGOData(fname,address,category,mno,email,website,insta,linkedin,facebook,twitter,youtube);
                            dataList.add(data1);
                        }

                        if(!catList.contains(category))
                        {
                            catList.add(category);
                        }
                    }
                    adapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                Toast.makeText(direct_contact_to_new_NGO.this, text, Toast.LENGTH_SHORT).show();
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(this);
        finish();
    }
}
