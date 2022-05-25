package android.ejemplo.atami.operaciones.addMoney;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.ejemplo.atami.R;
import android.ejemplo.atami.model.Cuenta_bancaria;
import android.ejemplo.atami.model.Transaccion;
import android.ejemplo.atami.operaciones.succesfullOperation.OperationCorrect;
import android.ejemplo.atami.operaciones.takeOut.TakeOutMoneyActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddMoneyActivity extends Activity {
    private FirebaseFirestore db;
    private FirebaseUser user;
    EditText ETCantidad, ETFecha, ETDescrpicion;
    DatePickerDialog picker;
    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    Float cantidadDinero;
    String cantidad, descripcion, selectedCategoria;
    Spinner spinCategoria;
    Bundle bundle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_money_manual);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        //Buscamos por id los campos necessarios para annadir dinero
        ETCantidad = (EditText) findViewById(R.id.ETCantidad);
        ETFecha = (EditText) findViewById(R.id.ETFecha);
        ETDescrpicion = (EditText) findViewById(R.id.ETDescripcion);
        spinCategoria = (Spinner) findViewById(R.id.desplegableCategorias);
        ETFecha.setInputType(InputType.TYPE_NULL);

        //Estas tres  lineas de codigo son para poner a punto el desplegable de categorias
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categorias, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCategoria.setAdapter(adapter);

        //Este Listener sirve para lanzar el seleccionador de fecha
        ETFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AddMoneyActivity.this,
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

    public void onClickAnnadir(View _) {
        Boolean correctData = true;
        cantidad = ETCantidad.getText().toString();
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
                Toast.makeText(getApplicationContext(),"El campo 'Fecha' és incorrecto" , Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(),"El campo 'Cantidad' es incorrecto", Toast.LENGTH_SHORT).show();
            }
        }
        //Como la descripcion no es obligatoria simplemente un trim para quitar espacios innecesarios
        descripcion = ETDescrpicion.getText().toString();
        descripcion.trim();
        //Aqui cojemos el valor de la categoria
        selectedCategoria = spinCategoria.getSelectedItem().toString();

        //En caso de que todos los datos sean correctos procedemos a abrir la Activity "operacionCorrecta"
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
        Transaccion transaccion = new Transaccion(cantidadDinero, fechaFormateada, selectedCategoria, descripcion);
        CollectionReference colRef = db.collection("users").document(this.user.getEmail()).collection("bankAcounts").document("cuentaPrincipal").collection("transactions");
        colRef.add(transaccion).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

            @Override
            public void onSuccess(DocumentReference documentReference) {
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
                Toast.makeText(AddMoneyActivity.this, "Ha ocurrido un error al realizar la operación", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getBankAccountData(){
        DocumentReference docRef = db.collection("users").document(this.user.getEmail()).collection("bankAcounts").document("cuentaPrincipal");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Cuenta_bancaria cuenta = documentSnapshot.toObject(Cuenta_bancaria.class);
                updateTotal(cuenta);
            }
        });
    }

    public void updateTotal(Cuenta_bancaria cuenta){
        DocumentReference docRef = db.collection("users").document(this.user.getEmail()).collection("bankAcounts").document("cuentaPrincipal");
        docRef.set(new Cuenta_bancaria(cuenta.getTotal()+cantidadDinero)).addOnSuccessListener(new OnSuccessListener<Void>() {
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