package io.github.flemmli97.runecraftory.common.config;

public class ClientConfig {

    public static int HEALTH_BAR_WIDGET_X = 2;
    public static int HEALTH_BAR_WIDGET_Y = 2;
    public static DisplayPosition HEALTH_BAR_WIDGET_POSITION = DisplayPosition.TOPLEFT;

    public static int SEASON_DISPLAY_X = 0;
    public static int SEASON_DISPLAY_Y = 35;
    public static DisplayPosition SEASON_DISPLAY_POSITION = DisplayPosition.TOPLEFT;

    public static int INVENTORY_OFFSET_X = 79;
    public static int INVENTORY_OFFSET_Y = 47;

    public static int CREATIVE_INVENTORY_OFFSET_X = 20;
    public static int CREATIVE_INVENTORY_OFFSET_Y = 22;

    public static int FARMLAND_X = 2;
    public static int FARMLAND_Y = -2;
    public static DisplayPosition FARMLAND_POSITION = DisplayPosition.BOTTOMLEFT;

    public static HealthRPRenderType RENDER_HEALTH_RP_BAR = HealthRPRenderType.BOTH;
    public static boolean RENDER_CALENDAR = true;
    public static boolean INVENTORY_BUTTON = true;

    public static boolean GRASS_COLOR = true;
    public static boolean FOLIAGE_COLOR = true;

    public enum HealthRPRenderType {
        NONE,
        BOTH,
        RPONLY
    }

    public enum DisplayPosition {

        TOPLEFT,
        TOPMIDDLE,
        TOPRIGHT,
        MIDDLELEFT,
        MIDDLERIGHT,
        BOTTOMLEFT,
        BOTTOMRIGHT;

        public int positionX(int screenWidth, int componentWidth, int componentX) {
            return switch (this) {
                case TOPLEFT, MIDDLELEFT, BOTTOMLEFT -> componentX;
                case TOPMIDDLE, MIDDLERIGHT -> (int) (screenWidth * 0.5 - componentWidth * 0.5) + componentX;
                case TOPRIGHT, BOTTOMRIGHT -> screenWidth - componentWidth + componentX;
            };
        }

        public int positionY(int screenHeight, int componentHeight, int componentY) {
            return switch (this) {
                case TOPLEFT, TOPMIDDLE, TOPRIGHT -> componentY;
                case MIDDLELEFT, MIDDLERIGHT -> (int) (screenHeight * 0.5 - componentHeight * 0.5) + componentY;
                case BOTTOMLEFT, BOTTOMRIGHT -> screenHeight - componentHeight + componentY;
            };
        }
    }
}
