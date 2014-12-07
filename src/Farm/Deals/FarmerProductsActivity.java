/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Farm.Deals;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dao = Dao.instance(this);
        dao.connect("b4tisp0t.ddns.net:1433", "projectpass", "projectuser", "projectdb;");
       
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
         
	
        
   
    }
    
    private void loadUIComponents() {
        int farmerID = 3; //To be loaded from login activity
        String[][] result = dao.getFarmerProducts(farmerID);
        TableRow tableRow = (TableRow) findViewById(R.id.farmertableRow);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.farmertableLayout);
        for (int k = 0; k < result.length; k++) {
            String[] row = result[k];
            TableRow newTableRow = new TableRow(tableRow.getContext());
            for (int j = 0; j < row.length; j++) {
                Context context = tableRow.getChildAt(j).getContext();
                TextView textView = new TextView(context);
                textView.setText(result[k][j]);
                textView.setPadding(5, 0, 0, 0);
                newTableRow.addView(textView);
            }
            tableLayout.addView(newTableRow);
        }
    }
}
