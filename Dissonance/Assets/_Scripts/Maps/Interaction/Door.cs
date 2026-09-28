using System.Collections;
using System.Collections.Generic;
using NaughtyAttributes;
using Unity.Cinemachine;
using UnityEngine;
using UnityEngine.InputSystem;

public class Door : Interactable, IDataPersistence
{
    [SerializeField]
    [HideIf("interactableType", InteractableType.UnOpenable)] public Door connectedDoor;
    [SerializeField] public Transform spawnPoint;
    [SerializeField] private DoorSoundType doorSoundType;    
    [SerializeField]protected string id;
    public bool isInteracted = false;
    private bool isUnlocked = false;
    protected Player player;
    void Start()
    {
        FindAnyObjectByType<MapUI>().SetDoorInteractedByID(id, isInteracted);
    }
    public string GetID()
    {
        return id;
    }
    public bool OpenDoor(Player player)
    {
        //if unlocked, set connected door to unlocked too
        this.player = player;
        if(interactableType == InteractableType.UnOpenable || 
        interactableType == InteractableType.Unlockable ||
        interactableType == InteractableType.UnlockableFromOtherSide)
        {
            return true;
        }
        if(interactableType == InteractableType.Locked || interactableType == InteractableType.UnlockableFromOtherSide)
        {
            FindAnyObjectByType<MapUI>().SetDoorLockedStateByID(id, false);
            if(connectedDoor!=null)
                FindAnyObjectByType<MapUI>().SetDoorLockedStateByID(connectedDoor.id, false);
            return true;
        }
        if (connectedDoor == null || connectedDoor.spawnPoint == null)
        {
            if(gameObject.name != "Final Door")
                return false;
        }
        
        if(gameObject.name != "Final Door" &&connectedDoor.interactableType == InteractableType.UnlockableFromOtherSide)
        {
            FindAnyObjectByType<MapUI>().SetDoorLockedStateByID(id, true);
            FindAnyObjectByType<MapUI>().SetDoorLockedStateByID(connectedDoor.id, true);
            connectedDoor.interactableType = InteractableType.Unlocked;
            UIDialogueBox.OpenDialogue(new string[]{"I unlocked the door from the other side."});
            //ShowDialogue();
            return true;
        }
        if(gameObject.name == "Final Door")
        {
            player.pauseManager.ShowEndScreen();
            Time.timeScale = 0;
            return true;
        }
        DisableEnemiesHere();
        PlayerInputs.DisableAllInputs();
        StartCoroutine(UIFade.StartLightToDarkTransition(TeleportPlayer));
        return true;
    }
    public void SetLockedState(bool isUnlockled)
    {
        FindAnyObjectByType<MapUI>().SetDoorLockedStateByID(id, isUnlockled);
        isInteracted = true;
        if(connectedDoor!= null)
            FindAnyObjectByType<MapUI>().SetDoorLockedStateByID(connectedDoor.id, isUnlockled);
            
    }
    public void SetUnopenable()
    {
        FindAnyObjectByType<MapUI>().SetDoorUnopenableByID(id);
    }
    public void SetInteracted(bool interacted)
    {
        FindAnyObjectByType<MapUI>().SetDoorInteractedByID(id, interacted);
        isInteracted = interacted;
        if(connectedDoor!= null)
        {
            FindAnyObjectByType<MapUI>().SetDoorInteractedByID(connectedDoor.id, interacted);
            connectedDoor.isInteracted = interacted;
        }
    }
  
