package uk.colessoft.android.hilllist.utility;

public class DistanceCalculator {

	private static double Radius=6371D;

	// R = earth's radius (mean radius = 6,371km)
	// Constructor
	DistanceCalculator(double R) {
		Radius = R;
	}

	public static double calculationByDistance(double lat1, double lat2, double lon1, double lon2) {

		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
				* Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return Radius * c;
	}

	public static double calculationByDistanceSimple(double lat1,double lat2,double lon1,double lon2){
		return Math.sqrt((lat2 - lat1) * (lat2 - lat1) + (lon2 - lon1) * (lon2 - lon1)) * Radius;
	}
}
