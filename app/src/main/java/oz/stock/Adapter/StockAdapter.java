package oz.stock.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import oz.stock.Model.Stock;
import oz.stock.R;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.MyViewHolder> {

    private ArrayList<Stock> stocks;
    private LayoutInflater inflater;
    private Context context;

    public StockAdapter(Context context, ArrayList<Stock> stocks) {
        inflater = LayoutInflater.from(context);
        this.stocks = stocks;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.stock_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Stock selectedStock = stocks.get(position);
        holder.setData(selectedStock, position);
    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView product, unit;

        MyViewHolder(View itemView) {
            super(itemView);
            product = (TextView) itemView.findViewById(R.id.product);
            unit = (TextView) itemView.findViewById(R.id.unit);
        }

        void setData(Stock selectedStock, int position) {
            this.product.setText(selectedStock.getProduct());
            this.unit.setText(selectedStock.getUnit());
        }
    }
}