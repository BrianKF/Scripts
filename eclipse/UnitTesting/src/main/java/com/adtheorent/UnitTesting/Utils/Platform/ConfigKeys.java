package com.adtheorent.UnitTesting.Utils.Platform;

/**
 * Keys for configuration overrides (via properties file).
 */
public final class ConfigKeys {

	private ConfigKeys() {
		// Private constructor to prevent instantiation
	}

	public static final String ENVIRONMENT_PROPERTIES_FILE_NAME_KEY = "platform-environment.properties";
	public static final String AWS_CREDENTIALS_FILE = "AwsCredentials.properties";

	public static final String DEBUG_PORT = "Debug.HttpPort";

	public static final String RTDM_OFF_KEYS_BUCKET = "RTDMOff.S3Bucket.Bucket";
	public static final String RTDM_OFF_KEYS_KEY = "RTDMOff.S3Bucket.Key";

	public static final String UALOADER_LOAD_HOUR = "ualoader.load.time.hour";
	public static final String UALOADER_LOAD_MIN = "ualoader.load.time.min";

	public static final String UALOADER_FILE_PATH = "ualoader.file.path";
	public static final String UALOADER_BUCKET_NAME = "ualoader.bucket.name";
	public static final String UALOADER_KEY_NAME = "ualoader.key.name";

	public static final String UALOADER_INITIAL_LOAD = "ualoader.initial.load";
	public static final String UALOADER_INIT_QUERY = "ualoader.init.query";
	public static final String UALOADER_INCREMENTAL_QUERY = "ualoader.incremental.query";

	public static final String UA_PROVIDER = "UAProvider";

	public static final String DE_FILE_PATH = "de.file.path";
	public static final String DE_BUCKET_NAME = "de.bucket.name";

	public static final String DE_04_FILE = "de.04.file";
	public static final String DE_08_FILE = "de.08.file";
	public static final String DE_09_FILE = "de.09.file";
	public static final String DE_24_FILE = "de.24.file";
	public static final String DE_TZ_FILE = "de.tz.file";

	public static final String DE_04_24_FILE = "de.04.24.file";
	public static final String DE_04_24_09_FILE = "de.04.24.09.file";
	public static final String DE_04_24_09_08_FILE = "de.04.24.09.08.file";

	public static final String DE_TARGET_FILE = "de.target.file";
	public static final String DE_BACKUP_FILE = "de.backup.file";

	public static final String DE_TARGET_LOCATION = "de.target.location";

	public static final String RETARGETING_FILE_PATH = "retargeting.file.location";
	public static final String RETARGETING_BUCKET = "retargeting.bucket";

}
