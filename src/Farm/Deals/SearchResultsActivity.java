/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Farm.Deals;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 *
 * @author Anestis
 */
public class SearchResultsActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchresults);
        
        Object[] objectArray = (Object[]) getIntent().getExtras().getSerializable("results");
        int horizontalLength = 0;
        if (objectArray.length > 0)
            horizontalLength = ((Object[]) objectArray[0]).length; 
        String[][] my_list = new String[objectArray.length][horizontalLength];
        int i = 0;
        for(Object row : objectArray)
        {
            for(int j=0; j < 10; j++)
            {
               my_list[i][j] = ((String[])row)[j];
            }
            i++;
        }
        
        TableRow tableRow = (TableRow)findViewById(R.id.tableRow1);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout1);
        for(int k=0;k<my_list.length; k++)
        {
            String[] row = my_list[k];
            TableRow newTableRow = new TableRow(tableRow.getContext());
            for (int j = 0; j < row.length; j++) 
            {
                Context context = tableRow.getChildAt(j).getContext();
                TextView textView = new TextView(context);
                textView.setText(my_list[k][j]);
                textView.setPadding(5, 0, 0, 0);
                newTableRow.addView(textView);
            }
            tableLayout.addView(newTableRow);
        }
        
        
        
             
    }
    
}
