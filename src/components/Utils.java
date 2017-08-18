package components;

import java.io.EOFException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Utilities class.
 * 
 * @author Manuel Calzolari
 */
public class Utils {
	/**
	 * Gets contents of a given web page address.
	 */
	public static String UrlGetContents(String pageaddress) {
		String result = null;

		InputStreamReader isr = null;
		StringWriter sw = null;
		try {
			URL url = new URL(pageaddress);
			URLConnection con = url.openConnection();

			isr = new InputStreamReader(con.getInputStream());
			sw = new StringWriter();
			int i;
			while ((i = isr.read()) != -1)
				sw.write(i);

			result = sw.toString();
		} catch (EOFException e) {
			// ok
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				sw.close();
			} catch (Exception ee) {
			}
			try {
				isr.close();
			} catch (Exception ee) {
			}
		}
		return result;
	}

	/**
	 * Parses the string into a map.
	 */
	public static HashMap<String, String> ParseStr(String q) {
		HashMap<String, String> result = new HashMap<String, String>();
		try {
			if (q != null) {
				String[] qq = q.split("&");
				for (int i = 0; i < qq.length; i++) {
					String[] kv = qq[i].split("=");
					result.put(kv[0], (kv.length > 1 ? URLDecoder.decode(kv[1], "UTF-8") : ""));
				}
			}
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * Converts a JSON string into separated values.
	 */
	public static String JsonToSv(String v) {
		Gson gson = new Gson();
		Type listType = new TypeToken<List<String>>(){}.getType();
		List<String> list = gson.fromJson(v, listType);

		StringBuilder sb = new StringBuilder();
		for (String item : list) {
			if (sb.length() > 0)
				sb.append("\r\n");
			sb.append(item);
		}

		return sb.toString();
	}

	/**
	 * Converts separated values into a JSON string.
	 */
	public static String SvToJson(String v) {
		List<String> items = Arrays.asList(v.split("\\r\\n"));
		Gson gson = new Gson();
		Type listType = new TypeToken<List<String>>(){}.getType();
		String json = gson.toJson(items, listType);
		return json;
	}
}
