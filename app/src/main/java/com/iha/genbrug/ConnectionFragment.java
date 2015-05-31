package com.iha.genbrug;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectionFragment extends Fragment {


    private TextView textView;
    IntentFilter filter= new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
    private FragmentActivity activity;
    private LoginActivity loginActivity;
    private CreateAccountActivity createAccountActivity;



    public ConnectionFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_connection, container, false);
        textView = (TextView) rootView.findViewById(R.id.connectionTextView);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().registerReceiver(bcReceived, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(bcReceived);
    }

    // BroadcatReceiver to check internetconnection
    private BroadcastReceiver bcReceived = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final ConnectivityManager connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            final android.net.NetworkInfo wifi = connMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            final android.net.NetworkInfo mobile = connMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if(mobile != null && mobile.isConnected())
            {
                activity = getActivity();

                if(activity instanceof LoginActivity)
                {
                    loginActivity = (LoginActivity) activity;
                    loginActivity.enabledState(true);
                }
                if (activity instanceof CreateAccountActivity)
                {
                    createAccountActivity = (CreateAccountActivity) activity;
                    createAccountActivity.enabledState(true);
                }

                getActivity().findViewById(R.id.login_button).setEnabled(true);

                textView.setText(" ");

            }
            else if(wifi != null && wifi.isConnected())
            {
                activity = getActivity();

                if(activity instanceof LoginActivity)
                {
                    loginActivity = (LoginActivity) activity;
                    loginActivity.enabledState(true);
                }
                if (activity instanceof CreateAccountActivity)
                {
                    createAccountActivity = (CreateAccountActivity) activity;
                    createAccountActivity.enabledState(true);
                }

                getActivity().findViewById(R.id.login_button).setEnabled(true);

                textView.setText(" ");
            }
            else
            {
                activity = getActivity();

                if(activity instanceof LoginActivity)
                {
                    loginActivity = (LoginActivity) activity;
                    loginActivity.enabledState(false);
                }

                if (activity instanceof CreateAccountActivity)
                {
                    createAccountActivity = (CreateAccountActivity) activity;
                    createAccountActivity.enabledState(false);
                }

                getActivity().findViewById(R.id.login_button).setEnabled(false);

                textView.setText("No Internet Connection!");
            }
        }
    };

}
