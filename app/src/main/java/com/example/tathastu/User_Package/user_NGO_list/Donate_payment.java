package com.example.tathastu.User_Package.user_NGO_list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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

public class Donate_payment extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, PaymentResultListener {

    CardView card_pay_100, card_pay_200, card_pay_500, card_pay_1000, card_pay_1500, card_pay_2000;

    private String donationAmount;

    private ConnectivityReceiver connectivityReceiver;

    String ngoName;

    SharedPreferences sharedPreferences;

    String completeFilePath;

    String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_payment);


        sharedPreferences = getSharedPreferences("USER",MODE_PRIVATE);


        FloatingActionButton BTN_back = findViewById(R.id.BTN_back_pay);
        ExtendedFloatingActionButton BTN_pay_donate = findViewById(R.id.BTN_pay_donate);
        TextInputLayout txtlayout_payment_user = findViewById(R.id.txtlayout_payment_user);
        TextInputEditText txt_payment_user = findViewById(R.id.txt_payment_user);
        TextView txt_user_ngo_name =findViewById(R.id.txt_user_ngo_name);
        LinearLayout layout_payment = findViewById(R.id.layout_payment);
        card_pay_100 = findViewById(R.id.card_pay_100);
        card_pay_200 = findViewById(R.id.card_pay_200);
        card_pay_500 = findViewById(R.id.card_pay_500);
        card_pay_1000 = findViewById(R.id.card_pay_1000);
        card_pay_1500 = findViewById(R.id.card_pay_1500);
        card_pay_2000 = findViewById(R.id.card_pay_2000);

//        layout_payment.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // Clear focus from EditText when touched outside
//                txt_payment_user.clearFocus();
//
//
//                hideSoftKeyboard(layout_payment);
//                return false;
//            }
//        });

        // Initialize the ConnectivityReceiver
        connectivityReceiver = new ConnectivityReceiver();
        ConnectivityReceiver.connectivityReceiverListener = this;

        // Register the receiver to listen for connectivity changes
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

                Animatoo.INSTANCE.animateSlideRight(Donate_payment.this);
            }
        });

        // Assuming card_pay_100, card_pay_200, ..., card_pay_2000 are your card views
        int[] cardIds = {R.id.card_pay_100, R.id.card_pay_200, R.id.card_pay_500, R.id.card_pay_1000, R.id.card_pay_1500, R.id.card_pay_2000};
        int[] amounts = {100, 200, 500, 1000, 1500, 2000};

        for (int i = 0; i < cardIds.length; i++) {
            final int amount = amounts[i];
            final TextView txtPaymentUser = findViewById(R.id.txt_payment_user);

            findViewById(cardIds[i]).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Clear previous selection
                    txtPaymentUser.setText("");

                    // Set the text for the clicked card
                    txtPaymentUser.setText(String.valueOf(amount));
                }
            });
        }

        Intent intent = getIntent();
        ngoName = intent.getStringExtra("ngoName");
        String ngoEmail = intent.getStringExtra("ngoEmail");
        String ngoMno = intent.getStringExtra("ngoMno");
        String ngoId = intent.getStringExtra("ngoId");

//        Toast.makeText(this, ngoEmail, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, ngoMno, Toast.LENGTH_SHORT).show();
        txt_user_ngo_name.setText(ngoName);

//DONATE BTN
        BTN_pay_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amt = txt_payment_user.getText().toString();
                if (!isInternetAvailable()) {
                    showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
                    return;
                } else {
                    if (amt.isEmpty()) {
                        // Set an error message
                        showSnackbar(findViewById(android.R.id.content), "Please enter amount...");
                    } else if (amt.length() < 2) {
                        showSnackbar(findViewById(android.R.id.content), "At least donate 10 Rs. ...");
                    } else if (!isValidAmount(amt)) {
                        showSnackbar(findViewById(android.R.id.content), "We can't suggest you to donate this big amount...");
                    } else {
                        // Set the donation amount for Razorpay
                        donationAmount = amt;

                        // Process the donation
                        initiateDonation(ngoName,ngoMno,ngoEmail,amt);
                    }
                }
            }
        });



    }

    //-------------------------------------------------------------------------------------------------------
