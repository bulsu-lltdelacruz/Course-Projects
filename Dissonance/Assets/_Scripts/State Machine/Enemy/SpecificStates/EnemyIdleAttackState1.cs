using UnityEngine;

public class EnemyIdleAttackState: EnemyState
{
    private float pathUpdateDeadline = 0f;
    private Vector3 playerLastKnownPosition = Vector3.zero;
    private LayerMask detectMask;
    public EnemyIdleAttackState(Enemy enemy,
     EnemyStateMachine movementStateMachine,
     EnemyStateMachine attackStateMachine,
     Animator animator) :
     base(enemy, movementStateMachine, attackStateMachine, animator)
    {
        detectMask = LayerMask.GetMask("Wall", "Player");
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
