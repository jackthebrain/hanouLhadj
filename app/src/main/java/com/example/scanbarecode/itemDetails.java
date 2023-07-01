package com.example.scanbarecode;

import static android.content.ContentValues.TAG;



import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class itemDetails extends AppCompatActivity {

    private TextView quantity, Barcode, itemSeller, buyingPrice, sellingPrice, date, itemName;
    private dataBase myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        quantity = findViewById(R.id.quantity);
        Barcode = findViewById(R.id.Barecode); // Updated ID
        sellingPrice = findViewById(R.id.sellingPrice);
        itemSeller = findViewById(R.id.itemSeller);
        itemName = findViewById(R.id.itemName);
        date = findViewById(R.id.date);
        buyingPrice = findViewById(R.id.buyingPrice);

        Intent intent = getIntent();
        String barcodeString = intent.getStringExtra("barecode");
        long barcodeValue = Long.parseLong(barcodeString);
        storeDataInArrays(barcodeValue);

    }

    private void storeDataInArrays(long barcode) {
        myDB = new dataBase(this);

        Cursor cursor = myDB.readData(barcode);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Not found", Toast.LENGTH_SHORT).show();
        } else {
            if (cursor.moveToFirst()) {
                Barcode.setText(String.valueOf(cursor.getLong(0)));
                itemName.setText(cursor.getString(1));
                itemSeller.setText(cursor.getString(2));
                date.setText(cursor.getString(3));
                quantity.setText(String.valueOf(cursor.getInt(6)));
                buyingPrice.setText(String.valueOf(cursor.getInt(4)));
                sellingPrice.setText(String.valueOf(cursor.getInt(5)));
            }
        }
    }
}