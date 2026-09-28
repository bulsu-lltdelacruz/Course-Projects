using System;
using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.InputSystem;
using UnityEngine.SceneManagement;
using UnityEngine.UIElements;

public class PauseManager : MonoBehaviour
{
    Player player;
    [SerializeField]UIDocument settingMenu;
    [SerializeField]UIDocument deathScreen;
    [SerializeField]SoundMixerManager soundMixer;
    VisualElement rootSettings;
    VisualElement rootDeathScreen;
    
    //Death Screen
    Button returnToMenu;

    VisualElement PARENT_deathScreen;
    VisualElement PARENT_setting;
    VisualElement PARENT_settingSound;
    VisualElement PARENT_settingControls;

    Button SETTINGS_sound;
    Button SETTINGS_controls;
    Button SETTINGS_return;
    Button SETTINGS_returnToMenu;
    
    //sound settings
    Button SOUND_return;
    Slider SOUND_SLIDER_master;
    Slider SOUND_SLIDER_SFX;
    Slider SOUND_SLIDER_Music;
    Slider SOUND_SLIDER_Menu;
    
    //controls settings
    Button CONTROLS_return;
    public bool isPaused = false;
    
    void Start()
    {
        player = GetComponent<Player>();
        
        //roots
        rootSettings = settingMenu.rootVisualElement;
        rootDeathScreen = deathScreen.rootVisualElement;

        returnToMenu = rootDeathScreen.Q<Button>("return-to-main-menu");
        
        SETTINGS_returnToMenu = rootSettings.Q<Button>("return-to-menu");
        SETTINGS_returnToMenu.style.display = DisplayStyle.Flex;

        PARENT_deathScreen = rootDeathScreen.Q<VisualElement>("parent");
        PARENT_setting = rootSettings.Q<VisualElement>("setting-parent");
        PARENT_settingSound = rootSettings.Q<VisualElement>("setting-sound-parent");
        PARENT_settingControls = rootSettings.Q<VisualElement>("setting-controls-parent");
        
        //setting menu buttons
        SETTINGS_sound = rootSettings.Q<Button>("sound");
        SETTINGS_controls = rootSettings.Q<Button>("controls");
        SETTINGS_return = rootSettings.Q<Button>("return");

        //setting sounds
        SOUND_return = rootSettings.Q<Button>("return-sound");
        //setting controls
        CONTROLS_return = rootSettings.Q<Button>("return-controls");
        
        SOUND_SLIDER_master = rootSettings.Q<Slider>("master-slider");
        SOUND_SLIDER_SFX= rootSettings.Q<Slider>("soundFX-slider");
        SOUND_SLIDER_Music = rootSettings.Q<Slider>("music-slider");
        SOUND_SLIDER_Menu = rootSettings.Q<Slider>("menu-slider");

        AddListeners();
        DisablePickingMode();
        
        // --- LOAD SAVED SETTINGS ---
        LoadSettings();
    }
    
    void AddListeners()
    {
        SETTINGS_sound.clicked+=OnSoundSettings;
        SETTINGS_controls.clicked+=OnControlsSettings;
        SETTINGS_return.clicked+=OnReturn;
        SOUND_return.clicked+=OnReturnFromSound;
        CONTROLS_return.clicked+=OnReturnFromControls;
        SETTINGS_returnToMenu.clicked+=OnReturnToMenu;
        returnToMenu.clicked+=OnReturnToMenu;

        

        SETTINGS_sound.RegisterCallback<MouseEnterEvent>(OnMouseOver);
        SETTINGS_controls.RegisterCallback<MouseEnterEvent>(OnMouseOver);
        SETTINGS_return.RegisterCallback<MouseEnterEvent>(OnMouseOver);
        SOUND_return.RegisterCallback<MouseEnterEvent>(OnMouseOver);
        CONTROLS_return.RegisterCallback<MouseEnterEvent>(OnMouseOver);
        SETTINGS_returnToMenu.RegisterCallback<MouseEnterEvent>(OnMouseOver);
        returnToMenu.RegisterCallback<MouseEnterEvent>(OnMouseOver);
        SETTINGS_sound.RegisterCallback<MouseEnterEvent>(OnMouseOver);

        SOUND_SLIDER_master.RegisterValueChangedCallback(OnMasterVolumeChanged);
        SOUND_SLIDER_SFX.RegisterValueChangedCallback(OnSFXVolumeChanged);
        SOUND_SLIDER_Music.RegisterValueChangedCallback(OnMusicVolumeChanged);
        SOUND_SLIDER_Menu.RegisterValueChangedCallback(OnMenuVolumeChanged);
    }
    
