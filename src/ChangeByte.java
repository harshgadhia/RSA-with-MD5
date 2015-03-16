package cs480;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ChangeByte {

	public void tamperMessageAtIndex(String filepathToTamper, Integer indexToTamper) {

		FileInputStream fileInputStream = null;
		final String dir = System.getProperty("user.dir");
		filepathToTamper = dir + "/assets/" + filepathToTamper;
		File file = new File(filepathToTamper);
		byte[] bytes = new byte[(int) file.length()];

		try {
			// convert file into array of bytes
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(bytes);
			fileInputStream.close();

			for (int i = 0; i < bytes.length; i++) {
				if (i == indexToTamper) {
					bytes[i] = (byte) i;
				}
			}

			// convert array of bytes into file
			FileOutputStream fileOuputStream = new FileOutputStream(
					filepathToTamper);
			fileOuputStream.write(bytes);
			fileOuputStream.close();

		} 
		catch (FileNotFoundException e) {
			System.err
			.println("The file you provided, does not exist. Please check the file path and try again.\n");
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}
