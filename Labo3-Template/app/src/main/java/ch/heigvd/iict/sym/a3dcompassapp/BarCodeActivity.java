package ch.heigvd.iict.sym.a3dcompassapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Created by pierre-samuelrochat on 08.12.17.
 */

public class BarCodeActivity extends AppCompatActivity {

    // For logging purposes
    private static final String TAG = BarCodeActivity.class.getSimpleName();
    private String resultString;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Creates activity form ZXING library
        setContentView(R.layout.activity_bar_code);
        textView = findViewById(R.id.result_text);
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
                // Gets result and display it in testView
                resultString = result.getContents();
                textView.setText(resultString);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}
