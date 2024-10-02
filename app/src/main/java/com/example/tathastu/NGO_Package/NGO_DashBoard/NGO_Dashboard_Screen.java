package com.example.tathastu.NGO_Package.NGO_DashBoard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.NGO_Package.NGO_Blood_Camp.NGO_BloodCamp_Selection;
import com.example.tathastu.NGO_Package.NGO_Campaign.NGO_Campaign;
import com.example.tathastu.NGO_Package.NGO_Education_Camp.NGO_Educamp_Selection;
import com.example.tathastu.NGO_Package.NGO_Entry.NGO_Login_Screen;
import com.example.tathastu.NGO_Package.NGO_Event.NGO_event_notify;
import com.example.tathastu.NGO_Package.NGO_Food_Camp.NGO_FoodCamp_Selection;
import com.example.tathastu.NGO_Package.NGO_History.NGO_History_Screen;
import com.example.tathastu.NGO_Package.NGO_Profile.NGO_Profile_Screen;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Common_Screens.About_us_Screen;
import com.example.tathastu.User_Package.user_Common_Screens.Contact_us_Screen;
import com.example.tathastu.User_Package.user_DashBoard.DashBoard_Screen;
import com.example.tathastu.User_Package.user_DashBoard.User_ngo_Selection_Screen;
import com.example.tathastu.User_Package.user_DashBoard.profile_getset;
import com.example.tathastu.User_Package.user_Entry.Login_Screen;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.example.tathastu.User_Package.user_HelpLine.Helpline_numbers_Screen;
import com.example.tathastu.User_Package.user_Quotes.AllQuotes_Screen;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NGO_Dashboard_Screen extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{


    ImageButton BTN_dash_food, BTN_dash_blood, BTN_dash_cloth, BTN_dash_edu, BTN_dash_aboutus, BTN_dash_contactus, BTN_dash_history, BTN_dash_helpline;
    MaterialTextView txt_dash_seeallquotes;
    CardView card_dash_directContact,card_dash_compaign,card_dash_event;
    private AppCompatTextView dash_quote;
    private int currentQuoteId = 1; // Initial quote ID
    private boolean isToastDisplayed = false; // Variable to track whether the toast has been displayed
    private static final int DAILY_QUOTE_REQUEST_CODE = 1001;
    private static final int MAX_REPEAT_COUNT = 500;
    private int repeatCount = 0;
    private Handler quoteHandler = new Handler(Looper.getMainLooper());
    private final int QUOTE_UPDATE_INTERVAL = 8 * 1000; // 20 seconds in milliseconds

    private ConnectivityReceiver connectivityReceiver;

    ShapeableImageView img_profile_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_dashboard_screen);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 100);
        }


        BTN_dash_food = findViewById(R.id.BTN_dash_food);
        BTN_dash_blood = findViewById(R.id.BTN_dash_blood);
        BTN_dash_edu = findViewById(R.id.BTN_dash_edu);
        BTN_dash_aboutus = findViewById(R.id.BTN_dash_aboutus);
        BTN_dash_contactus = findViewById(R.id.BTN_dash_contactus);
        BTN_dash_history = findViewById(R.id.BTN_dash_history);
        BTN_dash_helpline = findViewById(R.id.BTN_dash_helpline);
        card_dash_compaign=findViewById(R.id.card_dash_compaign);
        card_dash_event=findViewById(R.id.card_dash_event);
        FloatingActionButton BTN_dash_logout = findViewById(R.id.BTN_dash_logout);
        ImageButton BTN_dash_food = findViewById(R.id.BTN_dash_food);
        txt_dash_seeallquotes = findViewById(R.id.txt_dash_seeallquotes);
        card_dash_directContact = findViewById(R.id.card_dash_directcontact);

        img_profile_photo = findViewById(R.id.profile_icon);

        dash_quote = findViewById(R.id.dash_quote);
        dash_quote.setText("");

        SharedPreferences sharedPreferences1 = getSharedPreferences("NGO", MODE_PRIVATE);
        String ngoId = sharedPreferences1.getString("ngoId", "");

        DatabaseReference referenceprofile = FirebaseDatabase.getInstance().getReference("ngo");

        if (ngoId != null) {
            referenceprofile.child(ngoId).addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    profile_getset userpro = snapshot.getValue(profile_getset.class);
                    if (userpro != null) {
                        String profileImageUrl = userpro.getPhoto();

                        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                            // If a profile image is set, load it using Picasso
                            Picasso.get().load(profileImageUrl).into(img_profile_photo);
                        } else {
                            // If no profile image is set, use the default image
                            img_profile_photo.setImageResource(R.drawable.profile_img_icon_foreground);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error if needed
                }
            });
        } else {
            Toast.makeText(this, "Failed.", Toast.LENGTH_SHORT).show();
        }



