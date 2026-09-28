using System.Collections.Generic;
using Unity.Cinemachine;
using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.InputSystem;

public class EnemyLockInState : EnemyState
{
    //private List<GameObject> enemies;
    private GameObject prioritzedEnemy;
    private Camera mainCamera;
    private Vector3 direction;
    private LayerMask screenPointMask;
    private bool isLockedIn = false;
    private bool canRotate = false;
    public EnemyLockInState(Enemy enemy,
     EnemyStateMachine movementStateMachine,
     EnemyStateMachine attackStateMachine,
     Animator animator) :
     base(enemy, movementStateMachine, attackStateMachine, animator)
    {
        mainCamera = Camera.main;
        screenPointMask = LayerMask.GetMask("Ground", "Targetable");
    }
    public override void EnterState()
    {
        
    }
    public override void ExitState()
    {
        PlayerModelHandler.IsApplyRotation = true;
        PlayerModelHandler.SetRotationDirection(enemy.transform.forward);
        isLockedIn = false;
        canRotate = false;
    }
    public override void FrameUpdate()
    {
        if (!isLockedIn)
        {
            Vector3 mouse = Mouse.current.position.ReadValue();
            Ray ray = mainCamera.ScreenPointToRay(mouse);
            if (Physics.Raycast(ray, out RaycastHit screenHit, 100f, screenPointMask))
            {
                Vector3 targetPosition = screenHit.point;
                direction = targetPosition - enemy.transform.position;
            }
            if(canRotate)
            {
                Vector3 origin = enemy.transform.position;
                Vector3 forward = enemy.transform.forward;
                RaycastHit[] allHit;

                allHit = Physics.RaycastAll(origin, forward, 5f);
                if (allHit.Length > 0)
                {
                    float prevDistance = 1000f;
                    foreach (RaycastHit hit in allHit)
                    {
                        float currentDistance = Vector3.Distance(hit.collider.bounds.center, hit.point);
                        if (hit.collider.gameObject.CompareTag("Wall") || hit.collider.gameObject.CompareTag("Ground"))
                        {
                            break;
                        }
                            
                        if (currentDistance < prevDistance && hit.collider.gameObject.CompareTag("Enemy"))
                        {
                            prioritzedEnemy = hit.transform.gameObject;
                            prevDistance = currentDistance;
                            PlayerModelHandler.IsApplyRotation = false;
                            isLockedIn = true;
                        }
                    }
                }
            }
            
        }
        else
        {
            direction = prioritzedEnemy.transform.position - enemy.transform.position;
        }
       
    }
    public override void PhysUpdate()
    {
        if(direction!=Vector3.zero)
        {
            direction.y = 0;
            Quaternion targetRotation = Quaternion.LookRotation(direction);
            enemy.rb.MoveRotation(targetRotation);
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
