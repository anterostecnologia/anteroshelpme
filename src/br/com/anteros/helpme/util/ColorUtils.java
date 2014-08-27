package br.com.anteros.helpme.util;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class ColorUtils {
	public static float[] bodyColorHSV;

	static {
		bodyColorHSV = new float[] { 210.0F, 1.0F, 100.0F };
	}

	public static float[] convertRGBtoHSV(float r, float g, float b) {
		float h = 0.0F;
		float s = 0.0F;

		float minRGB = Math.min(Math.min(r, g), b);
		float maxRGB = Math.max(Math.max(r, g), b) * 1.0F;
		float delta = (maxRGB - minRGB) * 1.0F;
		if (maxRGB != 0.0F)
			s = 255.0F * delta / maxRGB;
		else {
			s = 0.0F;
		}
		if (s != 0.0F) {
			if (r == maxRGB)
				h = (g - b) / delta;
			else if (g == maxRGB)
				h = 2.0F + (b - r) / delta;
			else if (b == maxRGB)
				h = 4.0F + (r - g) / delta;
		} else {
			h = -1.0F;
		}
		h *= 60.0F;
		if (h < 0.0F) {
			h += 360.0F;
		}

		return new float[] { h, s * 100.0F / 255.0F, maxRGB * 100.0F / 255.0F };
	}

	public static float[] convertHSVtoRGB(float h0, float s0, float v0) {
		float h = h0 / 360.0F;
		float s = s0 / 100.0F;
		float v = v0 / 100.0F;
		if (s - 0.0D < 0.0D) {
			return new float[] { v * 255.0F, v * 255.0F, v * 255.0F };
		}
		float var_h = h * 6.0F;
		float var_i = (float) Math.floor(var_h);
		float var_1 = v * (1.0F - s);
		float var_2 = v * (1.0F - s * (var_h - var_i));
		float var_3 = v * (1.0F - s * (1.0F - (var_h - var_i)));
		float var_r;
		float var_g;
		float var_b;
		if (var_i == 0.0F) {
			var_r = v;
			var_g = var_3;
			var_b = var_1;
		} else {
			if (var_i == 1.0F) {
				var_r = var_2;
				var_g = v;
				var_b = var_1;
			} else {
				if (var_i == 2.0F) {
					var_r = var_1;
					var_g = v;
					var_b = var_3;
				} else {
					if (var_i == 3.0F) {
						var_r = var_1;
						var_g = var_2;
						var_b = v;
					} else {
						if (var_i == 4.0F) {
							var_r = var_3;
							var_g = var_1;
							var_b = v;
						} else {
							var_r = v;
							var_g = var_1;
							var_b = var_2;
						}
					}
				}
			}
		}
		return new float[] { var_r * 255.0F, var_g * 255.0F, var_b * 255.0F };
	}

	public static float[] getRGBColorDifference(Color targetColor) {
		return getHSVColorDifference(bodyColorHSV,
				convertRGBtoHSV(targetColor.getRed(), targetColor.getGreen(), targetColor.getBlue()));
	}

	public static float[] getRGBColorDifference(RGB targetColor) {
		return getHSVColorDifference(bodyColorHSV,
				convertRGBtoHSV(targetColor.red, targetColor.green, targetColor.blue));
	}

	public static float[] getRGBColorDifference(float sourceR, float sourceG, float sourceB, float targetR,
			float targetG, float targetB) {
		return getHSVColorDifference(convertRGBtoHSV(sourceR, sourceG, sourceB),
				convertRGBtoHSV(targetR, targetG, targetB));
	}

	public static float[] getHSVColorDifference(float[] sourceHSV, float[] targetHSV) {
		float diffH = 0.0F;
		float diffS = 0.0F;
		float diffS2 = 0.0F;
		float diffV = 0.0F;
		float diffV2 = 0.0F;

		diffH = sourceHSV[0] - targetHSV[0];
		if (sourceHSV[1] > 0.0F) {
			diffS = targetHSV[1] / sourceHSV[1];
			if (targetHSV[1] > sourceHSV[1])
				diffS2 = (100.0F - targetHSV[1]) / (100.0F - sourceHSV[1]);
		} else {
			diffS = -1.0F;
		}
		if (sourceHSV[2] > 0.0F) {
			diffV = targetHSV[2] / sourceHSV[2];
			if (targetHSV[2] > sourceHSV[2])
				diffV2 = (100.0F - targetHSV[2]) / (100.0F - sourceHSV[2]);
		} else {
			diffV = -1.0F;
		}

		return new float[] { diffH, diffS, diffS2, diffV, diffV2 };
	}

	public static RGB applyColorDifference(Color color, float[] colorDiff) {
		return applyColorDifference(color.getRed(), color.getGreen(), color.getBlue(), colorDiff, bodyColorHSV[1],
				bodyColorHSV[2]);
	}

	public static RGB applyColorDifference(RGB color, float[] colorDiff) {
		return applyColorDifference(color.red, color.green, color.blue, colorDiff, bodyColorHSV[1], bodyColorHSV[2]);
	}

	public static RGB applyColorDifference(float r, float g, float b, float[] colorDiff, float sourceS, float sourceV) {
		assert ((colorDiff != null) && (colorDiff.length == 5));
		float[] hsv = convertRGBtoHSV(r, g, b);
		return applyColorDifference(hsv, colorDiff, sourceS, sourceV);
	}

	public static RGB applyColorDifference(float[] hsv_orig, float[] colorDiff, float sourceS, float sourceV) {
		float diffH = colorDiff[0];
		float diffS = colorDiff[1];
		float diffS2 = colorDiff[2];
		float diffV = colorDiff[3];
		float diffV2 = colorDiff[4];
		float[] hsv = new float[3];
		hsv[0] = ((hsv_orig[0] - diffH) % 360.0F);
		if (diffS != -1.0F) {
			if (((diffS > 1.0F ? 1 : 0) & (hsv_orig[1] > sourceS ? 1 : 0)) != 0)
				hsv[1] = (100.0F - (100.0F - hsv_orig[1]) * diffS2);
			else {
				hsv_orig[1] *= diffS;
			}
		}
		if (diffV != -1.0F) {
			if (((diffV > 1.0F ? 1 : 0) & (hsv_orig[2] > sourceV ? 1 : 0)) != 0)
				hsv[2] = (100.0F - (100.0F - hsv_orig[2]) * diffV2);
			else {
				hsv_orig[2] *= diffV;
			}
		}
		float[] resultColor = convertHSVtoRGB(hsv[0], hsv[1], hsv[2]);
		return new RGB((int) resultColor[0], (int) resultColor[1], (int) resultColor[2]);
	}
}