// Initialize the ConnectivityReceiver
        connectivityReceiver = new ConnectivityReceiver();
        ConnectivityReceiver.connectivityReceiverListener = this;

        // Register the receiver to listen for connectivity changes
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


        //FOR FOOD DONATION
        BTN_dash_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NGO_Dashboard_Screen.this, NGO_FoodCamp_Selection.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(NGO_Dashboard_Screen.this);
            }
        });

        //FOR BLOOD DONATION
        BTN_dash_blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NGO_Dashboard_Screen.this, NGO_BloodCamp_Selection.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(NGO_Dashboard_Screen.this);
            }
        });
 //FOR Education DONATION
        BTN_dash_edu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NGO_Dashboard_Screen.this, NGO_Educamp_Selection.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(NGO_Dashboard_Screen.this);
            }
        });


        //FOR CONTACT US>>>
        BTN_dash_contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NGO_Dashboard_Screen.this, Contact_us_Screen.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(NGO_Dashboard_Screen.this);
            }
        });

        //FOR HISTORY
        BTN_dash_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NGO_Dashboard_Screen.this, NGO_History_Screen.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(NGO_Dashboard_Screen.this);
            }
        });

        //FOR ABOUT US >>>>
        BTN_dash_aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NGO_Dashboard_Screen.this, About_us_Screen.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(NGO_Dashboard_Screen.this);
            }
        });

        //FOR HELPLINE NUMBERS
        BTN_dash_helpline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NGO_Dashboard_Screen.this, Helpline_numbers_Screen.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(NGO_Dashboard_Screen.this);
            }
        });

        //CARDVIEW ----- DIRECT CONTACT TO NGO
        card_dash_directContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NGO_Dashboard_Screen.this, User_ngo_Selection_Screen.class);
                i.putExtra("source", "ngoDashboard");
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(NGO_Dashboard_Screen.this);
            }
        });

        //CARDVIEW ----- FUNDING CAMPAIGN
        card_dash_compaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NGO_Dashboard_Screen.this, NGO_Campaign.class);
                i.putExtra("source", "ngoDashboard");
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(NGO_Dashboard_Screen.this);
            }
        });
//CARDVIEW ----- EVENt
        card_dash_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NGO_Dashboard_Screen.this, NGO_event_notify.class);
                i.putExtra("source", "ngoDashboard");
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(NGO_Dashboard_Screen.this);
            }
        });

        //SEE ALL QUOTES
        txt_dash_seeallquotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NGO_Dashboard_Screen.this, AllQuotes_Screen.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(NGO_Dashboard_Screen.this);

            }
        });


        // LOGOUT BUTTON
        BTN_dash_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showlogoutDialog();
            }
        });

        // Initially fetch the quote when the activity starts
        new FetchQuoteTask().execute(currentQuoteId);

        // Schedule the task to fetch a new quote every 1 minute
        scheduleQuoteUpdate();


    }
