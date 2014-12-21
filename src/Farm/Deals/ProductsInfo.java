package Farm.Deals;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;


public class ProductsInfo extends Activity implements DBConnectionListener {
    private Dao dao;
    private String[] productInfo;
    @Override
    
    public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    Object[] objectArray = (Object[]) getIntent().getExtras().getSerializable("productsInfo");
    productInfo = (String[])objectArray;
   
    
    dao = Dao.instance((DBConnectionListener) this);
    dao.connect("b4tisp0t.ddns.net:1433", "projectpass", "projectuser", "projectdb;");
    setContentView(R.layout.productsinfo);
     
    
    
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
        setContentView(R.layout.productsinfo);
        String[] result = dao.getProductInfo(Integer.parseInt(productInfo[0]));
        
        TextView textview0 = (TextView)findViewById(R.id.Name1);
        textview0.setText(result[0]);
        
        TextView textview1 = (TextView)findViewById(R.id.Description1);
        textview1.setText(result[1]);
        
        TextView textview2 = (TextView)findViewById(R.id.Quantity1);
        textview2.setText(result[2]);
        
        TextView textview3 = (TextView)findViewById(R.id.Measurement1);
        textview3.setText(result[3]);
        
        TextView textview4 = (TextView)findViewById(R.id.Price1);
        textview4.setText(result[4]);
        
        TextView textview5 = (TextView)findViewById(R.id.Address1);
        textview5.setText(result[5]);
        
        TextView textview6 = (TextView)findViewById(R.id.Number1);
        textview6.setText(result[6]);
        
        TextView textview7 = (TextView)findViewById(R.id.P_S_1);
        textview7.setText(result[7]);
        
        TextView textview8 = (TextView)findViewById(R.id.Region1);
        textview8.setText(result[8]);
        
        TextView textview9 = (TextView)findViewById(R.id.County1);
        textview9.setText(result[9]);
        
        TextView textview10 = (TextView)findViewById(R.id.FNAme1);
        textview10.setText(result[10]);
        
        TextView textview11 = (TextView)findViewById(R.id.Surname1);
        textview11.setText(result[11]);
        
        TextView textview12 = (TextView)findViewById(R.id.Phone1);
        textview12.setText(result[12]);
    }
}
