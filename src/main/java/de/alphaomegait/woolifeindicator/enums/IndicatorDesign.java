package de.alphaomegait.woolifeindicator.enums;

public enum IndicatorDesign {

	COMPLEX("complex"),
	SIMPLE("simple")
	;

	// A way to select the pre-design of the indicator in the config
	private final String indicatorDesign;

	IndicatorDesign(
		final String indicatorDesign
	) {
		this.indicatorDesign = indicatorDesign;
	}
}