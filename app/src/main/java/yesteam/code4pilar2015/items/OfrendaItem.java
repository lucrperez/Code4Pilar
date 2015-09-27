package yesteam.code4pilar2015.items;

/**
 * Created by Luis on 27/09/2015.
 */
public class OfrendaItem {
    private int ID;
    private String name, meet_hour;

    public OfrendaItem(int ID, String name, String meet_hour, int access) {
        this.ID = ID;
        this.name = name;
        this.meet_hour = meet_hour;
        this.access = access;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeet_hour() {
        return meet_hour;
    }

    public void setMeet_hour(String meet_hour) {
        this.meet_hour = meet_hour;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    private int access;
}
