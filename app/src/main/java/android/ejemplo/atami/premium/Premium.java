package android.ejemplo.atami.premium;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.ejemplo.atami.R;
import android.ejemplo.atami.principal.PantallaPrincipal;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Premium extends AppCompatActivity {

    Button button;

    Intent intent;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.premium);

        button = (Button) findViewById(R.id.premium);
    }

    public void goToPremium2(View _) { //si lo hicieramos dirigiaría a la pantalla de premium"
        updatePremium();
    }

    public void updatePremium() {
            DocumentReference colRef = db.collection("users").document(this.user.getEmail());
            colRef.update("premium", true)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            intent = new Intent(Premium.this, Premium2.class);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Premium.this, "Ha ocurrido un error al realizar la operación", Toast.LENGTH_SHORT).show();
                        }
                    });
    }

    //https://www.youtube.com/watch?v=SxvM9B-IkGg -> video insertar pagos
    // https://es.acervolima.com/como-integrar-paypal-sdk-en-android/ -> paypal
}