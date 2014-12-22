/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Farm.Deals;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 *
 * @author Anestis
 */
public class FarmerProductsActivity extends Activity implements DBConnectionListener, View.OnClickListener {
    private Dao dao;
    private int accountID;
    String[][] productsList;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dao = Dao.instance(this);
        dao.connect("b4tisp0t.ddns.net:1433", "projectpass", "projectuser", "projectdb;");
       accountID = this.getIntent().getIntExtra("accountID", 0);
        setContentView(R.layout.farmerproducts);    
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
                            loadUIComponents();
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

    public void onClick(View arg0) {
        
        if (arg0.getId() == R.id.farmerAddProductButton) {
            dao.disconnect();
            Intent intent = new Intent(this,Insert_Product.class);
            intent.putExtra("farmerId", accountID);
            this.startActivity(intent);
        } else {
            TableRow selectedTableRow = (TableRow) arg0;
            int index = selectedTableRow.getId();

            String[] productDetails = productsList[index - 1];

            Intent intent = new Intent(this, productDetailsActivity.class);

            Bundle bundle = new Bundle();
            bundle.putSerializable("farmerProductsInfo", productDetails);
            intent.putExtras(bundle);
            dao.disconnect();
            this.startActivity(intent);
        }

    }
    
private void loadUIComponents() {
         setContentView(R.layout.farmerproducts);  
        String[][] result = dao.getFarmerProducts(accountID);
        productsList = result;
        TableRow tableRow = (TableRow) findViewById(R.id.farmertableRow);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.farmertableLayout);
        
        Button button = (Button)findViewById(R.id.farmerAddProductButton);
        button.setOnClickListener(this);
        
        for (int k = 0; k < result.length; k++) {
            String[] row = result[k];
            TableRow newTableRow = new TableRow(tableRow.getContext());
            newTableRow.setId(k+1);
            for (int j = 1; j < row.length; j++) {
                Context context = tableRow.getChildAt(j-1).getContext();
                TextView textView = new TextView(context);
                if(j == 2)
                {
                    textView.setText(result[k][j] + "\u20ac");
                }else
                {
                    textView.setText(result[k][j]);
                }
                textView.setTextColor(Color.parseColor("#000000"));
                textView.setPadding(6, 0, 0, 0);
                newTableRow.addView(textView);
            }
            newTableRow.setOnClickListener(this);
            tableLayout.addView(newTableRow);
        }
   } 
}
