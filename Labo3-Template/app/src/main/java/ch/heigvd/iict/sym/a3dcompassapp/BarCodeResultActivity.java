package ch.heigvd.iict.sym.a3dcompassapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by pierre-samuelrochat on 20.12.17.
 */

public class BarCodeResultActivity extends AppCompatActivity {

    // For logging purposes
    private static final String TAG = BarCodeResultActivity.class.getSimpleName();
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code);

        // Gets the string stored in the intent and displays it
        Intent intent = getIntent();
        textView = findViewById(R.id.result_text);
        textView.setText(intent.getStringExtra("RESULT"));
    }
}
