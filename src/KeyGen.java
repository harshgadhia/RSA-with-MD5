package cs480;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

public class KeyGen {
	public static void main(String args[]) {
		KeyGen key = new KeyGen();
		key.generateRSAKey();
	}

	public void generateRSAKey() {

		// Get the current project path.
		final String dir = System.getProperty("user.dir");
		
		// Set Integer bit length as 1024.
		Integer bitLength = 1024;
		Random rand = new Random(); // generate a random number

		// Pick p and q to be random primes of some specified length
		BigInteger p = BigInteger.probablePrime(bitLength, rand);
		BigInteger q = BigInteger.probablePrime(bitLength, rand);

		// Calculating n = p*q;
		BigInteger n = p.multiply(q);

		// ø(n) = ( p-1)x(q-1)
		BigInteger thetaOfN = (p.subtract(BigInteger.ONE)).multiply(q
				.subtract(BigInteger.ONE));

		// Pick e to be a random prime between 1 and ø(n), such that gcd(e,
		// ø(n)) = . e should be similar in (bit) length to p and q, but does
		// not have to be the same length.
		BigInteger e = BigInteger.probablePrime(bitLength / 2, rand);

		while (thetaOfN.gcd(e).compareTo(BigInteger.ONE) > 0
				&& e.compareTo(thetaOfN) < 0) {
			e.add(BigInteger.ONE);
		}

		BigInteger d = BigInteger.ZERO;

		// Calculate d = e-1 mod ø(n) :
		d = e.modInverse(thetaOfN);

		System.out.println("e = " + e + "\n");
		System.out.println("d = " + d + "\n");
		System.out.println("n = " + n + "\n");

		FileWriter filewriter = null;
		BufferedWriter bufferedwriter = null;
		try {
			filewriter = new FileWriter(dir + "/assets/pubkey.rsa");
			bufferedwriter = new BufferedWriter(filewriter);
			bufferedwriter.write("e=" + e);
			bufferedwriter.newLine();
			bufferedwriter.write("n=" + n);
			bufferedwriter.close();
			filewriter.close();

			filewriter = new FileWriter(dir + "/assets/privkey.rsa");
			bufferedwriter = new BufferedWriter(filewriter);
			bufferedwriter.write("d=" + d);
			bufferedwriter.newLine();
			bufferedwriter.write("n=" + n);
			bufferedwriter.close();
			filewriter.close();
			System.out.println("Public (pubkey.rsa) and private (privkey.rsa) Keys for RSA cryptosystem are generated successfully.\n");
		} catch (FileNotFoundException fnf) {
			System.err
					.println("The file you provided, does not exist. Please check the file path and try again.");
			// fnf.printStackTrace();

		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if (null != bufferedwriter)
					bufferedwriter.close();
				if (null != filewriter)
					filewriter.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
