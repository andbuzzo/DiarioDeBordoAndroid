package com.d3h.diariodebordo.database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by andrelbuzzo on 01/12/17.
 */

public final class ConfiguracaoFirebase {

    private static DatabaseReference referenciaFirebase;
    private static FirebaseAuth autenticacao;
    private static FirebaseStorage storage;
    private static StorageReference storageReference;

    public static DatabaseReference getFirebase() {

        if (referenciaFirebase == null) {
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }

        return referenciaFirebase;
    }

    public static FirebaseAuth getFirebaseAutenticacao() {
        if (autenticacao == null) {
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }

    public static FirebaseStorage getFirebaseStorage() {
        if (storage == null) {
            storage = getFirebaseStorage().getInstance();
        }
        return storage;
    }

    public static StorageReference getStorageReference() {
        if (storageReference == null) {
            storageReference = FirebaseStorage.getInstance().getReference();
        }
        return storageReference;
    }
}
