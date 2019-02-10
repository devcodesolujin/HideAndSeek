package me.devcode.hideandseek.mysql;

import java.sql.SQLException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import me.devcode.hideandseek.HideAndSeek;


public class MySQLMethods {
	
	
public void createPlayer(String uuid) {
		
		if(!HideAndSeek.getInstance().getStats().getBooleanMethod("HideAndSeek", "UUID", uuid)) {
			
			PreparedStatement createPlayer = HideAndSeek.getInstance().getMysql().prepare("INSERT INTO HideAndSeek(UUID, KILLS, DEATHS, WINS, GAMES, BLOCKS, COINS) VALUES (?, ?, ?, ?, ?, ?, ?);");
			try {
				createPlayer.setString(1, uuid);
				createPlayer.setInt(2, 0);
				createPlayer.setInt(3, 0);
				createPlayer.setInt(4, 0);
				createPlayer.setInt(5, 0);
				createPlayer.setString(6, "Empty");
				createPlayer.setInt(7, 100);
				createPlayer.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}	
	
public int getRank(String table, String from, String uuid) {
	int count = 0;
	if(HideAndSeek.getInstance().getStats().getBooleanMethod(table, from, uuid)) {
		ResultSet rs = null;
		try{
			
			PreparedStatement statement = HideAndSeek.getInstance().getMysql().prepare("SELECT * FROM " + table + " ORDER BY WINS DESC");
			rs = statement.executeQuery();
			while(rs.next()) {
				
				
				count++;
				String nameduuid = rs.getString("UUID");
				UUID uuid1 = UUID.fromString(nameduuid);
				
				if(uuid1.toString().equals(uuid)) {
					statement.close();
					return count;
				}
			}
		}catch(SQLException e) {
			
		}
		
	}
	return count;
}
	
	public Integer getGames(String uuid) {

		return HideAndSeek.getInstance().getStats().getIntMethod("HideAndSeek", "UUID",uuid,"GAMES");
	}

	public Integer getCoins(String uuid) {

		return HideAndSeek.getInstance().getStats().getIntMethod("HideAndSeek", "UUID",uuid,"COINS");
	}

	public String getBlocks(String uuid) {

		return HideAndSeek.getInstance().getStats().getStringMethod("HideAndSeek", "UUID",uuid,"BLOCKS");
	}
	
	public Integer getWins(String uuid) {

		return HideAndSeek.getInstance().getStats().getIntMethod("HideAndSeek", "UUID",uuid,"WINS");
	}
	
	public Integer getKills(String uuid) {
		return HideAndSeek.getInstance().getStats().getIntMethod("HideAndSeek", "UUID",uuid,"KILLS");
	}
	
	public Integer getDeaths(String uuid) {

		return HideAndSeek.getInstance().getStats().getIntMethod("HideAndSeek", "UUID",uuid,"DEATHS");
	}
		
	public void setAllMethod(String table, String from, String uuid,Integer kills, Integer deaths, Integer wins, Integer games, String blocks, Integer coins) {
		PreparedStatement statement = HideAndSeek.getInstance().getMysql().prepare(
				"UPDATE " + table + " SET KILLS = ?, DEATHS = ?, WINS = ?, GAMES = ?, BLOCKS = ?, COINS = ? WHERE " + from + "= ?;");
		try {
			statement.setInt(1, kills);
		
		statement.setInt(2, deaths);
		statement.setInt(3, wins);
		statement.setInt(4, games);
			statement.setString(5, blocks);
			statement.setInt(6, coins);
		statement.setString(7, uuid);
		statement.executeUpdate();
		statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
