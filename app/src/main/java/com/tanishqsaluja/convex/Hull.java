package com.tanishqsaluja.convex;

/**
 * Created by tanishqsaluja on 11/4/18.
 */

public class Hull {
    private Coordinate [] points;

    public Hull(Coordinate[] points){
        this.points = points;
    }

    public Coordinate [] getPoints(){
        return points;
    }
}
//data class Hull(var points:Coordinate[])