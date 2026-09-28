using System.Collections;
using UnityEngine;
using UnityEngine.InputSystem;

public class PlayerRunningState : PlayerState
{

    float runBuffer = 0.1f;
    #region animations
    static readonly int inputMagnitude = Animator.StringToHash("Input Magnitude");
    #endregion
    public PlayerRunningState(Player player,
    PlayerStateMachine movementStateMachine,
    PlayerStateMachine attackStateMachine,
    Animator animator) :
    base(player, movementStateMachine, attackStateMachine, animator)
    {
        
    }

    public override void EnterState()
    {
        S_PlayerStats.currentMoveSpeed = S_PlayerStats.runningSpeed;
    }
    public override void ExitState()
    {
        //player.StartCoroutine(BufferRun());
    }
    public override void FrameUpdate()
    {
        player.playerMovement.FrameUpdate();
        if (player.playerMovement.inputs.Direction == Vector2.zero)
            player.movementStateMachine.ChangeState(player.idleState);
        else if (!player.playerMovement.inputs.isRunning)
        {
            player.movementStateMachine.ChangeState(player.walkingState);
        }
        
        if(!CheckIfStuck())
            animator.SetFloat(inputMagnitude, S_PlayerStats.currentMoveSpeed, S_Animations.damp, Time.deltaTime);
        else
            animator.SetFloat(inputMagnitude, 0, S_Animations.damp, Time.deltaTime);
    }
    public override void PhysUpdate()
    {
        player.playerMovement.PhysUpdate();
    }
    private bool CheckIfStuck()
    {
        if(player.playerMovement.rb.linearVelocity.sqrMagnitude < 0.01f)
            return true;
        return false;
    }
    public override void AnimationTriggerEvent()
    {
        
    }
    IEnumerator BufferRun()
    {
        yield return new WaitForSeconds(runBuffer);
        if (player.playerMovement.inputs.Direction == Vector2.zero)
            player.playerMovement.inputs.isRunning = false;
    }
}
