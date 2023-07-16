package it.polito.tdp.gosales.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.gosales.model.DailySale;
import it.polito.tdp.gosales.model.Products;
import it.polito.tdp.gosales.model.Retailers;
import it.polito.tdp.gosales.model.Adiacenza;

public class GOsalesDAO {
	
	
	/**
	 * Metodo per leggere la lista di tutti i rivenditori dal database
	 * @return
	 */
	
	
	public List<String> getAllCauntries(){
		String query = "SELECT DISTINCT country from go_retailers";
		List<String> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(rs.getString("Country"));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}
	
	

	public List<Retailers> getAllRetailers(){
		String query = "SELECT * from go_retailers";
		List<Retailers> result = new ArrayList<Retailers>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Retailers(rs.getInt("Retailer_code"), 
						rs.getString("Retailer_name"),
						rs.getString("Type"), 
						rs.getString("Country")));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}
	
	
	
	
	
	public void getAllRetailerPerMappa(Map<Integer,Retailers> idMap){
		String query = "SELECT * from go_retailers";
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				if(! idMap.containsKey(rs.getInt("Retailer_code"))) {
				Retailers r = new Retailers(rs.getInt("Retailer_code"), 
						rs.getString("Retailer_name"),
						rs.getString("Type"), 
						rs.getString("Country"));
				idMap.put(rs.getInt("Retailer_code"), r);
				
				}
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}
	
	
	
	/**
	 * Metodo per leggere la lista di tutti i prodotti dal database
	 * @return
	 */
	public List<Products> getAllProducts(){
		String query = "SELECT * from go_products";
		List<Products> result = new ArrayList<Products>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Products(rs.getInt("Product_number"), 
						rs.getString("Product_line"), 
						rs.getString("Product_type"), 
						rs.getString("Product"), 
						rs.getString("Product_brand"), 
						rs.getString("Product_color"),
						rs.getDouble("Unit_cost"), 
						rs.getDouble("Unit_price")));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}

	
	/**
	 * Metodo per leggere la lista di tutte le vendite nel database
	 * @return
	 */
	public List<DailySale> getAllSales(){
		String query = "SELECT * from go_daily_sales";
		List<DailySale> result = new ArrayList<DailySale>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new DailySale(rs.getInt("retailer_code"),
				rs.getInt("product_number"),
				rs.getInt("order_method_code"),
				rs.getTimestamp("date").toLocalDateTime().toLocalDate(),
				rs.getInt("quantity"),
				rs.getDouble("unit_price"),
				rs.getDouble("unit_sale_price")  ));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}



	public List<Retailers> creaVertici(String nazione) {
		String query = "SELECT * "
				+ "FROM go_retailers  "
				+ "WHERE country=? ";
		
		List<Retailers> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, nazione);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Retailers(rs.getInt("Retailer_code"), 
						rs.getString("Retailer_name"),
						rs.getString("Type"), 
						rs.getString("Country")));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}



	public List<Adiacenza> creaArchi(String nazione, int anno, int m,Map<Integer,Retailers> idMap) {
		String query="SELECT r1.Retailer_code AS rc1, r2.Retailer_code AS rc2, COUNT(DISTINCT ds1.Product_number) AS peso "
				+ "FROM go_retailers r1, go_retailers r2, go_daily_sales ds1, go_daily_sales ds2 "
				+ "WHERE r1.Country=? AND r1.Country=r2.Country "
				+ "   AND r1.Retailer_code=ds1.Retailer_code  "
				+ "	AND r2.Retailer_code=ds2.Retailer_code "
				+ "   AND ds1.Product_number=ds2.Product_number "
				+ "   AND r1.Retailer_code > r2.Retailer_code "
				+ "	AND YEAR(ds1.Date)=? AND YEAR(ds1.Date)=YEAR(ds2.Date) "
				+ "GROUP BY r1.Retailer_code, r2.Retailer_code "
				+ "HAVING peso>=? ";
		
		/*
		 * Altro esempio della stessa query ma con il comando IN
		 */
//		String query2 = "SELECT s1.Retailer_code, s2.Retailer_code, COUNT(DISTINCT s1.Product_number) as N "
//				+ "FROM go_daily_sales s1, go_daily_sales s2 "
//				+ "WHERE s1.Product_number = s2.Product_number AND "
//				+ "YEAR(s1.Date) = 2017 AND YEAR(s2.Date)=YEAR(s1.Date) "
//				+ "AND s1.Retailer_code IN (SELECT r.Retailer_code "
//				+ "							FROM go_retailers r  "
//				+ "							WHERE r.Country = "France") "
//				+ "AND s2.Retailer_code IN (SELECT r.Retailer_code "
//				+ "							FROM go_retailers r  "
//				+ "						    WHERE r.Country = "France") "
//				+ "AND s1.Retailer_code < s2.Retailer_code "
//				+ "GROUP BY s1.Retailer_code, s2.Retailer_code "
//				+ "HAVING N >= 3";
		
		
		List<Adiacenza> adiacenze = new ArrayList<Adiacenza>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, nazione);
			st.setInt(2, anno);
			st.setInt(3, m);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Adiacenza a = new Adiacenza(idMap.get(rs.getInt("rc1")), idMap.get(rs.getInt("rc2")), rs.getDouble("peso")); 
						adiacenze.add(a);
			}
			conn.close();
			return adiacenze;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	
	
	
	
	
	
	
	
}
