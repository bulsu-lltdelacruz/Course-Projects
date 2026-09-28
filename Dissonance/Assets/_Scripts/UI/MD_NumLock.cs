using System.Collections.Generic;
using UnityEngine;

public class MD_NumLock : MonoBehaviour, IPuzzleInteractable, IDataPersistence
{
    [SerializeField] private string password;
    [SerializeField] private Door door;
    [SerializeField] private string id;
    [SerializeField] private Texture2D logo;
    
    private bool isUnlocked = false;

    void Start()
    {
       // door.SetLockedState(false);
    }
    public void OpenPuzzle()
    {
        door.SetInteracted(true);
        FindAnyObjectByType<MapUI>().SetDoorInteractedByID(door.GetID(), true);
        door.connectedDoor.isInteracted = true;

        if(door.interactableType == InteractableType.Unlocked)
        {
            door.SetLockedState(true);
        }else if(door.interactableType == InteractableType.Locked)
        {
            door.SetLockedState(false);
        }
        if (!Fuse.isFuseUnlocked)
        {
            UIDialogueBox.OpenDialogue(new string[] { "I can't operate the Door Lock due to the lack of power" });
            return;
        }

        // Tell the manager to open and what to do if the password is correct
        NumLockUIManager.Instance.Open(password, UnlockSuccess);
        NumLockUIManager.Instance.ChangeLogo(logo);
    }

    private void UnlockSuccess()
    {
        isUnlocked = true;
        door.gameObject.SetActive(true);
        door.interactableType = InteractableType.Unlocked;
        door.SetLockedState(true);
        
        gameObject.SetActive(false); 
    }

    public void LoadData(GameData data)
    {
        if (data.numLocksUnlocked.TryGetValue(id, out bool savedStatus))
        {
            if (savedStatus) UnlockSuccess();
        }
    }

    public void SaveData(ref GameData data)
    {
        data.numLocksUnlocked[id] = isUnlocked;
    }
}