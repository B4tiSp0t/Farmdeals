package Farm.Deals.Tests;


import Farm.Deals.R;
import Farm.Deals.SearchResultsActivity;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;

public class SearchResultsActivityTests extends ActivityUnitTestCase<Farm.Deals.SearchResultsActivity> {
Intent mLaunchIntent;
    public SearchResultsActivityTests(Class<SearchResultsActivity> activityClass) {
        super(activityClass);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mLaunchIntent = new Intent(getInstrumentation()
                .getTargetContext(), SearchResultsActivity.class);
        startActivity(mLaunchIntent, null, null);
        
    }
    
    @MediumTest
public void testNextActivityWasLaunchedWithIntent() {
    startActivity(mLaunchIntent, null, null);
    
    
    final Intent launchIntent = getStartedActivityIntent();
    assertNotNull("Intent was null", launchIntent);
    assertTrue(isFinishCalled());

    }
}
