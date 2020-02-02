package chatApp7.kruno;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;



public class UserConfig  extends ChatKlijent{
	private static final String propertiesFile = "chat.properties"; 
	private static final String hostPropertieName = "host"; 
	private static final String portPropertieName = "port"; 
	private static final String userPropertieName = "user";
	
	private static String host;
	private static int port;
	private static String korisnik;
	
	public static String getHost() {
		return host;
	}
	public static void setHost(String host) {
		UserConfig.host = host;
	}
	public static int getPort() {
		return port;
	}
	public static void setPort(int port) {
		UserConfig.port = port;
	}
	public static String getKorisnik() {
		return korisnik;
	}
	public static void setKorisnik(String korisnik) {
		UserConfig.korisnik = korisnik;
	}
	static {
		loadParams();
	}
	
	@SuppressWarnings("deprecation")
	
	public static void loadParams() { 
		Properties props = new Properties(); 
		InputStream is = null; 
		// Najprije pokušavamo uèitati iz lokalnog direktorija 
		// 
		try {
			File f = new File(propertiesFile); 
			is = new FileInputStream(f);
		} 
		catch (Exception e) 
		{ 
		e.printStackTrace(); 
		is = null;
		}
	 
	try {
	// pokušavaju se uèitati parametri
		props.load(is);
	} catch (Exception e) {
		System.out.println("Prvi parametar: ");
	}
	// prvi parametar: naziv postavke 
	// drugi parametar: ako nije naðena vrijednost onda se vraæa drugi 
	// parametar
	host = props.getProperty(hostPropertieName, "192.168.0.2");
	port = new Integer(props.getProperty(portPropertieName, "8080"));
	korisnik = props.getProperty(userPropertieName, "Krunoslav");
	}
	
		
	public static void saveParamChanges() { 
		try {
			Properties props = new Properties(); 
			
			props.setProperty(hostPropertieName, host); 
			
			props.setProperty(portPropertieName, "" + port);
			
			props.setProperty(userPropertieName, korisnik);
			File f = new File(propertiesFile);
			OutputStream out = new FileOutputStream(f); 
			props.store(out, "Opcionalni header komentar");
	} 
		catch (Exception e) {
			e.printStackTrace();
	} 
		
	}
}
