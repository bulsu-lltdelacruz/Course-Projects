using NaughtyAttributes;
using UnityEngine;

public class IInteractable: MonoBehaviour
{
    //[SerializeField][TextArea(3, 10)]protected string[] dialogueContents;
    [SerializeField] public InteractableType interactableType;
    public bool isNotADoor = false;
    [ShowIf("ShouldShowKeyItemID")] public MedicalWardKeyItemID keyItemID;

    public bool CheckKeyItem<T>(T keyID) where T : System.Enum
    {
        if(keyID.Equals(keyItemID))
        {
            if(keyItemID!= MedicalWardKeyItemID.AnimalKeys)
                interactableType = InteractableType.Unlocked;
            if(this is Door door && !door.isNotADoor && door.connectedDoor!=null)
                door.connectedDoor.interactableType = InteractableType.Unlocked;
            return true;
        }
        return false;
    }
    bool ShouldShowKeyItemID()
    {
        return (!interactableType.Equals(InteractableType.Dialogue)&&
        !interactableType.Equals(InteractableType.UnOpenable)&&
        !interactableType.Equals(InteractableType.Unlocked)&&
        !interactableType.Equals(InteractableType.UnlockableFromOtherSide));
    }
}

