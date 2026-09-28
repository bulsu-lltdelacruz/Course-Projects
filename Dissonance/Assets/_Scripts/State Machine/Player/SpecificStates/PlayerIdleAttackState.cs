using System.Collections.Generic;
using Unity.Cinemachine;
using UnityEngine;

public class PlayerIdleAttackState : PlayerState
{
    public PlayerIdleAttackState(Player player,
     PlayerStateMachine movementStateMachine,
     PlayerStateMachine attackStateMachine,
     Animator animator) :
     base(player, movementStateMachine, attackStateMachine, animator)
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
        if(player.input.isAiming && player.movementStateMachine.currentState!= player.hitState)
            player.attackStateMachines.ChangeState(player.lockInState);
    }
    public override void PhysUpdate()
    {
        
    }
    public override void AnimationTriggerEvent()
    {

    }
    
}
