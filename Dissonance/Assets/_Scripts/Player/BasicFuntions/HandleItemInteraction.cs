using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.InputSystem;
using UnityEngine.UIElements;

public class HandleItemInteraction : MonoBehaviour
{
    Player player;
    Item itemInRange;
    public Door doorInRange;
    public Interactable interactableInRange;
    public SaveSpot saveSpotInRange;
    public IPuzzleInteractable puzzleInRange;
    public EnemyReviveHandler bodyInRange;
    bool isInItemPickupRange = false;
    public bool isDoorInRange = false;
    public bool isInteractableInRange = false;
    public bool isSaveSpotInRange = false;
    public bool isPuzzleInRange = false;
    public bool isBodyInRange = false;
    //InventoryUI inventoryUI;
    void Start()
    {
        player = GetComponentInParent<Player>();
    }
    public void OnTriggerEnter2D(Collider2D other)
    {
        if (other.CompareTag("Item"))
        {
            isInItemPickupRange = true;
            itemInRange = other.GetComponent<Item>();
            ShowPickupButton();
        }else if (other.CompareTag("Door"))
        {
            isDoorInRange = true;
            doorInRange = other.GetComponent<Door>();
            ShowPickupButton();
        }else if (other.CompareTag("Interactable"))
        {
            isInteractableInRange = true;
            interactableInRange = other.GetComponent<Interactable>();
            ShowPickupButton();
        }else if (other.CompareTag("SaveSpot"))
        {
            isSaveSpotInRange = true;
            saveSpotInRange = other.GetComponent<SaveSpot>();
            ShowPickupButton();
        }else if (other.CompareTag("Puzzle"))
        {
            isPuzzleInRange = true;
            puzzleInRange = other.GetComponent<IPuzzleInteractable>();
            ShowPickupButton();
        }else if (other.CompareTag("EnemyBody"))
        {
            isBodyInRange = true;
            bodyInRange   = other.GetComponentInParent<EnemyReviveHandler>();
        }
        else if (other.CompareTag("ItemBox"))
        {
            ShowPickupButton();
        }
    }
    void OnTriggerStay2D(Collider2D other)
    {
        if (other.CompareTag("Item"))
        {
            isInItemPickupRange = true;
            itemInRange = other.GetComponent<Item>();
            ShowPickupButton();
        }else if (other.CompareTag("Door"))
        {
            isDoorInRange = true;
            doorInRange = other.GetComponent<Door>();
            ShowPickupButton();
        }else if (other.CompareTag("Interactable"))
        {
            isInteractableInRange = true;
            interactableInRange = other.GetComponent<Interactable>();
            ShowPickupButton();
        }
        else if (other.CompareTag("SaveSpot"))
        {
            isSaveSpotInRange = true;
            saveSpotInRange = other.GetComponent<SaveSpot>();
            ShowPickupButton();
        }
        else if (other.CompareTag("Puzzle"))
        {
            isPuzzleInRange = true;
            puzzleInRange = other.GetComponent<IPuzzleInteractable>();
            ShowPickupButton();
        }
        else if (other.CompareTag("EnemyBody"))
        {
            isBodyInRange = true;
            bodyInRange   = other.GetComponentInParent<EnemyReviveHandler>();
        }
        else if (other.CompareTag("ItemBox"))
        {
            //isBodyInRange = false;
            //bodyInRange   = null;
            ShowPickupButton();
        }
    }

    public void OnTriggerExit2D(Collider2D other)
    {
        if (other.CompareTag("Item"))
        {
            isInItemPickupRange = false;
            itemInRange = null;
            HidePickupButton();
        }else if (other.CompareTag("Door"))
        {
            isDoorInRange = false;
            doorInRange = null;
            HidePickupButton();
        }else if (other.CompareTag("Interactable"))
        {
            isInteractableInRange = false;
            interactableInRange = null;
            HidePickupButton();
        }else if (other.CompareTag("SaveSpot"))
        {
            isSaveSpotInRange = false;
            saveSpotInRange = null;
            HidePickupButton();
        }
        else if (other.CompareTag("Puzzle"))
        {
            isPuzzleInRange = false;
            puzzleInRange = null;
            HidePickupButton();
        }else if (other.CompareTag("EnemyBody"))
        {
            isBodyInRange = false;
            bodyInRange   = null;
        }
        else if (other.CompareTag("ItemBox"))
        {
            HidePickupButton();
        }
    }
    private void ShowPickupButton()
    {
        MobileControls mc = FindAnyObjectByType<MobileControls>();
        if(mc != null)
            mc.interactButton.style.display = DisplayStyle.Flex;
    }
    private void HidePickupButton()
    {
        MobileControls mc = FindAnyObjectByType<MobileControls>();
        if(mc != null)
            mc.interactButton.style.display = DisplayStyle.None;
    }
    
