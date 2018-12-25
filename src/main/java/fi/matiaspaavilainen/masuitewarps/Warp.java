package fi.matiaspaavilainen.masuitewarps;

public class Warp {

    private String name;
    private Boolean hidden;
    private Boolean global;

    public Warp() {
    }

    public Warp(String name, Boolean global, Boolean hidden) {
        this.name = name;
        this.global = global;
        this.hidden = hidden;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean isGlobal() {
        return global;
    }

    public void setGlobal(Boolean global) {
        this.global = global;
    }
}
