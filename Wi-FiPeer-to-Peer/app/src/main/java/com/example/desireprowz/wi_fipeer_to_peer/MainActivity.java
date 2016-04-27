package com.example.desireprowz.wi_fipeer_to_peer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends Activity {

    WifiP2pManager mManager;
    Channel mChannel;
    BroadcastReceiver mReceiver;

    IntentFilter mIntentFilter;

    WifiP2pDevice device;
    WifiP2pConfig config;
    WifiP2pDeviceList peerList;

    private List peers;

    //UI
    private Button discoverPeerButton;
    private Button connectToPeerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(mManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(mManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(mManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(mManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        discoverPeerButton = (Button) findViewById(R.id.discoverPeers);
        discoverPeerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mManager.discoverPeers(mChannel, new ActionListener() {
                    @Override
                    public void onSuccess() {
                        onPeersAvailable(peerList);
                        Log.i("discoverPeers", "SUCCESS");
                    }

                    @Override
                    public void onFailure(int reasonCode) {
                        Log.d("discoverPeers", "FAILURE: " + Integer.toString(reasonCode));
                    }
                });
            }
        });

        connectToPeerButton = (Button) findViewById(R.id.connectToPeer);
        connectToPeerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                config = new WifiP2pConfig();
                config.deviceAddress = device.deviceAddress;

                Log.i("Device: ", device.toString());
                Log.i("DeviceAdress: ", device.deviceAddress.toString());

                mManager.connect(mChannel, config, new ActionListener() {
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

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    public void onPeersAvailable(WifiP2pDeviceList peerList) {
        List peersAvailableList = new ArrayList((Collection) peerList);
        for (int i = 0; i < peerList.getDeviceList().size(); i++) {
            device = (WifiP2pDevice) peersAvailableList.get(i);

            if (device.deviceAddress == "") {

            }
        }

        peers.clear();
        peers.addAll(peerList.getDeviceList());
        Log.e("Peers Added:", peers.toString());

        if (peers.size() == 0) {
            Log.d("Peerssize0:", "No devices found");
            return;
        }
    }

}
