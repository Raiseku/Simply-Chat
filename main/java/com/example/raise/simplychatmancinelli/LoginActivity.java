package com.example.raise.simplychatmancinelli;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView tEmailLogin;
    private TextView tPasswordLogin;
    private Button loginButton;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private CheckBox mCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        tEmailLogin = (TextView) findViewById(R.id.tEmailLogin);
        tPasswordLogin = (TextView) findViewById(R.id.tPasswordLogin);
        loginButton = (Button) findViewById(R.id.loginButton);
        mCheckBox = (CheckBox) findViewById(R.id.mCheckBox);

        //Gestione delle ShareedPrefgerences:
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit(); //Immagazzino le Shared preferences nell'editor

        checkSharedPreferences(); //chiamo il metodo in fondo al codice
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = tEmailLogin.getText().toString();
                String password = tPasswordLogin.getText().toString();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Autenticazione Fallita. Controlla le Credenziali.", Toast.LENGTH_SHORT).show();
                } else if (mCheckBox.isChecked()) {
                    //l'utente vuole salvare le credenziali
                    mEditor.putString(getString(R.string.checkbox), "True");
                    mEditor.commit();

                    String Email = tEmailLogin.getText().toString();
                    mEditor.putString(getString(R.string.Email), Email);
                    mEditor.commit();

                    String Password = tPasswordLogin.getText().toString();
                    mEditor.putString(getString(R.string.password), password);
                    mEditor.commit();
                    loginUser(email, password);
                } else {
                    //l'utente non vuole salvare le credenziali
                    mEditor.putString(getString(R.string.checkbox), "False");
                    mEditor.commit();

                    mEditor.putString(getString(R.string.Email), "");
                    mEditor.commit();

                    mEditor.putString(getString(R.string.password), "");
                    mEditor.commit();
                    loginUser(email, password);
                }
            }
        });
    }
    private void loginUser(String email, String password) {
    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {

            if(task.isSuccessful()){
                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //linea necessaria per non permettere all'utente di poter tornare alla pagina iniziale
                startActivity(mainIntent);
                finish();
            }  else {
                Toast.makeText(LoginActivity.this, "Autenticazione Fallita. Controlla le Credenziali.", Toast.LENGTH_SHORT).show();
            }
                    }
    });
    }

    private void checkSharedPreferences(){
        //controllo se ci sono Credenziali salvate all'interno della memoria
        String checkbox = mPreferences.getString(getString(R.string.checkbox), "False");
        String Email = mPreferences.getString(getString(R.string.Email), "");
        String password = mPreferences.getString(getString(R.string.password), "");
        //creo all'interno del file Stringhe Values -> Strings.xml

        tEmailLogin.setText(Email);
        tPasswordLogin.setText(password);

        if(checkbox.equals("True")){
            mCheckBox.setChecked(true);
        } else {
            mCheckBox.setChecked(false);
        }
    }
}
