package Farm.Deals;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity implements DBConnectionListener, OnClickListener
{
    private Spinner categories_spinner , location_spinner , sort_spinner, spinnerArrayAdapter;
    private Dao dao;
 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
                
        dao = Dao.instance(this);
        dao.connect("b4tisp0t.ddns.net:1433", "projectpass", "projectuser", "projectdb;");
       
        setContentView(R.layout.main);
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
		// May be retry or show some error, errors are usually reported in
		// onConnectionStatusInfo()

	}

    private void loadUIComponents() {
        setContentView(R.layout.main);
 
        String categories[] = dao.getCategories();
        String location[] = dao.getLocation();
                
        categories_spinner = (Spinner) findViewById(R.id.categories_spinner);
        ArrayAdapter<String> categories_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        categories_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories_adapter.notifyDataSetChanged();
        categories_spinner.setAdapter(categories_adapter);
        
        location_spinner = (Spinner) findViewById(R.id.location_spinner);
        ArrayAdapter<String> location_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, location);
        location_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        location_adapter.notifyDataSetChanged();
        location_spinner.setAdapter(location_adapter);
        
        sort_spinner = (Spinner) findViewById(R.id.sort_spinner);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
        R.array.sort_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter3.notifyDataSetChanged();
        sort_spinner.setAdapter(adapter3);
        
        
        ImageButton imageButton = (ImageButton) findViewById(R.id.loginScreen);
        imageButton.setOnClickListener(this);
        
        Button button = (Button) findViewById(R.id.search_button);
        button.setOnClickListener(this);
        
    }

     public void onClick(View arg0) {
         if(arg0.getId() == R.id.search_button){
             int categoryID = categories_spinner.getSelectedItemPosition();
             int locationID = location_spinner.getSelectedItemPosition();
             EditText productNameField = (EditText)findViewById(R.id.productNameField);
             String productName = productNameField.getText().toString();
             String[][] result = dao.searchProducts(categoryID, productName, locationID);
             if (result.length == 0)
                     {
                     Toast.makeText(Main.this,
		       "Δέν υπάρχουν αποτελέσματα", Toast.LENGTH_LONG).show();
                     }
             else{
                 Intent intent = new Intent(this, SearchResultsActivity.class);
                 Bundle bundle = new Bundle();
                 bundle.putSerializable("results", result);
                 intent.putExtras(bundle);
                 this.startActivity(intent);
                 //newIntent.putExtra("name",value);
             }
         }
	
         if(arg0.getId() == R.id.loginScreen){
		//define a new Intent for the second Activity
		Intent intent = new Intent(this,LoginScreen.class);
 
		//start the second Activity
		this.startActivity(intent);
	}
   
    }

    
   
}
