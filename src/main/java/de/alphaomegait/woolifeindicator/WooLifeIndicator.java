package de.alphaomegait.woolifeindicator;

import de.alphaomegait.woolifeindicator.commands.LifeIndicator;
import de.alphaomegait.woolifeindicator.listener.DamageIndicatorListener;
import de.alphaomegait.woolifeindicator.listener.OnScoreboardCheck;
import lombok.Getter;
import me.blvckbytes.autowirer.AutoWirer;
import me.blvckbytes.bukkitboilerplate.PluginFileHandler;
import me.blvckbytes.bukkitcommands.BukkitCommand;
import me.blvckbytes.bukkitevaluable.ConfigManager;
import me.blvckbytes.bukkitevaluable.IConfigPathsProvider;
import org.bukkit.command.Command;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
public class WooLifeIndicator extends JavaPlugin implements IConfigPathsProvider {

	private final Logger logger = Logger.getLogger(this.getName());

	private AutoWirer autoWirer;

	@Override
	public void onLoad() {
		// Create the plugin folder if it doesn't exist
		if (this.getDataFolder().mkdir()) {
			// Log message
			this.logger.info("Plugin folder created.");
		}

		// Save default configuration files and log messages
		Arrays.stream(this.getConfigPaths()).toList().forEach(
			configPath -> {
				this.saveResource(
					configPath, // Configuration file path
					true // Replace existing file
				);
				this.logger.info("Config file created: " + configPath);
			});
	}

	@Override
// Called when the plugin is enabled
	public void onEnable() {
		final long beginTimestamp = System.nanoTime();

		// Initialize AutoWirer
		this.autoWirer = new AutoWirer();
		this.autoWirer
			.addExistingSingleton(this)
			.addExistingSingleton(this.logger)
			.addSingleton(ConfigManager.class)
			.addSingleton(PluginFileHandler.class)
			.addSingleton(LifeIndicator.class)
			.addSingleton(DamageIndicatorListener.class)
			.addSingleton(OnScoreboardCheck.class)
			.onException(exception -> {
				// Log exception and disable plugin
				this.logger.log(
					Level.SEVERE,
					"An exception occurred while loading the plugin: " + exception,
					exception
				);
				this.getServer().getPluginManager().disablePlugin(this);
				return;
			})
			.wire(success -> {
				// Log success message
				this.logger.info(
					"Successfully loaded " + success.getInstancesCount() + " classes (" + ((System.nanoTime() - beginTimestamp) / 1000 / 1000) + "ms)"
				);
			});

		// Register commands
		this.registerCommands(
			this.autoWirer.findInstance(LifeIndicator.class)
		);

		// Register listeners
		this.registerListener(
			this.autoWirer.findInstance(DamageIndicatorListener.class),
			this.autoWirer.findInstance(OnScoreboardCheck.class)
		);
	}

	@Override
	public void onDisable() {
		this.autoWirer.cleanup();
		this.logger.info("Plugin disabled.");
	}

	/**
	 * Returns an array of configuration file paths.
	 *
	 * @return an array of configuration file paths
	 */
	@Override
	public String[] getConfigPaths() {
		return new String[] {
			"commands/life-indicator-config.yml",
			"utilities/damage-indicator-config.yml"
		};
	}

	/**
	 * Register a list of Bukkit commands.
	 *
	 * @param  bukkitCommands  list of optional Bukkit commands
	 */
	@SafeVarargs
	private void registerCommands(
		final Optional<? extends BukkitCommand>... bukkitCommands
	) {
		for (final Optional<? extends BukkitCommand> command : bukkitCommands)
			command.ifPresentOrElse(
				bukkitCommand ->
					this.getServer()
					.getCommandMap()
					.register(
						bukkitCommand.getName(),
						bukkitCommand
					)
				,
				() -> this.logger.info(
					"Command: '" +
					command.getClass() +
					"' could not be registered. (" +
					(
						command.map(Command::getName).orElse("")
					) +
					")"
				)
			);
	}

	/**
	 * Registers the given Bukkit listeners with the plugin manager.
	 *
	 * @param  bukkitListeners  an optional array of Bukkit listeners to register
	 */
	@SafeVarargs
	private void registerListener(
		final Optional<? extends Listener>... bukkitListeners
	) {
		for (final Optional<? extends Listener> listener : bukkitListeners)
			listener.ifPresentOrElse(
				bukkitListener -> this.getServer()
															.getPluginManager()
															.registerEvents(
																bukkitListener,
																this
															),
				() -> this.logger.info("Listener: '" + listener.getClass() + "' could not be registered.")
			);
	}
}