package com.example.tathastu.User_Package.user_Common_Screens;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tathastu.NGO_Package.NGO_DashBoard.NGO_Dashboard_Screen;
import com.example.tathastu.NGO_Package.NGO_Entry.NGO_OTP_Screen;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class FeedBack_Screen extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private ConnectivityReceiver connectivityReceiver;

    TextInputEditText name,email,feedback;
    ExtendedFloatingActionButton btnfeedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_screen);

        name=findViewById(R.id.txt_feedback_name);
        email=findViewById(R.id.txt_feedback_Email);
        feedback=findViewById(R.id.txt_feedback);
        btnfeedback=findViewById(R.id.BTN_feedback);

        btnfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().isEmpty() || email.getText().toString().isEmpty() || feedback.getText().toString().isEmpty())
                {
                    showSnackbar(findViewById(android.R.id.content), "Please enter required details...");
                }else if(!isValidEmail(email.getText().toString()))
                {
                    showSnackbar(findViewById(android.R.id.content), "Please enter a valid email address (Gmail, Yahoo, or Outlook)...");
                }else{
                    String name1 = name.getText().toString().trim();
                    String email1 = email.getText().toString().trim();
                    String feedback1 = feedback.getText().toString().trim();

                    Map<String, Object> map = new HashMap<>();
                    map.put("name", name1);
                    map.put("email", email1);
                    map.put("feedback", feedback1);

                    String feedbackId = String.valueOf(System.currentTimeMillis());

                    FirebaseDatabase.getInstance().getReference().child("feedback").child(feedbackId)
                            .setValue(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    showSnackbar(findViewById(android.R.id.content), "Feedback Submitted Successfully.");
                                   finish();

                                    sendFeedbackEmail(name1,email1);

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    showSnackbar(findViewById(android.R.id.content), "Error: While Inserting Data.");

                                }
                            });


                }
            }
        });

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
                String source = getIntent().getStringExtra("source");
                if ("about".equals(source)) {
                    // If source is login, go back to LoginActivity
//                    Intent intent = new Intent(FeedBack_Screen.this, About_us_Screen.class);
//                    startActivity(intent);
                    finish();
                } else if ("contact".equals(source)) {
                    // If source is signin, go back to SignInActivity
//                    Intent intent = new Intent(FeedBack_Screen.this, Contact_us_Screen.class);
//                    startActivity(intent);
                    finish();
                } else {
                    // Default behavior (handle appropriately)
                    onBackPressed();
                    finish();
                }
            }
        });
    }

    private void sendFeedbackEmail(String name1, String email1) {
        try {
            String senderEmail = "tathastu052threesofficial@gmail.com";
            String password = "jwhqpkbuqwmkirwy";

            String loginMessage = "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                    "    <tr>\n" +
                    "        <td align=\"center\">\n" +
                    "            <table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: collapse;\">\n" +
                    "                <tr>\n" +
                    "                    <td bgcolor=\"#2E80DF\" style=\"padding: 1px; text-align: center;\">\n" +
                    "                        <h2 style=\"color: #ffffff;\">Thank You for Your Feedback!</h2>\n" +
                    "                    </td>\n" +
                    "                </tr>\n" +
                    "                <tr>\n" +
                    "                    <td bgcolor=\"#ffffff\" style=\"padding: 10px; color: black;\">\n" +
                    "                        <p>Hello " + name1 + ",</p>\n" +
                    "                        <p>Thank you for taking the time to share your feedback with us. Your input is invaluable and helps us improve our services.</p>\n" +
                    "                        <p>We appreciate your dedication to making Tathastu - The Donation App even better. Our team is committed to addressing your suggestions and concerns to enhance your experience further.</p>\n" +
                    "                        <p>If you have any additional comments or ideas, feel free to reach out. We value your opinion and strive to provide the best possible experience for our users.</p>\n" +
                    "                        <p>Thank you once again for being an essential part of Tathastu - The Donation App!</p>\n" +
                    "                        <p style=\"text-align: center; margin: 20px 0;\">Stay connected with us:</p>\n" +
                    "                        <p style=\"text-align: center;\">\n" +
                    "                            <a href=\"https://www.instagram.com/tathastu.g052?igsh=OGQ5ZDc2ODk2ZA==\" style=\"margin-right: 20px;\">\n" +
                    "                                <img src=\"https://freelogopng.com/images/all_img/1658588965instagram-logo-png-transparent-background.png\" alt=\"Instagram\" style=\"width: 30px; height: 30px;\">\n" +
                    "                            </a>\n" +
                    "                            <a href=\"https://www.facebook.com/tathastu.g052?mibextid=ZbWKwL\" style=\"margin-right: 20px;\">\n" +
                    "                                <img src=\"https://freelogopng.com/images/all_img/1658030214facebook-logo-hd.png\" alt=\"Facebook\" style=\"width: 30px; height: 30px;\">\n" +
                    "                            </a>\n" +
                    "                            <a href=\"https://twitter.com/tathastu_g052?t=X-SvynEa7on0GDAirv3UsQ&s=09\">\n" +
                    "                                <img src=\"https://freelogopng.com/images/all_img/1657045399twitter-icon-png.png\" alt=\"Twitter\" style=\"width: 30px; height: 30px;\">\n" +
                    "                            </a>\n" +
                    "                        </p>\n" +
                    "                        <p>Best regards,</p>\n" +
                    "                        <p>The Tathastu - The Donation App Team</p>\n" +
                    "                    </td>\n" +
                    "                </tr>\n" +
                    "                <tr>\n" +
                    "                    <td bgcolor=\"#2E80DF\" style=\"padding: 5px; text-align: center;\">\n" +
                    "                        <p style=\"color: #ffffff;\">We Value Your Feedback!</p>\n" +
                    "                    </td>\n" +
                    "                </tr>\n" +
                    "            </table>\n" +
                    "        </td>\n" +
                    "    </tr>\n" +
                    "</table>";


            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, password);
                }
            });

            // Creating a MimeMessage
            MimeMessage mimeMessage = new MimeMessage(session);

            // Setting the sender's name and email address
            InternetAddress senderAddress = new InternetAddress(senderEmail, "Tathastu - The Donation App");
            mimeMessage.setFrom(senderAddress);

            // Adding the recipient's email address
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email1));

            // Setting the subject and message content as HTML
            mimeMessage.setSubject("Feedback Mail !!");
            mimeMessage.setContent(loginMessage, "text/html");

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                    } catch (MessagingException e) {
                        // Handling messaging exception
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(FeedBack_Screen.this, "Error Occurred : ", Toast.LENGTH_SHORT).show();
                            }
                        });
                        e.printStackTrace();
                    }
                }
            });
            t.start();

        }

        catch (AddressException e) {
            // Handling address exception
            Toast.makeText(FeedBack_Screen.this, "Error Occurred : " + e, Toast.LENGTH_SHORT).show();
        }

        catch (MessagingException e) {
            // Handling messaging exception (e.g. network error)
            Toast.makeText(FeedBack_Screen.this, "Error Occurred : " + e, Toast.LENGTH_SHORT).show();
        }

        catch (UnsupportedEncodingException e) {
            Toast.makeText(FeedBack_Screen.this, "Error Occurred : " + e, Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(FeedBack_Screen.this, "Feedback Mail Sent Successfully.", Toast.LENGTH_SHORT).show();
    }
//-------------------------------------------------------------------------------------------------------

    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-z0-9._%+-]+@(gmail\\.com|yahoo\\.com|outlook\\.com)$";
        return email.matches(emailPattern);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unregister the receiver to avoid memory leaks
        unregisterReceiver(connectivityReceiver);
    }
    // Helper method to check if the device is connected to the internet
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
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