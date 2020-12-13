package com.flemmli97.runecraftory.common.utils;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.tenshilib.common.utils.ResourceStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConfigUtils {

    public static void copyDefaultConfigs(File dest, String conf) {
        try {
            if (!dest.exists())
                dest.createNewFile();
            OutputStream out = new FileOutputStream(dest);
            InputStream in = ResourceStream.getStream(RuneCraftory.MODID, "configs", conf);
            IOUtils.copy(in, out);
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
