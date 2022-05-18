package android.ejemplo.atami.operaciones;

import android.app.Activity;
import android.content.Intent;
import android.ejemplo.atami.R;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    Button infoCategorias;
    TextView infoMensaje, errorMensaje;
    Double cantidadDinero;
    boolean annadir, retirar, dineroEsCorrecto, categoriaCorrecta;
    String[] categorias;
    String categoriasEnFila, categoriaEscogida;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operacion_por_voz);

        micro = (ImageButton) findViewById(R.id.button_micro);
        infoCategorias = (Button) findViewById(R.id.infoCategorias);
        infoMensaje = (TextView) findViewById(R.id.infoMensaje);
        errorMensaje = (TextView) findViewById(R.id.errorMensaje);
        categorias = getResources().getStringArray(R.array.categorias);
        categoriasEnFila = "| ";
        Log.d("myTag", categorias.toString());
        for (String categoria : categorias) {
            Log.d("myTag", categoria);
            categoriasEnFila += categoria + " | ";
        }

        //Seteamos listener para realizar accion cuando se pulsa el boton del micro
        micro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });
        //Seteamos listener para realizar accion cuando se pulsa el boton de informacion
        infoCategorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfo();
            }
        });


    }

    //Esta funcion sirve para que aparezca el SpeechToText de Google
    private void speak() {
        infoMensaje.setText("");
        errorMensaje.setText("");

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "¡Recuerda usar la estructura correcta!");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            //En caso de error no seguira adelante, en su lugar enseñara un toast para advertir que algo no funciona,
            // en caso de que no se encuentre un paquete que se necesita abrira la direccion apra instalarlo
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("myTag", e.getMessage());
            Toast.makeText(this, "Algo no ha ido como debia...", Toast.LENGTH_LONG).show();
            String appPackageName = "com.google.android.googlequicksearchbox";

            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }

        }


    }

    //Esta funcion se activa en caso de que el SpeechToText haya funcionado,
    // en el se comprueba que la informacion que ha llegado sea correcta y en caso de serla realiza las acciones necessarias
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_SPEECH_INPUT:
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    infoMensaje.setText("Tu comando es:\n" + result.get(0));
                    String fullMensaje = result.get(0).replaceAll(",", ".");
                    String[] mensajeSplitted = fullMensaje.split(" ");
                    annadir = false;
                    retirar = false;
                    dineroEsCorrecto = false;
                    categoriaEscogida = null;
                    categoriaCorrecta = false;

                    //A partir de aqui significa que el mensaje que ha llegado no contiene
                    // errores por lo tanto comprobamos si tiene la informacion que necessitamos
                    for (String mensaje : mensajeSplitted) {
                        if (mensaje.equals("retirar") || mensaje.equals("quitar")) {
                            retirar = true;
                        }
                        if (mensaje.equals("añadir") || mensaje.equals("poner")) {
                            annadir = true;
                        }
                        if (isValidDouble(mensaje)) {
                            dineroEsCorrecto = true;
                            cantidadDinero = Double.parseDouble(mensaje);
                        }
                        for (String categoria : categorias) {
                            if (mensaje.equals(categoria.toLowerCase(Locale.ROOT))) {
                                categoriaCorrecta = true;
                                categoriaEscogida = mensaje;
                            } else if (mensaje.equals("ropa") || mensaje.equals("calzado")) {
                                categoriaCorrecta = true;
                                categoriaEscogida = "ROPA Y CALZADO";
                            }
                        }

                    }
                    infoMensaje.setText("Tu frase:\n" + result.get(0));
                }
        }
        //Aqui realizamos las acciones que pertocas dependiendo si tenemos la informacion necessaria
        if (!annadir && !retirar && !dineroEsCorrecto && !categoriaCorrecta) {
            Toast.makeText(this, "Frase Incorrecta, recuerda estructura correcta", Toast.LENGTH_LONG).show();
            errorMensaje.setText("Mensaje de error:\n" + "Frase Incorrecta, recuerda usar la estructura correcta");

        } else if (annadir && retirar) {
            Toast.makeText(this, "Solo una acción a la vez", Toast.LENGTH_LONG).show();
            errorMensaje.setText("Mensaje de error:\n" + "Solo una acción a la vez");

        } else if (!dineroEsCorrecto) {
            Toast.makeText(this, "No se ha encontrado la cantidad de dinero", Toast.LENGTH_LONG).show();
            errorMensaje.setText("Mensaje de error:\n" + "No se ha encontrado la cantidad de dinero");

        } else if (!categoriaCorrecta) {
            Toast.makeText(this, "No se ha encontrado la Categoria", Toast.LENGTH_LONG).show();
            errorMensaje.setText("Mensaje de error:\n" + "No se ha encontrado la categoria");
        } else if (dineroEsCorrecto && (!annadir && !retirar)) {
            Toast.makeText(this, "Falta la accion", Toast.LENGTH_LONG).show();

        } else if (annadir && dineroEsCorrecto && categoriaCorrecta) {
            //Realizar accion de annadir dinero a la bbdd, para saber la categoria escogida usa la variable @categoriaEscogida

        } else if (retirar && dineroEsCorrecto && categoriaCorrecta) {
            //Realizar accion de retirar dinero, para saber la categoria escogida usa la variable @categoriaEscogida
        }

    }

    //Con esta funcion comprobamos que el valor sea un Double
    private static boolean isValidDouble(String s) {
        boolean isValid = true;

        try {
            Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            isValid = false;
        }

        return isValid;
    }

    //Esta funcion sirve para enseñar un cartel de informacion.
    private void showInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Antes de empezar...");
        builder.setMessage("Las categorias disponibles son:\n" + categoriasEnFila + "\n\n" + "TUTORIAL:\n" +
                "Para realizar una operacion por voz es necessario hacerlo con una estructura muy concreta:" +
                "\n\n1. Pulsa el icono del micro \n\n2. Primero indica si quieres retirar o añadir dinero," +
                " seguidamente la cantidad.\n\n3. Por ultimo indica la categoria\n\n" +
                "Ejemplo: 'Quiero añadir 5€ en la categoria de seguros' o 'Quiero retirar 10€ en la categoria diversion'\n\n" +

                "Por defecto el dia de la transaccion será hoy, aunque puedes cambiarlo en cualquier momento en el apartado de 'movimientos'");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
