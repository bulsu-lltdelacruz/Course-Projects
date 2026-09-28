using System;
using Unity.VisualScripting;
using UnityEngine;

public class DetectPlayerInRange : MonoBehaviour
{
    SphereCollider playerInRange;
    Enemy enemy;
    public bool isPlayerInRange = false;
    public bool isWillChase = false;
    Transform enemyModelTransform;
    [NonSerialized] public GameObject player;
    [NonSerialized] public Vector3 playerLastPosition;
    LayerMask detectMask;
    void Start()
    {
        detectMask = LayerMask.GetMask("Wall", "Player");
        playerInRange = GetComponent<SphereCollider>();
    }
    private void OnDisable() {
        Player.isBeingChased = false;
    }
    public void Initialize(Enemy enemy)
    {
        this.enemy = enemy;
        enemyModelTransform = enemy.enemyModel.transform;
    }
    private void OnTriggerEnter2D(Collider2D other)
    {
        if (other.gameObject.CompareTag("Player"))
        {
            player = other.gameObject;
            playerLastPosition = player.transform.position;
            isPlayerInRange = true;
        }
    }
    void OnTriggerStay2D(Collider2D other)
    {
        if (other.gameObject.CompareTag("Player"))
        {
            player = other.gameObject;
            playerLastPosition = player.transform.position;
            isPlayerInRange = true;
        }
    }
    private void OnTriggerExit2D(Collider2D other) 
    {
        if(other.gameObject.CompareTag("Player"))
        {
            isPlayerInRange = false;
        }
    }
    public void TriggerChaseOnHit()
    {
        Player player = FindAnyObjectByType<Player>();
        playerLastPosition = player.transform.position;
        enemy.player = player.gameObject;
        enemy.movementStateMachines.ChangeState(enemy.chaseState);
        Player.isBeingChased = true;
    }
    void Update()
    {
        Debug.DrawRay(enemyModelTransform.position, new Vector3(enemyModelTransform.forward.x, enemyModelTransform.forward.z, 0), Color.red, 20.0f );

        if (isPlayerInRange && enemy.movementStateMachines.currentState != enemy.chaseState)
        {
            Vector2 enemyPos2D = enemyModelTransform.position;
            Vector2 playerPos2D = player.transform.position;
            Vector2 directionToPlayer = (playerPos2D - enemyPos2D).normalized;

            Vector2 enemyForward2D = new Vector2(enemyModelTransform.forward.x, enemyModelTransform.forward.z);

            RaycastHit2D hit = Physics2D.Raycast(enemyPos2D, directionToPlayer, 100f, detectMask);

            if (hit.collider != null)
            {
                float angle = Vector2.Angle(enemyForward2D, directionToPlayer);

                if (hit.collider.gameObject.CompareTag("Player") && 
                    angle < enemy.detectAngle && 
                    !(enemy.movementStateMachines.currentState is EnemyAttackState))
                {
                    playerLastPosition = player.transform.position;
                    enemy.player = player;
                    enemy.movementStateMachines.ChangeState(enemy.chaseState);
                    Player.isBeingChased = true;
                }
            }
        }
        /*if(isPlayerInRange && enemy.attackStateMachines.currentState != enemy.chaseState)
        {
                playerLastPosition = player.transform.position;
                enemy.player = player;
                enemy.attackStateMachines.ChangeState(enemy.chaseState);
                Debug.Log("habol");
        }*/
        
    }
}
