using System;
using System.Collections.Generic;
using UnityEngine;

[CreateAssetMenu(fileName = "ItemBoxObject", menuName = "Scriptable Objects/InventorySystem/ItemBox")]
public class ItemBoxObject : ScriptableObject, IDataPersistence
{
    public List<ItemBoxEntry> storedItems = new List<ItemBoxEntry>();
    

    public void StoreItem(ItemObject item, int amount)
    {
        foreach (ItemBoxEntry entry in storedItems)
        {
            if (entry.itemName == item.name)
            {
                entry.amount += amount;
                return;
            }
        }
        storedItems.Add(new ItemBoxEntry(item.name, amount));
    }

    public bool TakeItem(string itemName, int amount, out ItemObject item)
    {
        item = null;
        foreach (ItemBoxEntry entry in storedItems)
        {
            if (entry.itemName == itemName)
            {
                item = ItemObjectFinder.instance.FindItemObjectByName(itemName);
                entry.amount -= amount;
                if (entry.amount <= 0)
                    storedItems.Remove(entry);
                return true;
            }
        }
        return false;
    }

    public void SaveData(ref GameData data)
    {
        data.itemBoxItems.Clear();
        foreach (ItemBoxEntry entry in storedItems)
        {
            data.itemBoxItems[entry.itemName] = entry.amount;
        }
            
    }

    public void LoadData(GameData data)
    {
        storedItems.Clear();
        foreach (var kvp in data.itemBoxItems)
        {
            storedItems.Add(new ItemBoxEntry(kvp.Key, kvp.Value));
        }
            
    }
}

[Serializable]
public class ItemBoxEntry
{
    public string itemName;
    public int amount;
    public ItemBoxEntry(string name, int amount)
    {
        this.itemName = name;
        this.amount = amount;
    }
}