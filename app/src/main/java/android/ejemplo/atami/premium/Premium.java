package android.ejemplo.atami.premium;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.ejemplo.atami.R;
import android.ejemplo.atami.principal.PantallaPrincipal;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Premium extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.premium);

        button = (Button) findViewById(R.id.premium);
    }

    public void goToPremium2(View _) { //si lo hicieramos dirigiaría a la pantalla de premium"
        Intent intent = new Intent(this, Premium2.class);
        startActivity(intent);
    }

    //https://www.youtube.com/watch?v=SxvM9B-IkGg -> video insertar pagos
    // https://es.acervolima.com/como-integrar-paypal-sdk-en-android/ -> paypal
}