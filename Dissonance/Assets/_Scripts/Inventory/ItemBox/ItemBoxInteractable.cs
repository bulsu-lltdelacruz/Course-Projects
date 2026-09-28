using UnityEngine;

public class ItemBoxInteractable : MonoBehaviour
{
    [SerializeField] private ItemBoxObject itemBox;
    [SerializeField] private ItemBoxUI itemBoxUI;
    [SerializeField] private float interactRange = 2f;

    private Player player;
    private bool playerInRange = false;

    void Start()
    {
        player = FindObjectOfType<Player>();
    }
    public ItemBoxUI GetItemBoxUI()
    {
        return itemBoxUI;
    }
    void Update()
    {
        float dist = Vector3.Distance(transform.position, player.transform.position);
        playerInRange = dist <= interactRange;
    }

    // Call this from your existing interact input
    public bool IsPlayerInRange() => playerInRange;

    public void OpenItemBox()
    {
        if (!playerInRange) return;
        itemBoxUI.Open(itemBox, player.inventory);
    }
    
    void OnTriggerEnter2D(Collider2D other)
    {
        if (other.TryGetComponent<PlayerInputs>(out var inputs))
            inputs.nearbyItemBox = this;
    }
    void OnTriggerExit2D(Collider2D other)
    {
        if (other.TryGetComponent<PlayerInputs>(out var inputs))
            inputs.nearbyItemBox = null;
    }
}