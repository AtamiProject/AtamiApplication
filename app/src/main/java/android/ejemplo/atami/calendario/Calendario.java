package android.ejemplo.atami.calendario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.ejemplo.atami.R;
import android.ejemplo.atami.modelo.Eventos;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Calendario extends AppCompatActivity {

    CalendarView calendarView;
    ListView listView;
    private ArrayList<Eventos> e;
    private boolean found;
    private SimpleDateFormat dateFormat;
    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendario);

        c = Calendar.getInstance();
        listView = (ListView) findViewById(R.id.listaEventos);
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        dateFormat = new SimpleDateFormat("dd-MM-yyy");
        found = false;
        e = new ArrayList<>();
        //adding events manually
        try {
            e.add(new Eventos(dateFormat.parse("07-04-2022"),"Evento1", "Este es el primer evento"));
            e.add(new Eventos(dateFormat.parse("07-04-2022"),"Evento2", "Este es el segundo evento"));
            e.add(new Eventos(dateFormat.parse("10-04-2022"),"Evento3", "Este es el tercer evento"));
            e.add(new Eventos(dateFormat.parse("27-04-2022"),"Evento4", "Este es el cuarto evento"));
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Log.i("DATE", dayOfMonth + "-" + month + "-" + year);
                List<String> newEv = new ArrayList<>();
                for (Eventos ev: e) {

                    c.setTime(ev.getFecha()); // yourdate is an object of type Date
                    Log.i("DATE", c.get(Calendar.DAY_OF_MONTH) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.YEAR));

                    if(c.get(Calendar.DAY_OF_MONTH) == dayOfMonth && c.get(Calendar.MONTH) == month && c.get(Calendar.YEAR) == year) {
                        newEv.add(ev.toString());
                    }
                }
                listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, newEv));
                listView.setVisibility(View.VISIBLE);
            }
        });

    }


}