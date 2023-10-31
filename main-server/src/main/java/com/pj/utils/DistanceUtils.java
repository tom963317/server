package com.pj.utils;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

public class DistanceUtils {
    /**
     * 默认地球半径
     */
    private final static double EARTH_RADIUS = 6371000;//赤道半径(单位m)

    /**
     * 转化为弧度(rad)
     * */
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }
    /**
     * @param lon1 第一点的精度
     * @param lat1 第一点的纬度
     * @param lon2 第二点的精度
     * @param lat2 第二点的纬度
     * @return 返回的距离，单位m
     * */
    public static double getDistance(double lon1,double lat1,double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }
    public static double distance(double lon1,double lat1,double lon2, double lat2) {
        double d2;
        boolean z;
        double d3 = 0;
        GlobalCoordinates globalCoordinates = new GlobalCoordinates(lat2, lon2);
        GlobalCoordinates globalCoordinates2 = new GlobalCoordinates(lat1, lon1);
        Ellipsoid ellipsoid = Ellipsoid.Sphere;
        double semiMajorAxis = ellipsoid.getSemiMajorAxis();
        double semiMinorAxis = ellipsoid.getSemiMinorAxis();
        double flattening = ellipsoid.getFlattening();
        double latitude = globalCoordinates.getLatitude() * 0.017453292519943295d;
        double latitude2 = globalCoordinates2.getLatitude() * 0.017453292519943295d;
        double d4 = semiMinorAxis * semiMinorAxis;
        double d5 = ((semiMajorAxis * semiMajorAxis) - d4) / d4;
        double longitude = (globalCoordinates2.getLongitude() * 0.017453292519943295d) - (globalCoordinates.getLongitude() * 0.017453292519943295d);
        double d6 = 1.0d - flattening;
        double atan = Math.atan(Math.tan(latitude) * d6);
        double sin = Math.sin(atan);
        double cos = Math.cos(atan);
        double atan2 = Math.atan(Math.tan(latitude2) * d6);
        double sin2 = Math.sin(atan2);
        double cos2 = Math.cos(atan2);
        double d7 = sin * sin2;
        double d8 = sin2 * cos;
        double d9 = sin * cos2;
        double d10 = cos * cos2;
        double d11 = 0.0d;
        double d12 = longitude;
        double d13 = 0.0d;
        double d14 = 0.0d;
        double d15 = 0.0d;
        int i2 = 0;
        while (true) {
            d2 = cos;
            if (i2 >= 20) {
                z = false;
                break;
            }
            double sin3 = Math.sin(d12);
            double cos3 = Math.cos(d12);
            double d16 = d8 - (d9 * cos3);
            double d17 = (d16 * d16) + (cos2 * sin3 * cos2 * sin3);
            double sqrt = Math.sqrt(d17);
            double d18 = longitude;
            double d19 = (cos3 * d10) + d7;
            double atan22 = Math.atan2(sqrt, d19);
            double d20 = d17 == 0.0d ? 0.0d : (sin3 * d10) / sqrt;
            double cos4 = Math.cos(Math.asin(d20));
            double d21 = cos4 * cos4;
            double d22 = d21 == 0.0d ? 0.0d : d19 - ((d7 * 2.0d) / d21);
            double d23 = d21 * d5;
            double d24 = ((((((320.0d - (175.0d * d23)) * d23) - 768.0d) * d23) + 4096.0d) * (d23 / 16384.0d)) + 1.0d;
            double d25 = (((((74.0d - (47.0d * d23)) * d23) - 128.0d) * d23) + 256.0d) * (d23 / 1024.0d);
            double d26 = (2.0d * (d22 * d22)) - 1.0d;
            double d27 = ((((d19 * d26) - (((d3 * 4.0d) - 3.0d) * (((d17 * 4.0d) - 3.0d) * ((d25 / 6.0d) * d22)))) * (d25 / 4.0d)) + d22) * d25 * sqrt;
            double d28 = (((4.0d - (d21 * 3.0d)) * flattening) + 4.0d) * (flattening / 16.0d) * d21;
            double d29 = (((((d28 * d19 * d26) + d22) * sqrt * d28) + atan22) * (1.0d - d28) * flattening * d20) + d18;
            double abs = Math.abs((d29 - d12) / d29);
            if (i2 > 1 && abs < 1.0E-13d) {
                z = true;
                d13 = atan22;
                d12 = d29;
                d14 = d27;
                d15 = d24;
                break;
            }
            i2++;
            d13 = atan22;
            d12 = d29;
            cos = d2;
            d14 = d27;
            longitude = d18;
            d15 = d24;
        }
        double d30 = (d13 - d14) * semiMinorAxis * d15;
        double d31 = Double.NaN;
        if (z) {
            double atan23 = Math.atan2(Math.sin(d12) * cos2, d8 - (Math.cos(d12) * d9));
            if (atan23 < 0.0d) {
                atan23 += 6.283185307179586d;
            }
            double d32 = atan23 / 0.017453292519943295d;
            double atan24 = Math.atan2(Math.sin(d12) * d2, (Math.cos(d12) * d8) + (-d9)) + 3.141592653589793d;
            if (atan24 < 0.0d) {
                atan24 += 6.283185307179586d;
            }
            d31 = atan24 / 0.017453292519943295d;
            d11 = d32;
        } else if (latitude > latitude2) {
            d31 = 0.0d;
            d11 = 180.0d;
        } else if (latitude < latitude2) {
            d31 = 180.0d;
        } else {
            d11 = Double.NaN;
        }
        if (d11 >= 360.0d) {
            d11 -= 360.0d;
        }
        double d33 = d11;
        if (d31 >= 360.0d) {
            d31 -= 360.0d;
        }
        return new GeodeticCurve(d30, d33, d31).getEllipsoidalDistance();
    }

}
