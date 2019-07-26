package xyz.juno.advancegenerator.main.updater;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import xyz.juno.advancegenerator.main.AdvanceGenerator;

public class Updater {
	
	public static String verString() {
		String version_str = null;
		
		try {
            int resource_id = 1785;
            URL url = new URL("https://minecraftvn.net/resource.api?id=" + resource_id);
            String json = IOUtils.toString(url, StandardCharsets.UTF_8);
            JsonObject object = new Gson().fromJson(json, JsonObject.class);
            int code = object.getAsJsonPrimitive("code").getAsInt();
            
            if (code == 1) {
            	version_str = object.getAsJsonPrimitive("version").getAsString();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		return version_str;
	}
	
	public static int versionInt() {
		return Integer.valueOf(verString().substring(0, verString().indexOf("-")).replace(".", ""));
	}
	
	public static boolean hasUpdate() {
		int current = Integer.valueOf((AdvanceGenerator.getInstance().getDescription().getVersion().substring(0, AdvanceGenerator.getInstance().getDescription().getVersion().indexOf("-"))).replace(".", ""));
		int lastest = versionInt();
		return lastest > current ? true : false;
	}
}