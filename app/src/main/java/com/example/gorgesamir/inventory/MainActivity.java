package com.example.gorgesamir.inventory;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gorgesamir.inventory.data.Inventory;
import com.example.gorgesamir.inventory.data.InventoryContract;
import com.example.gorgesamir.inventory.data.InventoryDbHelper;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    InventoryCursorAdapter adapter;
    InventoryDbHelper content = new InventoryDbHelper(this);
    Inventory inventory = new Inventory();
    private int ID;

    @Override
    protected void onStart() {
        super.onStart();
        displayDataInfo();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Editor.class);
                startActivity(intent);
            }
        });
        onItemPressed();
    }

    public void onItemPressed() {
        listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ID = listView.getPositionForView(view);
                ID = ID + 1;
                Intent intent = new Intent(MainActivity.this, ItemEditor.class);
                intent.putExtra("my-ID", ID);
                startActivity(intent);
            }
        });
    }

    private void displayDataInfo() {
        SQLiteDatabase database = content.getReadableDatabase();
        String[] projection =
                {
                        InventoryContract.InventoryEntry._ID,
                        InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,
                        InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE,
                        InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY,
                        InventoryContract.InventoryEntry.COLUMN_PRODUCT_DESCRIPTION,
                        InventoryContract.InventoryEntry.COLUMN_PRODUCT_IMAGE
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
        if (cursor.moveToNext() == false) {
            ImageView emptyImageView = findViewById(R.id.empty_image_view);
            emptyImageView.setVisibility(View.VISIBLE);
            TextView emptyTextView = findViewById(R.id.empty_text_view);
            emptyTextView.setVisibility(View.VISIBLE);
        }

        ListView inventoryListView = findViewById(R.id.list);
        InventoryCursorAdapter adapter = new InventoryCursorAdapter(this, cursor);
        inventoryListView.setAdapter(adapter);
    }

    private void selectRow() {
        content.selectData(ID);
        SQLiteDatabase database = content.getReadableDatabase();
        String[] projection =
                {
                        InventoryContract.InventoryEntry._ID,
                        InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,
                        InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE,
                        InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY,
                        InventoryContract.InventoryEntry.COLUMN_PRODUCT_DESCRIPTION,
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
        try {
            int idColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
            int descriptionColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_DESCRIPTION);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE);


            while (cursor.moveToNext()) {
                int rowID = cursor.getInt(idColumnIndex);
                if (rowID == ID) {
                    inventory.setProductName(cursor.getString(nameColumnIndex));
                    inventory.setProductDescription(cursor.getString(descriptionColumnIndex));
                    inventory.setProductPrice(cursor.getInt(priceColumnIndex));
                    inventory.setProductQuantity(cursor.getInt(quantityColumnIndex));

                }
            }
        } finally {
            cursor.close();
        }
    }

    public void saleButton(View view) {
        ID = listView.getPositionForView(view);
        ID = ID + 1;
        selectRow();
        if (inventory.getProductQuantity() != 0) {
            inventory.setProductQuantity(inventory.getProductQuantity() - 1);
            content.updateQuantity(ID, inventory.getProductQuantity());
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "can't sale with quantity 0", Toast.LENGTH_SHORT).show();
        }
    }
}

