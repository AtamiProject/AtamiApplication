package android.ejemplo.atami.perfil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.ejemplo.atami.R;
import android.ejemplo.atami.auth.AuthActivity2;
import android.ejemplo.atami.model.Usuario;
import android.ejemplo.atami.operaciones.takeOut.TakeOutMoneyActivity;
import android.ejemplo.atami.principal.PantallaPrincipal;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Perfil extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        TextView nombre = (TextView) findViewById(R.id.nombre);
        TextView apellidos = (TextView) findViewById(R.id.apellidos);
        TextView correo = (TextView) findViewById(R.id.correo);

        DocumentReference docRef = db.collection("users").document(this.user.getEmail());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Usuario usuario = documentSnapshot.toObject(Usuario.class);
                nombre.setText(usuario.getNombre());
                apellidos.setText(usuario.getApellido());
                correo.setText(user.getEmail());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Perfil.this, "Ha ocurrido un error recuperando los datos", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void changePassword(View _){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(user.getEmail())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Se te ha enviado un correo para cambiar la contraseña" , Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Perfil.this, "Ha ocurrido un error conectando con la cuenta", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void logOut(View _) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, AuthActivity2.class);
        startActivity(intent);
    }

}