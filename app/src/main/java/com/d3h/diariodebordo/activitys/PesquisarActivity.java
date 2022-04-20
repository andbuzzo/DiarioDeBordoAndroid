package com.d3h.diariodebordo.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.d3h.diariodebordo.R;
import com.d3h.diariodebordo.database.ConfiguracaoFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class PesquisarActivity extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextView;
    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar);

        firebase = ConfiguracaoFirebase.getFirebase();
        final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        firebase.child("filtro").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot dados :dataSnapshot.getChildren()){
                    String valor = dados.getValue(String.class);
                    autoComplete.add(valor);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewPesquisar);
        autoCompleteTextView.setAdapter(autoComplete);
        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Intent intent = new Intent(PesquisarActivity.this, MainActivity.class);
                    intent.putExtra("textoPesquisa", autoCompleteTextView.getText().toString());
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }

        });


    }
}