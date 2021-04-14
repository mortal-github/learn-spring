package pers.mortal.learn.springdata.cache;

public class CacheData {
    private static int ID = 1;
    private int id;
    private boolean condition;
    private boolean unless;

    public CacheData(){}

    public CacheData(boolean condition, boolean unless){
        this.id = ID;
        this.condition = condition;
        this.unless = unless;
        ID++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCondition() {
        return condition;
    }

    public void setCondition(boolean condition) {
        this.condition = condition;
    }

    public boolean isUnless() {
        return unless;
    }

    public void setUnless(boolean unless) {
        this.unless = unless;
    }
}
