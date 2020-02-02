package chatApp7.kruno;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Font;

//***=>
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//==> 7.
import java.sql.*;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class ChatKlijent extends JFrame {
	
	/*
	 * 1. Prvo se pokreæe SERVER (SOCKET FILE> u path upisati cmd> Kad se otvori cmd upisati: java SocketServer....
	 * 2. pokrenuti Chat
	 * 3. koristi aplikaciju...kad se pokrene aplikacija trebale biti prikazane cijela povijest razgovora(history)
	 * 
	 * 4. Za LAB_7: imaj otvoren Postgresql kako bi kontrolirao upis podataka u tablicu (koriste se uobièajne sql naredbe); Nužno je imati prethodno 
	 * namještene sve postavke i driver(lib...addJar)
	 * 
	 */
	
	private JPanel contentPane;
	private JTextField textField;
	private JButton buttonPosalji;
	private JTextArea textArea;
	JButton buttonPostavke;
	
	private Socket clientSocket; 
	private PrintWriter pw; 
	private BufferedReader br;
	//***=>lab6
	Logger log =LoggerFactory.getLogger(ChatKlijent.class);
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatKlijent frame = new ChatKlijent();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			
		});
	
				
	   }	

	
	// lab 7
	//Class.forName("org.postgresql.Driver");
	//novo dodano(6lab)
	private void connect(){ 
		try {
	
			//log:
			
			clientSocket = new Socket(UserConfig.getHost(), UserConfig.getPort());
			
			pw = new PrintWriter(clientSocket.getOutputStream());
			br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String response; 
			try {
				
			response = br.readLine(); 
			if (textArea.getText().length()>0)
				textArea.append("\n");
			textArea.append(response); 
			textField.setText(null);
			} catch (IOException e) 
			{ 
				//...=>
				log.error("Greška kod èitanja inicijalnog odgovora", e);
				JOptionPane.showMessageDialog(textArea, "Greška kod èitanja inicijalnog odgovora",
						"Greška!", JOptionPane.ERROR_MESSAGE); 
				}
		} catch (UnknownHostException e)
		{ 
			//...=>
			log.error("Nepoznati host", e); 
			this.dispose();
		} catch (IOException e)
		{ 
			//...=>
			log.error("IO iznimka", e); 
			this.dispose();
		}
		//------------------
		//==>lab7: korištenje Baza.java klase
		//==> LAB7:  PreparedStatement
		//DOHVAT PORUKA IZ BAZE 
		textArea.setText(Baza.showPoruke());
		
		
}
		private void send(){ 
		pw.println(textField.getText()); 
		if (pw.checkError()) {
		JOptionPane.showMessageDialog(textArea, "Greška kod slanja poruke",
		"Greška!", JOptionPane.ERROR_MESSAGE); 
		}
		String response;
		
		try {
		response = br.readLine(); 
		if (textArea.getText().length()>0) 
			textArea.append("\n");
		textArea.append(response);
		textField.setText(null);
	
		} catch (IOException e) { 
					//...=>
					log.error("Greška kod èitanja", e); 
					JOptionPane.showMessageDialog(textArea, "Greška kod èitanja odgovora",
				"Greška!", JOptionPane.ERROR_MESSAGE);
		}
		//...=>
		log.info("Data Sent");
		

	}
	

	/**
	 * Create the frame.
	 */
	public ChatKlijent() {
		setForeground(new Color(204, 204, 204));
		setTitle("CHAT");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setForeground(new Color(0, 0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
	
		
		buttonPosalji = new JButton("Pošalji");
		buttonPosalji.setForeground(new Color(0, 51, 0));
		buttonPosalji.setBackground(new Color(152, 251, 152));
		buttonPosalji.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {				
				
				
				
				String name_txt  = textField.getText();
				
				textField.setText("");
			
				textArea.setText(textArea.getText().trim()+"\n"+UserConfig.getKorisnik()+": "+name_txt);
					
				send();
				//-------------------------------------------------------
				//==>lab7: korištenje Baza.java klase
				//==>PreparedStatement
				//-LAB:7 UNOS PORUKA U BAZU!
				Baza.insertMessage(UserConfig.getPort(),UserConfig.getKorisnik(),name_txt);
				//-------------------
				
			}

			
		});
		contentPane.add(buttonPosalji);
		//--------------------------------------------
		//LAB_7:  dodao velicinu za SCROLL BAR (9,40)
		textArea = new JTextArea(9,40);
		textArea.setBackground(new Color(255, 255, 204));
		textArea.setFont(new Font("Monospaced", Font.BOLD, 15));
		textArea.setForeground(new Color(51, 0, 153));
		sl_contentPane.putConstraint(SpringLayout.NORTH, textArea, 10, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, textArea, 10, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, textArea, -30, SpringLayout.NORTH, buttonPosalji);
		sl_contentPane.putConstraint(SpringLayout.EAST, textArea, -49, SpringLayout.EAST, contentPane);
		textArea.setEditable(false);
		contentPane.add(textArea);
		
		//==>---------------------------------------------------------------
		//==>	LAB_7: Dodan scrollbar na textArea-u kako bi bile vidljive sve poruke
		//SCROLL
		
		JScrollPane scrollBar = new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		contentPane.add(scrollBar);
		
		
		buttonPostavke = new JButton("Postavke");
		buttonPostavke.setForeground(new Color(255, 0, 0));
		buttonPostavke.setBackground(new Color(255, 255, 153));
		sl_contentPane.putConstraint(SpringLayout.SOUTH, buttonPostavke, 0, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.NORTH, buttonPosalji, 0, SpringLayout.NORTH, buttonPostavke);
		sl_contentPane.putConstraint(SpringLayout.EAST, buttonPosalji, -6, SpringLayout.WEST, buttonPostavke);
		sl_contentPane.putConstraint(SpringLayout.EAST, buttonPostavke, -10, SpringLayout.EAST, contentPane);
		
		buttonPostavke.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Postavke dialog = new Postavke();
				dialog.setVisible(true);
				UserConfig.loadParams();
				
			}
		});
		contentPane.add(buttonPostavke);
		
		textField = new JTextField();
		textField.setForeground(new Color(255, 0, 0));
		sl_contentPane.putConstraint(SpringLayout.WEST, textField, 0, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, textField, 0, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, textField, -25, SpringLayout.WEST, buttonPosalji);
		contentPane.add(textField);
		textField.setColumns(10);
		
		
		
		
		connect();
	}
}
