package com.example.scanbarecode;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class ProductsList extends AppCompatActivity {

    RecyclerView recyclerView;
    dataBase myDB;
    AdapterItem customAdapter;
    Button ScanBare;
    EditText searchItem;
    private Long barcodeToSearch;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        recyclerView = findViewById(R.id.recyclerView);

        myDB = new dataBase(ProductsList.this);
        ScanBare = findViewById(R.id.ScanBare);
        searchItem = findViewById(R.id.serachText);
        searchItem.clearFocus();

        searchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No implementation needed
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Filter the list based on the entered text
                listFiltered(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No implementation needed
            }
        });

        ScanBare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBarcodeScanning();
            }
        });

        List<item> itemList = myDB.readAllData();
        customAdapter = new AdapterItem(ProductsList.this, this, itemList);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProductsList.this));

        customAdapter.setItemClickListener(new AdapterItem.ItemClickListener() {
            @Override
            public void onItemClick(Long barcode) {
                Intent intent = new Intent(ProductsList.this, itemDetails.class);
                intent.putExtra("barcode", String.valueOf(barcode));
                startActivity(intent);
            }
        });

        customAdapter.setItemLongClickListener(new AdapterItem.ItemLongClickListener() {
            @Override
            public void onItemLongClick(Long barcode) {

            }
        });
    }

    private void listFiltered(String text) {
        List<item> filteredList = new ArrayList<>();
        List<item> itemList = myDB.readAllData();
        for (item item : itemList) {
            if (item.getArticleName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No products", Toast.LENGTH_SHORT).show();
            filteredList.clear();
            customAdapter.filtredList(filteredList);
        } else {
            customAdapter.filtredList(filteredList);
        }
    }

    private void startBarcodeScanning() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan a barcode");
        integrator.setOrientationLocked(false);
        integrator.setCaptureActivity(Capture.class);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            String scannedBarcode = result.getContents();
            Long barcodeToSearch = Long.parseLong(scannedBarcode);
            Intent intent = new Intent(ProductsList.this, itemDetails.class);
            intent.putExtra("barcode", String.valueOf(barcodeToSearch));
            startActivity(intent);
        } else {
            Toast.makeText(this, "Scan failed", Toast.LENGTH_SHORT).show();
        }
    }
}