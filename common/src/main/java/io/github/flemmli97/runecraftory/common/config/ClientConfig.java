package io.github.flemmli97.runecraftory.common.config;

public class ClientConfig {

    public static int healthBarWidgetX = 2;
    public static int healthBarWidgetY = 2;
    public static DisplayPosition healthBarWidgetPosition = DisplayPosition.TOPLEFT;

    public static int seasonDisplayX = 0;
    public static int seasonDisplayY = 35;
    public static DisplayPosition seasonDisplayPosition = DisplayPosition.TOPLEFT;

    public static int inventoryOffsetX = 79;
    public static int inventoryOffsetY = 47;

    public static int creativeInventoryOffsetX = 20;
    public static int creativeInventoryOffsetY = 22;

    public static int farmlandX = 2;
    public static int farmlandY = -2;
    public static DisplayPosition farmlandPosition = DisplayPosition.BOTTOMLEFT;

    public static HealthRPRenderType renderHealthRpBar = HealthRPRenderType.BOTH;
    public static boolean renderCalendar = true;
    public static boolean inventoryButton = true;

    public static boolean grassColor = true;
    public static boolean foliageColor = true;

    public static boolean bossMusic = true;
    public static int bossMusicFadeDelay = 80;

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
