class DivideExpression extends BinaryExpression {
   public DivideExpression(final Expression lft, final Expression rht)
   {
      super(lft, rht, "/");
   }

   public double _applyOperator(double lft, double rht) {
      if (lft != 0 && rht == 0) {
         return Double.NaN;
      }
      return lft/rht;
   }
}

