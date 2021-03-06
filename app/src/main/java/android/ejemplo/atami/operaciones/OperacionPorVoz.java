package android.ejemplo.atami.operaciones;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.ejemplo.atami.R;
import android.ejemplo.atami.model.Cuenta_bancaria;
import android.ejemplo.atami.model.Transaccion;
import android.ejemplo.atami.operaciones.succesfullOperation.OperationCorrect;
import android.ejemplo.atami.operaciones.takeOut.TakeOutMoneyActivity;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Text;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class OperacionPorVoz extends Activity {
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    private FirebaseFirestore db;
    private FirebaseUser user;
    ImageButton micro;
    Button infoCategorias;
    TextView infoMensaje, errorMensaje;
    Double cantidadDinero;
    boolean annadir, retirar, dineroEsCorrecto, categoriaCorrecta;
    String[] categorias;
    String categoriasEnFila, categoriaEscogida;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operacion_por_voz);
        //Comprobamos que tenga permisos de micro
        checkPermission(Manifest.permission.RECORD_AUDIO,REQUEST_AUDIO_PERMISSION_CODE);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

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
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "??Recuerda usar la estructura correcta!");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            //En caso de error no seguira adelante, en su lugar ense??ara un toast para advertir que algo no funciona,
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
                        if (mensaje.equals("a??adir") || mensaje.equals("poner") ||  mensaje.equals("a??ade") ||  mensaje.equals("pon")) {
                            annadir = true;
                        }
                        if (isValidDouble(mensaje)) {
                            dineroEsCorrecto = true;
                            cantidadDinero = Double.parseDouble(mensaje);
                        }
                        for (String categoria : categorias) {
                            Log.i("categorias", categoria.toLowerCase(Locale.ROOT));

                            if (StringUtils.stripAccents(mensaje.toLowerCase(Locale.ROOT)).equals(categoria.toLowerCase(Locale.ROOT))) {
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
            Toast.makeText(this, "Solo una acci??n a la vez", Toast.LENGTH_LONG).show();
            errorMensaje.setText("Mensaje de error:\n" + "Solo una acci??n a la vez");

        } else if (!dineroEsCorrecto) {
            Toast.makeText(this, "No se ha encontrado la cantidad de dinero", Toast.LENGTH_LONG).show();
            errorMensaje.setText("Mensaje de error:\n" + "No se ha encontrado la cantidad de dinero");

        } else if (!categoriaCorrecta) {
            Toast.makeText(this, "No se ha encontrado la Categoria", Toast.LENGTH_LONG).show();
            errorMensaje.setText("Mensaje de error:\n" + "No se ha encontrado la categoria");
        } else if (dineroEsCorrecto && (!annadir && !retirar)) {
            Toast.makeText(this, "Falta la accion", Toast.LENGTH_LONG).show();
            errorMensaje.setText("Mensaje de error:\n" + "Falta la acci??n");

        } else if (annadir && dineroEsCorrecto && categoriaCorrecta) {
            Toast.makeText(this, "Formato correcto", Toast.LENGTH_LONG).show();
            addTransactionData("annadir");


        } else if (retirar && dineroEsCorrecto && categoriaCorrecta) {
            //TODO
            Toast.makeText(this, "Formato correcto", Toast.LENGTH_LONG).show();
            addTransactionData("retirar");
        }

    }

    public void addTransactionData(String tipo) {
        long ahora = System.currentTimeMillis();
        Date fechaActual = new Date(ahora);
        String fechaActualString = formatoFecha.format(fechaActual);
        Date fechaFormateada = null;

        try {
            fechaFormateada = formatoFecha.parse( fechaActualString);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Algo no ha ido como se esperaba", Toast.LENGTH_SHORT).show();
        }
        if(tipo.equals("retirar")){
            cantidadDinero*=-1;
        }

        //Iniciamos la trasnaccion para enviar los datos al Firebase
        Transaccion transaccion = new Transaccion(Float.valueOf(String.valueOf(cantidadDinero)), fechaFormateada, categoriaEscogida.toUpperCase(Locale.ROOT), "Operacion realizada con comandos de voz :)");
        CollectionReference colRef = db.collection("users").document(this.user.getEmail()).collection("bankAcounts").document("cuentaPrincipal").collection("transactions");
        //Si to do va bien se envia la informacion a la pantalla de confirmacion
        colRef.add(transaccion).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

            @Override
            public void onSuccess(DocumentReference documentReference) {
                getBankAccountData(tipo);
         }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OperacionPorVoz.this, "Ha ocurrido un error al realizar la operaci??n", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getBankAccountData(String tipo){
        DocumentReference docRef = db.collection("users").document(this.user.getEmail()).collection("bankAcounts").document("cuentaPrincipal");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Cuenta_bancaria cuenta = documentSnapshot.toObject(Cuenta_bancaria.class);
                updateTotal(cuenta, tipo);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OperacionPorVoz.this, "Ha ocurrido un error al realizar la operaci??n", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void updateTotal(Cuenta_bancaria cuenta, String tipo){
        long ahora = System.currentTimeMillis();
        Date fechaActual = new Date(ahora);
        String fechaActualString = formatoFecha.format(fechaActual);
        Intent intent = new Intent(this, OperationCorrect.class);
        DocumentReference docRef = db.collection("users").document(this.user.getEmail()).collection("bankAcounts").document("cuentaPrincipal");
        double nuevoTotal = cuenta.getTotal()+cantidadDinero;
        docRef.set(new Cuenta_bancaria((float) (nuevoTotal))).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Bundle bundle = new Bundle();
                bundle.putString("cantidad", String.valueOf(cantidadDinero));
                bundle.putString("descripcion", "Operacion realizada con comandos de voz :)");
                bundle.putString("fechaNoFormateada", "Hoy-"+fechaActualString);
                bundle.putString("selectedCategoria", categoriaEscogida);

                //Este putString sirve para diferenciar si la informacion vendr?? de una operacion de quitar o annadir dinero
                if(tipo.equals("retirar")){
                    bundle.putString("tipo","quitar");
                }else{
                    bundle.putString("tipo","annadir");
                }

                intent.putExtras(bundle);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OperacionPorVoz.this, "Ha ocurrido un error al realizar la operaci??n", Toast.LENGTH_LONG).show();
            }
        });
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

    //Esta funcion sirve para ense??ar un cartel de informacion.
    private void showInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Antes de empezar...");
        builder.setMessage("Las categorias disponibles son:\n" + categoriasEnFila + "\n\n" + "TUTORIAL:\n" +
                "Para realizar una operacion por voz es necessario hacerlo con una estructura muy concreta:" +
                "\n\n1. Pulsa el icono del micro \n\n2. Primero indica si quieres retirar o a??adir dinero," +
                " seguidamente la cantidad.\n\n3. Por ultimo indica la categoria\n\n" +
                "Ejemplo: 'Quiero a??adir 5??? en la categoria de seguros' o 'Quiero retirar 10??? en la categoria diversion'\n\n" +

                "Por defecto el dia de la transaccion ser?? hoy, aunque puedes cambiarlo en cualquier momento en el apartado de 'movimientos'");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //Comprobamos que tenga persmiso de micro
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(OperacionPorVoz.this, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(OperacionPorVoz.this, new String[] { permission }, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_AUDIO_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(OperacionPorVoz.this, "Micro Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(OperacionPorVoz.this, "Para utilizar esta funcionalidad debes dar permisos de voz", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