//-------------------------------------------------------------------------------------------------------------------------



        //PROFILE METHOD
    public void onProfileClick(View view) {
        // Handle click on the profile icon

        Intent intent = new Intent(this, NGO_Profile_Screen.class);
        startActivity(intent);
        Animatoo.INSTANCE.animateSlideLeft(NGO_Dashboard_Screen.this);
    }
    private void showlogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.logout_exit_dialog, null);
        builder.setView(dialogView);

        ExtendedFloatingActionButton btnExitYes = dialogView.findViewById(R.id.BTN_exit_yes);
        ExtendedFloatingActionButton btnExitNo = dialogView.findViewById(R.id.BTN_exit_no);


        final AlertDialog dialog = builder.create();

        btnExitYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle 'Yes' button click

                SharedPreferences sharedPreferences = getSharedPreferences("NGOLogin", MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("hasLoggedIn", false).apply();
                sharedPreferences.edit().clear().apply();
                finish();
                Intent logout = new Intent(NGO_Dashboard_Screen.this, NGO_Login_Screen.class);
                startActivity(logout);
                Animatoo.INSTANCE.animateSlideRight(NGO_Dashboard_Screen.this);
            }
        });

        btnExitNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle 'No' button click
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false); // Prevent dismiss on outside touch
        dialog.show();
    }
    //SCHEDULE THE QUOTES
    private void scheduleQuoteUpdate() {
        quoteHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Increment repeat count and reset to 1 if it reaches the maximum
                repeatCount = (repeatCount % MAX_REPEAT_COUNT) + 1;

                // Update the quote
                new FetchQuoteTask().execute(currentQuoteId);

                // Schedule the task to run again after 1 minute
                quoteHandler.postDelayed(this, QUOTE_UPDATE_INTERVAL);
            }
        }, QUOTE_UPDATE_INTERVAL);
    }


    private class FetchQuoteTask extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... quoteIds) {
            // Adjust quoteId based on repeat count
            if (isOnline()) {
                // If there is an active internet connection, make the API request
                int adjustedQuoteId = (currentQuoteId + repeatCount - 1) % MAX_REPEAT_COUNT + 1;
                return fetchQuote(adjustedQuoteId);
            } else {
                // If there is no internet connection, return an error message
                return "No internet connection";
            }

        }

        @Override
        protected void onPostExecute(String result) {
            // Update the UI with the new quote or handle the error message
            if (result != null) {
                if (result.equals("No internet connection") && !isToastDisplayed) {
                    // Handle the case when there is no internet connection
                    // Display a Toast message to the user
                    showToast("Please check internet connection!");
                    isToastDisplayed = true; // Set the flag to true to indicate the Toast has been displayed
                } else if (!result.equals("No internet connection")) {
                    // Apply slide-out animation to hide the current quote
                    Animation slideOutAnimation = AnimationUtils.loadAnimation(NGO_Dashboard_Screen.this, R.anim.slide_out_left);
                    slideOutAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            // This method is called when the slide out animation starts
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            // This method is called when the slide out animation ends
                            // Update the UI with the new quote
                            dash_quote.setText(result);

                            // Apply slide-in animation to show the new quote
                            Animation slideInAnimation = AnimationUtils.loadAnimation(NGO_Dashboard_Screen.this, R.anim.slide_in_right);
                            dash_quote.startAnimation(slideInAnimation);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                            // This method is called if the animation repeats
                        }
                    });

                    dash_quote.startAnimation(slideOutAnimation);
                    isToastDisplayed = false; // Reset the flag when a new quote is displayed
                }
            }
        }

        // Method to check if the device is online
        private boolean isOnline() {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
    }
    // ON BACK PRESSED
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    // Show exit confirmation dialog
    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.exit_dialog, null);
        builder.setView(dialogView);

        ExtendedFloatingActionButton btnExitYes = dialogView.findViewById(R.id.BTN_exit_yes);
        ExtendedFloatingActionButton btnExitNo = dialogView.findViewById(R.id.BTN_exit_no);


        final AlertDialog dialog = builder.create();

        btnExitYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle 'Yes' button click
                finishAffinity(); // Finish this activity and all activities immediately below it
                System.exit(0); // Exit the app entirely
                dialog.dismiss();
            }
        });

        btnExitNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle 'No' button click
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false); // Prevent dismiss on outside touch
        dialog.show();
    }

    // FETCH QUOTES

    private String fetchQuote(int quoteId) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String quoteJsonString = null;

        try {
            // Specify the URL of your API with the quote ID
            URL url = new URL("https://meetk1803.github.io/tathastu_quotes_api/tathastu_quotes.json");

            // Open the connection to the URL
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder builder = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            if (builder.length() == 0) {
                return null;
            }
            quoteJsonString = builder.toString();
        } catch (IOException e) {
            // Handle IOException
            e.printStackTrace();
        } finally {
            // Close the HttpURLConnection and BufferedReader
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Check if the JSON response is null or empty
        if (quoteJsonString != null && !quoteJsonString.isEmpty()) {
            try {
                // Parse the JSON array and extract the quote
                JSONArray quotesArray = new JSONArray(quoteJsonString);
                if (quoteId <= quotesArray.length()) {
                    JSONObject quoteObject = quotesArray.getJSONObject(quoteId - 1);
                    return quoteObject.getString("quote");
                }
            } catch (JSONException e) {
                // Handle JSONException
                e.printStackTrace();
            }
        }

        return null;
    }


    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showSnackbar(findViewById(android.R.id.content),text);
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
