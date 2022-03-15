package android.ejemplo.atami.permisos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.ejemplo.atami.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermisosAlmacenaje extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 101;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permisos_almacenaje);
    }

    public void darPermisos(View _) {
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(PermisosAlmacenaje.this, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(PermisosAlmacenaje.this, new String[] { permission }, requestCode);
        }/* else { //hace falta else?
            Toast.makeText(PermisosAlmacenaje.this, "Permission already granted", Toast.LENGTH_SHORT).show();
            intent = new Intent(this, PantallaPrincipal.class);
            startActivity(intent);
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(PermisosAlmacenaje.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
                //MOVER SIGUIENTE ACTIVITY
                intent = new Intent(this, PermisosMicro.class); //permisos micro
                startActivity(intent);
            } else {
                Toast.makeText(PermisosAlmacenaje.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
