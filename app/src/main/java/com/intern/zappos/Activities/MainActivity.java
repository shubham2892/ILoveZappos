package com.intern.zappos.Activities;

import com.intern.zappos.Fragments.MainActivityFragment;
import com.intern.zappos.Network.RestAPI;
import com.intern.zappos.POJOs.Product;
import com.intern.zappos.R;
import com.squareup.picasso.Picasso;

import android.app.Fragment;
import android.databinding.BindingAdapter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity  {

    RestAPI service;
    Product mProduct;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final OvershootInterpolator interpolator = new OvershootInterpolator();

                ViewCompat.animate(view).setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {

                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        ((FloatingActionButton)view).hide();
                        // Add item to the cart here. Actually nothing happening, just showing
                        // the added to cart message with animation
                        Snackbar.make(view, "Item added to cart", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        view.setRotation(360f);
                    }

                    @Override
                    public void onAnimationCancel(View view) {

                    }
                }).rotation(90f).withLayer().setDuration(400).setInterpolator(interpolator).start();

            }
        });
        // Checking if saved Instance already exists
        if (savedInstanceState == null) {
            Fragment mainActivityFragment = MainActivityFragment.newInstance();
            getFragmentManager().beginTransaction().add(R.id.frame_base, mainActivityFragment, "MAIN_FRAGMENT").commit();
        } else {
            setProduct((Product) savedInstanceState.getParcelable("product"));
        }
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String url) {
        Picasso.with(view.getContext()).load(url).into(view);
    }

    public RestAPI getRetrofit() {
        if (service == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            httpClient.addInterceptor(logging);
            Retrofit mRetrofit = new Retrofit.Builder()
                    .baseUrl("https://api.zappos.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
            service = mRetrofit.create(RestAPI.class);
        }
        return service;
    }


    public Product getProduct() {
        // Make sure this is always called after setProduct
        if (mProduct == null) {
            throw new NullPointerException();
        }
        return mProduct;
    }

    public void setProduct(Product product) {
        mProduct = product;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putParcelable("product", getProduct());
        super.onSaveInstanceState(outState, outPersistentState);

    }

    public void showFab(){
        fab.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Hiding the fab button as it should only be visible on product page.
        // This would work because we are dealing with only two fragments and on backpress of main
        // fragment app would close.
        fab.hide();
    }
}
