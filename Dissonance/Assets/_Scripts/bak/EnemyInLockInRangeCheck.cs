using System.Collections.Generic;
using UnityEngine;

public class EnemyInLockInRangeCheck : MonoBehaviour
{
   /* Enemy player;
    List<GameObject> enemiesInLockInRange;
    SphereCollider lockInRange;
    void Start()
    {
        enemiesInLockInRange = new List<GameObject>();
        player = GetComponentInParent<Enemy>();
        lockInRange = GetComponent<SphereCollider>();
        lockInRange.excludeLayers += LayerMask.GetMask("Ground");
        lockInRange.excludeLayers += LayerMask.GetMask("Wall");
    }
    void OnTriggerEnter(Collider other)
    {
        enemiesInLockInRange.Add(other.gameObject);
    }

    void OnTriggerExit(Collider other)
    {
        enemiesInLockInRange.Remove(other.gameObject);
    }
    public List<GameObject> GetEnemiesInLockInRange()
    {
        return enemiesInLockInRange;
    }*/
}
