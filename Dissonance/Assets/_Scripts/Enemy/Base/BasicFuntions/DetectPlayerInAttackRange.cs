using UnityEngine;

public class DetectPlayerInAttackRange : MonoBehaviour
{
    Enemy enemy;
    IDamageable playerDamageable;
    Transform playerTransform;
    public bool isPlayerInAttackRange = false;
    void Start()
    {
        enemy = GetComponentInParent<Enemy>();
    }
    void OnTriggerEnter2D(Collider2D other)
    {
        if(other.CompareTag("Player"))
        {
            isPlayerInAttackRange = true;
            playerDamageable = other.transform.gameObject.GetComponent<IDamageable>();
            playerTransform = other.transform;
        }
    }
    void OnTriggerStay2D(Collider2D other)
    {
        if(other.CompareTag("Player"))
        {
            isPlayerInAttackRange = true;
            playerDamageable = other.transform.gameObject.GetComponent<IDamageable>();
            playerTransform = other.transform;
        }
    }
    void OnTriggerExit2D(Collider2D other)
    {
        if(other.CompareTag("Player"))
        {
            isPlayerInAttackRange = false;
            playerTransform = null;
            playerDamageable = null;
        }
    }
    public void Damage()
    {
        if(isPlayerInAttackRange)
        {
            Vector3 normalized = enemy.transform.position - playerTransform.position;
            normalized = normalized.normalized;
            playerDamageable.Damage(enemy.GetRandomDamageAmount(), normalized, enemy);
        }
            
    }
}
