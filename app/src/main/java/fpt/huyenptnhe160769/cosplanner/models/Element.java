package fpt.huyenptnhe160769.cosplanner.models;

public class Element {
    private double cost;
    private long fkCos;
    private boolean hasPhoto;
    private long id;
    private String name;
    private String notes;
    private int order;
    private int percent;
    private boolean priority;
    private int timeHH;
    private int timeMM;
    private int type;
    private int weight;

    public Element() {
        this.id = 0;
        this.fkCos = 0;
        this.type = 0;
        this.weight = 50;
        this.name = "";
        this.percent = 0;
        this.cost = 0.0d;
        this.timeHH = 0;
        this.timeMM = 0;
        this.order = 0;
        this.notes = "";
        this.priority = false;
        this.hasPhoto = false;
    }

    public Element(long id2, long fkCos2, int type2, int weight2, String name2, int percent2, double cost2, int timeHH2, int timeMM2, int order2, String notes2, boolean priority2, boolean hasPhoto2) {
        this.id = id2;
        this.fkCos = fkCos2;
        this.type = type2;
        this.weight = weight2;
        this.name = name2;
        this.percent = percent2;
        this.cost = cost2;
        this.timeHH = timeHH2;
        this.timeMM = timeMM2;
        this.order = order2;
        this.notes = notes2;
        this.priority = priority2;
        this.hasPhoto = hasPhoto2;
    }

    public void setId(long id2) {
        this.id = id2;
    }

    public void setFkCos(long fkCos2) {
        this.fkCos = fkCos2;
    }

    public void setType(int type2) {
        this.type = type2;
    }

    public void setWeight(int weight2) {
        this.weight = weight2;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public void setPercent(int percent2) {
        this.percent = percent2;
    }

    public void setCost(double cost2) {
        this.cost = cost2;
    }

    public void setTimeHH(int timeHH2) {
        this.timeHH = timeHH2;
    }

    public void setTimeMM(int timeMM2) {
        this.timeMM = timeMM2;
    }

    public void setOrder(int o) {
        this.order = o;
    }

    public void setNotes(String n) {
        this.notes = n;
    }

    public void setPriority(boolean p) {
        this.priority = p;
    }

    public void setHasPhoto(boolean p) {
        this.hasPhoto = p;
    }

    public long getId() {
        return this.id;
    }

    public long getFkCos() {
        return this.fkCos;
    }

    public int getType() {
        return this.type;
    }

    public int getWeight() {
        return this.weight;
    }

    public String getName() {
        return this.name;
    }

    public int getPercent() {
        return this.percent;
    }

    public double getCost() {
        return this.cost;
    }

    public int getTimeHH() {
        return this.timeHH;
    }

    public int getTimeMM() {
        return this.timeMM;
    }

    public int getOrder() {
        return this.order;
    }

    public String getNotes() {
        return this.notes;
    }

    public boolean isPriority() {
        return this.priority;
    }

    public boolean hasPhoto() {
        return this.hasPhoto;
    }
}
