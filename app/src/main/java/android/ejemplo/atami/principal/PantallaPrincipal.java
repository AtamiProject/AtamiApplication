package android.ejemplo.atami.principal;

import android.content.Intent;
import android.ejemplo.atami.PopUpWindow.PopUpWindow;
import android.ejemplo.atami.R;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class PantallaPrincipal extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_principal);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    public void addMoneyOnClick(View _){
            startActivity(new Intent(PantallaPrincipal.this, PopUpWindow.class));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
