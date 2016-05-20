package com.aj.socialImages;

import android.app.Application;

import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.sromku.simple.fb.utils.Logger;


public class MyApplication extends Application {

    private static final String APP_ID = "762514513885522";
    private static final String APP_NAMESPACE = "Social Images";

    @Override
    public void onCreate() {
        super.onCreate();

        // set log to true
        Logger.DEBUG_WITH_STACKTRACE = true;

        // initialize facebook configuration
        Permission[] permissions = new Permission[]{
                Permission.USER_PHOTOS
        };

        SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
                .setAppId(APP_ID)
                .setNamespace(APP_NAMESPACE)
                .setPermissions(permissions)
                .build();

        SimpleFacebook.setConfiguration(configuration);
    }

}
