package android.ejemplo.atami.permisos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.ejemplo.atami.R;
import android.ejemplo.atami.principal.PantallaPrincipal;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermisosMicro extends AppCompatActivity {

    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permisos_micro);
    }

    public void darPermisos2(View _) {
        checkPermission(Manifest.permission.RECORD_AUDIO,REQUEST_AUDIO_PERMISSION_CODE);
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(PermisosMicro.this, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(PermisosMicro.this, new String[] { permission }, requestCode);
        } /*else { //hace falta else?
            Toast.makeText(PermisosMicro.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        intent = new Intent(this, PantallaPrincipal.class);
        if (requestCode == REQUEST_AUDIO_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(PermisosMicro.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
                //MOVER SIGUIENTE ACTIVITY
                startActivity(intent);
            } else {
                Toast.makeText(PermisosMicro.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        }
    }
}
