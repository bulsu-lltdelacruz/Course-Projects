using System;
using NaughtyAttributes;
using UnityEngine;
using UnityEngine.Tilemaps;

public class Interactable : IInteractable
{
    
    [SerializeField] public ItemPickUpType itemPickUpType;
    //[ShowIf("ShouldShowTexture2D")] public Texture2D image;
    [ShowIf("ShouldShowDialogue")] public string[] dialogue;
    [ShowIf("itemPickUpType", ItemPickUpType.PhotoAndDialogueAndChoices)] public string[] endingDialogueAfterChoosingPositive;//for choices
    [ShowIf("itemPickUpType", ItemPickUpType.PhotoAndDialogueAndChoices)]  public string[] endingDialogueAfterChoosingNegative;//for choices
    [ShowIf("ShouldShowDialogue")] public string[] endingDialogueAfterUnlocking;//for after naunlock
    [ShowIf("ShouldShowDialogue")] public Texture2D endingPhotoAfterUnlocking;//for after naunlock
    [SerializeField] public bool shouldChangeTileMapAfterUnlock;
    [SerializeField] public bool isMultiplePhotos;
    [ShowIf("ShouldShowDialogue")] public Texture2D[] images;//for after naunlock
    [ShowIf("shouldChangeTileMapAfterUnlock")] public Tilemap targetTilemap;
    [ShowIf("shouldChangeTileMapAfterUnlock")] public TileBase newTile;
    [ShowIf("shouldChangeTileMapAfterUnlock")] public Vector3 tileToReplaceCoordinate;
    [ShowIf("interactableType", InteractableType.LockedAndHidesOtherItem)] public GameObject hiddenItem;//for after naunlock
    [ShowIf("itemPickUpType", ItemPickUpType.PhotoAndDialogueAndChoices)] public Choices[] choices;
    [ShowIf("itemPickUpType", ItemPickUpType.PhotoAndDialogueAndChoices)] public string[] choiceString;

    public bool ChangeTile()
    {
        if(shouldChangeTileMapAfterUnlock)
        {
            Vector3Int coordinate = targetTilemap.WorldToCell(tileToReplaceCoordinate);
            targetTilemap.SetTile(coordinate, newTile);
            return true;
        }
        return false;
    }
    bool ShouldShowTexture2D()
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
    }
    
    /*public bool ShowDialogue()
    {
        try
        {
            UIDialogueBox.OpenDialogue(dialogue);
        }catch(Exception)
        {
            return false;
        }
        return true;
    }*/
}
