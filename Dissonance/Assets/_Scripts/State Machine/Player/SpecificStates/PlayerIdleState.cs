using UnityEngine;
using UnityEngine.InputSystem;

public class PlayerIdleState : PlayerState
{
    #region animations
    static readonly int inputMagnitude = Animator.StringToHash("Input Magnitude");
    #endregion

    public PlayerIdleState(Player player,
    PlayerStateMachine movementStateMachine,
    PlayerStateMachine attackStateMachine,
    Animator animator) :
    base(player, movementStateMachine, attackStateMachine, animator)
    {
        
    }

    public override void EnterState()
    {
        S_PlayerStats.currentMoveSpeed = 0f;
    }
    public override void ExitState()
    {
    }
    public override void FrameUpdate()
    {
        
        animator.SetFloat(inputMagnitude, S_PlayerStats.currentMoveSpeed, S_Animations.damp, Time.deltaTime);
        player.playerMovement.FrameUpdate();
        if (player.playerMovement.inputs.Direction != Vector2.zero
            && player.attackStateMachines.currentState != player.attackState
            && player.input.isRunning
            && player.attackStateMachines.currentState != player.lockInState
            && player.attackStateMachines.currentState != player.hitState)
        {
            player.movementStateMachine.ChangeState(player.runningState);
        }else if(player.attackStateMachines.currentState != player.attackState
            && player.attackStateMachines.currentState != player.hitState
                && player.playerMovement.inputs.Direction != Vector2.zero)
        {
            player.movementStateMachine.ChangeState(player.walkingState);
        }
            
            
    }
    public override void PhysUpdate()
    {
        player.playerMovement.PhysUpdate();
    }
    public override void AnimationTriggerEvent()
    {

    }
    
    
}
