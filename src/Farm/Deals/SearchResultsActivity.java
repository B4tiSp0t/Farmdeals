package Farm.Deals;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class SearchResultsActivity extends Activity implements View.OnClickListener {

    String[][] productsList;
   
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
            for(int j=0; j < 5; j++)
            {
               my_list[i][j] = ((String[])row)[j];
            }
            i++;
        }
        productsList = my_list;
        
        TableRow tableRow = (TableRow)findViewById(R.id.tableRow1);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout1);
        for(int k=0;k<my_list.length; k++)
        {
            String[] row = my_list[k];
            TableRow newTableRow = new TableRow(tableRow.getContext());
            newTableRow.setId(k+1);
            for (int j = 1; j < row.length; j++) 
            {
                Context context = tableRow.getChildAt(j-1).getContext();
                TextView textView = new TextView(context);
                if(j == 2)
                {
                    textView.setText(my_list[k][j] + "\u20ac");
                }else
                {
                    textView.setText(my_list[k][j]);
                }
                textView.setTextColor(Color.parseColor("#000000"));
                textView.setPadding(6, 0, 0, 0);
                newTableRow.addView(textView);
            }
            newTableRow.setOnClickListener(this);
            tableLayout.addView(newTableRow);
        }
        
        
        
             
    }

    public void onClick(View v) {
        TableRow selectedTableRow = (TableRow) v;
        int index = selectedTableRow.getId();
        
        String[] productDetails = productsList[index-1];
        
        Intent intent = new Intent(this, ProductsInfo.class);
        
        Bundle bundle = new Bundle();
        bundle.putSerializable("productsInfo", productDetails);
        intent.putExtras(bundle);
        
        this.startActivity(intent);
//	}
    }
    
}
