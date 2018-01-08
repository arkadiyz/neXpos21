package com.arkadiy.enter.imenu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Arkadiy on 1/4/2018.
 */

public class ProductListAdapter extends ArrayAdapter<Product> {


    private Context mContext;
    private int mResource;
    private int lastPosition;

    private static final String TAG = "PersonListAdapter";
    /**
     * Holds variables in a View
     */



    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */

    public ProductListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Product> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Get the product information
        String name = getItem(position).getProductName();
        String amount = getItem(position).getAmount();
        String price = getItem(position).getPrice();

        //Create the product object with the informtion

        Product product = new Product(name,amount,price);

        LayoutInflater inflater = LayoutInflater.from(mContext);

        convertView =inflater.inflate(mResource,parent,false);

        TextView tvProduct = (TextView)convertView.findViewById(R.id.textViewProductName);
        TextView tvAmount = (TextView)convertView.findViewById(R.id.textViewProductAmount);
        TextView tvPrice = (TextView)convertView.findViewById(R.id.textViewProductPrice);

        tvProduct.setText(name);
        tvAmount.setText(amount);
        tvPrice.setText(price);
        return convertView;
    }
}
