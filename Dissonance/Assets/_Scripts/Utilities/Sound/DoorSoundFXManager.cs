using UnityEngine;

public class DoorSoundFXManager : MonoBehaviour
{
    public static DoorSoundFXManager instance;
    [Header("World Interaction")]
    [SerializeField]public AudioClip openDoorSound;
    [SerializeField]public AudioClip unlockDoorSound;
    [SerializeField]public AudioClip openLockedDoorSound;
    [SerializeField]public AudioClip unScrewSound;
    [SerializeField]public AudioClip cutWireSound;
    [SerializeField]public AudioClip elevator;

    void Start()
    {
        if(instance == null)
            instance = this;
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
    public void PlayUnScrew(float volume)
    {
        SoundFXManager.instance.PlaySoundFXClip(openLockedDoorSound, transform, volume, "soundFX");
    }
    public void PlayCutWire(float volume)
    {
        SoundFXManager.instance.PlaySoundFXClip(openLockedDoorSound, transform, volume, "soundFX");
    }
}
