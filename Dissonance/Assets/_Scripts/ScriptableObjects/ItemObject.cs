using System;
using NaughtyAttributes;
using UnityEngine;

#if UNITY_EDITOR
using UnityEditor;
#endif

public enum Itemtype
{
    Default,
    Key,
    Ammunition,
    Meelee,
    Ranged,
    Healing
}

public enum EquipType
{
    None,
    Weapon,
    Key
}

public enum AmmoType
{
    None,
    PistolAmmo,
    RifleAmmo,
    ShotgunAmmo
    // add more as needed
}

public enum Location
{
    MedicalWard
}

// Added this so you can right-click in the Project window to create the item
[CreateAssetMenu(fileName = "New Item", menuName = "Inventory/ItemObject")]
public class ItemObject : ScriptableObject
{
    // Renamed to avoid hiding the base Object.name property
    public string itemName; 
    public Texture2D image;
    public GameObject prefab;
    public string animationTrigger;
    public Location location;
    public Itemtype itemType;
    public bool shouldDestroyAfterUse;
    
    // Assuming MedicalWardKeyItemID is an enum defined elsewhere in your project
    [ShowIf("ShouldShowMedicalWard")] public MedicalWardKeyItemID keyName; 
    
    public EquipType equipType;
    
    [TextArea(15, 20)]
    public string description;

    // ── Ammo ─────────────────────────────────────────────────────────────────
    // For weapons: which ammo type does this gun use
    [ShowIf("IsWeapon")] public AmmoType requiredAmmoType;
    
    // For ammo items: what type is this ammo
    [ShowIf("IsAmmo")] public AmmoType ammoType;
    
    // Max bullets this weapon can hold per reload
    [ShowIf("IsWeapon")] public int maxAmmoPerReload = 10;
    [ShowIf("IsStackable")] public int maxStackSize = 1;

    bool IsStackable() => itemType == Itemtype.Ammunition || itemType == Itemtype.Default||itemType == Itemtype.Healing;

    bool ShouldShowMedicalWard()
    {
        return (itemType == Itemtype.Key && location == Location.MedicalWard);
    }
    
    bool IsWeapon() => equipType == EquipType.Weapon;
    bool IsAmmo()   => itemType == Itemtype.Ammunition;

    // ── Saving & Serialization ───────────────────────────────────────────────

    // OnValidate is called by Unity every time a value is changed in the Inspector
    private void OnValidate()
    {
    #if UNITY_EDITOR
            // Only run this in the Editor when the game is NOT playing
            if (!Application.isPlaying)
            {
                // Forces Unity to recognize the object has been changed and needs saving
                EditorUtility.SetDirty(this);
            }
    #endif
    }

    // Call this manually if you are modifying the ScriptableObject via code in the Editor
    public void ForceSave()
    {
    #if UNITY_EDITOR
            EditorUtility.SetDirty(this);
            AssetDatabase.SaveAssets(); // Forces an immediate write to disk
    #endif
    }
}