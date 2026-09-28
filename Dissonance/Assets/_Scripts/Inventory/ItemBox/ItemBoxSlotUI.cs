using UnityEngine.UIElements;

public class ItemBoxSlotUI : VisualElement
{
    public ItemObject item;
    public int amount;
    private ItemBoxUI itemBoxUI;

    public ItemBoxSlotUI(ItemObject item, int amount, ItemBoxUI ui)
    {
        this.item       = item;
        this.amount     = amount;
        this.itemBoxUI  = ui;

        AddToClassList("itemBoxSlot");
        Label amountLabel = new Label();
        VisualElement image = new VisualElement();

        

        if (item != null)
        {
            image.style.backgroundImage = item.image;
            image.AddToClassList("itemBoxImage");
            Add(image);
            amountLabel.AddToClassList("itemBoxText");
            amountLabel.text = "x"+amount.ToString();
            Add(amountLabel);
        }

        RegisterCallback<ClickEvent>(evt => itemBoxUI.OnBoxSlotClicked(this));
        RegisterCallback<MouseEnterEvent>(OnHover);
        RegisterCallback<MouseLeaveEvent>(OnLeave);
    }
    

    void OnHover(MouseEnterEvent evt)
    {
        if (item == null) return;
        InventoryObject.itemDescriptionText.text = item.description;
        InventoryObject.itemPicture.style.backgroundImage = item.image;
        PlayerSounds.instance.PlayHoverButton(S_VolumeStats.buttonHover);
    }

    void OnLeave(MouseLeaveEvent evt)
    {
        InventoryObject.itemDescriptionText.text = "";
        InventoryObject.itemPicture.style.backgroundImage = null;
    }
}