package estudo.s.ipsumintegrationtest.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

public class JsonUtil {

    public JsonUtil() {}
    
    public JSONObject fromFile(String path) {

        try {
            File file = new File(path);

            if(!file.exists()) {
                throw new RuntimeException(String.format("File not found. Path: %s", path));
            }
            
            InputStream inputStream = new FileInputStream(path);
            String jsonTxt = IOUtils.toString(inputStream, "UTF-8");

            return new JSONObject(jsonTxt);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
