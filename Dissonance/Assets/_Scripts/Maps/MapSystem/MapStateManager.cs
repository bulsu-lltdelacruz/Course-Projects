using System;
using NaughtyAttributes;
using UnityEngine;

public class MapStateManager : MonoBehaviour, IDataPersistence
{
    public static MapStateManager instance;
    [ShowNonSerializedField]public string currentLocation;
    [ShowNonSerializedField]public FloorLocation currentFloor;
    void Awake()
    {
        if(instance == null)
            instance = this;
    }
    public void LoadData(GameData data)
    {
        instance.currentLocation = data.locationName;
    }

    public void SaveData(ref GameData data)
    {
        data.locationName = instance.currentLocation;
    }    
}
