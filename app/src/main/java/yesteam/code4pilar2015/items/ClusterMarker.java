package yesteam.code4pilar2015.items;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ClusterMarker implements ClusterItem {

    private LatLng position;
    private String name, code;

    public ClusterMarker(double latitude, double longitude, String name, String code) {
        this.position = new LatLng(latitude, longitude);
        this.name = name;
        this.code = code;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
