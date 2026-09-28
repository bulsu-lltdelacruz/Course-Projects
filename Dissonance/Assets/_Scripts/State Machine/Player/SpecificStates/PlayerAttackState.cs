using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerAttackState : PlayerState
{
    // private EnemyInLockInRangeCheck enemyInLockInRangeCheck;
    GameObject enemy;
    Events events;
    Vector3 weaponInitialPosition;
    Vector3 direction;
    //Vector2 hitNormal;
    float timer = 0;
    
    static readonly int aimInput2DX = Animator.StringToHash("Aim2DX");
    static readonly int aimInput2DY = Animator.StringToHash("Aim2DY");
    public PlayerAttackState(Player player,
     PlayerStateMachine movementStateMachine,
     PlayerStateMachine attackStateMachine,
     Events events,
     Animator animator) :
     base(player, movementStateMachine, attackStateMachine, animator)
    {
        this.events = events;
        direction = Vector3.zero;
    }
    public override void EnterState()
    {
        timer = 0f;
        player.movementStateMachine.currentState = player.idleState;
        player.playerMovement.inputs.isRunning = false;
        PlayerModelHandler.IsApplyRotation = false;

        player.playerMovement.enableMovement = false;
        S_PlayerStats.currentMoveSpeed = 0;

          // ── Ammo check ────────────────────────────────────────────────────────
        if (!WeaponAmmoState.HasAmmo())
        {
            // Dry fire — play empty click sound if you have one, then exit
            // PlayerSounds.instance.PlayDryFire(...);
            player.StartCoroutine(ExitWithNoAmmo());
            Debug.Log("no ammo bt");
            return;
        }

        WeaponAmmoState.UseAmmo(); // consume one round
        IDamageable damageable = null;


        if(enemy!= null)
            damageable = enemy?.GetComponentInParent<IDamageable>();
        //if(damageable!=null && hitNormal != null)
        {
            
            player.playerSounds.PlayRandomPistolFireSound(player.transform, 1);
            Vector3 startPosition = player.bulletStartPosition.transform.position;
            GameObject bullet = Object.Instantiate(player.bulletPrefab, startPosition, Quaternion.identity);
            //Vector2 direction = enemy.transform.position - player.transform.position;
            bullet.GetComponent<Bullet>().Initialize(direction, player.GetRandomDamageAmount());
            //damageable.Damage(player.GetRandomDamageAmount(), hitNormal);
        }
            
        player.StartCoroutine(DisableMovement());
    }
    private IEnumerator ExitWithNoAmmo()
    {
        yield return null;
        if (player.playerMovement.inputs.isAiming)
            player.attackStateMachines.ChangeState(player.lockInState);
        else
            player.attackStateMachines.ChangeState(player.idleAttackState);

        player.playerMovement.enableMovement = true;
        PlayerModelHandler.IsApplyRotation = true;
    }
    public override void ExitState()
    {
        if(!player.input.isAiming)
        {
            events.onUnAim?.Invoke();
            animator.SetBool("IsAiming", false);
        }
        player.playerMovement.inputs.isRunning = true;
        player.playerMovement.enableMovement = true;
        PlayerModelHandler.IsApplyRotation = true;
        enemy = null;
        timer = 0f;
    }
    public override void FrameUpdate()
    {
        animator.SetFloat(aimInput2DX, 0, S_Animations.damp, Time.deltaTime);
        animator.SetFloat(aimInput2DY, 0, S_Animations.damp, Time.deltaTime);
    }
    public override void PhysUpdate()
    {
        
    }
    public override void AnimationTriggerEvent()
    {

    }
    public void UpdateEnemyLockedIn(GameObject enemy)
    {
        this.enemy = enemy;
    }
    public void UpdateAimDirection(Vector3 direction)
    {
        this.direction = direction;
    }
    /*public void UpdateHitNormal(Vector2 hitNormal)
    {
        this.hitNormal = hitNormal;
    }*/
    IEnumerator DisableMovement()
    {
        GameObject weapon = player.inventory.equipWeapon.itemEquipInfo.Pistol_aimWeaponPos;
        Vector3 startPos = weapon.transform.localPosition;
        Quaternion startRot = weapon.transform.localRotation;

        float duration = player.bobbingStats.fireDuration;
        float recoveryDuration = player.bobbingStats.recoveryDuration;
        float peakDuration = player.bobbingStats.peakDuration;
        float timer = 0f;

        Vector3 peakPos = startPos;
        peakPos.y += player.bobbingStats.verticalRecoil * duration; 
        peakPos.z -= player.bobbingStats.backwardRecoil * duration;

        Quaternion peakRot = startRot * Quaternion.Euler(0, player.bobbingStats.xRotation * duration, 0);

        while (timer < duration)
        {
            timer += Time.deltaTime;
            
            float t = timer / duration;
            weapon.transform.localPosition = Vector3.Lerp(startPos, peakPos, t);
            weapon.transform.localRotation = Quaternion.Slerp(startRot, peakRot, t);

            yield return null;
        }
        timer = 0f;

        while (timer < peakDuration)
        {
            timer += Time.deltaTime;
            yield return null;
        }
        timer = 0f;

        while (timer < recoveryDuration)
        {
            timer += Time.deltaTime;
            float t = timer / recoveryDuration;
            
            weapon.transform.localPosition = Vector3.Lerp(peakPos, startPos, t);
            weapon.transform.localRotation = Quaternion.Slerp(peakRot, startRot, t);
            
            yield return null;
        }

        weapon.transform.localPosition = startPos;
        weapon.transform.localRotation = startRot;

        //yield return new WaitForSeconds(player.bobbingStats.fireDuration);
        if(player.playerMovement.inputs.isAiming)
            player.attackStateMachines.ChangeState(player.lockInState);
        else
            player.attackStateMachines.ChangeState(player.idleAttackState);

            
        //player.movementStateMachine.ChangeState(player.idleState);
        
    }
    
}
