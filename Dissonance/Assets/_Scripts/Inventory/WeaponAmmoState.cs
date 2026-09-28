using System;
using UnityEngine;

public static class WeaponAmmoState
{
    public static int currentAmmo  = 0;
    public static int maxAmmo      = 8;
    public static bool isReloading = false;

    // ← Any listener (like InventorySlot) can subscribe to this
    public static event Action onAmmoChanged;

    public static bool HasAmmo() => currentAmmo > 0;

    public static void UseAmmo()
    {
        currentAmmo = Mathf.Max(0, currentAmmo - 1);
        onAmmoChanged?.Invoke();
    }

    public static void SetWeapon(ItemObject weapon)
    {
        maxAmmo = weapon.maxAmmoPerReload;
        onAmmoChanged?.Invoke();
    }

    public static void SetAmmo(int amount)
    {
        currentAmmo = amount;
        onAmmoChanged?.Invoke();
    }
    public static void NotifyAmmoChanged()
    {
        onAmmoChanged?.Invoke();
    }
}