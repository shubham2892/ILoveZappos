package com.intern.zappos.Fragments;

import com.intern.zappos.Activities.MainActivity;
import com.intern.zappos.GlobalVariables;
import com.intern.zappos.POJOs.Product;
import com.intern.zappos.POJOs.ProductSearch;
import com.intern.zappos.R;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityFragment extends Fragment implements View.OnClickListener {


    private EditText searchQuery;

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
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_button_local:
                Call<ProductSearch> productSearchCall = ((MainActivity) getActivity())
                        .getRetrofit().groupList(searchQuery.getText().toString(), GlobalVariables.KEY);
                Log.d("sg","Searching for product");
                productSearchCall.enqueue(new Callback<ProductSearch>() {
                    @Override
                    public void onResponse(Call<ProductSearch> call, Response<ProductSearch> response) {
                        Log.d("sg","Response Received");
                        if (response.isSuccessful()){
                            // set the product object
                            ProductSearch productSearch= response.body();
                            Product firstProduct = productSearch.getProducts().get(0);
                            ((MainActivity) getActivity()).setProduct(firstProduct);
                            // Show the product page
                            ((MainActivity) getActivity()).showProductFragment();

                            ProductFragment fragment = ProductFragment.newInstance();
                            getFragmentManager().beginTransaction().add(R.id.frame_base,fragment,
                                    "PRODUCT").commit();

                        }else{
                            // Show the error message

                        }
                    }

                    @Override
                    public void onFailure(Call<ProductSearch> call, Throwable t) {
                        // Show a error message
                        Log.d("sg","Network call failed");
                    }
                });
        }
    }
}
