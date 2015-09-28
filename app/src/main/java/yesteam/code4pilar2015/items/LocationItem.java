package yesteam.code4pilar2015.items;

/**
 * Created by Luis on 28/09/2015.
 */
public class LocationItem {
    private int _id;
    private String name, code;
    private Double lat, lng;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }
}
