package android.ejemplo.atami.operaciones.addMoney;

import android.app.Activity;
import android.ejemplo.atami.R;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class AddMoneyActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_money_manual);
    }
}
