package com.gadarts.parashoot.android;

import android.support.multidex.MultiDexApplication;
import com.crashlytics.android.Crashlytics;
import com.flurry.android.FlurryAgent;
import com.gadarts.parashoot.utils.GameSettings;
import com.gadarts.parashoot.utils.Rules;
import io.fabric.sdk.android.Fabric;

/**
 * Application class used for the Flurry.
 */
public class MyApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        if (GameSettings.ALLOW_BUG_REPORTING) {
            Fabric.with(this, new Crashlytics());
        }
        FlurryAgent.setLogEnabled(GameSettings.ALLOW_FLURRY_DEBUG);
        FlurryAgent.init(this, Rules.System.Analytics.ANALYTICS_KEY);
    }
}
