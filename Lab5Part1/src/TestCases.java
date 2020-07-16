import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.*;
import org.junit.rules.*;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;

import org.junit.Test;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCases
{
   public static final double DELTA = 0.00001;
   private Bindings bindings = new VariableBindings();
   
   @Before
   public void init() 
   {
      bindings.addBinding("x", 2.5);
      bindings.addBinding("y", 10);
      bindings.addBinding("a", 0);
      bindings.addBinding("b", 3.5);
      bindings.addBinding("c", 2.5);
      bindings.addBinding("d", 0);
      bindings.addBinding("e", -1);
      bindings.addBinding("f", -3);
   }

   @Test
   public void test01_AddExpressionEvaluate()
   {
      IdentifierExpression x = new IdentifierExpression("x");
      IdentifierExpression y = new IdentifierExpression("y");
      IdentifierExpression a = new IdentifierExpression("a");
      IdentifierExpression b = new IdentifierExpression("b");
      IdentifierExpression c = new IdentifierExpression("c");
      IdentifierExpression d = new IdentifierExpression("d");
      IdentifierExpression e = new IdentifierExpression("e");
      IdentifierExpression f = new IdentifierExpression("f");
      AddExpression add = new AddExpression(x, y);
      AddExpression add2 = new AddExpression(a, b);
      AddExpression add3 = new AddExpression(c, d);
      AddExpression add4 = new AddExpression(e, f);

      assertEquals(12.5, add.evaluate(bindings), DELTA);
      assertEquals(3.5, add2.evaluate(bindings), DELTA);
      assertEquals(2.5, add3.evaluate(bindings), DELTA);
      assertEquals(-4.0, add4.evaluate(bindings), DELTA);

   }

   @Test
   public void test02_AddExpressionToString()
   {
      IdentifierExpression x = new IdentifierExpression("x");
      IdentifierExpression y = new IdentifierExpression("y");
      IdentifierExpression z = new IdentifierExpression(null);
      AddExpression add = new AddExpression(x, y);
      AddExpression add2 = new AddExpression(x, z);

      assertEquals("(x + y)", add.toString());
      assertEquals("(x + null)", add2.toString());
   }

   @Test
   public void test01_DivideExpressionEvaluate()
   {
      IdentifierExpression x = new IdentifierExpression("x");
      IdentifierExpression y = new IdentifierExpression("y");
      IdentifierExpression a = new IdentifierExpression("a");
      IdentifierExpression b = new IdentifierExpression("b");
      IdentifierExpression c = new IdentifierExpression("c");
      IdentifierExpression d = new IdentifierExpression("d");
      IdentifierExpression e = new IdentifierExpression("e");
      IdentifierExpression f = new IdentifierExpression("f");
      DivideExpression divide = new DivideExpression(x, y);
      DivideExpression divide2 = new DivideExpression(a, b);
      DivideExpression divide3 = new DivideExpression(c, d);
      DivideExpression divide4 = new DivideExpression(e, f);
      DivideExpression divide5 = new DivideExpression(a, d);

      assertEquals(.25, divide.evaluate(bindings), DELTA);
      assertEquals(0, divide2.evaluate(bindings), DELTA);
      assertEquals(Double.NaN, divide3.evaluate(bindings), DELTA);
      assertEquals(.333333333, divide4.evaluate(bindings), DELTA);
      assertEquals(Double.NaN, divide5.evaluate(bindings), DELTA);


   }

   @Test
   public void test01_MultiplyExpressionEvaluate()
   {
      IdentifierExpression x = new IdentifierExpression("x");
      IdentifierExpression y = new IdentifierExpression("y");
      IdentifierExpression a = new IdentifierExpression("a");
      IdentifierExpression b = new IdentifierExpression("b");
      IdentifierExpression c = new IdentifierExpression("c");
      IdentifierExpression d = new IdentifierExpression("d");
      IdentifierExpression e = new IdentifierExpression("e");
      IdentifierExpression f = new IdentifierExpression("f");
      MultiplyExpression multiply = new MultiplyExpression(x, y);
      MultiplyExpression multiply2 = new MultiplyExpression(a, b);
      MultiplyExpression multiply3 = new MultiplyExpression(c, d);
      MultiplyExpression multiply4 = new MultiplyExpression(e, f);
      MultiplyExpression multiply5 = new MultiplyExpression(c, f);

      assertEquals(25, multiply.evaluate(bindings), DELTA);
      assertEquals(0, multiply2.evaluate(bindings), DELTA);
      assertEquals(0, multiply3.evaluate(bindings), DELTA);
      assertEquals(3, multiply4.evaluate(bindings), DELTA);
      assertEquals(-7.5, multiply5.evaluate(bindings), DELTA);

   }

   @Test
   public void test01_SubtractExpressionEvaluate()
   {
      IdentifierExpression x = new IdentifierExpression("x");
      IdentifierExpression y = new IdentifierExpression("y");
      IdentifierExpression a = new IdentifierExpression("a");
      IdentifierExpression b = new IdentifierExpression("b");
      IdentifierExpression c = new IdentifierExpression("c");
      IdentifierExpression d = new IdentifierExpression("d");
      IdentifierExpression e = new IdentifierExpression("e");
      IdentifierExpression f = new IdentifierExpression("f");
      SubtractExpression subtract = new SubtractExpression(x, y);
      SubtractExpression subtract2 = new SubtractExpression(a, b);
      SubtractExpression subtract3 = new SubtractExpression(c, d);
      SubtractExpression subtract4 = new SubtractExpression(e, f);
      SubtractExpression subtract5 = new SubtractExpression(c, f);
      SubtractExpression subtract6 = new SubtractExpression(x, c);

      assertEquals(-7.5, subtract.evaluate(bindings), DELTA);
      assertEquals(-3.5, subtract2.evaluate(bindings), DELTA);
      assertEquals(2.5, subtract3.evaluate(bindings), DELTA);
      assertEquals(2, subtract4.evaluate(bindings), DELTA);
      assertEquals(5.5, subtract5.evaluate(bindings), DELTA);
      assertEquals(0, subtract6.evaluate(bindings), DELTA);

   }

}