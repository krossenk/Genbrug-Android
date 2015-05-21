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


/**
 * A simple {@link Fragment} subclass.
 */
public class FBFragment extends Fragment {


    private CallbackManager mcallbackManager;
    private View view;
    LoginButton loginButton;

    // Callback method for login with facebook
    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            Intent intent = new Intent(getActivity(),MainActivity.class);

            Profile profile = Profile.getCurrentProfile();
            if (profile != null) {

                Uri profilePictureUri = profile.getProfilePictureUri(400,300);
                loginButton.setVisibility(View.INVISIBLE);


                //saving userinfo in SharedPrefrences
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                prefs.edit().putBoolean("Islogin", true).commit();
                prefs.edit().putString("FBUser", profile.getName()).commit();

                Toast.makeText(getActivity(), "Welcome " + profile.getId(),
                        Toast.LENGTH_SHORT).show();

                try {
                    URL url = new URL(profilePictureUri.toString());
                    prefs.edit().putString("ProfileURL", url.toString()).commit();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
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
