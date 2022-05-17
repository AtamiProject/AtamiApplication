package android.ejemplo.atami.graficos;

import androidx.appcompat.app.AppCompatActivity;

import android.ejemplo.atami.R;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Date;

public class Graficos extends AppCompatActivity {

    Spinner spinner;
    private String selected;
    ViewStub stub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graficos);

        stub = (ViewStub) findViewById(R.id.layout_stub);

        spinner = (Spinner) findViewById(R.id.spinner_graficos);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.graficos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //https://www.youtube.com/watch?v=vhKtbECeazQ

        /*PieChart pieChart = (PieChart) findViewById(R.id.piechart_view);

        ArrayList<PieEntry> dinero = new ArrayList<>();
        dinero.add(new PieEntry(12.9f,"En."));
        dinero.add(new PieEntry(16.5f, "Febr."));
        dinero.add(new PieEntry(22.6f, "Mzo."));
        dinero.add(new PieEntry(15.8f, "Abr."));
        dinero.add(new PieEntry(52.8f, "My."));
        dinero.add(new PieEntry(34.3f, "Jun."));
        dinero.add(new PieEntry(12.3f, "Jul."));
        dinero.add(new PieEntry(45.3f, "Ag."));
        dinero.add(new PieEntry(32.1f, "Sept."));
        dinero.add(new PieEntry(12.32f, "Oct."));
        dinero.add(new PieEntry(25.25f, "Nov."));
        dinero.add(new PieEntry(10.12f, "Dic."));

        PieDataSet pieDataSet = new PieDataSet(dinero, "");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Resumen anual");
        pieChart.animate();*/

    }

    public void seleccionar(View _) {
        //System.out.println(spinner.getSelectedItem().toString());
        Date actual = new Date();
        selected = spinner.getSelectedItem().toString().toLowerCase();
        switch(selected) {
            case "anual":
                //System.out.println("anual"); -> entre 1 enero del año actual a fecha actual
                stub.setLayoutResource(R.layout.anual);
                stub.inflate();
                break;
            case "semestral":
                //System.out.println("semestral"); -> 1 enero - 30 junio, 1 julio - 31 diciembre
                break;
            case "trimestral":
                //System.out.println("trimestral"); ->
                break;
            case "mensual":
                System.out.println("mensual");
                break;
            case "personalizado":
                System.out.println("personalizado");
                break;
        }
    }
}