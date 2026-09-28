using System.Collections;
using UnityEngine;

public class PlayerHitState : PlayerState
{
    Player player;
    float totalTime = 0f;
    float stateDuration = .3f;
    Quaternion rotationBeforeStop;
    Rigidbody2D rb;
    Enemy enemy;
    static readonly int inputMagnitude = Animator.StringToHash("Input Magnitude");
    public PlayerHitState(Player player, 
    PlayerStateMachine movementStateMachine, 
    PlayerStateMachine attackStateMachine, 
    Animator animator) : base(player, movementStateMachine, attackStateMachine, animator)
    {
        this.player = player;
        this.animator = animator;
        rotationBeforeStop = Quaternion.identity;
    }
    public override void EnterState()
    {
        PlayerModelHandler.IsApplyRotation = false;
        rotationBeforeStop = player.playerModel.transform.localRotation;
        player.StartCoroutine(StartHit());
        S_PlayerStats.currentMoveSpeed = 0f;
        animator.SetFloat(inputMagnitude, S_PlayerStats.currentMoveSpeed);
        player.playerMovement.enableMovement = false;
        player.playerMovement.rb.linearVelocity = Vector2.zero;
        rb = player.playerMovement.rb;
        animator.SetTrigger("triggerHit");
    }
    
    public override void ExitState()
    {
        PlayerModelHandler.IsApplyRotation = true;
        player.playerMovement.enableMovement = true;
        animator.ResetTrigger("triggerHit");
    }
    public override void FrameUpdate()
    {
        
    }
    public override void PhysUpdate()
    {
        if(enemy == null)
            return;
        Vector2 pushDirection = (player.transform.position - enemy.transform.position).normalized;
        //rb.linearVelocity = Vector2.zero;
        rb.AddForce(pushDirection * enemy.attackForce, ForceMode2D.Impulse);
    }
    IEnumerator StartHit()
    {
        totalTime = Time.time+stateDuration;
        while (Time.time < totalTime)
        {
            yield return null;
        }
        totalTime = 0;
        player.movementStateMachine.ChangeState(player.idleState);
        player.attackStateMachines.ChangeState(player.idleAttackState);
    }
    public void SetEnemy(Enemy enemy)
    {
        this.enemy = enemy;
    }
    
}
