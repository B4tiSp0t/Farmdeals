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
import android.view.View.OnClickListener;
import android.widget.EditText;

/**
 *
 * @author user
 */
public class SignUpScreen extends Activity implements DBConnectionListener, OnClickListener{
    private Dao dao;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
         dao = Dao.instance(this);
        dao.connect("b4tisp0t.ddns.net:1433", "projectpass", "projectuser", "projectdb;");
       
         
       super.onCreate(savedInstanceState);
 	this.setContentView(R.layout.signup);
        //get the Button reference
	//Button is a subclass of View
	//buttonClick is defined in main.xml "@+id/buttonClick"
        View v = findViewById(R.id.submitButton);
	//set event listener
        v.setOnClickListener(this);
        
    } 
      
        public void onClick(View arg0) {

         if(arg0.getId() == R.id.submitButton){
		
     
                 EditText nameField = (EditText)findViewById(R.id.nameField);
             String name = nameField.getText().toString();
             
             EditText surnameField = (EditText)findViewById(R.id.surnameField);
             String surname = surnameField.getText().toString();
             
                EditText RCField = (EditText)findViewById(R.id.RCField);
             String RC = RCField.getText().toString();
             
                EditText phoneField = (EditText)findViewById(R.id.phoneField);
             String phone = phoneField.getText().toString();
             
             EditText DOBField = (EditText)findViewById(R.id.DOBField);
             String DOB = DOBField.getText().toString();
             
              EditText addressField = (EditText)findViewById(R.id.addressField);
             String address = addressField.getText().toString();
             
              EditText emailField = (EditText)findViewById(R.id.emailField);
             String email = emailField.getText().toString();
             
             EditText passwordField = (EditText)findViewById(R.id.passwordField);
             String password = passwordField.getText().toString();
             
              EditText passwordConfirmField = (EditText)findViewById(R.id.passwordConfirmField);
             String passwordConfirm = passwordConfirmField.getText().toString();
             
              String[][] result = dao.signup(name, surname, RC, phone, DOB, address, email, password, passwordConfirm); }
   Intent intent = new Intent(this, LoginScreen.class);

  startActivity(intent);
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
		// May be retry or show some error, errors are usually reported in
		// onConnectionStatusInfo()

	}}
        