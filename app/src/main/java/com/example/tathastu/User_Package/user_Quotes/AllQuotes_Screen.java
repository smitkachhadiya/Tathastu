package com.example.tathastu.User_Package.user_Quotes;


import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.RewindAnimationSetting;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.util.List;

public class AllQuotes_Screen extends AppCompatActivity implements SwipeAdapter.SwipeAdapterListener, ConnectivityReceiver.ConnectivityReceiverListener {
    private CardStackView cardStackView;
    private CardStackAdapter cardStackAdapter;
    private ConnectivityReceiver connectivityReceiver;
    ExtendedFloatingActionButton BTN_play, BTN_stop, BTN_previous, BTN_refresh;

    private Handler handler;
    private Runnable swipeRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_quotes_screen);

        BTN_play = findViewById(R.id.BTN_play);
        BTN_stop = findViewById(R.id.BTN_stop);
        BTN_previous = findViewById(R.id.BTN_previous);
        BTN_refresh = findViewById(R.id.BTN_refresh);
        cardStackView = findViewById(R.id.card_stack_view);
        CardStackLayoutManager layoutManager = new CardStackLayoutManager(this);
        cardStackView.setLayoutManager(layoutManager);

        FloatingActionButton BTN_back=findViewById(R.id.BTN_back);
        //BACK
        BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i =new Intent(AllQuotes_Screen.this, DashBoard_Screen.class);
//                startActivity(i);
                finish();
                Animatoo.INSTANCE.animateSlideRight(AllQuotes_Screen.this);
            }
        });


        // Initialize the ConnectivityReceiver
        connectivityReceiver = new ConnectivityReceiver();
        ConnectivityReceiver.connectivityReceiverListener = this;

        // Register the receiver to listen for connectivity changes
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        // CUSTOMIZATION
        // Set your desired stack and animation settings
        layoutManager.setStackFrom(StackFrom.Top);
        layoutManager.setTranslationInterval(12.0f);
        layoutManager.setScaleInterval(0.90f);
        SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                .setDirection(Direction.Top)
                .setDuration(Duration.Slow.duration)
                .build();
        layoutManager.setSwipeAnimationSetting(setting);
        layoutManager.setDirections(Direction.FREEDOM);

        SwipeAdapter swipeAdapter = new SwipeAdapter(this, this);
        swipeAdapter.fetchQuotes();

        handler = new Handler(Looper.getMainLooper());

        // PLAY
        BTN_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInternetAvailable()) {
                    showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
                    return;
                } else {
                    startSwiping();
                    showSnackbar(findViewById(android.R.id.content), "Play");
                }
            }
        });

        // STOP
        BTN_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInternetAvailable()) {
                    showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
                    return;
                } else {
                    stopSwiping();
                    showSnackbar(findViewById(android.R.id.content), "Stop");
                }
            }
        });

        // PREVIOUS
        BTN_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInternetAvailable()) {
                    showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
                    return;
                } else {
                    RewindAnimationSetting setting = new RewindAnimationSetting.Builder()
                            .setDirection(Direction.Top)
                            .setDuration(Duration.Slow.duration)
                            .build();
                    layoutManager.setRewindAnimationSetting(setting);
                    cardStackView.rewind();
                }

            }
        });

        BTN_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isInternetAvailable()) {
                    showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
                    return;
                } else {// Clear any existing data
                    swipeAdapter.clearData();

                    // Fetch new quotes
                    swipeAdapter.fetchQuotes();

                    // Reset CardStackView to the first position
                    if (cardStackAdapter.getItemCount() > 0) {
                        cardStackView.scrollToPosition(0);
                    }

                }

            }
        });


    }

//-------------------------------------------------------------------------------------------------

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

    private void startSwiping() {

        swipeRunnable = new Runnable() {
            @Override
            public void run() {
                cardStackView.swipe();
                handler.postDelayed(this, 3000); // Delay for 1000 milliseconds (adjust as needed)
            }
        };
        handler.postDelayed(swipeRunnable, 3000); // Initial delay before the first swipe
    }

    private void stopSwiping() {
        if (swipeRunnable != null) {
            handler.removeCallbacks(swipeRunnable);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(this);
        finish();
    }

    @Override
    public void onQuotesFetched(List<itemmodel> quotes) {
        // Set quotes to the adapter
        cardStackAdapter = new CardStackAdapter(quotes);
        cardStackView.setAdapter(cardStackAdapter);
    }
}
