package com.example.tathastu.NGO_Package.NGO_Profile;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.example.tathastu.NGO_Package.NGO_Reset_PWD.NGO_Reset_Pwd_Screen;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class NGO_Update_Profile extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    //ALL
    private TextInputEditText txt_updatep_Fname_ngo, txt_updatep_email_ngo, txt_updatep_mno_ngo, txt_updatep_address_ngo, txt_updatep_pwd_ngo;

    private MaterialAutoCompleteTextView txt_type_ngo;

    // INTERNET
    private ConnectivityReceiver connectivityReceiver;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 300;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int REQUEST_IMAGE_CROP = 3;
    private LinearLayout update_parentLayout;


    //PROFILE IMAGE
    int SELECT_PICTURE = 100;
    ShapeableImageView img_profile_photo;

    String photo = "";

    Uri imageUri;

    MaterialTextView txt_updatep_change;

    //EDIT PROFILE
    private ExtendedFloatingActionButton BTN_Profile_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_update_profile);

        fetchProfileData();

        img_profile_photo = findViewById(R.id.img_updatep_photo);
        txt_updatep_Fname_ngo = findViewById(R.id.txt_updatep_Fname_ngo);
        txt_updatep_email_ngo = findViewById(R.id.txt_updatep_email_ngo);
        txt_updatep_mno_ngo = findViewById(R.id.txt_updatep_mno_ngo);
        txt_updatep_address_ngo = findViewById(R.id.txt_updatep_address_ngo);
        txt_type_ngo = findViewById(R.id.txt_type_ngo);
        txt_updatep_pwd_ngo = findViewById(R.id.txt_updatep_pwd_ngo);
        BTN_Profile_update = findViewById(R.id.BTN_updatep_update);
        txt_updatep_change = findViewById(R.id.txt_updatep_change);

        update_parentLayout = findViewById(R.id.update_parentLayout);

        // Define the blood groups array within the same class
        String[] bloodGroups = new String[]{
                "All", "Food", "Blood", "Animal Welfare", "Cloth", "Woman Development", "Education"
        };

        // Create an ArrayAdapter using the blood groups array and a default layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                bloodGroups
        );

        // Set the adapter to the AutoCompleteTextView
        txt_type_ngo.setAdapter(adapter);


        // Set up touch listener for the parent layout
        update_parentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Clear focus from EditText when touched outside
                img_profile_photo.clearFocus();
                txt_updatep_Fname_ngo.clearFocus();
                txt_updatep_email_ngo.clearFocus();
                txt_updatep_mno_ngo.clearFocus();
                txt_updatep_address_ngo.clearFocus();
                txt_type_ngo.clearFocus();
                txt_updatep_pwd_ngo.clearFocus();

                hideSoftKeyboard(update_parentLayout);
                return false;
            }
        });

        txt_updatep_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, SELECT_PICTURE);
            }
        });


        // Initialize the ConnectivityReceiver
        connectivityReceiver = new ConnectivityReceiver();
        ConnectivityReceiver.connectivityReceiverListener = this;

        // Register the receiver to listen for connectivity changes
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        FloatingActionButton BTN_back = findViewById(R.id.BTN_back);
        //BACK
        BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExitDialog();

            }
        });

        //UPDATE BTN
        BTN_Profile_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fname = txt_updatep_Fname_ngo.getText().toString();
                String email = txt_updatep_email_ngo.getText().toString();
                String mno = txt_updatep_mno_ngo.getText().toString();
                String address = txt_updatep_address_ngo.getText().toString();
                String type = txt_type_ngo.getText().toString();
                String pwd = txt_updatep_pwd_ngo.getText().toString();
                if (!isInternetAvailable()) {
                    showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
                    return;
                } else {
                    if (fname.isEmpty() || email.isEmpty() || mno.isEmpty() || address.isEmpty() || type.isEmpty() || pwd.isEmpty()) {
                        showSnackbar(findViewById(android.R.id.content), "Please enter required details...");
                    } else if (mno.length() < 13) {
                        showSnackbar(findViewById(android.R.id.content), "Please enter a valid mobile number...");
                    } else if (!isValidEmail(email)) {
                        showSnackbar(findViewById(android.R.id.content), "Please enter a valid email address (Gmail, Yahoo, or Outlook)...");
                    } else if (pwd.length() < 8 ) {
                        showSnackbar(findViewById(android.R.id.content), "Password should be 8 characters long...");
                    } else if (!isValidPassword(pwd)) {
                        showSnackbar(findViewById(android.R.id.content), "Password must include at least one uppercase letter, one lowercase letter, one special character, and one digit...");
                    }else {

                        final ProgressDialog p = new ProgressDialog(new ContextThemeWrapper(NGO_Update_Profile.this, R.style.CustomProgressDialog));
                        p.setTitle("Updating.....");
                        p.show();

                        if (imageUri != null) {

                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference imageRef = storage.getReference().child("images/" + System.currentTimeMillis() + ".jpg");
                            imageRef.putFile(imageUri)
                                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(NGO_Update_Profile.this, "Images Upload Successfully.", Toast.LENGTH_SHORT).show();

                                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {


                                                        SharedPreferences sharedPreferences1 = getSharedPreferences("NGO", MODE_PRIVATE);
                                                        String ngoId = sharedPreferences1.getString("ngoId", "");

                                                        Map<String, Object> map = new HashMap<>();
                                                        map.put("fname", fname);
                                                        map.put("email", email);
                                                        map.put("mobile", mno);
                                                        map.put("type", type);
                                                        map.put("address", address);
                                                        map.put("password", pwd);
                                                        map.put("photo", uri.toString());

                                                        FirebaseDatabase.getInstance().getReference().child("ngo").child(ngoId)
                                                                .updateChildren(map)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Toast.makeText(NGO_Update_Profile.this, "Profile Updated Successfully !!", Toast.LENGTH_SHORT).show();
                                                                        } else {
                                                                            Toast.makeText(NGO_Update_Profile.this, "Failed To Update Profile !!", Toast.LENGTH_SHORT).show();
                                                                        }

                                                                        //startActivity(new Intent(NGO_Update_Profile.this, NGO_Profile_Screen.class));
                                                                        finish();
                                                                        Animatoo.INSTANCE.animateSlideRight(NGO_Update_Profile.this);

                                                                    }
                                                                });
                                                    }

                                                });
                                            }
                                        }
                                    });
                        } else {
                            SharedPreferences sharedPreferences1 = getSharedPreferences("NGO", MODE_PRIVATE);
                            String ngoId = sharedPreferences1.getString("ngoId", "");

                            Map<String, Object> map = new HashMap<>();
                            map.put("fname", fname);
                            map.put("email", email);
                            map.put("mobile", mno);
                            map.put("type", type);
                            map.put("address", address);
                            map.put("password", pwd);

                            FirebaseDatabase.getInstance().getReference().child("ngo").child(ngoId)
                                    .updateChildren(map)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(NGO_Update_Profile.this, "Profile Updated Successfully !!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(NGO_Update_Profile.this, "Failed To Update Profile !!", Toast.LENGTH_SHORT).show();
                                            }

                                            startActivity(new Intent(NGO_Update_Profile.this, NGO_Profile_Screen.class));
                                            finish();
                                            Animatoo.INSTANCE.animateSlideRight(NGO_Update_Profile.this);
                                        }
                                    });
                        }
                    }
                }
            }
        });
    }
    //------------------------------------------------------------------------------------------------------------

    public void reset_pwd_screen(View v){
        startActivity(new Intent(NGO_Update_Profile.this, NGO_Reset_Pwd_Screen.class));
        Animatoo.INSTANCE.animateSlideLeft(NGO_Update_Profile.this);
    }

    private void fetchProfileData() {
        SharedPreferences sharedPreferences1 = getSharedPreferences("NGO", MODE_PRIVATE);
        String ngoId = sharedPreferences1.getString("ngoId", "");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ngo").child(ngoId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    NGO_Profile_Model data = snapshot.getValue(NGO_Profile_Model.class);
                    String fname = data.getFname();
                    String email = data.getEmail();
                    String mno = data.getMobile();
                    String address = data.getAddress();
                    String type = data.getType();
                    String pwd = data.getPassword();
                    photo = data.getPhoto();

                    txt_updatep_Fname_ngo.setText(fname);
                    txt_updatep_email_ngo.setText(email);
                    txt_updatep_mno_ngo.setText(mno);
                    txt_updatep_address_ngo.setText(address);
                    txt_type_ngo.setText(type, false);
                    txt_updatep_pwd_ngo.setText(pwd);

                    // Check if photo is not null or empty
                    if (photo != null && !photo.isEmpty()) {
                        // Load the profile photo using Glide
                        Glide.with(NGO_Update_Profile.this)
                                .load(photo)
                                .centerCrop()
                                .into(img_profile_photo);
                    } else {
                        // Set a default image when no image is found or not set
                        img_profile_photo.setImageResource(R.drawable.profile_img_icon_foreground);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //FOR IMAGE PICKER
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE || requestCode == REQUEST_IMAGE_CAPTURE) {
                if (data != null) {
                    // Start cropping activity with the selected image
                    imageUri = data.getData();
                    startCropActivity(imageUri);
                } else {
                    Toast.makeText(this, "No Image Selected.", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == REQUEST_IMAGE_CROP) {
                if (data != null) {
                    // Handle the cropped image result
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap croppedBitmap = extras.getParcelable("data");
                        img_profile_photo.setImageBitmap(croppedBitmap);
                    } else {
                        Toast.makeText(this, "Error cropping image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void startCropActivity(Uri sourceUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(sourceUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Start cropping activity with the original image
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(sourceUri, "image/*");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 200);
            cropIntent.putExtra("outputY", 200);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, REQUEST_IMAGE_CROP);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        showExitDialog();

    }


    // showExitDialog
    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.exit_dialog_update, null);
        builder.setView(dialogView);

        ExtendedFloatingActionButton btnExitYes = dialogView.findViewById(R.id.BTN_exit_yes);
        ExtendedFloatingActionButton btnExitNo = dialogView.findViewById(R.id.BTN_exit_no);


        final AlertDialog dialog = builder.create();

        btnExitYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle 'Yes' button click

                finish();
                Animatoo.INSTANCE.animateSlideRight(NGO_Update_Profile.this);
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


    //HIDE THE KEYBOARD
    private void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // Helper method to validate Gmail, Yahoo, and Outlook addresses
    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-z0-9._%+-]+@(gmail\\.com|yahoo\\.com|outlook\\.com)$";
        return email.matches(emailPattern);
    }

    // Helper method to validate password
    private boolean isValidPassword(String password) {
        // Password should have minimum 8 characters, 1 uppercase, 1 special character, 1 lowercase, and 1 number
        String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()-=_+{};:'\\\"<>,.?/\\\\|]).{8,}$";
        return password.matches(passwordPattern);
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
