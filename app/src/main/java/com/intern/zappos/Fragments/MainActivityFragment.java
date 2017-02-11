package com.intern.zappos.Fragments;

import com.intern.zappos.Activities.MainActivity;
import com.intern.zappos.GlobalVariables;
import com.intern.zappos.POJOs.Product;
import com.intern.zappos.POJOs.ProductSearch;
import com.intern.zappos.R;

import android.support.annotation.Nullable;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityFragment extends Fragment implements View.OnClickListener {


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
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
    }


    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.search_button_local:
                if (searchQuery.getText().toString().trim().length() > 0) {
                    Call<ProductSearch> productSearchCall = ((MainActivity) getActivity())
                            .getRetrofit().groupList(searchQuery.getText().toString(), GlobalVariables.KEY);

                    progressBar.setVisibility(View.VISIBLE);
                    productSearchCall.enqueue(new Callback<ProductSearch>() {
                        @Override
                        public void onResponse(Call<ProductSearch> call, Response<ProductSearch> response) {
                            progressBar.setVisibility(View.GONE);
                            if (response.isSuccessful()) {
                                // set the product object
                                ProductSearch productSearch = response.body();
                                if (productSearch.getCurrentResultCount() > 0) {
                                    Product firstProduct = productSearch.getProducts().get(0);
                                    ((MainActivity) getActivity()).setProduct(firstProduct);
                                    // Show the product fragment
                                    ((MainActivity) getActivity()).showProductFragment();

                                    ProductFragment fragment = ProductFragment.newInstance();
                                    getFragmentManager().beginTransaction().add(R.id.frame_base, fragment,
                                            "PRODUCT").addToBackStack("PRODUCT").commit();
                                } else {
                                    // Convert this to snackbar
//                                    Toast.makeText(getActivity(), "No Product found with this search " +
//                                            "term", Toast.LENGTH_SHORT).show();
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
                            Snackbar.make(view, "Please check your internet connection", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    });
                } else {
                    Snackbar.make(view, "Enter a term", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

        }
    }
}
