package com.task.omnify.appomnify.Fragments;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.internal.FacebookSignatureValidator;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.task.omnify.appomnify.Activities.BaseActivity;
import com.task.omnify.appomnify.Activities.LoginActivity;
import com.task.omnify.appomnify.Interfaces.FaceBookLoginListener;
import com.task.omnify.appomnify.Interfaces.GoogleSignInListener;
import com.task.omnify.appomnify.Interfaces.PhoneAuthenticationPressedListener;
import com.task.omnify.appomnify.Interfaces.StoredOnServerListener;
import com.task.omnify.appomnify.Models.UserDataWithPassword;
import com.task.omnify.appomnify.Models.UserDataWithoutPassword;
import com.task.omnify.appomnify.R;
import com.task.omnify.appomnify.Utils.StoredVariables;


public class SignInOptionsFragment extends Fragment implements View.OnClickListener {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("user_data");
    StoredOnServerListener storedOnServerListener;
    private BaseActivity parentActivity;
    private Context context;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManagerFB;
    private static final String TAG = "SignIn";
    private static final int RC_SIGN_IN = 9001;
    private static final int FB_LOGIN_REQUESTCODE = 64206;
    private GoogleSignInListener googleSignInListener;
    private FaceBookLoginListener faceBookLoginListener;
    private PhoneAuthenticationPressedListener phoneAuthenticationPressedListener;
    private FirebaseAuth mAuth;


    public SignInOptionsFragment() {
        // Required empty public constructor
    }

    public static SignInOptionsFragment newInstance(BaseActivity parentActivity) {
        SignInOptionsFragment fragment = new SignInOptionsFragment();
        fragment.parentActivity=parentActivity;
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        faceBookLoginListener= (FaceBookLoginListener) context;
        googleSignInListener= (GoogleSignInListener) context;
        phoneAuthenticationPressedListener= (PhoneAuthenticationPressedListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCallbackManagerFB = CallbackManager.Factory.create();
        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]
        mGoogleSignInClient = GoogleSignIn.getClient(parentActivity, gso);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_sign_in_options, container, false);
        mAuth = FirebaseAuth.getInstance();
        view.findViewById(R.id.sign_in_button).setOnClickListener(this);
        view.findViewById(R.id.button_via_phone).setOnClickListener(this);
        LoginButton loginButton = view.findViewById(R.id.button_facebook_login);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManagerFB, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());


            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");


            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);

            }
        });

        return view;
    }

    // [START auth_with_facebook]
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]

        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(parentActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            faceBookLoginListener.onFacebookLoginSucessfull();
                            uploadToken(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(parentActivity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            faceBookLoginListener.onFacebookLoginFailed();

                        }

                    }
                });


    }
    // [END auth_with_facebook]

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]

                // [END_EXCLUDE]
            }
        }
        else if(requestCode==FB_LOGIN_REQUESTCODE){
            // Pass the activity result back to the Facebook SDK
            mCallbackManagerFB.onActivityResult(requestCode, resultCode, data);
        }

    }

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(parentActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            googleSignInListener.onGoogleSignInSucessful();
                            uploadToken(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            googleSignInListener.onGoogleSignInFailed();
                        }
                    }
                });
    }
    // [END auth_with_google]

    // [START Google signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END Google signin]





    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(parentActivity,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        }});
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i==R.id.button_via_phone){
        phoneAuthenticationPressedListener.onPhoneButtonPressed();
        }
        if (i == R.id.sign_in_button) {
        signIn();
        }
    }

        private void uploadToken(FirebaseUser firebaseUser){
            UserDataWithoutPassword user = new UserDataWithoutPassword(
                    StoredVariables.getTokenId(),
                    mAuth.getCurrentUser().getUid().toString()
                    );
            String realTimeDatabaseFriendlyPathName=mAuth.getCurrentUser().getEmail().replace("@","+").replace(".","");
            myRef.child(realTimeDatabaseFriendlyPathName).setValue(user);
        }

}
