package com.hassan.masla7ty.map;

/**
 * MapAreaMeasure
 *
 */
public class MapAreaMeasure {
    
	public static enum Unit {pixels, meters}
	
	public double value;
	public Unit unit;
	
	public MapAreaMeasure(double value, Unit unit) {
		this.value = value;
		this.unit = unit;
	}
}