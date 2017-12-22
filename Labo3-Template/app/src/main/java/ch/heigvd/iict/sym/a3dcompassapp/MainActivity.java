package ch.heigvd.iict.sym.a3dcompassapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by pierre-samuelrochat on 08.12.17.
 */

public class MainActivity extends AppCompatActivity {

    // For logging purposes
    private static final String TAG = MainActivity.class.getSimpleName();

    // Buttons
    private Button barCodeButton   = null;
    private Button beaconButton    = null;
    private Button nfcButton       = null;
    private Button compassButton   = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link to GUI elements -- defined in res/layout/
        this.barCodeButton   = findViewById(R.id.button_bar_code);
        this.beaconButton    = findViewById(R.id.button_beacon);
        this.nfcButton       = findViewById(R.id.button_nfc);
        this.compassButton   = findViewById(R.id.button_captors);

        barCodeButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, BarCodeActivity.class);
                MainActivity.this.startActivity(intent);
            }


        });

        beaconButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ch.heigvd.iict.sym.a3dcompassapp.BeaconActivity.class);
                MainActivity.this.startActivity(intent);
            }


        });

        nfcButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, NFCLoginActivity.class);
                MainActivity.this.startActivity(intent);
            }


        });

        compassButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ch.heigvd.iict.sym.a3dcompassapp.CompassActivity.class);
                MainActivity.this.startActivity(intent);
            }


        });

    }
}
