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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 *
 * @author Anestis
 */
public class productDetailsActivity extends Activity implements DBConnectionListener, View.OnClickListener{
    private Dao dao;
    private String[] farmerProductInfo;
    private String[] fullProductInfo;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Object[] objectArray = (Object[]) getIntent().getExtras().getSerializable("farmerProductsInfo");
        farmerProductInfo = (String[])objectArray;
   
    
        dao = Dao.instance((DBConnectionListener) this);
        dao.connect("b4tisp0t.ddns.net:1433", "projectpass", "projectuser", "projectdb;");
        
             
        setContentView(R.layout.productdetails);  
        
    }
    
    public void onConnectionStatusInfo(final String status)
    {
        this.runOnUiThread(new Runnable(){
            @Override
            public void run(){
            }
        });
    }
    
    @Override
    public void onConnectionSuccessful()
    {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadUIComponents();
            }
        });
    }
    
    @Override
    public void onConnectionFailed()
    {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
            }
        });
    }
    
    private void loadUIComponents() {
        setContentView(R.layout.productdetails);
        
        Button deleteButton = (Button)findViewById(R.id.deleteProductButton);
        deleteButton.setOnClickListener(this);
        Button editButton = (Button)findViewById(R.id.editProductButton);
        editButton.setOnClickListener(this);
        
        
        String[] result = dao.getProductInfo(Integer.parseInt(farmerProductInfo[0]));
        
        TextView textview0 = (TextView)findViewById(R.id.FarmerProductName1);
        textview0.setText(result[0]);
        
        TextView textview1 = (TextView)findViewById(R.id.FarmerProductDescription1);
        textview1.setText(result[1]);
        
        TextView textview2 = (TextView)findViewById(R.id.FarmerProductQuantity1);
        textview2.setText(result[2]);
        
        TextView textview3 = (TextView)findViewById(R.id.FarmerProductMeasurement1);
        textview3.setText(result[3]);
        
        TextView textview4 = (TextView)findViewById(R.id.FarmerProductPrice1);
        textview4.setText(result[4] + "\u20ac");
        
        TextView textview5 = (TextView)findViewById(R.id.FarmerProductAddress1);
        textview5.setText(result[5]);
        
        TextView textview6 = (TextView)findViewById(R.id.FarmerProductNumber1);
        textview6.setText(result[6]);
        
        TextView textview7 = (TextView)findViewById(R.id.FarmerProductP_S_1);
        textview7.setText(result[7]);
        
        TextView textview8 = (TextView)findViewById(R.id.FarmerProductRegion1);
        textview8.setText(result[8]);
        
        TextView textview9 = (TextView)findViewById(R.id.FarmerProductCounty1);
        textview9.setText(result[9]);
        
        TextView textview10 = (TextView)findViewById(R.id.FarmerProductFNAme1);
        textview10.setText(result[10]);
        
        TextView textview11 = (TextView)findViewById(R.id.FarmerProductSurname1);
        textview11.setText(result[11]);
        
        TextView textview12 = (TextView)findViewById(R.id.FarmerProductPhone1);
        textview12.setText(result[12]);
        
        fullProductInfo = result;
    }

    public void onClick(View v) {
        if (v.getId() == R.id.deleteProductButton) {
            dao.deleteProduct(Integer.parseInt(farmerProductInfo[0]));
            Toast.makeText(productDetailsActivity.this,
		       "Το προϊόν διαγράφηκε επιτυχώς!", Toast.LENGTH_LONG).show();
            dao.disconnect();
            this.finish();
        }else if (v.getId() == R.id.editProductButton) {
           Intent intent = new Intent(this, EditProductActivity.class);

            Bundle bundle = new Bundle();
            bundle.putSerializable("farmerProductInfo", fullProductInfo);
            intent.putExtras(bundle);
            
            intent.putExtra("productID", farmerProductInfo[0]);
            
            Bundle bundle1 = new Bundle();
            bundle1.putSerializable("farmerInfo", farmerProductInfo);
            intent.putExtras(bundle1);
            
            
            dao.disconnect();
            this.finish();
            this.startActivity(intent);
        }
    }
    
}
