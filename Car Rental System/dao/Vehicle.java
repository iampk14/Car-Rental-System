package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;




public class Vehicle {
	private Connection conn;
	public Vehicle(Connection conn) {
		super();
		this.conn = conn;
	}
	public boolean showvehicle(Vehiclegs v)  {
		
		boolean f =false;
		try {
			PreparedStatement ps =conn.prepareStatement("insert into booking values(?,?,?,?,?)");
			ps.setString(1,v.getCustomerID());
			ps.setString(2,v.getRentDate());
			ps.setString(3,v.getReturnDate());
			ps.setString(4,v.getregistrationno());
			ps.setDouble(5, v.getFees());
			

			int a = ps.executeUpdate();
			if(a==1) {
				f=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}
	
}
