using UnityEngine;

public class PlayerSounds : MonoBehaviour
{
    public static PlayerSounds instance;
    [SerializeField]public AudioClip[] footstepSounds;
    [SerializeField]public AudioClip[] gunFireSounds;
    [Header("UI")]
    [SerializeField]public AudioClip hoverButtonSound;
    [SerializeField]public AudioClip clickButtonSound;
    [Header("World Interaction")]
    [SerializeField]public AudioClip openDoorSound;
    [SerializeField]public AudioClip unlockDoorSound;
    [SerializeField]public AudioClip openLockedDoorSound;
    [SerializeField]public AudioClip pickUpItemSound;
    [SerializeField]public AudioClip reloadSound;
    [SerializeField]public AudioClip healSound;
    
    [Header("Horror Sounds")]
    [SerializeField]public AudioClip chaseSound;
    
    [SerializeField]public AudioClip heartbeatSound;

    private AudioSource chaseSource;
    private AudioSource heartbeatSource;

    void Awake()
    {
        if(instance == null)
            instance = this;

        chaseSource = gameObject.AddComponent<AudioSource>();
        chaseSource.clip = chaseSound;
        chaseSource.loop = true;
        chaseSource.playOnAwake = false;

        heartbeatSource = gameObject.AddComponent<AudioSource>();
        heartbeatSource.clip = heartbeatSound;
        heartbeatSource.loop = true;
        heartbeatSource.playOnAwake = false;
    }
    public void Play(AudioClip audioClip,float volume)
    {
        SoundFXManager.instance.PlaySoundFXClip(audioClip, transform, volume, "soundFX");
    }
    public void PlayRandomPistolFireSound(Transform playerTransform, float volume)
    {
        int random = Random.Range(0, gunFireSounds.Length);
        SoundFXManager.instance.PlaySoundFXClip(gunFireSounds[random], playerTransform, volume, "soundFX");
    }
    public void PlayRandomFootstepSound(float volume)
    {
        int random = Random.Range(0, footstepSounds.Length);
        SoundFXManager.instance.PlaySoundFXClip(footstepSounds[random], transform, volume, "soundFX");
    }
    public void PlayHoverButton(float volume)
    {
        SoundFXManager.instance.PlaySoundFXClip(hoverButtonSound, transform, volume, "menuFX");
    }
    public void PlayClickButton(float volume)
    {
        SoundFXManager.instance.PlaySoundFXClip(clickButtonSound, transform, volume, "menuFX");
    }
    public void PlayOpenDoor(float volume)
    {
        SoundFXManager.instance.PlaySoundFXClip(openDoorSound, transform, volume, "soundFX");
    }
    public void PlayUnlockDoor(float volume)
    {
        SoundFXManager.instance.PlaySoundFXClip(unlockDoorSound, transform, volume, "soundFX");
    }
    public void PlayOpenLockedDoor(float volume)
    {
        SoundFXManager.instance.PlaySoundFXClip(openLockedDoorSound, transform, volume, "soundFX");
    }
    public void PlayPickUpItem(float volume)
    {
        SoundFXManager.instance.PlaySoundFXClip(pickUpItemSound, transform, volume, "soundFX");
    }
    public void PlayReloadSound(float volume)
    {
        SoundFXManager.instance.PlaySoundFXClip(reloadSound, transform, volume, "soundFX");
    }
    public void PlayHeal(float volume)
    {
        SoundFXManager.instance.PlaySoundFXClip(healSound, transform, volume, "soundFX");
    }
    
    /*public void PlayChaseSound(float volume)
    {
        SoundFXManager.instance.PlaySoundFXClip(chaseSound, transform, volume, "soundFX");
    }
    public void PlayHeartBeat(float volume)
    {
        SoundFXManager.instance.PlaySoundFXClip(pickUpItemSound, transform, volume, "soundFX");
    }*/
    public void PlayChaseSound(float volume)
    {
        if (!chaseSource.isPlaying)
        {
            chaseSource.volume = 0f;
            chaseSource.Play();
            StartCoroutine(FadeAudio(chaseSource, volume, 1f));
        }
    }

    public void StopChaseSound()
    {
        if (chaseSource.isPlaying)
        {
            StartCoroutine(FadeAudio(chaseSource, 0f, 1f, true));
        }
    }

    public void PlayHeartBeat(float volume)
    {
        if (!heartbeatSource.isPlaying)
        {
            heartbeatSource.volume = 0f;
            heartbeatSource.Play();
            StartCoroutine(FadeAudio(heartbeatSource, volume, 1f));
        }
    }

    public void StopHeartBeat()
    {
        if (heartbeatSource.isPlaying)
        {
            StartCoroutine(FadeAudio(heartbeatSource, 0f, 1f, true));
        }
    }
    private System.Collections.IEnumerator FadeAudio(AudioSource source, float targetVolume, float duration, bool stopAfter = false)
    {
        float startVolume = source.volume;
        float time = 0;

        while (time < duration)
        {
            time += Time.deltaTime;
            source.volume = Mathf.Lerp(startVolume, targetVolume, time / duration);
            yield return null;
        }

        source.volume = targetVolume;
        
        if (stopAfter) 
        {
            source.Stop();
        }
    }
    
}
