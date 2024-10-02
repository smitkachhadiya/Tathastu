package com.example.tathastu.User_Package.user_Campaign;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
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
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Random;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class user_Campaign_Donate extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, PaymentResultListener {
    private ConnectivityReceiver connectivityReceiver;

    DatabaseReference reference,reference1;
    FirebaseStorage storage;
    StorageReference storageReference;
    ShapeableImageView campaignimage;
    TextInputEditText txt_amount;
    SwitchCompat hidename;

    MaterialTextView ename;

    CardView card_pay_100, card_pay_200, card_pay_500, card_pay_1000, card_pay_1500, card_pay_2000;
    ExtendedFloatingActionButton btn_donate;
    String username,email,contactNo,userId,key,date;
    private String donationAmount;
    String ngoName;

    SharedPreferences sharedPreferences;

    String ioname;

    String completeFilePath;

    String fileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_campaign_donate);

        sharedPreferences = getSharedPreferences("USER",MODE_PRIVATE);

        ename = findViewById(R.id.ename);

        txt_amount =  findViewById(R.id.editText_amount);
        campaignimage = findViewById(R.id.eventimage);
        btn_donate =  findViewById(R.id.btn_donate);
        card_pay_100 = findViewById(R.id.card_pay_100);
        card_pay_200 = findViewById(R.id.card_pay_200);
        card_pay_500 = findViewById(R.id.card_pay_500);
        card_pay_1000 = findViewById(R.id.card_pay_1000);
        card_pay_1500 = findViewById(R.id.card_pay_1500);
        card_pay_2000 = findViewById(R.id.card_pay_2000);
        hidename = (SwitchCompat) findViewById(R.id.hidename);

        // Initialize the ConnectivityReceiver
        connectivityReceiver = new ConnectivityReceiver();
        ConnectivityReceiver.connectivityReceiverListener = this;

        // Register the receiver to listen for connectivity changes
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        Intent intent = this.getIntent();
         key = intent.getStringExtra("key");

        reference = FirebaseDatabase.getInstance().getReference().child("campaigns").child(key);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                campaigns campaign = snapshot.getValue(campaigns.class);
                if (campaign != null) {
                    String description = campaign.getDescription();
                    String ename1 =campaign.getName();
                    ioname = campaign.getOrganizer_name();
                    String iocontact = campaign.getOrganizer_contact();
                    String icdonated = campaign.getDonation_received();
                    String imageUrl = campaign.getImageUrl();

                    ename.setText(ename1);
                    Glide.with(user_Campaign_Donate.this).load(imageUrl).into(campaignimage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btn_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // checking if campaign is closed

//                if(total_amount > received_amount){

                // fetch the current time and date

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                date = dtf.format(now).toString();

                if(hidename.isChecked()){
                    userId=sharedPreferences.getString("userId","");
                    username = "Anonymous";
                    email = sharedPreferences.getString("email","");
                    contactNo = sharedPreferences.getString("mobile","");
                }else {
                    // Fetch current user Details
                    userId=sharedPreferences.getString("userId","");
                    username = sharedPreferences.getString("fname","");
                    email = sharedPreferences.getString("email","");
                    contactNo = sharedPreferences.getString("mobile","");
                }

                // coding to store donations data

                if(!txt_amount.getText().toString().isEmpty()) {

                    donationAmount = txt_amount.getText().toString();

                    initiateDonation(username,email,contactNo,txt_amount.getText().toString());
                }else{
                    txt_amount.setError("Enter Amount");
                }
            }
        });

        txt_amount = findViewById(R.id.editText_amount);

// Assuming card_pay_100, card_pay_200, ..., card_pay_2000 are your card views
        int[] cardIds = {R.id.card_pay_100, R.id.card_pay_200, R.id.card_pay_500, R.id.card_pay_1000, R.id.card_pay_1500, R.id.card_pay_2000};
        int[] amounts = {100, 200, 500, 1000, 1500, 2000};

        for (int i = 0; i < cardIds.length; i++) {
            final int amount = amounts[i];

            findViewById(cardIds[i]).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Clear previous selection
                    txt_amount.setText("");

                    // Set the text for the clicked card
                    txt_amount.setText(String.valueOf(amount));
                }
            });
        }

        FloatingActionButton BTN_back=findViewById(R.id.BTN_event_back);
        //BACK
        BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Animatoo.INSTANCE.animateSlideRight(user_Campaign_Donate.this);
            }
        });
    }

