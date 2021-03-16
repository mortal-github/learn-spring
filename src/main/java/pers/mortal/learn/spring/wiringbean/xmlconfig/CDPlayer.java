package pers.mortal.learn.spring.wiringbean.xmlconfig;

public class CDPlayer implements MediaPlayer {
    private CompactDisc cd;

    public CDPlayer(CompactDisc cd){
        this.cd = cd;
    }

    public void setCompactDisc(CompactDisc cd){
        this.cd = cd;
    }

    public void play() {
        cd.play();
    }

}
