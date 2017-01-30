package com.intern.zappos.Fragments;

import com.intern.zappos.Activities.MainActivity;
import com.intern.zappos.POJOs.Product;
import com.intern.zappos.R;
import com.intern.zappos.databinding.FragmentProductBinding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProductFragment extends Fragment {

    Product mProduct;
    public ProductFragment() {

    }

    public static ProductFragment newInstance() {
        return new ProductFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("sg","Product fragment created");
        FragmentProductBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_product, container, false);
        mProduct = ((MainActivity)getActivity()).getProduct();
        View view = binding.getRoot();
        binding.setProduct(mProduct);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
