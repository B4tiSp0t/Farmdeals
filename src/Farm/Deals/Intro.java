package Farm.Deals;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import static android.view.View.INVISIBLE;
import android.view.View.OnClickListener;
import static android.view.View.VISIBLE;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.io.Serializable;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class Intro extends Activity implements DBConnectionListener, OnClickListener
{

    private Dao dao;
 //   private TextView tvConnectionLog;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
                
        dao = Dao.instance(this);
        dao.connect("b4tisp0t.ddns.net:1433", "projectpass", "projectuser", "projectdb;");
        
       
        setContentView(R.layout.intro);
   //     tvConnectionLog = (TextView) findViewById(R.id.tv_connection_log);
        Button button = (Button) findViewById(R.id.next_button);
        button.setEnabled(FALSE);
        button.setOnClickListener(this);
    }

    @Override
	public void onConnectionStatusInfo(final String status) {

		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {	
                        //    tvConnectionLog.setText(tvConnectionLog.getText() + "\n"
			//			+ status);
			}
		});

	}

	@Override
	public void onConnectionSuccessful() {
                
            
            
		this.runOnUiThread(new Runnable() {
                            
			@Override
			public void run() {
                             View textview = findViewById(R.id.intromsg);
                             textview.setVisibility(INVISIBLE);
                             Button button = (Button) findViewById(R.id.next_button);
		             button.setEnabled(TRUE);
			}
		});
                
              
                
	}

	@Override
	public void onConnectionFailed() {

            this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
                               TextView textview = (TextView)findViewById(R.id.intromsg);
                               textview.setText("Δεν υπάρχει σύνδεση");
				

			}
		});

	}


     public void onClick(View arg0) {
         if(arg0.getId() == R.id.next_button){
                dao.disconnect();
		Intent intent = new Intent(this,Main.class);
		this.startActivity(intent);
	}
   
    }

    
   
}
