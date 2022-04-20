package com.d3h.diariodebordo.model;

import com.d3h.diariodebordo.database.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by d3h on 15/04/18.
 */

public class Usuario {
    private String id;
    private String nome;
    private String email;
    private String senha;
    private String tipoConta;


    public Usuario(){

    }

    public void salvar(){
        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        referenciaFirebase.child("usuarios").child( getId() ).setValue( this );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(String tipoConta) {
        this.tipoConta = tipoConta;
    }

}
