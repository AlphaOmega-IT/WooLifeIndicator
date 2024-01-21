package de.alphaomegait.woolifeindicator.listener;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import de.alphaomegait.woolifeindicator.configs.DamageIndicatorConfigurationSection;
import de.alphaomegait.woolifeindicator.utilities.DisplayFactory;
import me.blvckbytes.bukkitevaluable.ConfigManager;
import me.blvckbytes.bukkitevaluable.section.PermissionsSection;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class OnScoreboardCheck implements Listener {

	private final DamageIndicatorConfigurationSection damageIndicatorConfigurationSection;
	private final PermissionsSection permissionsSection;
	private final List<String> blockedWorlds;

	private Scoreboard scoreboard;

	/**
	 * Constructor for OnScoreboardCheck.
	 * @param configManager The configuration manager for retrieving configuration settings.
	 * @throws Exception if there is an issue with retrieving the configuration settings.
	 */
	public OnScoreboardCheck(
		final @NotNull ConfigManager configManager
	) throws Exception {
		this.damageIndicatorConfigurationSection = configManager
			.getMapper(
				"utilities/damage-indicator-config.yml"
			)
			.mapSection(
				"damage-indicator",
			DamageIndicatorConfigurationSection.class
			);
		this.permissionsSection = configManager
			.getMapper(
				"commands/life-indicator-config.yml"
			)
			.mapSection(
				"permissions",
				PermissionsSection.class
			);

		this.blockedWorlds = this
			.damageIndicatorConfigurationSection
			.getBlockedWorlds()
			.stream()
			.map(String::toLowerCase)
			.map(world -> world.replace(" ", "_"))
			.toList();
	}

	// This method handles the player join event
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerJoin(
		final @NotNull PlayerJoinEvent event
	) {
		// Get the player from the event
		final Player player = event.getPlayer();

		// Check if the player's world is blocked
		if (this.checkIfWorldIsBlocked(player))
			return;

		// Register a new scoreboard for the player
		this.registerNewScoreboard();

		// Display the scoreboard for the player
		new DisplayFactory(this.damageIndicatorConfigurationSection)
			.displayScoreboard(this.permissionsSection, this.scoreboard, player, 0.00);
	}

	// Handle player world change event
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onWorldChange(final @NotNull PlayerChangedWorldEvent event) {
		// Get the player from the event
		final Player player = event.getPlayer();

		// Check if the world is blocked for the player
		if (this.checkIfWorldIsBlocked(player)) {
			return;
		}

		// Register a new scoreboard for the player
		this.registerNewScoreboard();

		// Display the scoreboard for the player
		new DisplayFactory(this.damageIndicatorConfigurationSection)
			.displayScoreboard(this.permissionsSection, this.scoreboard, player, 0.00);
	}

	/**
	 * Listens for the player post respawn event and updates the scoreboard.
	 * @param event The PlayerPostRespawnEvent instance
	 */
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onEntityKill(final @NotNull PlayerPostRespawnEvent event) {
		final Player player = event.getPlayer();

		// Check if the player's world is blocked
		if (this.checkIfWorldIsBlocked(player)) {
			return;
		}

		// Register a new scoreboard and display it for the player
		this.registerNewScoreboard();
		new DisplayFactory(this.damageIndicatorConfigurationSection)
			.displayScoreboard(this.permissionsSection, this.scoreboard, player, 0.00);
	}

	/**
	 * Registers a new scoreboard with a life indicator objective.
	 */
	private void registerNewScoreboard() {
		// Get the scoreboard manager
		final ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
		// Create a new scoreboard
		this.scoreboard = scoreboardManager.getNewScoreboard();
		// Register a new objective for life indicator
		Objective objective = this.scoreboard.registerNewObjective(
			"lifeIndicator", // Objective name
			Criteria.DUMMY, // Criteria for the objective
			Component.empty() // Display name of the objective
		);
		// Set the display slot for the objective
		objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
	}

	/**
	 * Check if the player is in a blocked world and unregister their scoreboard objective.
	 *
	 * @param player the player to check
	 * @return true if the player is in a blocked world, false otherwise
	 */
	private boolean checkIfWorldIsBlocked(final @NotNull Player player) {
		if (this.blockedWorlds.contains(player.getWorld().getName().toLowerCase())) {
			Objects.requireNonNull(player.getScoreboard().getObjective(DisplaySlot.BELOW_NAME)).unregister();
			return true;
		}
		return false;
	}
}