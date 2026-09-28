using System;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.InputSystem;
using UnityEngine.SceneManagement;
using UnityEngine.UIElements;

[Serializable]
public struct NewGameDialogueEntry
{
    [TextArea(2, 5)]
    public string dialogue;
    public Texture2D photo;
}

public class MenuManager : MonoBehaviour
{
    [Header("New Game")]
    [SerializeField] string gameSceneName = "GameScene";
    [SerializeField] NewGameDialogueEntry[] newGameIntro;

    [SerializeField]UIDocument mainMenu;
    [SerializeField]UIDocument settingMenu;
    [SerializeField]UIDocument saveMenu;
    [SerializeField]SoundMixerManager soundMixer;
    VisualElement rootMenu;
    VisualElement rootSettings;
    VisualElement rootSave;

    
    VisualElement PARENT_mainMenu;
    VisualElement PARENT_saveMenu;
    //save
    Button SAVE_return;

    //settings
    VisualElement PARENT_setting;
    VisualElement PARENT_settingSound;
    VisualElement PARENT_settingControls;

    Button MAINMENU_newGame;
    Button MAINMENU_loadGame;
    Button MAINMENU_settings;
    Button SETTINGS_sound;
    Button SETTINGS_controls;
    Button SETTINGS_return;
    
    //sound settings
    Button SOUND_return;
    Slider SOUND_SLIDER_master;
    Slider SOUND_SLIDER_SFX;
    Slider SOUND_SLIDER_Music;
    Slider SOUND_SLIDER_Menu;
    //sound settings
    //controls settings
    Button CONTROLS_return;
    //controls settings
    void Start()
    {
        //roots
        rootMenu = mainMenu.rootVisualElement;
        rootSettings = settingMenu.rootVisualElement;
        rootSave = saveMenu.rootVisualElement;

        //setting menu parents
        PARENT_mainMenu= rootMenu.Q<VisualElement>("parent");
        //save menu parents
        PARENT_saveMenu= rootSave.Q<VisualElement>("parent");

        PARENT_setting = rootSettings.Q<VisualElement>("setting-parent");
        PARENT_settingSound = rootSettings.Q<VisualElement>("setting-sound-parent");
        PARENT_settingControls = rootSettings.Q<VisualElement>("setting-controls-parent");

        //main menu buttons
        MAINMENU_newGame = rootMenu.Q<Button>("new-game");
        MAINMENU_loadGame = rootMenu.Q<Button>("load-game");
        MAINMENU_settings = rootMenu.Q<Button>("settings");

        //save menu buttons
        SAVE_return = rootSave.Q<Button>("exit");
        
        //setting menu buttons
        SETTINGS_sound = rootSettings.Q<Button>("sound");
        SETTINGS_controls = rootSettings.Q<Button>("controls");
        SETTINGS_return = rootSettings.Q<Button>("return");

        //setting sounds
        SOUND_return = rootSettings.Q<Button>("return-sound");
        //setting controls
        CONTROLS_return = rootSettings.Q<Button>("return-controls");

        AddListeners();
        DisablePickingMode();
    }
    void AddListeners()
    {
        MAINMENU_newGame.clicked+=OnNewGame;
        MAINMENU_loadGame.clicked+=OnLoadGame;
        SAVE_return.clicked+=OnReturnFromLoad;
        MAINMENU_settings.clicked+=OnSettings;
        SETTINGS_sound.clicked+=OnSoundSettings;
        SETTINGS_controls.clicked+=OnControlsSettings;
        SETTINGS_return.clicked+=OnReturnFromSettingOption;
        //sound settings
        SOUND_return.clicked+=OnReturnFromSound;
        CONTROLS_return.clicked+=OnReturnFromControls;

        

        SETTINGS_sound.RegisterCallback<MouseEnterEvent>(OnMouseOver);
        SETTINGS_controls.RegisterCallback<MouseEnterEvent>(OnMouseOver);
        SETTINGS_return.RegisterCallback<MouseEnterEvent>(OnMouseOver);
        SOUND_return.RegisterCallback<MouseEnterEvent>(OnMouseOver);
        CONTROLS_return.RegisterCallback<MouseEnterEvent>(OnMouseOver);
        SETTINGS_sound.RegisterCallback<MouseEnterEvent>(OnMouseOver);
        
        MAINMENU_newGame.RegisterCallback<MouseEnterEvent>(OnMouseOver);
        MAINMENU_loadGame.RegisterCallback<MouseEnterEvent>(OnMouseOver);
        SAVE_return.RegisterCallback<MouseEnterEvent>(OnMouseOver);
        MAINMENU_settings.RegisterCallback<MouseEnterEvent>(OnMouseOver);
        //sound settings
    }
    void OnDestroy()
    {
        MAINMENU_newGame.clicked-=OnNewGame;
        MAINMENU_loadGame.clicked-=OnLoadGame;
        SAVE_return.clicked-=OnReturnFromLoad;
        MAINMENU_settings.clicked-=OnSettings;
        SETTINGS_sound.clicked-=OnSoundSettings;
        SETTINGS_controls.clicked-=OnControlsSettings;
        SETTINGS_return.clicked-=OnReturnFromSettingOption;
        //sound settings
        SOUND_return.clicked-=OnReturnFromSound;
        CONTROLS_return.clicked-=OnReturnFromControls;
        

        SETTINGS_sound.UnregisterCallback<MouseEnterEvent>(OnMouseOver);
        SETTINGS_controls.UnregisterCallback<MouseEnterEvent>(OnMouseOver);
        SETTINGS_return.UnregisterCallback<MouseEnterEvent>(OnMouseOver);
        SOUND_return.UnregisterCallback<MouseEnterEvent>(OnMouseOver);
        CONTROLS_return.UnregisterCallback<MouseEnterEvent>(OnMouseOver);
        SETTINGS_sound.UnregisterCallback<MouseEnterEvent>(OnMouseOver);

        MAINMENU_newGame.UnregisterCallback<MouseEnterEvent>(OnMouseOver);
        MAINMENU_loadGame.UnregisterCallback<MouseEnterEvent>(OnMouseOver);
        SAVE_return.UnregisterCallback<MouseEnterEvent>(OnMouseOver);
        MAINMENU_settings.UnregisterCallback<MouseEnterEvent>(OnMouseOver);
        //sound settings
    }
        private void OnMouseOver(MouseEnterEvent evt)
    {
        PlayerSounds.instance.PlayHoverButton(S_VolumeStats.buttonHover);
    }
    private void OnMasterVolumeChanged(ChangeEvent<float> evt)
    {
        soundMixer.SetMasterVolume(evt.newValue);
        PlayerPrefs.SetFloat("MasterVol", evt.newValue); // Save
    }

