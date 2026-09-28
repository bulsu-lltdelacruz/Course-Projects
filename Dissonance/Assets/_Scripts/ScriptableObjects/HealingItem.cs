using UnityEngine;

[CreateAssetMenu(fileName = "ItemObject", menuName = "Scriptable Objects/Items/HealingObject")]
public class HealingItem : ItemObject
{
    //=================add implementation, then can make new script with diff stats
    [SerializeField]public int healingValue;
    public void Awake()
    {
        itemType = Itemtype.Healing;
    }
}
