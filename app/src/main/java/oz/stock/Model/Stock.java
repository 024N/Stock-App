package oz.stock.Model;

import java.io.Serializable;

public class Stock implements Serializable {

    private String info;
    private String price;
    private String product;
    private String unit;
    private String documentID;

    public Stock() {
    }

    public Stock(String info, String price, String product, String unit) {
        this.info = info;
        this.price = price;
        this.product = product;
        this.unit = unit;
    }

    public Stock(String info, String price, String product, String unit, String documentID) {
        this.info = info;
        this.price = price;
        this.product = product;
        this.unit = unit;
        this.documentID = documentID;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "info='" + info + '\'' +
                ", price='" + price + '\'' +
                ", product='" + product + '\'' +
                ", unit='" + unit + '\'' +
                ", documentID='" + documentID + '\'' +
                '}';
    }
}