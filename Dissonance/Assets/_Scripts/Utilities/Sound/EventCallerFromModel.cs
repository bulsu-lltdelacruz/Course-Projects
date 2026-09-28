using UnityEngine;

public class EventCallerFromModel : MonoBehaviour
{
    Player player;
    [SerializeField]float runFootstepVolume;
    [SerializeField]float walkFootstepVolume;
    [SerializeField]float aimFootstepVolume;
    void Start()
    {
        player = GetComponentInParent<Player>();
    }
    void PlayFootsteps()
    {
        if(player.input.isAiming)
        {
            player.playerSounds.PlayRandomFootstepSound(aimFootstepVolume);
            return;
        }
            
        if(player.input.isRunning)
            player.playerSounds.PlayRandomFootstepSound(runFootstepVolume);
        else
            player.playerSounds.PlayRandomFootstepSound(walkFootstepVolume);
    }
}
