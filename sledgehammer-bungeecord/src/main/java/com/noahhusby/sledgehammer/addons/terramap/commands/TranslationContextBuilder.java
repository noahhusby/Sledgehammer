package com.noahhusby.sledgehammer.addons.terramap.commands;

import java.util.HashMap;
import java.util.Map;

import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.addons.terramap.TerramapVersion;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * 
 * @author SmylerMC
 * 
 * Useful stuff when dealing with translated text components and Terramap
 *
 */
public class TranslationContextBuilder {
	
	private Map<String, String> texts = new HashMap<>();
	private TerramapVersion minTranslationVersion = null;
	
	public TranslationContextBuilder addTranslation(String key, String text) {
		this.texts.put(key, text);
		return this;
	}
	
	/**
	 * Clients with a version newer or equal to this one will be sent the translation key instead of the translated text.
	 * 
	 * @param version - minimum client version that supports translation in this context. null if none do.
	 */
	public TranslationContextBuilder setMinimumVersion(TerramapVersion version) {
		this.minTranslationVersion = version;
		return this;
	}
	
	public TranslationContext createContext(CommandSender sender) {
		return new TranslationContext(sender);
	}
	
	public class TranslationContext {
		
		private final ProxiedPlayer remotePlayer;
		private final TerramapVersion remoteVersion;
		private final boolean remoteSupportsTranslation;
		
		private TranslationContext(CommandSender remote) {
			if(remote instanceof ProxiedPlayer) {
				this.remotePlayer  = (ProxiedPlayer)remote;
				this.remoteVersion = TerramapVersion.getClientVersion(this.remotePlayer);
			} else {
				this.remoteVersion = null;
				this.remotePlayer = null;
			}
			if(TranslationContextBuilder.this.minTranslationVersion == null) {
				this.remoteSupportsTranslation = false;
			} else {
				this.remoteSupportsTranslation = TranslationContextBuilder.this.minTranslationVersion.isOlderOrSame(this.remoteVersion);
			}
		}
		
		public boolean doesSupportTranslation() {
			return this.remoteSupportsTranslation;
		}
		
		public boolean doesSupportFormatting() {
			return this.remotePlayer != null; //i.e. not console
		}
		
		public boolean willTranslateHere(String key) {
			return !this.remoteSupportsTranslation && TranslationContextBuilder.this.texts.containsKey(key);
		}
		
		public String getString(String key) {
			if(this.willTranslateHere(key)) return TranslationContextBuilder.this.texts.get(key);
			return key;
		}
		
		public BaseComponent getBaseTextComponent(String key, Object... objects) {
			if(!this.willTranslateHere(key)) return new TranslatableComponent(key, objects);
			return new TextComponent(String.format(TranslationContextBuilder.this.texts.getOrDefault(key, key), objects));
		}
		
		public void sendError(String key, Object... objects) {
			BaseComponent base = this.getBaseTextComponent(key, objects);
			if(this.remotePlayer != null) {
				base.setColor(ChatColor.RED);
				this.remotePlayer.sendMessage(base);
			} else {
				String text = this.getString(key);
				if(this.willTranslateHere(key)) text = String.format(text, objects);
				Sledgehammer.logger.warning(text);
			}
		}
		
		public void sendRawMessage(String key, Object... objects) {
			BaseComponent base = this.getBaseTextComponent(key, objects);
			if(this.remotePlayer != null) {
				this.remotePlayer.sendMessage(base);
			} else {
				String text = this.getString(key);
				if(this.willTranslateHere(key)) text = String.format(text, objects);
				Sledgehammer.logger.info(text);
			}
		}
		
	}

}
