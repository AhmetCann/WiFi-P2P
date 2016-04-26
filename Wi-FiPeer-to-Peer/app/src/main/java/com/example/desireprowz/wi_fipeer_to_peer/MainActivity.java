package com.example.desireprowz.wi_fipeer_to_peer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    WifiP2pManager mManager;
    Channel mChannel;
    BroadcastReceiver mReceiver;

    IntentFilter mIntentFilter;

    WifiP2pDevice device;
    WifiP2pConfig config;

    //UI
    private Button discoverPeerButton;
    private Button connectToPeerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        discoverPeerButton = (Button) findViewById(R.id.discoverPeers);
        connectToPeerButton = (Button) findViewById(R.id.connectToPeer);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(mManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(mManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(mManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(mManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;

        discoverPeerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Log.d("discoverPeers", "SUCCESS");
                    }

                    @Override
                    public void onFailure(int reasonCode) {
                        Log.d("discoverPeers", "FAILURE");
                    }
                });
            }
        });

        connectToPeerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Log.d("CONNECT", "CONNECTION SUCCES");
                    }

                    @Override
                    public void onFailure(int reason) {
                        Log.d("CONNECT", "CONNECTION FAILED");
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }
    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }


}
