package ch.heigvd.iict.sym.a3dcompassapp;

import android.Manifest;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;



public class BeaconActivity extends AppCompatActivity implements BeaconConsumer {

    /* Layout of the type of Beacon we want to detect */
    protected static final String BEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    /* ForLogging Purpose */
    private static final String TAG = BeaconActivity.class.getSimpleName();

    /* To display iBeacon activity deteted information */
    private ListView listView;

    /* Used to detect, sort and parse Beacons */
    private BeaconManager beaconManager;
    List<String> data = new ArrayList();
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);

        /* Use of Dexter Library to ask for permissions (pop-up window)
         * Permissions needed : Bluetooth, Fine_Location
         * */
        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                Toast.makeText(getApplicationContext(), "Permission Ok", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) { /* ... */ }
        }).check();

        /* Create new BeaconManager
         * give it a BeaconParser and bind it to current Activity
         */
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BEACON_LAYOUT));
        beaconManager.bind(this);

        /*
         * Initialize data recipients
         */
        listView = findViewById(R.id.listView);
        data = new ArrayList<>();

        /*
         * Initialize adapter and bind it to the ListView
         */
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1  , data);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    /*
     * Method called upon detection of a Beacon
     * Uses BeaconManager's method addRangeNotifier to access all detected
     * Beacons and then proceed to extract informations from them
     * into a String
     */
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier((beacons, region) -> {
            if(beacons.size() > 0){
                /* Clear List to avoid duplication :
                 * all detected Beacons are parsed at each call
                 * the methods keeps no memory of previously detected ones
                 */
                data.clear();
                for (Beacon b : beacons) {
                    /* Add data to String List */
                    data.add("UUID: " + b.getId1()
                            + "\nMAJOR: " + b.getId2()
                            + "\nMINOR: " + b.getId3()
                            + "\nRSSI: " + b.getRssi()
                            + "\nTX: " + b.getTxPower()
                            + "\nDISTANCE: " + b.getDistance() + "m");
                }
                /* Update View */
            runOnUiThread(() -> adapter.notifyDataSetChanged());
            }
        });

        try {
            /* Tells BeaconService to start looking for Beacons  matching the given Region
             *  Provides an update on mDistance every ms while Beacons are visible
             */
            beaconManager.startRangingBeaconsInRegion(new Region("RanginUniqueId", null, null, null));
        } catch (RemoteException e) {
            Log.e(TAG, "BeaconActivity : RemoteException", e);
            e.printStackTrace();
        }
    }

}
