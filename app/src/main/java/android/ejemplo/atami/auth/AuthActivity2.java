package android.ejemplo.atami.auth;

import android.content.Intent;
import android.ejemplo.atami.R;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

public class AuthActivity2 extends AppCompatActivity {

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

    private void setUp (){
        String title = "Authentication";

        findViewById(R.id.loginButton).setOnClickListener(v -> {
            EditText editEmail = findViewById(R.id.emailEditText);
            EditText editPassword = findViewById(R.id.passwordEditText);
            String email = editEmail.getText().toString();
            String password = editPassword.getText().toString();
            if(!email.isEmpty() && !password.isEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(complete -> {
                    Log.e("holaaaaaaaaaaaaaaaaaa","holaaaaaaaaaaaaaaaaaa");
                    if(complete.isSuccessful()){
                        showHome(complete.getResult().getUser().getEmail(), ProviderType.BASIC);
                    } else {
                        showAlert();
                    }
                });
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

    private void showHome(String email, android.ejemplo.atami.auth.ProviderType provider){
        Intent homeIntent = new Intent(this, android.ejemplo.atami.principal.PantallaPrincipal.class);
        homeIntent.putExtra("email",email);
        homeIntent.putExtra("provider",provider.name());

        startActivity(homeIntent);
    }

    public void goToRegister(View v){
        Intent regitro = new Intent(this, android.ejemplo.atami.auth.AuthActivity.class);
        startActivity(regitro);
    }
}
















