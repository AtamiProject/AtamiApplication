package android.ejemplo.atami.graficos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.ejemplo.atami.R;
import android.ejemplo.atami.model.Transaccion;
import android.ejemplo.atami.operaciones.takeOut.TakeOutMoneyActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.MonthDisplayHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Graficos extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    Spinner spinner;
    private String selected;

    private List<Transaccion> transacciones = new ArrayList<>();
    public static List<Transaccion> filteredTransactions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graficos);

        spinner = (Spinner) findViewById(R.id.spinner_graficos);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.graficos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //https://www.youtube.com/watch?v=vhKtbECeazQ

    }

    public void seleccionar(View view) {
        selected = spinner.getSelectedItem().toString().toLowerCase();
        getAllTransactionsData(view, selected);
    }

    public void getAllTransactionsData(View view, String seleccion){
        db.collection("users")
                .document(this.user.getEmail())
                .collection("bankAcounts")
                .document("cuentaPrincipal")
                .collection("transactions")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Transaccion transaccion = document.toObject(Transaccion.class);
                        transacciones.add(transaccion);
                    }
                    switch(seleccion) {
                        case "anual":
                            anual(view);
                            break;
                        case "semestral":
                            //System.out.println("semestral"); -> 1 enero - 30 junio, 1 julio - 31 diciembre
                            break;
                        case "trimestral":
                            //System.out.println("trimestral"); ->
                            break;
                        case "mensual":
                            System.out.println("mensual");
                            break;
                        case "personalizado":
                            System.out.println("personalizado");
                            break;
                    }
                } else {
                    Toast.makeText(Graficos.this, "Ha ocurrido un error al conectarse a la base de datos", Toast.LENGTH_LONG).show();
                    //Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void anual(View view) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+2"));
        int year = Calendar.getInstance().get(Calendar.YEAR) - 1;
        Date dateAfter = new Date();
        try {
            DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String dateString = "31-12-".concat(String.valueOf(year));
            dateAfter = sdf.parse(dateString);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        //Date dateAfter = new DateTime(year, Month.DECEMBER.getValue(), 31);
        Date dateBefore = new Date();
        for (int i = 0; i < transacciones.size(); i++) {
            if(transacciones.get(i).getFecha().after(dateAfter) && transacciones.get(i).getFecha().before(dateBefore)) {
                filteredTransactions.add(transacciones.get(i));
            }
        }
        Intent intent = new Intent(this, Anual.class);
        startActivity(intent);
    }
}