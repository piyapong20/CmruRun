package devded.silkyland.cmrurun;

/**
 * Created by silkyland on 6/28/16 AD.
 */
public class MyData {
    // Explicit
    private int[] avatarInts = new int[]{R.drawable.bird48,
            R.drawable.doremon48, R.drawable.kon48,
            R.drawable.nobita48, R.drawable.rat48 };

    private double[] latStationDoubles = new double[]{18.807656,  18.807900, 18.806036, 18.805579};
    private double[] lngStationDoubles = new double[]{98.985281, 98.987223, 98.987666, 98.985533};

    private int[] iconStationInts = new int[]{R.drawable.build1, R.drawable.build2,
            R.drawable.build3, R.drawable.build4};

    public int[] getIconStationInts() {
        return iconStationInts;
    }

    public double[] getLatStationDoubles() {
        return latStationDoubles;
    }

    public double[] getLngStationDoubles() {
        return lngStationDoubles;
    }

    public int[] getAvatarInts() {
        return avatarInts;
    }
} // Main Class
