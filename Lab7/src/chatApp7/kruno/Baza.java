package chatApp7.kruno;

import java.sql.*;

import javax.swing.JTextField;


public class Baza {
	 Connection conn = null;
	   
	        	static String url="jdbc:postgresql://localhost:5432/Lab7data";
	    		static String user="postgres";
	    		static String pass="Zg1Pv";
	    
	    public static Connection connect() throws SQLException {
	        return DriverManager.getConnection(url, user, pass);
	    }
	   
	    //==>
		public static void insertMessage(int id,String korisnik, String textField) {
			
			 String SQL = "INSERT INTO public.chatporuke(id,name,history) "
		                + "VALUES(?,?,?)";
		 		
		        try (
		        		Connection conn = connect();
		                PreparedStatement pstmt = conn.prepareStatement(SQL,
		                Statement.RETURN_GENERATED_KEYS)) 
		        {
		        	pstmt.setInt(1, id);
		            pstmt.setString(2, korisnik);
		            pstmt.setString(3, textField);
		 
		            int affectedRows = pstmt.executeUpdate();
		            // check the affected rows 
		            
		        } catch (SQLException ex) {
		            System.out.println(ex.getMessage());
		        }
		       			
		}
		//==>
		public static String showPoruke() {
			String query = "SELECT * FROM public.chatporuke";
			String stringic="";
			try(Connection conn = connect();
					PreparedStatement pst = conn.prepareStatement(query)) 
			{
				boolean isResult = pst.execute();
				
				do {
	                try (ResultSet rs = pst.getResultSet()) {

	                    while (rs.next()) {
	                    
	                      stringic+="["+rs.getString(1)+"]"+rs.getString(2)+": "+rs.getString(3)+"\n";
	                      
	                    }

	                    isResult = pst.getMoreResults();
	                }
	            } while (isResult);

        } catch (SQLException ex) {

           ex.printStackTrace();
          
        }
			return stringic;					
		}

}
