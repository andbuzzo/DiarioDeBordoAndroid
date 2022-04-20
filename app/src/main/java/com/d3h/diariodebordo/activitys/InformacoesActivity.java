package com.d3h.diariodebordo.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.d3h.diariodebordo.R;
import com.d3h.diariodebordo.database.ConfiguracaoFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class InformacoesActivity extends AppCompatActivity {

    private TextView linhaMaquina;
    private TextView tipoParada;
    private TextView dataCriacao;
    private TextView horaCriacao;
    private TextView usuario;
    private TextView descricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes);

        Bundle dados = getIntent().getExtras();

        linhaMaquina = (TextView) findViewById(R.id.txtMaquinaLinha);
        tipoParada = (TextView) findViewById(R.id.txtTipoParada);
        dataCriacao = (TextView) findViewById(R.id.txtDataCriacao);
        horaCriacao = (TextView) findViewById(R.id.txtHoraCriacao);
        usuario = (TextView) findViewById(R.id.txtNomeUsuario);
        descricao = (TextView) findViewById(R.id.txtObservacao);

        DatabaseReference reference = ConfiguracaoFirebase.getFirebase();
        reference.child("usuarios").child(dados.get("UID").toString()).child("nome").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String data = dataSnapshot.getValue().toString();
                usuario.setText(data);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        linhaMaquina.setText(dados.get("LINHA").toString()+ " " + dados.get("MAQUINA").toString());
        tipoParada.setText(dados.get("TIPOPARADA").toString());
        dataCriacao.setText(dados.get("DATA").toString());
        horaCriacao.setText(dados.get("HORA").toString());
//        usuario.setText(dados.get("USUARIO").toString());
        descricao.setText(dados.get("DESCRICAO").toString());


    }
}
