package com.example.scanbarecode;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

class dataBase extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "hanoutLhaj.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "hanout";
    private static final String COLUMN_BARCODE = "barecode";
    private static final String COLUMN_NAME = "articleName";
    private static final String COLUMN_SELLER = "seller";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_BUYINGPRICE = "buyingprice";
    private static final String COLUMN_SELLINGPRICE = "sellingprice";
    private static final String COLUMN_QUANTITY = "quantity";

    dataBase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_BARCODE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_SELLER + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_BUYINGPRICE + " INTEGER, " +
                COLUMN_SELLINGPRICE + " INTEGER, " +
                COLUMN_QUANTITY + " INTEGER);";
        db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addItem(Activity activity, item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_BARCODE, item.getBarecode());
        cv.put(COLUMN_QUANTITY, item.getQuantity());
        cv.put(COLUMN_NAME, item.getArticleName());
        cv.put(COLUMN_SELLER, item.getSeller());
        cv.put(COLUMN_DATE, item.getDate());
        cv.put(COLUMN_BUYINGPRICE, item.getBuyingprice());
        cv.put(COLUMN_SELLINGPRICE, item.getSellingprice());

        try {
            long result = db.insertOrThrow(TABLE_NAME, null, cv);
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
            // Return to previous activity
            activity.finish();
        } catch (SQLiteConstraintException e) {
            Toast.makeText(context, "Item with the same barcode already exists!", Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
        }
    }

    List<item> readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        List<item> itemList = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                long barcode = cursor.getLong(0);
                String articleName = cursor.getString(1);
                String seller = cursor.getString(2);
                String date = cursor.getString(3);
                int quantity = cursor.getInt(5);
                int buyingPrice = cursor.getInt(4);
                int sellingPrice = cursor.getInt(6);

                item item = new item(barcode, articleName, seller, date, buyingPrice, sellingPrice, quantity);
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return itemList;
    }
    Cursor readData(Long barCode) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_BARCODE + " = " + barCode;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    /*void updateData(String row_id, String title, String author, String pages){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_AUTHOR, author);
        cv.put(COLUMN_PAGES, pages);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }

    }*/

    public void deleteItem(long barcode) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_BARCODE + " = ?";
        String[] whereArgs = {String.valueOf(barcode)};

        int rowsDeleted = db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();

        if (rowsDeleted > 0) {
            Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show();
        }
    }

}