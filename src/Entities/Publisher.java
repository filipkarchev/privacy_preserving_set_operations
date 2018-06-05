package Entities;
import java.math.BigInteger;
import java.util.ArrayList;
import Ciphers.Paillier;
import Ciphers.Utils;

public class Publisher extends Participant {
	
	public Publisher(ArrayList<Integer> set) {
		super(set);
		
		generateCipher();
	}

	/**
	 * Get polynomial coefficients of the local set and encrypt them
	 * @return list of encryptions 
	 * 
	 * */
	public ArrayList<BigInteger> encryptSet() {
		System.out.println("Computing polynomial coefficients");
		ArrayList<BigInteger> cipherSet = new ArrayList<BigInteger>();

		//WE are using Vieta's theorem to calculate the coefficients of the polynomial
		for(int i = 0; i<= getSet().size();i++)
		{
			BigInteger coef = Utils.sumOfCombMult(getSet().toArray(new Integer[getSet().size()]), i);
			if(coef==null)
			{
				coef = BigInteger.valueOf(1);
			}
			
			cipherSet.add(getCipher().Encryption(coef));
		}
		
		return cipherSet;
	}
	
	public Paillier getCipher() {
		return cipher;
	}

	private void generateCipher() {
		cipher = new Paillier();
	}
	
	/**
	 * VALID FOR SETS UNION
	 * Get Union set from the received tuples of encryptions
	 * @param tuples as ArrayList<Tuple>
	 * 			list of Tuple objects containing the encryptions, generated from the Sender
	 * 
	 * */
	public ArrayList<Integer> getUnionSet(ArrayList<Tuple> tuples)
	{
		ArrayList<Integer> unionSet = new ArrayList<Integer>();
		unionSet.addAll(this.set);
		
		for(Tuple tuple : tuples)
		{
			BigInteger first = cipher.Decryption(tuple.getFirst());
			BigInteger second = cipher.Decryption(tuple.getSecond());
			
			//If the decryption of the tuples are zeros, then this element is already in the union set
			if(first.compareTo(BigInteger.valueOf(0))==0 && second.compareTo(BigInteger.valueOf(0))==0)
			{
				continue;
			}
			
			//This is new element, add it to the union set
			try {
				BigInteger y_inverse = second.modInverse(getCipher().getN());
				//System.out.println("value is: "+ first.multiply(y_inverse).mod(getCipher().getN()));
				unionSet.add((first.multiply(y_inverse)).mod(getCipher().getN()).intValue());
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		//The union set is ready
		return unionSet;
	}
	
	/**
	 * VALID FOR SETS INTERSECTION 
	 * Get Intersection set from the received encryptions
	 * @param encryptionsList as ArrayList<BigInteger>
	 * 			list of encryptions generated from the Sender
	 * 
	 * */
	public ArrayList<Integer> getIntersectionSet(ArrayList<BigInteger> encryptionsList)
	{
		System.out.println("Decrypting item encryptions");
		ArrayList<Integer> intersectionSet = new ArrayList<Integer>();
		
		for(BigInteger encryption : encryptionsList)
		{
			BigInteger setItem = cipher.Decryption(encryption);
			
			//If the decryption of the item is contained in the first set, then this element is presented in part of the intersection
			if(getSet().contains(setItem.intValue())==true)
			{
				intersectionSet.add(setItem.intValue());
			}
		}
		
		//The intersection set is ready
		return intersectionSet;
	}
}
