package com.vizlore.phasmafood.utils;

/**
 * Created by smedic on 1/4/18.
 */

public class Config {
	public static final String BASE_URL = "https://35.198.85.188";
	public static final String AUTH_SUBURL = "/api/v1/auth/";
	public static final String USERS_URL = "users/create/";

	public static final String BT_DEVICE_UUID_KEY = "BLUETOOTH_DEVICE_UUID";
	public static final String MOBILE_DEVICE_UUID_KEY = "MOBILE_DEVICE_UUID";

	public static final String DEVICE_MAC = "MAC";
	public static final String DEVICE_UUID = "UUID";

	//configuration

	// preferences
	public static final String KEY_CAMERA_EXPOSURE_TIME = "camera_exposure_time";
	public static final String KEY_CAMERA_VOLTAGE = "camera_voltage";

	public static final String KEY_NIR_SINGLE_SHOT = "nir_single_shot";
	public static final String KEY_NIR_SPEC_AVERAGES = "nir_spec_averages";
	public static final String KEY_NIR_MICROLAMPS_VOLTAGE = "nir_microlamps_voltage";
	public static final String KEY_NIR_MICROLAMPS_WARMING_TIME = "nir_mirolamps_warming_time";

	// NIR
	public static final String KEY_VIS_EXPOSURE_TIME_REFLECTANCE = "vis_exposure_time_reflectence";
	public static final String KEY_VIS_GAIN_REFLECTANCE = "vis_gain_reflectance";
	public static final String KEY_VIS_BINNING_REFLECTANCE = "vis_binning_reflectance";
	public static final String KEY_VIS_WHITE_LEDS_VOLTAGE = "vis_white_leds_voltage";

	// VIS
	public static final String KEY_VIS_EXPOSURE_TIME_FLOURESCENCE = "vis_exposure_time_fluorescence";
	public static final String KEY_VIS_GAIN_FLOURESCENCE = "vis_gain_fluorescence";
	public static final String KEY_VIS_BINNING_FLOURESCENCE = "vis_binning_fluorescence";
	public static final String KEY_VIS_UV_LEDS_VOLTAGE = "vis_uv_leds_voltage";

	//default values
	//camera
	public static final int DEFAULT_CAMERA_EXPOSURE_TIME = 100000; // TODO: 4/22/18 check
	public static final int DEFAULT_CAMERA_VOLTAGE = 3000;
	public static final int DEFAULT_NIR_SPEC_AVERAGES = 500;
	public static final int DEFAULT_NIR_MICROLAMPS_VOLTAGE = 2500;
	public static final int DEFAULT_NIR_MICROLAMPS_WARMING_TIME = 500;

	// NIR
	public static final int DEFAULT_VIS_EXPOSURE_TIME_REFLECTANCE = 100000;
	public static final int DEFAULT_VIS_GAIN_REFLECTANCE = 5;
	public static final int DEFAULT_VIS_BINNING_REFLECTANCE = 2;
	public static final int DEFAULT_VIS_WHITE_LEDS_VOLTAGE = 3000;

	// VIS
	public static final int DEFAULT_VIS_EXPOSURE_TIME_FLUORESCENCE = 100000;
	public static final int DEFAULT_VIS_GAIN_FLUORESCENCE = 5;
	public static final int DEFAULT_VIS_BINNING_FLUORESCENCE = 3;
	public static final int DEFAULT_VIS_UV_LEDS_VOLTAGE = 3000;

	// Camera
	public static final int MIN_CAMERA_EXPOSURE_TIME = 0; // TODO: 4/22/18 check
	public static final int MAX_CAMERA_EXPOSURE_TIME = 300000; // TODO: 4/22/18 check
	public static final int MIN_CAMERA_VOLTAGE_TIME = 0;
	public static final int MAX_CAMERA_VOLTAGE_TIME = 4500;

	// NIR
	public static final int MIN_NIR_SPEC_AVERAGES = 1;
	public static final int MAX_NIR_SPEC_AVERAGES = 1000;
	public static final int MIN_NIR_MICROLAMPS_VOLTAGE = 1;
	public static final int MAX_NIR_MICROLAMPS_VOLTAGE = 5000;
	public static final int MIN_NIR_MICROLAMPS_WARMING_TIME = 10;
	public static final int MAX_NIR_MICROLAMPS_WARMING_TIME = 1000;

	// VIS
	public static final int MIN_VIS_EXPOSURE_TIME_REFLECTANCE = 1;
	public static final int MAX_VIS_EXPOSURE_TIME_REFLECTANCE = 300000;
	public static final int MIN_VIS_GAIN_REFLECTANCE = 1;
	public static final int MAX_VIS_GAIN_REFLECTANCE = 10;
	public static final int MIN_VIS_BINNING_REFLECTANCE = 1;
	public static final int MAX_VIS_BINNING_REFLECTANCE = 4;
	public static final int MIN_VIS_WHITE_LEDS_VOLTAGE = 0;
	public static final int MAX_VIS_WHITE_LEDS_VOLTAGE = 4500;
	public static final int MIN_VIS_EXPOSURE_TIME_FLUORESCENCE = 1;
	public static final int MAX_VIS_EXPOSURE_TIME_FLUORESCENCE = 300000;
	public static final int MIN_VIS_GAIN_FLUORESCENCE = 1;
	public static final int MAX_VIS_GAIN_FLUORESCENCE = 10;
	public static final int MIN_VIS_BINNING_FLUORESCENCE = 1;
	public static final int MAX_VIS_BINNING_FLUORESCENCE = 4;
	public static final int MIN_VIS_UV_LEDS_VOLTAGE = 0;
	public static final int MAX_VIS_UV_LEDS_VOLTAGE = 4200;
}