    public void DoOtherFunction()
    {
        
    }
    public void TeleportPlayer()
    {
        
        Player.followCam.TrackerSettings.PositionDamping = Vector3.zero;
        Player.enteredNewRoom = true;
        player.transform.position = connectedDoor.spawnPoint.position;

        Player.followCam.ForceCameraPosition(
            connectedDoor.spawnPoint.position,
            Player.followCam.transform.rotation);

        StartCoroutine(UIFade.StartDarkToLightTransition());
        EnableEnemiesOnSpawnPoint();
        EnemyReviveTracker.instance?.OnDoorEntered();

        StartCoroutine(ABitDelay());
    }
    IEnumerator ABitDelay()
    {
        float timer = 0f;
         while(timer<1f)
        {
            timer+= Time.deltaTime;
            yield return null;
        }
        Player.followCam.TrackerSettings.PositionDamping = new Vector3(0.5f, 1f, 1f);
        PlayerInputs.EnableAllInputs();
    }
    void EnableEnemiesOnSpawnPoint()
    {
        Spawn spawn = connectedDoor.spawnPoint.GetComponent<Spawn>();
        spawn.EnableEnemies();
    }
    public void DisableEnemiesHere()
    {
        if(!isNotADoor)
        {
            Spawn spawn = spawnPoint.GetComponent<Spawn>();
            spawn.DisableEnemies();
        }
        
    }
    public AudioClip GetOpenDoorSound()
    {
       /* if(doorSoundType.Equals(DoorSoundType.Normal))
        {
            return DoorSoundFXManager.instance.openDoorSound;
        }else if(doorSoundType.Equals(DoorSoundType.ScrewDriver))
        {
            return DoorSoundFXManager.instance.openDoorSound;
        }else if(doorSoundType.Equals(DoorSoundType.WireCutter))
        {
            return DoorSoundFXManager.instance.openDoorSound;
        }*/
        if(doorSoundType.Equals(DoorSoundType.Elevator))
        {
            return DoorSoundFXManager.instance.elevator;
        }
            return DoorSoundFXManager.instance.openDoorSound;
    }
    public AudioClip GetOpenLockedDoorSound()
    {
       /* if(doorSoundType.Equals(DoorSoundType.Normal))
        {
            return DoorSoundFXManager.instance.openLockedDoorSound;
        }
        else if(doorSoundType.Equals(DoorSoundType.ScrewDriver))
        {
            return DoorSoundFXManager.instance.openLockedDoorSound;
        }
        else if(doorSoundType.Equals(DoorSoundType.WireCutter))
        {
            return DoorSoundFXManager.instance.openLockedDoorSound;
        }*/
        if(!doorSoundType.Equals(DoorSoundType.Normal))
            return null;
        return DoorSoundFXManager.instance.openLockedDoorSound;
    }
    public AudioClip GetUnlockDoorSound()
    {
        if(doorSoundType.Equals(DoorSoundType.Normal))
        {
            return DoorSoundFXManager.instance.unlockDoorSound;
        }else if(doorSoundType.Equals(DoorSoundType.ScrewDriver))
        {
            return DoorSoundFXManager.instance.unScrewSound;
        }else if(doorSoundType.Equals(DoorSoundType.WireCutter))
        {
            return DoorSoundFXManager.instance.cutWireSound;
        }
        else if(doorSoundType.Equals(DoorSoundType.Elevator))
        {
            return DoorSoundFXManager.instance.elevator;
        }
            return DoorSoundFXManager.instance.unlockDoorSound;
    } 
    public float GetVolume()
    {
        if(id == "MD_PatientRoomHallway_Room2_FromHole")
        {
            return 0f;
        }
        if(doorSoundType.Equals(DoorSoundType.Normal))
        {
            return S_VolumeStats.unlockDoor;
        }else if(doorSoundType.Equals(DoorSoundType.ScrewDriver))
        {
            return S_VolumeStats.screwdriver;
        }else if(doorSoundType.Equals(DoorSoundType.WireCutter))
        {
            return S_VolumeStats.wirecutter;
        }
        else if(doorSoundType.Equals(DoorSoundType.Elevator))
        {
            return 3;
        }
            return S_VolumeStats.unlockDoor;
    }
    void OnTriggerExit2D(Collider2D other)
    {
        
    }

    public virtual void LoadData(GameData data)
    {
        //other doorunlocked is setting it bruh
        Dictionary<string, bool> saveInfo           = data.doorsUnlocked;
        Dictionary<string, bool> doorsInteractedInfo = data.doorsInteracted;
        //SetInteracted(true);
        if(this is Fuse)
        {
            Fuse.isFuseUnlocked = data.isPowerOn;
        }
        bool isDoorUnlocked = false;

        if (saveInfo.ContainsKey(id) && saveInfo[id])
        {
            if (this is not AnimalPuzzle) // Assuming AnimalPuzzle exists
            {
                interactableType = InteractableType.Unlocked;
                isDoorUnlocked   = true;
            }
            if (shouldChangeTileMapAfterUnlock) // Assuming from base class
            {
                ChangeTile(); 
                isDoorUnlocked = false;
            }
        }
        DoOtherFunction();
        




        if (!doorsInteractedInfo.ContainsKey(id))//pag wala dine, bye
        {
            return;
        }
        isInteracted = doorsInteractedInfo[id];
        if(!isInteracted) //pag dipa interact, wala
        {
            return;
        }



        if (this is AnimalPuzzle)
        {
            DoOtherFunction();
            return;
        }
        if(connectedDoor!=null && connectedDoor.interactableType.Equals(InteractableType.Locked))
            SetLockedState(false);
        else
        {
            SetLockedState(isDoorUnlocked);
        }
        if (interactableType == InteractableType.UnOpenable)
        {
            SetUnopenable();
        }
/*
        if (!isInteracted)
        {
            //SetInteracted(false); 
            FindAnyObjectByType<MapUI>().SetDoorInteractedByID(id, false);
            return;
        }

        if (this is AnimalPuzzle)
        {
            DoOtherFunction();
            return;
        }

        if (interactableType == InteractableType.UnOpenable)
        {
            SetUnopenable();
        }
        else
        {
            FindAnyObjectByType<MapUI>().SetDoorLockedStateByID(id, isDoorUnlocked);
            //SetLockedState(isDoorUnlocked);
        }*/

        DoOtherFunction();
    }

    public virtual void SaveData(ref GameData data)
    {
        if(this is Fuse)
        {
            data.isPowerOn = Fuse.isFuseUnlocked;
        }
        if(data.doorsUnlocked.ContainsKey(id))
        {
            data.doorsUnlocked.Remove(id);
        }
        if(data.doorsInteracted.ContainsKey(id))
        {
            data.doorsInteracted.Remove(id);
        }
        bool isUnlocked = (interactableType.Equals(InteractableType.Unlocked))?true:false;
        data.doorsUnlocked.Add(id, isUnlocked);
        data.doorsInteracted.Add(id, isInteracted);
    }
}
public enum DoorSoundType
{
    Normal,
    ScrewDriver,
    WireCutter,
    Elevator
}
