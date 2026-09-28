using System.Collections.Generic;
using Unity.Cinemachine;
using UnityEngine;
using UnityEngine.AI;

public class EnemyPatrolState : EnemyState
{
    private float idleTimer = 0f;
    bool hasChecked = false;
    List<Transform> patrolAreas;
    GameObject enemyModel;
    Quaternion enemyRotationBeforeStop;
    NavMeshAgent agent;
    int counter;
    public EnemyPatrolState(Enemy enemy,
     EnemyStateMachine movementStateMachine,
     EnemyStateMachine attackStateMachine,
     List<Transform> patrolAreas,
     Animator animator) :
     base(enemy, movementStateMachine, attackStateMachine, animator)
    {
        this.patrolAreas = patrolAreas;
        enemyModel = enemy.enemyModel;
        agent = enemy.navMeshAgent;
        counter = 0;
        idleTimer = Time.time+enemy.idleForSeconds;
        enemyRotationBeforeStop = Quaternion.identity;
    }
    public override void EnterState()
    {
        enemy.navMeshAgent.SetDestination(patrolAreas[0].position);
        animator.SetBool("isWalking", true);
    }
    public override void ExitState()
    {
        animator.SetBool("isWalking", false);
    }
    public override void FrameUpdate()
    {
        if(HasAgentReachedDestination())
        {
            animator.SetBool("isWalking", false);
            
            if(!hasChecked)
            {
                idleTimer = Time.time + enemy.idleForSeconds;
                hasChecked = true;
            }
            
            if(Time.time >= idleTimer)
            {
                idleTimer = 0;
                hasChecked = false;
                
                if(counter + 1 >= patrolAreas.Count)
                    counter = 0;
                else
                    counter++;
                    
                enemy.navMeshAgent.SetDestination(patrolAreas[counter].position);
            }
            enemy.enemyModel.transform.rotation = enemyRotationBeforeStop;
        }
        else
        {
            animator.SetBool("isWalking", true);
            enemyRotationBeforeStop = enemy.enemyModel.transform.rotation;

            Vector3 localMovementDirection = enemy.transform.InverseTransformDirection(enemy.navMeshAgent.desiredVelocity).normalized;
            localMovementDirection.z = localMovementDirection.y;
            localMovementDirection.y=0;
            if (enemy.navMeshAgent.desiredVelocity.sqrMagnitude > 0.1f && localMovementDirection != Vector3.zero)
            {
                Quaternion targetRotation = Quaternion.LookRotation(localMovementDirection);
                targetRotation.x = 0; 
                targetRotation.z = 0; 
                
                enemy.enemyModel.transform.rotation = Quaternion.Lerp(
                    enemy.enemyModel.transform.rotation, 
                    targetRotation, 
                    Time.deltaTime * 8f
                );  
            }
        }     
    }
    private bool HasAgentReachedDestination()
    {
        if (!agent.pathPending) 
        {
            if (agent.remainingDistance <= agent.stoppingDistance) 
            {
                if (!agent.hasPath || agent.velocity.sqrMagnitude == 0f)
                    {
                        return true;
                    }
            }
        }
        return false;
    }
    public override void PhysUpdate()
    {
        
    }
    public override void AnimationTriggerEvent()
    {

    }
    
}
