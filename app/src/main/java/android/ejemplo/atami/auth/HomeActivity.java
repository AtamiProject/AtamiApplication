package android.ejemplo.atami.auth;

import android.ejemplo.atami.R;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

enum ProviderType{
    BASIC
}

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("email");
        String provider = bundle.getString("provider");
        setUp(email, provider);
    }

    private void setUp(String email, String provider){
        String title = "Inicio";

        TextView editEmail = findViewById(R.id.emaiTextView);
        editEmail.setText(email);

        TextView editProvider = findViewById(R.id.providerTextView);
        editProvider.setText(provider);

        findViewById(R.id.logoutButton).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            onBackPressed();
        });
    }
}