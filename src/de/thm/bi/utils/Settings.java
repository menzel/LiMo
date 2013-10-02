package de.thm.bi.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Properties;

/**
 * The Settings class is responsible for managing and persistently saving
 * Settings. The Settings can be stored in different propety files. If no
 * property file is specified, the file name config.ini is chosen.
 * 
 * @author Artur Klos
 * 
 */
public class Settings {

	/**
	 * @return Returns the applications settings path
	 */
	public static String getSettingsPath() {
		String path = "";

		try {
			path = Preferences.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		path = path.substring(0, path.lastIndexOf("/") + 1);
		if ((path.startsWith("/") || path.startsWith(File.separator))
				&& System.getProperty("os.name").toLowerCase().contains("win")) {
			path = path.substring(1);
			// TODO: AUF SEPERATOR ANPASSEN FALLS ES NICHT ORDENTLICH
			// FUNKTIONIERT
		}

		return path + "/settings/";
	}

	private String defaultPropertyFile;

	private final HashMap<String, Properties> settingsCache = new HashMap<String, Properties>();

	/**
	 * Creates a default Settings Object. The path the default settings file is
	 * located in the folder APPLICATIONFOLDER/settings/config.ini
	 */
	public Settings() {
		this("config.ini");
	}

	/**
	 * Creates a Settings object with a specific property file as default
	 * propety file. The property file has to be in the settings folder that is
	 * located in the root application folder
	 * 
	 * @param defaultPropertyFile
	 */
	public Settings(String defaultPropertyFile) {
		this.defaultPropertyFile = defaultPropertyFile;
		try {
			loadProperties();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return Gets a preference
	 */
	protected boolean getBoolean(Object prefName, boolean defaultValue) {
		return getBoolean(defaultPropertyFile, prefName.toString(), defaultValue);
	}

	/**
	 * @return Gets a preference
	 */
	protected boolean getBoolean(String prefName, boolean defaultValue) {
		return getBoolean(defaultPropertyFile, prefName, defaultValue);
	}

	/**
	 * @return Gets a preference
	 */
	protected boolean getBoolean(String fileName, Object prefName, boolean defaultValue) {
		return Boolean.valueOf(getProperty(fileName, prefName.toString(), defaultValue));
	}

	/**
	 * @return Gets a preference
	 */
	protected boolean getBoolean(String fileName, String prefName, boolean defaultValue) {
		return Boolean.valueOf(getProperty(fileName, prefName, defaultValue));
	}

	/**
	 * @return Gets a preference
	 */
	protected float getFloat(Object prefName, float defaultValue) {
		return getFloat(defaultPropertyFile, prefName.toString(), defaultValue);
	}

	/**
	 * @return Gets a preference
	 */
	protected float getFloat(String prefName, float defaultValue) {
		return getFloat(defaultPropertyFile, prefName, defaultValue);
	}

	/**
	 * @return Gets a preference
	 */
	protected float getFloat(String fileName, Object prefName, float defaultValue) {
		return Float.valueOf(getProperty(fileName, prefName.toString(), defaultValue));
	}

	/**
	 * @return Gets a preference
	 */
	protected float getFloat(String fileName, String prefName, float defaultValue) {
		return Float.valueOf(getProperty(fileName, prefName, defaultValue));
	}

	/**
	 * @return Gets a preference
	 */
	protected int getInt(Object prefName, int defaultValue) {
		return getInt(defaultPropertyFile, prefName.toString(), defaultValue);
	}

	/**
	 * @return Gets a preference
	 */
	protected int getInt(String prefName, int defaultValue) {
		return getInt(defaultPropertyFile, prefName, defaultValue);
	}

	/**
	 * @return Gets a preference
	 */
	protected int getInt(String fileName, Object prefName, int defaultValue) {
		return Integer.valueOf(getProperty(fileName, prefName.toString(), defaultValue));
	}

	/**
	 * @return Gets a preference
	 */
	protected int getInt(String fileName, String prefName, int defaultValue) {
		return Integer.valueOf(getProperty(fileName, prefName, defaultValue));
	}

	/**
	 * @return Gets a preference
	 */
	protected long getLong(Object prefName, long defaultValue) {
		return getLong(defaultPropertyFile, prefName.toString(), defaultValue);
	}

	/**
	 * @return Gets a preference
	 */
	protected long getLong(String prefName, long defaultValue) {
		return getLong(defaultPropertyFile, prefName, defaultValue);
	}

	/**
	 * @return Gets a preference
	 */
	protected long getLong(String fileName, Object prefName, long defaultValue) {
		return Long.valueOf(getProperty(fileName, prefName.toString(), defaultValue));
	}

	/**
	 * @return Gets a preference
	 */
	protected long getLong(String fileName, String prefName, long defaultValue) {
		return Long.valueOf(getProperty(fileName, prefName, defaultValue));
	}

	/**
	 * Retrieves a property from a specified file with the specified preference
	 * name. If no property is stored the default Value will be returned.
	 * 
	 * @param fileName
	 *            File to search
	 * @param prefName
	 *            Preference name to search for
	 * @param defaultValue
	 *            Default value to return if no property is stored yet.
	 * @return Returns a stored property if no property is available the default
	 *         value will be returned
	 */
	public String getProperty(String fileName, String prefName, Object defaultValue) {
		Properties file = settingsCache.get(fileName);
		if (file == null) {
			setProperty(fileName, prefName, defaultValue);
			file = settingsCache.get(fileName);
		}
		Object value = file.get(prefName);
		if (value == null) {
			setProperty(fileName, prefName, defaultValue);
			value = file.get(prefName);
		}
		return value.toString();
	}

	/**
	 * @return Gets a preference
	 */
	protected String getString(Object prefName, String defaultValue) {
		return getString(defaultPropertyFile, prefName.toString(), defaultValue);
	}

	/**
	 * @return Gets a preference
	 */
	protected String getString(String fileName, Object prefName, String defaultValue) {
		return getProperty(fileName, prefName.toString(), defaultValue);
	}

	/**
	 * @return Gets a preference
	 */
	protected String getString(String prefName, String defaultValue) {
		return getString(defaultPropertyFile, prefName, defaultValue);
	}

	/**
	 * @return Gets a preference
	 */
	protected String getString(String fileName, String prefName, String defaultValue) {
		return getProperty(fileName, prefName, defaultValue);
	}

	/**
	 * Loads all properties from the specified property file. All unsaved
	 * properties will be deleted with this call.
	 */
	public void loadProperties() {
		settingsCache.clear();
		File settingsFolder = new File(getSettingsPath());
		File[] settingsListing = settingsFolder.listFiles();
		if (settingsListing != null) {
			int limit = settingsListing.length;
			try {
				for (int i = 0; i < limit; i++) {
					File file = settingsListing[i];
					boolean isDir = file.isDirectory();
					boolean isVisible = !file.getName().startsWith(".");
					if (!isDir && isVisible) {
						Properties props = new Properties();
						InputStream fileStream = new FileInputStream(file);
						props.load(fileStream);
						fileStream.close();
						settingsCache.put(file.getName(), props);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Saves all stored properties
	 */
	public void saveProperties() {
		for (String key : settingsCache.keySet()) {
			saveProperty(key, settingsCache.get(key));
		}
	}

	private void saveProperty(String fileName, Properties props) {
		try {
			String propPath = getSettingsPath() + defaultPropertyFile;
			FileOutputStream stream = new FileOutputStream(propPath);
			props.store(stream, "");
			stream.close();
		} catch (FileNotFoundException e) {
			File f = new File(getSettingsPath());
			try {
				f.mkdirs();
				f = new File(getSettingsPath() + defaultPropertyFile);
				f.createNewFile();
				saveProperty(fileName, props);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return Gets a preference
	 */
	protected void setBoolean(Object prefName, boolean value) {
		setBoolean(defaultPropertyFile, prefName.toString(), value);
	}

	/**
	 * @return Gets a preference
	 */
	protected void setBoolean(String prefName, boolean value) {
		setBoolean(defaultPropertyFile, prefName, value);
	}

	/**
	 * @return Gets a preference
	 */
	protected void setBoolean(String fileName, Object prefName, boolean value) {
		setProperty(fileName, prefName.toString(), value);
	}

	/**
	 * @return Gets a preference
	 */
	protected void setBoolean(String fileName, String prefName, boolean value) {
		setProperty(fileName, prefName, value);
	}

	/**
	 * @return Gets a preference
	 */
	protected void setFloat(Object prefName, float value) {
		setFloat(defaultPropertyFile, prefName.toString(), value);
	}

	/**
	 * @return Gets a preference
	 */
	protected void setFloat(String prefName, float value) {
		setFloat(defaultPropertyFile, prefName, value);
	}

	/**
	 * @return Gets a preference
	 */
	protected void setFloat(String fileName, Object prefName, float value) {
		setProperty(fileName, prefName.toString(), value);
	}

	/**
	 * @return Gets a preference
	 */
	protected void setFloat(String fileName, String prefName, float value) {
		setProperty(fileName, prefName, value);
	}

	/**
	 * @return Gets a preference
	 */
	protected void setInt(Object prefName, int value) {
		setInt(defaultPropertyFile, prefName.toString(), value);
	}

	/**
	 * @return Gets a preference
	 */
	protected void setInt(String prefName, int value) {
		setInt(defaultPropertyFile, prefName, value);
	}

	/**
	 * @return Gets a preference
	 */
	protected void setInt(String fileName, Object prefName, int value) {
		setProperty(fileName, prefName.toString(), value);
	}

	/**
	 * @return Gets a preference
	 */
	protected void setInt(String fileName, String prefName, int value) {
		setProperty(fileName, prefName, value);
	}

	/**
	 * @return Gets a preference
	 */
	protected void setLong(Object prefName, long value) {
		setLong(defaultPropertyFile, prefName.toString(), value);
	}

	/**
	 * @return Gets a preference
	 */
	protected void setLong(String prefName, long value) {
		setLong(defaultPropertyFile, prefName, value);
	}

	/**
	 * @return Gets a preference
	 */
	protected void setLong(String fileName, Object prefName, long value) {
		setProperty(fileName, prefName.toString(), value);
	}

	/**
	 * @return Gets a preference
	 */
	protected void setLong(String fileName, String prefName, long value) {
		setProperty(fileName, prefName, value);
	}

	/**
	 * Sets a property to the specified file with the specified preference name
	 * and the given value.
	 * 
	 * @param fileName
	 *            File the preference will be saved to
	 * @param prefName
	 *            Preference the value is for
	 * @param value
	 *            Value that should be set for the preference
	 */
	public void setProperty(String fileName, String prefName, Object value) {
		Properties file = settingsCache.get(fileName);
		if (file == null) {
			file = new Properties();
			settingsCache.put(fileName, file);
		}
		String sValue = value.toString();
		file.put(prefName, sValue);
	}

	/**
	 * @return Gets a preference
	 */
	protected void setString(Object prefName, String value) {
		setString(defaultPropertyFile, prefName.toString(), value);
	}

	/**
	 * @return Gets a preference
	 */
	protected void setString(String fileName, Object prefName, String value) {
		setProperty(fileName, prefName.toString(), value);
	}

	/**
	 * @return Gets a preference
	 */
	protected void setString(String prefName, String value) {
		setString(defaultPropertyFile, prefName, value);
	}

	/**
	 * @return Gets a preference
	 */
	protected void setString(String fileName, String prefName, String value) {
		setProperty(fileName, prefName, value);
	}

}
