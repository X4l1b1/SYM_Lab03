package ch.heigvd.iict.sym.a3dcompassapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Created by pierre-samuelrochat on 08.12.17.
 */

public class BarCodeScannerActivity extends AppCompatActivity {

    // For logging purposes
    private static final String TAG = BarCodeScannerActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Creates activity form ZXING library
        new IntentIntegrator(this).initiateScan();
    }

    // Get the results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Scanning Cancelled", Toast.LENGTH_LONG).show();
            } else {
                // Creates activity to display result of scan
                Intent intent = new Intent(BarCodeScannerActivity.this, BarCodeResultActivity.class);
                // Put result of scanning in the intent
                intent.putExtra("RESULT", result.getContents());
                // Start the new activity
                BarCodeScannerActivity.this.startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}
