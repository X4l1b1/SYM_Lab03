package ch.heigvd.iict.sym.a3dcompassapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

/**
 * Created by pierre-samuelrochat on 08.12.17.
 */

public class NFCLoginActivity extends AppCompatActivity {

    // UI elements
    EditText email;
    EditText password;
    Switch securityDegree;
    Button loginButton;

    // For logging purposes
    private static final String TAG = NFCLoginActivity.class.getSimpleName();

    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_login);

        // Creates UI elements
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        securityDegree = findViewById(R.id.security_degree);
        loginButton = findViewById(R.id.button);

        // Check for available NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(TAG, email.getText().toString() + " vs " + getString(R.string.user_email));
                Log.i(TAG, password.getText().toString() + " vs " + getString(R.string.user_password));
                if(!securityDegree.isChecked()) {
                    if ((email.getText().toString()).equals(getString(R.string.user_email))
                            && (password.getText().toString()).equals(getString(R.string.user_password))) {
                        Intent intent2 = new Intent(NFCLoginActivity.this, NFCActivity.class);
                        NFCLoginActivity.this.startActivity(intent2);
                    } else {
                        Toast.makeText(v.getContext(), "Failed to LOG IN", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
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
                Log.i(TAG, getString(R.string.ndef_password));

                if(securityDegree.isChecked()) {
                    // Password and NFC required
                    if((email.getText().toString()).equals(getString(R.string.user_email))
                            && (password.getText().toString()).equals(getString(R.string.user_password))
                            && tagMessage.equals(getString(R.string.ndef_password))) {

                        Intent intent2 = new Intent(NFCLoginActivity.this, NFCActivity.class);
                        NFCLoginActivity.this.startActivity(intent2);

                    } else {
                        Toast.makeText(this, "Failed to LOG IN", Toast.LENGTH_LONG).show();
                    }

                } else {
                    // Password or NFC required
                    
                    // Check if NFC message id right
                    if((email.getText().toString()).equals(getString(R.string.user_email))
                            && tagMessage.equals(getString(R.string.ndef_password))) {

                        Intent intent2 = new Intent(NFCLoginActivity.this, NFCActivity.class);
                        NFCLoginActivity.this.startActivity(intent2);

                    } else {
                        Toast.makeText(this, "Failed to LOG IN", Toast.LENGTH_LONG).show();
                    }
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
