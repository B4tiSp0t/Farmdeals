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
 * @author Chringostan
 */
public class protuctDetailsActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
        
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        // ToDo add your GUI initialization code here 
        this.setContentView(R.layout.EditProtuctActivity);
    }
    Intent intent= new Intent(this,EditProtuctActivity.class);
       
        this.startActivity(intent);
}
