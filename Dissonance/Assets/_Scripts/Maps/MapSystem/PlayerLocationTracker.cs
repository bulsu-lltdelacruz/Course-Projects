using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerLocationTracker : MonoBehaviour, IDataPersistence
{
    [SerializeField] private string id;
    [SerializeField] private string LocationDisplayName;
    [SerializeField] private FloorLocation floorLocation;
    //DONOT USE THIS
    [SerializeField] public Enemy[] enemiesInside;
    /// ////////
    [SerializeField] public GameObject[] lightsInside;
    
    private bool isDiscovered = false;
    private Coroutine disableCoroutine;
    private MapUI mapUI;

    void Awake()
    {
        mapUI = FindAnyObjectByType<MapUI>();
        DisableEnemiesHere();
    }

    void Start()
    {
        if (mapUI != null) mapUI.SetLocationVisibilityByID(id, isDiscovered);
    }

    void OnTriggerEnter2D(Collider2D collision)
    {
        if (!collision.CompareTag("Player")) return;

        EnableEnemiesOnSpawnPoint();
        isDiscovered = true;
        
        if (mapUI != null)
        {
            mapUI.SetLocationVisibilityByID(id, isDiscovered);
            mapUI.SetPlayerInLocation(id, true);
        }

        if (!String.IsNullOrEmpty(LocationDisplayName))
            MapStateManager.instance.currentLocation = LocationDisplayName;
            
        MapStateManager.instance.currentFloor = floorLocation;
    }

    void OnTriggerExit2D(Collider2D collision)
    {
        if (!collision.CompareTag("Player")) return;
        
        if (mapUI != null) mapUI.SetPlayerInLocation(id, false);
        DisableEnemiesHere();
    }

    void EnableEnemiesOnSpawnPoint()
    {
        if (disableCoroutine != null)
        {
            StopCoroutine(disableCoroutine);
            disableCoroutine = null;
        }

        if (enemiesInside != null)
        {
            foreach (Enemy enemy in enemiesInside)
            {
                if (enemy == null) continue;
                enemy.gameObject.SetActive(true);
            }
        }
        if (lightsInside != null)
        {
            foreach (GameObject light in lightsInside)
            {
                if (light == null) continue;
                light.gameObject.SetActive(true);
            }
        }
    }

    public void DisableEnemiesHere()
    {
        if (lightsInside != null)
        {
            foreach (GameObject light in lightsInside)
            {
                if (light == null) continue;
                light.gameObject.SetActive(false);
            }
        }
        if (enemiesInside != null && lightsInside != null)
        {
            if (disableCoroutine != null) StopCoroutine(disableCoroutine);
            disableCoroutine = StartCoroutine(DisableAfterTime());
        }
    }

    IEnumerator DisableAfterTime()
    {
        yield return new WaitForSeconds(0.5f); 

        foreach (Enemy enemy in enemiesInside)
        {
            if (enemy == null) continue;
            if (enemy.isAlive)
                enemy.gameObject.SetActive(false);
        }

        
    }
    

    public void LoadData(GameData data)
    {
        if (data.discoveredLocations.TryGetValue(id, out bool discovered))
        {
            isDiscovered = discovered;
            if (mapUI != null) mapUI.SetLocationVisibilityByID(id, isDiscovered);
        }
    }

    public void SaveData(ref GameData data)
    {
        if (data.discoveredLocations.ContainsKey(id))
        {
            data.discoveredLocations.Remove(id);
        }
        data.discoveredLocations.Add(id, isDiscovered);
    }
}

public enum FloorLocation
{
    _1stFloor,
    _2ndFloor
}