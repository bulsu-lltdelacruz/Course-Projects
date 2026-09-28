using System;
using UnityEngine;
using UnityEngine.UIElements;

public class Radio : MonoBehaviour, IPuzzleInteractable, IDataPersistence
{
    [SerializeField] private MedicalWardKeyItemID keyItemID1Cassette;
    [SerializeField] private MedicalWardKeyItemID keyItemID2Battery;
    [SerializeField]private bool isCasetteIn;
    [SerializeField]private bool isBatteryIn;
    [SerializeField]Texture2D[] image;
    [SerializeField]private string[] dialogueWithoutCassette;
    [SerializeField]private string[] dialogueWithoutBattery;
    [SerializeField]private string[] dialogueWithoutBoth;
    [SerializeField]private string[] dialogueUnlocked;
    
    void Start()
    {
        
    }
    public bool CheckKeyItem<T>(T keyID) where T : System.Enum
    {
        if(keyID.Equals(keyItemID1Cassette)||keyID.Equals(keyItemID2Battery))
        {
            if(keyID.Equals(keyItemID1Cassette))
            {
                isCasetteIn = true;
            }else if(keyID.Equals(keyItemID2Battery))
            {
                isBatteryIn = true;
            }
            if(isCasetteIn && isBatteryIn)
            {
                UIDialogueBox.OpenDialogue(dialogueUnlocked,image);
            }
            return true;
        }else
        {
            UIDialogueBox.OpenDialogue(new string[] {"That won't work."});
            return false;
        }

    }


    public void OpenPuzzle()
    {
        if(!isCasetteIn && !isBatteryIn)
        {
            UIDialogueBox.OpenDialogue(dialogueWithoutCassette,image);
        }else if(!isCasetteIn)
        {
            UIDialogueBox.OpenDialogue(dialogueWithoutCassette,image);
        }else if(!isBatteryIn)
        {
            UIDialogueBox.OpenDialogue(dialogueWithoutBattery,image);
        }else
        {
            UIDialogueBox.OpenDialogue(dialogueUnlocked,image);
        }
        
    }

    public void SaveData(ref GameData data)
    {
        data.isRadioCassetteIn = isCasetteIn;
        data.isRadioBatteryIn = isBatteryIn;
    }
    public void LoadData(GameData data)
    {
        isCasetteIn = data.isRadioCassetteIn;
        isBatteryIn = data.isRadioBatteryIn;
    }
}
