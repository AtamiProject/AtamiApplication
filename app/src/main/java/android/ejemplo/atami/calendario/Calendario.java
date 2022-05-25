package android.ejemplo.atami.calendario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.ejemplo.atami.R;
import android.ejemplo.atami.model.Transaccion;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Calendario extends AppCompatActivity {

    CalendarView calendarView;
    ListView listView;
    private boolean found;
    private SimpleDateFormat dateFormat;
    Calendar c;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private List<Transaccion> transacciones = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendario);

        c = Calendar.getInstance();
        listView = (ListView) findViewById(R.id.listaEventos);
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        dateFormat = new SimpleDateFormat("dd-MM-yyy");
        found = false;
        //get events bbdd
        getAllTransactionsData();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Log.i("DATE", dayOfMonth + "-" + month + "-" + year);
                ArrayList<Transaccion> newTransacciones = new ArrayList<>();
                for (Transaccion t: transacciones) {

                    c.setTime(t.getFecha()); // yourdate is an object of type Date
                    Log.i("DATE", c.get(Calendar.DAY_OF_MONTH) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.YEAR));

                    if(c.get(Calendar.DAY_OF_MONTH) == dayOfMonth && c.get(Calendar.MONTH) == month && c.get(Calendar.YEAR) == year) {
                        newTransacciones.add(t);
                    }
                }
                listView.setAdapter(new ListViewAdapter(getApplicationContext(), newTransacciones));
                listView.setVisibility(View.VISIBLE);
            }
        });

    }

    public void getAllTransactionsData(){
        db.collection("users")
                .document(this.user.getEmail())
                .collection("bankAcounts")
                .document("cuentaPrincipal")
                .collection("transactions")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            transacciones.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Transaccion transaccion = document.toObject(Transaccion.class);
                                transacciones.add(transaccion);
                            }
                        } else {
                            Toast.makeText(Calendario.this, "Ha ocurrido un error al conectarse a la base de datos", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}