package com.example.gorgesamir.inventory;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.gorgesamir.inventory.data.InventoryContract;
import com.example.gorgesamir.inventory.data.InventoryDbHelper;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onStart() {
        super.onStart();
        displayDataInfo();
    }

    InventoryDbHelper helper = new InventoryDbHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Editor.class);
                startActivity(intent);
            }
        });
    }

    private void onItemPressed(){

    }

    private void displayDataInfo() {
        SQLiteDatabase database = helper.getReadableDatabase();
        String [] projection =
                {
                        InventoryContract.InventoryEntry._ID,
                        InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,
                        InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE,
                        InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY,
                        InventoryContract.InventoryEntry.COLUMN_PRODUCT_DESCRIPTION
                };
        Cursor cursor = database.query(
                InventoryContract.InventoryEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null,
                null
        );

        ListView inventoryListView = (ListView) findViewById(R.id.list);
        InventoryCursorAdapter adapter = new InventoryCursorAdapter(this, cursor);
        inventoryListView.setAdapter(adapter);
    }

}
