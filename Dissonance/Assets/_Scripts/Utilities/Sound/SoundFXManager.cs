using UnityEngine;

public class SoundFXManager : MonoBehaviour
{
    public static SoundFXManager instance;
    [SerializeField]  private AudioSource soundFXObject;
    [SerializeField]  private AudioSource menuFXObject;
    private bool isQuitting = false;

    void OnApplicationQuit() {
        isQuitting = true;
    }

    void Awake()
    {
        if(instance == null)
            instance = this;
    }

    public void PlaySoundFXClip(AudioClip audioClip, Transform spawnTransform, float volume, string source)
    {
        if(isQuitting)
            return;
        AudioSource audioSource = null;
        if(source == "soundFX")
        {
            audioSource = Instantiate(soundFXObject, spawnTransform.position, Quaternion.identity);
        }else if(source == "menuFX")
        {
            audioSource = Instantiate(menuFXObject, spawnTransform.position, Quaternion.identity);
        }else if(source == "musicFX")
        {
            audioSource = Instantiate(soundFXObject, spawnTransform.position, Quaternion.identity);
        }
        
        
        audioSource.clip = audioClip;
        audioSource.volume = volume;
        audioSource.Play();
        float clipLength = audioSource.clip.length;
        
        Destroy(audioSource.gameObject, clipLength);
    }
}
