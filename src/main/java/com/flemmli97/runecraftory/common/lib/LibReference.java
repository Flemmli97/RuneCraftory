package com.flemmli97.runecraftory.common.lib;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class LibReference
{
    public static final String MODID = "runecraftory";
    public static final String MODNAME = "RuneCraftory";
    public static final String VERSION = "${@VERSION}";
    public static final Logger logger = LogManager.getLogger(MODNAME);
    public static final String commonProxy = "com.flemmli97.runecraftory.proxy.CommonProxy";
    public static final String clientProxy = "com.flemmli97.runecraftory.proxy.ClientProxy";
    public static final String dependencies = "required:tenshilib;";
	public static final String guiFactory = "com.flemmli97.runecraftory.client.gui.GuiFactory";

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
