using System.Collections.Generic;
using Unity.Cinemachine;
using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.InputSystem;

public class PlayerLockInState : PlayerState
{
    //private List<GameObject> enemies;
    private GameObject aimedEnemy;
    private Camera mainCamera;
    private Vector3 modelRotateDirection;
    private Vector3 switchDir;
    private LayerMask groundPointMask;
    private LayerMask screenPointMask;  
    LayerMask attackMask;
    private bool canRotate = false;
    public bool isJoyStick;
    public Vector3 joystickAimDir;
    private Events events;
    
    static readonly int aimInput2DX = Animator.StringToHash("Aim2DX");
    static readonly int aimInput2DY = Animator.StringToHash("Aim2DY");
    static int layerFullBody;

    public PlayerLockInState(Player player,
     PlayerStateMachine movementStateMachine,
     PlayerStateMachine attackStateMachine,
     Events events,
     Animator animator) :
     base(player, movementStateMachine, attackStateMachine, animator)
    {
        this.events = events;
        mainCamera = Camera.main;
        attackMask = LayerMask.GetMask("Enemy");
        screenPointMask = LayerMask.GetMask("ScreenHit");
        groundPointMask = LayerMask.GetMask("Ground");
        layerFullBody = animator.GetLayerIndex("Full Body");
    }
    public override void EnterState()
    {
        if (mainCamera == null || !mainCamera.gameObject.activeInHierarchy)
        {
            mainCamera = Camera.main;
        }
        animator.SetBool("IsAiming", true);
        PlayerModelHandler.IsApplyRotation = false; // Ensure rotation logic is handed over to this state
        player.playerMovement.inputs.isRunning = false;
        events.onAim?.Invoke(); 
    }
    public override void ExitState()
    {
        animator.SetBool("IsAiming", false);
        events.onUnAim?.Invoke();
        PlayerModelHandler.IsApplyRotation = true;
        player.input.isRunning = true;
        PlayerModelHandler.SetRotationDirection(player.transform.forward);
        canRotate = false;
        switchDir = Vector3.zero;
        //player.attackState.UpdateEnemyLockedIn(aimedEnemy);
    }
    public void ExitStateToFire()
    {
        //animator.SetBool("IsAiming", false);
        PlayerModelHandler.IsApplyRotation = true;
        PlayerModelHandler.SetRotationDirection(player.transform.forward);
        player.input.isRunning = true;
        canRotate = false;
        switchDir = Vector3.zero;
        player.attackState.UpdateEnemyLockedIn(aimedEnemy);
    }
    public override void FrameUpdate()
    {
        Debug.Log("Locking in");
        if(!player.input.isAiming)
            player.attackStateMachines.ChangeState(player.idleAttackState);
        Vector3 playerF = player.playerModel.transform.forward;
        Vector3 playerH = player.playerModel.transform.right;
        playerF.y = 0;
        playerH.y = 0;
        playerF = playerF.normalized;
        playerH = playerH.normalized;

        Vector3 movementRelativeToPlayer = playerH * player.playerMovement.inputs.Direction.x + playerF * player.playerMovement.inputs.Direction.y;

        animator.SetFloat(aimInput2DX, Mathf.Clamp(movementRelativeToPlayer.x, -1f, 1f), S_Animations.damp, Time.deltaTime);
        animator.SetFloat(aimInput2DY, Mathf.Clamp(movementRelativeToPlayer.z, -1f, 1f), S_Animations.damp, Time.deltaTime);

        if (isJoyStick)
        {
            UpdateJoystickAim();
            switchDir = Vector3.zero;
            return;
        }

        if (Mouse.current == null)
        {
            switchDir = Vector3.zero;
            return;
        }

        Vector3 mousePosition = Mouse.current.position.ReadValue();
        mousePosition.x/=Initializer.scaleX;
        mousePosition.y/=Initializer.scaleY;
        Ray ray = mainCamera.ScreenPointToRay(mousePosition);
        if (Physics.Raycast(ray, out RaycastHit groundHit, 100f, groundPointMask))
        {
            modelRotateDirection = groundHit.point - player.transform.position;
        }
        
        CastRayFromMouseTo(mousePosition);
        
        switchDir = Vector3.zero;
    }
    private void UpdateJoystickAim()
    {
        Vector3 worldAimDirection = GetJoystickWorldDirection();
        if (worldAimDirection == Vector3.zero)
            return;

        modelRotateDirection = worldAimDirection;
        player.attackState.UpdateAimDirection(modelRotateDirection);

        Vector2 aimDirection2D = new Vector2(modelRotateDirection.x, modelRotateDirection.z).normalized;
        RaycastHit2D aimHit = Physics2D.Raycast(player.transform.position, aimDirection2D, Mathf.Infinity, attackMask);
        if (aimHit)
        {
            aimedEnemy = aimHit.transform.gameObject;
            player.attackState.UpdateEnemyLockedIn(aimedEnemy);
        }
        else
        {
            aimedEnemy = null;
            player.attackState.UpdateEnemyLockedIn(null);
        }
    }
    private Vector3 GetJoystickWorldDirection()
    {
        if (joystickAimDir == Vector3.zero)
            return Vector3.zero;

        if (mainCamera == null || !mainCamera.gameObject.activeInHierarchy)
            mainCamera = Camera.main;

        if (mainCamera == null)
            return joystickAimDir.normalized;

        Vector3 cameraForward = mainCamera.transform.forward;
        Vector3 cameraRight = mainCamera.transform.right;
        cameraForward.y = 0f;
        cameraRight.y = 0f;
        cameraForward.Normalize();
        cameraRight.Normalize();

        Vector3 worldAimDirection = (cameraRight * joystickAimDir.x) + (cameraForward * joystickAimDir.z);
        worldAimDirection.y = 0f;
        return worldAimDirection.normalized;
    }
    private void CastRayFromMouseTo(Vector3 mousePosition)
    {
        Ray ray = mainCamera.ScreenPointToRay(mousePosition);
        //Debug.DrawRay(ray.origin, ray.direction * 100f, Color.green);
        if (Physics.Raycast(ray, out RaycastHit screenHit, 100f, screenPointMask))
        {
            player.attackState.UpdateAimDirection(modelRotateDirection/*screenHit.point - player.bulletStartPosition.transform.position*/);
            //Debug.Log(screenHit.point - player.bulletStartPosition.transform.position);
            RaycastHit2D aimHit = Physics2D.Raycast(player.transform.position, screenHit.point-player.transform.position, Mathf.Infinity, attackMask);
            if(aimHit)
            {
                aimedEnemy = aimHit.transform.gameObject;
                player.attackState.UpdateEnemyLockedIn(aimedEnemy);
            }else
            {
                aimedEnemy = null;
                player.attackState.UpdateEnemyLockedIn(aimedEnemy);
            }
        }
    }
    public override void PhysUpdate()
    {
        /*if (modelRotateDirection != Vector3.zero)
        {
            modelRotateDirection.y = 0;
            
            modelRotateDirection = modelRotateDirection.normalized;

            Quaternion targetRotation = Quaternion.LookRotation(modelRotateDirection);
            player.playerModel.transform.localRotation = targetRotation;
            canRotate = true;
        }*/
        // In PlayerLockInState.PhysUpdate
        if (modelRotateDirection != Vector3.zero)
        {
            modelRotateDirection.y = 0;
            modelRotateDirection = modelRotateDirection.normalized;

            // Use world rotation instead of local to avoid parent-offset issues
            Quaternion targetRotation = Quaternion.LookRotation(modelRotateDirection);
            player.playerModel.transform.rotation = targetRotation; 
            canRotate = true;
        }

        if (switchDir != Vector3.zero)
        {
            switchDir.y = 0;
            Quaternion targetRotation = Quaternion.LookRotation(switchDir);
            player.switchTarget.rotation = targetRotation;
            canRotate = true;
        }
        
        
    }
    public override void AnimationTriggerEvent()
    {
        /*PlayerModelHandler.IsApplyRotation = false;
        // Else just choose closest
        enemies = enemyInLockInRangeCheck.GetEnemiesInLockInRange();
        prioritzedEnemy = enemies[0];
        foreach (GameObject enemy in enemies)
        {
            float newDistance = Vector3.Distance(player.transform.position, enemy.transform.position);
            float oldPriorityDistance = Vector3.Distance(player.transform.position,prioritzedEnemy.transform.position);
            if (newDistance < oldPriorityDistance)
            {
                prioritzedEnemy = enemy;
            }

        }*/
    }
    
}
