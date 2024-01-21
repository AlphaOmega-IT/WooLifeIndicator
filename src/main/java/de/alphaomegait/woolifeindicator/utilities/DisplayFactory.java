package de.alphaomegait.woolifeindicator.utilities;

import de.alphaomegait.woolifeindicator.configs.DamageIndicatorConfigurationSection;
import de.alphaomegait.woolifeindicator.configs.LifeIndicatorPermissionSection;
import me.blvckbytes.bukkitevaluable.section.PermissionsSection;
import net.kyori.adventure.text.Component;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Optional;

public class DisplayFactory {

	private final DamageIndicatorConfigurationSection damageIndicatorConfigurationSection;

	/**
	 * DisplayFactory is responsible for managing and displaying life indicators on the scoreboard.
	 */
	public DisplayFactory(
		final @NotNull DamageIndicatorConfigurationSection damageIndicatorConfigurationSection
		) {
		this.damageIndicatorConfigurationSection = damageIndicatorConfigurationSection;
	}

	/**
	 * Calculates the life indicator for a living entity after taking damage.
	 *
	 * @param damage the amount of damage taken
	 * @param entity the living entity
	 * @return the calculated life indicator component
	 */
	public Component calculateLifeIndicator(final @NotNull Double damage, final @NotNull LivingEntity entity) {
		// Calculate the rounded health based on the damage taken and the entity's maximum health
		final int roundedHealth = BigDecimal.valueOf(
			(entity.getHealth() - damage) * 20 /
			Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue()
		).setScale(2, RoundingMode.HALF_DOWN).intValue();

		// Get the damage indicator from the configuration section based on the rounded health
		return this.damageIndicatorConfigurationSection.getDamageIndicators().getOrDefault(
			roundedHealth,
			this.damageIndicatorConfigurationSection.getDamageIndicators().get(
				this.damageIndicatorConfigurationSection.getDamageIndicators().size() - 1)
		);
	}

	/**
	 * Displays the scoreboard for a player if certain conditions are met.
	 *
	 * @param permissionsSection The permissions section to check for permission.
	 * @param scoreboard The scoreboard to display.
	 * @param player The player to display the scoreboard for.
	 * @param damage The damage value to calculate life indicator.
	 * @return Optional of the player's scoreboard if displayed, empty otherwise.
	 */
	public Optional<Scoreboard> displayScoreboard(
		final @NotNull PermissionsSection permissionsSection,
		final @NotNull Scoreboard scoreboard,
		final @NotNull Player player,
		final @NotNull Double damage
	) {
		// Check if the damage indicator should be displayed
		if (!this.damageIndicatorConfigurationSection.getShowLifeIndicatorOfPlayers()) {
			return Optional.empty();
		}

		// Check if the player has permission to see others' life indicator
		if (!permissionsSection.hasPermission(player, LifeIndicatorPermissionSection.SEE_OTHERS_LIFE_INDICATOR)) {
			return Optional.empty();
		}

		// Check if the player is alive
		if (player.isDead()) {
			return Optional.empty();
		}

		// Set the scoreboard display and name
		Objects.requireNonNull(scoreboard.getObjective(DisplaySlot.BELOW_NAME)).setAutoUpdateDisplay(true);
		Objects.requireNonNull(scoreboard.getObjective(DisplaySlot.BELOW_NAME)).displayName(this.calculateLifeIndicator(damage, player));

		// Set the player's scoreboard and return it
		player.setScoreboard(scoreboard);
		return Optional.of(player.getScoreboard());
	}
}