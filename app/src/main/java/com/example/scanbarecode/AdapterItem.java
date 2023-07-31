package com.example.scanbarecode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterItem extends RecyclerView.Adapter<AdapterItem.MyViewHolder> {

    private Context context;
    private Activity activity;
    private List<item> itemList;
    private ItemClickListener itemClickListener;
    private ItemLongClickListener itemLongClickListener;

    AdapterItem(Activity activity, Context context, List<item> itemList) {
        this.activity = activity;
        this.context = context;
        this.itemList = itemList;
    }

    public void filtredList(List<item> filtredList) {
        this.itemList = filtredList;
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setItemLongClickListener(ItemLongClickListener listener) {
        this.itemLongClickListener = listener;
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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        item currentItem = itemList.get(position);

        holder.itemNameView.setText(currentItem.getArticleName());
        holder.sellingPriceView.setText(String.valueOf(currentItem.getSellingprice()));
        holder.quantityView.setText(String.valueOf(currentItem.getQuantity()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    long barcode = currentItem.getBarecode();
                    itemClickListener.onItemClick(barcode);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (itemLongClickListener != null) {
                    long barcode = currentItem.getBarecode();
                    itemLongClickListener.onItemLongClick(barcode);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itemNameView, sellingPriceView, quantityView;

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

    public interface ItemLongClickListener {
        void onItemLongClick(Long barcode);
    }
}
