package de.alphaomegait.woolifeindicator.listener;

import de.alphaomegait.woolifeindicator.WooLifeIndicator;
import de.alphaomegait.woolifeindicator.configs.DamageIndicatorConfigurationSection;
import de.alphaomegait.woolifeindicator.utilities.DisplayFactory;
import me.blvckbytes.bukkitevaluable.ConfigManager;
import me.blvckbytes.bukkitevaluable.section.PermissionsSection;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DamageIndicatorListener implements Listener {

	private final WooLifeIndicator wooLifeIndicator;
	private final DamageIndicatorConfigurationSection damageIndicatorConfigurationSection;
	private final PermissionsSection permissionsSection;

	private final Map<UUID, Component> fallbackNames = new LinkedHashMap<>();
	private final List<String> blockedEntities;
	private final List<String> blockedWorlds;

	/**
	 * Listens for damage events and handles damage indicators.
	 *
	 * @param wooLifeIndicator the instance of WooLifeIndicator
	 * @param configManager the ConfigManager instance for retrieving configuration settings
	 * @throws Exception if there is an issue with configuration mapping
	 */
	public DamageIndicatorListener(
		final @NotNull WooLifeIndicator wooLifeIndicator,
		final @NotNull ConfigManager configManager
	) throws Exception {
		this.wooLifeIndicator = wooLifeIndicator;
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
		
		this.blockedEntities = this
			.damageIndicatorConfigurationSection
			.getBlockedEntities()
			.stream()
			.map(String::toLowerCase)
			.map(entity -> entity.replace(" ", "_"))
			.toList();

		this.blockedWorlds = this
			.damageIndicatorConfigurationSection
			.getBlockedWorlds()
			.stream()
			.map(String::toLowerCase)
			.map(world -> world.replace(" ", "_"))
			.toList();
	}

	// This method is called when an entity is damaged
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onDamageHit(final @NotNull EntityDamageEvent event) {
		// Handle the damage event
		this.handleDamageEvents(event);
	}

	/**
	 * Handles the damage events for entities.
	 *
	 * @param event the EntityDamageEvent to handle
	 */
	@SuppressWarnings(value = "deprecation")
	private void handleDamageEvents(final @NotNull EntityDamageEvent event) {
		// Check if the entity's type is blocked
		if (this.blockedEntities.contains(event.getEntity().getType().getName())) return;

		// Check if the entity's world is blocked
		if (this.blockedWorlds.contains(event.getEntity().getWorld().getName().toLowerCase())) return;

		// Check if the entity is a living entity and has a last damage cause
		if (!(event.getEntity() instanceof LivingEntity livingEntity) || event.getEntity().getLastDamageCause() == null) return;

		// Check if the damage is above the current health of the entity
		final boolean isDamageAboveCurrentHealth = event.getFinalDamage() >= livingEntity.getHealth();
		Entity entity = null;
		try {
			entity = ((EntityDamageByEntityEvent) event).getDamager();
		} catch (final ClassCastException ignored) {}

		// Handle damage for non-players
		if (!(livingEntity instanceof Player) && entity instanceof Player) {
			// Update the custom name and visibility for the entity
			if (!fallbackNames.containsKey(livingEntity.getUniqueId())) {
				fallbackNames.put(livingEntity.getUniqueId(), livingEntity.customName());
			}
			livingEntity.setCustomNameVisible(true);
			livingEntity.customName(new DisplayFactory(this.damageIndicatorConfigurationSection)
																.calculateLifeIndicator(event.getFinalDamage(), livingEntity));

			// Schedule task to hide custom name after a certain time
			int taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(this.wooLifeIndicator, () -> {
				livingEntity.setCustomNameVisible(false);
				livingEntity.customName(this.fallbackNames.get(livingEntity.getUniqueId()));
			}, this.damageIndicatorConfigurationSection.getFadeOutTime() * 20L);

			// Cancel task and reset custom name if damage is not above current health
			if (!isDamageAboveCurrentHealth) return;
			Bukkit.getScheduler().cancelTask(taskId);
			livingEntity.customName(this.fallbackNames.get(livingEntity.getUniqueId()));
			livingEntity.setCustomNameVisible(false);
			this.fallbackNames.remove(livingEntity.getUniqueId());
		} else {
			// Handle damage for players
			if (!(livingEntity instanceof Player player)) return;

			// Display scoreboard for the player
			new DisplayFactory(this.damageIndicatorConfigurationSection).displayScoreboard(this.permissionsSection,
																																										 player.getScoreboard(), player, event.getFinalDamage());

			// Unregister scoreboard objective if damage is not above current health
			if (!isDamageAboveCurrentHealth) return;
			Objects.requireNonNull(player.getScoreboard().getObjective(DisplaySlot.BELOW_NAME)).unregister();
		}
	}

	// Listen for the EntityRegainHealthEvent and display the player's scoreboard
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	private void onPlayerRegeneration(final EntityRegainHealthEvent event) {
		// Check if the entity regaining health is a player
		if (event.getEntity() instanceof Player player) {
			// Create a display factory and use it to display the player's scoreboard with a score of 0.00
			new DisplayFactory(this.damageIndicatorConfigurationSection).displayScoreboard(
				this.permissionsSection,
				player.getScoreboard(),
				player,
				0.00
			);
		}
	}
}