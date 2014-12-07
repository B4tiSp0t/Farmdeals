/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Farm.Deals;

import android.app.Activity;
import android.os.Bundle;

/**
 *
 * @author θεοδωρα ζαχαρουδη
 */
public class Insert_Product extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        // ToDo add your GUI initialization code here   
        //define a new Intent for the InsertProduct Activity
		Intent intent = new Intent(this,InsertProductActivity.class);
 
		//start the InsertProduct Activity
		this.startActivity(intent);
        
    }
    
}
