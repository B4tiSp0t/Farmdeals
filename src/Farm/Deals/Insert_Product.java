/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Farm.Deals;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

/**
 *
 * @author Anestis
 */
public class Insert_Product extends Activity implements DBConnectionListener, View.OnClickListener {

    private Dao dao;
    private int accountId;
    private Spinner categories_spinner , location_spinner , sort_spinner, spinnerArrayAdapter;
    
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        accountId = this.getIntent().getIntExtra("farmerId", accountId);
                
        dao = Dao.instance(this);
        dao.connect("b4tisp0t.ddns.net:1433", "projectpass", "projectuser", "projectdb;");
        this.setContentView(R.layout.insertproduct);
        //get the Button reference
        //Button is a subclass of View
        //buttonClick is defined in main.xml "@+id/buttonClick"
        
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

                loadUIcomponents();
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
		// May be retry or show some error, errors are usually reported in
        // onConnectionStatusInfo()

    }

    public void onClick(View arg0) {

        if (arg0.getId() == R.id.newProductButton) {

            EditText nameField = (EditText) findViewById(R.id.newname1);
            String name = nameField.getText().toString();

            EditText descriptionField = (EditText) findViewById(R.id.newdescription1);
            String description = descriptionField.getText().toString();

            EditText quantityField = (EditText) findViewById(R.id.newquantity1);
            String quantity = quantityField.getText().toString();

            EditText priceField = (EditText) findViewById(R.id.newprice1);
            String price = priceField.getText().toString();

            EditText regionField = (EditText) findViewById(R.id.newregion1);
            String region = regionField.getText().toString();

            EditText addressField = (EditText) findViewById(R.id.newaddress1);
            String address = addressField.getText().toString();

            EditText numberField = (EditText) findViewById(R.id.newnumber1);
            String number = numberField.getText().toString();

            EditText zipcodeField = (EditText) findViewById(R.id.newps1);
            String zipcode = zipcodeField.getText().toString();

            int categoryID = categories_spinner.getSelectedItemPosition();
            int locationID = location_spinner.getSelectedItemPosition();
             
            if ("".equals(name) || "".equals(description) || "".equals(quantity) || "".equals(price) || "".equals(region) || "".equals(address) || "".equals(number) || "".equals(zipcode) || categoryID == 0 || locationID == 0) {
                Toast.makeText(Insert_Product.this,
                        "Παρακαλώ συμπηρώστε όλα τα πεδία!", Toast.LENGTH_LONG).show();
            } else {
                int result = dao.insertProduct(name, description, quantity, price, region, address, number, zipcode, categoryID, locationID, accountId);
                if (result == 1) {
                        Toast.makeText(Insert_Product.this,
                            "Η εγγραφή ήταν επιτυχής!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Insert_Product.this,
                            "Η εγγραφή ήταν ανεπιτυχής, παρακαλώ προσπαθήστε ξανά!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void loadUIcomponents() {
        setContentView(R.layout.insertproduct);
        String categories[] = dao.getCategories();
        String location[] = dao.getLocation();

        categories_spinner = (Spinner) findViewById(R.id.newcategoryspinner);
        ArrayAdapter<String> categories_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        categories_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories_adapter.notifyDataSetChanged();
        categories_spinner.setAdapter(categories_adapter);

        location_spinner = (Spinner) findViewById(R.id.newcountyspinner);
        ArrayAdapter<String> location_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, location);
        location_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        location_adapter.notifyDataSetChanged();
        location_spinner.setAdapter(location_adapter);

        View v = findViewById(R.id.newProductButton);
        //set event listener
        v.setOnClickListener(this);
        
        
    }
}
