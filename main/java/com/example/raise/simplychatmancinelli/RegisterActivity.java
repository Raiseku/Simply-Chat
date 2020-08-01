package com.example.raise.simplychatmancinelli;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView tEmail;
    private TextView tPassword;
    private Button creaButton;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance(); //firebase attributo
        tEmail = (TextView) findViewById(R.id.tEmail);
        tPassword = (TextView) findViewById(R.id.tPassword);
        creaButton = (Button) findViewById(R.id.creaButton);

        creaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = tEmail.getText().toString();
                String password = tPassword.getText().toString();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this, "Registrazione Fallita. Controlla le Credenziali.", Toast.LENGTH_SHORT).show();
                } else {
                    //ora registro tutto su Firebase
                    register_user(email, password);
                }
            }
        });
    }
    private void register_user(final String email, final String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override


                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { //l'user è registrato

                            FirebaseUser current_users = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = current_users.getUid();
                            //linko il database alla lista generale degli utenti
                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Utenti").child(uid);

                            //creo dei figli per ogni utente che si registra e Aggiorno il DB
                            HashMap<String, String > userMap = new HashMap<>();
                            userMap.put("Email", email);
                            userMap.put("Password", md5(password)); //cripto la password così che dalla Console l'admin non possa vederla

                            mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //linea necessaria per non permettere all'utente di poter tornare alla pagina di registrazione
                                        startActivity(mainIntent);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Autenticazione Fallita. Controlla le Credenziali", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    //Codice per l'Hashing della password utente
    public static String md5(String s)
    {
        MessageDigest digest;
        try
        {
            digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes(Charset.forName("US-ASCII")),0,s.length());
            byte[] magnitude = digest.digest();
            BigInteger bi = new BigInteger(1, magnitude);
            String hash = String.format("%0" + (magnitude.length << 1) + "x", bi);
            return hash;
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return "";
    }
}
