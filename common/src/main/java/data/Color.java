package data;

/**
 * Color can be chosen by user
 */
public enum Color {
    RED,
    BLACK,
    WHITE;

    /**
     * Generates a beautiful list of enum string values.
     * @return String with all enum values splitted bt comma.
     */
    public static String nameList(){
        String nameList = "";
        for (Color color : values()){
            nameList += color.name() + ", ";
        }
        return nameList.substring(0, nameList.length()-2);
    }
}
