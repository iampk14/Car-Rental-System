package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import connection.DBconnect;
import dao.Cust;
import dao.Customers;
import dao.Vehicle;
import dao.Vehiclegs;

public class MainCode {
	private static Logger logger = LogManager.getLogger(MainCode.class.getName());
	public static void main(String[] args) throws SQLException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		boolean f=true;
		
		while(f){
			  DBconnect conn =new DBconnect("jdbc:mysql://localhost:3306/carrental","root","Omkar@1234");
			  Connection con=conn.getConn();
			  Scanner sc = new Scanner(System.in);
			  BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
			  String s="yyyy-MM-dd";
			  logger.info("\t**************************************************************************************************\n");
			  logger.info("\t*                                        Rent Car                       : 1                      *\n");
			  logger.info("\t*                                        Return Car                     : 2                      *\n");
			  logger.info("\t*                                        Registration                   : 3                      *\n");
			  logger.info("\t*                                        exit                           : 4                      *\n");
			  logger.info("\t**************************************************************************************************\n");
			  logger.info("Enter your choice: ");
			  int choice =sc.nextInt();
				switch (choice) {
				case 1:
					Vehiclegs v =new Vehiclegs();
					String regno="";
					while(true) {
					Statement st =con.createStatement();
					ResultSet rs=st.executeQuery("Select * from vehicle Where status='Available'");
					logger.info("Reg No. \t\t" + "Brand\t\t" + "Model_name\t"+"status\t\t"+"Price"  );
					logger.info("*****************************************************************************************\n");
					while(rs.next()) {
						logger.info("{}\t\t{}\t\t{}\t\t{}\t\t{}",rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getInt(5));
						
					}
					logger.info("Enter Which car want to book(Use Reg No. ) :");
					regno = sc.next();
					PreparedStatement psregno = con.prepareStatement("select status from vehicle where registrationno = ?");
					psregno.setString(1, regno);
					ResultSet r2=psregno.executeQuery();
			     	if(r2.next()) {
						if(r2.getString(1).equals("Available")){
							logger.info("Available");
							v.setregistrationno(regno);
							break;
						}
					}else {
						logger.info("*****************************");
						logger.info("Not Available choose diffrent");
						logger.info("*****************************");
					}
					}
					logger.info("Enter customerID(phonenumber) : ");
					String custID = sc.next();
					PreparedStatement ps = con.prepareStatement("select * from customer where customerID = ?");
					ps.setString(1, custID);
					ResultSet r1=ps.executeQuery();
					if(r1.next()) {
						String rentDate;
						String returnDate;
						LocalDate date11;
						LocalDate date22;
						
						long fees=1;
						
						while(true) {
							logger.info("Enter Rent date(yyyy-mm-dd) :");
							rentDate =sc.next();	
							DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(s);
							try
							{
								date11 = LocalDate.parse(rentDate,dateTimeFormatter);
							   break;
							}
							catch(Exception e)
							{
								logger.info("Enter Valid Date:");
							}
						}
						while(true) {
							logger.info("Enter Return date(yyyy-mm-dd) :");
							returnDate =sc.next();	
							DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(s);
							try
							{
								date22 = LocalDate.parse(returnDate,dateTimeFormatter);
							   long days = ChronoUnit.DAYS.between(date11, date22);
							   if(days> 0) {
								   fees =days;
							   break;
							   }else logger.info("Enter Valid Date :");
							   
							}
							catch(Exception e)
							{
								logger.info("Enter Valid Date  :");
							}
						}
						
						v.setRentDate(rentDate);					
						v.setReturnDate(returnDate);
						
						PreparedStatement psregno1 = con.prepareStatement("select Price from vehicle where registrationno = ?");
						psregno1.setString(1, regno);
						ResultSet r2=psregno1.executeQuery();
						if(r2.next()) v.setFees(fees * r2.getDouble(1));
						v.setCustomerID(custID);
						Statement stmt=con.createStatement();

						stmt.executeUpdate("update vehicle set status='Booked' where registrationno='"+regno+"'");
					}else {
						logger.info("*********************");
						logger.info("Please Register first");
						logger.info("*********************");
						break;
					}
					logger.info("Booked Successfully");
					Vehicle v1 =new Vehicle(conn.getConn());
					v1.showvehicle(v);
					break;
					
				case 2:
					Statement stmt=con.createStatement();
					String regnoreturn;
					while(true) {
						logger.info("Enter register No. : ");
						regnoreturn = sc.next();
						ResultSet rs9 =stmt.executeQuery("Select registrationno from booking Where registrationno='"+regnoreturn+"'");
						
						if(rs9.next()) { 
							break;
						}else logger.info("Enter Valid register No. ");
					}
					
					String areturnDate = null;
					
					DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(s);
					ResultSet rs4 =stmt.executeQuery("Select returnDate from booking Where registrationno='"+regnoreturn+"'");
					while(rs4.next()) { 
						areturnDate =rs4.getString(1);
					}
					
					LocalDate date1 = LocalDate.now();
					
					LocalDate date2 = LocalDate.parse(areturnDate,dateTimeFormatter);
					
				    long days = ChronoUnit.DAYS.between(date1, date2);
					stmt.executeUpdate("update vehicle set status='Available' where registrationno='"+regnoreturn+"'");
					ResultSet rs3 =stmt.executeQuery("Select fees from booking Where registrationno='"+regnoreturn+"'");
					
					while(rs3.next()) { 
						logger.info("Actual Fees : {}",(rs3.getDouble(1)));
						logger.info("Penalty Fees : {}",(-1*0.1*rs3.getDouble(1)*days));
						logger.info("Total Fees : {}",(rs3.getDouble(1)-(0.1*rs3.getDouble(1)*days)));
					}
					stmt.executeUpdate("delete from booking where registrationno='"+regnoreturn+"'");
					
					logger.info("Successfuly return car");
					break;
				case 3:
					Statement stmt1=con.createStatement();
					Matcher matcher;
					String regexEmail = "^(.+)@(.+)$"; 
					Pattern pattern = Pattern.compile(regexEmail);  
					Pattern p = Pattern.compile("^[6-9]{1}\\d{9}$");
					Cust c =new Cust();
					logger.info("Enter Username : ");
					String uname = r.readLine();
					c.setCustomerName(uname);
					
					logger.info("Enter Address : ");
					String address = r.readLine();
					c.setAddress(address);
					
					
					while(true) {
					logger.info("Enter Contact no : ");
					String conno = r.readLine();
					matcher = p.matcher(conno);
					
					if(matcher.matches()) {
						c.setPhone(conno);
						c.setCustomerID(conno);
						ResultSet rs8 =stmt1.executeQuery("Select * from customer Where mobileno='"+conno+"'");
						if(rs8.next()) { 
							logger.info("***********************************************************");
							logger.info(" *	Customer is already register with this number	* ");
							logger.info("***********************************************************");
						}else{
							break;
						}
					}else logger.info("Input Valid Contact Id : ");
					
					}
					while(true) {
					logger.info("Enter Email : ");
					String email = r.readLine();
					matcher = pattern.matcher(email);
					if(matcher.matches()) {c.setEmail(email);break;
					}else logger.info("Input Valid Email Id : ");
					} 
					
					logger.info("Successfully Registered");
					logger.info("________________________Thank You________________________");
					
					Customers cust =new Customers(conn.getConn());
					cust.dataInsert(c);
					break;
				case 4:
					logger.info("** Thank You For Visit **");
					System.exit(0);
					
					break;
				default:
					logger.info("** PLEASE CHOOSE AN APPROPRIATE OPTION **");
					break;
				}
				
		}

	}

}
