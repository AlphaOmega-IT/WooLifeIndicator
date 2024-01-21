package de.alphaomegait.woolifeindicator.configs;

import me.blvckbytes.bbconfigmapper.sections.CSAlways;
import me.blvckbytes.bbconfigmapper.sections.IConfigSection;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DamageIndicatorConfigurationSection implements IConfigSection {

	@CSAlways
	private List<String> blockedWorlds;

	@CSAlways
	private List<String> blockedEntities;

	@CSAlways
	private Integer fadeOutTime;

	@CSAlways
	private String damageIndicatorDesign;

	@CSAlways
	private Boolean showLifeIndicatorOfPlayers;

	@CSAlways
	private Map<Integer, String> damageIndicators;

	/**
	 * Retrieves the list of blocked worlds.
	 *
	 * @return         the list of blocked worlds
	 */
	public List<String> getBlockedWorlds() {
		return this.blockedWorlds == null ? new ArrayList<>() : this.blockedWorlds;
	}

	/**
	 * Retrieves the list of blocked entities.
	 *
	 * @return         	the list of blocked entities
	 */
	public List<String> getBlockedEntities() {
		return this.blockedEntities == null ? new ArrayList<>() : this.blockedEntities;
	}

	/**
	 * Returns the fade out time, or 3 if not set.
	 *
	 * @return the fade out time, or 3 if not set
	 */
	public Integer getFadeOutTime() {
		return this.fadeOutTime == null ? 3 : this.fadeOutTime;
	}

	/**
	 * Retrieves the damage indicator design.
	 *
	 * @return  the damage indicator design, or an empty string if it is null
	 */
	public String getDamageIndicatorDesign() {
		return this.damageIndicatorDesign == null ? "" : this.damageIndicatorDesign;
	}

	/**
	 * Returns the value of showLifeIndicatorOfPlayers, or true if it is null.
	 *
	 * @return         	the value of showLifeIndicatorOfPlayers, or true if it is null
	 */
	public Boolean getShowLifeIndicatorOfPlayers() {
		return this.showLifeIndicatorOfPlayers == null || this.showLifeIndicatorOfPlayers;
	}

	/**
	 * Get the damage indicators with different designs
	 * @return a map of damage indicators
	 */
	public Map<Integer, Component> getDamageIndicators() {
		// Initialize damageIndicators if it's null
		if (this.damageIndicators == null) {
			this.damageIndicators = new LinkedHashMap<>();
		}

		// Re-generate damageIndicators if it's empty
		if (this.damageIndicators.isEmpty()) {
			this.damageIndicators = new LinkedHashMap<>();
			if (this.getDamageIndicatorDesign().equalsIgnoreCase("Complex")) {
				// Generate damage indicators with complex design
				for (int i = 0; i < 20; i++) {
					boolean isHalf = i % 2 == 0;
					String color = i <= 4 ? "<dark_red>" : i <= 8 ? "<red>" : i <= 12 ? "<gold>" : i <= 16 ? "<yellow>" : "<green>";
					this.damageIndicators.put(
						i,
						color + (i == 0 ? "❥" : "❤".repeat(i)) + (isHalf ? "❥" : "") + color.replace("<", "</")
					);
				}
			} else if (this.getDamageIndicatorDesign().equalsIgnoreCase("Simple")) {
				// Generate damage indicators with simple design
				for (int i = 0; i < 20; i++) {
					boolean isHalf = i % 2 == 0;
					String color = i <= 4 ? "<dark_red>" : i <= 8 ? "<red>" : i <= 12 ? "<gold>" : i <= 16 ? "<yellow>" : "<green>";
					this.damageIndicators.put(
						i,
						color + Double.sum((i == 0 ? 0.5 : i + 1), + (isHalf ? 0.5 : 0)) + color.replace("<", "</")
					);
				}
			}
		}

		// Convert the stored damage indicators to Component objects
		final Map<Integer, Component> damageIndicators = new LinkedHashMap<>();
		for (final Map.Entry<Integer, String> entry : this.damageIndicators.entrySet()) {
			damageIndicators.put(entry.getKey(), MiniMessage.miniMessage().deserialize(entry.getValue()));
		}

		return damageIndicators;
	}
}