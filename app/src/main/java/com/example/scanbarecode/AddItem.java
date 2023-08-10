package com.example.scanbarecode;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddItem extends AppCompatActivity {

    private EditText ItemName, ItemSeller, BuyingPrice, SellingPrice, Quantity;
    private TextView BareCode;
    private Button addItem;
    private ImageView scanCode;
    private long bareCode;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        BareCode = findViewById(R.id.barecode);
        ItemName = findViewById(R.id.ItemName);
        ItemSeller = findViewById(R.id.ItemSeller);
        BuyingPrice = findViewById(R.id.BuyingPrice);
        SellingPrice = findViewById(R.id.SellingPrice);
        Quantity = findViewById(R.id.Quantity);
        addItem = findViewById(R.id.saveEdit);
        scanCode = findViewById(R.id.scanBare);

        // Getting current date
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(currentDate);

        // Scan barcode
        scanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBarcodeScanning();
            }
        });


        // Add item
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bareCodeString = BareCode.getText().toString().trim();
                String itemName = ItemName.getText().toString().trim();
                String itemSeller = ItemSeller.getText().toString().trim();
                String buyingPriceString = BuyingPrice.getText().toString().trim();
                String sellingPriceString = SellingPrice.getText().toString().trim();
                String quantityString = Quantity.getText().toString().trim();

                if (bareCodeString.isEmpty() || itemName.isEmpty() || itemSeller.isEmpty() ||
                        buyingPriceString.isEmpty() || sellingPriceString.isEmpty() || quantityString.isEmpty()) {
                    // At least one field is empty, show an error message or a Toast
                    Toast.makeText(AddItem.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return; // Don't proceed further
                }

                // If all fields are filled, continue with adding the item to the database
                long bareCode = Long.parseLong(bareCodeString);
                int buyingPrice = Integer.parseInt(buyingPriceString);
                int sellingPrice = Integer.parseInt(sellingPriceString);
                int quantity = Integer.parseInt(quantityString);

                dataBase myDB = new dataBase(AddItem.this);
                item newItem = new item(bareCode, itemName, itemSeller, formattedDate, buyingPrice, sellingPrice, quantity);
                myDB.addItem(AddItem.this, newItem);
            }
        });
    }

    private void startBarcodeScanning() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan a barcode");  // Optional: Set a prompt message
        integrator.setOrientationLocked(true);
        integrator.setCaptureActivity(Capture.class);// Optional: Lock the orientation
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            BareCode.setText(result.getContents());
        } else {
            Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
        }
    }
}