package com.zeitheron.hammercore.utils.math;

import java.util.ArrayList;
import java.util.List;

import com.zeitheron.hammercore.utils.Chars;
import com.zeitheron.hammercore.utils.math.functions.ExpressionFunction;
import com.zeitheron.hammercore.utils.math.functions.FunctionMath;

public class ExpressionEvaluator
{
	private final String str;
	private int pos = -1, ch;
	
	private final List<ExpressionFunction> functions = new ArrayList<>();
	
	{
		// ADD FUNCTIONS. rand(num) is very good, if you try :P
		addFunction(FunctionMath.inst);
	}
	
	public ExpressionEvaluator(String str)
	{
		str = str.replaceAll(Chars.PI + "", "PI");
		str = str.replaceAll("PI", Math.PI + ""); // Include Math.PI into this
		                                          // expression
		str = str.replaceAll("E", Math.E + ""); // Include Math.E (Euler's
		                                        // number) into this expression
		this.str = str;
	}
	
	private void nextChar()
	{
		ch = (++pos < str.length()) ? str.charAt(pos) : -1;
	}
	
	private boolean eat(int charToEat)
	{
		while(ch == ' ')
			nextChar();
		if(ch == charToEat)
		{
			nextChar();
			return true;
		}
		return false;
	}
	
	/**
	 * Parses expression. Uses + - * / ^ % and all functions defined by
	 * {@link #addFunction(ExpressionFunction)}
	 * 
	 * @return parsed double
	 */
	public final double parse()
	{
		pos = -1;
		
		nextChar();
		double x = parseExpression();
		if(pos < str.length())
			throw new RuntimeException("Unexpected: " + (char) ch);
		return x;
	}
	
	// Grammar:
	// expression = term | expression `+` term | expression `-` term
	// term = factor | term `*` factor | term `/` factor
	// factor = `+` factor | `-` factor | `(` expression `)`
	// | number | functionName factor | factor `^` factor
	private double parseExpression()
	{
		double x = parseTerm();
		for(;;)
		{
			if(eat('+'))
				x += parseTerm(); // addition
			else if(eat('-'))
				x -= parseTerm(); // subtraction
			else
				return x;
		}
	}
	
	private double parseTerm()
	{
		double x = parseFactor();
		for(;;)
		{
			if(eat('*'))
				x *= parseFactor(); // multiplication
			else if(eat('/') || eat(':'))
				x /= parseFactor(); // division
			else if(eat('%'))
				x %= parseFactor(); // mod
			else if(eat('^'))
				x = Math.pow(x, parseFactor()); // exponentiation
			else
				return x;
		}
	}
	
	private double parseFactor()
	{
		if(eat('+'))
			return parseFactor(); // unary plus
		if(eat('-'))
			return -parseFactor(); // unary minus
			
		double x;
		int startPos = this.pos;
		if(eat('(')) // parentheses
		{
			x = parseExpression();
			eat(')');
		} else if((ch >= '0' && ch <= '9') || ch == '.') // numbers
		{
			while((ch >= '0' && ch <= '9') || ch == '.')
				nextChar();
			x = Double.parseDouble(str.substring(startPos, this.pos));
		} else if(ch >= 'a' && ch <= 'z') // functions
		{
			while(ch >= 'a' && ch <= 'z')
				nextChar();
			String func = str.substring(startPos, this.pos).toLowerCase();
			x = parseFactor();
			
			boolean funcFound = false;
			for(ExpressionFunction f : functions)
				if(f.accepts(func, x))
				{
					x = f.apply(func, x);
					funcFound = true;
					break;
				}
			
			if(!funcFound)
				throw new RuntimeException("Unknown function: " + func);
		} else
			throw new RuntimeException("Unexpected: " + (char) ch);
		return x;
	}
	
	/**
	 * Adds function to evaluation process.
	 * 
	 * @param func
	 *            The function to add
	 */
	public void addFunction(ExpressionFunction func)
	{
		if(functions.contains(func))
			return; // Don't add duplicates - Bad!
		functions.add(func);
	}
	
	/**
	 * Evaluates expression with optional functions passed. If result is equal
	 * to floor() function, an int will be returned
	 * 
	 * @param expression
	 *            The expression
	 * @param functions
	 *            The function
	 * @return The result as a string
	 */
	public static String evaluate(String expression, ExpressionFunction... functions)
	{
		double result = evaluateDouble(expression, functions);
		if(result == Math.floor(result))
			return ((int) result) + "";
		return result + "";
	}
	
	/**
	 * Evaluates expression with optional functions passed. Returns exact
	 * number, in double
	 * 
	 * @param expression
	 *            The expression
	 * @param functions
	 *            The function
	 * @return The result as a double
	 */
	public static double evaluateDouble(String expression, ExpressionFunction... functions)
	{
		try
		{
			return Double.parseDouble(expression);
		} catch(Throwable err)
		{
		}
		ExpressionEvaluator eval = new ExpressionEvaluator(expression);
		for(ExpressionFunction func : functions)
			eval.addFunction(func);
		return eval.parse();
	}
}