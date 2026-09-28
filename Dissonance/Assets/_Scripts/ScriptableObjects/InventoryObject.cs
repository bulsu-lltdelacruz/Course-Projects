using System;
using System.Collections.Generic;
using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.InputSystem;
using UnityEngine.UIElements;
using UnityEngine.Video;

[CreateAssetMenu(fileName = "InventoryObject", menuName = "Scriptable Objects/InventorySystem/Inventory")]
public class InventoryObject : ScriptableObject, IDataPersistence
{
    ///<summary>
    /// //////////////add how many inventory slots later
    /// </summary>
    private Key key = new Key();
    public Dictionary<int, InventorySlot> Container = new Dictionary<int, InventorySlot>();
    [NonSerialized] UIDocument UIDOC;
    [NonSerialized] public VisualElement root;
    VisualElement inventoryGrid;
    public ItemObject equipedItem;
    public Player player;
    //////===========WEAPON TRANSFORM==========
    ItemEquip itemEquipInfo;
    GameObject itemPrefab;
    IK_Base ikPistol;
    public EquipWeapon equipWeapon;
    public VisualElement parent;
    public VisualElement inventoryParent;
    
    public static Label itemDescriptionText;
    public static VisualElement itemPicture;

    public VisualElement health;
    public VisualElement healthFilter;

    public Label navigationLeft;
    public Label navigationRight;
    public Label pageLabel;

    public List<CombineRecipe> combineRecipes = new List<CombineRecipe>();
    [NonSerialized] public bool isCombining = false;
    [NonSerialized] public InventorySlot pendingCombineSlot = null;

    [NonSerialized] private ScrollView mapScrollView;
    [NonSerialized] private float savedScrollPosition = 0f;
    [NonSerialized] private int savedMapFloor       = 0;

