using System.Collections.Generic;
using UnityEngine;

public class AnimalPuzzleConnection : MonoBehaviour, IDataPersistence
{
    [SerializeField] private string id; // Don't forget to generate a unique ID in the Inspector!

    public bool isEagleConnected;
    public bool isLionConnected;
    public bool isMonkeyConnected;
    public bool isRabbitConnected;
    public bool isTigerConnected;
    public bool isBearConnected;
    
    void Awake()
    {
    
    }

    public void UpdateKeys(string keyName, bool isCorrect)
    {
        switch(keyName)
        {
            case "Eagle Key": isEagleConnected = isCorrect; break;
            case "Lion Key": isLionConnected = isCorrect; break;
            case "Monkey Key": isMonkeyConnected = isCorrect; break;
            case "Rabbit Key": isRabbitConnected = isCorrect; break;
            case "Tiger Key": isTigerConnected = isCorrect; break;
            case "Bear Key": isBearConnected = isCorrect; break;
        }

        if(isEagleConnected && 
            isLionConnected &&
            isMonkeyConnected &&
            isRabbitConnected &&
            isTigerConnected &&
            isBearConnected)
        {
            Door door = GetComponent<Door>();
            
            // SAFETY CHECK: Only show dialogue if the door isn't ALREADY unlocked.
            // This prevents the dialogue from popping up when you load a finished puzzle!
            if (door.interactableType != InteractableType.Unlocked)
            {
                door.interactableType = InteractableType.Unlocked;
                UIDialogueBox.OpenDialogue(new string[]{"...", "It seems I unlocked something"});
            }
        }
    }

    public void LoadData(GameData data)
    {
        // Try to grab each animal's boolean state. If it exists, set it.
        if (data.animalPuzzleStates.TryGetValue(id + "_Eagle", out bool eagle)) isEagleConnected = eagle;
        if (data.animalPuzzleStates.TryGetValue(id + "_Lion", out bool lion)) isLionConnected = lion;
        if (data.animalPuzzleStates.TryGetValue(id + "_Monkey", out bool monkey)) isMonkeyConnected = monkey;
        if (data.animalPuzzleStates.TryGetValue(id + "_Rabbit", out bool rabbit)) isRabbitConnected = rabbit;
        if (data.animalPuzzleStates.TryGetValue(id + "_Tiger", out bool tiger)) isTigerConnected = tiger;
        if (data.animalPuzzleStates.TryGetValue(id + "_Bear", out bool bear)) isBearConnected = bear;
    }

    public void SaveData(ref GameData data)
    {
        // Save each state into the dictionary using the unique ID + Animal Name
        data.animalPuzzleStates[id + "_Eagle"] = isEagleConnected;
        data.animalPuzzleStates[id + "_Lion"] = isLionConnected;
        data.animalPuzzleStates[id + "_Monkey"] = isMonkeyConnected;
        data.animalPuzzleStates[id + "_Rabbit"] = isRabbitConnected;
        data.animalPuzzleStates[id + "_Tiger"] = isTigerConnected;
        data.animalPuzzleStates[id + "_Bear"] = isBearConnected;
    }
}