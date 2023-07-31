package com.example.scanbarecode;

public class item {

     private long barecode;
     private String articleName;
     private String seller;
     private String date;
     private int buyingprice;
     private int sellingprice;
     private int quantity;

     public item(long barecode, String articleName, String seller, String date, int buyingprice, int sellingprice, int quantity) {
          this.barecode = barecode;
          this.articleName = articleName;
          this.seller = seller;
          this.date = date;
          this.buyingprice = buyingprice;
          this.sellingprice = sellingprice;
          this.quantity = quantity;
     }

     public long getBarecode() {
          return barecode;
     }

     public String getArticleName() {
          return articleName;
     }

     public String  getSeller() {
          return seller;
     }

     public String getDate() {
          return date;
     }

     public int getBuyingprice() {
          return buyingprice;
     }

     public int getSellingprice() {
          return sellingprice;
     }

     public int getQuantity() {
          return quantity;
     }

     public void setBarecode(long barecode) {
          this.barecode = barecode;
     }

     public void setArticleName(String articleName) {
          this.articleName = articleName;
     }

     public void setSeller(String seller) {
          this.seller = seller;
     }

     public void setDate(String date) {
          this.date = date;
     }

     public void setBuyingprice(int buyingprice) {
          this.buyingprice = buyingprice;
     }

     public void setSellingprice(int sellingprice) {
          this.sellingprice = sellingprice;
     }

     public void setQuantity(int quantity) {
          this.quantity = quantity;
     }
}
