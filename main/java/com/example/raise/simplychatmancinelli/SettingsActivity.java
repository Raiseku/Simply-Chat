package com.example.raise.simplychatmancinelli;
import android.app.ProgressDialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity {

    private Button bCancella;
    ProgressDialog dialog1;
    private TextView tvUsername, tvEmail;
    private String user_name; //parametro passato dall'attività Main
    public SettingsActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        user_name = getIntent().getExtras().get("Username").toString();
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        dialog1 = new ProgressDialog(SettingsActivity.this);
        bCancella = (Button) findViewById(R.id.bCancella);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            String Email = user.getEmail(); //uso una funzione di Firebase che mi permette direttamente di prendere l'email dell'utente
            tvUsername.setText(user_name);
            tvEmail.setText(Email);
        }
        bCancella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminaAccount(view);
            }
        });
        }

    public void eliminaAccount(View v)
    {
        final FirebaseUser current_users = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_users.getUid();
        final  DatabaseReference utente = FirebaseDatabase.getInstance().getReference("Utenti").child(uid);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(SettingsActivity.this);
        builder1.setMessage("Sei sicuro di Voler Eliminare i tuoi dati?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if(current_users != null){

                           dialog1.setMessage("Disattivazione in corso...");
                           dialog1.show();

                            utente.removeValue();
                            current_users.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        Toast.makeText(SettingsActivity.this, "Cancellazione avvenuta con successo!", Toast.LENGTH_SHORT).show();

                                        Intent startIntent = new Intent(SettingsActivity.this, StartActivity.class);
                                        startActivity(startIntent);
                                        dialog1.dismiss();
                                    } else {
                                        dialog1.dismiss();
                                        Toast.makeText(SettingsActivity.this, "Errore durante la cancellazione, Riprovare.", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    }