    public void Initialize(UIDocument UIDOC, ItemEquip itemEquipInfo, IK_Base ikPistol, Player player)
    {
        foreach (var slot in Container.Values)
        {
            slot.RemoveListeners();
        }
        Container.Clear();
        key = new Key();
        this.UIDOC = UIDOC;
        this.itemEquipInfo = itemEquipInfo;
        this.ikPistol = ikPistol;
        this.player = player;
        equipWeapon = new EquipWeapon(itemEquipInfo, ikPistol, player);
        equipedItem = null;
        root = UIDOC.rootVisualElement;
        disablePickingMode();
        
        
        health = root.Q<VisualElement>("health");
        healthFilter = root.Q<VisualElement>("health-filter");
        health.style.backgroundImage = new StyleBackground(Background.FromRenderTexture(player.heartRate));
        
        parent = root.Q<VisualElement>("parent");
        inventoryParent = root.Q<VisualElement>("inventory-parent");
        inventoryGrid = root.Q<VisualElement>("grid");

        itemDescriptionText = root.Q<Label>("itemDescriptionText");
        itemPicture = root.Q<VisualElement>("itemPicture");

        navigationLeft = root.Q<Label>("navigation-left");
        navigationRight = root.Q<Label>("navigation-right");
        pageLabel = root.Q<Label>("page-label");
        root.Q<VisualElement>("close").RegisterCallback<ClickEvent>(Close);
        navigationLeft.RegisterCallback<ClickEvent>(Left);
        navigationRight.RegisterCallback<ClickEvent>(Right);

        root.Q<VisualElement>("up-floor").RegisterCallback<ClickEvent>(NextFloor);
        root.Q<VisualElement>("down-floor").RegisterCallback<ClickEvent>(NextFloor);

        mapScrollView = root.Q<ScrollView>("map-child-scroll");
        if (mapScrollView != null)
        {
            mapScrollView.verticalScroller.valueChanged += OnMapScrolled;
        }
        
        for (int i = 0; i < 6; i++)
        {
            Container.Add(key.GetKey(), new InventorySlot(this));
        }
        foreach (KeyValuePair<int, InventorySlot> slot in Container)
        {
            VisualElement slotParent = new VisualElement();
            VisualElement slotFilter = new VisualElement();
            slotParent.AddToClassList("itemSlotParent");
            slotFilter.AddToClassList("itemSlotFilter");
            slotParent.Add(slotFilter);
            slotParent.Add(slot.Value);
            inventoryGrid.Add(slotParent);
        }
    }
    private void OnMapScrolled(float value)
    {
        savedScrollPosition = value;
    }
    private void NextFloor(ClickEvent e)
    {
        GoToNextFloor();
    }
    public void CancelAllCombines()
    {
        foreach (KeyValuePair<int, InventorySlot> slot in Container)
        {
            slot.Value.OnCancel();
        }
    }
    private void Close(ClickEvent e)
    {
        player.input.InventoryClose(new InputAction.CallbackContext());
    }
    private void Left(ClickEvent e)
    {
        e.StopPropagation();
        if (player == null || player.input == null)
            return;

        player.input.OnNavigateLeft(new InputAction.CallbackContext());
    }
    private void Right(ClickEvent e)
    {
        e.StopPropagation();
        if (player == null || player.input == null)
            return;

        player.input.OnNavigateRight(new InputAction.CallbackContext());
    }
    public void GoToNextFloor(FloorLocation floor)
    {
        VisualElement _1stFloor = root.Q<VisualElement>("map-content");
        VisualElement _2ndFloor = root.Q<VisualElement>("map-content-2nd");
        if(floor.Equals(FloorLocation._2ndFloor))
        {
            _1stFloor.style.display = DisplayStyle.None;
            _2ndFloor.style.display = DisplayStyle.Flex;
            root.Q<Label>("page-label").text = "2nd Floor";
            savedMapFloor = 1;

        }else if(floor.Equals(FloorLocation._1stFloor))
        {
            _2ndFloor.style.display = DisplayStyle.None;
            _1stFloor.style.display = DisplayStyle.Flex;
            root.Q<Label>("page-label").text = "1st Floor";
            savedMapFloor = 0;
        }
    }
    public void CacheScrollPosition()
    {
        FloorLocation floor = savedMapFloor == 1
        ? FloorLocation._2ndFloor
        : FloorLocation._1stFloor;
        //GoToNextFloor(floor);
        if (mapScrollView != null)
        {
            mapScrollView.schedule.Execute(() =>
            {
                mapScrollView.verticalScroller.value = savedScrollPosition;
            }).StartingIn(100);
        }
    }
    public void GoToNextFloor()
    {
        VisualElement _1stFloor = root.Q<VisualElement>("map-content");
        VisualElement _2ndFloor = root.Q<VisualElement>("map-content-2nd");
        if(_1stFloor.style.display.value == DisplayStyle.Flex)
        {
            _1stFloor.style.display = DisplayStyle.None;
            _2ndFloor.style.display = DisplayStyle.Flex;
            root.Q<Label>("page-label").text = "2nd Floor";
            savedMapFloor = 1;

        }else if(_2ndFloor.style.display.value == DisplayStyle.Flex)
        {
            _2ndFloor.style.display = DisplayStyle.None;
            _1stFloor.style.display = DisplayStyle.Flex;
            root.Q<Label>("page-label").text = "1st Floor";
            savedMapFloor = 0;
        }
    }
    void disablePickingMode()
    {
        root.Query(className: "crt").ForEach(el => {
            el.pickingMode = PickingMode.Ignore;
        });
    }
    public void OnDisable()
    {
        UnEquipItem();
        foreach (KeyValuePair<int, InventorySlot> slot in Container)
        {
            if (slot.Value != null)
            {
                slot.Value.RemoveListeners();
                if(slot.Value.item!=null && slot.Value.item.equipType == EquipType.Weapon)
                    slot.Value.OnUnEquip();
            }
        }

        Container.Clear();
        player = null; 
        UIDOC = null;

        if (root != null)
        {
            root.Q<VisualElement>("up-floor")?.UnregisterCallback<ClickEvent>(NextFloor);
            root.Q<VisualElement>("down-floor")?.UnregisterCallback<ClickEvent>(NextFloor);
            root.Q<VisualElement>("close")?.UnregisterCallback<ClickEvent>(Close);
        }

        if (navigationLeft != null)
            navigationLeft.UnregisterCallback<ClickEvent>(Left);

        if (navigationRight != null)
            navigationRight.UnregisterCallback<ClickEvent>(Right);

        if (mapScrollView != null)
            mapScrollView.verticalScroller.valueChanged -= OnMapScrolled;

        root = null;
        mapScrollView = null;
    }
    void OnDestroy()
    {
        UnEquipItem();
        Container.Clear();

        if (root != null)
            root.Q<VisualElement>("close")?.UnregisterCallback<ClickEvent>(Close);

        if (navigationLeft != null)
            navigationLeft.UnregisterCallback<ClickEvent>(Left);

        if (navigationRight != null)
            navigationRight.UnregisterCallback<ClickEvent>(Right);
    }
    public bool AddItem(ItemObject item, int amount)
    {
        int remaining = amount;

        foreach (KeyValuePair<int, InventorySlot> inventorySlot in Container)
        {
            if (remaining <= 0) break;
            InventorySlot slot = inventorySlot.Value;

            if (slot.isTaken && slot.item == item)
            {
                int canFit = item.maxStackSize - slot.amount;
                if (canFit <= 0) continue;

                int toAdd = Mathf.Min(canFit, remaining);
                slot.AddAmount(toAdd);
                remaining -= toAdd;
            }
        }

        foreach (KeyValuePair<int, InventorySlot> inventorySlot in Container)
        {
            if (remaining <= 0) break;
            InventorySlot slot = inventorySlot.Value;

            if (!slot.isTaken)
            {
                int toAdd = Mathf.Min(item.maxStackSize, remaining);
                slot.AddItem(item, toAdd);
                slot.AddHover();
                remaining -= toAdd;
            }
        }
//navigation-right
        return remaining <= 0;
    }

