package me.devcode.hideandseek.mysql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.devcode.hideandseek.HideAndSeek;

public class MySQLUtils extends MySQLMethods{
	
	private HashMap<String, Integer> kills = new HashMap<>();
	private HashMap<String, Integer> deaths = new HashMap<>();
	private HashMap<String, Integer> wins = new HashMap<>();
	private HashMap<String, Integer> games = new HashMap<>();
	private HashMap<String, Integer> rank = new HashMap<>();
	private HashMap<String, String> blocks = new HashMap<>();
	private HashMap<String, Integer> coins = new HashMap<>();
	public ArrayList<UUID> uuids = new ArrayList<>();
	public void updateStatsForPlayer(String uuid) {
		if(!HideAndSeek.getInstance().isMysqlActivated()) {
			return;
		}
		setAllMethod("HideAndSeek", "UUID", uuid, getKillsByPlayer(uuid), getDeathsByPlayer(uuid), getWinsByPlayer(uuid), getGamesByPlayer(uuid), getBlocksByPlayer(uuid), getCoinsByPlayer(uuid));
	    uuids.remove(UUID.fromString(uuid));
	}

    public void updateStatsForPlayer2(String uuid) {
        if(!HideAndSeek.getInstance().isMysqlActivated()) {
            return;
        }
        setAllMethod("HideAndSeek", "UUID", uuid, getKillsByPlayer(uuid), getDeathsByPlayer(uuid), getWinsByPlayer(uuid), getGamesByPlayer(uuid), getBlocksByPlayer(uuid), getCoinsByPlayer(uuid));
    }
	
	public void setMapValues(Player player) {
		if(!HideAndSeek.getInstance().isMysqlActivated()) {
			kills.put(player.getUniqueId().toString(), 0);
			deaths.put(player.getUniqueId().toString(), 0);
			wins.put(player.getUniqueId().toString(), 0);
			games.put(player.getUniqueId().toString(), 0);
			rank.put(player.getUniqueId().toString(), 0);
			coins.put(player.getUniqueId().toString(), 0);
			blocks.put(player.getUniqueId().toString(), "Empty");
			return;
		}
		kills.put(player.getUniqueId().toString(), getKills(player.getUniqueId().toString()));
		deaths.put(player.getUniqueId().toString(), getDeaths(player.getUniqueId().toString()));
		wins.put(player.getUniqueId().toString(), getWins(player.getUniqueId().toString()));
		games.put(player.getUniqueId().toString(), getGames(player.getUniqueId().toString()));
		rank.put(player.getUniqueId().toString(), getRank("HideAndSeek", "UUID", player.getUniqueId().toString()));
		coins.put(player.getUniqueId().toString(), getCoins(player.getUniqueId().toString()));
		blocks.put(player.getUniqueId().toString(), getBlocks(player.getUniqueId().toString()));
		uuids.add(player.getUniqueId());
	}

	public void update() {
		if(!HideAndSeek.getInstance().isMysqlActivated()) {
			return;
		}
		uuids.forEach(uuid ->
			updateStatsForPlayer2(uuid.toString()));
		uuids.clear();
	}
	
	public void addKillsByPlayer(String uuid) {
		kills.put(uuid, getKillsByPlayer(uuid)+1);
	}
	
	public void addDeathsByPlayer(String uuid) {
		deaths.put(uuid, getDeathsByPlayer(uuid)+1);
	}
	
	public void addWinsByPlayer(String uuid) {
		wins.put(uuid, getWinsByPlayer(uuid)+1);
	}

	public void addCoinsByPlayer(String uuid, Integer coins) {
		this.coins.put(uuid, getCoinsByPlayer(uuid)+coins);
	}

	public void removeCoinsByPlayer(String uuid, Integer coins) {
		this.coins.put(uuid, getCoinsByPlayer(uuid)-coins);
	}
	
	public Integer getRankByPlayer(String uuid) {
		if(rank.containsKey(uuid)) {
			return rank.get(uuid);
			}
			return 0;
	}
	
	public void addGameByPlayer(String uuid, Integer spiele) {
		this.games.put(uuid, getGamesByPlayer(uuid)+spiele);
	}
		
	public Integer getWinsByPlayer(String uuid) {
		if(wins.containsKey(uuid)) {
		return wins.get(uuid);
		}
		return 0;
	}
	
	public Integer getGamesByPlayer(String uuid) {
		if(games.containsKey(uuid)) {
			return games.get(uuid);
			}
			return 0;
	}
		
	public Integer getKillsByPlayer(String uuid) {
		if(kills.containsKey(uuid)) {
		return kills.get(uuid);
		}
		return 0;
	}
	
	public Integer getDeathsByPlayer(String uuid) {
		if(deaths.containsKey(uuid)) {
			return deaths.get(uuid);
			}
			return 0;
	}

	public Integer getCoinsByPlayer(String uuid) {
		if(coins.containsKey(uuid))
			return coins.get(uuid);
		return 0;
	}

	public void setBlocks(String uuid, String blocks) {
		this.blocks.put(uuid, blocks);
	}

	public String getBlocksByPlayer(String uuid) {
		if(blocks.containsKey(uuid)) {
			return blocks.get(uuid);
		}
		return "Empty";
	}
	

}
