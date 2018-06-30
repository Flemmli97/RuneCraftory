package com.flemmli97.runecraftory.common.lib;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class LibReference
{
    public static final String MODID = "runecraftory";
    public static final String MODNAME = "Rune Craftory";
    public static final String VERSION = "a0.3.0 [1.12.2]";
    public static final Logger logger = LogManager.getLogger("RuneCraftory");
    public static final String commonProxy = "com.flemmli97.runecraftory.proxy.CommonProxy";
    public static final String clientProxy = "com.flemmli97.runecraftory.proxy.ClientProxy";

    public static File modDir;
    public static int dimID = 0;
    public static final int guiInfo = 0;
    public static final int guiInfoSub = 1;
    public static final int guiMaking = 2;
    public static final int guiUpgrade = 3;
    public static final int guiResearch = 4;
    public static final int guiShipping = 5;
    public static final int guiShop = 6;
}
