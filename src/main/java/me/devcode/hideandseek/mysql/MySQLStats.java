package me.devcode.hideandseek.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.devcode.hideandseek.HideAndSeek;

public class MySQLStats {

	public boolean getBooleanMethod(String table, String from, String uuid) {
		
			 boolean contains = false;
			ResultSet rs = null;
			PreparedStatement ps = null;
			try {
				 ps = HideAndSeek.getInstance().getMysql()
						.prepare("SELECT " + from + " FROM " + table + " WHERE " + from + "=?");
				ps.setString(1, uuid);

				rs = ps.executeQuery();
			
			} catch (SQLException e) {
				e.printStackTrace();
			}

			try {
				while (rs.next()) {
					if (rs != null)

						contains = true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		

		return contains;
	}

	public int getIntMethod(String table, String from, String uuid, String get) {
		PreparedStatement statement = null;
		if (getBooleanMethod(table, from, uuid)) {
			ResultSet rs = null;
			try {
				 statement = HideAndSeek.getInstance().getMysql()
						.prepare("SELECT " + get + " FROM " + table + " WHERE " + from + "=?");

				statement.setString(1, uuid);
				rs = statement.executeQuery();
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			}

			int i = 0;
			try {
				while (rs.next()) {
					if (rs != null) {
						i = rs.getInt(get);
						
					}
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				statement.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return i;
		}
		return 0;
	}
	
	public long getLongMethod(String table, String from, String uuid, String get) {
		PreparedStatement statement = null;
		if (getBooleanMethod(table, from, uuid)) {
			ResultSet rs = null;
			try {
				 statement = HideAndSeek.getInstance().getMysql()
						.prepare("SELECT " + get + " FROM " + table + " WHERE " + from + "=?");

				statement.setString(1, uuid);
				rs = statement.executeQuery();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}

			long l = 0;
			try {
				while (rs.next()) {
					if (rs != null) {
						l = rs.getInt(get);
						
					}
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return l;
		}
		return 0;
	}

	public String getStringMethod(String table, String from, String uuid, String get) {

		ResultSet rs = null;
		PreparedStatement statement = null;
		try {
			 statement = HideAndSeek.getInstance().getMysql()
					.prepare("SELECT " + get + " FROM " + table + " WHERE " + from + "=?");
			statement.setString(1, uuid);
			rs = statement.executeQuery();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String s = "";
		try {
			while (rs.next()) {
				if (rs != null) {
					s = rs.getString(get);
					
				}
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;

	}

	public void setIntMethod(String table, String from, String uuid, int setint, String set) {

		if (getBooleanMethod(table, from, uuid)) {
			HideAndSeek.getInstance().getMysql().update(
					"UPDATE " + table + " SET " + set + "= '" + setint + "' WHERE " + from + "= '" + uuid + "';");
		}
	}
	
	public void setLongMethod(String table, String from, String uuid, long setlong, String set) {

		if (getBooleanMethod(table, from, uuid)) {
			HideAndSeek.getInstance().getMysql().update(
					"UPDATE " + table + " SET " + set + "= '" + setlong + "' WHERE " + from + "= '" + uuid + "';");
		}
	}

	public void setStringMethod(String table, String from, String uuid, String setstring, String set) {

		HideAndSeek.getInstance().getMysql().update(
				"UPDATE " + table + " SET " + set + "= '" + setstring + "' WHERE " + from + "= '" + uuid + "';");

	}

	public void setListMethod(String table, String from, String uuid, List<String> a, String set) {
		
		HideAndSeek.getInstance().getMysql().update(
				"UPDATE " + table + " SET " + set + "= '" + a + "' WHERE " + from + "= '" + uuid + "';");

	}

}
