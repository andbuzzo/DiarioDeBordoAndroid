package com.d3h.diariodebordo.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.d3h.diariodebordo.R;
import com.d3h.diariodebordo.database.ConfiguracaoFirebase;
import com.d3h.diariodebordo.model.Lista;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NovoActivity extends AppCompatActivity {

    private DatabaseReference firebaseReferencia;
    private FirebaseAuth firebaseAuth;
    private Spinner spinnerLinha;
    private Spinner spinnerMaquina;
    private Spinner spinnerTipoParada;
    private EditText txtDescricao;
    private Button btnConfirmar;
    private List<String> linhas;
    private List<String> maquinas;
    private List<String> tipoParadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo);

        spinnerLinha = (Spinner) findViewById(R.id.spinnerLinha);
        spinnerMaquina = (Spinner) findViewById(R.id.spinnerMaquina);
        spinnerTipoParada = (Spinner) findViewById(R.id.spinnerTipoParada);
        txtDescricao = (EditText) findViewById(R.id.txtDescricao);
        btnConfirmar = (Button) findViewById(R.id.btnConfirmar);

        firebaseAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();

        firebaseReferencia = ConfiguracaoFirebase.getFirebase();
        firebaseReferencia.child("linha").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                linhas = new ArrayList<String>();
                linhas.add("SELECIONE A LINHA:");

                for (DataSnapshot linhaSnapshot : dataSnapshot.getChildren()) {
                    String linhaName = linhaSnapshot.getValue(String.class);
                    linhas.add(linhaName);
                }


                ArrayAdapter<String> linhasAdapter = new ArrayAdapter<String>(NovoActivity.this, android.R.layout.simple_spinner_item, linhas);
                linhasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinnerLinha.setAdapter(linhasAdapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        firebaseReferencia.child("maquina").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                maquinas = new ArrayList<String>();
                maquinas.add("SELECIONE A MÁQUINA:");

                for (DataSnapshot maquinaSnapshot : dataSnapshot.getChildren()) {
                    String maquinaName = maquinaSnapshot.getValue(String.class);
                    maquinas.add(maquinaName);
                }


                ArrayAdapter<String> maquinasAdapter = new ArrayAdapter<String>(NovoActivity.this, android.R.layout.simple_spinner_item, maquinas);
                maquinasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinnerMaquina.setAdapter(maquinasAdapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        firebaseReferencia.child("tipoParada").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                tipoParadas = new ArrayList<String>();
                tipoParadas.add("SELECIONE TIPO PARADA:");

                for (DataSnapshot tipoParadaSnapshot : dataSnapshot.getChildren()) {
                    String tipoParadaName = tipoParadaSnapshot.getValue(String.class);
                    tipoParadas.add(tipoParadaName);
                }


                ArrayAdapter<String> tipoParadaAdapter = new ArrayAdapter<String>(NovoActivity.this, android.R.layout.simple_spinner_item, tipoParadas);
                tipoParadaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinnerTipoParada.setAdapter(tipoParadaAdapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Lista dado = new Lista();
                Date d = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat sdh = new SimpleDateFormat("dd/MM/yyyy");
                Date hora = Calendar.getInstance().getTime();
                String horaFormatada = sdf.format(hora);
                String dataFormatada = sdh.format(d);

                if(spinnerLinha.getSelectedItem().toString().equalsIgnoreCase("SELECIONE A LINHA:")){
                    Toast.makeText(NovoActivity.this, "Selecione uma Linha!", Toast.LENGTH_SHORT).show();
                }else if (spinnerMaquina.getSelectedItem().toString().equalsIgnoreCase("SELECIONE A MÁQUINA:")){
                    Toast.makeText(NovoActivity.this, "Selecione uma Máquina!", Toast.LENGTH_LONG).show();
                }else if (spinnerTipoParada.getSelectedItem().toString().equalsIgnoreCase("SELECIONE TIPO PARADA:")){
                    Toast.makeText(NovoActivity.this, "Selecione um tipo de parada!", Toast.LENGTH_LONG).show();
                }else if (txtDescricao.getText().toString().equals("")){
                    Toast.makeText(NovoActivity.this, "Campo Observação não pode ser vazio!", Toast.LENGTH_LONG).show();
                }else {
                    dado.setLinha(spinnerLinha.getSelectedItem().toString());
                    dado.setMaquina(spinnerMaquina.getSelectedItem().toString());
                    dado.setTipoParada(spinnerTipoParada.getSelectedItem().toString());
                    dado.setDescricao(txtDescricao.getText().toString());
                    dado.setData(dataFormatada);
                    dado.setHora(horaFormatada);
                    dado.setUidUserLogado(firebaseAuth.getCurrentUser().getUid().toString());

                    firebaseReferencia = ConfiguracaoFirebase.getFirebase();
                    firebaseReferencia.child("usuarios").child(firebaseAuth.getCurrentUser().getUid()).child("nome").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            dado.setNomeUsuario(dataSnapshot.getValue().toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    DatabaseReference reference = ConfiguracaoFirebase.getFirebase();
                    reference.child("listas");
                    String myKey = reference.push().getKey();
                    dado.setKey(myKey);
                    dado.salvar(myKey);
                    finish();
                    Intent intent = new Intent(NovoActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(NovoActivity.this, "Item cadastrado com sucesso", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}