    public bool AddFromItemBoxItem(ItemObject item, int amount)
    {
        int remaining = amount;

        foreach (KeyValuePair<int, InventorySlot> inventorySlot in Container)
        {
            if (remaining <= 0) break;
            InventorySlot slot = inventorySlot.Value;

            if (slot.isTaken && slot.item == item)
            {
                int canFit = item.maxStackSize - slot.amount;
                if (canFit <= 0) continue;

                int toAdd = Mathf.Min(canFit, remaining);
                slot.AddAmount(toAdd);
                remaining -= toAdd;
            }
        }

        foreach (KeyValuePair<int, InventorySlot> inventorySlot in Container)
        {
            if (remaining <= 0) break;
            InventorySlot slot = inventorySlot.Value;

            if (!slot.isTaken)
            {
                int toAdd = Mathf.Min(item.maxStackSize, remaining);
                slot.AddItem(item, toAdd);
                remaining -= toAdd;
            }
        }

        return remaining <= 0;
    }
    
    public void EquipItem(ItemObject item)
    {
        if (item.itemName == "Pistol")
            player.animator.SetBool("isPistolEquipped", true);

        equipedItem = item;

        itemPrefab = Instantiate(item.prefab, new Vector3(0, 0, 0), Quaternion.identity);
        itemPrefab.transform.localScale = new Vector3(15, 15, 15);
        equipWeapon.Equip(itemPrefab, equipedItem.name.ToSafeString());

        WeaponAmmoState.NotifyAmmoChanged();

    }
    public void UnEquipItem()
    {
        if (equipedItem == null) return;
        if (equipedItem.name == "Pistol")
            player.animator.SetBool("isPistolEquipped", false);
        if (itemPrefab != null)
            Destroy(itemPrefab);
        equipedItem = null;
        equipWeapon.UnEquip();
    }

    public int GetAmmoCount(AmmoType ammoType)
    {
        int total = 0;
        foreach (var kvp in Container)
        {
            InventorySlot slot = kvp.Value;
            if (slot.isTaken && slot.item != null
                && slot.item.itemType == Itemtype.Ammunition
                && slot.item.ammoType == ammoType)
            {
                total += slot.amount;
            }
        }
        return total;
    }


    public int ConsumeAmmo(AmmoType ammoType, int amount)
    {
        int remaining = amount;
        foreach (var kvp in Container)
        {
            if (remaining <= 0) break;
            InventorySlot slot = kvp.Value;
            if (slot.isTaken && slot.item != null
                && slot.item.itemType == Itemtype.Ammunition
                && slot.item.ammoType == ammoType)
            {
                int take = Mathf.Min(slot.amount, remaining);
                slot.SubtractItem(take);
                remaining -= take;
            }
        }
        return amount - remaining;
    }

    
    public bool ReloadWeapon()
    {
        if (equipedItem == null) return false;
        if (equipedItem.equipType != EquipType.Weapon)
        {
            UIDialogueBox.OpenDialogue(new string[] { "No weapon is currently equipped." });
            return false;
        } 

        AmmoType needed  = equipedItem.requiredAmmoType;
        int maxAmmo      = equipedItem.maxAmmoPerReload;
        int currentAmmo  = WeaponAmmoState.currentAmmo;
        int ammoToLoad   = maxAmmo - currentAmmo;

        if (ammoToLoad <= 0)
        {
            UIDialogueBox.OpenDialogue(new string[] { "Already fully loaded." });
            return false;
        }

        int available = GetAmmoCount(needed);
        if (available <= 0)
        {
            UIDialogueBox.OpenDialogue(new string[] { "No ammo available." });
            return false;
        }

        int actualLoad = Mathf.Min(ammoToLoad, available);
        ConsumeAmmo(needed, actualLoad);
        WeaponAmmoState.currentAmmo += actualLoad;

        PlayerSounds.instance.PlayReloadSound(S_VolumeStats.buttonClick);
        return true;
    }
    public void Update()
    {
        equipWeapon.Update();
    }

