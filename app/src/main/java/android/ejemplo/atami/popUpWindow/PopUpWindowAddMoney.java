package android.ejemplo.atami.popUpWindow;

import android.app.Activity;
import android.content.Intent;
import android.ejemplo.atami.R;
import android.ejemplo.atami.operaciones.OperacionPorVoz;
import android.ejemplo.atami.operaciones.addMoney.AddMoneyActivity;
import android.ejemplo.atami.permisos.PermisosAlmacenaje;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

public class PopUpWindowAddMoney extends Activity {

    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.addmoneylayout);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.4));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y=-20;

        getWindow().setAttributes(params);

    }

    public void addMoneyManual(View _){
        intent = new Intent(this, AddMoneyActivity.class);
        startActivity(intent);
    }
    public void addMoneyPorVoz(View _){
        intent = new Intent(this, OperacionPorVoz.class);
        startActivity(intent);
    }

}
