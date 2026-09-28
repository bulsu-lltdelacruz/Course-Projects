using System.Collections;
using UnityEngine;
using UnityEngine.InputSystem;

public class EnemyRunningState : EnemyState
{
    #region animations
    int inputMagnitude = Animator.StringToHash("Input Magnitude");
    #endregion
    public EnemyRunningState(Enemy enemy,
    EnemyStateMachine movementStateMachine,
    EnemyStateMachine attackStateMachine,
    Animator animator) :
    base(enemy, movementStateMachine, attackStateMachine, animator)
    {
        
    }

    public override void EnterState()
    {
        S_PlayerStats.currentMoveSpeed = S_PlayerStats.runningSpeed;
    }
    public override void ExitState()
    {
        
    }
    public override void FrameUpdate()
    {
        enemy.playerMovement.FrameUpdate();
        /*if (enemy.playerMovement.inputs.Direction == Vector2.zero)
            enemy.movementStateMachine.ChangeState(enemy.idleState);
        else if (!enemy.playerMovement.inputs.isRunning)
            enemy.movementStateMachine.ChangeState(enemy.walkingState);
        animator.SetFloat(inputMagnitude, S_PlayerStats.currentMoveSpeed, S_Animations.damp, Time.deltaTime);*/
    }
    public override void PhysUpdate()
    {
        enemy.playerMovement.PhysUpdate();
    }
    public override void AnimationTriggerEvent()
    {

    }
}
