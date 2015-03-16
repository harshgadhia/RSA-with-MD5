package cs480;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class DigitalSignature {

	public void EncryptFile(String sendFilePath) {

		try {
			String text, plaintext = "";
			BigInteger[] key = new BigInteger[2];

			// reading private keys : starts
			final String dir = System.getProperty("user.dir");
			sendFilePath = dir + "/assets/" + sendFilePath;
			FileReader fr = new FileReader(dir + "/assets/pubkey.rsa");
			BufferedReader br = new BufferedReader(fr);
			for (int i = 0; (text = br.readLine()) != null; i++) {
				key[i] = new BigInteger(text.split("=")[1]);
			}
			BigInteger e = key[0];
			BigInteger n = key[1];

			br.close();
			fr.close();
			System.out.println("e = " + e);
			System.out.println("n = " + n);
			// reading private keys : ends

			// reading plaintext : starts
			fr = new FileReader(sendFilePath);
			br = new BufferedReader(fr);
			do {
				plaintext += br.readLine();
			} while (br.readLine() != null);

			br.close();
			fr.close();

			System.out.println("plaintext = " + plaintext);
			System.out.println("plaintext in bytes = "
					+ bytesToString(plaintext.getBytes()));
			// reading plaintext : ends

			// Encrypting plaintext : starts
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] plaintextBytes = plaintext.getBytes();
			md.update(plaintextBytes);
			byte[] digestArray = md.digest();

			BigInteger digest = new BigInteger(digestArray);
			System.out.println("digest = " + digest);
			System.out.println("digest str = "
					+ bytesToString(digest.toByteArray()));
			BigInteger cipher = digest.modPow(e, n);
			// BigInteger cipher = (new
			// BigInteger(plaintext.getBytes())).modPow(e, n);

			System.out.println("cipher = " + cipher);
			System.out.println("cipher in bytes = "
					+ bytesToString(cipher.toByteArray()));

			// Encrypting plaintext : ends

			// copying cipher into file : starts
			FileWriter fw = new FileWriter(sendFilePath + ".signed");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("" + cipher);
			bw.newLine();
			bw.write(plaintext);
			bw.close();
			fw.close();
			// copying cipher into file : ends

		} catch (FileNotFoundException fnf) {
			System.err
					.println("The file you provided, does not exist. Please check the file path and try again.\n");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

	}

	public void DecrpytFile(String receiveFilePath) {
		try {
			final String dir = System.getProperty("user.dir");
			receiveFilePath = dir + "/assets/" + receiveFilePath;

			String signedBigInt, plainText = "", text = "";
			BigInteger cipherText;
			BigInteger[] key = new BigInteger[2];

			// reading encrypted text : starts
			FileReader fr = new FileReader(receiveFilePath);
			BufferedReader br = new BufferedReader(fr);
			signedBigInt = br.readLine();
			do {
				text = br.readLine();
				if (text != null)
					plainText += text;
			} while (text != null);
			br.close();
			fr.close();

			System.out.println("signedBigInt=" + signedBigInt);
			System.out.println("plainText=" + plainText);
			// reading encrypted text : ends

			// reading public keys : starts
			fr = new FileReader(dir + "/assets/privkey.rsa");
			br = new BufferedReader(fr);
			for (int i = 0; (text = br.readLine()) != null; i++) {
				key[i] = new BigInteger(text.split("=")[1]);
			}
			BigInteger d = key[0];
			BigInteger n = key[1];

			br.close();
			fr.close();

			System.out.println("d=" + d);
			System.out.println("n=" + n);
			// reading public keys : ends

			cipherText = new BigInteger(signedBigInt);
			System.out.println("ciphetText = "
					+ bytesToString(cipherText.toByteArray()));

			BigInteger originalDigest = cipherText.modPow(d, n);
			System.out.println("originalDigest = " + originalDigest);
			System.out.println("originalDigest str = "
					+ bytesToString(originalDigest.toByteArray()));

			// now converting plaintext to 16bytes digest
			// Encrypting plaintext : starts
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] plaintextBytes = plainText.getBytes();
			md.update(plaintextBytes);
			byte[] digestArray = md.digest();

			BigInteger digest = new BigInteger(digestArray);
			System.out.println("digest = " + digest);
			System.out.println("digest str = "
					+ bytesToString(digest.toByteArray()));

			System.out.println("digest.compareTo(originalDigest) = "
					+ digest.compareTo(originalDigest));
			if (digest.compareTo(originalDigest) == 0) {
				System.out
						.println("\nSuccess : Received Message has not been tampered.\n");
			} else {
				System.out
						.println("Alert!! : Received message is tampered.");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public static String bytesToString(byte[] encrypted) {
		String test = "";
		for (byte b : encrypted) {
			test += Byte.toString(b);
		}
		return test;
	}
}
