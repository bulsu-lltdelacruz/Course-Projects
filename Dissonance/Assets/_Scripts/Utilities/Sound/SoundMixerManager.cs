using UnityEngine;
using UnityEngine.Audio;

public class SoundMixerManager : MonoBehaviour
{
    [SerializeField] AudioMixer audioMixer;

    public void SetMasterVolume(float level)
    {
        level = Mathf.Max(level, 0.0001f);
        audioMixer.SetFloat("masterVolume", Mathf.Log10(level) * 20f);
    } 
    
    public void SetSoundFXVolume(float level)
    {
        level = Mathf.Max(level, 0.0001f);
        audioMixer.SetFloat("soundFXVolume", Mathf.Log10(level) * 20f);
    } 
    
    public void SetMusicVolume(float level)
    {
        level = Mathf.Max(level, 0.0001f);
        audioMixer.SetFloat("musicVolume", Mathf.Log10(level) * 20f);
    }

    public void SetMenuVolume(float level)
    {
        level = Mathf.Max(level, 0.0001f);
        audioMixer.SetFloat("menuVolume", Mathf.Log10(level) * 20f); 
    }
}