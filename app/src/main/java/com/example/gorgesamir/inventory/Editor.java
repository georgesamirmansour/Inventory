package com.example.gorgesamir.inventory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gorgesamir.inventory.data.Inventory;
import com.example.gorgesamir.inventory.data.InventoryDbHelper;

import java.io.ByteArrayOutputStream;

public class Editor extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1;
    Inventory inventory = new Inventory();
    EditText nameEditText;
    EditText priceEditText;
    EditText descriptionEditText;
    InventoryDbHelper content = new InventoryDbHelper(this);
    private Bitmap emptyBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        displayQuantities(inventory.getProductQuantity());
        increment();
        decrement();
        insertData();
        addImage();

    }

    private void insertData() {
        ImageButton saveImageButton = findViewById(R.id.save_button);
        saveImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEditTextEntry(view);
                if (!inventory.getProductName().isEmpty() && !inventory.getProductDescription().isEmpty() &&
                        inventory.getProductPrice() != 0 && inventory.getProductQuantity() != 0 &&
                        inventory.getProductImage() != null) {

                    content.insert(inventory.getProductName(),
                            inventory.getProductDescription(),
                            inventory.getProductQuantity(),
                            inventory.getProductPrice(),
                            inventory.getProductImage());

                    Toast.makeText(getApplicationContext(),
                            getString(R.string.data_added), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Editor.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.fill_required_filled), Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }

    private void getEditTextEntry(View view) {
        nameEditText = findViewById(R.id.edit_product_name);
        priceEditText = findViewById(R.id.edit_product_price);
        descriptionEditText = findViewById(R.id.edit_product_description);
        inventory.setProductName(nameEditText.getText().toString());
        inventory.setProductDescription(descriptionEditText.getText().toString());
        try {
            inventory.setProductPrice(Integer.parseInt(priceEditText.getText().toString()));
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }


    }

    public void increment() {
        ImageButton incrementImageButton = findViewById(R.id.increment_product);
        incrementImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inventory.setProductQuantity(inventory.getProductQuantity() + 1);
                displayQuantities(inventory.getProductQuantity());
            }
        });
    }

    public void decrement() {
        ImageButton decrementImageButton = findViewById(R.id.decrement_product);
        decrementImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), getString(R.string._0_products), Toast.LENGTH_SHORT).show();
                if (inventory.getProductQuantity() == 0) {
                    return;
                }
                inventory.setProductQuantity(inventory.getProductQuantity() - 1);
                displayQuantities(inventory.getProductQuantity());
            }
        });
    }

    private void displayQuantities(int productQuantity) {
        TextView quantityTextView = findViewById(R.id.text_view_product_quantity);
        quantityTextView.setText(productQuantity + " products");
    }

    private void addImage() {
        ImageButton takePhotoImageButton = findViewById(R.id.take_photo_button);
        takePhotoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bundle extra = data.getExtras();
            Bitmap bitmap = (Bitmap) extra.get("data");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            inventory.setProductImage(bitmap);
            ImageView productImageView = findViewById(R.id.product_image_view);
            productImageView.setImageBitmap(inventory.getProductImage());
        }
    }
}
