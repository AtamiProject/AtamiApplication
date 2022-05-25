package android.ejemplo.atami.auth;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.ejemplo.atami.R;
import android.ejemplo.atami.permisos.PermisosAlmacenaje;
import android.ejemplo.atami.principal.PantallaPrincipal;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AuthActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 101;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // Obtain the FirebaseAnalytics instance.
        FirebaseAnalytics f = FirebaseAnalytics.getInstance(this);

        //Evento de inicio de aplicacion
        Bundle bundle = new Bundle();
        bundle.putString("message", "Integracion de Firebase completa");
        f.logEvent("InitScreen", bundle);

        setUp();
    }

    private void setUp (){
        findViewById(R.id.signUpButton).setOnClickListener(v -> {
            EditText editEmail = findViewById(R.id.emailEditText);
            EditText editPassword = findViewById(R.id.passwordEditText);
            EditText editNombre = findViewById(R.id.nombreEditText);
            EditText editApellido = findViewById(R.id.apellidoEditText);
            EditText editPassword2 = findViewById(R.id.passwordEditText2);
            String email = editEmail.getText().toString();
            String password = editPassword.getText().toString();
            String nombre = editNombre.getText().toString();
            String apellido = editApellido.getText().toString();
            String password2 = editPassword2.getText().toString();
            Map<String, Object> user = new HashMap<>();
            user.put("nombre", nombre);
            user.put("apellido", apellido);
            user.put("email", email);
            if(!email.isEmpty() && !password.isEmpty() && password.equals(password2)){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(complete -> {
                    if (complete.isSuccessful()) {
                        db.collection("users").document(email).set(user).addOnCompleteListener(complete2 -> {
                            if (complete2.isSuccessful()) {
                                Map<String, Object> relleno = new HashMap<>();
                                relleno.put("vacio", "vacio");
                                Map<String, Object> totalCuenta = new HashMap<>();
                                relleno.put("total", 0);
                                db.collection("users").document(email).collection("bankAcounts").document("cuentaPrincipal").set(totalCuenta);
                                db.collection("users").document(email).collection("bankAcounts").document("cuentaPrincipal").collection("transactions").document().set(relleno);
                                db.collection("users").document(email).collection("bankAcounts").document("cuentaPrincipal").collection("cards").document().set(relleno);
                                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
                            }
                        });
                    } else {
                        showAlert();
                    }
                });
            } else {
                showAlert();
            }
        });
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Se ha producido un error autenticando el usuario");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showHome(){
        Intent homeIntent = new Intent(this, PantallaPrincipal.class);
        //homeIntent.putExtra("email",email);
        //homeIntent.putExtra("provider",provider.name());
        startActivity(homeIntent);
    }

    public void checkPermission(String permission, int requestCode) {
        Intent intent;
        if (ContextCompat.checkSelfPermission(AuthActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            intent = new Intent(this, PermisosAlmacenaje.class);
            startActivity(intent);
        } else {
            showHome();
            //intent = new Intent(this, PantallaPrincipal.class);
            //startActivity(intent);
        }
    }
}
















