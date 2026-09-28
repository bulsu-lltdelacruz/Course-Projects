using System;
using System.Collections.Generic;
using Unity.Cinemachine;
using UnityEngine;
using UnityEngine.AI;
using UnityEngine.InputSystem;

public class Enemy : MonoBehaviour, IDamageable
{
    public EnemySounds enemySounds;
    #region flags
    public bool isChasing = false;
    public bool isAttacking = false;
    public bool isAlive = true;
    public bool isAggroed;
    #endregion flags
    public EnemyStateMachine attackStateMachines;
    public EnemyStateMachine movementStateMachines;
    [NonSerialized]public EnemyMovementHandler playerMovement;
    [NonSerialized]public Rigidbody rb;
    #region states
    public EnemyIdleState idleState;
    public EnemyWalkState walkingState;
    public EnemyRunningState runningState;
    public EnemyLockInState lockInState;
    public EnemyPatrolState patrolState;
    public EnemyChasingState chaseState;
    public EnemyAttackState attackState;
    public EnemyDieState dieState;
    [SerializeField]public GameObject hitbox;
    public DetectPlayerInAttackRange detectPlayerInAttackRange;
    #endregion states
    #region trigger checks
    #endregion trigger checks
    [SerializeField] public Animator animator;
    public float attackForce;
    [Header("Path Finding")]
    [NonSerialized] public DetectPlayerInRange detectPlayerInRange;
    [NonSerialized] public GameObject player;
    
    [SerializeField] public GameObject enemyModel;
    
    
    [SerializeField] public Vector3 initialPosition;
    [SerializeField] List<Transform> patrolAreas;
    [NonSerialized] public NavMeshAgent navMeshAgent;
    public float detectAngle;
    public float idleForSeconds;
    public float pathUpdateDelay = 0.01f;
    static readonly int inputMagnitude = Animator.StringToHash("Input Magnitude");

    [NonSerialized] public float[] damageRange;
    public float maxHealth { get ; set ; }
    public float currentHealth { get ; set ; }
    

    private void Awake()
    {
        initialPosition = transform.position;
        //damage
        damageRange = new float[] {8,16};
        enemySounds = GetComponent<EnemySounds>();
        attackStateMachines = new EnemyStateMachine();
        movementStateMachines = new EnemyStateMachine();
        playerMovement = GetComponent<EnemyMovementHandler>();
        navMeshAgent = GetComponent<NavMeshAgent>();
        detectPlayerInRange = GetComponentInChildren<DetectPlayerInRange>();
        detectPlayerInAttackRange = GetComponentInChildren<DetectPlayerInAttackRange>();

        // Locomotion States
        idleState = new EnemyIdleState(this, attackStateMachines, movementStateMachines, animator);
        walkingState = new EnemyWalkState(this, attackStateMachines, movementStateMachines, animator);
        runningState = new EnemyRunningState(this, attackStateMachines, movementStateMachines, animator);

        // Attacking States
        lockInState = new EnemyLockInState(this, attackStateMachines, movementStateMachines, animator);
        patrolState = new EnemyPatrolState(this, attackStateMachines, movementStateMachines, patrolAreas, animator);
        chaseState = new EnemyChasingState(this, attackStateMachines, movementStateMachines,animator);
        attackState= new EnemyAttackState(this, attackStateMachines, movementStateMachines, animator);
        dieState= new EnemyDieState(this, attackStateMachines, movementStateMachines, animator);
        attackStateMachines.Initialize(idleState);
    }
    void Start()
    {
        //navMeshAgent.SetDestination(new Vector3(-1.01f,0,0));
        rb = GetComponent<Rigidbody>();
        // Initialize State Machines
        //movementStateMachine.Initialize(idleState);
        movementStateMachines.Initialize(patrolState);
        attackStateMachines.Initialize(idleState);

        detectPlayerInRange.Initialize(this);
        maxHealth = 100f;
        currentHealth = 100f;
        navMeshAgent.updateUpAxis = false;
        navMeshAgent.updateRotation = false;
        GetComponent<EnemyReviveHandler>().Initialize();
    }
    void OnDisable()
    {
        if(movementStateMachines.currentState == dieState)
            movementStateMachines.ChangeState(dieState);
        else
            movementStateMachines.ChangeState(patrolState);
        transform.position = initialPosition;
    }
    void Update()
    {
        //attackStateMachines.currentState.FrameUpdate();
        movementStateMachines.currentState.FrameUpdate();
        //Debug.Log(movementStateMachines.currentState);
    }
    void FixedUpdate()
    {
        //attackStateMachines.currentState.PhysUpdate();
        movementStateMachines.currentState.PhysUpdate();
    }

    public void Damage(float damageAmount)
    {
        currentHealth -= damageAmount;
        if (currentHealth <= 0)
            Die();
    }
    
    public float GetRandomDamageAmount()
    {
        return UnityEngine.Random.Range(damageRange[0], damageRange[1]);
    }

    public void Die()
    {
        
    }

   // public void Damage(float damageAmount, System.Numerics.Vector2 hitNormal){/*fuck NO*/}

    public void Damage(float damageAmount, Vector2 hitNormal)
    {
        if(GetComponent<EnemyReviveHandler>().IsDead)
            return;
        currentHealth -= damageAmount;

        Vector3 worldNormal = new Vector3(hitNormal.x, hitNormal.y, 0f);

        Vector3 localNormal = enemyModel.transform.InverseTransformDirection(worldNormal);
        
        detectPlayerInRange.TriggerChaseOnHit();
        animator.SetTrigger("triggerHit");
        animator.SetFloat("hitX", localNormal.x);
        animator.SetFloat("hitY", localNormal.z);
        
        if(currentHealth<=0)
        {
            movementStateMachines.ChangeState(dieState);
            Player.isBeingChased = false;
            isAlive = false;
        }
    }
}