using UnityEngine;

public class EventCallerFromEnemy : MonoBehaviour
{
    Enemy enemy;
    [SerializeField]float runFootstepVolume;
    [SerializeField]float walkFootstepVolume;
    [SerializeField]float aimFootstepVolume;
    void Start()
    {
        enemy = GetComponentInParent<Enemy>();
    }
    void PlayFootsteps()
    {
        if(enemy.movementStateMachines.currentState == enemy.chaseState)
            enemy.enemySounds.PlayRandomFootstepSound(runFootstepVolume);
        if(enemy.movementStateMachines.currentState == enemy.patrolState)
            enemy.enemySounds.PlayRandomFootstepSound(walkFootstepVolume);
    }
}
