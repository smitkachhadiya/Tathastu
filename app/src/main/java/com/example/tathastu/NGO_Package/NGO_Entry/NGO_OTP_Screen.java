package com.example.tathastu.NGO_Package.NGO_Entry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.NGO_Package.NGO_DashBoard.NGO_Dashboard_Screen;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class NGO_OTP_Screen extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    MaterialTextView txt_otp_mno_ngo;
    ExtendedFloatingActionButton BTN_otp_ngo;
    FirebaseAuth mAuth;
    String otpid;
    public EditText edtotp_ngo;
    private ConnectivityReceiver connectivityReceiver;

    String otp="";

    String fname,email,mno,omno,address,type,cpwd,check,ngoId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_otp_screen);

        while(otp.length()!=6)
        {
            otp= String.valueOf(new Random().nextInt(999999));
        }

//        tvOtpTime_ngo = findViewById(R.id.tv_OTPtime_ngo);
        BTN_otp_ngo = findViewById(R.id.BTN_otp_ngo);
        mAuth = FirebaseAuth.getInstance();
        txt_otp_mno_ngo=findViewById(R.id.txt_otp_mno_ngo);
        edtotp_ngo = findViewById(R.id.txt_OTP_ngo);

        FloatingActionButton BTN_back_ngo=findViewById(R.id.BTN_back_ngo);
        //BACK
        BTN_back_ngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the back button based on the source
                String source = getIntent().getStringExtra("source");
                if ("login".equals(source)) {

                    finish();
                    Animatoo.INSTANCE.animateSlideRight(NGO_OTP_Screen.this);
                } else if ("signin".equals(source)) {

                    finish();
                    Animatoo.INSTANCE.animateSlideRight(NGO_OTP_Screen.this);
                } else {
                    onBackPressed();
                    finish();
                    Animatoo.INSTANCE.animateSlideRight(NGO_OTP_Screen.this);
                }
            }
        });

        fname = getIntent().getStringExtra("fname");
        email = getIntent().getStringExtra("email");
        mno = getIntent().getStringExtra("mno");
        omno=getIntent().getStringExtra("omno");
        address =getIntent().getStringExtra("address");
        type = getIntent().getStringExtra("type");
        cpwd = getIntent().getStringExtra("cpwd");
        check = getIntent().getStringExtra("check");
        ngoId = getIntent().getStringExtra("ngoId");

        txt_otp_mno_ngo.setText(email);


        // Initialize the ConnectivityReceiver
        connectivityReceiver = new ConnectivityReceiver();
        ConnectivityReceiver.connectivityReceiverListener = this;

        // Register the receiver to listen for connectivity changes
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        BTN_otp_ngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enteredotp = edtotp_ngo.getText().toString().trim();
                if (!isInternetAvailable()) {
                    showSnackbar(findViewById(android.R.id.content),"Please check your internet connection...");
                    return;
                }else {
                    if (enteredotp.isEmpty()) {
                        showSnackbar(findViewById(android.R.id.content), "Please enter an OTP...");
                    } else if (enteredotp.length() < 6) {
                        showSnackbar(findViewById(android.R.id.content), "Please enter 6 digits long OTP...");
                    }
                    else if(enteredotp.matches(otp)){

                        signInWithEmailAuth();
                    }else{
                        showSnackbar(findViewById(android.R.id.content), "Please enter a Valid OTP...");
                    }
                }
            }
        });



//        initiateotp();

        initiateMailOtp(fname,email);

    }
