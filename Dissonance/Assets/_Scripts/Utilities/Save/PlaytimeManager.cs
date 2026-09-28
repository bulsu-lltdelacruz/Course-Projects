using UnityEngine;
using TMPro;
using System; // Include if using TextMeshPro

public class PlaytimeManager : MonoBehaviour, IDataPersistence
{
    public static PlaytimeManager instance;
    [NonSerialized]public float totalTime;

    void Awake()
    {
        if (instance == null)
        {
            instance = this;
        }
    }

    void Update()
    {
        totalTime += Time.deltaTime;
    }

    public string getTimerDisplay()
    {
        int hours = Mathf.FloorToInt(totalTime / 3600);
        int minutes = Mathf.FloorToInt((totalTime % 3600) / 60);
        int seconds = Mathf.FloorToInt(totalTime % 60);

        return string.Format("{0:00}:{1:00}:{2:00}", hours, minutes, seconds);
        
    }
    public string getTimerDisplay(float totalTime)
    {
        int hours = Mathf.FloorToInt(totalTime / 3600);
        int minutes = Mathf.FloorToInt((totalTime % 3600) / 60);
        int seconds = Mathf.FloorToInt(totalTime % 60);

        return string.Format("{0:00}:{1:00}:{2:00}", hours, minutes, seconds);
        
    }
    public void LoadData(GameData data)
    {
        instance.totalTime = data.playTime;
    }

    public void SaveData(ref GameData data)
    {
        data.playTime = instance.totalTime;
    }
}
