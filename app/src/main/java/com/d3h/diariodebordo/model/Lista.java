package com.d3h.diariodebordo.model;

import android.support.annotation.NonNull;

import com.d3h.diariodebordo.database.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

/**
 * Created by d3h on 15/04/18.
 */

public class Lista implements Comparable<Lista> {

    private String linha;
    private String maquina;
    private String tipoParada;
    private String descricao;
    private String data;
    private String hora;
    private String nomeUsuario;
    private String uidUserLogado;
    private String key;

    public Lista() {
    }
    public void salvar(String myKey){
        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        referenciaFirebase.child("lista").child(myKey).setValue( this );
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLinha() {
        return linha;
    }

    public void setLinha(String linha) {
        this.linha = linha;
    }

    public String getMaquina() {
        return maquina;
    }

    public void setMaquina(String maquina) {
        this.maquina = maquina;
    }

    public String getTipoParada() {
        return tipoParada;
    }

    public void setTipoParada(String tipoParada) {
        this.tipoParada = tipoParada;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getUidUserLogado() {
        return uidUserLogado;
    }

    public void setUidUserLogado(String uidUserLogado) {
        this.uidUserLogado = uidUserLogado;
    }

    @Override
    public int compareTo(@NonNull Lista lista) {
        long dataConvertida = Date.parse(this.data);
        long dataCompare = Date.parse(lista.getData());

        if (dataConvertida > dataCompare) {
            return -1;
        }
        if (dataConvertida < dataCompare) {
            return 1;
        }
        return 0;
    }


}
