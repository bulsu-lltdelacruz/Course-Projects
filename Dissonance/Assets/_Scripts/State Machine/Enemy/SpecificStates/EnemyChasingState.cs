using UnityEngine;
using UnityEngine.AI;

public class EnemyChasingState : EnemyState
{
    private float pathUpdateDeadline = 0.1f;
    private Vector3 playerLastKnownPosition = Vector3.zero;
    private LayerMask detectMask;
    Quaternion enemyRotationBeforeStop;
    private NavMeshAgent agent;
    public EnemyChasingState(Enemy enemy,
     EnemyStateMachine movementStateMachine,
     EnemyStateMachine attackStateMachine,
     Animator animator) :
     base(enemy, movementStateMachine, attackStateMachine, animator)
    {
        detectMask = LayerMask.GetMask("Wall", "Player");
        agent = enemy.navMeshAgent;
        enemyRotationBeforeStop = Quaternion.identity;
    }
    public override void EnterState()
    {
        enemy.isChasing = true;
        enemy.isAggroed = true;
        animator.SetBool("isRunning", true);
    }
    public override void ExitState()
    {
        enemy.isChasing = false;
        enemy.isAggroed = false;
        animator.SetBool("isRunning", false);
        enemy.navMeshAgent.ResetPath();
    }
    //float e = 0;
    public override void FrameUpdate()
    {
        Player.isBeingChased = true;
        if (Time.time >= pathUpdateDeadline)
        {
            playerLastKnownPosition = enemy.player.transform.position;
            pathUpdateDeadline = Time.time + enemy.pathUpdateDelay;
            enemy.navMeshAgent.SetDestination(playerLastKnownPosition);
        }

        Vector3 velocity = enemy.navMeshAgent.desiredVelocity;

        Vector3 lookTowards = new Vector3(velocity.x,0f, velocity.y).normalized;
        
        
        if (velocity != Vector3.zero)
        {
            animator.SetBool("isWalking", true);
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
        else
        {
            animator.SetBool("isRunning", false);
            enemy.enemyModel.transform.rotation = enemyRotationBeforeStop;
        }

        if (enemy.detectPlayerInAttackRange.isPlayerInAttackRange)
            enemy.movementStateMachines.ChangeState(enemy.attackState);
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
