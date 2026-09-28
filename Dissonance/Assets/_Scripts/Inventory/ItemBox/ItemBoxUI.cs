using System.Collections.Generic;
using UnityEngine;
using UnityEngine.InputSystem;
using UnityEngine.UIElements;

public class ItemBoxUI : MonoBehaviour
{
    [SerializeField] private UIDocument uiDocument;
    [SerializeField] private ItemBoxObject itemBox;

    private VisualElement root;
    private VisualElement itemBoxGrid;
    private VisualElement inventoryGrid;
    private VisualElement parent;
    private VisualElement close;

    private ItemBoxObject currentBox;
    private InventoryObject currentInventory;

    private ItemBoxSlotUI selectedBoxSlot;
    private InventorySlot selectedInventorySlot;

    private List<ItemBoxSlotUI> boxSlots = new List<ItemBoxSlotUI>();

    public bool IsOpen { get; private set; }

    void Awake()
    {
        root = uiDocument.rootVisualElement;
        parent = root.Q<VisualElement>("parent");
        itemBoxGrid = root.Q<VisualElement>("item-box");
        inventoryGrid = root.Q<VisualElement>("grid");
        close = root.Q<VisualElement>("close-itembox");
        close.RegisterCallback<ClickEvent>(CloseItemBox);
        parent.AddToClassList("parent-close");
        disablePickingMode();
    }
    void OnDisable()
    {
        close.UnregisterCallback<ClickEvent>(CloseItemBox);
    }
    private void CloseItemBox(ClickEvent e)
    {
        Close();
    }
    public void Open(ItemBoxObject box, InventoryObject inventory)
    {
        currentBox       = box;
        currentInventory = inventory;
        IsOpen           = true;

        parent.RemoveFromClassList("parent-close");
        Time.timeScale = 0f;

        RefreshAll();
    }

    public void Close()
    {
        IsOpen = false;
        parent.AddToClassList("parent-close");
        Time.timeScale = 1f;
        
        PlayerInputs.UI.Disable();
        PlayerInputs.Player.Enable();
        Player player = FindAnyObjectByType<Player>();
        if(player!=null)
            player.input.isUI = false;
        
        PlayerSounds.instance.PlayClickButton(S_VolumeStats.buttonClick);
        
        /*PlayerInputs.UI.Disable();
        PlayerInputs.Player.Enable();
        PlayerInputs.isUI = false;*/
        ClearSlots();
    }


    void RefreshAll()
    {
        RefreshBoxSlots();
        RefreshInventorySlots();
    }

    void RefreshBoxSlots()
    {
        itemBoxGrid.Clear();
        boxSlots.Clear();

        foreach (ItemBoxEntry entry in currentBox.storedItems)
        {
            ItemObject item = ItemObjectFinder.instance.FindItemObjectByName(entry.itemName);
            if (item == null) continue;

            ItemBoxSlotUI slot = new ItemBoxSlotUI(item, entry.amount, this);
            boxSlots.Add(slot);

            //VisualElement slotParent = new VisualElement();
            //slotParent.AddToClassList("itemSlotParent");
            //slotParent.Add(slot);
            itemBoxGrid.Add(slot);
        }

        //ItemBoxSlotUI emptySlot = new ItemBoxSlotUI(null, 0, this);
        //boxSlots.Add(emptySlot);
        //VisualElement emptyParent = new VisualElement();
        //emptyParent.AddToClassList("itemSlotParent");
        //emptyParent.Add(emptySlot);
        //itemBoxGrid.Add(emptyParent);
    }
    void disablePickingMode()
    {
        root.Query(className: "crt").ForEach(el => {
            el.pickingMode = PickingMode.Ignore;
        });
    }

    void RefreshInventorySlots()
    {
        inventoryGrid.Clear();

        foreach (var kvp in currentInventory.Container)
        {
            InventorySlot realSlot = kvp.Value;

            VisualElement proxy = new VisualElement();
            proxy.AddToClassList("itemSlot");

            if (realSlot.isTaken && realSlot.item != null)
            {
                proxy.style.backgroundImage = realSlot.item.image;
            }
            Label amountLabel = new Label(realSlot.amount>1?realSlot.amount.ToString():"");
            amountLabel.AddToClassList("amountLabel");
            proxy.Add(amountLabel);
            
            InventorySlot captured = realSlot;
            proxy.RegisterCallback<ClickEvent>(evt => OnInventorySlotClicked(captured));
            proxy.RegisterCallback<MouseEnterEvent>(evt =>
            {
                if (captured.item != null)
                {
                    
                    InventoryObject.itemDescriptionText.text = captured.item.description;
                    InventoryObject.itemPicture.style.backgroundImage = captured.item.image;
                    PlayerSounds.instance.PlayHoverButton(S_VolumeStats.buttonHover);
                }
            });
            proxy.RegisterCallback<MouseLeaveEvent>(evt =>
            {
                InventoryObject.itemDescriptionText.text = "";
                InventoryObject.itemPicture.style.backgroundImage = null;
            });

            VisualElement slotParent = new VisualElement();
            VisualElement slotFilter = new VisualElement();
            slotParent.AddToClassList("itemSlotParent");
            slotFilter.AddToClassList("itemSlotFilter");
            slotParent.Add(slotFilter);
            slotParent.Add(proxy);
            inventoryGrid.Add(slotParent);
        }
    }

    void ClearSlots()
    {
        inventoryGrid.Clear();
        itemBoxGrid.Clear();
        boxSlots.Clear();
    }

    void OnInventorySlotClicked(InventorySlot slot)
    {
        if (!slot.isTaken) return;
        
        slot.RemoveHover();

        currentBox.StoreItem(slot.item, slot.amount);
        InventoryObject.itemDescriptionText.text = "";
        InventoryObject.itemPicture.style.backgroundImage = null;
        slot.item   = null;
        slot.amount = 0;
        slot.amountLabel.text = "";
        slot.isTaken = false;
        slot.style.backgroundImage = null;

        PlayerSounds.instance.PlayClickButton(S_VolumeStats.buttonClick);
        RefreshAll();
    }

    public void OnBoxSlotClicked(ItemBoxSlotUI boxSlot)
    {
        if (boxSlot.item == null) return;
        bool added = currentInventory.AddItem(boxSlot.item, 1);
        if (!added)
        {
            UIDialogueBox.OpenDialogue(new string[] { "Your inventory is full." });
            return;
        }

        currentBox.TakeItem(boxSlot.item.name, 1, out _);

        PlayerSounds.instance.PlayClickButton(S_VolumeStats.buttonClick);
        RefreshAll();
    }
}