package android.ejemplo.atami.graficos;

import androidx.appcompat.app.AppCompatActivity;

import android.ejemplo.atami.R;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Graficos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graficos);

        //https://www.youtube.com/watch?v=vhKtbECeazQ
        //

        PieChart pieChart = (PieChart) findViewById(R.id.piechart_view);

        ArrayList<PieEntry> dinero = new ArrayList<>();
        dinero.add(new PieEntry(12,"En."));
        dinero.add(new PieEntry(16, "Febr."));
        dinero.add(new PieEntry(22, "Mzo."));
        dinero.add(new PieEntry(15, "Abr."));
        dinero.add(new PieEntry(56, "My."));
        dinero.add(new PieEntry(34, "Jun."));
        dinero.add(new PieEntry(12, "Jul."));
        dinero.add(new PieEntry(45, "Ag."));
        dinero.add(new PieEntry(32, "Sept."));
        dinero.add(new PieEntry(12, "Oct."));
        dinero.add(new PieEntry(25, "Nov."));
        dinero.add(new PieEntry(10, "Dic."));

        PieDataSet pieDataSet = new PieDataSet(dinero, "");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Resumen anual");
        pieChart.animate();

    }
}