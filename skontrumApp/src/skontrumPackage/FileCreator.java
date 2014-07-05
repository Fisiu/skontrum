package skontrumPackage;

import java.io.File;
import java.io.PrintWriter;

import javax.swing.JTextArea;

public class FileCreator {

	/*
	 * public void helloNazwa(String nazwa){ JOptionPane.showMessageDialog(null,
	 * "wybrana nazwa pliku to: "+ " "+ nazwa ); }
	 */
	public void tworzPlik(String nazwaPliku) {
		try {
			new PrintWriter(nazwaPliku + ".txt");
		} catch (Exception err) {
			err.printStackTrace();
			System.out.println("oooo");
		}
	};

	public void aktualizujPlik(String nazwaPliku, JTextArea dane) {
		try {
			PrintWriter dataFile = new PrintWriter(nazwaPliku + ".txt");
			dane.write(dataFile);
		} catch (Exception err) {
			err.printStackTrace();
			System.out.println("oooo");
		}
	};

	/**
	 * Sprawdź czy plik o podanej nazwie już istnieje.
	 * 
	 * @param nazwaPliku
	 * @return true, jeśli taki plik już istnieje, w przeciwnym razie false
	 */
	public boolean czyPlikIstnieje(String nazwaPliku) {
		boolean istnieje = false;
		File file = new File(nazwaPliku + ".txt");
		if (file.exists() && !file.isDirectory()) {
			istnieje = true;
		}
		return istnieje;
	}

}
