using UnityEngine;

public class EnemyReviveHandler : MonoBehaviour
{
    [Header("Revive Settings")]
    [SerializeField] public string enemyId;
    [SerializeField] [Range(0f, 1f)] private float reviveChance = 0.3f;
    [SerializeField] private int minDoorsUntilRevive = 3;
    [SerializeField] private int maxDoorsUntilRevive = 7;
    [SerializeField] private float speedMultiplierOnRevive = 1.3f;

    private bool isDead = false;
    private bool isBurned = false;
    public  bool willRevive = false;
    private bool hasRevived = false;
    private int  doorsUntilRevive = 0;

    private bool pendingDeadState = false;
    public bool IsLoadingDeadState { get; private set; } = false;

    private Enemy enemy;
    [SerializeField] private CircleCollider2D bodyCollider;
    [SerializeField] private GameObject burnCollider;
    [SerializeField] private CircleCollider2D zombie;
    [SerializeField] private CircleCollider2D detectionRange;
    [SerializeField] private CircleCollider2D attackRange;
    [SerializeField] private CircleCollider2D playerCollider;

    void Awake()
    {
        enemy = GetComponent<Enemy>();
    }
    void OnTriggerEnter2D(Collider2D collision)
    {
        
    }
    /*void Start()
    {
        EnemyReviveTracker.instance?.RegisterEnemy(this);

        // Apply dead state AFTER Enemy.Start() has initialized the state machine
        if (pendingDeadState)
            ApplyDeadState();
    }*/
    public void Initialize()
    {
        EnemyReviveTracker.instance?.RegisterEnemy(this);
        if (pendingDeadState)
            ApplyDeadState();
        else
            DisableForSpawn();
    }
    private void DisableForSpawn()
    {
        gameObject.SetActive(false);
    }

    void OnDestroy()
    {
        EnemyReviveTracker.instance?.UnregisterEnemy(this);
    }

    public void OnEnemyDied()
    {
        isDead = true;
        Vector3 pos = enemy.transform.position;
        zombie.enabled = false;
        detectionRange.enabled = false;
        attackRange.enabled = false;
        playerCollider.enabled = false;
        burnCollider.SetActive(true);
        float rand = Random.value;
        if (!hasRevived && !isBurned && rand <= reviveChance)
        {
            willRevive = true;
            doorsUntilRevive = Random.Range(minDoorsUntilRevive, maxDoorsUntilRevive + 1);
        }
        else
        {
            willRevive = false;
        }

        gameObject.tag = "EnemyBody";
        if (bodyCollider != null)
            bodyCollider.isTrigger = true;
    }

    public void OnDoorEntered()
    {
        if (!isDead || !willRevive || isBurned) return;
        doorsUntilRevive--;
        if (doorsUntilRevive <= 0)
            Revive();
    }

    private void Revive()
    {
        isDead = false;
        willRevive = false;
        hasRevived = true;
        gameObject.tag = "Enemy";
        
        enemy.hitbox.SetActive(true);

        enemy.animator.speed = 1;
        enemy.animator.ResetTrigger("triggerDie");
        enemy.animator.ResetTrigger("triggerHit");
        enemy.animator.SetBool("isRunning", false);
        enemy.animator.SetBool("isWalking", false);

        enemy.animator.Play("idle placeholder", 0, 0f);
        
        zombie.enabled = true;
        detectionRange.enabled = true;
        attackRange.enabled = true;
        playerCollider.enabled = true;
        burnCollider.SetActive(false);

        if (bodyCollider != null)
            bodyCollider.isTrigger = false;

        enemy.currentHealth = enemy.maxHealth;
        enemy.isAlive = true;

        if (enemy != null)
            enemy.navMeshAgent.speed = 3.5f;

        enemy.StartCoroutine(ReviveStateChangeDelay());
    }

    private System.Collections.IEnumerator ReviveStateChangeDelay()
    {
        yield return null;
        enemy.movementStateMachines.ChangeStateFromRevive(enemy.patrolState);
    }

    public void BurnBody()
    {
        if (!isDead) return;
        isBurned = true;
        willRevive = false;
    }

    public bool IsDead => isDead;
    public bool IsBurned => isBurned;

    private void ApplyDeadState()
    {
        pendingDeadState = false;
        IsLoadingDeadState = true;
        enemy.isAlive = false;
        enemy.currentHealth = 0;
        gameObject.tag = "EnemyBody";
        zombie.enabled = false;
        detectionRange.enabled = false;
        attackRange.enabled = false;
        playerCollider.enabled = false;
        burnCollider.SetActive(true);

        if (bodyCollider != null)
            bodyCollider.isTrigger = true;

        enemy.gameObject.SetActive(true);
        enemy.movementStateMachines.ChangeStateFromRevive(enemy.dieState);
        enemy.navMeshAgent.ResetPath();
        enemy.navMeshAgent.velocity = Vector3.zero;
        enemy.animator.speed = 100;
        IsLoadingDeadState = false;
    }

    public EnemyReviveData GetSaveData()
    {
        Vector3 pos = enemy.transform.position;
        return new EnemyReviveData(enemyId)
        {
            isDead = this.isDead,
            isBurned = this.isBurned,
            willRevive = this.willRevive,
            doorsUntilRevive = this.doorsUntilRevive,
            hasRevived = this.hasRevived,
            deathPosX = pos.x,
            deathPosY = pos.y,
            deathPosZ = pos.z
        };
    }

    public void LoadSaveData(EnemyReviveData data)
    {
        isDead = data.isDead;
        isBurned = data.isBurned;
        willRevive = data.willRevive;
        doorsUntilRevive = data.doorsUntilRevive;
        hasRevived = data.hasRevived;

        if (isDead)
        { 
            enemy.transform.position = new Vector3(
                data.deathPosX,
                data.deathPosY,
                data.deathPosZ);
            enemy.initialPosition = enemy.transform.position;
            pendingDeadState = true;
        }
    }
}