package oz.stock.Utils;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import oz.stock.Model.Stock;

public class Util {

    private String TAG = "Util";
    private FirebaseFirestore database;

    public void createStockData(Stock stock) {
        Map<String, String> stocks = new HashMap<>();
        stocks.put("product", stock.getProduct());
        stocks.put("unit", stock.getUnit());

        long now = System.currentTimeMillis();

        database = FirebaseFirestore.getInstance();
        database.collection("stock").document(String.valueOf(now))
                .set(stocks)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

}