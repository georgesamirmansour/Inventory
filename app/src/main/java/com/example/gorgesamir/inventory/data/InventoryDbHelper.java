package com.example.gorgesamir.inventory.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import com.example.gorgesamir.inventory.data.InventoryContract.InventoryEntry;

/**
 * Created by gorge samir on 2018-02-04.
 */

public class InventoryDbHelper extends SQLiteOpenHelper {



    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "inventory.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = " ,";
    private static final String NOT_NULL = " NOT NULL";
    private static final String DEFAULT_0 = " DEFAULT 0";
    private static final String BLOB_TYPE = " BLOB ";
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + InventoryEntry.TABLE_NAME +
            " ( " + InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
            InventoryEntry.COLUMN_PRODUCT_NAME + TEXT_TYPE + NOT_NULL + COMMA_SEP +
            InventoryEntry.COLUMN_PRODUCT_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
            InventoryEntry.COLUMN_PRODUCT_IMAGE + BLOB_TYPE + NOT_NULL + COMMA_SEP +
            InventoryEntry.COLUMN_PRODUCT_PRICE + INTEGER_TYPE + NOT_NULL + DEFAULT_0 + COMMA_SEP +
            InventoryEntry.COLUMN_PRODUCT_QUANTITY + INTEGER_TYPE + NOT_NULL + DEFAULT_0 + " ); ";
    private static final String SQL_DELETE_ENTRIES = " DROP TABLE IF EXIST " + InventoryEntry.TABLE_NAME;
    Inventory inventory = new Inventory();

    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public boolean insert(String productName, String productDescription, int productQuantity,
                          int productPrice, Bitmap productImage) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME, productName);
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY, productDescription);
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_DESCRIPTION, productQuantity);
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_IMAGE, String.valueOf(productImage));
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE, productPrice);
        long rowID = database.insert(InventoryContract.InventoryEntry.TABLE_NAME, null, values);
        return rowID != 0;
    }

    public void delete(int item) {
        SQLiteDatabase database = getReadableDatabase();
        database.delete(InventoryContract.InventoryEntry.TABLE_NAME,
                InventoryContract.InventoryEntry._ID + "=" + item, null);
    }

    public Cursor selectData(int ID) {
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor;
        String query = "SELECT * FROM " + InventoryContract.InventoryEntry.TABLE_NAME + " WHERE " +
                InventoryContract.InventoryEntry._ID + " = " + ID;
        cursor = database.rawQuery(query, null);
        return cursor;
    }

    public void update(int ID) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME, inventory.getProductName());
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY, inventory.getProductQuantity());
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_DESCRIPTION, inventory.getProductDescription());
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE, inventory.getProductPrice());
        database.update(InventoryContract.InventoryEntry.TABLE_NAME, values,
                InventoryContract.InventoryEntry._ID + " = " + ID, null);
    }

    public boolean updateQuantity(int ID) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY, inventory.getProductQuantity());
        long rowID = database.update(InventoryContract.InventoryEntry.TABLE_NAME, values,
                InventoryContract.InventoryEntry._ID + " = " + ID, null);
        return rowID != 0;
    }
}
