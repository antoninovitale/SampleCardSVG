package sample.svgcard.application;

import android.app.Application;
import android.content.Context;

public class AppStartupManager extends Application {
	private static boolean flipAllowed = true;
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return appContext;
    }

	public static boolean isFlipAllowed() {
		return flipAllowed;
	}

	public static void setFlipAllowed(boolean flipAllowed) {
		AppStartupManager.flipAllowed = flipAllowed;
	}
}
