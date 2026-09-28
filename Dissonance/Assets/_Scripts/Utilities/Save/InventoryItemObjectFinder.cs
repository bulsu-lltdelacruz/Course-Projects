using System.Collections.Generic;
using UnityEngine;

public class ItemObjectFinder : MonoBehaviour
{
    /*InventoryObject pistol;
    InventoryObject MD_eastHallwayKey;
    InventoryObject MD_pharmacyRoomKey;
    InventoryObject MD_screwDriver;
    InventoryObject MD_wireCutter;*/
    [SerializeField] private List<ItemObject> itemObjects;
    public static ItemObjectFinder instance;

    void Awake()
    {
        if(instance == null)
            instance = this;
    }
    public ItemObject FindItemObjectByName(string name)
    {
        foreach(ItemObject itemObject in itemObjects)
        {
            if(itemObject.name.Equals(name))
                return itemObject;
        }
        return null;
    }
    
}