    private void OnApplicationQuit()
    {
        Container.Clear();
    }
    public void LoadData(GameData data)
    {
        int index = 0;
        Dictionary<int, string[]> inventoryItems = data.inventoryItems;
        //slot, name, string
        foreach(KeyValuePair<int, string[]> slot in inventoryItems)
        {
            string itemName = slot.Value[0];
            if(!string.IsNullOrEmpty(itemName))
            {
                int amount = int.Parse( slot.Value[1]);
                ItemObject item = ItemObjectFinder.instance.FindItemObjectByName(itemName);
                if(item!=null)
                    AddItem(item, amount);
                else
                    Debug.LogError("item does not exist");
            }
            index ++;
        }
        WeaponAmmoState.SetAmmo(data.loadedAmmo);
    }

    public void SaveData(ref GameData data)
    {
        int index = 0;
        foreach(KeyValuePair<int, InventorySlot> slot in Container)
        {
            string[] nameAndAmount = new string[2];
            if(slot.Value.item != null)
            {
                nameAndAmount[0] = slot.Value.item.name;
                nameAndAmount[1] = slot.Value.amount.ToString();
            }
                
            else
            {
                nameAndAmount[0] = "";
                nameAndAmount[1] = "";
            }
            if(data.inventoryItems.ContainsKey(index))
            {
                data.inventoryItems.Remove(index);
            }
            data.inventoryItems.Add(index, nameAndAmount);
            index ++;
        }
        data.loadedAmmo = WeaponAmmoState.currentAmmo;
    }
    // combination
    public void StartCombine(InventorySlot firstSlot)
    {
        isCombining = true;
        pendingCombineSlot = firstSlot;
        itemDescriptionText.text = "Select an item to combine with.";
    }

    public void ProcessCombine(InventorySlot secondSlot)
    {
        //if clic the exact same item, cancel
        if (pendingCombineSlot == secondSlot)
        {
            CancelCombine();
            return;
        }

        ItemObject itemA = pendingCombineSlot.item;
        ItemObject itemB = secondSlot.item;

        bool recipeFound = false;
        CombineRecipe validRecipe = default;

        foreach (var recipe in combineRecipes)
        {
            if ((recipe.itemA == itemA && recipe.itemB == itemB) ||
                (recipe.itemA == itemB && recipe.itemB == itemA))
            {
                validRecipe = recipe;
                recipeFound = true;
                break;
            }
        }
        if (recipeFound)
        {
            pendingCombineSlot.SubtractItem(1);
            secondSlot.SubtractItem(1);

            int amountToAdd = validRecipe.resultAmount > 0 ? validRecipe.resultAmount : 1;
            AddItem(validRecipe.resultItem, amountToAdd);
            
            itemDescriptionText.text = "Combination successful.";
            //PlayerSounds.instance.Play(S_VolumeStats.successSound);
        }
        else
        {
            
            itemDescriptionText.text = "Items can't be combined";
            PlayerSounds.instance.PlayClickButton(S_VolumeStats.buttonClick);
        }

        CancelCombine();
    }

    public void CancelCombine()
    {
        isCombining = false;
        pendingCombineSlot = null;
    }

    public void HideAllPopups(InventorySlot except = null)
    {
        foreach (InventorySlot slot in Container.Values)
        {
            if (slot != except)
                slot.HidePopup();
        }
    }
}
[System.Serializable]
public class InventorySlot : VisualElement
{
    public ItemObject item;
    public int amount;
    public bool isTaken = false;
    VisualElement hoverPopUp;
    List<Button> buttonPopUpList = new List<Button>();
    InventoryObject inventoryObject;
    public Label amountLabel;
    private bool isWeaponSlot = false;
   // PlayerSounds playerSounds;

