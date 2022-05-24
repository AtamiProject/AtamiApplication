package android.ejemplo.atami.operaciones.editar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.ejemplo.atami.R;
import android.ejemplo.atami.model.Transaccion;
import android.ejemplo.atami.operaciones.addMoney.AddMoneyActivity;
import android.ejemplo.atami.operaciones.succesfullOperation.OperationCorrect;
import android.icu.text.CaseMap;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditarTransaccion extends Activity {
    //Todos estos son campos del layout
    private EditText cantidadA_, ETDescripcion, ETFecha;
    private TextView TVFecha, titulo;
    private DatePickerDialog picker;
    private Float cantidadDinero;
    private String cantidad, descripcion, selectedCategoria;
    private Spinner spinCategoria;
    private Bundle bundle;
    final private SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

    //A partir de aqui son campos que recogemos de la activity anterior
    private String cantidadTransaacion, fechaTransaccion, idTransaccion, categoriaTransaccion, descripcionTransaccion;

    //Para obtener los datos de la transaccion los pasaremos por bundle igual que en la clase OperationCorrect
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_transaccion);


        cantidadA_ = (EditText) findViewById(R.id.ETCantidad);
        ETDescripcion = (EditText) findViewById(R.id.ETDescripcion);
        TVFecha = (TextView) findViewById(R.id.TVFecha);
        titulo = (TextView) findViewById(R.id.Titulo);
        ETFecha = (EditText) findViewById(R.id.ETFecha);
        ETFecha.setInputType(InputType.TYPE_NULL);

        //Estas tres  lineas de codigo son para poner a punto el desplegable de categorias
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categorias, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCategoria.setAdapter(adapter);

        //Aqui assignamos los parametros que le hayamos pasado a la activity
        Bundle bundle = getIntent().getExtras();
        //En caso de que no esten vacios //excepto descripcion// cojemos los valores
        if (bundle.getString("fechaEnString") != null && bundle.getString("cantidadTransaccion") != null && bundle.getString("categoria") != null &&
                bundle.getString("idTransaccion") != null) {

            //Ahora si cojemos la informacion de la activity anterior
            cantidadTransaacion = bundle.getString("cantidad");
            fechaTransaccion = bundle.getString("fechaEnString");
            idTransaccion = bundle.getString("idTransaccion");
            categoriaTransaccion = bundle.getString("categoria");

            //Aqui assignamos los datos de la transaccion en el layout
            cantidadA_.setText(cantidadTransaacion);
            ETFecha.setText(fechaTransaccion);
            titulo.setText("Transaccion: "+idTransaccion);
            int spinnerPosition = adapter.getPosition(categoriaTransaccion.toUpperCase(Locale.ROOT));
            spinCategoria.setSelection(spinnerPosition);


            //ahora cojemos la descripcion que podría ser null
            if (bundle.getString("descripcion") != null) {
                descripcionTransaccion = bundle.getString("descripcion");
                ETDescripcion.setText(descripcionTransaccion);
            } else {
                ETDescripcion.setText("Sin descripción");
            }
        }else{//En caso de fallar y que no esten los campos llenos
            Toast.makeText(getApplicationContext(), "Falta algun campo de la transaccion", Toast.LENGTH_SHORT).show();
            //cierra la activity
            finish();
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
        descripcion = cantidadA_.getText().toString();
        descripcion.trim();
        //Aqui cojemos el valor de la categoria
        selectedCategoria = spinCategoria.getSelectedItem().toString();

        //En caso de que todos los datos sean correctos procedemos a abrir la Activity "operacionCorrecta"
        //aqui iria el metodo para añadir los datos en la BBDD que en esta caso no seria añadirlos sino modificarlos
        /*
        if (correctData) {
            try {

               addTransactionData(fechaFormateada, fechaNoFormateada);
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Algo no ha ido como se esperaba", Toast.LENGTH_SHORT).show();

            }
        }*/

    }



}
