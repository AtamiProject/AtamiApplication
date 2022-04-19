package android.ejemplo.atami.operaciones.takeOut;

import android.app.Activity;
import android.ejemplo.atami.R;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class TakeOutMoneyActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.takeout_money_manual);
    }
}
