using System;
using UnityEngine;

public class AnimalPuzzle : Door
{
    [NonSerialized]public ItemObject keyInSlot;
    [SerializeField]ItemObject keyItemNeeded;
    [SerializeField]Sprite keyInSlotSprite;
    [SerializeField]Sprite keyNotInSlotSprite;
    
    [SerializeField]Texture2D[] imageNoKey;
    [SerializeField]Texture2D[] imageYesKey;
    string keyInSlotName;
    [SerializeField]AnimalPuzzleConnection doorToUnlock;
    SpriteRenderer spriteRenderer;
    void Awake()
    {
        spriteRenderer = GetComponent<SpriteRenderer>();

        images = imageNoKey;
        if(keyItemNeeded.name == "Rabbit Key" || keyItemNeeded.name == "Eagle Key")
        {
            PutKeyInSlot(keyItemNeeded);
        }
        
    }
    public bool OpenDoor(Player player)
    {
        //only gets called if teheres a key in slot
        this.player = player;
        if (connectedDoor == null || connectedDoor.spawnPoint == null)
        {
            return false;
        }
        return true;
    }
    public bool PutKeyInSlot(ItemObject itemObject)
    {
        if(keyInSlot!= null)
        {
            return false;
        }
        keyInSlot = itemObject;
        keyInSlotName = keyInSlot.name;

        if(spriteRenderer != null && keyInSlotSprite != null)
            spriteRenderer.sprite = keyInSlotSprite;

        itemPickUpType = ItemPickUpType.PhotoAndDialogueAndChoices;
        string choiceStr = "Should I take the "+keyInSlotName+"?";
        dialogue = new string[]{choiceStr,choiceStr};
        choices = new Choices[]{Choices.pickUp, Choices.leave};
        choiceString = new string[]{"Take the "+keyInSlotName+"", "Leave it"};
        images = imageYesKey;
        
        doorToUnlock.UpdateKeys(keyItemNeeded.name, keyInSlot.name.Trim() == keyItemNeeded.name.Trim());
        //Debug.Log("keyneed: "+ keyItemNeeded.name.Trim()+",,,,,   key has :"+keyInSlot.name.Trim() + "   "+(keyInSlot.name.Trim() == keyItemNeeded.name.Trim()));
        return true;
        //images = 
        //change photo
        //make it unlocked// actually dont, since if its unlocked handleinteraction just opens the door without showing the UI.
    }
    public void RemoveKeyInSlot()
    {

        if(spriteRenderer != null && keyNotInSlotSprite != null)
            spriteRenderer.sprite = keyNotInSlotSprite;
        itemPickUpType = ItemPickUpType.PhotoAndDialogue;
        if(keyItemNeeded.name == "Rabbit Key")
            dialogue = new string[]{"Theres a strange keyhole here", "I can vaugely read the word \"Rabbit\""};
        else if(keyItemNeeded.name == "Eagle Key")
            dialogue = new string[]{"Theres a strange keyhole here", "I can vaugely read the word \"Eagle\""};
        else
            dialogue = new string[]{"Theres a strange keyhole here"};
        keyInSlot = null;
        images = imageNoKey;
        doorToUnlock.UpdateKeys(keyItemNeeded.name, false);
    }
    public override void SaveData(ref GameData data)
    {
        // First, call the base Door logic to save the 'isUnlocked' state
        base.SaveData(ref data);

        // Save the specific key currently in the slot
        if (data.puzzleSlots.ContainsKey(id))
        {
            data.puzzleSlots.Remove(id);
        }

        // If there's a key in the slot, save its name; otherwise, save empty
        string nameToSave = (keyInSlot != null) ? keyInSlot.name : "";
        data.puzzleSlots.Add(id, nameToSave);
    }

    public override void LoadData(GameData data)
    {
        // First, call the base Door logic
        base.LoadData(data);

        if (data.puzzleSlots.TryGetValue(id, out string savedItemName))
        {
            if (!string.IsNullOrEmpty(savedItemName))
            {
                // Use your existing ItemObjectFinder to get the ScriptableObject reference
                ItemObject item = ItemObjectFinder.instance.FindItemObjectByName(savedItemName);
                if (item != null)
                {
                    PutKeyInSlot(item);
                }
            }
            else
            {
                RemoveKeyInSlot();
            }
        }
    }
    public bool CheckKeyItem<T>(T keyID) where T : System.Enum
    {
        if(keyID.Equals(keyItemID))
        {
            //dont make it ubnlokced
            return true;
        }
        return false;
    }
}