//------------------------------------------------------------------------------------------------

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

    public void initiateDonation(String username, String email, String contactNo, String donationAmount) {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_iiWet5Chi79qWI"); // Replace with your actual Razorpay key

        try {
            JSONObject options = new JSONObject();
            options.put("name", username);
            options.put("currency", "INR");
            options.put("amount", Integer.parseInt(donationAmount) * 100);
            options.put("prefill.email", email);
            options.put("prefill.contact", contactNo);
            options.put("environment", "sandbox"); // "sandbox" or "production"
            options.put("theme.color", "#2e80df");
            options.put("method", new JSONObject().put("upi", true));

            checkout.open((Activity)  user_Campaign_Donate.this, options);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void generateAndDownloadBill(String s){
        View view = LayoutInflater.from(this).inflate(R.layout.payment_receipt,null);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        TextView donor = view.findViewById(R.id.donor);
        TextView donationTo = view.findViewById(R.id.donationTo);
        TextView paymentMethod = view.findViewById(R.id.paymentMethod);
        TextView dateTime = view.findViewById(R.id.dateTime);
        TextView amount = view.findViewById(R.id.amount);
        TextView paymentId = view.findViewById(R.id.paymentId);
        TextView status = view.findViewById(R.id.status);

        donor.setText(sharedPreferences.getString("fname","")+sharedPreferences.getString("lname",""));
        donationTo.setText(ioname);
        paymentMethod.setText("Online");
        dateTime.setText(dtf.format(now));
        amount.setText("₹"+donationAmount);
        paymentId.setText(s);
        status.setText("Success");

        fileName="Payment-Receipt-"+new Random().nextInt(1000000);

        completeFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+fileName+".pdf";


        PdfGenerator.getBuilder()
                .setContext(user_Campaign_Donate.this)
                .fromViewSource()
                .fromView(view)
                .setFileName(fileName)
                .setFolderNameOrPath(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)))
                .actionAfterPDFGeneration(PdfGenerator.ActionAfterPDFGeneration.OPEN)
                .build(new PdfGeneratorListener() {
                    @Override
                    public void onFailure(FailureResponse failureResponse) {
                        super.onFailure(failureResponse);
                        Toast.makeText(user_Campaign_Donate.this, "Failed To Download Payment Receipt. ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void showLog(String log) {
                        super.showLog(log);
                    }

                    @Override
                    public void onStartPDFGeneration() {
                    }

                    @Override
                    public void onFinishPDFGeneration() {
                    }

                    @Override
                    public void onSuccess(SuccessResponse response) {
                        super.onSuccess(response);
                        Toast.makeText(user_Campaign_Donate.this, "Payment Receipt Downloaded Successfully.", Toast.LENGTH_SHORT).show();
                        sendPaymentMail();
                    }
                });
    }

    void sendPaymentMail()
    {
        try {

            String fname = sharedPreferences.getString("fname","");
            String email = sharedPreferences.getString("email","");

            String senderEmail = "tathastu052threesofficial@gmail.com";
            String password = "jwhqpkbuqwmkirwy";

            String receipt = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <title>Donation Receipt from TATHASTU</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                    "        <tr>\n" +
                    "            <td align=\"center\">\n" +
                    "                <table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: collapse;\">\n" +
                    "                    <tr>\n" +
                    "                        <td bgcolor=\"#2E80DF\" style=\"padding: 1px; text-align: center;\">\n" +
                    "                            <h2 style=\"color: #ffffff;\">Donation Receipt from TATHASTU</h2>\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                    <tr>\n" +
                    "                        <td bgcolor=\"#ffffff\" style=\"padding: 10px; color: black;\">\n" +
                    "                            <p>Hello "+fname+",</p>\n" +
                    "                            <p>Thank you for your generous donation to Tathastu - The Donation App! Your contribution is greatly appreciated.</p>\n" +
                    "                            <p>We have attached your donation receipt below.</p>\n" +
                    "                            <p>If you have any questions or need further assistance, please feel free to contact us at tathastu052threesofficial@gmail.com.</p>\n" +
                    "                            <p style=\"text-align: center; margin: 20px 0;\">Follow us on:</p>\n" +
                    "                            <p style=\"text-align: center;\">\n" +
                    "                                <a href=\"https://www.instagram.com/tathastu.g052?igsh=OGQ5ZDc2ODk2ZA==\" style=\"margin-right: 20px;\">\n" +
                    "                                    <img src=\"https://freelogopng.com/images/all_img/1658588965instagram-logo-png-transparent-background.png\" alt=\"Instagram\" style=\"width: 30px; height: 30px;\">\n" +
                    "                                </a>\n" +
                    "                                <a href=\"https://www.facebook.com/tathastu.g052?mibextid=ZbWKwL\" style=\"margin-right: 20px;\">\n" +
                    "                                    <img src=\"https://freelogopng.com/images/all_img/1658030214facebook-logo-hd.png\" alt=\"Facebook\" style=\"width: 30px; height: 30px;\">\n" +
                    "                                </a>\n" +
                    "                                <a href=\"https://twitter.com/tathastu_g052?t=X-SvynEa7on0GDAirv3UsQ&s=09\">\n" +
                    "                                    <img src=\"https://freelogopng.com/images/all_img/1657045399twitter-icon-png.png\" alt=\"Twitter\" style=\"width: 30px; height: 30px;\">\n" +
                    "                                </a>\n" +
                    "                            </p>\n" +
                    "                            <p>Best regards,<br>\n" +
                    "                                The Tathastu - The Donation App Team\n" +
                    "                            </p>\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                    <tr>\n" +
                    "                        <td bgcolor=\"#2E80DF\" style=\"padding: 5px; text-align: center;\">\n" +
                    "                            <p style=\"color: #ffffff;\">Thank You For Your Generosity!</p>\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                </table>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "    </table>\n" +
                    "</body>\n" +
                    "</html>";

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
            mimeMessage.setSubject("Donation Receipt !!");

            MimeBodyPart attachmentPart = new MimeBodyPart();
            FileDataSource source = new FileDataSource(completeFilePath);
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName(fileName+".pdf");

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(receipt,"text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            mimeMessage.setContent(multipart);

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
                                Toast.makeText(user_Campaign_Donate.this, "Error Occurred : ", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(user_Campaign_Donate.this, "Error Occurred : " + e, Toast.LENGTH_SHORT).show();
        }

        catch (MessagingException e) {
            // Handling messaging exception (e.g. network error)
            Toast.makeText(user_Campaign_Donate.this, "Error Occurred : " + e, Toast.LENGTH_SHORT).show();
        }

        catch (UnsupportedEncodingException e) {
            Toast.makeText(user_Campaign_Donate.this, "Error Occurred : " + e, Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(user_Campaign_Donate.this, "Payment Receipt Sent On Your Registered Email Id !!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPaymentSuccess(String s) {

        generateAndDownloadBill(s);

        String amount = txt_amount.getText().toString();
        Random r = new Random();
        Integer transaction_id = r.nextInt(99999);
        String tid  = Integer.toString(transaction_id);

        reference.child("Donations").child(transaction_id.toString()).child("name").setValue(username);
        reference.child("Donations").child(transaction_id.toString()).child("email").setValue(email);
        reference.child("Donations").child(transaction_id.toString()).child("contact_no").setValue(contactNo);
        reference.child("Donations").child(transaction_id.toString()).child("userId").setValue(userId);
        reference.child("Donations").child(transaction_id.toString()).child("amount").setValue(amount);
        reference.child("Donations").child(transaction_id.toString()).child("date").setValue(date);
        reference.child("Donations").child(transaction_id.toString()).child("transaction_id").setValue(transaction_id.toString());
        reference.child("Donations").child(transaction_id.toString()).child("organizer_name").setValue(ioname);

        // adding the user donation to the total received donation

        reference1 = FirebaseDatabase.getInstance().getReference().child("campaigns").child(key).child("Donations");
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int total = 0;

                for(DataSnapshot donationSnapshot : snapshot.getChildren()){
                    if(donationSnapshot.hasChild("amount")){
                        String amount = donationSnapshot.child("amount").getValue(String.class);
                        total+=Integer.parseInt(amount);
                    }
                }
                reference.child("donation_received").setValue(String.valueOf(total));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(user_Campaign_Donate.this,"Error....",Toast.LENGTH_SHORT).show();
            }
        });
        Toast.makeText(this, "Payment Successful !!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed !!", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(this);
        finish();
    }
}