    public void pickUpItem()
    {
        if (isPuzzleInRange)
        {
            puzzleInRange.OpenPuzzle();
            return;
        }
        if (isInItemPickupRange)
        {
            if(itemInRange.itemPickUpType == ItemPickUpType.Dialogue)
            {
            }
            else if(itemInRange.itemPickUpType == ItemPickUpType.Photo)
            {
                
            }
            else if(itemInRange.itemPickUpType == ItemPickUpType.PhotoAndDialogue)
            {
                //UIImage.OpenPhoto(itemInRange.images[0]);
                UIDialogueBox.OpenDialogue(itemInRange.dialogue, itemInRange.images);
            }
            else if(itemInRange.itemPickUpType == ItemPickUpType.PhotoAndDialogueAndChoices)
            {
                //UIImage.OpenPhoto(itemInRange.images);
                UIDialogueBox.OpenDialogue(itemInRange.dialogue, 
                itemInRange.choices, 
                itemInRange.choiceString, 
                itemInRange.endingDialogueAfterChoosingPositive,
                itemInRange.endingDialogueAfterChoosingNegative,
                itemInRange.images,
                this);
            }
            else if(itemInRange.itemPickUpType == ItemPickUpType.ShowNothing)
            {
                // Check if there's enough space for the FULL amount before adding anything
                if (!CanFitAllItems(itemInRange.itemObject, itemInRange.amount))
                {
                    UIDialogueBox.OpenDialogue(new string[]{"Inventory can't fit all items"});
                    return;
                }

                if(player.inventory.AddItem(itemInRange.itemObject, itemInRange.amount))
                {
                    player.playerSounds.PlayPickUpItem(S_VolumeStats.pickUpItem);
                    itemInRange.DestroySelf();
                }
            }
        }else if (isDoorInRange)
        {
            doorInRange.SetInteracted(true);
            if(doorInRange.interactableType == InteractableType.UnOpenable)
            {
                PlayerSounds.instance.Play(doorInRange.GetOpenLockedDoorSound(), doorInRange.GetVolume());
                doorInRange.SetUnopenable();
            }
            
            if(doorInRange.interactableType == InteractableType.Unlocked)
            {
                
                if(doorInRange.GetOpenDoorSound()!= null)
                    PlayerSounds.instance.Play(doorInRange.GetOpenDoorSound(), doorInRange.GetVolume());
                doorInRange.SetLockedState(true);
                doorInRange.OpenDoor(player);
                return;
            }
            if(doorInRange.interactableType == InteractableType.UnlockableFromOtherSide)
            {
                if(doorInRange.GetOpenDoorSound()!= null)
                    PlayerSounds.instance.Play(doorInRange.GetOpenLockedDoorSound(), doorInRange.GetVolume());
                    
                doorInRange.SetLockedState(false);
                doorInRange.OpenDoor(player);
                HandleDoorItemPickupType();
                return;
            }
            player.input.InventoryClose(new InputAction.CallbackContext());
           HandleDoorItemPickupType();
            
        }else if (isInteractableInRange)
        {
            if(interactableInRange.itemPickUpType == ItemPickUpType.Dialogue)
            {
                UIDialogueBox.OpenDialogue(interactableInRange.dialogue);
            }
            else if(interactableInRange.itemPickUpType == ItemPickUpType.Photo)
            {
                UIImage.OpenPhoto(interactableInRange.images[0]);
            }
            else if(interactableInRange.itemPickUpType == ItemPickUpType.PhotoAndDialogue)
            {
                //UIImage.OpenPhoto(interactableInRange.images[0]);
                UIDialogueBox.OpenDialogue(interactableInRange.dialogue,
                interactableInRange.images);
            }
            else if(interactableInRange.itemPickUpType == ItemPickUpType.PhotoAndDialogueAndChoices)
            {
                //UIImage.OpenPhoto(interactableInRange.images);
                UIDialogueBox.OpenDialogue(interactableInRange.dialogue, 
                interactableInRange.choices, 
                interactableInRange.choiceString, 
                interactableInRange.endingDialogueAfterChoosingPositive,
                interactableInRange.endingDialogueAfterChoosingNegative,
                interactableInRange.images,
                this);
            }
            else if(interactableInRange.itemPickUpType == ItemPickUpType.ShowNothing)
            {
                
            }
            //interactableInRange.ShowDialogue();
        }
        else if (isSaveSpotInRange)
        {
            MobileControls mc = FindAnyObjectByType<MobileControls>();
            if(mc!=null)
                mc.RemoveControls();

            player.saveScreen.parent.style.display = DisplayStyle.Flex;
            PlayerInputs.UI.Enable();
            PlayerInputs.Player.Disable();
            player.input.isUI = true;
            Time.timeScale = 0f;
            player.isSaving = true;
        }
    }
    private void HandleDoorItemPickupType()
    {
        if(doorInRange.itemPickUpType == ItemPickUpType.Dialogue)
            {
                if(doorInRange.GetOpenLockedDoorSound()!= null)
                    PlayerSounds.instance.Play(doorInRange.GetOpenLockedDoorSound(), doorInRange.GetVolume());
                UIDialogueBox.OpenDialogue(doorInRange.dialogue);
            }
            else if(doorInRange.itemPickUpType == ItemPickUpType.Photo)
            {
                if(doorInRange.GetOpenLockedDoorSound()!= null)
                    PlayerSounds.instance.Play(doorInRange.GetOpenLockedDoorSound(), doorInRange.GetVolume());
                UIImage.OpenPhoto(doorInRange.images[0]);
            }
            else if(doorInRange.itemPickUpType == ItemPickUpType.PhotoAndDialogue)
            {
                if(doorInRange.GetOpenLockedDoorSound()!= null)
                    PlayerSounds.instance.Play(doorInRange.GetOpenLockedDoorSound(), doorInRange.GetVolume());
                    if(doorInRange is AnimalPuzzle)
                    {
                        UIDialogueBox.OpenDialogue(doorInRange.dialogue,
                        doorInRange.images, "");
                    }
                    else
                    {
                        UIDialogueBox.OpenDialogue(doorInRange.dialogue,
                        doorInRange.images);
                    }
                
            }
            else if(doorInRange.itemPickUpType == ItemPickUpType.PhotoAndDialogueAndChoices)
            {
                if(doorInRange.GetOpenLockedDoorSound()!= null)
                    PlayerSounds.instance.Play(doorInRange.GetOpenLockedDoorSound(), doorInRange.GetVolume());
                    if(doorInRange is AnimalPuzzle)
                {
                    UIDialogueBox.OpenDialogue(doorInRange.dialogue, 
                    doorInRange.choices, 
                    doorInRange.choiceString, 
                    doorInRange.endingDialogueAfterChoosingPositive,
                    doorInRange.endingDialogueAfterChoosingNegative,
                    doorInRange.images,
                    doorInRange,
                    this,"");
                }else
                {
                    UIDialogueBox.OpenDialogue(doorInRange.dialogue, 
                    doorInRange.choices, 
                    doorInRange.choiceString, 
                    doorInRange.endingDialogueAfterChoosingPositive,
                    doorInRange.endingDialogueAfterChoosingNegative,
                    doorInRange.images,
                    doorInRange,
                    this);
                }
                
            }
            else if(doorInRange.itemPickUpType == ItemPickUpType.ShowNothing)
            {
                if(doorInRange.GetOpenLockedDoorSound()!= null)
                    PlayerSounds.instance.Play(doorInRange.GetOpenLockedDoorSound(), doorInRange.GetVolume());
            }
            doorInRange.OpenDoor(player);
            /*if(doorInRange.interactableType == InteractableType.Locked)
            {
                doorInRange.ShowDialogue();
                return;
            }*/
            return;
    }
    public bool CanFitAllItems(ItemObject item, int amount)
    {
        int remaining = amount;

        foreach (var kvp in player.inventory.Container)
        {
            if (remaining <= 0) break;
            InventorySlot slot = kvp.Value;
            if (slot.isTaken && slot.item == item)
            {
                int canFit = item.maxStackSize - slot.amount;
                remaining -= Mathf.Max(0, canFit);
            }
        }

        foreach (var kvp in player.inventory.Container)
        {
            if (remaining <= 0) break;
            InventorySlot slot = kvp.Value;
            if (!slot.isTaken)
                remaining -= item.maxStackSize;
        }

        return remaining <= 0;
    }
    public bool TryAddItemToInventory()
    {
        if (itemInRange == null) return false;

        if (!CanFitAllItems(itemInRange.itemObject, itemInRange.amount))
        {
            UIDialogueBox.OpenDialogue(new string[]{"Inventory can't fit all items."});
            return false;
        }
            

        if (player.inventory.AddItem(itemInRange.itemObject, itemInRange.amount))
        {
            player.playerSounds.PlayPickUpItem(S_VolumeStats.pickUpItem);
            itemInRange.DestroySelf();
            return true;
        }
        return false;
    }
    public void AddItemToInventory()
    {
        if (itemInRange == null) return;

        if (!CanFitAllItems(itemInRange.itemObject, itemInRange.amount))
        {
            UIDialogueBox.OpenDialogue(new string[]{"Inventory is full"});
            return;
        }

        if(player.inventory.AddItem(itemInRange.itemObject, itemInRange.amount))
        {
            player.playerSounds.PlayPickUpItem(S_VolumeStats.pickUpItem);
            itemInRange.DestroySelf();
        }
    }
    public bool AddItemToInventory(ItemObject item)
    {
        
        if(player.inventory.AddItem(item, 1))
        {
            player.playerSounds.PlayPickUpItem(S_VolumeStats.pickUpItem);
            return true;
        }else
        {
            return false;
        }
    }
}