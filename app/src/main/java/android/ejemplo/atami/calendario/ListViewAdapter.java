package android.ejemplo.atami.calendario;

import android.content.Context;
import android.ejemplo.atami.R;
import android.ejemplo.atami.model.Transaccion;
import android.graphics.Color;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    Context context;
    ArrayList<Transaccion> transacciones;
    LayoutInflater inflter;

    private SimpleDateFormat dateFormat;
    DecimalFormat df;


    public ListViewAdapter(Context applicationContext, ArrayList<Transaccion> transacciones) {
        this.context = applicationContext;
        this.transacciones = transacciones;
        inflter = (LayoutInflater.from(applicationContext));
        dateFormat = new SimpleDateFormat("dd-MM-yyy");
        df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
    }

    @Override
    public int getCount() {
        return transacciones.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_listview, null);
        TextView fecha = (TextView) view.findViewById(R.id.fecha);
        fecha.setText(dateFormat.format(transacciones.get(i).getFecha()));
        TextView categoria = (TextView) view.findViewById(R.id.categoria);
        categoria.setText(transacciones.get(i).getCategoria());
        TextView cantidad = (TextView) view.findViewById(R.id.cantidad);
        if(transacciones.get(i).getCantidad() < 0) cantidad.setTextColor(Color.RED);
        else cantidad.setTextColor(Color.parseColor("#12B115"));
        cantidad.setText(df.format(transacciones.get(i).getCantidad()) + " €");
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        icon.setImageResource(R.drawable.ic_baseline_keyboard_double_arrow_right_24);
        return view;
    }
}
