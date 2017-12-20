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

                Intent intent = new Intent(MainActivity.this, BarCodeScannerActivity.class);
                //   intent.putExtra(EXTRA_MESSAGE, "Async");

                //   MainActivity.this.startActivityForResult(intent, 1);
                MainActivity.this.startActivity(intent);
                // Wrong combination, display pop-up dialog and stay on login screen
                //    showErrorDialog(??, ??);
            }


        });

        beaconButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ch.heigvd.iict.sym.a3dcompassapp.BeaconActivity.class);
                //   intent.putExtra(EXTRA_MESSAGE, "Diff");

                //   MainActivity.this.startActivityForResult(intent, 1);
                MainActivity.this.startActivity(intent);
                // Wrong combination, display pop-up dialog and stay on login screen
                //    showErrorDialog(??, ??);
            }


        });

        nfcButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ch.heigvd.iict.sym.a3dcompassapp.NFCActivity.class);
                //   intent.putExtra(EXTRA_MESSAGE, "Object");

                //   MainActivity.this.startActivityForResult(intent, 1);
                MainActivity.this.startActivity(intent);
                // Wrong combination, display pop-up dialog and stay on login screen
                //    showErrorDialog(??, ??);
            }


        });

        compassButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ch.heigvd.iict.sym.a3dcompassapp.CompassActivity.class);
                //   intent.putExtra(EXTRA_MESSAGE, "Compr");

                //   MainActivity.this.startActivityForResult(intent, 1);
                MainActivity.this.startActivity(intent);
                // Wrong combination, display pop-up dialog and stay on login screen
                //    showErrorDialog(??, ??);
            }


        });

    }
}
