using System.Collections;
using UnityEngine;

public class PlayerDieState : PlayerState
{
    Animator animator;
    Player player;
    public PlayerDieState(Player player, 
    PlayerStateMachine movementStateMachine, 
    PlayerStateMachine attackStateMachine, 
    Animator animator) : 
    base(player, movementStateMachine, attackStateMachine, animator)
    {
        this.animator = animator;
        this.player = player;
    }

    public override void EnterState()
    {
        PlayerInputs.Player.Disable();
        animator.SetTrigger("triggerDie");
        player.playerMovement.rb.bodyType = RigidbodyType2D.Static;
        player.StartCoroutine(ShowDeathScreen());
    }
    private IEnumerator ShowDeathScreen()
    {
        float totalTime = Time.time+2f;
        while(Time.time<totalTime)
        {
            yield return null;
        }
        player.pauseManager.ShowDeathScreen();
    }
    public override void ExitState()
    {
        PlayerInputs.Player.Enable();
    }

}
