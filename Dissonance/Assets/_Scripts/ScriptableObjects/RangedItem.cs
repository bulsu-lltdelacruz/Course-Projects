using UnityEngine;

[CreateAssetMenu(fileName = "ItemObject", menuName = "Scriptable Objects/Items/RangedItem")]
public class RangedItem : ItemObject
{
    //=================add implementation, then can make new script with diff stats
    [Header("Damage/Range")]
    public float damageFloor;
    public float damageCeiling;
    public float range;
    [Header("Timings")]
    public float attackDuration;
    public float timingFloor;
    public float timingCeiling;
    public void Awake()
    {
        itemType = Itemtype.Ranged;
    }
}
