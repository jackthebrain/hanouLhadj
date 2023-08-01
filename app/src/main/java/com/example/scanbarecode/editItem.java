package com.example.scanbarecode;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class editItem extends AppCompatActivity {
    private EditText ItemName, ItemSeller, BuyingPrice, SellingPrice, Quantity,Date;
    private TextView BareCode;
    private Button addItem, scanCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        BareCode = findViewById(R.id.barecode);
        ItemName = findViewById(R.id.ItemName);
        ItemSeller = findViewById(R.id.ItemSeller);
        BuyingPrice = findViewById(R.id.BuyingPrice);
        SellingPrice = findViewById(R.id.SellingPrice);
        Quantity = findViewById(R.id.Quantity);
        addItem = findViewById(R.id.addItem);
        scanCode = findViewById(R.id.scanCode);
        Date = findViewById(R.id.Date);
    }
    private void storeDataInArrays(long barcode) {
        dataBase myDB = new dataBase(this);
        Cursor cursor = myDB.readData(barcode);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Not found", Toast.LENGTH_SHORT).show();
        } else {
            if (cursor.moveToFirst()) {
                BareCode.setText(String.valueOf(cursor.getLong(0)));
                ItemName.setText(cursor.getString(1));
                ItemSeller.setText(cursor.getString(2));
                Date.setText(cursor.getString(3));
                Quantity.setText(String.valueOf(cursor.getInt(6)));
                BuyingPrice.setText(String.valueOf(cursor.getInt(4)));
                SellingPrice.setText(String.valueOf(cursor.getInt(5)));
            }
        }
    }
}