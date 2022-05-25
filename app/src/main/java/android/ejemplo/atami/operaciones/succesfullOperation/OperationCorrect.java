package android.ejemplo.atami.operaciones.succesfullOperation;

import android.app.Activity;
import android.content.Intent;
import android.ejemplo.atami.R;
import android.ejemplo.atami.principal.PantallaPrincipal;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

public class OperationCorrect extends Activity {

    Button button;
    private String Scantidad, fechaNoFormateada, selectedCategoria,descrpicion = null;
    private TextView detalles, TVDescripcion;
    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.correcto_peration);
        button = (Button) findViewById(R.id.gopagprincipal);

        detalles = (TextView) findViewById(R.id.TextDetalles);
        TVDescripcion = (TextView) findViewById(R.id.TextDescripcion);
        //Aqui assignamos los parametros que le hayamos pasado a la activity
        Bundle bundle = getIntent().getExtras();
        //En caso de que no esten vacios //excepto descripcion// cojemos los valores
        if(bundle.getString("cantidad")!= null && bundle.getString("fechaNoFormateada")!= null && bundle.getString("selectedCategoria")!= null ) {
            Scantidad = bundle.getString("cantidad");
            fechaNoFormateada= bundle.getString("fechaNoFormateada");
            selectedCategoria = bundle.getString("selectedCategoria");
            descrpicion = bundle.getString("descripcion");
            detalles.setText("Se han añadido " +Scantidad+"€ a fecha del "+fechaNoFormateada+" en la categoria de "+selectedCategoria+".");
            if(bundle.getString("tipo").equals("annadir")){
                detalles.setText("Se ha" +
                        "n añadido "+Scantidad+"€ a fecha de "+fechaNoFormateada+" en la categoria: "+selectedCategoria);
            }else if(bundle.getString("tipo").equals("quitar")){
                detalles.setText("Se ha" +
                        "n extraido "+Scantidad+"€ a fecha de "+fechaNoFormateada+" de la categoria: "+selectedCategoria);
            }if(descrpicion !=null){
                TVDescripcion.setText(descrpicion);
            }else{
                TVDescripcion.setText("Sin descricpión");
            }
        }else{
            detalles.setText(bundle.getString("cantidad")+" "+ bundle.getString("fechaNoFormateada")+" "+bundle.getString("selectedCategoria"));
            TVDescripcion.setText("afsaf");
        }

    }

    public void goToPantallaPrincipal(View _) {
        Intent intent = new Intent(this, PantallaPrincipal.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {}
}
