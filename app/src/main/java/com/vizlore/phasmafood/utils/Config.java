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
	public static final String KEY_CAMERA_EXPOSURE_TIME_WHITE_LEDS = "t_cam_white";
	public static final String KEY_CAMERA_EXPOSURE_TIME_UV = "t_cam_uv";
	public static final String KEY_CAMERA_EXPOSURE_TIME_NIR = "t_cam_nir";
	public static final String KEY_CAMERA_CAPTURE_IMAGE_WHITE = "capture_image_white";
	public static final String KEY_CAMERA_CAPTURE_IMAGE_NIR = "capture_image_nir";
	public static final String KEY_CAMERA_CAPTURE_IMAGE_UV = "capture_image_uv";

	public static final String KEY_NIR_SINGLE_SHOT = "nir_single_shot";
	public static final String KEY_NIR_SPEC_AVERAGES = "nir_spec_averages";

	// NIR
	public static final String KEY_VIS_EXPOSURE_TIME_REFLECTANCE = "vis_exposure_time_reflectence";
	public static final String KEY_VIS_WHITE_LEDS_VOLTAGE = "vis_white_leds_voltage";

	// VIS
	public static final String KEY_VIS_EXPOSURE_TIME_FLOURESCENCE = "vis_exposure_time_fluorescence";
	public static final String KEY_VIS_UV_LEDS_VOLTAGE = "vis_uv_leds_voltage";

	public static final String KEY_MICROBIOLOGICAL_UNIT = "microbiological_unit";

	//default values
	//camera
	public static final int DEFAULT_CAMERA_EXPOSURE_TIME = 100000; // TODO: 4/22/18 check
	public static final int DEFAULT_CAMERA_VOLTAGE = 3000;
	public static final int DEFAULT_NIR_SPEC_AVERAGES = 100;
	public static final int DEFAULT_NIR_MICROLAMPS_CURRENT = 50;
	public static final int DEFAULT_NIR_MICROLAMPS_WARMING_TIME = 500;

	public static final String DEFAULT_MICROBIOLOGICAL_UNIT = "log CFU/g";

	// NIR
	public static final int DEFAULT_VIS_EXPOSURE_TIME_REFLECTANCE = 100000;
	public static final int DEFAULT_VIS_BINNING_REFLECTANCE = 2;
	public static final int DEFAULT_VIS_WHITE_LEDS_CURRENT = 1;

	// VIS
	public static final int DEFAULT_VIS_EXPOSURE_TIME_FLUORESCENCE = 100000;
	public static final int DEFAULT_VIS_UV_LEDS_VOLTAGE = 200;

	// Camera
	public static final int MIN_CAMERA_EXPOSURE_TIME = 1000; // TODO: 4/22/18 check

	// NIR
	public static final int MIN_NIR_SPEC_AVERAGES = 1;
	public static final int MAX_NIR_SPEC_AVERAGES = 500;

	// VIS
	public static final int MIN_VIS_EXPOSURE_TIME_REFLECTANCE = 1;
	public static final int MAX_VIS_EXPOSURE_TIME_REFLECTANCE = 300000;
	public static final int MIN_VIS_WHITE_LEDS_CURRENT = 0;
	public static final int MAX_VIS_WHITE_LEDS_CURRENT = 100;
	public static final int MIN_VIS_EXPOSURE_TIME_FLUORESCENCE = 1;
	public static final int MAX_VIS_EXPOSURE_TIME_FLUORESCENCE = 300000;
	public static final int MIN_VIS_UV_LEDS_CURRENT = 0;
	public static final int MAX_VIS_UV_LEDS_CURRENT = 350;

	// Test use case - lights on duration
	public static final int MIN_LIGHTS_ON_DURATION = 5;
	public static final int MAX_LIGHTS_ON_DURATION = 300;

}
