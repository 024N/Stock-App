package oz.stock.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import oz.stock.Adapter.StockAdapter;
import oz.stock.DB.DBHelper;
import oz.stock.Model.Stock;
import oz.stock.R;
import oz.stock.Utils.RecyclerItemClickListener;

public class StockFragment extends BaseFragment {
    private DBHelper dbHelper;
    private String TAG = "StockFragment";
    private Button btnSearch;
    private EditText edtSearch;
    private String searchedWord;
    private RecyclerView macRecyclerView;

    private Stock stockItem;

    private ArrayList<Stock> searchList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.stock_fragment, container, false);
        macRecyclerView = view.findViewById(R.id.stock_recylerview);
        setHideKeyboardOnTouch(getActivity(), view);

        btnSearch = view.findViewById(R.id.btn_search);
        edtSearch = view.findViewById(R.id.edt_search);

        dbHelper = new DBHelper(getActivity());

        final ArrayList<Stock> sortedList = getSortedList();
        Collections.reverse(sortedList);

        StockAdapter stockAdapter = new StockAdapter(getActivity(), sortedList);
        macRecyclerView.setAdapter(stockAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        macRecyclerView.setLayoutManager(linearLayoutManager);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtSearch.getText() != null){
                    searchedWord = edtSearch.getText().toString().trim();
                    try {
                        search(sortedList);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        macRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), macRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Log.i(TAG, "Selected position and symbol :" + position + "/" + sortedList.get(position));
                        stockItem = null;
                        if(searchList != null)
                            stockItem = searchList.get(position);
                        else
                            stockItem = sortedList.get(position);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showPopup(stockItem);
                            }
                        });
                    }

                    @Override public void onLongItemClick(View view, int position) {
                    }
                })
        );

        return view;
    }

    private ArrayList<Stock> getDataFromDB() {
        return  dbHelper.getAllStocks();
    }

    private ArrayList<Stock> getSortedList(){

        ArrayList<Stock> sortedList = getDataFromDB();

        Collections.sort( sortedList, new Comparator<Stock>() {
            @Override
            public int compare(Stock o1, Stock o2) {
                return o1.getDocumentID().compareTo(o2.getDocumentID());
            }
        });
        return sortedList;
    }

    public void search(ArrayList<Stock> response) throws ParserConfigurationException, SAXException, IOException {
        ArrayList<Stock> rowItemList = new ArrayList<Stock>();
        StockAdapter stockAdapter;

        if(!(searchedWord.isEmpty() || searchedWord == null)){
            for (int i = 0; i < response.size(); i++){
                if (searchedWord.equalsIgnoreCase(response.get(i).getProduct()))
                    rowItemList.add(response.get(i));
            }
            stockAdapter = new StockAdapter(getActivity(), rowItemList);
            searchList = (ArrayList<Stock>)rowItemList.clone();
            if(rowItemList.size() == 0)
                Toast.makeText(getContext(), "No data", Toast.LENGTH_SHORT).show();
        } else{
            stockAdapter = new StockAdapter(getActivity(), response);
            searchList = null;
        }
        macRecyclerView.setAdapter(stockAdapter);

        stockAdapter.notifyDataSetChanged();
        macRecyclerView.invalidate();
    }

    private void showPopup(Stock stockItemInfo) {

        final Dialog myDialog = new Dialog(getActivity());
        myDialog.setContentView(R.layout.stock_dialog_page);

        TextView popupClose, popupProduct, popupUnit, popupPrice, popupInfo;

        popupClose = myDialog.findViewById(R.id.tv_close_popup);

        popupProduct =  myDialog.findViewById(R.id.tv_popup_product);
        popupUnit = myDialog.findViewById(R.id.tv_popup_unit);
        popupPrice =  myDialog.findViewById(R.id.tv_popup_price);
        popupInfo =  myDialog.findViewById(R.id.tv_popup_info);

        popupProduct.setText(stockItemInfo.getProduct());
        popupUnit.setText(stockItemInfo.getUnit());
        popupPrice.setText(stockItemInfo.getPrice());
        popupInfo.setText(stockItemInfo.getInfo());

        popupClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public static void setHideKeyboardOnTouch(final Context context, View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        try {
            //Set up touch listener for non-text box views to hide keyboard.
            if (!(view instanceof EditText || view instanceof ScrollView)) {
                view.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        return false;
                    }
                });
            }
            //If a layout container, iterate over children and seed recursion.
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    View innerView = ((ViewGroup) view).getChildAt(i);
                    setHideKeyboardOnTouch(context, innerView);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}