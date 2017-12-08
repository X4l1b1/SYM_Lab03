package ch.heigvd.iict.sym.a3dcompassapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by pierre-samuelrochat on 08.12.17.
 */

public class BarCodeActivity extends AppCompatActivity {

    // For logging purposes
    private static final String TAG = BarCodeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code);

    }
}
