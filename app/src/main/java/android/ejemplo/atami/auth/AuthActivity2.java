package android.ejemplo.atami.auth;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.ejemplo.atami.MainActivity;
import android.ejemplo.atami.R;
import android.ejemplo.atami.permisos.PermisosAlmacenaje;
import android.ejemplo.atami.principal.PantallaPrincipal;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AuthActivity2 extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 101;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth2);

        // Obtain the FirebaseAnalytics instance.
        FirebaseAnalytics f = FirebaseAnalytics.getInstance(this);

        //Evento de inicio de aplicacion
        Bundle bundle = new Bundle();
        bundle.putString("message", "Integracion de Firebase completa");
        f.logEvent("InitScreen", bundle);

        setUp();
    }

    private void setUp() {
        findViewById(R.id.loginButton).setOnClickListener(v -> {
            EditText editEmail = findViewById(R.id.emailEditText);
            EditText editPassword = findViewById(R.id.passwordEditText);
            String email = editEmail.getText().toString();
            String password = editPassword.getText().toString();
            if (!email.isEmpty() && !password.isEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(complete -> {
                    Log.e("holaaaaaaaaaaaaaaaaaa", "holaaaaaaaaaaaaaaaaaa");
                    if (complete.isSuccessful()) {
                        Log.e("aaaaaaaaa", "aaaaaaaaaaaaa");
                        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
                        //showHome(complete.getResult().getUser().getEmail(), ProviderType.BASIC);
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

    private void showHome() {
        Intent homeIntent = new Intent(this, android.ejemplo.atami.principal.PantallaPrincipal.class);
        //homeIntent.putExtra("email",email);
        //homeIntent.putExtra("provider",provider.name());
        startActivity(homeIntent);
    }

    public void checkPermission(String permission, int requestCode) {
        Intent intent;
        if (ContextCompat.checkSelfPermission(AuthActivity2.this, permission) == PackageManager.PERMISSION_DENIED) {
            intent = new Intent(this, PermisosAlmacenaje.class);
            startActivity(intent);
        } else {
            showHome();
        }
    }

    public void goToRegister(View v) {
        Intent regitro = new Intent(this, android.ejemplo.atami.auth.AuthActivity.class);
        startActivity(regitro);
    }

    public void changePassword() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        EditText editEmail = findViewById(R.id.emailEditText);
        String email = editEmail.getText().toString();
        if (!email.isEmpty()) {
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Se te ha enviado un correo para cambiar la contraseña", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            showAlert();
        }


    }
}
















