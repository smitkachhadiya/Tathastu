package com.example.tathastu.User_Package.user_Entry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_DashBoard.DashBoard_Screen;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

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

public class Otp_Screen extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    MaterialTextView txt_otp_mno;
    ExtendedFloatingActionButton BTN_otp;
    private MaterialTextView tvOtpTime;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private static final long OTP_TIMER_DURATION = 120 * 1000; // 5 minutes in milliseconds
    private static final long INTERVAL = 1000; // 1 second in milliseconds
    public String fname,lname,email,dob,password,mobile,check,userId;
    FirebaseAuth mAuth;
    String otpid;
    public EditText edtotp;
    private ConnectivityReceiver connectivityReceiver;

    Token t;

    String otp="";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_screen);

        generateToken();


        while(otp.length()!=6)
        {
            otp= String.valueOf(new Random().nextInt(999999));
        }

//        tvOtpTime = findViewById(R.id.tv_OTPtime);
        BTN_otp = findViewById(R.id.BTN_otp);
        mAuth = FirebaseAuth.getInstance();
        txt_otp_mno=findViewById(R.id.txt_otp_mno);
        edtotp = findViewById(R.id.txt_OTP);

        FloatingActionButton BTN_back=findViewById(R.id.BTN_back);
        //BACK
        BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the back button based on the source
                String source = getIntent().getStringExtra("source");
                if ("login".equals(source)) {
                    // If source is login, go back to LoginActivity
//            Intent intent = new Intent(Otp_Screen.this, Login_Screen.class);
//            startActivity(intent);
                    Animatoo.INSTANCE.animateSlideRight(Otp_Screen.this);
                    finish();
                } else if ("signin".equals(source)) {
                    // If source is signin, go back to SignInActivity
//            Intent intent = new Intent(Otp_Screen.this, Signin_Screen.class);
//            startActivity(intent);
                    Animatoo.INSTANCE.animateSlideRight(Otp_Screen.this);
                    finish();
                } else {
                    // Default behavior (handle appropriately)
                    Animatoo.INSTANCE.animateSlideRight(Otp_Screen.this);
                    onBackPressed();
                }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    }
        });

        fname = getIntent().getStringExtra("fname");
        lname = getIntent().getStringExtra("lname");
        email = getIntent().getStringExtra("email");
        dob = getIntent().getStringExtra("dob");
        password = getIntent().getStringExtra("password");
        mobile = getIntent().getStringExtra("mobile");
        check = getIntent().getStringExtra("check");
        userId = getIntent().getStringExtra("userId");

        txt_otp_mno.setText(email);


        // Initialize the ConnectivityReceiver
        connectivityReceiver = new ConnectivityReceiver();
        ConnectivityReceiver.connectivityReceiverListener = this;

        // Register the receiver to listen for connectivity changes
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        BTN_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enteredotp = edtotp.getText().toString();
                if (!isInternetAvailable()) {
                    showSnackbar(findViewById(android.R.id.content),"Please check your internet connection...");
                    return;
                }else {
                    if (enteredotp.isEmpty()) {
                        showSnackbar(findViewById(android.R.id.content), "Please enter an OTP...");
                    } else if (enteredotp.length() < 6) {
                        showSnackbar(findViewById(android.R.id.content), "Please enter 6 digits long OTP...");
                    }else if(enteredotp.matches(otp)){
//                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid, enteredotp);
//                        signInWithPhoneAuthCredential(credential);
//                        startOtpTimer();

                        signInWithEmailAuth();
                    }else{
                        showSnackbar(findViewById(android.R.id.content), "Please enter a Valid OTP...");
                    }
                }
            }
        });



//        initiateotp();

        initiateMailOtp(fname);
    }

    private void generateToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            String token = task.getResult();
