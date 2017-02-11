package com.intern.zappos.Fragments;

import com.intern.zappos.Activities.MainActivity;
import com.intern.zappos.GlobalVariables;
import com.intern.zappos.POJOs.Product;
import com.intern.zappos.POJOs.ProductSearch;
import com.intern.zappos.R;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityFragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener {


    private EditText searchQuery;
    private ProgressBar progressBar;

    public static MainActivityFragment newInstance() {
        return new MainActivityFragment();
    }

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button searchButton = (Button) view.findViewById(R.id.search_button_local);
        searchButton.setOnClickListener(this);

        searchQuery = (EditText) view.findViewById(R.id.edit_query);
        searchQuery.setOnEditorActionListener(this);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
    }

    void searchProduct(final View view){
        Call<ProductSearch> productSearchCall = ((MainActivity) getActivity())
                .getRetrofit().groupList(searchQuery.getText().toString(), GlobalVariables.KEY);
        // Show the progress bar, as response to button click
        progressBar.setVisibility(View.VISIBLE);
        productSearchCall.enqueue(new Callback<ProductSearch>() {
            @Override
            public void onResponse(Call<ProductSearch> call, Response<ProductSearch> response) {
                // hide the progress bar as request complete
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    // set the product object
                    ProductSearch productSearch = response.body();
                    // Checking if product count is more than 0, else show error message
                    if (productSearch.getCurrentResultCount() > 0) {
                        // get the first product
                        Product firstProduct = productSearch.getProducts().get(0);
                        // set the product object in mainactivity
                        ((MainActivity) getActivity()).setProduct(firstProduct);
                        // show the fab button
                        ((MainActivity) getActivity()).showFab();

                        // Hide the keyboard before inflating new fragment
                        InputMethodManager imm = (InputMethodManager)
                                getActivity().getSystemService(Context
                                        .INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                        // Show the product fragment
                        ProductFragment fragment = ProductFragment.newInstance();
                        getFragmentManager().beginTransaction().add(R.id.frame_base, fragment,
                                "PRODUCT").addToBackStack("PRODUCT").commit();
                    } else {
                        Snackbar.make(view, "No Product found with this search term",
                                Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }

                } else {
                    // Show the error message
                    Snackbar.make(view, "Something wrong with your network", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }


            @Override
            public void onFailure(Call<ProductSearch> call, Throwable t) {
                // Show a error message
                // hide the progress bar as request complete
                progressBar.setVisibility(View.GONE);
                Snackbar.make(view, "Please check your internet connection", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.search_button_local:
                if (searchQuery.getText().toString().trim().length() > 0) {
                   searchProduct(view);
                } else {
                    Snackbar.make(view, "Enter a term", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            searchProduct(textView);
            handled = true;
        }
        return handled;
    }
}
