package com.example.gorgesamir.inventory;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gorgesamir.inventory.data.InventoryContract.InventoryEntry;

import com.example.gorgesamir.inventory.data.InventoryDbHelper;

public class Editor extends AppCompatActivity {

    private int quantity;
    private String orderMessage;
    private String productName;
    private int productPrice;
    private String productDescription;
    InventoryDbHelper helper = new InventoryDbHelper(this);
    EditText nameEditText;
    EditText priceEditText;
    EditText descriptionEditText;
    ListView listView;
    InventoryCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        displayQuantities(quantity);
        increment();
        decrement();
        makeOrder();
        insertData();
        deleteData();
    }

    private void getEditTextEntry(View view) {
        nameEditText = findViewById(R.id.edit_product_name);
        priceEditText = findViewById(R.id.edit_product_price);
        descriptionEditText = findViewById(R.id.edit_product_description);
        productName = nameEditText.getText().toString();
        String price = priceEditText.getText().toString();
        productPrice = Integer.parseInt(price);
        productDescription = descriptionEditText.getText().toString();
    }

    private void quantityToast() {
        if (quantity == 0) {
            Toast.makeText(this, getString(R.string.warning_message_0_product), Toast.LENGTH_LONG).show();
        }
    }

    private void dataToast() {
        Toast.makeText(this, "Please fill out required filed", Toast.LENGTH_LONG).show();
    }

    private void composeEmail(String subject, String text) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto: "));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void increment() {
        ImageButton incrementImageButton = (ImageButton) findViewById(R.id.increment_product);
        incrementImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity = quantity + 1;
                displayQuantities(quantity);
            }
        });
    }

    public void decrement() {
        ImageButton decrementImageButton = (ImageButton) findViewById(R.id.decrement_product);
        decrementImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantityToast();
                if (quantity == 0) {
                    return;
                }
                quantity = quantity - 1;
                displayQuantities(quantity);
            }
        });
    }

    public void makeOrder() {
        ImageButton orderImageButton = (ImageButton) findViewById(R.id.order_button);
        orderImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEditTextEntry(view);
                if (quantity == 0 && productName.isEmpty() && productName.isEmpty() && productDescription.isEmpty()) {
                    dataToast();
                } else {
                    orderMessage = "Product name \t" + productName + "\n"
                            + "ProductPrice \t" + productPrice + "\n"
                            + "Product description \t" + productDescription + "\n"
                            + "Product quantity \t" + quantity;
                    composeEmail(getString(R.string.order_subject_message), orderMessage);
                }
            }
        });
    }

    private void displayQuantities(int productQuantity) {
        TextView quantityTextView = (TextView) findViewById(R.id.text_view_product_quantity);
        quantityTextView.setText(productQuantity + " products");
    }

    private void insertData() {
        ImageButton saveImageButton = (ImageButton) findViewById(R.id.save_button);
        saveImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEditTextEntry(view);
                if (quantity == 0 && productPrice == 0 && productName.isEmpty() && productDescription.isEmpty()) {
                    dataToast();
                } else {
                    SQLiteDatabase database = helper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(InventoryEntry.COLUMN_PRODUCT_NAME, productName);
                    values.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, quantity);
                    values.put(InventoryEntry.COLUMN_PRODUCT_DESCRIPTION, productDescription);
                    values.put(InventoryEntry.COLUMN_PRODUCT_PRICE, productPrice);
                    long rowID = database.insert(InventoryEntry.TABLE_NAME, null, values);
                    if (rowID == -1) {
                        Toast.makeText(getApplicationContext(), "error while adding", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "data added", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Editor.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    public void deleteData() {
        ImageButton deleteImageButton = (ImageButton) findViewById(R.id.delete_button);
        deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        SQLiteDatabase database = helper.getWritableDatabase();
                        database.delete(InventoryEntry.TABLE_NAME,
                                InventoryEntry._ID + "=" + adapter.getItem(i), null);
                    }
                });
            }
        });
    }
}
