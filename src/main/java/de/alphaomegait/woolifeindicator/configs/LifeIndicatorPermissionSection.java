package de.alphaomegait.woolifeindicator.configs;

import me.blvckbytes.bukkitevaluable.section.IPermissionNode;
import org.jetbrains.annotations.NotNull;

public enum LifeIndicatorPermissionSection implements IPermissionNode {

	LIFE_INDICATOR_ADMIN("lifeIndicatorAdmin", "woolifeindicator.lifeindicator.admin"),
	SEE_OTHERS_LIFE_INDICATOR("seeOthersLifeIndicator", "woolifeindicator.lifeindicator.visible.players");

	private final String internalName;
	private final String fallbackNode;

	/**
	 * Represents the permission section for the Life Indicator feature.
	 */
	LifeIndicatorPermissionSection(
		final @NotNull String internalName,
		final @NotNull String fallbackNode
	) {
		this.internalName = internalName;
		this.fallbackNode = fallbackNode;
	}

	@Override
	public String getInternalName() {
		return this.internalName;
	}

	@Override
	public String getFallbackNode() {
		return this.fallbackNode;
	}
}