using System;
using System.IO;
using Newtonsoft.Json;
using UnityEngine;

public class FileDataHandler
{
    private string dataDirPath = "";
    private string dataFileName = "";

    public FileDataHandler(string dataDirPath, string dataFileName)
    {
        this.dataDirPath = dataDirPath;
        this.dataFileName = dataFileName;
    }
    public GameData Load(int index)
    {
        string fullPath = Path.Combine(dataDirPath, dataFileName+index);
        GameData loadedData = null;
        if(File.Exists(fullPath))
        {
            try
            {
                string dataToLoad = "";
                using(FileStream stream = new FileStream(fullPath, FileMode.Open))
                {
                    using(StreamReader reader = new StreamReader(stream))
                    {
                        dataToLoad = reader.ReadToEnd();
                    }
                }
                loadedData = JsonConvert.DeserializeObject<GameData>(dataToLoad);

            }catch(Exception e)
            {
                Debug.LogError("cant save to " + fullPath);
            }
        }
        return loadedData;
    }
    public void Save(GameData data, int index)
    {
        string fullPath = Path.Combine(dataDirPath, dataFileName+index);
        try
        {
            Directory.CreateDirectory(Path.GetDirectoryName(fullPath));
            string dataToStore = JsonConvert.SerializeObject(data, Formatting.Indented, new JsonSerializerSettings 
            { 
                ReferenceLoopHandling = ReferenceLoopHandling.Ignore 
            });
            using(FileStream stream = new FileStream(fullPath, FileMode.Create))
            {
                using(StreamWriter writer = new StreamWriter(stream))
                {
                    writer.Write(dataToStore);
                }
            }

        }catch(Exception e)
        {
            Debug.LogError("cant save to " + fullPath + "because " + e.Message);
        }
    }
}
