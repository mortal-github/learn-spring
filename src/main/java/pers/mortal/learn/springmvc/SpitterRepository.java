package pers.mortal.learn.springmvc;

public interface SpitterRepository {
    void save(Spitter spitter);
    Spitter findByUserName(String userName);
}