//                            Toast.makeText(Otp_Screen.this, "Token Generated Successfully !!\n" + token, Toast.LENGTH_SHORT).show();
                            t = new Token(token);
                        } else {
//                            Toast.makeText(Otp_Screen.this, "Failed To Generate Token !!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void signInWithEmailAuth() {
        if (check.equals("register")){

            SharedPreferences sharedPreferences = getSharedPreferences("UserLogin",MODE_PRIVATE);
            sharedPreferences.edit().putBoolean("hasLoggedIn",true).apply();


            String userId = UUID.randomUUID().toString();


            Map<String, Object> map = new HashMap<>();
            map.put("fname", fname);
            map.put("lname", lname);
            map.put("email", email);
            map.put("dob", dob);
            map.put("mobile", mobile);
            map.put("password", password);
            map.put("userId",userId);
            map.put("token",t.getToken());

            FirebaseDatabase.getInstance().getReference().child("user").child(userId)
                    .setValue(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            SharedPreferences sharedPreferences1 = getSharedPreferences("USER",MODE_PRIVATE);
                            sharedPreferences1.edit().putString("userId",userId).apply();
                            sharedPreferences1.edit().putString("email",email).apply();
                            sharedPreferences1.edit().putString("fname",fname).apply();
                            sharedPreferences1.edit().putString("lname",lname).apply();
                            sharedPreferences1.edit().putString("mobile",mobile).apply();

                            SharedPreferences sharedPreferences = getSharedPreferences("UserLogin",MODE_PRIVATE);
                            sharedPreferences.edit().putBoolean("hasLoggedIn",true).apply();

                            showSnackbar(findViewById(android.R.id.content), "Successfully Registered.");
                            Intent i = new Intent(Otp_Screen.this, DashBoard_Screen.class);
                            startActivity(i);
                            Animatoo.INSTANCE.animateSlideLeft(Otp_Screen.this);
                            sendregistermail(fname,lname,email,mobile,password);
                            sendloggedinmail(fname,lname,email);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            showSnackbar(findViewById(android.R.id.content), "Error: While Inserting Data.");

                        }
                    });

        } else {

            FirebaseDatabase.getInstance().getReference().child("user").child(userId).child("token")
                    .setValue(t.getToken())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
//                            Toast.makeText(Otp_Screen.this, "Token Saved In Firebase.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(Otp_Screen.this, "Failed To Save Token In Firebase.", Toast.LENGTH_SHORT).show();
                        }
                    });

            SharedPreferences sharedPreferences1 = getSharedPreferences("USER",MODE_PRIVATE);
            sharedPreferences1.edit().putString("userId",userId).apply();
            sharedPreferences1.edit().putString("email",email).apply();
            sharedPreferences1.edit().putString("fname",fname).apply();
            sharedPreferences1.edit().putString("mobile",mobile).apply();


            SharedPreferences sharedPreferences = getSharedPreferences("UserLogin",MODE_PRIVATE);
            sharedPreferences.edit().putBoolean("hasLoggedIn",true).apply();

            Intent i = new Intent(Otp_Screen.this, DashBoard_Screen.class);
            startActivity(i);
            Animatoo.INSTANCE.animateSlideLeft(Otp_Screen.this);
            sendloggedinmail(fname,lname,email);

        }
    }

//---------------------------------------------------------------------------------------------------


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String source = getIntent().getStringExtra("source");
        if ("login".equals(source)) {
            // If source is login, go back to LoginActivity
//            Intent intent = new Intent(Otp_Screen.this, Login_Screen.class);
//            startActivity(intent);
            Animatoo.INSTANCE.animateSlideRight(Otp_Screen.this);
            finish();
        } else if ("signin".equals(source)) {
            // If source is signin, go back to SignInActivity
//            Intent intent = new Intent(Otp_Screen.this, Signin_Screen.class);
//            startActivity(intent);
            Animatoo.INSTANCE.animateSlideRight(Otp_Screen.this);
            finish();
        } else {
            // Default behavior (handle appropriately)
            Animatoo.INSTANCE.animateSlideRight(Otp_Screen.this);
            onBackPressed();
        }
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
                                Toast.makeText(Otp_Screen.this, "Error Occurred : ", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(Otp_Screen.this, "Otp Mail Sent Successfully.", Toast.LENGTH_SHORT).show();

    }
    public void sendregistermail(String fname,String lname,String email,String contact,String pass){

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
                    "                        <p>Hello "+fname +" "+ lname+",</p>\n" +
                    "                        <p>Thank you for joining Tathastu - The Donation App! Your commitment to making a difference is truly appreciated.</p>\n" +
                    "                        <p>You are registered with your mobile number & password as below :</p>\n" +
                    "                        <ul>\n" +
                    "                            <li><strong>Mobile Number  :  </strong>+ "+contact+"</li>\n" +
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
                                Toast.makeText(Otp_Screen.this, "Error Occurred : ", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(Otp_Screen.this, "Error Occurred : " + e, Toast.LENGTH_SHORT).show();
        }

        catch (MessagingException e) {
            // Handling messaging exception (e.g. network error)
            Toast.makeText(Otp_Screen.this, "Error Occurred : " + e, Toast.LENGTH_SHORT).show();
        }

        catch (UnsupportedEncodingException e) {
            Toast.makeText(Otp_Screen.this, "Error Occurred : " + e, Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(Otp_Screen.this, "Register Mail Sent Successfully.", Toast.LENGTH_SHORT).show();

    }

    public void sendloggedinmail(String fname1,String lname2,String email) {

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
                    "                        <p>Hello "+fname1 +" "+ lname2+",</p>\n" +
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
                                Toast.makeText(Otp_Screen.this, "Error Occurred : ", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(Otp_Screen.this, "Error Occurred : " + e, Toast.LENGTH_SHORT).show();
        }

        catch (MessagingException e) {
            // Handling messaging exception (e.g. network error)
            Toast.makeText(Otp_Screen.this, "Error Occurred : " + e, Toast.LENGTH_SHORT).show();
        }

        catch (UnsupportedEncodingException e) {
            Toast.makeText(Otp_Screen.this, "Error Occurred : " + e, Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(Otp_Screen.this, "Loggedin Mail Sent Successfully.", Toast.LENGTH_SHORT).show();

    }

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
//                tvOtpTime.setText("00:00"); // Update the UI or trigger OTP resend, etc.
//            }
//        }.start();
//    }
//
//    private void updateTimerText() {
//        int minutes = (int) (timeLeftInMillis / 1000) / 60;
//        int seconds = (int) (timeLeftInMillis / 1000) % 60;
//
//        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
//        tvOtpTime.setText(timeFormatted);
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

class Token{
    String token;

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
