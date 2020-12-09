package com.noahhusby.sledgehammer.addons.terramap;

import java.util.List;

import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.players.SledgehammerPlayer;

/**
 * Manages player display preferences
 * 
 * @author SmylerMC
 *
 */
public final class PlayerDisplayPreferences {

	private final static String SHOW_ATTRIBUTE = "TerraShow";
	private final static String HIDE_ATTRIBUTE = "TerraHide";
	
	/**
	 * Indicates if the player should be visible on the map to others
	 * 
	 * @param player - the player
	 * @return a boolean
	 */
	public static boolean shouldDisplayPlayer(SledgehammerPlayer player) {
		List<String> playerAttribs = player.getAttributes();
		synchronized(playerAttribs) {
			if(playerAttribs.contains(SHOW_ATTRIBUTE)) return true;
			if(playerAttribs.contains(HIDE_ATTRIBUTE)) return false;
		}
		return ConfigHandler.terramapPlayersDisplayDefault;
	}

	/**
	 * Set whether or not a player should be visible on the map to others
	 * 
	 * @param player - the player
	 * @param yesNo
	 */
	public static void setShouldDisplayPlayer(SledgehammerPlayer player, boolean yesNo) {
		List<String> playerAttribs = player.getAttributes();
		synchronized(playerAttribs) {
			playerAttribs.remove(SHOW_ATTRIBUTE);
			playerAttribs.remove(HIDE_ATTRIBUTE);
			playerAttribs.add(yesNo? SHOW_ATTRIBUTE: HIDE_ATTRIBUTE);
		}
	}

}