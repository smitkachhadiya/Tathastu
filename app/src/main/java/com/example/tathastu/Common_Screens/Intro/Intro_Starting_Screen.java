package com.example.tathastu.Common_Screens.Intro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.Common_Screens.After_Intro_Start;
import com.example.tathastu.Common_Screens.Selection_Screen;
import com.example.tathastu.Common_Screens.Splash_Screen;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Entry.Login_Screen;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Intro_Starting_Screen extends AppCompatActivity {
    public ViewPager viewPager;
    public LinearLayout lineardot;
    public FloatingActionButton btnnext;
    public ExtendedFloatingActionButton btnget,btnskip;
    public com.example.tathastu.Common_Screens.Intro.intropref intropref;
    public int[] layouts;
    public TextView[] dots;
    public Myviewpageradapter myviewpageradapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_starting_screen);

        intropref = new intropref(this);
        if (!intropref.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }
        viewPager = findViewById(R.id.viewpager);
        lineardot = findViewById(R.id.lineardot);
        btnnext = findViewById(R.id.BTN_intro_first );
        btnget = findViewById(R.id.BTN_intro_get );
        btnskip = findViewById(R.id.BTN_intro_skip );

        btnget.setVisibility(View.INVISIBLE);

        layouts = new int[]{
                R.layout.intro_one,
                R.layout.intro_two,
                R.layout.intro_three
        };
        btnget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the login screen
                launchHomeScreen();
            }
        });
        btnskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    // Set the ViewPager position to the third screen
                    viewPager.setCurrentItem(layouts.length - 1);

                    // Update the visibility of buttons
                    btnnext.setVisibility(View.GONE);
                    btnget.setVisibility(View.VISIBLE);

            }
        });
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = getitem(+1);
                if (current < layouts.length) {
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });



        myviewpageradapter = new Myviewpageradapter();
        viewPager.setAdapter(myviewpageradapter);
        viewPager.addOnPageChangeListener(onPageChangeListener);
        addbottomdots(0);
    }

    ViewPager.OnPageChangeListener onPageChangeListener=new ViewPager.OnPageChangeListener() {


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addbottomdots(position);

            if (position == layouts.length - 1) {
                // Third screen is displayed
                btnnext.setVisibility(View.GONE);
                btnget.setVisibility(View.VISIBLE);
                btnskip.setVisibility(View.INVISIBLE); // Set "Skip" button to invisible
            } else {
                // First or second screen is displayed
                btnnext.setVisibility(View.VISIBLE);
                btnget.setVisibility(View.INVISIBLE);
                btnskip.setVisibility(View.VISIBLE); // Set "Skip" button to visible
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }


    };
    private void addbottomdots(int currentpage){
        dots=new TextView[layouts.length];
        int[] activecolors= getResources().getIntArray(R.array.active);
        int[] inactivecolors= getResources().getIntArray(R.array.inactive);
        lineardot.removeAllViews();

        for (int i=0;i<dots.length;i++){
            dots[i] =new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(50);
            dots[i].setTextColor(inactivecolors[currentpage]);
            lineardot.addView(dots[i]);
        }
        if (dots.length>0){
            dots[currentpage].setTextColor(activecolors[currentpage]);
        }
    }
    public class Myviewpageradapter extends PagerAdapter {
        LayoutInflater inflater;
        public  Myviewpageradapter(){

        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view= inflater.inflate(layouts[position],container,false);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view= (View) object;
            container.removeView(view);
        }
    }

    private int getitem(int i) {
        return viewPager.getCurrentItem() + 1;
    }

    private void launchHomeScreen() {
        intropref.setIsFirstTimeLaunch(false);
        Intent i = new Intent(Intro_Starting_Screen.this, After_Intro_Start.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        Animatoo.INSTANCE.animateSlideLeft(Intro_Starting_Screen.this);
        finish();
    }
}