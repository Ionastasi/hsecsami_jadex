package jammission.world;

public interface IEnvironment{

    public boolean harvestRaspberry(Bear bear, Bush bush);
    public boolean giveAwayRaspberry(Bear bear, Lair lair);

    public boolean getWater(Bearwife bearwife);
    public boolean pourWater(Bearwife bearwife, Lair lair);
    public boolean makeJam(Lair lair);


    public Vision getVision(LocationObject obj);
}