    void OnDestroy()
    {
        SETTINGS_sound.clicked-=OnSoundSettings;
        SETTINGS_controls.clicked-=OnControlsSettings;
        SETTINGS_return.clicked-=OnReturn;
        SOUND_return.clicked-=OnReturnFromSound;
        CONTROLS_return.clicked-=OnReturnFromControls;
        SETTINGS_returnToMenu.clicked-=OnReturnToMenu;
        returnToMenu.clicked-=OnReturnToMenu;
        SETTINGS_sound.clicked-=OnSoundSettings;

        SETTINGS_sound.UnregisterCallback<MouseEnterEvent>(OnMouseOver);
        SETTINGS_controls.UnregisterCallback<MouseEnterEvent>(OnMouseOver);
        SETTINGS_return.UnregisterCallback<MouseEnterEvent>(OnMouseOver);
        SOUND_return.UnregisterCallback<MouseEnterEvent>(OnMouseOver);
        CONTROLS_return.UnregisterCallback<MouseEnterEvent>(OnMouseOver);
        SETTINGS_returnToMenu.UnregisterCallback<MouseEnterEvent>(OnMouseOver);
        returnToMenu.UnregisterCallback<MouseEnterEvent>(OnMouseOver);
        SETTINGS_sound.UnregisterCallback<MouseEnterEvent>(OnMouseOver);
        
        SETTINGS_returnToMenu.style.display = DisplayStyle.None;
        
        SOUND_SLIDER_master.UnregisterValueChangedCallback(OnMasterVolumeChanged);
        SOUND_SLIDER_SFX.UnregisterValueChangedCallback(OnSFXVolumeChanged);
        SOUND_SLIDER_Music.UnregisterValueChangedCallback(OnMusicVolumeChanged);
        SOUND_SLIDER_Menu.UnregisterValueChangedCallback(OnMenuVolumeChanged);
    }
    private void OnMouseOver(MouseEnterEvent evt)
    {
        PlayerSounds.instance.PlayHoverButton(S_VolumeStats.buttonHover);
    }

    // --- LOADING LOGIC ---
    private void LoadSettings()
    {
        // Get floats from PlayerPrefs. If they don't exist yet, default to 1f (max volume).
        // Using SetValueWithoutNotify so it doesn't accidentally trigger the change events before the scene is ready.
        SOUND_SLIDER_master.SetValueWithoutNotify(PlayerPrefs.GetFloat("MasterVol", 1f));
        SOUND_SLIDER_SFX.SetValueWithoutNotify(PlayerPrefs.GetFloat("SFXVol", 1f));
        SOUND_SLIDER_Music.SetValueWithoutNotify(PlayerPrefs.GetFloat("MusicVol", 1f));
        SOUND_SLIDER_Menu.SetValueWithoutNotify(PlayerPrefs.GetFloat("MenuVol", 1f));

        // Immediately apply these loaded values to the Audio Mixer
        soundMixer.SetMasterVolume(SOUND_SLIDER_master.value);
        soundMixer.SetSoundFXVolume(SOUND_SLIDER_SFX.value);
        soundMixer.SetMusicVolume(SOUND_SLIDER_Music.value);
        soundMixer.SetMenuVolume(SOUND_SLIDER_Menu.value);
    }

    // --- SAVING LOGIC IN CALLBACKS ---
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

    public void OnPause()
    {
        AddDisplay(PARENT_setting);
        isPaused = true;
    }
    
    public void OnReturn()
    {
        // When leaving the menu, we ensure preferences are written to disk
        PlayerPrefs.Save(); 
        
        RemoveDisplay(PARENT_setting);
        isPaused = false;
        PlayerInputs.UI.Disable();
        PlayerInputs.Player.Enable();
        player.input.isUI = false;
        Time.timeScale = 1f;
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
    
    private void OnReturnToMenu()
    {
        PlayerPrefs.Save(); // Save on quit
        
        RemoveDisplay(PARENT_setting);
        isPaused = false;
        PlayerInputs.UI.Disable();
        PlayerInputs.Player.Enable();
        player.input.isUI = false;
        Time.timeScale = 1f;
        Initializer.isInGame = false;
        StartCoroutine(UIFade.MenuStartLightToDarkTransition(()=>SceneManager.LoadScene("MainMenuScene")));
    }

    private void RemoveDisplay(VisualElement v)
    {
        v.style.display = DisplayStyle.None;
    }
    
    private void AddDisplay(VisualElement v)
    {
        v.style.display = DisplayStyle.Flex;
    }
    
    public void ShowDeathScreen()
    {
        PARENT_deathScreen.style.display = DisplayStyle.Flex;
        PARENT_deathScreen.style.opacity = 1f;
    }
    public void ShowEndScreen()
    {
        ShowDeathScreen();
        PARENT_deathScreen.Q<Label>("title").text = "Demo Finished";
    }
    void DisablePickingMode()
    {
        rootSettings.Query(className: "crt").ForEach(el => {
            el.pickingMode = PickingMode.Ignore;
        });
        rootDeathScreen.Query(className: "crt").ForEach(el => {
            el.pickingMode = PickingMode.Ignore;
        });
    }
}