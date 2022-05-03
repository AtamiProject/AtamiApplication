package android.ejemplo.atami.premium;

import androidx.appcompat.app.AppCompatActivity;

import android.ejemplo.atami.R;
import android.os.Bundle;
import android.widget.Button;

public class Premium extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.premium);

        button = (Button) findViewById(R.id.premium);
    }

    public void pantallaPago() {
        //dirigir a la pantalla de pago
    }

    //https://www.youtube.com/watch?v=SxvM9B-IkGg -> video insertar pagos
    // https://es.acervolima.com/como-integrar-paypal-sdk-en-android/ -> paypal
}