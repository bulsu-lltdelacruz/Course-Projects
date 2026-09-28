using UnityEngine;
using UnityEngine.InputSystem;

public class EnemyIdleState : EnemyState
{
    #region animations
    int inputMagnitude = Animator.StringToHash("Input Magnitude");
    #endregion

    public EnemyIdleState(Enemy enemy,
    EnemyStateMachine movementStateMachine,
    EnemyStateMachine attackStateMachine,
    Animator animator) :
    base(enemy, movementStateMachine, attackStateMachine, animator)
    {
        
    }

    public override void EnterState()
    {
    }
    public override void ExitState()
    {
    }
    public override void FrameUpdate()
    {
    }
    public override void PhysUpdate()
    {
        
    }
    public override void AnimationTriggerEvent()
    {

    }
    
    
}
