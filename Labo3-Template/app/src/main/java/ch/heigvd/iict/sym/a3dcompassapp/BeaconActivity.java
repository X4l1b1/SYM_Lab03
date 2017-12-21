package ch.heigvd.iict.sym.a3dcompassapp;

import android.Manifest;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by pierre-samuelrochat on 08.12.17.
 */

public class BeaconActivity extends AppCompatActivity implements BeaconConsumer {

    protected static final String BEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    // For logging purposes
    private static final String TAG = BeaconActivity.class.getSimpleName();
    private ListView listView;

    private BeaconManager beaconManager;
    List<String> data = new ArrayList();
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);


        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) { /* ... */ }
        }).check();


        // Create and bind a Beacon Manager for our consumer
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BEACON_LAYOUT));
        beaconManager.bind(this);

        listView = findViewById(R.id.listView);
        data = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    /*
     * Called by the BeaconManager callback provided when ready to use
     */
    @Override
    public void onBeaconServiceConnect(){
        beaconManager.addRangeNotifier(new RangeNotifier() {
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    Log.i(TAG, "The first beacon I see is about "+beacons.iterator().next().getDistance()+" meters away.");
                    data.clear();
                    for (Beacon beacon : beacons) {
                        data.add("UUID: " + beacon.getId1()
                                + "\nMAJOR: " + beacon.getId2()
                                + "\nMINOR: " + beacon.getId3()
                                + "\nRSSI: " + beacon.getRssi()
                                + "\nTX: " + beacon.getTxPower()
                                + "\nDISTANCE: " + beacon.getDistance());
                    }
                    updateList();

                    adapter.notifyDataSetChanged();

                }
            }
        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
    public void updateList(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.add(data);

            }
        });
    }
}
