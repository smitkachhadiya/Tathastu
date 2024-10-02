package com.example.tathastu.NGO_Package.NGO_Profile;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class NGO_Social_Media extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    private ConnectivityReceiver connectivityReceiver;

    ExtendedFloatingActionButton  BTN_social_add;

    TextInputEditText txt_social_website,txt_social_insta,txt_social_linkedin,txt_social_fb,txt_social_twitter,txt_social_youtube;

    String website = "";
    String insta ="";
    String linkedin = "";
    String facebook = "";
    String twitter = "";
    String youtube = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_social_media);

        fetchProfileData();


        txt_social_website = findViewById(R.id.txt_social_website);
        txt_social_insta = findViewById(R.id.txt_social_insta);
        txt_social_linkedin = findViewById(R.id.txt_social_linkedin);
        txt_social_fb = findViewById(R.id.txt_social_fb);
        txt_social_twitter = findViewById(R.id.txt_social_twitter);
        txt_social_youtube = findViewById(R.id.txt_social_youtube);
        BTN_social_add = findViewById(R.id.BTN_social_add);


        FloatingActionButton BTN_back=findViewById(R.id.BTN_social_back);

        BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                finish(); Animatoo.INSTANCE.animateSlideRight(NGO_Social_Media.this);
            }
        });

        BTN_social_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                website=txt_social_website.getText().toString();
                insta=txt_social_insta.getText().toString();
                linkedin=txt_social_linkedin.getText().toString();
                facebook=txt_social_fb.getText().toString();
                twitter=txt_social_twitter.getText().toString();
                youtube=txt_social_youtube.getText().toString();
                if (website.isEmpty() && insta.isEmpty() && linkedin.isEmpty()
                        && facebook.isEmpty() && twitter.isEmpty() && youtube.isEmpty()) {
                    // No changes to update
                    Toast.makeText(NGO_Social_Media.this, "No changes to update.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Validate URLs
                if (!isValidNonEmptyUrl(website)) {
                    txt_social_website.setError("Invalid URL");
                    return;
                } else {
                    txt_social_website.setError(null);  // Clear the error if the URL is valid
                }

                if (!isValidNonEmptyUrl(insta)) {
                    txt_social_insta.setError("Invalid URL");
                    return;
                } else {
                    txt_social_insta.setError(null);
                }

                if (!isValidNonEmptyUrl(linkedin)) {
                    txt_social_linkedin.setError("Invalid URL");
                    return;
                } else {
                    txt_social_linkedin.setError(null);
                }

                if (!isValidNonEmptyUrl(facebook)) {
                    txt_social_fb.setError("Invalid URL");
                    return;
                } else {
                    txt_social_fb.setError(null);
                }

                if (!isValidNonEmptyUrl(twitter)) {
                    txt_social_twitter.setError("Invalid URL");
                    return;
                } else {
                    txt_social_twitter.setError(null);
                }

                if (!isValidNonEmptyUrl(youtube)) {
                    txt_social_youtube.setError("Invalid URL");
                    return;
                } else {
                    txt_social_youtube.setError(null);
                }
                SharedPreferences sharedPreferences1 = getSharedPreferences("NGO",MODE_PRIVATE);
                String ngoId = sharedPreferences1.getString("ngoId","");

                Map<String, Object> map = new HashMap<>();
                map.put("website", website);
                map.put("instagram", insta);
                map.put("linkedin", linkedin);
                map.put("facebook", facebook);
                map.put("twitter", twitter);
                map.put("youtube", youtube);

                FirebaseDatabase.getInstance().getReference().child("ngo").child(ngoId)
                        .updateChildren(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(NGO_Social_Media.this, "Social Media Updated Successfully !!", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(NGO_Social_Media.this, "Failed To Social Media !!", Toast.LENGTH_SHORT).show();
                                }

                                startActivity(new Intent(NGO_Social_Media.this, NGO_Profile_Screen.class));
                                finish();
                                Animatoo.INSTANCE.animateSlideLeft(NGO_Social_Media.this);
                            }
                        });
            }
        });

        // Initialize the ConnectivityReceiver
        connectivityReceiver = new ConnectivityReceiver();
        ConnectivityReceiver.connectivityReceiverListener = this;

        // Register the receiver to listen for connectivity changes
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


    }
    //--------------------------------------------------------------------------------------


    private void fetchProfileData() {
        SharedPreferences sharedPreferences1 = getSharedPreferences("NGO",MODE_PRIVATE);
        String ngoId = sharedPreferences1.getString("ngoId","");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ngo").child(ngoId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    NGO_Profile_Model data = snapshot.getValue(NGO_Profile_Model.class);
                    String website = data.getWebsite();
                    String insta = data.getInstagram();
                    String linkedin=data.getLinkedin();
                    String facebook=data.getFacebook();
                    String twitter=data.getTwitter();
                    String youtube=data.getYoutube();


                    txt_social_website.setText(website);
                    txt_social_insta.setText(insta);
                    txt_social_linkedin.setText(linkedin);
                    txt_social_fb.setText(facebook);
                    txt_social_twitter.setText(twitter);
                    txt_social_youtube.setText(youtube);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    // Validate a URL only if it's not empty
    private boolean isValidNonEmptyUrl(String url) {
        if (url.isEmpty()) {
            return true;  // Empty URL is considered valid
        }
        return Patterns.WEB_URL.matcher(url).matches();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(NGO_Social_Media.this);
        finish();
    }
    // Helper method to check if the internet connection is available
    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
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
        snackbarLayout.setPadding(1,1,1,1);
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