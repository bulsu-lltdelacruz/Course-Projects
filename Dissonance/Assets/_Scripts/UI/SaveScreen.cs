using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UIElements;

public class SaveScreen : MonoBehaviour
{
    [SerializeField]UIDocument saveDoc;
    [SerializeField]bool isSaveMode;
    [SerializeField]bool isInPlayer;
    public Button SAVE_return;
    VisualElement root;
    public VisualElement parent;
    ScrollView saveParent;
    SaveSlot[] allSaveSlots;
    Player player;
    public static float playTime = 0;
    void Start()
    {
        player = GetComponent<Player>();
        root = saveDoc.rootVisualElement;
        
        parent= root.Q<VisualElement>("parent");
        saveParent= root.Q<ScrollView>("save-parent");
        SAVE_return = root.Q<Button>("exit");
        allSaveSlots = new SaveSlot[10];
        for(int i =0; i< allSaveSlots.Length; i++)
        {
            allSaveSlots[i] = new SaveSlot();
            saveParent.Add(allSaveSlots[i]);
        }
        //add the save files
        for(int i =0; i< allSaveSlots.Length; i++)
        {
            int index = i;
            GameData data = DataPersistenceManager.instance.GetLoadGameDataAtIndex(index);
            
            
            if(data == null)//if walang file, mkeep it empty
            {
                
            }
            else
            {
                if(isSaveMode)
                    allSaveSlots[index].AddToSaveSlotSaveMode(data);
                else
                    allSaveSlots[index].AddToSaveSlot(data);
            }
        }
        if(isSaveMode)
        {
            SaveMode();
        }
        else
        {
            LoadMode();
        }
        disablePickingMode();
        if(isInPlayer)
            player.saveScreen.SAVE_return.clicked+=player.input.CloseSaveMenu;
    }
    void SaveMode()
    {
        for(int i =0; i< allSaveSlots.Length; i++)
        {
            int index = i;
            allSaveSlots[i].RegisterCallback<MouseEnterEvent>(OnMouseOver);
            allSaveSlots[i].RegisterCallback<ClickEvent>(e =>
            {
                DataPersistenceManager.instance.SaveGame(index);
                allSaveSlots[index].AddToSaveSlot(DataPersistenceManager.instance.GetLoadGameDataAtIndex(index));
                PlayerSounds.instance.PlayClickButton(S_VolumeStats.buttonClick);
            });
        }
    }
    void LoadMode()
    {
         for(int i =0; i< allSaveSlots.Length; i++)
        {
            int index = i;
            allSaveSlots[i].RegisterCallback<MouseEnterEvent>(OnMouseOver);

            allSaveSlots[i].RegisterCallback<ClickEvent>(e =>
            {
                PlayerSounds.instance.PlayClickButton(S_VolumeStats.buttonClick);
                StartCoroutine(UIFade.StartLightToDarkTransition(()=>
                {SceneManager.LoadScene("GameScene");
                Initializer.loadFileIndex = index;}));
                Initializer.isInGame = true;
            });
        }
    }
    void OnDisable()
    {
        for(int i =0; i< allSaveSlots.Length; i++)
        {
            saveParent.Clear();
        }
        if(isInPlayer)
            player.saveScreen.SAVE_return.clicked-=player.input.CloseSaveMenu;
    }
    void OnDestroy()
    {
        for(int i =0; i< allSaveSlots.Length; i++)
        {
            allSaveSlots[i].UnregisterCallback<MouseEnterEvent>(OnMouseOver);
        }
    }
    private void OnMouseOver(MouseEnterEvent evt)
    {
        PlayerSounds.instance.PlayHoverButton(S_VolumeStats.buttonHover);
    }

    void disablePickingMode()
    {
        root.Query(className: "crt").ForEach(el => {
            el.pickingMode = PickingMode.Ignore;
        });
    }
}
public class SaveSlot : VisualElement
{
    Label location = new Label();
    Label timePlayed = new Label();
    public SaveSlot()
    {
        AddToClassList("empty-save-slot");
        location.AddToClassList("save-location");
        timePlayed.AddToClassList("save-time");

        location.text = "data.location";
        timePlayed.text = "data.playTime";

        timePlayed.style.opacity = 0;
        location.style.opacity = 0;

        Add(location);
        Add(timePlayed);
    }
    public void AddToSaveSlot(GameData data)
    {
        RemoveFromClassList("empty-save-slot");
        AddToClassList("save-slot");

        location.text = data.locationName;
        timePlayed.text = PlaytimeManager.instance.getTimerDisplay(data.playTime);

        timePlayed.style.opacity = 1;
        location.style.opacity = 1;
        
    }
      public void AddToSaveSlotSaveMode(GameData data)
    {
        RemoveFromClassList("empty-save-slot");
        AddToClassList("save-slot");

        location.text = data.locationName;
        timePlayed.text = PlaytimeManager.instance.getTimerDisplay(data.playTime);

        timePlayed.style.opacity = 1;
        location.style.opacity = 1;
        
    }
}
