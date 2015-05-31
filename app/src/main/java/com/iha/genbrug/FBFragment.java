package com.iha.genbrug;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import webservice.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class FBFragment extends Fragment {


    private CallbackManager mcallbackManager;
    private View view;
    private LoginButton loginButton;
    private GlobalSettings globalSettings;
    private ProfileTracker mProfileTracker;


    // Callback method for login with facebook.
    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {


            Profile currentProfile = Profile.getCurrentProfile();

            if(currentProfile != null) {
                Uri profilePictureUri = currentProfile.getProfilePictureUri(400, 300);
                loginButton.setVisibility(View.INVISIBLE);

                globalSettings = GlobalSettings.getInstance();
                User user = new User();

                user.id = Long.valueOf(currentProfile.getId());
                user.firstname = currentProfile.getFirstName();
                user.lastname = currentProfile.getLastName();
                user.profileimageURL = profilePictureUri.toString();

                globalSettings.saveUserToPref(user);
                globalSettings.sharedPreferences.edit().putBoolean("Islogin", true).commit();

                Toast.makeText(getActivity(), "Welcome " + currentProfile.getName(),
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }



            // Creates profile tracker to get the newest profile information.
            mProfileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                    if (newProfile != null) {
                        Uri profilePictureUri = newProfile.getProfilePictureUri(400, 300);
                        loginButton.setVisibility(View.INVISIBLE);

                        globalSettings = GlobalSettings.getInstance();
                        User user = new User();

                        user.id = Long.valueOf(newProfile.getId());
                        user.firstname = newProfile.getFirstName();
                        user.lastname = newProfile.getLastName();
                        user.profileimageURL = profilePictureUri.toString();

                        globalSettings.saveUserToPref(user);
                        globalSettings.sharedPreferences.edit().putBoolean("Islogin", true).commit();

                        if(getActivity() != null) {
                            Toast.makeText(getActivity(), "Welcome " + newProfile.getName(),
                                    Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }
                    mProfileTracker.stopTracking();
                }
            };

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

        if (mcallbackManager.onActivityResult(requestCode, resultCode, data))
            return;
    }

}
