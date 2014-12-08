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
 * @author aris
 */
public class LoginScreen extends Activity implements DBConnectionListener, OnClickListener {
    
    private Dao dao;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        dao = Dao.instance(this);
        dao.connect("b4tisp0t.ddns.net:1433", "projectpass", "projectuser", "projectdb;");
        
        setContentView(R.layout.login);
        //get the Button reference
	//Button is a subclass of View
	//signUpScreen is defined in main.xml "@+id/signUpScreen"
        View btnSignUp = findViewById(R.id.signUpScreen);
	//set event listener
        btnSignUp.setOnClickListener(this);
        //get the Button reference
	//Button is a subclass of View
	//farmerProductsScreen is defined in main.xml "@+id/farmerProductsScreen"
        View btnLogin = findViewById(R.id.farmerProductsScreen);
	//set event listener
        btnLogin.setOnClickListener(this);   
    }
    
    @Override
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
        // May be retry or show some error, errors are usually reported in
        // onConnectionStatusInfo()
    }
    
    @Override
    public void onClick(View arg0){
        if(arg0.getId() == R.id.signUpScreen){
            //define a new Intent for the second Activity
            Intent intent = new Intent(this,SignUpScreen.class);
            //start the second Activity
            this.startActivity(intent);
        }
        if(arg0.getId() == R.id.farmerProductsScreen){
            EditText emailField = (EditText)findViewById(R.id.text_email);
            EditText passwordField = (EditText)findViewById(R.id.text_password);
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            String result = dao.login(email, password);
             
            if (result =="0")
            {
                //define a new Intent for the second Activity
                Intent intent = new Intent(this,LoginFailedMessageScreen.class);
                //start the second Activity
                this.startActivity(intent);
            }
            if (result =="1")
            {
                //define a new Intent for the second Activity
                Intent intent = new Intent(this,FarmerProductsScreen.class);
                //start the second Activity
                this.startActivity(intent);
            }
        }
   
    }
    
    
}