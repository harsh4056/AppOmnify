

package com.task.omnify.appomnify.Activities;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.task.omnify.appomnify.Fragments.EnterPasswordFragment;
import com.task.omnify.appomnify.Fragments.PhoneOTPFragment;
import com.task.omnify.appomnify.Fragments.SignInOptionsFragment;
import com.task.omnify.appomnify.Fragments.StorePasswordFragment;
import com.task.omnify.appomnify.Interfaces.FaceBookLoginListener;
import com.task.omnify.appomnify.Interfaces.GoogleSignInListener;
import com.task.omnify.appomnify.Interfaces.PhoneAuthListener;
import com.task.omnify.appomnify.Interfaces.PhoneAuthenticationPressedListener;
import com.task.omnify.appomnify.Interfaces.PhonePasswordVerificationListener;
import com.task.omnify.appomnify.Interfaces.StoredOnServerListener;
import com.task.omnify.appomnify.R;
import com.task.omnify.appomnify.Utils.Constants;
import com.task.omnify.appomnify.Utils.StoredVariables;

public class LoginActivity extends BaseActivity implements FaceBookLoginListener,GoogleSignInListener,PhoneAuthenticationPressedListener,PhoneAuthListener,PhonePasswordVerificationListener,StoredOnServerListener{

    private static final String TAG = "LoginActivity";

    FrameLayout frameLayout;
    private FirebaseAuth mAuth;
    SignInOptionsFragment signInOptionsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.setApplicationId("160568181306428");
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        signInOptionsFragment=SignInOptionsFragment.newInstance(this);
        //Adding first sign in main fragment that is required
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frameLayout,signInOptionsFragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }



    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null || StoredVariables.getSignInMethod()== Constants.CASE_PHONE) {
            launchActivity();
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Sending resulst back to requested fragment
        signInOptionsFragment.onActivityResult(requestCode,resultCode,data);

    }
    private void launchActivity(){
        Intent i= new Intent(this,MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onFacebookLoginSucessfull() {
        //Stored login method as facebook
        StoredVariables.storeSignInMethod(Constants.CASE_FACEBOOK);
        launchActivity();
    }

    @Override
    public void onFacebookLoginFailed() {

    }

    @Override
    public void onGoogleSignInSucessful() {
        //Stored login method as Google
        StoredVariables.storeSignInMethod(Constants.CASE_GOOGLE);
    launchActivity();
    }

    @Override
    public void onGoogleSignInFailed() {

    }

    @Override
    public void onPhoneButtonPressed() {
        //Enter password and phone if account exists
        addFragment( EnterPasswordFragment.newInstance(this));
    }

    @Override
    public void onOTPVerified(final String phoneNo) {
        //If new account registration with phone is to be done
       addFragment(StorePasswordFragment.newInstance());
    }




    @Override
    public void onPasswordMatched() {
        //Login method as phone is set
        StoredVariables.storeSignInMethod(Constants.CASE_PHONE);
        launchActivity();
    }

    @Override
    public void onNewSignUpWithPhonePressed() {

        //If new account registration with phone is to be done
        addFragment(PhoneOTPFragment.newInstance(this));
    }

    @Override
    public void onDataUploaded() {

        mAuth = FirebaseAuth.getInstance();
        //Signing out is being done due to a bug at firebase and a way around it has been
        //achieved here by storing the sign in method manually rather than depending on mAuth variable
        mAuth.signOut();
        StoredVariables.storeSignInMethod(Constants.CASE_PHONE);
        launchActivity();
    }

    private void addFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

}
