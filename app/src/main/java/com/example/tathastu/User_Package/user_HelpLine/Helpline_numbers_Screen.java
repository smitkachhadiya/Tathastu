
package com.example.tathastu.User_Package.user_HelpLine;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Helpline_numbers_Screen extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private RecyclerView recyclerView;
    private UserAdapter_Helpline_Numbers helplineAdapter;
    private List<UserModel_Helpline_Numbers> helplineList;
    private ConnectivityReceiver connectivityReceiver;

    private  List<String> catList;

    MaterialAutoCompleteTextView dropcat;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpline_numbers_screen);

        recyclerView = findViewById(R.id.recycle_helpline_numbers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        helplineList = new ArrayList<>();
        helplineAdapter = new UserAdapter_Helpline_Numbers(helplineList, this);
        recyclerView.setAdapter(helplineAdapter);

        dropcat=findViewById(R.id.dropcat);
        catList = new ArrayList<>();
        catList.add("All");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(Helpline_numbers_Screen.this, android.R.layout.simple_list_item_1,catList);
        dropcat.setAdapter(arrayAdapter);
        dropcat.setText("All",false);

        fetchJsonData("All");

        dropcat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fetchJsonData(catList.get(position));
                hideSoftKeyboard(dropcat);
            }
        });

        // Fetch helpline numbers from the API
//        new FetchHelplineNumbersTask().execute("https://meetk1803.github.io/tathastu_quotes_api/tathastu_helpline_numbers.json");

        // Initialize the ConnectivityReceiver
        connectivityReceiver = new ConnectivityReceiver();
        ConnectivityReceiver.connectivityReceiverListener = this;

        // Register the receiver to listen for connectivity changes
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        FloatingActionButton BTN_back=findViewById(R.id.BTN_back);
        //BACK
        BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

                Animatoo.INSTANCE.animateSlideRight(Helpline_numbers_Screen.this);
            }
        });
    }

//--------------------------------------------------------------------
@Override
public void onBackPressed() {
    super.onBackPressed();
    Animatoo.INSTANCE.animateSlideRight(this);
    finish();
}
    private void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    void fetchJsonData(String cat){

        helplineList.clear();

        requestQueue = Volley.newRequestQueue(this);

        // URL of the JSON file on the server
        String url = "https://meetk1803.github.io/tathastu_quotes_api/tathastu_helpline_numbers.json";

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

                                String name = jsonObject.getString("name");
                                String number = jsonObject.getString("number");
                                String category = jsonObject.getString("category");


                                if(cat.equals("All")) {
                                    UserModel_Helpline_Numbers helpline = new UserModel_Helpline_Numbers(name, number, category);
                                    helplineList.add(helpline);
                                }else if(category.equals(cat)){
                                    UserModel_Helpline_Numbers helpline = new UserModel_Helpline_Numbers(name, number, category);
                                    helplineList.add(helpline);
                                }

                                if(!catList.contains(category))
                                {
                                    catList.add(category);
                                }
                            }
                            helplineAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Toast.makeText(Helpline_numbers_Screen.this, "Error to Fetch Data!!", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the request queue
        requestQueue.add(jsonArrayRequest);
    }

//    private void copyToClipboard(String phoneNumber) {
//        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//        ClipData clip = ClipData.newPlainText("Phone Number", phoneNumber);
//        if (clipboard != null) {
//            clipboard.setPrimaryClip(clip);
//            showToast("Phone number copied to clipboard");
//        }
//    }
//    private class FetchHelplineNumbersTask extends AsyncTask<String, Void, String> {
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
//                parseHelplineNumbers(result);
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
//
//
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
//
//    private void parseHelplineNumbers(String jsonData) {
//        try {
//            JSONArray jsonArray = new JSONArray(jsonData);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                String name = jsonObject.getString("name");
//                String number = jsonObject.getString("number");
//                String category = jsonObject.getString("category");
//
//                UserModel_Helpline_Numbers helpline = new UserModel_Helpline_Numbers(name, number, category);
//                helplineList.add(helpline);
//            }
//
//            // Notify the adapter that the data has changed
//            helplineAdapter.notifyDataSetChanged();
//
//            // Show a toast indicating successful data fetching
//            showToast("Helpline numbers loaded successfully");
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Helpline_numbers_Screen.this, text, Toast.LENGTH_SHORT).show();
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