//---------------------------------------------------------------------------------------------------


    private void initiateMailOtp(String fname1,String email1) {
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
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email1));

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
                                Toast.makeText(NGO_OTP_Screen.this, "Error Occurred : ", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(NGO_OTP_Screen.this, "Otp Mail Sent Successfully.", Toast.LENGTH_SHORT).show();

    }

    private void signInWithEmailAuth() {
        if (check.equals("register")){

            String ngoId = UUID.randomUUID().toString();

            Map<String, Object> map = new HashMap<>();
            map.put("fname", fname);
            map.put("email", email);
            map.put("mobile", mno);
            map.put("type", type);
            map.put("optional_mobile", omno);
            map.put("address", address);
            map.put("password", cpwd);

            map.put("ngoId",ngoId);

            FirebaseDatabase.getInstance().getReference().child("ngo").child(ngoId)
                    .setValue(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            showSnackbar(findViewById(android.R.id.content), "Successfully Registered.");
                            Intent i = new Intent(NGO_OTP_Screen.this, NGO_Dashboard_Screen.class);
                            startActivity(i);
                            Animatoo.INSTANCE.animateSlideLeft(NGO_OTP_Screen.this);
                            SharedPreferences sharedPreferences1 = getSharedPreferences("NGO",MODE_PRIVATE);
                            sharedPreferences1.edit().putString("ngoId",ngoId).apply();
                            sharedPreferences1.edit().putString("fname",fname).apply();
                            sharedPreferences1.edit().putString("email",email).apply();
                            sharedPreferences1.edit().putString("mobile",mno).apply();

                            SharedPreferences sharedPreferences = getSharedPreferences("NGOLogin",MODE_PRIVATE);
                            sharedPreferences.edit().putBoolean("hasLoggedIn",true).apply();

                            sendregistermail(fname,email,mno,cpwd);
                            sendloggedinmail(fname,email);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            showSnackbar(findViewById(android.R.id.content), "Error: While Inserting Data.");

                        }
                    });

        } else {

            SharedPreferences sharedPreferences1 = getSharedPreferences("NGO",MODE_PRIVATE);
            sharedPreferences1.edit().putString("ngoId",ngoId).apply();
            sharedPreferences1.edit().putString("fname",fname).apply();
            sharedPreferences1.edit().putString("email",email).apply();
            sharedPreferences1.edit().putString("mobile",mno).apply();


            SharedPreferences sharedPreferences = getSharedPreferences("NGOLogin",MODE_PRIVATE);
            sharedPreferences.edit().putBoolean("hasLoggedIn",true).apply();

            Intent i = new Intent(NGO_OTP_Screen.this, NGO_Dashboard_Screen.class);
            startActivity(i);
            Animatoo.INSTANCE.animateSlideLeft(NGO_OTP_Screen.this);

            sendloggedinmail(fname,email);

        }
    }

    public void sendregistermail(String finame,String email,String contact,String pass){

        try {
            String senderEmail = "tathastu052threesofficial@gmail.com";
            String password = "jwhqpkbuqwmkirwy";

            String registerMessage = "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
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
                    "                        <p>Hello "+finame +" </p>\n" +
                    "                        <p>Thank you for joining Tathastu - The Donation App! Your commitment to making a difference is truly appreciated.</p>\n" +
                    "                        <p>You are registered with your mobile number & password as below :</p>\n" +
                    "                        <ul>\n" +
                    "                            <li><strong>Mobile Number  :  </strong> "+contact+"</li>\n" +
                    "                            <li><strong>Password  :  </strong>"+pass+"</li>\n" +
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


            String loginMessage = "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                    "    <tr>\n" +
                    "        <td align=\"center\">\n" +
                    "            <table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: collapse;\">\n" +
                    "                <tr>\n" +
                    "                    <td bgcolor=\"#2E80DF\" style=\"padding: 1px; text-align: center;\">\n" +
                    "                        <h2 style=\"color: #ffffff;\">Welcome Back to TATHASTU</h2>\n" +
                    "                    </td>\n" +
                    "                </tr>\n" +
                    "                <tr>\n" +
                    "                    <td bgcolor=\"#ffffff\" style=\"padding: 10px; color: black;\">\n" +
                    "                        <p>Hello "+fname +",</p>\n" +
                    "                        <p>Welcome back to Tathastu - The Donation App! You have successfully logged in with your registered mobile number.</p>\n" +
                    "                        <p>If you have any questions or need assistance, feel free to reach out to us at tathastu052threesofficial@gmail.com.</p>\n" +
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
                    "                        <p>Best regards,\n" +
                    "                            The Tathastu - The Donation App Team\n" +
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
                                Toast.makeText(NGO_OTP_Screen.this, "Error Occurred : ", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(NGO_OTP_Screen.this, "Error Occurred : " + e, Toast.LENGTH_SHORT).show();
        }

        catch (MessagingException e) {
            // Handling messaging exception (e.g. network error)
            Toast.makeText(NGO_OTP_Screen.this, "Error Occurred : " + e, Toast.LENGTH_SHORT).show();
        }

        catch (UnsupportedEncodingException e) {
            Toast.makeText(NGO_OTP_Screen.this, "Error Occurred : " + e, Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(NGO_OTP_Screen.this, "Register Mail Sent Successfully.", Toast.LENGTH_SHORT).show();

    }

    public void sendloggedinmail(String fname1,String email1) {

        try {
            String senderEmail = "tathastu052threesofficial@gmail.com";
            String password = "jwhqpkbuqwmkirwy";

            String loginMessage = "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                    "    <tr>\n" +
                    "        <td align=\"center\">\n" +
                    "            <table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: collapse;\">\n" +
                    "                <tr>\n" +
                    "                    <td bgcolor=\"#2E80DF\" style=\"padding: 1px; text-align: center;\">\n" +
                    "                        <h2 style=\"color: #ffffff;\">Welcome Back to TATHASTU</h2>\n" +
                    "                    </td>\n" +
                    "                </tr>\n" +
                    "                <tr>\n" +
                    "                    <td bgcolor=\"#ffffff\" style=\"padding: 10px; color: black;\">\n" +
                    "                        <p>Hello "+fname1 +" ,</p>\n" +
                    "                        <p>Welcome back to Tathastu - The Donation App! You have successfully logged in with your registered mobile number.</p>\n" +
                    "                        <p>If you have any questions or need assistance, feel free to reach out to us at tathastu052threesofficial@gmail.com.</p>\n" +
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
                    "                        <p>Best regards,\n" +
                    "                            The Tathastu - The Donation App Team\n" +
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
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email1));

            // Setting the subject and message content as HTML
            mimeMessage.setSubject("Loggedin Successfully !!");
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
                                Toast.makeText(NGO_OTP_Screen.this, "Error Occurred : ", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(NGO_OTP_Screen.this, "Error Occurred : " + e, Toast.LENGTH_SHORT).show();
        }

        catch (MessagingException e) {
            // Handling messaging exception (e.g. network error)
            Toast.makeText(NGO_OTP_Screen.this, "Error Occurred : " + e, Toast.LENGTH_SHORT).show();
        }

        catch (UnsupportedEncodingException e) {
            Toast.makeText(NGO_OTP_Screen.this, "Error Occurred : " + e, Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(NGO_OTP_Screen.this, "Loggedin Mail Sent Successfully.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String source = getIntent().getStringExtra("source");
        if ("login".equals(source)) {
            // If source is login, go back to LoginActivity
            Intent intent = new Intent(NGO_OTP_Screen.this, NGO_Login_Screen.class);
            startActivity(intent);
            Animatoo.INSTANCE.animateSlideRight(this);
        } else if ("signin".equals(source)) {
            // If source is signin, go back to SignInActivity
            Intent intent = new Intent(NGO_OTP_Screen.this, NGO_Signin_Screen.class);
            startActivity(intent);
            Animatoo.INSTANCE.animateSlideRight(this);
        } else {
            // Default behavior (handle appropriately)
            onBackPressed();
            Animatoo.INSTANCE.animateSlideRight(this);
        }
    }

//    private void initiateotp() {
//
//        PhoneAuthOptions options =
//                PhoneAuthOptions.newBuilder(mAuth)
//                        .setPhoneNumber(phonenumber_ngo)       // Phone number to verify
//                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                        .setActivity(this)                 // (optional) Activity for callback binding
//                        // If no activity is passed, reCAPTCHA verification can not be used.
//                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                            @Override
//                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//
//                                otpid = s; // When the sim card is in to the other device these method call
//
//                            }
//
//                            @Override
//                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//
//                                signInWithPhoneAuthCredential(phoneAuthCredential); // when the sim card in current device thse method is call
//
//                            }
//
//                            @Override
//                            public void onVerificationFailed(@NonNull FirebaseException e) {
//
//                                // OTP Generate Time Show the Error These Method is called
//                                Toast.makeText(NGO_OTP_Screen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                            }
//                        })          // OnVerificationStateChangedCallbacks
//                        .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);
//
//    }
//
//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//
//                            Intent i = new Intent(NGO_OTP_Screen.this, DashBoard_Screen.class);
//                            startActivity(i);
//                            Animatoo.INSTANCE.animateSlideLeft(NGO_OTP_Screen.this);
//
//                        } else {
//
//                            Toast.makeText(NGO_OTP_Screen.this, "Sign In Code Error", Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                });
//    }

//    private void startOtpTimer() {
//        timeLeftInMillis = OTP_TIMER_DURATION;
//
//        countDownTimer = new CountDownTimer(timeLeftInMillis, INTERVAL) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                timeLeftInMillis = millisUntilFinished;
//                updateTimerText();
//            }
//
//            @Override
//            public void onFinish() {
//                // The timer has finished, handle accordingly
////                tvOtpTime_ngo.setText("00:00"); // Update the UI or trigger OTP resend, etc.
//            }
//        }.start();
//    }
//
//    private void updateTimerText() {
//        int minutes = (int) (timeLeftInMillis / 1000) / 60;
//        int seconds = (int) (timeLeftInMillis / 1000) % 60;
//
//        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
////        tvOtpTime_ngo.setText(timeFormatted);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        // Ensure to cancel the timer to avoid memory leaks
//        if (countDownTimer != null) {
//            countDownTimer.cancel();
//        }
//        // Unregister the receiver to avoid memory leaks
//        unregisterReceiver(connectivityReceiver);
//    }

    // Helper method to check if the internet connection is available
    private boolean isInternetAvailable() {
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
