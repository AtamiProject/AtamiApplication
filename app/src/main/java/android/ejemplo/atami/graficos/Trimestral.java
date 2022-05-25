package android.ejemplo.atami.graficos;

import android.ejemplo.atami.R;
import android.ejemplo.atami.model.Transaccion;
import android.ejemplo.atami.principal.PantallaPrincipal;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Trimestral extends AppCompatActivity {

    ArrayList<Transaccion> transacciones = new ArrayList<>();
    float positive, negative, balance;

    TextView b;

    float casaTotalP, saludTotalP, transporteTotalP, ropaCalzadoTotalP, segurosTotalP, diversionTotalP, higieneTotalP, otrosTotalP;
    float casaTotalN, saludTotalN, transporteTotalN, ropaCalzadoTotalN, segurosTotalN, diversionTotalN, higieneTotalN, otrosTotalN;
    int countPositives, countNegatives;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trimestral);

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        transacciones.clear();
        transacciones.addAll(PantallaPrincipal.filteredTransactions);

        resetVariables();

        for(Transaccion t: transacciones) {
            if(t.getCantidad() > 0) {
                positive += t.getCantidad();
                switch(t.getCategoria().toUpperCase()) {
                    case "CASA":
                        casaTotalP += t.getCantidad();
                        break;
                    case "SALUD":
                        saludTotalP += t.getCantidad();
                        break;
                    case "TRANSPORTE":
                        transporteTotalP += t.getCantidad();
                        break;
                    case "ROPA Y CALZADO":
                        ropaCalzadoTotalP += t.getCantidad();
                        break;
                    case "SEGUROS":
                        segurosTotalP += t.getCantidad();
                        break;
                    case "DIVERSION":
                        diversionTotalP += t.getCantidad();
                        break;
                    case "HIGIENE":
                        higieneTotalP += t.getCantidad();
                        break;
                    case "OTROS":
                        otrosTotalP += t.getCantidad();
                        break;
                }
                countPositives++;
            } else {
                negative += t.getCantidad();
                switch(t.getCategoria().toUpperCase()) {
                    case "CASA":
                        casaTotalN += (-1)*t.getCantidad();
                        break;
                    case "SALUD":
                        saludTotalN += (-1)*t.getCantidad();
                        break;
                    case "TRANSPORTE":
                        transporteTotalN += (-1)*t.getCantidad();
                        break;
                    case "ROPA Y CALZADO":
                        ropaCalzadoTotalN += (-1)*t.getCantidad();
                        break;
                    case "SEGUROS":
                        segurosTotalN += (-1)*t.getCantidad();
                        break;
                    case "DIVERSION":
                        diversionTotalN += (-1)*t.getCantidad();
                        break;
                    case "HIGIENE":
                        higieneTotalN += (-1)*t.getCantidad();
                        break;
                    case "OTROS":
                        otrosTotalN += (-1)*t.getCantidad();
                        break;
                }
                countNegatives++;
            }
        }

        balance = positive + negative;

        b = (TextView) findViewById(R.id.balance);
        b.setText("Balance: " + df.format(balance).concat(" €"));

        barChart();
        pieChartPositive();
        pieChartNegative();
    }

    private void resetVariables() {
        positive = 0.0f;
        negative = 0.0f;
        balance = 0.0f;

        countPositives = 0;
        countNegatives = 0;

        casaTotalP = 0;
        saludTotalP = 0;
        transporteTotalP = 0;
        ropaCalzadoTotalP = 0;
        segurosTotalP = 0;
        diversionTotalP = 0;
        higieneTotalP = 0;
        otrosTotalP = 0;

        casaTotalN = 0;
        saludTotalN = 0;
        transporteTotalN = 0;
        ropaCalzadoTotalN = 0;
        segurosTotalN = 0;
        diversionTotalN = 0;
        higieneTotalN = 0;
        otrosTotalN = 0;
    }

    private void barChart() {
        BarChart barChart = (BarChart) findViewById(R.id.barChart);
        ArrayList<BarEntry> dinero = new ArrayList<>();
        dinero.add(new BarEntry(1, positive));
        dinero.add(new BarEntry(2, negative));

        BarDataSet barDataSet = new BarDataSet(dinero, "");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        barChart.setScaleYEnabled(false);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Semestral");
        barChart.getDescription().setTextSize(16f);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getXAxis().setDrawLabels(false);
    }

    public void pieChartPositive() {
        PieChart pieChartPositive = (PieChart) findViewById(R.id.pieChartPositive);

        ArrayList<PieEntry> positives = new ArrayList<>();
        if(casaTotalP != 0) positives.add(new PieEntry(casaTotalP, "CASA"));
        if(saludTotalP != 0) positives.add(new PieEntry(saludTotalP, "SALUD"));
        if(transporteTotalP != 0) positives.add(new PieEntry(transporteTotalP, "TRANSPORTE"));
        if(ropaCalzadoTotalP != 0) positives.add(new PieEntry(ropaCalzadoTotalP, "ROPA Y CALZADO"));
        if(segurosTotalP != 0) positives.add(new PieEntry(segurosTotalP, "SEGUROS"));
        if(diversionTotalP != 0) positives.add(new PieEntry(diversionTotalP, "DIVERSION"));
        if(higieneTotalP != 0) positives.add(new PieEntry(higieneTotalP, "HIGIENE"));
        if(otrosTotalP != 0) positives.add(new PieEntry(otrosTotalP, "OTROS"));

        PieDataSet pieDataSet = new PieDataSet(positives, "Ingresos");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);

        pieChartPositive.setData(pieData);
        pieChartPositive.getDescription().setEnabled(false);
        pieChartPositive.setCenterText("INGRESOS (€)");
        pieChartPositive.getLegend().setEnabled(false);
        pieChartPositive.setEntryLabelColor(Color.BLACK);
        pieChartPositive.animate();

    }

    public void pieChartNegative() {
        PieChart pieChartNegative = (PieChart) findViewById(R.id.pieChartNegative);

        ArrayList<PieEntry> negatives = new ArrayList<>();
        if(casaTotalN != 0) negatives.add(new PieEntry(casaTotalN, "CASA"));
        if(saludTotalN != 0) negatives.add(new PieEntry(saludTotalN, "SALUD"));
        if(transporteTotalN != 0) negatives.add(new PieEntry(transporteTotalN, "TRANSPORTE"));
        if(ropaCalzadoTotalN != 0) negatives.add(new PieEntry(ropaCalzadoTotalN, "ROPA Y CALZADO"));
        if(segurosTotalN != 0) negatives.add(new PieEntry(segurosTotalN, "SEGUROS"));
        if(diversionTotalN != 0) negatives.add(new PieEntry(diversionTotalN, "DIVERSION"));
        if(higieneTotalN != 0) negatives.add(new PieEntry(higieneTotalN, "HIGIENE"));
        if(otrosTotalN != 0) negatives.add(new PieEntry(otrosTotalN, "OTROS"));

        PieDataSet pieDataSet2 = new PieDataSet(negatives, "Gastos");
        pieDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet2.setValueTextColor(Color.BLACK);
        pieDataSet2.setValueTextSize(16f);

        PieData pieData2 = new PieData(pieDataSet2);

        pieChartNegative.setData(pieData2);
        pieChartNegative.getDescription().setEnabled(false);
        pieChartNegative.setCenterText("GASTOS (€)");
        pieChartNegative.getLegend().setEnabled(false);
        pieChartNegative.setEntryLabelColor(Color.BLACK);
        pieChartNegative.animate();

    }

}
