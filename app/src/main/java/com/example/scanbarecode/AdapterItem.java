package com.example.scanbarecode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterItem extends RecyclerView.Adapter<AdapterItem.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList barcode, name, seller, quantity,date,buyingPrice,sellingPrice;

    AdapterItem(Activity activity, Context context, ArrayList barcode, ArrayList name, ArrayList seller,
                ArrayList date, ArrayList quantity, ArrayList buyingPrice, ArrayList sellingPrice){
        this.activity = activity;
        this.context = context;
        this.barcode = barcode;
        this.name = name;
        this.seller = seller;
        this.date = date;
        this.quantity = quantity;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.itemNameView.setText(String.valueOf(name.get(position)));
        holder.SellingPriceView.setText(String.valueOf(sellingPrice.get(position)));
        holder.QuantityView.setText(String.valueOf(quantity.get(position)));
    }

    @Override
    public int getItemCount() {
        return barcode.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itemNameView, SellingPriceView, QuantityView;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameView= itemView.findViewById(R.id.ItemNameView);
            SellingPriceView = itemView.findViewById(R.id.SellingPriceView);
            QuantityView = itemView.findViewById(R.id.QuantityView);
        }

    }

}
