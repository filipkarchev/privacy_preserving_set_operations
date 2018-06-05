package Ciphers;

import java.math.*;
import java.util.*;

public class Paillier {

	private BigInteger p, q; // p and q are two large prime numbers
	private BigInteger lambda; // lambda = lcm(p-1, q-1) = (p-1)*(q-1)/gcd(p-1,* q-1)
	private BigInteger n; // n = p*q
	private BigInteger nsq; // nsq = n*n
	private BigInteger g; // g is generator in Z*_{n^2} where gcd (L(g^lambda mod n^2), n) = 1
	private int bitLength = 2048; // number of bits of modulus

	/**
	 * Constructor for full use of Paillier: encryption and decryption
	 */
	public Paillier() {

		KeyGeneration();
	}
	
	/**
	 * Constructor for full use of Paillier: encryption and decryption
	 * @param bitLength
	 *            number of bits as int
	 */
	public Paillier(int bitLength) {
		this.bitLength = bitLength;
		KeyGeneration();
	}

	/**
	 * Constructor for Paillier only for encryption
	 */
	public Paillier(BigInteger g, BigInteger n) {
		this.g = g;
		this.n = n;
		this.nsq = n.multiply(n);;
	}

	/**
	 * Sets up public and private key pair
	 */
	public void KeyGeneration() {

		// Generate two randomly positive probably prime numbers, with the requested
		// length and certainty to be prime
		p = new BigInteger(bitLength / 2, 66, new Random());
		q = new BigInteger(bitLength / 2, 66, new Random());

		n = p.multiply(q);
		nsq = n.multiply(n);

		// For simplification g = n + 1
		g = n.add(new BigInteger("1"));

		// lambda = (p-1)*(q-1)/gcd(p-1,* q-1)
		lambda = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE))
				.divide(p.subtract(BigInteger.ONE).gcd(q.subtract(BigInteger.ONE)));

		// g should hold gcd (L(g^lambda mod n^2), n) = 1, where L(x) = (x-1)/n
		if (g.modPow(lambda, nsq).subtract(BigInteger.ONE).divide(n).gcd(n).intValue() != 1) {
			System.out.println(
					"There is problem with the generator g. It should hold: gcd (L(g^lambda mod n^2), n) = 1, where L(x) = (x-1)/n");
			// Stop the program
			System.exit(1);
		}
	}

	/**
	 * Encryption of plaintext m, where ciphertext c = g^m * r^n mod n^2. Where r in
	 * a random number
	 * 
	 * @param m
	 *            plaintext as a BigInteger
	 * @return ciphertext as a BigInteger
	 */
	public BigInteger Encryption(BigInteger m) {
		BigInteger r = new BigInteger(bitLength, new Random());
		return g.modPow(m, nsq).multiply(r.modPow(n, nsq)).mod(nsq);

	}

	/**
	 * Decryption of ciphertext c. plaintext m = L(c^lambda mod n^2) * u mod n,
	 * where u = (L(g^lambda mod n^2))^(-1) mod n.
	 * 
	 * @param c
	 *            ciphertext as a BigInteger
	 * @return plaintext as a BigInteger
	 */
	public BigInteger Decryption(BigInteger c) {
		//Check if this instance has the private key
		if(lambda==null)
		{
			System.out.println("You can not decrypt");
			return c;
		}
		BigInteger u = g.modPow(lambda, nsq).subtract(BigInteger.ONE).divide(n).modInverse(n);
		return c.modPow(lambda, nsq).subtract(BigInteger.ONE).divide(n).multiply(u).mod(n);
	}

	public BigInteger getN() {
		return n;
	}

	public void setN(BigInteger n) {
		this.n = n;
	}

	public BigInteger getNsq() {
		return nsq;
	}

	public void setNsq(BigInteger nsq) {
		this.nsq = nsq;
	}

	public BigInteger getG() {
		return g;
	}

	public void setG(BigInteger g) {
		this.g = g;
	}

	public int getBitLength() {
		return bitLength;
	}

	public void setBitLength(int bitLength) {
		this.bitLength = bitLength;
	}
	
	

}
