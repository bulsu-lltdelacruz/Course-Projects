using UnityEngine;

public class Events: MonoBehaviour
{
    Player player;
    public delegate void OnAim();
    public delegate void OnUnAim();
    public OnAim onAim;
    public OnUnAim onUnAim;
    void OnEnable()
    {
        
    }
    public void Initialize()
    {
        player = GetComponent<Player>();
        onAim += player.inventory.equipWeapon.Aim;
        onUnAim += player.inventory.equipWeapon.UnAim;
    }
    void OnDisable()
    {
        onAim -=player.inventory.equipWeapon.Aim;
        onUnAim -= player.inventory.equipWeapon.UnAim;
    }
}
