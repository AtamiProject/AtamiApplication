package android.ejemplo.atami.operaciones;

import android.app.Activity;
import android.content.Intent;
import android.ejemplo.atami.R;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class OperacionPorVoz extends Activity {
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    ImageButton micro;
    TextView infoMensaje;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operacion_por_voz);
        showInfo();

        micro = (ImageButton) findViewById(R.id.button_micro);
        infoMensaje = (TextView) findViewById(R.id.infoMensaje);

        micro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });


    }
    private void speak(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "¡Recuerda usar la estructura correcta!");
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        }catch (Exception e){
            Toast.makeText(this,  e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("myTag", e.getMessage());
            Toast.makeText(this,  "Algo no ha ido como debia...", Toast.LENGTH_LONG).show();
            String appPackageName = "com.google.android.googlequicksearchbox";
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CODE_SPEECH_INPUT:
                if(resultCode == RESULT_OK && null!=data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    infoMensaje.setText(result.get(0));
                }
        }
    }

    private void showInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Antes de empezar...");
        builder.setMessage("Para realizar una operacion por voz es necessario hacerlo con una estructura muy concreta:\n\n1. Pulsa el icono del micro \n\n2. Primero indica si quieres retirar o añadir dinero, seguidamente la cantidad.\n\nEjemplo: 'Quiero añadir 5€' o 'Quiero retirar 10€");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
