using UnityEngine;

public class EnemyDieState : EnemyState
{
    
    Animator animator;
    Enemy enemy;
    public EnemyDieState(Enemy enemy, 
    EnemyStateMachine movementStateMachine, 
    EnemyStateMachine attackStateMachine, 
    Animator animator) : 
    base(enemy, movementStateMachine, attackStateMachine, animator)
    {
        this.animator = animator;
        this.enemy = enemy;
    }

    public override void EnterState()
    {
        //animator.applyRootMotion = true;
        animator.SetTrigger("triggerDie");
        enemy.hitbox.SetActive(false);
        EnemyReviveHandler handler = enemy.GetComponent<EnemyReviveHandler>();
        if (handler != null && !handler.IsLoadingDeadState)
            handler.OnEnemyDied();
    }
    public override void ExitState()
    {
        
    }
}
