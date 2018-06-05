package Ciphers;

import java.math.BigInteger;

public class Utils {

	/**
	 * Use Vieta's Theorem to calculate polynomial coefficient of given position
	 * @param roots as Integer[]
	 * 			all roots of the polynomial
	 * @param M as int
	 * 			the power of the unknown x, at which we want to get the coefficient 
	 * */
	public static BigInteger sumOfCombMult(Integer[] roots, int M)
	{
	    if (roots.length < M)
	    {
	        throw new IllegalArgumentException("size of roots cannot be smaller than M");
	    }

	    BigInteger[] R = new BigInteger[roots.length];

	    for (int i = 0; i < roots.length; i++)
	    {
	        R[i] = BigInteger.valueOf(roots[i]);
	    }

	    BigInteger[] coeffs = new BigInteger[roots.length + 1];

	    coeffs[0] = BigInteger.valueOf(roots[0]);

	    int lb = 0;

	    for (int i = 1; i < roots.length; i++)
	    {
	        lb = Math.max(i - M, 0);

	        coeffs[i] = R[i].add(coeffs[i - 1]);

	        for (int j = i - 1; j > lb; j--)
	        {
	            coeffs[j] = R[i].multiply(coeffs[j]).add(coeffs[j - 1]);

	        }

	        if (lb == 0)
	        {
	            coeffs[0] = coeffs[0].multiply(R[i]);
	        }
	    }
	    
	    if(M>0 && M%2==1)
        {
        	//Add the (-1) for odd positions
        	 coeffs[roots.length - M] =  coeffs[roots.length - M].negate();
        }

	    return coeffs[roots.length - M];
	}
	
}
