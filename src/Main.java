package cs480;
import java.util.Scanner;

public class Main {

	public static void main(String args[]) {

		Scanner src = new Scanner(System.in);

		try {
			int iterations = 0;
			while (true) {
				if (iterations == 0)
					printHeader();
				iterations++;
				printMenu();
				String choice = src.next();

				try {

					Integer option = Integer.parseInt(choice);

					switch (option) {
					case 1:
						System.out
								.println("Generating public and private keys");
						generateKeys();
						break;

					case 2:
						System.out
								.println("Enter filename/path of file to send (e.g. test.txt)");
						String sendFilePath = src.next();
						DigitalSignature encrypt = new DigitalSignature();
						encrypt.EncryptFile(sendFilePath);
						break;

					case 3:
						String receiveFilePath;
						System.out
								.println("Enter signed filename/path for decryption (e.g. test.txt.signed)");
						do {
							receiveFilePath = src.next();
							if (!receiveFilePath.endsWith(".signed")) {
								System.err
										.println("Filename should have a .signed extension. Please provide a valid filename.\n");
								System.out
										.println("Enter filename/path of encrypted file");
							}

						} while (!receiveFilePath.endsWith(".signed"));
						DigitalSignature decrypt = new DigitalSignature();
						decrypt.DecrpytFile(receiveFilePath);
						break;

					case 4:
						ChangeByte changeByte = new ChangeByte();
						System.out
								.println("Enter signed filename/path that you wish to tamper (e.g. test.txt.signed)");
						String tamperFilePath = src.next();
						System.out
								.println("Enter the byte index that you wish to tamper.");
						Integer indexToTamper = src.nextInt();
						changeByte.tamperMessageAtIndex(tamperFilePath,
								indexToTamper);
						System.out
								.println("Message in the file has been tampered.");
						break;
					case 5:
						System.out
								.println("Thank you for using this system. Good Bye!");
						src.close();
						System.exit(0);
						break;

					default:
						System.err
								.println("\nPlease provide a valid menu choice.");
						break;
					}
				} catch (NumberFormatException e) {
					System.err.println("\nPlease provide a valid menu choice.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void printMenu() {

		System.out.println("|--------------------------------------------|");
		System.out.println("| Menu:                                      |");
		System.out.println("| 1. Generate public and private Keys        |");
		System.out.println("| 2. Encrypt File for Sending                |");
		System.out.println("| 3. Decrypt received file                   |");
		System.out.println("| 4. Tamper byte in file                     |");
		System.out.println("| 5. Terminate Program                       |");
		System.out.println("|____________________________________________|");
		System.out.println("Please select one option from above:");
	}

	public static void printHeader() {
		System.out.println(" --------------------------------------------");
		System.out.println("|      Welcome to CS480 RSA Algorithm        |");
	}

	public static void generateKeys() {
		KeyGen key = new KeyGen();
		key.generateRSAKey();
	}

	public static String bytesToString(byte[] encrypted) {
		String test = "";
		for (byte b : encrypted) {
			test += Byte.toString(b);
		}
		return test;
	}
}
