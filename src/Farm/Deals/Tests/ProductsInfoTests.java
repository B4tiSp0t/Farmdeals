
package Farm.Deals.Tests;


import Farm.Deals.ProductsInfo;
import Farm.Deals.R;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;

public class ProductsInfoTests extends ActivityUnitTestCase<ProductsInfo> {
Intent mLaunchIntent;
    public ProductsInfoTests(Class<ProductsInfo> activityClass) {
        super(activityClass);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mLaunchIntent = new Intent(getInstrumentation()
                .getTargetContext(), ProductsInfo.class);
        startActivity(mLaunchIntent, null, null);
        final Button launchNextButton =
                (Button) getActivity()
                .findViewById(R.id.farmerProductsScreen);
    }
    
    @MediumTest
public void testNextActivityWasLaunchedWithIntent() {
    startActivity(mLaunchIntent, null, null);
    final Button launchNextButton =
            (Button) getActivity()
            .findViewById(R.id.farmerProductsScreen);
    launchNextButton.performClick();

    final Intent launchIntent = getStartedActivityIntent();
    assertNotNull("Intent was null", launchIntent);
    assertTrue(isFinishCalled());

    }
}
