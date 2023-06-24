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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class ProductsList extends AppCompatActivity {

    RecyclerView recyclerView;
    dataBase myDB;
    ArrayList<String> name, seller, date;
    ArrayList<Integer> quantity, buyingPrice, sellingPrice;
    ArrayList<Long> barcode;
    AdapterItem customAdapter;
    Button ScanBare;
    EditText SearchItem;
    private Long barcodeToSearch;

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
        ScanBare= findViewById(R.id.ScanBare);
        SearchItem= findViewById(R.id.SearchItem);



        ScanBare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBarcodeScanning();
            }
        });

        SearchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this implementation
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                barcode.clear();
                name.clear();
                seller.clear();
                date.clear();
                quantity.clear();
                buyingPrice.clear();
                sellingPrice.clear();

                String searchText = charSequence.toString().trim();
                storeDataItem(searchText);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed for this implementation
            }
        });


    customAdapter = new AdapterItem(ProductsList.this, this, barcode, name, seller, date, quantity, buyingPrice, sellingPrice);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProductsList.this));
        storeDataItem("");

    }

    private void startBarcodeScanning() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan a barcode");  // Optional: Set a prompt message
        integrator.setOrientationLocked(false);
        integrator.setCaptureActivity(Capture.class);// Optional: Lock the orientation
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            String scannedBarcode = result.getContents();
            barcodeToSearch = Long.parseLong(scannedBarcode);
            storeDataInArrays(barcodeToSearch);
            customAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Scan failed", Toast.LENGTH_SHORT).show();
        }
    }

    void storeDataInArrays(Long barcode) {
        Cursor cursor = myDB.readData(barcode);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "non trouver", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                this.barcode.add(cursor.getLong(0));
                name.add(cursor.getString(1));
                seller.add(cursor.getString(2));
                date.add(cursor.getString(3));
                quantity.add(cursor.getInt(6));
                buyingPrice.add(cursor.getInt(4));
                sellingPrice.add(cursor.getInt(5));
            }
        }
    }
    void storeDataItem(String item) {

        Cursor cursor = myDB.readDataItem(item);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show();
        } else {

            while (cursor.moveToNext()) {
                this.barcode.add(cursor.getLong(0));
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