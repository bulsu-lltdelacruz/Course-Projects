using UnityEngine;
using System.Linq;
using System.Collections.Generic;
using System;
public class DataPersistenceManager : MonoBehaviour
{
    [Header("File Storage Config")]
    
    [SerializeField] private string fileName;
    [SerializeField] private Player player;
    public static DataPersistenceManager instance{get; private set;}
    [NonSerialized]public GameData gameData;
    private List<IDataPersistence> dataPersistencesObjects;
    [SerializeField] private List<ScriptableObject> scriptableDataObjects;
    private FileDataHandler dataHandler;
    void Awake()
    {
        if(instance != null)
        {
            Debug.LogError("more than one data persistence manager");
        }
        instance = this;
        dataHandler = new FileDataHandler(Application.persistentDataPath, fileName);
    }
    void Start()
    {
        this.dataPersistencesObjects = FindAllDataPersistenceObjects();
        foreach (var so in scriptableDataObjects)
        {
            if (so is IDataPersistence dataPersistence)
            {
                dataPersistencesObjects.Add(dataPersistence);
            }
            else
            {
                Debug.LogWarning($"{so.name} does not implement IDataPersistence!");
            }
        }
        if(player !=  null)
            dataPersistencesObjects.Add(player.inventory);
        if(Initializer.loadFileIndex !=-1)
            LoadGame(Initializer.loadFileIndex);
        else
            NewGame();
    }
    public void NewGame()
    {
        this.gameData = new GameData();
        foreach (IDataPersistence dataPersistence in dataPersistencesObjects)
        {
            dataPersistence.LoadData(gameData);
        }
    }
    public void LoadGame(int index)
    {
        this.gameData = dataHandler.Load(index);

        if(this.gameData == null)
        {
            NewGame();
        }
        foreach(IDataPersistence dataPersistence in dataPersistencesObjects)
        {
            dataPersistence.LoadData(gameData);
        }
    }
    public GameData GetLoadGameDataAtIndex(int index)
    {
        GameData data = dataHandler.Load(index);
        return data;
    }
    
    public void SaveGame(int index)
    {
        foreach(IDataPersistence dataPersistence in dataPersistencesObjects)
        {
            dataPersistence.SaveData(ref gameData);
        }
        dataHandler.Save(gameData, index);
    }
    
    void OnApplicationQuit()
    {
        //SaveGame(); fuck auto save bruh
    }

    private List <IDataPersistence> FindAllDataPersistenceObjects()
    {
        IEnumerable<IDataPersistence> dataPersistenceObjects = FindObjectsOfType<MonoBehaviour>().OfType<IDataPersistence>();

        return new List<IDataPersistence>(dataPersistenceObjects);
    }
}
