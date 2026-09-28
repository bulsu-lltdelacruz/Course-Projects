using System.Collections.Generic;
using UnityEngine;

public class EnemyReviveTracker : MonoBehaviour, IDataPersistence
{
    public static EnemyReviveTracker instance;

    private List<EnemyReviveHandler> registeredEnemies = new List<EnemyReviveHandler>();
    private List<EnemyReviveData> pendingLoadData = new List<EnemyReviveData>();

    void Awake()
    {
        if (instance != null) { Destroy(gameObject); return; }
        instance = this;
    }

    public void RegisterEnemy(EnemyReviveHandler handler)
    {
        if (!registeredEnemies.Contains(handler))
        {
            registeredEnemies.Add(handler);
            foreach (var savedEnemy in pendingLoadData)
            {
                if (handler.enemyId == savedEnemy.enemyId)
                {
                    handler.LoadSaveData(savedEnemy);
                    break;
                }
            }
        }
    }

    public void UnregisterEnemy(EnemyReviveHandler handler)
    {
        registeredEnemies.Remove(handler);
    }

    public void OnDoorEntered()
    {
        foreach (var enemy in registeredEnemies)
            enemy.OnDoorEntered();
    }

    public void SaveData(ref GameData data)
    {
        data.enemyReviveStates.Clear();
        foreach (var enemy in registeredEnemies)
            data.enemyReviveStates.Add(enemy.GetSaveData());
    }

    public void LoadData(GameData data)
    {
        pendingLoadData = new List<EnemyReviveData>(data.enemyReviveStates);

        foreach (var savedEnemy in pendingLoadData)
        {
            foreach (var enemy in registeredEnemies)
            {
                if (enemy.enemyId == savedEnemy.enemyId)
                {
                    enemy.LoadSaveData(savedEnemy);
                    break;
                }
            }
        }
    }
}