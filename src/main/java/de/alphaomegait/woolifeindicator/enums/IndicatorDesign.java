package de.alphaomegait.woolifeindicator.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IndicatorDesign {

	COMPLEX("complex"),
	SIMPLE("simple")
	;

	// A way to select the pre-design of the indicator in the config
	private final String indicatorDesign;
}