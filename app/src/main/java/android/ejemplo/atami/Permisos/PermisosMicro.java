package android.ejemplo.atami.Permisos;

import android.Manifest;
import android.content.pm.PackageManager;
import android.ejemplo.atami.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermisosMicro extends AppCompatActivity {
    private Button buttonPermisos;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permisos_micro);
    }

    public void darPermisos(View _) {
        checkPermission(Manifest.permission.RECORD_AUDIO,REQUEST_AUDIO_PERMISSION_CODE);
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(PermisosMicro.this, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(PermisosMicro.this, new String[] { permission }, requestCode);
        } else { //hace falta else?
            Toast.makeText(PermisosMicro.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_AUDIO_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(PermisosMicro.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
                //MOVER SIGUIENTE ACTIVITY
            } else {
                Toast.makeText(PermisosMicro.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
