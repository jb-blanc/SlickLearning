package fr.angemaster.slick.utils;

public class MathUtils {

    /**
     * Retrieve angle between center of an image and another point on the screen
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static float getRotation(float x1, float y1, float x2, float y2){
        //Formule : α = arccos[(b² + c² − a²) ÷ 2bc]
        float[] p1 = {x1,y1};
        float[] p2 = {x2,y2};
        float[] p3 = {x1+100,y1};

        double a = getDistance(p2[0],p2[1],p3[0],p3[1]);
        double b = getDistance(p1[0],p1[1],p3[0],p3[1]);
        double c = getDistance(p1[0],p1[1],p2[0],p2[1]);


        float result = ((float)Math.acos((Math.pow(b,2)+Math.pow(c,2)-Math.pow(a,2))/(2*b*c)));
        if(y1<y2){
            result = (float)((180 * result/Math.PI));
        }
        else{
            result = (float)(360 - (180 * result/Math.PI));
        }

        return result;
    }

    /**
     * Retrieve distance between 2 point
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static double getDistance(float x1, float y1, float x2, float y2){
        return Math.sqrt((Math.pow(x2-x1,2))+(Math.pow(y2-y1,2)));
    }

}
