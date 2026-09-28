using UnityEngine;
using UnityEngine.InputSystem;

public class PlayerWalkState : PlayerState
{

    #region animations
    static readonly int inputMagnitude = Animator.StringToHash("Input Magnitude");
    Transform playerTransform;
    #endregion
    public PlayerWalkState(Player player,
    PlayerStateMachine movementStateMachine,
    PlayerStateMachine attackStateMachine,
    Animator animator) :
    base(player, movementStateMachine, attackStateMachine, animator)
    {
        playerTransform = player.transform;
    }

    public override void EnterState()
    {
        S_PlayerStats.currentMoveSpeed = S_PlayerStats.walkSpeed;
    }
    public override void ExitState()
    {
        player.playerMovement.inputs.isRunning = true;
    }
    public override void FrameUpdate()
    {
        player.playerMovement.FrameUpdate();
        if (player.playerMovement.inputs.Direction == Vector2.zero)
            movementStateMachines.ChangeState(player.idleState);
        else if (player.playerMovement.inputs.isRunning && attackStateMachines.currentState != player.lockInState)
        {
            player.movementStateMachine.ChangeState(player.runningState);
        }
        
        if(!CheckIfStuck())
            animator.SetFloat(inputMagnitude, S_PlayerStats.currentMoveSpeed, S_Animations.damp, Time.deltaTime);
        else
            animator.SetFloat(inputMagnitude, 0, S_Animations.damp, Time.deltaTime);
    }
    private bool CheckIfStuck()
    {
        if(player.playerMovement.rb.linearVelocity.sqrMagnitude < 0.01f)
            return true;
        return false;
    }
    public override void PhysUpdate()
    {
        player.playerMovement.PhysUpdate();
    }
    public override void AnimationTriggerEvent()
    {

    }
}
