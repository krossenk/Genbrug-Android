package com.iha.genbrug;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class FBFragment extends Fragment {


    private CallbackManager mcallbackManager;


    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            Intent intent = new Intent(getActivity(),MainActivity.class);

            Profile profile = Profile.getCurrentProfile();
            if (profile != null) {
                Toast.makeText(getActivity(), "Welcome " + profile.getName(),
                        Toast.LENGTH_SHORT).show();
                Uri profilePictureUri = profile.getProfilePictureUri(400,300);

                try {
                    URL url = new URL(profilePictureUri.toString());
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
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setFragment(this);
        loginButton.registerCallback(mcallbackManager, mCallback);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fb, container, false);

        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mcallbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
