package pers.mortal.learn.spring;

public class BraveKnight implements Knight {
    private Quest quest;

    public BraveKnight(Quest quest) {// Quest被注入进来
        this.quest = quest;
    }

    public void embarkOnQuest() {
        quest.embark();//测试简单，只需在构造对象时传入一个mock对象就可，该虚拟对象的embark()一定争取调用。
    }
}
