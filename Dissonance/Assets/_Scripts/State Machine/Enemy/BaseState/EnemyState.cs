using Unity.VisualScripting;
using UnityEngine;

public class EnemyState
{
    protected Enemy enemy;
    protected EnemyStateMachine movementStateMachine;
    protected EnemyStateMachine attackStateMachine;
    protected Animator animator;
    protected EnemyState(Enemy enemy, EnemyStateMachine movementStateMachine, EnemyStateMachine attackStateMachine, Animator animator)
    {
        this.enemy = enemy;
        this.attackStateMachine = attackStateMachine;
        this.movementStateMachine = movementStateMachine;
        this.animator = animator;
    }
    public virtual void EnterState()
    {

    }
    public virtual void ExitState()
    {

    }
    public virtual void FrameUpdate()
    {

    }
    public virtual void PhysUpdate()
    {

    }
    public virtual void AnimationTriggerEvent()
    {
        
    }
}
