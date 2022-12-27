package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;



public class Customers {
	private Connection conn;
	public Customers(Connection conn) {
		super();
		this.conn = conn;
	}
	public boolean dataInsert(Cust cust) {
		boolean f =false;
		try {
			PreparedStatement ps =conn.prepareStatement("insert into customer values(?,?,?,?,?)");
			ps.setString(1,cust.getCustomerID());
			ps.setString(2,cust.getCustomerName());
			ps.setString(5,cust.getAddress());
			ps.setString(3,cust.getPhone());
			ps.setString(4,cust.getEmail());
			

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
