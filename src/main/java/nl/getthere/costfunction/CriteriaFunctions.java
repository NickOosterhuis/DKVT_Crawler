package nl.getthere.costfunction;

import java.awt.*;

public class CriteriaFunctions {

    public static boolean overlap(Rectangle r1, Rectangle r2) {
        System.out.println("X: coordinate: R1: " + r1.x + " R2: " + r2.x);
        System.out.println("Y: coordinate: R1: " + r1.y + " R2: " + r2.y);
        System.out.println("WIDTH: R1: " + r1.width + " R2" + r2.width);
        System.out.println("HEIGHT: R1 " + r1.height + " R2 " + r2.height);
        System.out.println();

        return r1.x < r2.x + r2.width && r1.x + r1.width > r2.x && r1.y < r2.y + r2.height && r1.y + r1.height > r2.y;
    }


}
