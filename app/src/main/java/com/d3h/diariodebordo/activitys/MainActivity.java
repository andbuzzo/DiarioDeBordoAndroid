package com.d3h.diariodebordo.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.d3h.diariodebordo.R;
import com.d3h.diariodebordo.adapter.ListaAdapter;
import com.d3h.diariodebordo.database.ConfiguracaoFirebase;
import com.d3h.diariodebordo.model.Lista;
import com.d3h.diariodebordo.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listView;
    private ArrayAdapter<Lista> adapter;
    private ArrayList<Lista> lista;
    private Toolbar toolbar;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerLista;
    private FirebaseAuth firebaseAuth;
    private TextView emailLogado;
    private TextView userLogado;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NovoActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        firebase = ConfiguracaoFirebase.getFirebase();
        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        emailLogado = (TextView) header.findViewById(R.id.txt_email_logado);
        userLogado = (TextView) header.findViewById(R.id.txt_usuario_logado);

        emailLogado.setText(auth.getCurrentUser().getEmail().toString());

        firebase.child("usuarios").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                userLogado.setText(usuario.getNome().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView = (ListView) findViewById(R.id.list_view_dados);

        //montar listview e adapter
        lista = new ArrayList<>();
        adapter = new ListaAdapter(MainActivity.this, lista);
        listView.setAdapter( adapter );

        //Recuperar mensagens do firebase;
        firebase = ConfiguracaoFirebase.getFirebase().child("lista");

        valueEventListenerLista = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lista.clear();

                for(DataSnapshot dados: dataSnapshot.getChildren()){
                    Lista listaDados = dados.getValue(Lista.class);
                    Bundle extras = getIntent().getExtras();
                    String pesquisa = "";
                    if (extras != null) {
                        if (extras.containsKey("textoPesquisa")) {
                            pesquisa = getIntent().getExtras().get("textoPesquisa").toString();

                        }else {
                            pesquisa = "";
                        }
                    }
                    if (pesquisa.isEmpty()){
                        lista.add(listaDados);
                    }else if(pesquisa.equalsIgnoreCase(listaDados.getLinha()) || pesquisa.equalsIgnoreCase(listaDados.getMaquina())
                            || pesquisa.equalsIgnoreCase(listaDados.getData()) || pesquisa.equalsIgnoreCase(listaDados.getHora())
                            || pesquisa.equalsIgnoreCase(listaDados.getDescricao()) || pesquisa.equalsIgnoreCase(listaDados.getNomeUsuario())
                            || pesquisa.equalsIgnoreCase(listaDados.getTipoParada())
                            || pesquisa.equalsIgnoreCase(listaDados.getMaquina()+ " " + listaDados.getLinha())){

                        lista.add(listaDados);
                    }
                }

                Collections.sort(lista);

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        firebase.addValueEventListener( valueEventListenerLista );



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Lista dado = lista.get(position);
                Intent intent = new Intent(MainActivity.this, InformacoesActivity.class);
                intent.putExtra("LINHA", dado.getLinha());
                intent.putExtra("MAQUINA", dado.getMaquina());
                intent.putExtra("TIPOPARADA", dado.getTipoParada());
                intent.putExtra("DESCRICAO", dado.getDescricao());
                intent.putExtra("DATA", dado.getData());
                intent.putExtra("HORA", dado.getHora());
                intent.putExtra("USUARIO", dado.getNomeUsuario());
                intent.putExtra("UID", dado.getUidUserLogado());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.item_pesquisa:
                abrirIntentPesquisa();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_meus_itens) {
            Intent intent = new Intent(MainActivity.this, MeusItemsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_novo) {
            Intent intent = new Intent(MainActivity.this, NovoActivity.class);
            startActivity(intent);
       } else if (id == R.id.nav_desconectar){
            deslogarUsuario();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void abrirIntentPesquisa(){
        Intent intent = new Intent(MainActivity.this, PesquisarActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebase.addListenerForSingleValueEvent(valueEventListenerLista);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerLista);
    }

    public void deslogarUsuario(){
        auth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
