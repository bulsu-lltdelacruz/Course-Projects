using UnityEngine;

public class Bullet : MonoBehaviour
{
    [SerializeField] float speed = 20f;
    [SerializeField] LayerMask hitLayers;
    
    private float damage;
    private Rigidbody2D rb;
    private Vector2 lastPosition;

    public void Initialize(Vector3 direction, float damage)
    {
        this.damage = damage;
        rb = GetComponent<Rigidbody2D>();
        Vector2 direction2D = new Vector2(direction.x, direction.z).normalized;
        transform.right = direction2D; 
        rb.linearVelocity = transform.right * speed;
        
        lastPosition = transform.position;
        Destroy(gameObject, 1f);
    }

    void FixedUpdate()
    {
        // Calculate how far the bullet moved this physics frame
        Vector2 currentPosition = transform.position;
        Vector2 travelDirection = currentPosition - lastPosition;
        float distance = travelDirection.magnitude;

        if (distance > 0)
        {
            // Raycast between the previous and current position
            RaycastHit2D hit = Physics2D.Raycast(lastPosition, travelDirection, distance, hitLayers);
            if (hit.collider != null)
            {
                HandleCollision(hit.collider.gameObject, hit.normal);
            }
        }

        lastPosition = currentPosition;
    }

    private void HandleCollision(GameObject hitObject, Vector2 normal)
    {
        if(hitObject.name == "hitbox")
        {
            hitObject.GetComponentInParent<IDamageable>().Damage(damage, normal);
        }
        Destroy(gameObject);
    }

    // Keep these as backups for slower speeds or static overlaps
    //void OnTriggerEnter2D(Collider2D other) { if(hitLayers == (hitLayers | (1 << other.gameObject.layer))) HandleCollision(other.gameObject); }
}
