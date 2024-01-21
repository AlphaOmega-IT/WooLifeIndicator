package de.alphaomegait.woolifeindicator.commands;

import de.alphaomegait.woolifeindicator.configs.LifeIndicatorConfigurationSection;
import de.alphaomegait.woolifeindicator.configs.LifeIndicatorPermissionSection;
import me.blvckbytes.bukkitcommands.PlayerCommand;
import me.blvckbytes.bukkitevaluable.ConfigManager;
import me.blvckbytes.bukkitevaluable.section.PermissionsSection;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class LifeIndicator extends PlayerCommand {

	private final PermissionsSection permissionsSection;

	/**
	 * Constructs a new LifeIndicator instance with the given logger and config manager.
	 *
	 * @param logger The logger used for logging events
	 * @param configManager The configuration manager for retrieving and managing the configuration files
	 * @throws Exception if an error occurs during construction
	 */
	public LifeIndicator(
		final @NotNull Logger logger,
		final @NotNull ConfigManager configManager
	) throws Exception {
		super(
			configManager
				.getMapper(
					"commands/life-indicator-config.yml"
				)
				.mapSection(
					"commands.life-indicator",
					LifeIndicatorConfigurationSection.class
				),
			logger
		);
		this.permissionsSection = configManager
			.getMapper(
				"commands/life-indicator-config.yml"
			)
			.mapSection(
				"permissions",
				PermissionsSection.class
			);
	}

	/**
	 * Overrides the onPlayerInvocation method to handle player commands.
	 *
	 * @param player the player who invoked the command
	 * @param label the label of the command
	 * @param args the arguments of the command
	 */
	@Override
	protected void onPlayerInvocation(
		final Player player,
		final String label,
		final String[] args
	) {
		if (!this.permissionsSection.hasPermission(player, LifeIndicatorPermissionSection.LIFE_INDICATOR_ADMIN)) {
			this.permissionsSection.sendMissingMessage(player, LifeIndicatorPermissionSection.LIFE_INDICATOR_ADMIN);
			return;
		}

		//TODO: Add functionality command for admin without accessing the config directly!
	}

	/**
	 * Overrides the onTabComplete method to provide tab completion for the command.
	 *
	 * @param commandSender The command sender
	 * @param label The label of the command
	 * @param args The arguments passed to the command
	 * @return A list of tab completion options
	 */
	@Override
	protected List<String> onTabComplete(
		final CommandSender commandSender,
		final String label,
		final String[] args
	) {
		return new ArrayList<>();
	}
}