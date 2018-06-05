package Simulations;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

import Entities.Publisher;
import Entities.Tuple;
import Entities.Sender;

public class SimulationSetUnion {

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		// Input first set
		ArrayList<Integer> P1 = new ArrayList<>(Arrays.asList(9, 32, 17, 45, 98, 23, 54, 234, 87));
		Publisher Alice = new Publisher(P1);

		// Input Second set
		ArrayList<Integer> P2 = new ArrayList<>(Arrays.asList(19, 65, 17, 54, 8, 76, 29, 81, 87, 36, 23));
		Sender Bob = new Sender(P2);

		// STEP 1: Encrypt full set P1
		ArrayList<BigInteger> P1Enc = Alice.encryptSet();

		// Send Paillier public parameters to Bob
		Bob.setEncryptCipher(Alice.getCipher().getG(), Alice.getCipher().getN());

		// STEP 2: Bob Generates Tuples with the encryption for its set
		ArrayList<Tuple> tuples = Bob.generateTuples(P1Enc);
		System.out.println("Tuples generated -> size: " + tuples.size());

		// STEP 3: Alice generates Union set
		ArrayList<Integer> unionSet = Alice.getUnionSet(tuples);

		System.out.println("The union set is: " + Arrays.toString(unionSet.toArray()));
		long endTime = System.currentTimeMillis();
		System.out.println("Total computational time: " + (endTime-startTime) + " mqs");
	}

}
