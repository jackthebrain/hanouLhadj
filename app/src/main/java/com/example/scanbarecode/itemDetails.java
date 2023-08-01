package com.example.scanbarecode;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class itemDetails extends AppCompatActivity {

    private TextView quantity, Barcode, itemSeller, buyingPrice, sellingPrice, date, itemName;
    private Button deleteImage;
    dataBase myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        quantity = findViewById(R.id.quantity);
        Barcode = findViewById(R.id.Barecode);
        sellingPrice = findViewById(R.id.sellingPrice);
        itemSeller = findViewById(R.id.itemSeller);
        itemName = findViewById(R.id.itemName);
        date = findViewById(R.id.date);
        buyingPrice = findViewById(R.id.buyingPrice);
        deleteImage = findViewById(R.id.deleteImage);
        

        Intent intent = getIntent();
        String barcodeString = intent.getStringExtra("barcode");
        long barcodeValue = Long.parseLong(barcodeString);
        storeDataInArrays(barcodeValue);

        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(itemDetails.this);
                builder.setTitle("suppression article")
                        .setMessage("vous êtes sûr ?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                myDB = new dataBase(itemDetails.this);
                                myDB.deleteItem(barcodeValue);
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

    }

    private void storeDataInArrays(long barcode) {
        dataBase myDB = new dataBase(this);
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