//    private void initiateDonation() {
//        Intent intent = getIntent();
//        String ngoName = intent.getStringExtra("ngoName");
//        String ngoEmail = intent.getStringExtra("ngoEmail");
//        String ngoMno = intent.getStringExtra("ngoMno");
//
//        // Check if the donation amount is set
//        if (donationAmount != null && !donationAmount.isEmpty()) {
//            List<NGOData> dataList = fetchData(); // Replace with your data-fetching logic
//
//            // Call the initiateDonation method in the adapter
//            NGODataAdapter adapter = new NGODataAdapter(dataList, Donate_payment.this);
//            adapter.initiateDonation(ngoName, ngoMno, ngoEmail, donationAmount);
//        }
//    }

    public void initiateDonation(String ngoName, String ngoMno, String ngoEmail, String donationAmount) {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_iiWet5Chi79qWI"); // Replace with your actual Razorpay key

        try {
            JSONObject options = new JSONObject();
            options.put("name", ngoName);
//            Toast.makeText(this, ngoEmail, Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, ngoMno, Toast.LENGTH_SHORT).show();
            options.put("description", "Sent to " + ngoName);
            options.put("currency", "INR");
            options.put("amount", Integer.parseInt(donationAmount) * 100);
            options.put("prefill.email", sharedPreferences.getString("email",""));
            options.put("prefill.contact", ngoMno);
            options.put("environment", "sandbox"); // "sandbox" or "production"
            options.put("theme.color", "#2e80df");
            options.put("method", new JSONObject().put("upi", true));

            JSONObject notes = new JSONObject();
            notes.put("note1", "Received From "+sharedPreferences.getString("fname",""));
            notes.put("note2",sharedPreferences.getString("mobile","") );
            notes.put("address",sharedPreferences.getString("userId",""));

            options.put("notes", notes);
            checkout.open((Activity)  Donate_payment.this, options);
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

        donor.setText(sharedPreferences.getString("fname",""));
        donationTo.setText(ngoName);
        paymentMethod.setText("Online");
        dateTime.setText(dtf.format(now));
        amount.setText("₹"+donationAmount);
        paymentId.setText(s);
        status.setText("Success");

        fileName="Payment-Receipt-"+new Random().nextInt(1000000);

        completeFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+fileName+".pdf";


        PdfGenerator.getBuilder()
                .setContext(Donate_payment.this)
                .fromViewSource()
                .fromView(view)
                .setFileName(fileName)
                .setFolderNameOrPath(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)))
                .actionAfterPDFGeneration(PdfGenerator.ActionAfterPDFGeneration.OPEN)
                .build(new PdfGeneratorListener() {
                    @Override
                    public void onFailure(FailureResponse failureResponse) {
                        super.onFailure(failureResponse);
                        Toast.makeText(Donate_payment.this, "Failed To Download Payment Receipt. ", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Donate_payment.this, "Payment Receipt Downloaded Successfully.", Toast.LENGTH_SHORT).show();
                        sendPaymentMail();
                    }
                });
    }


    @Override
    public void onPaymentSuccess(String s) {
        // Handle payment success
        showToast("Payment Successful");

        generateAndDownloadBill(s);

    }

    @Override
    public void onPaymentError(int i, String s) {
        // Handle payment failure
        showToast("Payment Declined");
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
                                Toast.makeText(Donate_payment.this, "Error Occurred : ", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(Donate_payment.this, "Error Occurred : " + e, Toast.LENGTH_SHORT).show();
        }

        catch (MessagingException e) {
            // Handling messaging exception (e.g. network error)
            Toast.makeText(Donate_payment.this, "Error Occurred : " + e, Toast.LENGTH_SHORT).show();
        }

        catch (UnsupportedEncodingException e) {
            Toast.makeText(Donate_payment.this, "Error Occurred : " + e, Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(Donate_payment.this, "Payment Receipt Sent On Your Registered Email Id !!", Toast.LENGTH_SHORT).show();

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


    // Helper method to validate Amount
    private boolean isValidAmount(String number) {
        // Validate if the amount is less than or equal to 5 digits
        String numberPattern = "\\b\\d{1,5}\\b";
        return number.matches(numberPattern);
    }

//    //HIDE THE KEYBOARD
//    private void hideSoftKeyboard(View view) {
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//    }

    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Donate_payment.this, text, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(this);
        finish();
    }

}