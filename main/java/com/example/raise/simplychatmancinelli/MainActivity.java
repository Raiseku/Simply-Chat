package com.example.raise.simplychatmancinelli;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private Button bLogout;
    private Button bSettings;
    private Button bChat;
    private TextView Nome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        bLogout = (Button) findViewById(R.id.bLogout);
        bSettings = (Button) findViewById(R.id.bSettings);
        bChat = (Button) findViewById(R.id.bChat);
        Nome = (TextView) findViewById(R.id.txName);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit(); //Immagazzino le Shared preferences nell'editor


        bChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username = Nome.getText().toString();

                if(TextUtils.isEmpty(Username)){

                    Toast.makeText(MainActivity.this, "Inserisci il Nome da utilizzare nella Chat!.", Toast.LENGTH_SHORT).show();
                } else {

                    //Salvo il nome nelle variabili locali e disabilito la PlainText
                    String username = Nome.getText().toString();
                    mEditor.putString(getString(R.string.Username), username);
                    mEditor.commit();

                    disableEditText(Nome);
                    //Salvo il nome nelle Shared Preferences
                    Intent startIntent = new Intent(MainActivity.this, ChatActivity.class);
                    startIntent.putExtra("Username", Username); //passo il valore appena inmesso all'altra attività
                    startActivity(startIntent);
                }
            }
        });

        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Username = Nome.getText().toString();

                if (TextUtils.isEmpty(Username)) {

                    Toast.makeText(MainActivity.this, "Inserisci il Nome da utilizzare nella Chat!.", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseAuth.getInstance().signOut();
                    tornaInizio();

                }
            }
        });

        bSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Username = Nome.getText().toString();

                if(TextUtils.isEmpty(Username)) {
                    Toast.makeText(MainActivity.this, "Inserisci il Nome da utilizzare nella Chat!.", Toast.LENGTH_SHORT).show();
                } else {
                    disableEditText(Nome);
                    Intent startIntent = new Intent(MainActivity.this, SettingsActivity.class);
                    startIntent.putExtra("Username", Username);
                    startActivity(startIntent);
                }

            }
        });
    }

    private void tornaInizio() {

        Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(startIntent);
    }

    @Override
    public void onStart() {
        super.onStart();
        // salva nella variabile l'user correttamente loggato, se non fosse così allora NULL
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            tornaInizio();
        }
    }
    //Metodo per disabilitare la PlainText per l'inserimento dell'Username
    private void disableEditText(TextView Nome) {
        Nome.setFocusable(false);
        Nome.setEnabled(false);
        Nome.setCursorVisible(false);
        Nome.setKeyListener(null);
    }
}
