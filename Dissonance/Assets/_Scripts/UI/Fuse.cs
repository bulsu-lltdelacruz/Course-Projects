using UnityEngine;

public class Fuse : Door
{
    public static bool isFuseUnlocked = false;
    public bool OpenDoor(Player player)
    {
        DisableEnemiesHere();
        this.player = player;
        if(interactableType == InteractableType.UnOpenable || 
        interactableType == InteractableType.Locked || 
        interactableType == InteractableType.Unlockable ||
        interactableType == InteractableType.UnlockableFromOtherSide)
        {
            return true;
        }
        if (connectedDoor == null || connectedDoor.spawnPoint == null)
        {
            return false;
        }
        
        if(connectedDoor.interactableType == InteractableType.UnlockableFromOtherSide)
        {
            connectedDoor.interactableType = InteractableType.Unlocked;
            //ShowDialogue();
            return true;
        }     
        StartCoroutine(UIFade.StartLightToDarkTransition(TeleportPlayer));
        return true;
    }
    public void DoOtherFunction()
    {
        Debug.Log("power");
        isFuseUnlocked = true;       
    }
}
