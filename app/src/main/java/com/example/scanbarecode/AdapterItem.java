package com.example.scanbarecode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterItem extends RecyclerView.Adapter<AdapterItem.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList barcode, name, seller, quantity, date, buyingPrice, sellingPrice;
    private ItemClickListener itemClickListener;

    AdapterItem(Activity activity, Context context, ArrayList barcode, ArrayList name, ArrayList seller,
                ArrayList date, ArrayList quantity, ArrayList buyingPrice, ArrayList sellingPrice) {
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

    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
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
        holder.sellingPriceView.setText(String.valueOf(sellingPrice.get(position)));
        holder.quantityView.setText(String.valueOf(quantity.get(position)));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    // Get the barcode of the clicked item
                    Long barcode = (Long) AdapterItem.this.barcode.get(position);
                    itemClickListener.onItemClick(barcode);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Long barcode = (Long) AdapterItem.this.barcode.get(position);
                itemClickListener.onItemClick(barcode);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return barcode.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itemNameView, sellingPriceView, quantityView;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameView = itemView.findViewById(R.id.ItemNameView);
            sellingPriceView = itemView.findViewById(R.id.SellingPriceView);
            quantityView = itemView.findViewById(R.id.QuantityView);

        }

    }

    public interface ItemClickListener {
        void onItemClick(Long barcode);
    }
}