    public InventorySlot(InventoryObject inventoryObject)
    {
        this.inventoryObject = inventoryObject;
        amountLabel = new Label();
        amountLabel.AddToClassList("amountLabel");
        //this.playerSounds = playerSounds;
        RegisterCallback<MouseEnterEvent>(OnMouseOver);
        RegisterCallback<MouseLeaveEvent>(OnMouseLeave);
        
        RegisterCallback<ClickEvent>(OnClick);

        AddToClassList("itemSlot");
        hoverPopUp = new VisualElement();
        Add(amountLabel);
    }
    public void RemoveListeners()
    {
        WeaponAmmoState.onAmmoChanged -= RefreshAmmoLabel;
        if (hoverPopUp != null)
        {
            foreach(VisualElement v in hoverPopUp.Children())
            {
                if (v is Button b)
                {
                    b.clicked -= OnEquip;
                    b.clicked -= OnUnEquip;
                    b.clicked -= OnUse;
                    b.clicked -= OnReload;
                    b.clicked -= OnCombine;
                    b.clicked -= OnCancel;
                }
            }
        }
        buttonPopUpList.Clear();
    }
    public void AddItem(ItemObject item, int amount)
    {
        this.item   = item;
        this.amount = amount;
        style.backgroundImage = item.image;
        isTaken = true;

        if (this.amount > 1)
            amountLabel.text = "x" + this.amount;
        else
            amountLabel.text = "";

        if (item.equipType == EquipType.Weapon)
        {
            isWeaponSlot = true;
            WeaponAmmoState.onAmmoChanged -= RefreshAmmoLabel;
            WeaponAmmoState.onAmmoChanged += RefreshAmmoLabel;
            RefreshAmmoLabel();
        }
    }
    void RefreshAmmoLabel()
{
    if (!isWeaponSlot || item == null) return;

    // Only show ammo label if this weapon is the currently equipped one
    if (inventoryObject.equipedItem == item)
    {
        amountLabel.text = WeaponAmmoState.currentAmmo + "/" + WeaponAmmoState.maxAmmo;
    }
    else
    {
        amountLabel.text = "";
    }
}
    public void ClearSlot()
    {
        this.item = null;
        this.amount = 0;
        this.isTaken = false;
        isWeaponSlot = false;
        WeaponAmmoState.onAmmoChanged -= RefreshAmmoLabel;
        
        style.backgroundImage = null;
        amountLabel.text = "";
        
        hoverPopUp.style.display = DisplayStyle.None;
        RemoveHover(); 
    }
    public void SubtractItem(int amount)
    {
        this.amount -= amount;
        
        if (this.amount > 1)
            amountLabel.text = "x" + this.amount;
        else
            amountLabel.text = "";

        if(this.amount <=0)
        {
            ClearSlot();
            style.backgroundImage = null;
            amountLabel.text = "";
            hoverPopUp.style.display = DisplayStyle.None;
            if (hoverPopUp.parent == this)
                RemoveHover(); 
        }
        
        
    }
    public void AddAmount(int amount)
    {
        this.amount += amount;
        if (this.amount > 1)
            amountLabel.text = "x" + this.amount;
        else
            amountLabel.text = "";
    }
    public void AddItemBoxHover()
    {
        hoverPopUp.Clear();
        buttonPopUpList.Clear();
        buttonPopUpList.Add(createbuttonPopup("popUpButton", ButtonType.Cancel));
        foreach (Button b in buttonPopUpList)
        {
            hoverPopUp.Add(b);
        }

        hoverPopUp.AddToClassList("popUp");
        hoverPopUp.style.display = DisplayStyle.None;
        Add(hoverPopUp);
    }
    public void AddHover()
    {
        hoverPopUp.Clear();
        buttonPopUpList.Clear();
        if (item.equipType == EquipType.Weapon)
        {
            buttonPopUpList.Add(createbuttonPopup("popUpButton", ButtonType.Equip));
            if (item.itemType == Itemtype.Ranged)
                buttonPopUpList.Add(createbuttonPopup("popUpButton", ButtonType.Reload));
            buttonPopUpList.Add(createbuttonPopup("popUpButton", ButtonType.Cancel));
            foreach (Button b in buttonPopUpList)
            {
                hoverPopUp.Add(b);
            }
        }
        else if (item.equipType == EquipType.Key)
        {
            buttonPopUpList.Add(createbuttonPopup("popUpButton", ButtonType.Use));
            buttonPopUpList.Add(createbuttonPopup("popUpButton", ButtonType.Combine));
            buttonPopUpList.Add(createbuttonPopup("popUpButton", ButtonType.Cancel));
            foreach (Button b in buttonPopUpList)
            {
                hoverPopUp.Add(b);
            }
        }
        else if (item.equipType == EquipType.None)
        {
            /*hoverPopUp.Add(createbuttonPopup("popUpButton", ButtonType.Examine));
            hoverPopUp.Add(createbuttonPopup("popUpButton", ButtonType.Combine));*/
        }

        hoverPopUp.AddToClassList("popUp");
        hoverPopUp.style.display = DisplayStyle.None;
        Add(hoverPopUp);
    }
    //Evnt stuff
    void OnUse()
    {
        if(item.itemType == Itemtype.Healing)
            PlayerSounds.instance.PlayHeal(S_VolumeStats.buttonClick);
        else
            PlayerSounds.instance.PlayClickButton(S_VolumeStats.buttonClick);
            
        hoverPopUp.style.display = DisplayStyle.None;

        if(item.itemType == Itemtype.Healing)
        {
            HealingItem heal = (HealingItem)item;
            inventoryObject.player.Heal(heal.healingValue);
            InventoryObject.itemDescriptionText.text = "I was healed.";
            if (item.shouldDestroyAfterUse)
                ClearSlot();
            Debug.Log("PLAYER HEALED");
            return;
        }

        HandleItemInteraction itemPickup = inventoryObject.player.itemPickUp;
        
        if(itemPickup.isDoorInRange)
        {
            Door door = itemPickup.doorInRange.GetComponent<Door>();
            if(door.CheckKeyItem(item.keyName))
            {
                if (itemPickup.doorInRange is Fuse fuse) 
                {
                    fuse.DoOtherFunction();
                }
                if (itemPickup.doorInRange is AnimalPuzzle animalPuzzle) 
                {
                    if(animalPuzzle.PutKeyInSlot(item))
                    {
                        ClearSlot();
                    }else
                    {
                        PlayerSounds.instance.Play(animalPuzzle.GetOpenLockedDoorSound(), animalPuzzle.GetVolume());
                        UIDialogueBox.OpenDialogue(new string[]{animalPuzzle.keyInSlot.name+" is already here"});
                        return;
                    }
                }
                if(door.GetUnlockDoorSound()!=null)
                    PlayerSounds.instance.Play(door.GetUnlockDoorSound(), door.GetVolume());
                if(door.hiddenItem!=null)
                {
                    door.hiddenItem.SetActive(true);
                    door.transform.gameObject.SetActive(false);
                }
                if(door.shouldChangeTileMapAfterUnlock)
                {
                    door.ChangeTile();
                }
                if(item!=null && item.shouldDestroyAfterUse)
                {
                    ClearSlot();
                }
                inventoryObject.player.input.InventoryClose(new InputAction.CallbackContext());
                UIImage.OpenPhoto(door.endingPhotoAfterUnlocking);
                UIDialogueBox.OpenDialogue(door.endingDialogueAfterUnlocking);
            }
            else
            {
                if(door.GetOpenLockedDoorSound()!=null)
                    PlayerSounds.instance.Play(door.GetOpenLockedDoorSound(), door.GetVolume());
                inventoryObject.player.input.InventoryClose(new InputAction.CallbackContext());
                
                Debug.Log("4");
                UIDialogueBox.OpenDialogue(new string[] {"That won't work."});
            }
        }else if(itemPickup.isPuzzleInRange)
        {
            if(itemPickup.puzzleInRange is MD_Computer computer)
            {
                HandleComputerPuzzle(computer);
            }else if(itemPickup.puzzleInRange is Radio radio)
            {
                HandleRadioPuzzle(radio);
            }
        }
        else if (itemPickup.isBodyInRange)
        {
            if (item.itemName == "Igniter")
            {
                EnemyReviveHandler body = itemPickup.bodyInRange;
                if (body != null && body.IsDead && !body.IsBurned)
                {
                    body.BurnBody();
                    PlayerSounds.instance.PlayClickButton(S_VolumeStats.buttonClick);
                    UIDialogueBox.OpenDialogue(new string[] { "The body has been burned." });
                    if (item.shouldDestroyAfterUse)
                        ClearSlot();
                    inventoryObject.player.input.InventoryClose(new InputAction.CallbackContext());
                }
                else if (body != null && body.IsBurned)
                {
                    UIDialogueBox.OpenDialogue(new string[] { "The body is already burned." });
                    inventoryObject.player.input.InventoryClose(new InputAction.CallbackContext());
                }
            }
            else
            {
                inventoryObject.player.input.InventoryClose(new InputAction.CallbackContext());
                Debug.Log("1");
                UIDialogueBox.OpenDialogue(new string[] { "That won't work." });
            }
        }
        else
        {
            inventoryObject.player.input.InventoryClose(new InputAction.CallbackContext());
            UIDialogueBox.OpenDialogue(new string[] {"That won't work."});
            Debug.Log("2");
        }
    }
   void OnClick(ClickEvent evt)
    {
        // If we are currently combining and this slot has an item, process it
        if (inventoryObject.isCombining && isTaken)
        {
            Debug.Log("COMBINE");
            inventoryObject.ProcessCombine(this);
            hoverPopUp.style.display = DisplayStyle.None;
            evt.StopPropagation(); // Stop the popup from interfering
            return;
        }

        inventoryObject.HideAllPopups(this);

        if (!isTaken)
        {
            InventoryObject.itemDescriptionText.text = "";
            InventoryObject.itemPicture.style.backgroundImage = null;
            evt.StopPropagation();
            return;
        }

        bool shouldOpenPopup = hoverPopUp.style.display.value != DisplayStyle.Flex;
        hoverPopUp.style.display = shouldOpenPopup ? DisplayStyle.Flex : DisplayStyle.None;

        if (shouldOpenPopup)
        {
            InventoryObject.itemDescriptionText.text = item.description;
            InventoryObject.itemPicture.style.backgroundImage = item.image;
        }
        else
        {
            InventoryObject.itemDescriptionText.text = "";
            InventoryObject.itemPicture.style.backgroundImage = null;
        }

        evt.StopPropagation();
    }

