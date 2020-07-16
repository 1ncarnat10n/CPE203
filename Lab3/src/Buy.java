public class Buy {
    private String product;
    private int product_cost;
    private int quantity;

    public Buy (String product, String product_cost, String quantity) {
        this.product = product;
        this.product_cost = Integer.parseInt(product_cost);
        this.quantity = Integer.parseInt(quantity);
    }

    public String getProduct() {
        return product;
    }

    public int getProduct_cost() {
        return product_cost;
    }

    public int getQuantity() {
        return quantity;
    }
}
