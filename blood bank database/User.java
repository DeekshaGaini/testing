import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author rangaraju's
 */
public class User
{
    public static void main(String[] args)
    {
        try {
                Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
           
                Connection con = DriverManager.getConnection("jdbc:odbc:deeksha","system","deeksha");
           
                Statement st=con.createStatement();
                BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
         
                
                System.out.println("enter user id");
                int userid=Integer.parseInt(br.readLine());
           
                System.out.println("enter user name");
                String username =br.readLine();

                System.out.println("enter age");
                int age =Integer.parseInt(br.readLine());

                System.out.println("enter blood group");
                String bloodgrp =br.readLine();

                System.out.println("enter contact");
                double contact = Double.parseDouble(br.readLine());

                System.out.println("enter systolic pressure");
                int syspre =Integer.parseInt(br.readLine());

                System.out.println("enter diastolic pressure");
                int diapre =Integer.parseInt(br.readLine());

                System.out.println("enter temperature");
                float temp =Float.parseFloat(br.readLine());  
                
                if(((syspre==120)&&(diapre==80))&&(temp==98.6f))
                 {         
                   int p;
                   p = st.executeUpdate("insert into usertab values ("+userid+",'"+username+"',"+age+",'"+bloodgrp+"',"+contact+")");
                   
                   if((p!=0))
                   {
                     System.out.println("record inserted");
                   }
                   else
                   {
                     System.out.println("record not inserted");
                   }
                }
		else
		 System.out.println("not eligible to donate blood");
            }
          catch(ClassNotFoundException | SQLException | IOException | NumberFormatException e)   
          {
            System.out.println(e);
          }
        
   }
}