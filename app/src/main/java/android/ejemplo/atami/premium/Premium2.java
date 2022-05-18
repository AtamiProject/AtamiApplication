package android.ejemplo.atami.premium;

import android.content.Intent;
import android.ejemplo.atami.R;
import android.ejemplo.atami.principal.PantallaPrincipal;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Premium2 extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.premium2);

        button = (Button) findViewById(R.id.gopagprincipalfromPremium);
    }

    public void goToPantallaPrincipal(View _) {
        Intent intent = new Intent(this, PantallaPrincipal.class);
        startActivity(intent);
    }

}
