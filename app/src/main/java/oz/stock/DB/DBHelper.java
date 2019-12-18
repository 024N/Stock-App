package oz.stock.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import oz.stock.Model.Stock;


public class DBHelper extends SQLiteOpenHelper {

    private String TAG = "SQLiteOpenHelper";

    private static final String DATABASE_NAME = "stock";
    // Contacts table name
    private static final String TABLE_STOCK = "stockTable";

    String stockSQL = "CREATE TABLE "
            + TABLE_STOCK + "(id INTEGER PRIMARY KEY, "
            + "info TEXT, "
            + "price TEXT, "
            + "product TEXT, "
            + "unit TEXT, "
            + "documentID TEXT" + ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.d("DBHelper", "Stock SQL : " + stockSQL);
        database.execSQL(stockSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCK);
        onCreate(database);
    }

    @Override
    public void onDowngrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        onUpgrade(database, oldVersion, newVersion);
    }

    public void insertStock(Stock stock, String documentID) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("info", stock.getInfo());
        values.put("price", stock.getPrice());
        values.put("product", stock.getProduct());
        values.put("unit", stock.getUnit());
        values.put("documentID", documentID);

        db.insert(TABLE_STOCK, null, values);
    }

    public void createTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
    }

    public void deleteStock() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCK);
    }

    public ArrayList<Stock> getAllStocks() {
        ArrayList<Stock> stocks = new ArrayList<Stock>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_STOCK,
                new String[]{"id", "info", "price", "product", "unit", "documentID"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Stock stock = new Stock();
            stock.setInfo(cursor.getString(1));
            stock.setPrice(cursor.getString(2));
            stock.setProduct(cursor.getString(3));
            stock.setUnit(cursor.getString(4));
            stock.setDocumentID(cursor.getString(5));

            stocks.add(stock);
        }
        db.close();
        return stocks;
    }
}