package com.d3h.diariodebordo.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.d3h.diariodebordo.R;
import com.d3h.diariodebordo.database.ConfiguracaoFirebase;
import com.d3h.diariodebordo.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private Button btnLoginEntrar;
    private EditText txtLoginEmail;
    private EditText txtLoginSenha;
    private Usuario usuario;
    private FirebaseAuth autenticacao;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        if (autenticacao.getCurrentUser() != null) {
            abrirTelaPrincipal();
            return;

        } else {
            setContentView(R.layout.activity_login);

            btnLoginEntrar = (Button) findViewById(R.id.btnLoginEntrar);
            txtLoginEmail = (EditText) findViewById(R.id.txtLoginEmail);
            txtLoginSenha = (EditText) findViewById(R.id.txtLoginSenha);

            //verificarUsuarioLogado();
            mProgressDialog = new ProgressDialog(this);

            btnLoginEntrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    usuario = new Usuario();
                    usuario.setEmail(txtLoginEmail.getText().toString());
                    usuario.setSenha(txtLoginSenha.getText().toString());
                    validarLogin();

                }
            });
        }
    }

    private void validarLogin() {
        mProgressDialog.setMessage("Entrando ... ");
        mProgressDialog.show();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    abrirTelaPrincipal();
                    Toast.makeText(LoginActivity.this, "Sucesso ao fazer login!", Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();

                } else {
                    String msgErro = "";
                    mProgressDialog.hide();
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        msgErro = "e-mail não cadastrado no APP!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        msgErro = "senha incorreta!";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, "Erro: " + msgErro, Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void verificarUsuarioLogado() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        if (autenticacao.getCurrentUser() != null) {
            abrirTelaPrincipal();

        } else {

        }
    }

    private void abrirTelaPrincipal() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        referenciaFirebase.child("usuarios").child(autenticacao.getCurrentUser().getUid()).child("tipoConta").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("nome", dataSnapshot.getValue().toString());
                if (dataSnapshot.getValue().toString().equals("admin")) {
                    Intent intent = new Intent(LoginActivity.this, MainActivityAdmin.class);
                    startActivity(intent);
                    finish();
                } else if (dataSnapshot.getValue().toString().equals("user")) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
