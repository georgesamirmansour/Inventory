package com.example.gorgesamir.inventory;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gorgesamir.inventory.data.InventoryContract;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by gorge samir on 2018-02-04.
 */

public class InventoryCursorAdapter extends CursorAdapter {

    private static final String TAG = InventoryCursorAdapter.class.getSimpleName();
    Context context;

    public InventoryCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        TextView nameTextView = view.findViewById(R.id.product_name);
        TextView priceTextView = view.findViewById(R.id.product_price);
        TextView quantityTextView = view.findViewById(R.id.product_quantity);
        ImageView productImageView = view.findViewById(R.id.image_view_product_image);

        int nameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY);
        int productImageColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_IMAGE);

        String productName = cursor.getString(nameColumnIndex);
        String productQuantity = cursor.getString(quantityColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);
        String productImage = cursor.getString(productImageColumnIndex);

        nameTextView.setText(context.getString(R.string.Name) + productName);
        priceTextView.setText(productPrice + context.getString(R.string.price_list_view));
        quantityTextView.setText(context.getString(R.string.Quantity) + productQuantity);
        productImageView.setImageBitmap(getUri(Uri.parse(productImage)));
    }

    private Bitmap getUri(Uri uri) {
        ParcelFileDescriptor parcelFileDescriptor = null;
        try {
            parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor descriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap imageAsBitmap = BitmapFactory.decodeFileDescriptor(descriptor);
            parcelFileDescriptor.close();
            return imageAsBitmap;
        } catch (Exception e) {
            Log.e(TAG, context.getString(R.string.error_in_loading), e);
            return null;
        } finally {
            try {
                if (parcelFileDescriptor != null) {
                    parcelFileDescriptor.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, context.getString(R.string.error_closing_parcel_file), e);
            }
        }
    }
}
