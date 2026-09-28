using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.AI;

public class EnemyAttackState : EnemyState
{
   // private EnemyInLockInRangeCheck enemyInLockInRangeCheck;
    string animationTriggerName = "playMeleeAttack";

    private float pathUpdateDeadline = 0f;
    private Vector3 playerLastKnownPosition = Vector3.zero;
    private LayerMask detectMask;
    Quaternion enemyRotationBeforeStop;
    private NavMeshAgent agent;

    public EnemyAttackState(Enemy enemy,
     EnemyStateMachine movementStateMachine,
     EnemyStateMachine attackStateMachine,
     Animator animator) :
     base(enemy, movementStateMachine, attackStateMachine, animator)
    {
        
        agent = enemy.navMeshAgent;
        enemyRotationBeforeStop = Quaternion.identity;
    }
    float duration = 2.267f/2f;
    float totalTime = 0f;
    float totalForMoveSpeed = 0f;
    public override void EnterState()
    {
        animator.SetTrigger(animationTriggerName);
        enemy.StartCoroutine(CheckAnimationIsFinished());
        enemy.StartCoroutine(RevertMovespeedAfterTime());
        agent.acceleration = 10f;
        agent.speed = 1.5f;
    }

        
    IEnumerator RevertMovespeedAfterTime()
    {
        totalForMoveSpeed = Time.time+(duration*.7f);
        while (Time.time < totalForMoveSpeed)
        {
            yield return null;
        }
        totalForMoveSpeed = 0;
            
        agent.acceleration = 10f;
        agent.speed = 3f;
    }
    IEnumerator CheckAnimationIsFinished()
    {
        totalTime = Time.time+(duration*2.4f);
        

        while (Time.time < totalTime)
        {
            yield return null;
        }
        totalTime = 0;
        if(enemy.detectPlayerInAttackRange.isPlayerInAttackRange)
        {
            enemy.movementStateMachines.ChangeState(this);
            //enemy.StartCoroutine(secret());
        }
        else
        {
            enemy.movementStateMachines.ChangeState(enemy.chaseState);
        }
            
    }
    float timer2;
    IEnumerator secret()
    {
        while (timer2 < .5f)
        {
            timer2 += Time.time;
            yield return null;
        }
        timer2 = 0;
        if(enemy.detectPlayerInAttackRange.isPlayerInAttackRange && enemy.movementStateMachines.currentState != enemy.attackState)
            enemy.movementStateMachines.ChangeState(enemy.attackState);
    }

    public override void ExitState()
    {
        animator.ResetTrigger(animationTriggerName);
        agent.acceleration = 10f;
        agent.speed = 3f;
       // Debug.Log("exit");
    }
    public override void FrameUpdate()
    {
        if (Time.time >= pathUpdateDeadline  )
        {
            playerLastKnownPosition = enemy.player.transform.position;
            pathUpdateDeadline = Time.time + enemy.pathUpdateDelay;
            enemy.navMeshAgent.SetDestination(playerLastKnownPosition);
        }
        /*Vector3 localMovementDirection = enemy.transform.InverseTransformDirection(enemy.navMeshAgent.desiredVelocity).normalized;
        if(localMovementDirection!= Vector3.zero)
        {
            Quaternion targetRotation = Quaternion.LookRotation(localMovementDirection);
            targetRotation.x=0;
            targetRotation.z=0;
            enemy.enemyModel.transform.rotation = Quaternion.Lerp(enemy.enemyModel.transform.rotation, targetRotation, Time.deltaTime*8f);
        }*/
        Vector3 velocity = enemy.navMeshAgent.desiredVelocity;
        Vector3 lookTowards = new Vector3(velocity.x,0f, velocity.y).normalized;
        
        if (velocity != Vector3.zero)
        {
            enemyRotationBeforeStop = enemy.enemyModel.transform.rotation;

            if (lookTowards != Vector3.zero)
            {
                Quaternion targetRotation = Quaternion.LookRotation(lookTowards);
                
                enemy.enemyModel.transform.rotation = Quaternion.Lerp(
                    enemy.enemyModel.transform.rotation, 
                    targetRotation, 
                    Time.deltaTime * 8f
                );  
            }
        }
        if(HasAgentReachedDestination())
        {
            animator.SetBool("isRunning", false);
            animator.SetBool("isWalking", false);
            //enemy.enemyModel.transform.rotation = enemyRotationBeforeStop;
        }else
        {
            animator.SetBool("isRunning", true);
            animator.SetBool("isWalking", true);
            //enemyRotationBeforeStop = enemy.enemyModel.transform.rotation;
        }
        if(enemy.detectPlayerInAttackRange.isPlayerInAttackRange && enemy.movementStateMachines.currentState != enemy.attackState)
            enemy.movementStateMachines.ChangeState(enemy.attackState);
    }
    public override void PhysUpdate()
    {
        
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
    public override void AnimationTriggerEvent()
    {

    }
    
}