    void OnCombine()
    {
        PlayerSounds.instance.PlayClickButton(S_VolumeStats.buttonClick);
        hoverPopUp.style.display = DisplayStyle.None;
        
        inventoryObject.StartCombine(this);
    }
    public void OnCancel()
    {
        PlayerSounds.instance.PlayClickButton(S_VolumeStats.buttonClick);
        HidePopup();
        InventoryObject.itemDescriptionText.text = "";
        InventoryObject.itemPicture.style.backgroundImage = null;
    }
    void OnEquip()
    {
        PlayerSounds.instance.PlayClickButton(S_VolumeStats.buttonClick);
        inventoryObject.EquipItem(item);
        hoverPopUp.style.display = DisplayStyle.None;
        foreach(VisualElement v in hoverPopUp.Children())
        {
            if (v is Button b)
            {
                if (b.text.ToString() == "Equip")
                {
                    b.text = "Unequip";
                    b.clicked -= OnEquip;
                    b.clicked += OnUnEquip;
                }
            }
        }
    }
    public void OnUnEquip()
    {
        PlayerSounds.instance.PlayClickButton(S_VolumeStats.buttonClick);
        inventoryObject.UnEquipItem();
        hoverPopUp.style.display = DisplayStyle.None;
        foreach(VisualElement v in hoverPopUp.Children())
        {
            if (v is Button b)
            {
                if (b.text.ToString() == "Unequip")
                {
                    b.text = "Equip";
                    b.clicked -= OnUnEquip;
                    b.clicked += OnEquip;
                }
            }
        }
    }

