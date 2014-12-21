package Farm.Deals;

import static Farm.Deals.R.id.productNameField;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 *
 * @author Anestis
 */
public class EditProductActivity extends Activity implements DBConnectionListener, View.OnClickListener {

    private Dao dao;
    private String[] productInfo;
    private String[] farmerInfo;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Object[] objectArray = (Object[]) getIntent().getExtras().getSerializable("farmerProductInfo");
        productInfo = (String[])objectArray;
        
        Object[] objectArray1 = (Object[]) getIntent().getExtras().getSerializable("farmerInfo");
        farmerInfo = (String[])objectArray1;
        
        setContentView(R.layout.editproduct);
        
        dao = Dao.instance(this);
        dao.connect("b4tisp0t.ddns.net:1433", "projectpass", "projectuser", "projectdb;");

                
        Button button = (Button) findViewById(R.id.saveEditButton);
        button.setOnClickListener(this);
        
        EditText quantityTextField = (EditText)findViewById(R.id.editQuantityEditTextView);
        quantityTextField.setText(productInfo[2]);
        
        EditText priceTextField = (EditText)findViewById(R.id.editPriceEditTextView);
        priceTextField.setText(productInfo[4]);
        
        EditText descriptionTextField = (EditText)findViewById(R.id.editDescriptionEditTextView);
        descriptionTextField.setText(productInfo[1]);
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
            String price = priceTextView.getText().toString();
            
            EditText quantityTextView = (EditText) findViewById(R.id.editQuantityEditTextView);
            int quantity = Integer.parseInt(quantityTextView.getText().toString());

            EditText productDescriptionTextView = (EditText) findViewById(R.id.editDescriptionEditTextView);
            String productDescription = productDescriptionTextView.getText().toString();

            String productIdString = this.getIntent().getStringExtra("productID");
            int productID = Integer.parseInt(productIdString);
            dao.editFarmerProduct(productID, price, productDescription, quantity);
            
            Toast.makeText(EditProductActivity.this,
		       "Οι αλλαγές αποθηκεύτηκαν.", Toast.LENGTH_LONG).show();
            
            
            Intent intent = new Intent(this, productDetailsActivity.class);

            Bundle bundle = new Bundle();
            bundle.putSerializable("farmerProductsInfo", farmerInfo);
            intent.putExtras(bundle);
            dao.disconnect();
            this.finish();
            this.startActivity(intent);
        }
    }
}
