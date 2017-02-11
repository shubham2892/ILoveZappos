package com.intern.zappos.Fragments;

import com.intern.zappos.Activities.MainActivity;
import com.intern.zappos.POJOs.Product;
import com.intern.zappos.R;
import com.intern.zappos.databinding.FragmentProductBinding;

import android.app.Fragment;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ShareActionProvider;

public class ProductFragment extends Fragment implements View.OnClickListener {

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
        // Locate MenuItem with ShareActionProvider
        Button item = (Button) view.findViewById(R.id.menu_item_share);

        item.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.menu_item_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Check out this awesome product:" + mProduct.getProductUrl();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mProduct.getProductName());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
        }
    }
}
