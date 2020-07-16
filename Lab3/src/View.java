public class View {
    private String product;
    private int product_cost;

    public View(String product, String product_cost) {
        this.product = product;
        this.product_cost = Integer.parseInt(product_cost);
    }
    public String getProduct() {return product;}
    public int getProduct_cost() {return product_cost;}
}
