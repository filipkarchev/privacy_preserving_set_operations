package Simulations;
import java.math.BigInteger;
import java.util.Scanner;

import Ciphers.Paillier;

public class SimulationPaillier {
	
	@SuppressWarnings("resource")
	public static void main(String[] str) {

		Paillier paillier = new Paillier();

		// Get the message
		System.out.println("Enter your message:");
		String messageStr = new Scanner(System.in).nextLine();
		BigInteger message = new BigInteger(messageStr.getBytes());

		// Encryption
		BigInteger encryption = paillier.Encryption(message);
		System.out.println("Encrypted text: " + encryption);

		// Decryption
		BigInteger decrypted = paillier.Decryption(encryption);
		byte[] array = decrypted.toByteArray(); // Convert BigInteger to Byte array
		if (array[0] == 0) {
			byte[] tmp = new byte[array.length - 1];
			System.arraycopy(array, 1, tmp, 0, tmp.length);
			array = tmp;
		}

		System.out.println("Decrypted text: "+new String(array));

	}
}
