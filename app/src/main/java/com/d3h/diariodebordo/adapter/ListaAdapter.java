package com.d3h.diariodebordo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.d3h.diariodebordo.R;
import com.d3h.diariodebordo.model.Lista;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by d3h on 15/04/18.
 */

public class ListaAdapter extends ArrayAdapter<Lista> {

    private ArrayList<Lista> listas;
    private Context context;
    private TextView titulo;
    private TextView descricao;

    public ListaAdapter(@NonNull Context c, @NonNull ArrayList<Lista> objects) {
        super(c, 0, objects);
        this.context = c;
        this.listas = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;
        // verifica se a lista está preenchida
        if(listas != null){

            //inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //recuperar lista
            Lista lista = listas.get(position);
            //montar view
            if (lista.getTipoParada().equals("operacional")){
                view = inflater.inflate(R.layout.listar_lista_operador, parent, false);

            }else if (lista.getTipoParada().equals("mecanica")){
                view = inflater.inflate(R.layout.listar_lista_mecanica, parent, false);

            }else if (lista.getTipoParada().equals("eletrica")){
                view = inflater.inflate(R.layout.listar_lista_eletrica, parent, false);

            }else if (lista.getTipoParada().equals("externa")){
                view = inflater.inflate(R.layout.listar_lista_externa, parent, false);

            }

            titulo = (TextView) view.findViewById(R.id.tv_titulo);
            descricao = (TextView) view.findViewById(R.id.txt_descricao_parada);

            titulo.setText(lista.getMaquina() + " " + lista.getLinha());
            descricao.setText("Observações: " + lista.getDescricao());
        }

        return view;
    }
}
