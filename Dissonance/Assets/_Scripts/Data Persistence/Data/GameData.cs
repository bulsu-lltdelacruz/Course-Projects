using System.Collections.Generic;
using UnityEngine;

[System.Serializable]
public class GameData
{
    public Vector2 playerCoordinates;
    public string locationName;
    public float playTime;
    public Dictionary<string, int> itemBoxItems = new Dictionary<string, int>();
    //name, is collected
    public Dictionary<string, bool> itemsCollected; 
    public Dictionary<string, bool> doorsUnlocked;
    public Dictionary<string, bool> doorsInteracted;
    public Dictionary<string, bool> discoveredLocations;
    //itemslot,(name, amount) parse amount to int
    public Dictionary<int, string[]> inventoryItems;
    public Dictionary<string, string> puzzleSlots;
    public Dictionary<string, bool> animalPuzzleStates;
    
    public Dictionary<string, string> MDComputerContents;
    public Dictionary<string, bool> numLocksUnlocked = new Dictionary<string, bool>();

    //revivve
    public List<EnemyReviveData> enemyReviveStates = new List<EnemyReviveData>();
    
    public bool isPowerOn;
    public string playerNotes = "";
    public int loadedAmmo = 0;
    
    public float currentHp = 100;
    
    public bool isRadioCassetteIn = false;
    public bool isRadioBatteryIn = false;
    public GameData()
    {
        MDComputerContents = new Dictionary<string, string> ();
        itemBoxItems = new Dictionary<string, int>();
        inventoryItems = new Dictionary<int, string[]>();
        itemsCollected = new Dictionary<string, bool>();
        doorsUnlocked = new Dictionary<string, bool>();

        doorsInteracted = new Dictionary<string, bool>();
        discoveredLocations = new Dictionary<string, bool>();

        puzzleSlots = new Dictionary<string, string>();
        animalPuzzleStates = new Dictionary<string, bool>();
        numLocksUnlocked = new Dictionary<string, bool>();
        
        this.locationName = "0";
        this.playTime = 0;
        this.playerCoordinates = new Vector3(0,-2000,0);
        isPowerOn = false;
    }
}
