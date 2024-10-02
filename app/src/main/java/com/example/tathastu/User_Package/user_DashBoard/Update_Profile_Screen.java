package com.example.tathastu.User_Package.user_DashBoard;

import static com.example.tathastu.R.style.CustomDatePickerStyle;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.Reset_PWD.Reset_Pwd_Screen;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Update_Profile_Screen extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, ConnectivityReceiver.ConnectivityReceiverListener {
    //ALL
    private TextInputEditText txt_Profile_Fname, txt_Profile_Lname, txt_Profile_email, txt_Profile_mno, txt_Profile_dob, txt_Profile_pwd;

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

    MaterialTextView txt_updatep_change;

    //EDIT PROFILE
    private ExtendedFloatingActionButton BTN_Profile_update;
    //DATE OF BIRTH
    private int minYear, maxYear;
    private SimpleDateFormat dateFormatter;

    String photo="";
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_screen);

        fetchProfileData();

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        img_profile_photo = findViewById(R.id.img_updatep_photo);
        txt_Profile_Fname = findViewById(R.id.txt_updatep_Fname);
        txt_Profile_Lname = findViewById(R.id.txt_updatep_Lname);
        txt_Profile_email = findViewById(R.id.txt_updatep_email);
        txt_Profile_mno = findViewById(R.id.txt_updatep_mno);
        txt_Profile_dob = findViewById(R.id.txt_updatep_dob);
        txt_Profile_pwd = findViewById(R.id.txt_updatep_pwd);
        BTN_Profile_update = findViewById(R.id.BTN_updatep_update);

        txt_updatep_change = findViewById(R.id.txt_updatep_change);

        update_parentLayout = findViewById(R.id.update_parentLayout);

        // Set up touch listener for the parent layout
        update_parentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Clear focus from EditText when touched outside
                img_profile_photo.clearFocus();
                txt_Profile_Fname.clearFocus();
                txt_Profile_Lname.clearFocus();
                txt_Profile_email.clearFocus();
                txt_Profile_mno.clearFocus();
                txt_Profile_dob.clearFocus();
                txt_Profile_pwd.clearFocus();

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

        setOnClickListeners();

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

                String fname = txt_Profile_Fname.getText().toString();
                String lname = txt_Profile_Lname.getText().toString();
                String email = txt_Profile_email.getText().toString();
                String mob = txt_Profile_mno.getText().toString();
                String dob1 = txt_Profile_dob.getText().toString();
                String pwd = txt_Profile_pwd.getText().toString();
                if (!isInternetAvailable()) {
                    showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
                    return;
                } else {
                    if (fname.isEmpty() || lname.isEmpty() || email.isEmpty() || mob.isEmpty() || dob1.isEmpty() || pwd.isEmpty() ) {
                        showSnackbar(findViewById(android.R.id.content), "Please enter required details...");
                    } else if (mob.length() < 10) {
                        showSnackbar(findViewById(android.R.id.content), "Please enter a valid mobile number...");
                    } else if (!isValidEmail(email)) {
                        showSnackbar(findViewById(android.R.id.content), "Please enter a valid email address (Gmail, Yahoo, or Outlook)...");
                    } else if (pwd.length() < 8 ) {
                        showSnackbar(findViewById(android.R.id.content), "Password should be 8 characters long...");
                    } else if (!isValidPassword(pwd)) {
                        showSnackbar(findViewById(android.R.id.content), "Password must include at least one uppercase letter, one lowercase letter, one special character, and one digit...");
                    }  else {

                        final ProgressDialog p = new ProgressDialog(new ContextThemeWrapper(Update_Profile_Screen.this, R.style.CustomProgressDialog));
                        p.setTitle("Updating.....");
                        p.show();

                        if (imageUri != null) {

                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference imageRef = storage.getReference().child("images/" +  System.currentTimeMillis() + ".jpg");
                            imageRef.putFile(imageUri)
                                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                // Image upload successful
                                                // You can get the download URL from task.getResult().getDownloadUrl()
                                                // and use it to display or further process the uploaded image
                                                // For Firebase SDK version 16.0.0 and later, use task.getResult().getMetadata().getReference().getDownloadUrl()
                                                // For example: Uri downloadUrl = task.getResult().getMetadata().getReference().getDownloadUrl();

                                                Toast.makeText(Update_Profile_Screen.this, "Images Upload Successfully.", Toast.LENGTH_SHORT).show();

                                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {


                                                        SharedPreferences sharedPreferences1 = getSharedPreferences("USER", MODE_PRIVATE);
                                                        String userId = sharedPreferences1.getString("userId", "");


                                                        Map<String, Object> map = new HashMap<>();
                                                        map.put("photo",uri.toString());
                                                        map.put("fname", fname);
                                                        map.put("lname", lname);
                                                        map.put("email", email);
                                                        map.put("dob", dob1);
                                                        map.put("password", pwd);

                                                        FirebaseDatabase.getInstance().getReference().child("user").child(userId)
                                                                .updateChildren(map)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Toast.makeText(Update_Profile_Screen.this, "Profile Updated Successfully !!", Toast.LENGTH_SHORT).show();
                                                                        } else {
                                                                            Toast.makeText(Update_Profile_Screen.this, "Failed To Update Profile !!", Toast.LENGTH_SHORT).show();
                                                                        }

                                                                       // startActivity(new Intent(Update_Profile_Screen.this, Profile_Screen.class));
                                                                        finish();
                                                                    }
                                                                });

                                                    }
                                                });

                                            }
                                        }
                                    });
                        } else {

                            SharedPreferences sharedPreferences1 = getSharedPreferences("USER", MODE_PRIVATE);
                            String userId = sharedPreferences1.getString("userId", "");

                            Map<String, Object> map = new HashMap<>();
                            map.put("fname", fname);
                            map.put("lname", lname);
                            map.put("email", email);
                            map.put("dob", dob1);
                            map.put("password", pwd);

                            FirebaseDatabase.getInstance().getReference().child("user").child(userId)
                                    .updateChildren(map)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Update_Profile_Screen.this, "Profile Updated Successfully !!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(Update_Profile_Screen.this, "Failed To Update Profile !!", Toast.LENGTH_SHORT).show();
                                            }

                                            startActivity(new Intent(Update_Profile_Screen.this, Profile_Screen.class));
                                            finish();
                                            Animatoo.INSTANCE.animateSlideRight(Update_Profile_Screen.this);
                                        }
                                    });
                        }
                    }
                }
            }
        });


    }

    private void fetchProfileData() {
        SharedPreferences sharedPreferences1 = getSharedPreferences("USER", MODE_PRIVATE);
        String userId = sharedPreferences1.getString("userId", "");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    profile_getset data = snapshot.getValue(profile_getset.class);
                    String fname = data.getFname();
                    String lname = data.getLname();
                    String email = data.getEmail();
                    String mno=data.getMobile();
                    String dob=data.getDob();
                    String pwd = data.getPassword();
                    photo = data.getPhoto();


                    txt_Profile_Fname.setText(fname);
                    txt_Profile_Lname.setText(lname);
                    txt_Profile_email.setText(email);
                    txt_Profile_mno.setText(mno);
                    txt_Profile_dob.setText(dob);
                    txt_Profile_pwd.setText(pwd);

                    // Check if photo is not null or empty
                    if (photo != null && !photo.isEmpty()) {
                        // Load the profile photo using Glide
                        Glide.with(Update_Profile_Screen.this)
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

//-------------------------------------------------------------------------------------------------------------


    public void reset_pwd_screen(View v){
        Intent intent = new Intent(Update_Profile_Screen.this, Reset_Pwd_Screen.class);
        startActivity(intent);
        Animatoo.INSTANCE.animateSlideLeft(Update_Profile_Screen.this);
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


    //FOR DOB
    private void setOnClickListeners() {
        txt_Profile_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(txt_Profile_dob);
                showDatePicker();

            }
        });


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
                Animatoo.INSTANCE.animateSlideRight(Update_Profile_Screen.this);
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

    private void showDatePicker() {
        Calendar newCalendar = Calendar.getInstance();
        int year = newCalendar.get(Calendar.YEAR);
        int month = newCalendar.get(Calendar.MONTH);
        int dayOfMonth = newCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Update_Profile_Screen.this,
                CustomDatePickerStyle,
                Update_Profile_Screen.this,
                year, month, dayOfMonth
        );
        minYear = 1950;
        maxYear = Calendar.getInstance().get(Calendar.YEAR) - 18;

        DatePicker datePicker = datePickerDialog.getDatePicker();
        if (datePicker != null) {
            datePicker.setMinDate(getMinDate());
            datePicker.setMaxDate(getMaxDate());
        }
        datePickerDialog.show();
    }

    public long getMinDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(minYear, 0, 1);
        return calendar.getTimeInMillis();
    }

    public long getMaxDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(maxYear, 11, 31);
        return calendar.getTimeInMillis();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        Calendar newDate = Calendar.getInstance();
        newDate.set(year, monthOfYear, dayOfMonth);
        txt_Profile_dob.setText(dateFormatter.format(newDate.getTime()));
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