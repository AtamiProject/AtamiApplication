package android.ejemplo.atami.operaciones.editar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.ejemplo.atami.R;

import android.ejemplo.atami.model.Cuenta_bancaria;
import android.ejemplo.atami.model.Transaccion;
import android.ejemplo.atami.operaciones.addMoney.AddMoneyActivity;
import android.ejemplo.atami.operaciones.succesfullOperation.OperationCorrect;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditarTransaccion extends Activity {
    //Todos estos son campos del layout
    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    private EditText cantidadA_, ETDescripcion, ETFecha;
    private TextView TVFecha, titulo;
    private DatePickerDialog picker;
    private Float cantidadDinero, cantidadTransaacion;
    private String cantidad, descripcion, selectedCategoria;
    private Spinner spinCategoria;
    private Bundle bundle;
    private FirebaseFirestore db;
    private FirebaseUser user;

    //A partir de aqui son campos que recogemos de la activity anterior
    private String fechaTransaccion, idTransaccion, categoriaTransaccion, descripcionTransaccion, tipo;

    //Para obtener los datos de la transaccion los pasaremos por bundle igual que en la clase OperationCorrect
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_transaccion);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        cantidadA_ = (EditText) findViewById(R.id.ETCantidad);
        ETDescripcion = (EditText) findViewById(R.id.ETDescripcion);
        ETFecha = (EditText) findViewById(R.id.ETFecha);
        TVFecha = (TextView) findViewById(R.id.TVFecha);
        titulo = (TextView) findViewById(R.id.Titulo);
        spinCategoria = (Spinner) findViewById(R.id.desplegableCategorias);
        ETFecha.setInputType(InputType.TYPE_NULL);

        //Estas tres  lineas de codigo son para poner a punto el desplegable de categorias
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categorias, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCategoria.setAdapter(adapter);

        //Aqui asignamos los parametros que le hayamos pasado a la activity
        bundle = getIntent().getExtras();
        //En caso de que no esten vacios //excepto descripcion// cojemos los valores
        if (bundle.getString("fechaEnString") != null && bundle.getString("categoria") != null && bundle.getString("idTransaccion") != null) {

            //Ahora si cojemos la informacion de la activity anterior
            cantidadTransaacion = bundle.getFloat("cantidadTransaccion");
            fechaTransaccion = bundle.getString("fechaEnString");
            idTransaccion = bundle.getString("idTransaccion");
            categoriaTransaccion = bundle.getString("categoria");

            //Aqui assignamos los datos de la transaccion en el layout
            cantidadA_.setText(cantidadTransaacion.toString().replace("-",""));
            ETFecha.setText(fechaTransaccion);
            titulo.setText("Transaccion: " + (bundle.getString("tipo").equals("restar")?"Gasto":"Ingreso"));
            int spinnerPosition = adapter.getPosition(categoriaTransaccion.toUpperCase(Locale.ROOT));
            spinCategoria.setSelection(spinnerPosition);

            //ahora cojemos la descripcion que podría ser null
            if (bundle.getString("descripcion") != null) {
                descripcionTransaccion = bundle.getString("descripcion");
                ETDescripcion.setText(descripcionTransaccion);
            } else {
                ETDescripcion.setText("Sin descripción");
            }
        } else {//En caso de fallar y que no esten los campos llenos
            Toast.makeText(getApplicationContext(), "Falta algun campo de la transaccion", Toast.LENGTH_SHORT).show();
            finish();//cierra la activity
        }

        //Este Listener sirve para lanzar el seleccionador de fecha
        ETFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(EditarTransaccion.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                ETFecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.getDatePicker().setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
                picker.show();
            }
        });
    }

    public void onClickModificar(View _) {
        Boolean correctData = true;
        cantidad = cantidadA_.getText().toString();
        Date fechaFormateada = null;
        String fechaNoFormateada = ETFecha.getText().toString();

        //Aqui transformamos la fecha de String  Date y parseamos el String a Float,
        // en caso de no funcionar lanza un mensaje toast con el campo incorrecto
        try {
            fechaFormateada = formatoFecha.parse(fechaNoFormateada);
            cantidadDinero = Float.valueOf(cantidad);
        } catch (ParseException | NumberFormatException e) {
            e.printStackTrace();
            String[] message = e.getMessage().split(" ");
            correctData = false;
            if (message[1].equals("date:")) {
                Toast.makeText(getApplicationContext(), "El campo 'Fecha' és incorrecto", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "El campo 'Cantidad' es incorrecto", Toast.LENGTH_SHORT).show();
            }
        }
        //Como la descripcion no es obligatoria simplemente un trim para quitar espacios innecesarios
        descripcion = ETDescripcion.getText().toString();
        descripcion.trim();
        //Aqui cojemos el valor de la categoria
        selectedCategoria = spinCategoria.getSelectedItem().toString();

        //En caso de que todos los datos sean correctos procedemos a abrir la Activity "operacionCorrecta"
        //aqui iria el metodo para añadir los datos en la BBDD que en esta caso no seria añadirlos sino modificarlos

        if (correctData) {
            try {
                addTransactionData(fechaFormateada, fechaNoFormateada);
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Algo no ha ido como se esperaba", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void addTransactionData(Date fechaFormateada, String fechaNoFormateada) throws ParseException {
        Intent intent = new Intent(this, OperationCorrect.class);
        if(bundle.getString("tipo").equals("restar")){
            cantidadDinero *= -1;
        }
        Transaccion transaccion = new Transaccion(cantidadDinero, fechaFormateada, selectedCategoria, descripcion);
        CollectionReference colRef = db.collection("users").document(this.user.getEmail()).collection("bankAcounts").document("cuentaPrincipal").collection("transactions");
        colRef.document(idTransaccion).set(transaccion).addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void aVoid) {
                getBankAccountData();
                bundle = new Bundle();
                bundle.putString("cantidad", cantidad);
                bundle.putString("descripcion", descripcion);
                bundle.putString("fechaNoFormateada", fechaNoFormateada);
                bundle.putString("selectedCategoria", selectedCategoria);

                //Este putString sirve para diferenciar si la informacion vendrá de una operacion de quitar o annadir dinero
                bundle.putString("tipo", "annadir");

                intent.putExtras(bundle);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditarTransaccion.this, "Ha ocurrido un error al realizar la operación", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getBankAccountData() {
        DocumentReference docRef = db.collection("users").document(this.user.getEmail()).collection("bankAcounts").document("cuentaPrincipal");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Cuenta_bancaria cuenta = documentSnapshot.toObject(Cuenta_bancaria.class);
                updateTotal(cuenta);
            }
        });
    }

    public void onClickDelete(View _){
        Intent intent = new Intent(this, OperationCorrect.class);
        CollectionReference colRef = db.collection("users").document(this.user.getEmail()).collection("bankAcounts").document("cuentaPrincipal").collection("transactions");
        colRef.document(idTransaccion).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                updateTotalDelete();
                bundle = new Bundle();

                //Este putString sirve para diferenciar si la informacion vendrá de una operacion de quitar o annadir dinero
                bundle.putString("tipo", "delete");

                intent.putExtras(bundle);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditarTransaccion.this, "Ha ocurrido un error al realizar la operación", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void updateTotalDelete() {
        cantidad = cantidadA_.getText().toString();

        //Aqui transformamos la fecha de String  Date y parseamos el String a Float,
        // en caso de no funcionar lanza un mensaje toast con el campo incorrecto
        try {
            cantidadDinero = Float.valueOf(cantidad);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            String[] message = e.getMessage().split(" ");
            if (message[1].equals("date:")) {
                Toast.makeText(getApplicationContext(), "El campo 'Fecha' és incorrecto", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "El campo 'Cantidad' es incorrecto", Toast.LENGTH_SHORT).show();
            }
        }
        DocumentReference docRef = db.collection("users").document(this.user.getEmail()).collection("bankAcounts").document("cuentaPrincipal");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Cuenta_bancaria cuenta = documentSnapshot.toObject(Cuenta_bancaria.class);
                if(bundle.getString("tipo").equals("restar")){
                    cantidadDinero *= -1;
                }
                docRef.set(new Cuenta_bancaria(cuenta.getTotal() - (cantidadDinero))).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error deleting document", e);
                    }
                });
            }
        });
    }

    public void updateTotal(Cuenta_bancaria cuenta) {
        DocumentReference docRef = db.collection("users").document(this.user.getEmail()).collection("bankAcounts").document("cuentaPrincipal");
        docRef.set(new Cuenta_bancaria(cuenta.getTotal() - (cantidadTransaacion-cantidadDinero))).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Log.d(TAG, "DocumentSnapshot successfully deleted!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Log.w(TAG, "Error deleting document", e);
            }
        });
    }

}
