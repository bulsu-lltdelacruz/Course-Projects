using System;
using System.Collections.Generic;
using NaughtyAttributes;
using UnityEngine;

public class Item : Interactable, IDataPersistence
{
    [SerializeField] public ItemObject itemObject;
    [SerializeField] private string id;
    [SerializeField] public int amount = 1;
    private bool isTaken = false;

    public void LoadData(GameData data)
    {
        Dictionary<string, bool> saveInfo = data.itemsCollected;
        if(saveInfo.ContainsKey(id))
            if(saveInfo[id])//if its taken
            {
                DestroySelf();
            }
    }

    public void SaveData(ref GameData data)
    {
        if(data.itemsCollected.ContainsKey(id))
        {
            data.itemsCollected.Remove(id);
        }
        data.itemsCollected.Add(id, isTaken);
    }
    public void DestroySelf()
    {
        gameObject.SetActive(false);
        isTaken = true;
    }
    /*[SerializeField] public ItemPickUpType itemPickUpType;
[ShowIf("ShouldShowTexture2D")] public Texture2D image;
[ShowIf("ShouldShowDialogue")] public string[] dialogue;*/
    //[ShowIf("ShouldShowDialogue")] public string[] endingDialogue;
    /*[ShowIf("itemPickUpType", ItemPickUpType.PhotoAndDialogueAndChoices)] public Choices[] choices;
    [ShowIf("itemPickUpType", ItemPickUpType.PhotoAndDialogueAndChoices)] public string[] choiceString;*/
    /*bool ShouldShowTexture2D()
    {
        return itemPickUpType.Equals(ItemPickUpType.Photo) || 
        itemPickUpType.Equals(ItemPickUpType.PhotoAndDialogue)||
        itemPickUpType.Equals(ItemPickUpType.PhotoAndDialogueAndChoices);
    }
    bool ShouldShowDialogue()
    {
        return itemPickUpType.Equals(ItemPickUpType.PhotoAndDialogue)||
        itemPickUpType.Equals(ItemPickUpType.Dialogue)||
        itemPickUpType.Equals(ItemPickUpType.PhotoAndDialogueAndChoices);
    }*/

}
public enum ItemPickUpType
{
    ShowNothing,
    Dialogue,
    Photo,
    PhotoAndDialogue,
    PhotoAndDialogueAndChoices
}

