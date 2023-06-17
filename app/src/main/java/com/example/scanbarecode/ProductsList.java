package com.example.scanbarecode;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ProductsList extends AppCompatActivity {

    RecyclerView recyclerView;
    dataBase myDB;
    ArrayList<String> name, seller, date;
    ArrayList<Integer> quantity, buyingPrice, sellingPrice;
    ArrayList<Long> barcode;
    AdapterItem customAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        recyclerView = findViewById(R.id.recyclerView);


        myDB = new dataBase(ProductsList.this);
        name = new ArrayList<>();
        seller = new ArrayList<>();
        date = new ArrayList<>();
        quantity = new ArrayList<>();
        buyingPrice = new ArrayList<>();
        sellingPrice = new ArrayList<>();
        barcode = new ArrayList<>();


        storeDataInArrays();

        customAdapter = new AdapterItem( ProductsList.this,  this,  barcode,  name,  seller,
                 date,  quantity,  buyingPrice,  sellingPrice);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProductsList.this));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            storeDataInArrays();
            customAdapter.notifyDataSetChanged();
        }
    }

    void storeDataInArrays(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){
        }else{
            while (cursor.moveToNext()){
                barcode.add(cursor.getLong(0));
                name.add(cursor.getString(1));
                seller.add(cursor.getString(2));
                date.add(cursor.getString(3));
                quantity.add(cursor.getInt(6));
                buyingPrice.add(cursor.getInt(4));
                sellingPrice.add(cursor.getInt(5));
            }
        }
    }


}