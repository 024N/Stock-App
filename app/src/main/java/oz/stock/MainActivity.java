package oz.stock;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import oz.stock.DB.DBHelper;
import oz.stock.Fragment.StockFragment;
import oz.stock.Model.Stock;
import oz.stock.Utils.Util;

import android.util.Log;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

    private String TAG = "MainActivity";
    private FirebaseFirestore database;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        startProgressDialog();
        if(isNetworkAvailable()) {
            database = FirebaseFirestore.getInstance();            // Access a Cloud Firestore instance from your Activity
            readAdminFromCloud();
            readStockFromCloud();
        }
        else{
            fragmentRouter("stockFragment");
            Toast.makeText(this, "Güncellenemiyor, lütfen internet bağlantınızı kontrol edin!", Toast.LENGTH_LONG).show();
            stopProgressDialog();
        }
    }

    private void readStockFromCloud(){
        database.collection("stock")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            dbHelper.deleteStock();
                            dbHelper.createTables();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                dbHelper.insertStock(document.toObject(Stock.class), document.getId());
                            }
                            fragmentRouter("stockFragment");
                            stopProgressDialog();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                            Toast.makeText(getApplicationContext(),"Bir hata oluştu daha sonra tekrar deneyiniz.", Toast.LENGTH_LONG).show();
                            stopProgressDialog();
                        }
                    }
                });
    }

    private void readAdminFromCloud(){
        database.collection("admin")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if(!document.getData().get("sifre").equals("123asd12345")){
                                    Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                                    homeIntent.addCategory( Intent.CATEGORY_HOME );
                                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(homeIntent);
                                }
                            }
                        } else {
                        }
                    }
                });
    }

    private void fragmentRouter(String fragmentType){
        if(!isNetworkAvailable()) {
            Toast.makeText(this, "Güncellenemiyor, lütfen internet bağlantınızı kontrol edin!", Toast.LENGTH_LONG).show();
        }

        Fragment fragment = new StockFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); //fragmentleri temizler
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
                .replace(R.id.content, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(getIntent());
    }
}