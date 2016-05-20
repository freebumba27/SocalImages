package com.aj.socialImages;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnLogoutListener;
import com.sromku.simple.fb.listeners.OnProfileListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private SimpleFacebook mSimpleFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getHashKey();
    }

    private void getHashKey() {
        try {
            PackageInfo info = getBaseContext().getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", "KeyHash:" + Base64.encodeToString(md.digest(),
                        Base64.DEFAULT));
                Toast.makeText(getBaseContext().getApplicationContext(), Base64.encodeToString(md.digest(),
                        Base64.DEFAULT), Toast.LENGTH_LONG).show();
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mSimpleFacebook = SimpleFacebook.getInstance(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mSimpleFacebook.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    OnLoginListener onLoginListener = new OnLoginListener() {

        @Override
        public void onException(Throwable throwable) {

        }

        @Override
        public void onFail(String reason) {

        }

        @Override
        public void onLogin(String accessToken, List<Permission> acceptedPermissions, List<Permission> declinedPermissions) {
            // change the state of the button or do whatever you want
            Log.i(TAG, "Logged in");
            //mSimpleFacebook.getPhotos(onPhotosListener);
            //mSimpleFacebook.getAlbums(onAlbumsListener);

            Profile.Properties properties = new Profile.Properties.Builder()
                    .add(Profile.Properties.ID)
                    .add(Profile.Properties.NAME)
                    .add(Profile.Properties.FIRST_NAME)
                    .add(Profile.Properties.LAST_NAME)
                    .add(Profile.Properties.PICTURE)
                    .add(Profile.Properties.COVER)
                    .add(Profile.Properties.GENDER)
                    .add(Profile.Properties.BIRTHDAY)
                    .build();

            mSimpleFacebook.getProfile(properties, onProfileListener);
        }

        @Override
        public void onCancel() {

        }
    };

    OnLogoutListener onLogoutListener = new OnLogoutListener() {
        @Override
        public void onLogout() {
            Log.i(TAG, "You are logged out");
            mSimpleFacebook.login(onLoginListener);
        }
    };

    OnProfileListener onProfileListener = new OnProfileListener() {
        @Override
        public void onComplete(Profile response) {
            super.onComplete(response);

            Intent i = new Intent(MainActivity.this, ActivityFacebookImages.class);
            i.putExtra("facebookId", response.getId());
            startActivity(i);
        }

        @Override
        public void onFail(String reason) {
            super.onFail(reason);
        }

        @Override
        public void onException(Throwable throwable) {
            super.onException(throwable);
        }
    };

    public void facebookLogin(View view) {
        if (!mSimpleFacebook.isLogin())
            mSimpleFacebook.login(onLoginListener);
        else
            mSimpleFacebook.logout(onLogoutListener);
    }
}
