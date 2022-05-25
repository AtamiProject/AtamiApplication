package android.ejemplo.atami.principal;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.ejemplo.atami.graficos.Anual;
import android.ejemplo.atami.graficos.Semestral;
import android.ejemplo.atami.graficos.Trimestral;
import android.ejemplo.atami.model.Cuenta_bancaria;
import android.ejemplo.atami.model.Transaccion;
import android.ejemplo.atami.popUpWindow.PopUpWindowAddMoney;
//import android.ejemplo.atami.PopUpWindow.PopUpWindowTakeOut;
import android.ejemplo.atami.popUpWindow.PopUpWindowTakeOut;
import android.ejemplo.atami.R;
import android.ejemplo.atami.calendario.Calendario;
import android.ejemplo.atami.cuentas.Cuentas;
import android.ejemplo.atami.graficos.Graficos;
import android.ejemplo.atami.perfil.Perfil;
import android.ejemplo.atami.premium.Premium;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class PantallaPrincipal extends AppCompatActivity {

    ListView listViewGastos;
    ListView listViewIngresos;
    TextView textView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    Intent intent;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private List<Transaccion> transacciones = new ArrayList<>();
    public static List<Transaccion> filteredTransactions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_principal);

        listViewGastos = (ListView) findViewById(R.id.gastos);
        listViewIngresos = (ListView) findViewById(R.id.ingresos);
        textView = (TextView) findViewById(R.id.total);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if(id == R.id.perfil) {
                    if(drawerLayout.isDrawerOpen(Gravity.LEFT)) drawerLayout.closeDrawer(Gravity.LEFT);
                    intent = new Intent(PantallaPrincipal.this, Perfil.class);
                    startActivity(intent);
                } else if (id == R.id.graficos) {
                    if(drawerLayout.isDrawerOpen(Gravity.LEFT)) drawerLayout.closeDrawer(Gravity.LEFT);
                    openDialog();
                    /*intent = new Intent(PantallaPrincipal.this, Graficos.class);
                    startActivity(intent);*/

                } else if (id == R.id.calendario) {
                    if(drawerLayout.isDrawerOpen(Gravity.LEFT)) drawerLayout.closeDrawer(Gravity.LEFT);
                    intent = new Intent(PantallaPrincipal.this, Calendario.class);
                    startActivity(intent);
                } else if (id == R.id.premium) {
                    if(drawerLayout.isDrawerOpen(Gravity.LEFT)) drawerLayout.closeDrawer(Gravity.LEFT);
                    intent = new Intent(PantallaPrincipal.this, Premium.class);
                    startActivity(intent);
                } else if (id == R.id.cuentas) {
                    if(drawerLayout.isDrawerOpen(Gravity.LEFT)) drawerLayout.closeDrawer(Gravity.LEFT);
                    intent = new Intent(PantallaPrincipal.this, Cuentas.class);
                    startActivity(intent);
                }
                //Toast.makeText(PantallaPrincipal.this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        getBankAccountData();
        getAllTransactionsData();
    }

    public void getAllTransactionsData() {
        db.collection("users")
                .document(this.user.getEmail())
                .collection("bankAcounts")
                .document("cuentaPrincipal")
                .collection("transactions")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> ingresos = new ArrayList<String>();
                            List<String> gastos = new ArrayList<String>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Transaccion transaccion = document.toObject(Transaccion.class);
                                if(transaccion.getCantidad() >= 0){
                                    ingresos.add(transaccion.toString());
                                } else {
                                    gastos.add(transaccion.toString());
                                }
                            }
                            listViewGastos.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, gastos));
                            listViewIngresos.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, ingresos));
                            listViewGastos.setVisibility(View.VISIBLE);
                            listViewIngresos.setVisibility(View.VISIBLE);
                        } else {
                            //TODO
                        }
                    }
                });
    }

    public void getBankAccountData() {
        DocumentReference docRef = db.collection("users").document(this.user.getEmail()).collection("bankAcounts").document("cuentaPrincipal");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Cuenta_bancaria cuenta = documentSnapshot.toObject(Cuenta_bancaria.class);
                textView.setText("Total: " + cuenta.getTotal()+" €");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //TODO
            }
        });

    }

    public void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Escoge una opción:");
        builder.setItems(R.array.graficos, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                getAllTransactionsData(which);
                // The 'which' argument contains the index position
                // of the selected item
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    public void getAllTransactionsData(int seleccion){
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
                    filteredTransactions.clear();
                    switch(seleccion) {
                        case 0:
                            anual();
                            break;
                        case 1:
                            //meses -> 1-6 7-12
                            semestral();
                            break;
                        case 2:
                            //meses -> 1-3 4-6 7-9 10-12
                            trimestral(); // revisar recuento correcto
                            break;
                        case 3:
                            mensual();
                            break;
                    }
                } else {
                    Toast.makeText(PantallaPrincipal.this, "Ha ocurrido un error al conectarse a la base de datos", Toast.LENGTH_LONG).show();
                    //Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void anual() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+2"));
        int lastYear = Calendar.getInstance().get(Calendar.YEAR) - 1;
        Date dateAfter = new Date();
        try {
            DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String dateString = "31-12-".concat(String.valueOf(lastYear));
            dateAfter = sdf.parse(dateString);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        Date dateBefore = new Date();
        for (int i = 0; i < transacciones.size(); i++) {
            if(transacciones.get(i).getFecha().after(dateAfter) && transacciones.get(i).getFecha().before(dateBefore)) {
                filteredTransactions.add(transacciones.get(i));
            }
        }
        intent = new Intent(PantallaPrincipal.this, Anual.class);
        startActivity(intent);
    }

    private void semestral() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+2"));
        int lastYear = Calendar.getInstance().get(Calendar.YEAR) - 1;
        int actualYear = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1; // conseguir mes actual
        Date dateAfter = new Date();
        Date dateBefore = new Date();
        if(month >= 1 && month <= 6) {
            try {
                DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String dateString = "31-12-".concat(String.valueOf(lastYear));
                dateAfter = sdf.parse(dateString);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        } else {
            try {
                DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String dateString = "30-6-".concat(String.valueOf(actualYear));
                dateAfter = sdf.parse(dateString);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < transacciones.size(); i++) {
            if(transacciones.get(i).getFecha().after(dateAfter) && transacciones.get(i).getFecha().before(dateBefore)) {
                filteredTransactions.add(transacciones.get(i));
            }
        }
        intent = new Intent(PantallaPrincipal.this, Semestral.class);
        startActivity(intent);
    }

    private void trimestral() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+2"));
        int lastYear = Calendar.getInstance().get(Calendar.YEAR) - 1;
        int actualYear = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1; // conseguir mes actual
        Date dateAfter = new Date();
        Date dateBefore = new Date();
        if(month >= 1 && month <= 3) {
            try {
                DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String dateString = "31-12-".concat(String.valueOf(lastYear));
                dateAfter = sdf.parse(dateString);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        } else if(month >=4 && month <= 6) {
            try {
                DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String dateString = "31-3-".concat(String.valueOf(actualYear));
                dateAfter = sdf.parse(dateString);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        } else if(month >= 7 && month <= 9) {
            try {
                DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String dateString = "30-6-".concat(String.valueOf(actualYear));
                dateAfter = sdf.parse(dateString);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        } else {
            try {
                DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String dateString = "30-7-".concat(String.valueOf(actualYear));
                dateAfter = sdf.parse(dateString);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < transacciones.size(); i++) {
            if(transacciones.get(i).getFecha().after(dateAfter) && transacciones.get(i).getFecha().before(dateBefore)) {
                filteredTransactions.add(transacciones.get(i));
            }
        }
        intent = new Intent(PantallaPrincipal.this, Trimestral.class);
        startActivity(intent);
    }

    private void mensual() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+2"));
        int lastYear = Calendar.getInstance().get(Calendar.YEAR) - 1;
        int actualYear = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1; // conseguir mes actual
        Date dateAfter = new Date();
        Date dateBefore = new Date();
        switch (month) {
            case 1:
                try {
                    DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    String dateString = "31-12-".concat(String.valueOf(lastYear));
                    dateAfter = sdf.parse(dateString);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    String dateString = "31-1-".concat(String.valueOf(actualYear));
                    dateAfter = sdf.parse(dateString);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    String dateString = "28-2-".concat(String.valueOf(actualYear));
                    dateAfter = sdf.parse(dateString);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    String dateString = "31-3-".concat(String.valueOf(actualYear));
                    dateAfter = sdf.parse(dateString);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                try {
                    DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    String dateString = "30-4-".concat(String.valueOf(actualYear));
                    dateAfter = sdf.parse(dateString);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                break;
            case 6:
                try {
                    DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    String dateString = "31-5-".concat(String.valueOf(actualYear));
                    dateAfter = sdf.parse(dateString);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                break;
            case 7:
                try {
                    DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    String dateString = "30-6-".concat(String.valueOf(actualYear));
                    dateAfter = sdf.parse(dateString);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                break;
            case 8:try {
                DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String dateString = "31-7-".concat(String.valueOf(actualYear));
                dateAfter = sdf.parse(dateString);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
                break;
            case 9:
                try {
                    DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    String dateString = "31-8-".concat(String.valueOf(actualYear));
                    dateAfter = sdf.parse(dateString);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                break;
            case 10:
                try {
                    DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    String dateString = "30-9-".concat(String.valueOf(actualYear));
                    dateAfter = sdf.parse(dateString);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                break;
            case 11:
                try {
                    DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    String dateString = "31-10-".concat(String.valueOf(actualYear));
                    dateAfter = sdf.parse(dateString);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                break;
            case 12:
                try {
                    DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    String dateString = "30-11-".concat(String.valueOf(actualYear));
                    dateAfter = sdf.parse(dateString);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                break;
        }
        for (int i = 0; i < transacciones.size(); i++) {
            if(transacciones.get(i).getFecha().after(dateAfter) && transacciones.get(i).getFecha().before(dateBefore)) {
                filteredTransactions.add(transacciones.get(i));
            }
        }
        intent = new Intent(PantallaPrincipal.this, Semestral.class);
        startActivity(intent);
    }

    public void addMoneyOnClick(View _){
        startActivity(new Intent(PantallaPrincipal.this, PopUpWindowAddMoney.class));
    }
    public void takeOutMoneyOnClick(View _){
        startActivity(new Intent(PantallaPrincipal.this, PopUpWindowTakeOut.class));
    }

    public void mostrarMenu(View _) {
        if(!drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.openDrawer(Gravity.LEFT);
        } else {
            drawerLayout.closeDrawer(Gravity.RIGHT);
        }
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }

    //https://material.io/components/buttons-floating-action-button/android#regular-fabs
    //https://maven.google.com/web/index.html#
    //https://stackoverflow.com/questions/22530394/how-to-open-sliding-menu-on-buttons-click-event
    //https://www.youtube.com/watch?v=63Ipzp9U_bU
    //https://www.youtube.com/watch?v=do4vb0MdLFY ->slidemenu
}