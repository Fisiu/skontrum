package skontrumPackage;

/*SkontrumApp is an application wrote for 
 * Municical Public Library in Oświęcim 
 * for book inventarisation. At first, librarians 
 * used windows notepad for "reading" book codes,
 * minding for errors, saving file from time to time
 * and then manually copying all .txt files from notebooks 
 * to NAS server.
 * This app saves librarians work every couple of codes, 
 * on new file creation and on finishing work
 * (by that i mean pushing "send" button)
 * and automatically sends data to a NAS server.
 * It's not perfect - that's why it's on github,
 * where it can be polished:)*/

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

public class SkontrumHelper extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	JPanel panelPrzyciskow;
	JPanel panelKodow;

	JButton przyciskNowy;
	JButton przyciskWyslij;

	JTextArea kody;
	JScrollPane lista;

	String nazwaPliku;
	static String userLogin;
	String fullName;
	File plik = new File("");

	final FileCreator plikoKreator = new FileCreator();
	final FileSender plikoWysylacz = new FileSender();

	Boolean czyPierwszy = true;
	static String longWelcomeText = new String(
			"Cześć!\nNajpierw kliknij 'Nowy plik', żeby stworzyć plik, do którego zapiszesz kody.\nWpisz jakąkolwiek nazwę, wciśnij 'OK', kursor automatycznie wskoczy na pole do skanowania.\nPo skończeniu całego Skontrum wciśnij przycisk 'Wyślij dane', aby wysłać zeskanowane kody na serwer.\nPo paru(~5) sekundach otrzymasz informację o zakończeniu operacji.\nMożesz wyłączyć aplikację krzyżykiem w prawym górnym rogu.");

	public SkontrumHelper() {
		super(new BorderLayout());
		// GUI creation
		panelPrzyciskow = new JPanel();
		panelKodow = new JPanel();

		przyciskNowy = new JButton("Nowy plik");
		przyciskWyslij = new JButton("Wyślij dane");

		kody = new JTextArea("");
		kody.getDocument().addDocumentListener(new podsluchiwacz());
		kody.setEnabled(false);
		lista = new JScrollPane(kody, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		panelPrzyciskow.setLayout(new BorderLayout());
		panelPrzyciskow.add(przyciskNowy, BorderLayout.NORTH);
		panelPrzyciskow.add(przyciskWyslij, BorderLayout.SOUTH);

		panelKodow.add(lista);
		add(panelPrzyciskow, BorderLayout.NORTH);
		add(lista);

		ActionListener nowyListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (czyPierwszy == false) {
					plikoKreator.aktualizujPlik(fullName, kody);
					kody.setText("");
				}
				if (e.getSource() == przyciskNowy) {
					nazwaPliku = JOptionPane.showInputDialog("Input filename");
					fullName = (userLogin + "_" + "sko" + "_" + nazwaPliku);
					System.out.println(fullName);
					if (!plikoKreator.czyPlikIstnieje(fullName)) {
						plikoKreator.tworzPlik(fullName);
						if (kody.isEnabled() == (false))
							kody.setEnabled(true);
						czyPierwszy = false;
						kody.requestFocusInWindow();
					} else {
						// file exists, notify user
						JOptionPane.showMessageDialog(getParent(),
								"Plik o takiej nazwie już istnieje.\nUtwórz plik z inną nazwą.",
								"Plik istnieje",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		};

		ActionListener WyslijListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				plikoKreator.aktualizujPlik(fullName, kody);
				System.out.println(fullName);
				if (e.getSource() == przyciskWyslij)
					plikoWysylacz.TimeToBuildAndATimeToDestroy();
			}
		};
		przyciskWyslij.addActionListener(WyslijListener);
		przyciskNowy.addActionListener(nowyListener);
	}

	class podsluchiwacz implements DocumentListener {
		final String newline = "\n";

		public void insertUpdate(DocumentEvent e) {
			updateLog(e, "inserted into");
		}

		public void removeUpdate(DocumentEvent e) {
			updateLog(e, "removed from");
		}

		public void changedUpdate(DocumentEvent e) {
		}

		public void updateLog(DocumentEvent e, String action) {
			Document doc = (Document) e.getDocument();
			dorzucEnter(doc);
		}
	}

	/** Handle button click. */
	public void actionPerformed(ActionEvent e) {
	}

	public void dorzucEnter(final Document dok) {
		Runnable dorzucEnt = new Runnable() {
			@Override
			public void run() {
				int counter = dok.getLength() + 1;
				if (counter % 9 == 0)
					kody.append("\n");
				if (counter % 135 == 0)
					plikoKreator.aktualizujPlik(fullName, kody);
				// System.out.println("juz znakow: "+counter+"  oraz "+kody.getLineCount());
			}
		};
		SwingUtilities.invokeLater(dorzucEnt);
	}

	public boolean keyPressed(KeyEvent eKey) {
		boolean czyEsc = false;
		if (eKey.getKeyCode() == KeyEvent.VK_F5)
			czyEsc = true;
		return czyEsc;
	}

	private static void createAndShowGUI() {

		JFrame ramka = new JFrame("SkontrumHelper");
		ramka.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension wymiar = Toolkit.getDefaultToolkit().getScreenSize();
		int wymiarX = (int) wymiar.getWidth();
		int wymiarY = (int) wymiar.getHeight();

		ramka.setBounds(wymiarX / 3, wymiarY / 3, wymiarX / 9 + 25,
				(wymiarY / 3) + 10);

		JComponent zawartosc = new SkontrumHelper();
		zawartosc.setOpaque(true);

		ramka.setContentPane(zawartosc);
		ramka.setResizable(false);
		ramka.setVisible(true);
		JOptionPane.showMessageDialog(ramka, longWelcomeText);
		userLogin = JOptionPane
				.showInputDialog("Input your Windows login, eg \n adamz");
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}