    private void OnSFXVolumeChanged(ChangeEvent<float> evt)
    {
        soundMixer.SetSoundFXVolume(evt.newValue);
        PlayerPrefs.SetFloat("SFXVol", evt.newValue); // Save
    }

    private void OnMusicVolumeChanged(ChangeEvent<float> evt)
    {
        soundMixer.SetMusicVolume(evt.newValue);
        PlayerPrefs.SetFloat("MusicVol", evt.newValue); // Save
    }

    private void OnMenuVolumeChanged(ChangeEvent<float> evt)
    {
        soundMixer.SetMenuVolume(evt.newValue);
        PlayerPrefs.SetFloat("MenuVol", evt.newValue); // Save
    }

    private void OnNewGame()
    {
        Initializer.loadFileIndex = -1;
        QueueNewGameIntro();
        StartCoroutine(UIFade.StartLightToDarkTransition(()=>SceneManager.LoadScene(gameSceneName)));
    }
    private void OnLoadGame()
    {
        RemoveDisplay(PARENT_mainMenu);
        AddDisplay(PARENT_saveMenu);
    }
    private void OnSettings()
    {
        RemoveDisplay(PARENT_mainMenu);
        AddDisplay(PARENT_setting);
    }
    private void OnSoundSettings()
    {
        RemoveDisplay(PARENT_setting);
        AddDisplay(PARENT_settingSound);
    }    
    private void OnControlsSettings()
    {
        RemoveDisplay(PARENT_setting);
        AddDisplay(PARENT_settingControls);
    }
    private void OnReturnFromSettingOption()
    {
        RemoveDisplay(PARENT_setting);
        AddDisplay(PARENT_mainMenu);
    }
    private void OnReturnFromSound()
    {
        RemoveDisplay(PARENT_settingSound);
        AddDisplay(PARENT_setting);
    }
    private void OnReturnFromControls()
    {
        RemoveDisplay(PARENT_settingControls);
        AddDisplay(PARENT_setting);
    }
    private void OnReturnFromLoad()
    {
        RemoveDisplay(PARENT_saveMenu);
        AddDisplay(PARENT_mainMenu);
    }



    /// ///////////
    private void RemoveDisplay(VisualElement v)
    {
        v.style.display = DisplayStyle.None;
    }
    private void AddDisplay(VisualElement v)
    {
        v.style.display = DisplayStyle.Flex;
    }

    private void QueueNewGameIntro()
    {
        if (newGameIntro == null || newGameIntro.Length == 0)
        {
            Initializer.QueueNewGameIntro(null, null);
            return;
        }

        List<string> dialogueContents = new List<string>();
        List<Texture2D> imageContents = new List<Texture2D>();

        foreach (NewGameDialogueEntry entry in newGameIntro)
        {
            if (string.IsNullOrWhiteSpace(entry.dialogue) && entry.photo == null)
                continue;

            dialogueContents.Add(entry.dialogue ?? string.Empty);
            imageContents.Add(entry.photo);
        }

        if (dialogueContents.Count == 0)
        {
            Initializer.QueueNewGameIntro(null, null);
            return;
        }

        Initializer.QueueNewGameIntro(dialogueContents.ToArray(), imageContents.ToArray());
    }
    
    void DisablePickingMode()
    {
        rootMenu.Query(className: "crt").ForEach(el => {
            el.pickingMode = PickingMode.Ignore;
        });
        rootSettings.Query(className: "crt").ForEach(el => {
            el.pickingMode = PickingMode.Ignore;
        });
    }
}
