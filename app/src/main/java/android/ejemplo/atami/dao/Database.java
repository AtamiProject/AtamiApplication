package android.ejemplo.atami.dao;

import android.ejemplo.atami.model.Cuenta_bancaria;
import android.ejemplo.atami.model.Tarjeta;
import android.ejemplo.atami.model.Transaccion;
import android.ejemplo.atami.model.Usuario;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class Database {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private String correo;

    public void getUserData(){
        DocumentReference docRef = db.collection("users").document(this.user.getEmail());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Usuario usuario = documentSnapshot.toObject(Usuario.class);
            }
        });
    }

    public void getBankAccountData(){
        DocumentReference docRef = db.collection("users").document(this.user.getEmail()).collection("bankAcounts").document("cuentaPrincipal");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Cuenta_bancaria cuenta = documentSnapshot.toObject(Cuenta_bancaria.class);
            }
        });
    }

    public void addBankAccountData(Cuenta_bancaria cuenta){
        CollectionReference colRef = db.collection("users").document(this.user.getEmail()).collection("bankAcounts");
        colRef.add(cuenta).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Log.w(TAG, "Error adding document", e);
            }
        });
    }

    public void updateBankAccountData(Cuenta_bancaria cuenta){
        DocumentReference colRef = db.collection("users").document(this.user.getEmail()).collection("bankAcounts").document(String.valueOf(cuenta.getId_cuenta()));
        colRef.set(cuenta)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    public void deleteBankAccountData(Cuenta_bancaria cuenta){
        DocumentReference docRef = db.collection("users").document(this.user.getEmail()).collection("bankAcounts").document(String.valueOf(cuenta.getId_cuenta()));
        docRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    public void getAllTAccountsnData(){
        db.collection("users")
                .document(this.user.getEmail())
                .collection("bankAcounts")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Cuenta_bancaria cuenta = document.toObject(Cuenta_bancaria.class);
                        // Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    //Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void getTransactionData(String id_transaccion){
        DocumentReference docRef = db.collection("users").document(this.user.getEmail()).collection("bankAcounts").document("cuentaPrincipal").collection("transactions").document(id_transaccion);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Transaccion transaccion = documentSnapshot.toObject(Transaccion.class);
            }
        });
    }

    public void addTransactionData(Transaccion trasaccion){
        CollectionReference colRef = db.collection("users").document(this.user.getEmail()).collection("bankAcounts").document("cuentaPrincipal").collection("transactions");
        colRef.add(trasaccion).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void updateTrasactionData(Transaccion transaccion){
        DocumentReference docRef = db.collection("users").document(this.user.getEmail()).collection("bankAcounts").document("cuentaPrincipal").collection("transactions").document(String.valueOf(transaccion.getId_transaccion()));
        docRef.set(transaccion)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    public void deleteTrasactionData(Cuenta_bancaria cuenta){
        DocumentReference docRef = db.collection("users").document(this.user.getEmail()).collection("bankAcounts").document("cuentaPrincipal").collection("transactions").document(String.valueOf(cuenta.getId_cuenta()));
        docRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    public void getAllTransactiosnData(){
         db.collection("users")
                 .document(this.user.getEmail())
                 .collection("bankAcounts")
                 .document("cuentaPrincipal")
                 .collection("transactions")
                 .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Transaccion transaccion = document.toObject(Transaccion.class);

                                // Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
        });
    }

    public void getCardData(String id_tarjeta){
        DocumentReference docRef = db.collection("users")
                .document(this.user.getEmail())
                .collection("bankAcounts")
                .document("cuentaPrincipal")
                .collection("tarjeta")
                .document(id_tarjeta);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Tarjeta tarjeta = documentSnapshot.toObject(Tarjeta.class);
            }
        });
    }

    public void addCardData(Tarjeta tarjeta){
        CollectionReference colRef = db.collection("users").document(this.user.getEmail()).collection("bankAcounts").document("cuentaPrincipal").collection("cards");
        colRef.add(tarjeta).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Log.w(TAG, "Error adding document", e);
            }
        });
    }

    public void updateCardData(Tarjeta tarjeta){
        DocumentReference docRef = db.collection("users").document(this.user.getEmail()).collection("bankAcounts").document("cuentaPrincipal").collection("cards").document(String.valueOf(tarjeta.getId_tarjeta()));
        docRef.set(tarjeta)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    public void deleteCardData(Tarjeta tarjeta){
        DocumentReference docRef = db.collection("users").document(this.user.getEmail()).collection("bankAcounts").document("cuentaPrincipal").collection("cards").document(String.valueOf(tarjeta.getId_tarjeta()));
        docRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    public void getAllCardsData(){
        db.collection("users")
                .document(this.user.getEmail())
                .collection("bankAcounts")
                .document("cuentaPrincipal").
                collection("cards")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Tarjeta tarjeta = document.toObject(Tarjeta.class);

                        // Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    //Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

}
