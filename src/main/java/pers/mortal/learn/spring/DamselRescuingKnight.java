package pers.mortal.learn.spring;

public class DamselRescuingKnight implements Knight {
    private RescueDamselQuest quest;

    public DamselRescuingKnight() {
        this.quest = new RescueDamselQuest();//与RescueDamselQuest高度耦合。
    }

    public void embarkOnQuest() {
        quest.embark();//难以测试：需要先测试RescueDamselQuest.embark正确，且要求保证测试embarkOnQuest，embark一定是正确调用。
    }
}
