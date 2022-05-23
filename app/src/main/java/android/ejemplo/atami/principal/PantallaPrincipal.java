package android.ejemplo.atami.principal;

import android.content.Intent;
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
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
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

import java.util.ArrayList;
import java.util.List;

public class PantallaPrincipal extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ListView listViewGastos;
    ListView listViewIngresos;
    TextView textView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    Intent intent;

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

                if (id == R.id.perfil) {
                    if (drawerLayout.isDrawerOpen(Gravity.LEFT))
                        drawerLayout.closeDrawer(Gravity.LEFT);
                    intent = new Intent(PantallaPrincipal.this, Perfil.class);
                    startActivity(intent);
                } else if (id == R.id.graficos) {
                    if (drawerLayout.isDrawerOpen(Gravity.LEFT))
                        drawerLayout.closeDrawer(Gravity.LEFT);
                    intent = new Intent(PantallaPrincipal.this, Graficos.class);
                    startActivity(intent);
                } else if (id == R.id.calendario) {
                    if (drawerLayout.isDrawerOpen(Gravity.LEFT))
                        drawerLayout.closeDrawer(Gravity.LEFT);
                    intent = new Intent(PantallaPrincipal.this, Calendario.class);
                    startActivity(intent);
                } else if (id == R.id.premium) {
                    if (drawerLayout.isDrawerOpen(Gravity.LEFT))
                        drawerLayout.closeDrawer(Gravity.LEFT);
                    intent = new Intent(PantallaPrincipal.this, Premium.class);
                    startActivity(intent);
                } else if (id == R.id.cuentas) {
                    if (drawerLayout.isDrawerOpen(Gravity.LEFT))
                        drawerLayout.closeDrawer(Gravity.LEFT);
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

    public void addMoneyOnClick(View _) {
        startActivity(new Intent(PantallaPrincipal.this, PopUpWindowAddMoney.class));
    }

    public void takeOutMoneyOnClick(View _) {
        startActivity(new Intent(PantallaPrincipal.this, PopUpWindowTakeOut.class));
    }

    public void mostrarMenu(View _) {
        if (!drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.openDrawer(Gravity.LEFT);
        } else {
            drawerLayout.closeDrawer(Gravity.RIGHT);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }
    }

    //https://material.io/components/buttons-floating-action-button/android#regular-fabs
    //https://maven.google.com/web/index.html#
    //https://stackoverflow.com/questions/22530394/how-to-open-sliding-menu-on-buttons-click-event
    //https://www.youtube.com/watch?v=63Ipzp9U_bU
    //https://www.youtube.com/watch?v=do4vb0MdLFY ->slidemenu
}