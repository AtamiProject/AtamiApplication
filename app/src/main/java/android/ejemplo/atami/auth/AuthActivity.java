package android.ejemplo.atami.auth;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.ejemplo.atami.R;
import android.ejemplo.atami.calendario.Calendario;
import android.ejemplo.atami.model.Cuenta_bancaria;
import android.ejemplo.atami.permisos.PermisosAlmacenaje;
import android.ejemplo.atami.principal.PantallaPrincipal;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

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

    private void setUp() {
        findViewById(R.id.signUpButton).setOnClickListener(v -> {
            EditText editEmail = findViewById(R.id.emailEditText);
            EditText editPassword = findViewById(R.id.passwordEditText);
            EditText editNombre = findViewById(R.id.nombreEditText);
            EditText editApellido = findViewById(R.id.apellidoEditText);
            EditText editPassword2 = findViewById(R.id.passwordEditText2);
            EditText editTotal = findViewById(R.id.total);
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();
            String nombre = editNombre.getText().toString().trim();
            String apellido = editApellido.getText().toString().trim();
            String password2 = editPassword2.getText().toString().trim();
            String totalString = editTotal.getText().toString().trim();
            Float total;
            if (totalString != null && totalString.length() > 0) {
                total = Float.valueOf(totalString);
            } else {
                total = Float.valueOf(0);
            }
            Map<String, Object> user = new HashMap<>();
            user.put("nombre", nombre);
            user.put("apellido", apellido);
            user.put("email", email);
            user.put("premium", false);
            if (!email.isEmpty() && !password.isEmpty() && !password2.isEmpty() && !nombre.isEmpty() && !apellido.isEmpty() && !editTotal.getText().toString().isEmpty()) {
                if (password.equals(password2)) {
                    try {
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(complete -> {
                            if (complete.isSuccessful()) {
                                db.collection("users").document(email).set(user).addOnCompleteListener(complete2 -> {
                                    if (complete2.isSuccessful()) {
                                        Map<String, Object> relleno = new HashMap<>();
                                        relleno.put("vacio", "vacio");
                                        db.collection("users").document(email).collection("bankAcounts").document("cuentaPrincipal").collection("transactions").document();
                                        db.collection("users").document(email).collection("bankAcounts").document("cuentaPrincipal").set(new Cuenta_bancaria(total));
                                        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
                                    }
                                });
                            } else {
                                showAlert();
                            }
                        });
                    } catch (Exception e) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Error");
                        builder.setMessage("Ha habido un error con la conexion");
                        builder.setPositiveButton("Aceptar", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                } else {
                    Toast.makeText(AuthActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(AuthActivity.this, "Los datos estan incompletos", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Se ha producido un error creando el usuario, puede que el formato sea erroneo");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showHome() {
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
        }
    }

}
















