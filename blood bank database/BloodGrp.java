import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;


/*<applet code="BloodGrp" width=600 height=600></applet> */
public class BloodGrp extends Applet implements ItemListener {

    Connection con;
    ResultSet rs;
    Statement st;
    String str = "";
    Choice blood;
    int d;
    char ch;
    float f[] = new float[50], a, b;

    public void init() {
        try
        {
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        con = DriverManager.getConnection("jdbc:odbc:deeksha", "system", "deeksha");
        st = con.createStatement();
        }
        catch(Exception e)
        {
            System.out.println("Error in connection to sql\n");
        }
        blood=new Choice();
	blood.add("O+");
	blood.add("O-");
	blood.add("A+");
	blood.add("A-");
	blood.add("B+");
	blood.add("B-");
	blood.add("AB+");
	blood.add("AB-");
	add(blood);
	blood.addItemListener(this);
    }
    
    public void itemStateChanged(ItemEvent ie) {
        
	str=blood.getSelectedItem();
        if (str == "O+") {
            d = 1;
        }

        else if (str == "O-") {
           d=2;
        }
        else if (str == "A+") {
            d = 3;
        }
        else if (str == "A-") {
            d = 4;
        }
        else if (str == "B+") {
            d = 5;
        }
        else if (str == "B-") {
            d = 6;
        }
        else if (str == "AB+") {
            d = 7;
        }
        else if (str == "AB-") {
            d = 8;
        }
         repaint();
    }

    public void paint(Graphics g) {
       try
       {
        if (d == 1) {
            rs = st.executeQuery("select username,contact from usertab where bloodgrp='O+'");
        }
        if (d == 2) {
            rs = st.executeQuery("select username,contact from usertab where bloodgrp='O-'");
        }
        if (d == 3) {
            rs = st.executeQuery("select username,contact from usertab where bloodgrp='A+'");
        }
        if (d == 4) {
            rs = st.executeQuery("select username,contact from usertab where bloodgrp='A-'");
        }
        if (d == 5) {
            rs = st.executeQuery("select username,contact from usertab where bloodgrp='B+'");
        }
        if (d == 6) {
            rs = st.executeQuery("select username,contact from usertab where bloodgrp='B-'");
        }
        if (d == 7) {
            rs = st.executeQuery("select username,contact from usertab where bloodgrp='AB+'");
        }
        if (d == 8) {
            rs = st.executeQuery("select username,contact from usertab where bloodgrp='AB-'");
        }
        int i=30,j=20;
        g.drawString("name \t contact",j,i-20);
	while(rs.next())
               {
                   g.drawString(rs.getString(1)+"\t",j,i);
                   g.drawString(rs.getDouble(2)+"\t",j+40,i); 
                   i=i+20;
                }
     }
       catch(Exception e)
       {
           System.out.println("Query processing error\n");
       }     
    }
}
