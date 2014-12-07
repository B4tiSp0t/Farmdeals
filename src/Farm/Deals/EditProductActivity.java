/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Farm.Deals;

import static Farm.Deals.R.id.productNameField;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 *
 * @author Anestis
 */
public class EditProductActivity extends Activity implements DBConnectionListener, View.OnClickListener {

    private Dao dao;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        dao = Dao.instance(this);
        dao.connect("b4tisp0t.ddns.net:1433", "projectpass", "projectuser", "projectdb;");

        Button button = (Button) findViewById(R.id.saveEditButton);
        button.setOnClickListener(this);
        setContentView(R.layout.editproduct);
    }

    @Override
    public void onConnectionStatusInfo(final String status) {

        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {

            }
        });

    }

    @Override
    public void onConnectionSuccessful() {

        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {

            }

        });

    }
 
    @Override
    public void onConnectionFailed() {

        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {

            }
        });

    }

    public void onClick(View v) {
        if (v.getId() == R.id.saveEditButton) {
            EditText priceTextView = (EditText) findViewById(R.id.editPriceEditTextView);
            int price = Integer.parseInt(priceTextView.getText().toString());
            EditText quantityTextView = (EditText) findViewById(R.id.editQuantityEditTextView);
            int quantity = Integer.parseInt(quantityTextView.getText().toString());

            EditText productDescriptionTextView = (EditText) findViewById(R.id.editDescriptionEditTextView);
            String productDescription = productDescriptionTextView.getText().toString();

            int productID = 5; // we will get this from previous activity
            dao.editFarmerProduct(productID, price, productDescription, quantity);
        }
    }
}
