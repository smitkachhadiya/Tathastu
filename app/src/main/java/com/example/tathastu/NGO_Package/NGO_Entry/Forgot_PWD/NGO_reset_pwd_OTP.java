package com.example.tathastu.NGO_Package.NGO_Entry.Forgot_PWD;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.NGO_Package.NGO_Entry.NGO_Login_Screen;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class NGO_reset_pwd_OTP extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    TextInputEditText forgot_OTP;
    ExtendedFloatingActionButton forgot_submit_OTP;
    private ConnectivityReceiver connectivityReceiver;

    String mno,fname,lname,email,pwd;

    String otp="";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_reset_pwd_otp);


        while(otp.length()!=6)
        {
            otp= String.valueOf(new Random().nextInt(999999));
        }

        forgot_OTP =findViewById(R.id.ngo_forgot_OTP);
        forgot_submit_OTP =findViewById(R.id.ngo_forgot_submit_OTP);


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
                // Handle the back button based on the source
                finish();Animatoo.INSTANCE.animateSlideRight(NGO_reset_pwd_OTP.this);

            }
        });

        Intent intent = getIntent();
        mno = intent.getStringExtra("forgot_number");
        fname = getIntent().getStringExtra("fname");
        email = getIntent().getStringExtra("email");
        pwd = getIntent().getStringExtra("pwd");

        forgot_submit_OTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String otp1 = forgot_OTP.getText().toString();

                if (!isInternetAvailable()) {
                    showSnackbar(findViewById(android.R.id.content),"Please check your internet connection...");
                    return;
                }else {
                    if (otp.isEmpty()) {
                        showSnackbar(findViewById(android.R.id.content), "Please enter an OTP...");
                    } else if (otp.length() < 6) {
                        showSnackbar(findViewById(android.R.id.content), "Please enter 6 digits long OTP...");
                    }else if(otp1.matches(otp)){

                        forgotpwdmail(fname,email,mno,pwd);

                    }else{
                        showSnackbar(findViewById(android.R.id.content), "Please enter a Valid OTP...");
                    }
                }

            }
        });

        initiateMailOtp(fname);

    }

    private void initiateMailOtp(String fname1) {
        try {
            String senderEmail = "tathastu052threesofficial@gmail.com";
            String password = "jwhqpkbuqwmkirwy";

            String loginMessage = "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                    "<tr>\n" +
                    "    <td align=\"center\">\n" +
                    "        <table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: collapse;\">\n" +
                    "            <tr>\n" +
                    "                <td bgcolor=\"#3871c1\" style=\"padding: 5px; text-align: center;\">\n" +
                    "                    <h2 style=\"color: #ffffff;\">Email Verification</h2>\n" +
                    "                </td>\n" +
                    "            </tr>\n" +
                    "            <tr>\n" +
                    "                <td bgcolor=\"#ffffff\" style=\"padding: 10px; color: black;\">\n" +
                    "                    <p>Hello " + fname1 + ",</p>\n" +
                    "                    <p>Your Email Verification Request is Accepted By Us.</p>\n" +
                    "                    <p>OTP For Verifying Your Email Id :</p>\n" +
                    "                    <center>\n" +
                    "                        <h1 style=\"color:Green\">"+otp+"</h1>\n" +
                    "                    </center>\n" +
                    "                    <p>For Security Reasons, Please Don't Share This OTP With Anyone.\n" +
                    "                    </p>\n" +
                    "                    <p>If You Don't Register For Your Account Then You Can Safely Ignore This Email.</p>\n" +
                    "                </td>\n" +
                    "            </tr>\n" +
                    "            <tr>\n" +
                    "                <td bgcolor=\"#3871c1\" style=\"padding: 5px; text-align: center;\">\n" +
                    "                    <p style=\"color: #ffffff;\">Thank You For Using Our Service !</p>\n" +
                    "                </td>\n" +
                    "            </tr>\n" +
                    "        </table>\n" +
                    "    </td>\n" +
                    "</tr>\n" +
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
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

            // Setting the subject and message content as HTML
            mimeMessage.setSubject("Verify Your Email !!");
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
                                Toast.makeText(NGO_reset_pwd_OTP.this, "Error Occurred : ", Toast.LENGTH_SHORT).show();
                            }
                        });
                        e.printStackTrace();
                    }
                }
            });
            t.start();

        } catch (AddressException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        Toast.makeText(NGO_reset_pwd_OTP.this, "Otp Mail Sent Successfully.", Toast.LENGTH_SHORT).show();

    }

    public void forgotpwdmail(String fname, String email, String mno, String pwd) {

        Intent intent = new Intent(NGO_reset_pwd_OTP.this, NGO_Login_Screen.class);
        startActivity(intent);
        Animatoo.INSTANCE.animateSlideRight(NGO_reset_pwd_OTP.this);

        try {
            String senderEmail = "tathastu052threesofficial@gmail.com";
            String password = "jwhqpkbuqwmkirwy";

            String registerMessage =  "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                    "    <tr>\n" +
                    "        <td align=\"center\">\n" +
                    "            <table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: collapse;\">\n" +
                    "                <tr>\n" +
                    "                    <td bgcolor=\"#2E80DF\" style=\"padding: 1px; text-align: center;\">\n" +
                    "                        <h2 style=\"color: #ffffff;\">Welcome to Tathastu</h2>\n" +
                    "                    </td>\n" +
                    "                </tr>\n" +
                    "                <tr>\n" +
                    "                    <td bgcolor=\"#ffffff\" style=\"padding: 10px; color: black;\">\n" +
                    "                        <p>Hello "+fname +" </p>\n" +
                    "                        <p>Thank you for joining Tathastu - The Donation App! Your commitment to making a difference is truly appreciated.</p>\n" +
                    "                        <p>You are registered with your mobile number & password as below :</p>\n" +
                    "                        <ul>\n" +
                    "                            <li><strong>Mobile Number  :  </strong> "+mno+"</li>\n" +
                    "                            <li><strong>Password  :  </strong>"+pwd+"</li>\n" +
                    "                        </ul>\n" +
                    "                        </p>\n" +
                    "                        <p>Thank you for joining hands with [Tathastu - The Donation App] in making a positive impact! If you have any questions or need assistance, feel free to reach out to us at tathastu052threesofficial@gmail.com.\n" +
                    "                        <p style=\"text-align: center; margin: 20px 0;\">Follow us on:</p>\n" +
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
                    "                       <p>Best regards,\n" +
                    "                        </p>\n" +
                    "                               The Tathastu - The Donation App Team\n" +
                    "                            </p>\n" +
                    "                    </td>\n" +
                    "                </tr>\n" +
                    "                <tr>\n" +
                    "                    <td bgcolor=\"#2E80DF\" style=\"padding: 5px; text-align: center;\">\n" +
                    "                        <p style=\"color: #ffffff;\">Thank You For Using Our Service!</p>\n" +
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
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

            // Setting the subject and message content as HTML
            mimeMessage.setSubject("Registration Successfully !!");
            mimeMessage.setContent(registerMessage,"text/html");

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
                                Toast.makeText(NGO_reset_pwd_OTP.this, "Error Occurred : ", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(NGO_reset_pwd_OTP.this, "Error Occurred : " + e, Toast.LENGTH_SHORT).show();
        }

        catch (MessagingException e) {
            // Handling messaging exception (e.g. network error)
            Toast.makeText(NGO_reset_pwd_OTP.this, "Error Occurred : " + e, Toast.LENGTH_SHORT).show();
        }

        catch (UnsupportedEncodingException e) {
            Toast.makeText(NGO_reset_pwd_OTP.this, "Error Occurred : " + e, Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(NGO_reset_pwd_OTP.this, "Register Mail Sent Successfully.", Toast.LENGTH_SHORT).show();


    }


    //----------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(this);
        finish();
    }

    //HIDE THE KEYBOARD
    private void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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