    void OnMouseOver(MouseEnterEvent evt)
    {
        if(item!=null)
        {
            InventoryObject.itemDescriptionText.text = item.description;
            InventoryObject.itemPicture.style.backgroundImage = item.image;
        }
        
    }
    void OnMouseLeave(MouseLeaveEvent evt)
    {
        if (hoverPopUp.style.display.value != DisplayStyle.Flex)
        {
            InventoryObject.itemDescriptionText.text = "";
            InventoryObject.itemPicture.style.backgroundImage = null;
        }
    }
    public void HidePopup()
    {
        hoverPopUp.style.display = DisplayStyle.None;
    }
    public void RemoveHover()
    {
        buttonPopUpList.Clear();
        hoverPopUp.Clear();
        if (hoverPopUp.parent == this)
            Remove(hoverPopUp);
    }
    public void AddExistingItem()
    {

    }
    Button createbuttonPopup(string className, ButtonType buttonType)
    {
        Button button = new Button();
        button.AddToClassList(className);
        button.text = buttonType.ToString();
        button.RegisterCallback<ClickEvent>(evt => evt.StopPropagation());
        if (buttonType == ButtonType.Equip)
        {
            button.clicked += OnEquip;
        }
        if (buttonType == ButtonType.Use)
        {
            button.clicked += OnUse;
        }
        if (buttonType == ButtonType.Reload)
                button.clicked += OnReload;
        if (buttonType == ButtonType.Combine)
            button.clicked += OnCombine;
        if (buttonType == ButtonType.Cancel)
            button.clicked += OnCancel;

        button.RegisterCallback<MouseEnterEvent>(e=>{PlayerSounds.instance.PlayHoverButton(S_VolumeStats.buttonHover);});
        return button;
    }
    void OnReload()
    {
        //PlayerSounds.instance.PlayReloadSound(S_VolumeStats.buttonClick);
        hoverPopUp.style.display = DisplayStyle.None;
        inventoryObject.ReloadWeapon();
        RefreshAmmoLabel();
    }
    private void HandleComputerPuzzle(MD_Computer computer)
    {
        if(computer.CheckKeyItem(item.keyName))
        {
            List<string> titles = new List<string>();
            List<string> contents = new List<string>();
            if(item.keyName.Equals(MedicalWardKeyItemID.CDGreen))
            {
                titles.Add("Operation Week 3 Log");
                titles.Add("I'm sure of it");
                titles.Add("Security Room Lock");

                contents.Add("So far, I still feel relatively normal, aside from being a little forgetful at times. "+ 
                "Been feeling great physically lately, as if my bodily funtions have been enhanced.");
                contents.Add("I'm sure this is the right thing. I've been loyal to the Empire since they saved me. They want nothing but peace for the entire system"+
                " I'm sure others question their methods, but I completely agree with them. After all, Verel has to take drastic measures, if what they aim to achieve spans multiple planets right?\n\n"+
                "The others don't know, but that chip is there for more than communication. But still, they need to COMPLETELY obey the Empire to achieve their goal. After all, we soldiers are replaceable");
                
                contents.Add("This week : 0926");
                
            }else if(item.keyName.Equals(MedicalWardKeyItemID.CDRed))
            {
                
                titles.Add("Star");
                titles.Add("Moon");
                titles.Add("Sun");
                titles.Add("I wonder");

                contents.Add("1110");
                contents.Add("2315");
                contents.Add("0451");
                contents.Add("All of these, I wonder where they got it from? I'm all for their revolution but I hope they're not sinking their teeth into something more dangerous");
            }
            
            computer.AddContent(titles, contents);
            
            if(item!=null && item.shouldDestroyAfterUse)
                ClearSlot();
        }else
        {
            
            inventoryObject.player.input.InventoryClose(new InputAction.CallbackContext());
            UIDialogueBox.OpenDialogue(new string[] {"That won't work."});
            
                Debug.Log("3");
        }
    }
    private void HandleRadioPuzzle(Radio radio)
    {
        if(radio.CheckKeyItem(item.keyName))
        {
            if(item!=null && item.shouldDestroyAfterUse)
            {
                ClearSlot();
            }
            inventoryObject.player.input.InventoryClose(new InputAction.CallbackContext());
        }else
        {
            
            inventoryObject.player.input.InventoryClose(new InputAction.CallbackContext());
        }
    }
}


public enum ButtonType
{
    Equip,
    Reload,
    Examine,
    Use,
    Cancel,
    Combine

}

class Key
{
    List<int> usedKeys = new List<int>();
    public int GetKey()
    {
        int currentKey = 0;
        for (int i = 0; i < 999999; i++)
        {
            bool isUniqueKey = true;
            currentKey = i;
            foreach (int key in usedKeys)
            {
                if (currentKey == key)
                {
                    isUniqueKey = false;
                    break;
                }
            }
            if (isUniqueKey)
            {
                usedKeys.Add(currentKey);
                return currentKey;
            }
        }

        return 999999;
    }
    public void ReturnKey(int key)
    {
        usedKeys.Remove(key);
        usedKeys.Sort();
    }
}
[System.Serializable]
public struct CombineRecipe
{
    public ItemObject itemA;
    public ItemObject itemB;
    public ItemObject resultItem;
    public int resultAmount;
}
