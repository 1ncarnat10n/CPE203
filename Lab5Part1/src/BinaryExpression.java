public abstract class BinaryExpression implements Expression {
    private final Expression lft;
    private final Expression rht;
    private final String op;
    public BinaryExpression(final Expression lft, final Expression rht, String op) {
        this.lft = lft;
        this.rht = rht;
        this.op = op;
    }

    public double evaluate(Bindings bindings) {
        double left = lft.evaluate(bindings);
        double right = rht.evaluate(bindings);
         return _applyOperator(left, right);

    }
    public String toString() {
        return "(" + lft + " " + op + " " + rht + ")";
    }

    protected abstract double _applyOperator(double lft, double rht);
}
