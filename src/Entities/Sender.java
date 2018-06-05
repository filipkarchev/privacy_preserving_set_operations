package Entities;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import Ciphers.Paillier;

public class Sender extends Participant {

	public Sender(ArrayList<Integer> set) {
		super(set);
	}

	public void setEncryptCipher(BigInteger g, BigInteger n) {
		this.cipher = new Paillier(g, n);
	}

	/**
	 * VALID FOR SETS INTERSECTION. 
	 * The sender generates the proper encryption tuples and sends them back to the publisher
	 * 
	 * @param p1Enc
	 *            as ArrayList<BigInteger> List of the encrypted polynomial
	 *            coefficients
	 */
	public ArrayList<BigInteger> generateIntersectionEncryptions(ArrayList<BigInteger> p1Enc) {
		ArrayList<BigInteger> encryptionsList = new ArrayList<BigInteger>();
		System.out.println("Generating Intersection Encryptions");
		for (int i = 0; i < set.size(); i++) {
			// For every s from P2, Calculate tuple (Epk(fs1(s)*s*r), Epk(fs1(s)*r))
			int s = set.get(i);

			// Evaluate for this item of the set
			BigInteger encf1_s = evaluateEncryptedPolynomial(s, p1Enc);

			// Generate random value r,in the paper it is not specified in what range
			BigInteger r = new BigInteger(getCipher().getBitLength(), new Random());

			// calculate encf1_s on pow r
			BigInteger ranodmisedEncryption = encf1_s.modPow(r, getCipher().getNsq());

			// Encrypt the item s with the public key of the cipher
			BigInteger encryptedItem = getCipher().Encryption(BigInteger.valueOf(s));

			// Use the additional homomorphic property of the cipher Enc(a+b) =
			// Enc(a)*Enc(b)
			BigInteger finalEncryption = ranodmisedEncryption.multiply(encryptedItem).mod(getCipher().getNsq());
			// Add in the list
			encryptionsList.add(finalEncryption);

		}

		// Make Permutation on the elements
		Collections.shuffle(encryptionsList);

		return encryptionsList;
	}

	/**
	 * VALID FOR SETS UNION. 
	 * The sender generates the proper encryption tuples and sends them back to the publisher
	 * 
	 * @param p1Enc
	 *            List of the encrypted polynomial coefficients
	 */
	public ArrayList<Tuple> generateTuples(ArrayList<BigInteger> p1Enc) {

		ArrayList<Tuple> tuplesSet = new ArrayList<Tuple>();
		System.out.println("Generating Typles");
		for (int i = 0; i < set.size(); i++) {
			// For every s from P2, Calculate Epk(fs1(s)*r + s)
			int s = set.get(i);

			// Evaluate for this item of the set
			BigInteger encf1_s = evaluateEncryptedPolynomial(s, p1Enc);

			// Generate random value r,in the paper it is not specified in what range
			BigInteger r = new BigInteger(getCipher().getBitLength(), new Random());

			// calculate encf1_s on pow (s*r)
			BigInteger first = encf1_s.modPow(r.multiply(BigInteger.valueOf(s)).mod(getCipher().getNsq()),
					getCipher().getNsq());
			// calculate encf1_s on pow r
			BigInteger second = encf1_s.modPow(r, getCipher().getNsq());
			// Generate the tuple
			Tuple secretTuple = new Tuple(first, second);
			// Add in the list
			tuplesSet.add(secretTuple);

		}

		// Make Permutation on the elements
		Collections.shuffle(tuplesSet);

		return tuplesSet;
	}

	public Paillier getCipher() {
		return cipher;
	}

	/**
	 * Used to get the evaluated encryption Enc(fs1(s))
	 * 
	 * @param s
	 *            as int number from the second set
	 * @param p1Enc
	 *            as ArrayList<BigInteger> List of the encrypted polynomial
	 *            coefficients
	 */
	private BigInteger evaluateEncryptedPolynomial(int s, ArrayList<BigInteger> p1Enc) {
		// We are going to multiply => 1
		BigInteger encf1_s = BigInteger.valueOf(1);
		for (int n = 0; n < p1Enc.size(); n++) {
			// Evaluate the polynomial for s, by summing all encryptions of the polynomial
			// encryptions
			// fs1(s) = (s^n).a + (s^(n-1)).b+...+z, where a,b,...z are the encryption of
			// the polynomial coefficents

			BigInteger plaintext = BigInteger.valueOf(s).modPow(BigInteger.valueOf(p1Enc.size() - n - 1),
					getCipher().getN());
			// ex: plaintext = 3 => Enc(3b) = Enc(b+b+b) = Enc(b).Enc(b).Enc(b) = Enc(b)^3

			BigInteger encryptionOfCoeff = p1Enc.get(n).modPow(plaintext, getCipher().getNsq());

			// Replace addition from the cipher text to multiplication of the plaintext
			// ex: s = 3 => Enc(fs1(s)) = Enc(a)*Enc(b)^3*Enc(c)^9*...Enc(z)^{3^n}
			encf1_s = encf1_s.multiply(encryptionOfCoeff).mod(getCipher().getNsq());
		}

		return encf1_s;
	}
}
