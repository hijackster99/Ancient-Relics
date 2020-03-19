package com.hijackster99.core.util;

public class EntityRenderUtil {

	public static float calcYaw(float rot) {
		
		return (float) (rot * 2 * Math.PI);
	}
	
	public static float calcY(float rot, float frequency, float amplitude) {
		
		return (float) (amplitude * Math.sin(frequency * calcYaw(rot)));
	}
	
}
