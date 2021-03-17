package pers.mortal.learn.spring.advancewiring.runtimeinjectvalue;

public class CompactDisc {

    private String title;
    private String artist;

    public CompactDisc(String title, String artist){
        this.title = title;
        this.artist = artist;
    }

    public String getTitle(){
        return this.title;
    }

    public String getArtist(){
        return this.artist;
    }
}
