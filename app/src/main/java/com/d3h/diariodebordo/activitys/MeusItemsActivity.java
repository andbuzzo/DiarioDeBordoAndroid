package com.d3h.diariodebordo.activitys;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.d3h.diariodebordo.R;
import com.d3h.diariodebordo.adapter.ListaAdapter;
import com.d3h.diariodebordo.database.ConfiguracaoFirebase;
import com.d3h.diariodebordo.model.Lista;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class MeusItemsActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<Lista> adapter;
    private ArrayList<Lista> listas;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerConversas;
    private FirebaseAuth firebaseAuth;
    private Toolbar toolbar;





    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_items);

        listas = new ArrayList<>();
        listView = (ListView) findViewById(R.id.lv_items);
        adapter = new ListaAdapter(MeusItemsActivity.this, listas);
        listView.setAdapter( adapter );
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();

        //recuperar conversas do firebase
        firebase = ConfiguracaoFirebase.getFirebase().child("lista");

        valueEventListenerConversas = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Limpar Lista
                listas.clear();
                for(DataSnapshot dados: dataSnapshot.getChildren()){
                    Lista lista = dados.getValue(Lista.class);
                    String uidUsuarioLogado = firebaseAuth.getCurrentUser().getUid().toString();

                    if (lista.getUidUserLogado().equals(uidUsuarioLogado)){
                        listas.add(lista);
                    }
                }

                Collections.sort(listas);
                //Avisar que ouve auteração
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final Lista dados = listas.get(position);
                new AlertDialog.Builder(MeusItemsActivity.this)
                        .setTitle("Item")
                        .setMessage("Escolha uma Opção:")
                        .setPositiveButton("Ver Item",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(MeusItemsActivity.this, InformacoesActivity.class);
                                        intent.putExtra("LINHA", dados.getLinha());
                                        intent.putExtra("MAQUINA", dados.getMaquina());
                                        intent.putExtra("TIPOPARADA", dados.getTipoParada());
                                        intent.putExtra("DESCRICAO", dados.getDescricao());
                                        intent.putExtra("DATA", dados.getData());
                                        intent.putExtra("HORA", dados.getHora());
                                       // intent.putExtra("USUARIO", dados.getNomeUsuario());
                                        intent.putExtra("UID", dados.getUidUserLogado());
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                        .setNegativeButton("Remover Item", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Lista lista = listas.get(position);
                                firebase = ConfiguracaoFirebase.getFirebase().child("lista");
                                firebase.getRef().child(lista.getKey()).removeValue();
                                recreate();

                            }
                        })
                        .show();
            }

        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        firebase.addListenerForSingleValueEvent(valueEventListenerConversas);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerConversas);
    }
}
