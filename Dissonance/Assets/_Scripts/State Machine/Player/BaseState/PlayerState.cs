using Unity.VisualScripting;
using UnityEngine;

public class PlayerState
{
    protected Player player;
    protected PlayerStateMachine movementStateMachines;
    protected PlayerStateMachine attackStateMachines;
    protected Animator animator;
    protected PlayerState(Player player, PlayerStateMachine movementStateMachine, PlayerStateMachine attackStateMachine, Animator animator)
    {
        this.player = player;
        this.attackStateMachines = attackStateMachine;
        this.movementStateMachines = movementStateMachine;
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
