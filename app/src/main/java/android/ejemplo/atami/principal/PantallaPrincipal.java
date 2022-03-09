package android.ejemplo.atami.principal;

import android.content.Intent;
import android.ejemplo.atami.Permisos.PermisosMicro;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class PantallaPrincipal extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_principal);
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
                    intent = new Intent(PantallaPrincipal.this, Graficos.class);
                    startActivity(intent);
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
        }else{
            super.onBackPressed();
        }
    }

    //https://material.io/components/buttons-floating-action-button/android#regular-fabs
    //https://maven.google.com/web/index.html#
    //https://stackoverflow.com/questions/22530394/how-to-open-sliding-menu-on-buttons-click-event
    //https://www.youtube.com/watch?v=63Ipzp9U_bU
    //https://www.youtube.com/watch?v=do4vb0MdLFY ->slidemenu
}
