package ch.heigvd.iict.sym.a3dcompassapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by pierre-samuelrochat on 21.12.17.
 */

public class NFCActivity extends AppCompatActivity {

    // For log purposes
    private static final String TAG = NFCActivity.class.getSimpleName();

    private static final int AUTHENTIFICATE_DEFAULT = 15;
    private static final int AUTHENTIFICATE_MAX = 10;
    private static final int AUTHENTIFICATE_MEDIUM = 5;
    private static final int AUTHENTIFICATE_LOW = 0;

    private int authentificateLevel;

    private Handler handler;
    private final int DELAY = 1000;

    private TextView textView;

    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        textView = findViewById(R.id.authentificate_text);

        authentificateLevel = AUTHENTIFICATE_DEFAULT;
        textView.setText(getString(R.string.authentificate_high_text));
        textView.setTextColor(Color.GREEN);

        handler = new Handler();

        handler.postDelayed(new Runnable(){
            public void run(){

                // Updates authentificate level
                if(authentificateLevel > AUTHENTIFICATE_LOW) {
                    authentificateLevel--;
                }

                // Does stuff according to authentificateLevel
                if(authentificateLevel < AUTHENTIFICATE_MEDIUM) {
                    textView.setText(getString(R.string.authentificate_low_text));
                    textView.setTextColor(Color.RED);
                } else if(authentificateLevel > AUTHENTIFICATE_MEDIUM && authentificateLevel < AUTHENTIFICATE_MAX) {
                    textView.setText(getString(R.string.authentificate_medium_text));
                    textView.setTextColor(Color.YELLOW);
                } else if(authentificateLevel > AUTHENTIFICATE_MAX) {
                    textView.setText(getString(R.string.authentificate_high_text));
                    textView.setTextColor(Color.GREEN);
                }
                handler.postDelayed(this, DELAY);
            }
        }, DELAY);

        // Check for available NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupForegroundDispatch();

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopForegroundDispatch();
    }

    // When NFC near device
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {

            Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if (rawMessages != null) {

                NdefMessage[] messages = new NdefMessage[rawMessages.length];
                for (int i = 0; i < rawMessages.length; i++) {
                    messages[i] = (NdefMessage) rawMessages[i];
                }

                // Extract message
                String tagMessage = new String(messages[0].getRecords()[0].getPayload());
                Log.i(TAG, tagMessage);

                // Restore default authentificate level
                if(tagMessage.equals(getString(R.string.ndef_password))) {
                    authentificateLevel = AUTHENTIFICATE_DEFAULT;
                    textView.setText(getString(R.string.authentificate_high_text));
                    textView.setTextColor(Color.GREEN);
                }
            }
        }
    }

    // called in onResume()
    private void setupForegroundDispatch() {

        if(mNfcAdapter == null)
            return;

        final Intent intent = new Intent(this.getApplicationContext(), this.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent =  PendingIntent.getActivity(this.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            Log.e(TAG, "MalformedMimeTypeException", e);
        }
        mNfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, techList);
    }

    // called in onPause()
    private void stopForegroundDispatch() {

        if(mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }
}
