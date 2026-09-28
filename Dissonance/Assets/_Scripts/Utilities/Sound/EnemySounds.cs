using UnityEngine;

public class EnemySounds : MonoBehaviour
{
    [SerializeField]public AudioClip[] footstepSounds;
    [SerializeField]public AudioClip[] attackSounds;

    /*public void PlayRandomPistolFireSound(Transform playerTransform, float volume)
    {
        int random = Random.Range(0, gunFireSounds.Length);
        SoundFXManager.instance.PlaySoundFXClip(gunFireSounds[random], playerTransform, volume);
    }*/
    public void PlayRandomFootstepSound(float volume)
    {
        int random = Random.Range(0, footstepSounds.Length);
        SoundFXManager.instance.PlaySoundFXClip(footstepSounds[random], transform, volume, "soundFX");
    }
}
