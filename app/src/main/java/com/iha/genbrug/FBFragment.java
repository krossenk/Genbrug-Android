package com.iha.genbrug;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.net.MalformedURLException;
import java.net.URL;


import webservice.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class FBFragment extends Fragment {


    private CallbackManager mcallbackManager;
    private View view;
    LoginButton loginButton;
    private GlobalSettings globalSettings;

    // Callback method for login with facebook
    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            Intent intent = new Intent(getActivity(),MainActivity.class);

            Profile profile = Profile.getCurrentProfile();
            if (profile != null) {

                Uri profilePictureUri = profile.getProfilePictureUri(400, 300);
                loginButton.setVisibility(View.INVISIBLE);

                 globalSettings = GlobalSettings.getInstance();
                 User user = new User();

                user.id = Long.valueOf(profile.getId());
                user.firstname = profile.getFirstName();
                user.lastname = profile.getLastName();
                user.profileimageURL = profilePictureUri.toString();



                globalSettings.saveUserToPref(user);
                globalSettings.sharedPreferences.edit().putBoolean("Islogin", true).commit();

                Toast.makeText(getActivity(), "Welcome " + profile.getId(),
                        Toast.LENGTH_SHORT).show();

                startActivity(intent);
                getActivity().finish();

            }
        }


        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mcallbackManager = CallbackManager.Factory.create();



    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setFragment(this);
        loginButton.registerCallback(mcallbackManager, mCallback);



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fb, container, false);
        setFragmentView(rootView);

        return rootView;
    }

    public void setFragmentView (View v)
    {
        this.view = v;
    }
    public View getFragmentView ()
    {
        return this.view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mcallbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
