package br.com.feinstein.technicaltest_mf;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

/**
 * Initializes the {@link AndroidThreeTen} library.
 */
public final class TechnicalTest_MF_App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
    }
}
