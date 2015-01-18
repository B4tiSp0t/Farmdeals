/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Farm.Deals.Tests;

import Farm.Deals.R;
import Farm.Deals.SignUpScreen;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;

public class SignUpScreenTests extends ActivityUnitTestCase<Farm.Deals.SignUpScreen> {

    Intent mLaunchIntent;

    public SignUpScreenTests(Class<SignUpScreen> activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mLaunchIntent = new Intent(getInstrumentation()
                .getTargetContext(), SignUpScreen.class);
        startActivity(mLaunchIntent, null, null);
        final Button launchNextButton
                = (Button) getActivity()
                .findViewById(R.id.submitButton);
    }

    @MediumTest
    public void testNextActivityWasLaunchedWithIntent() {
        startActivity(mLaunchIntent, null, null);
        final Button launchNextButton
                = (Button) getActivity()
                .findViewById(R.id.submitButton);
        launchNextButton.performClick();

        final Intent launchIntent = getStartedActivityIntent();
        assertNotNull("Intent was null", launchIntent);
        assertTrue(isFinishCalled());
    }
}
