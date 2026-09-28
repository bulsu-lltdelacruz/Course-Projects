using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using UnityEngine;
using UnityEngine.Events;
using UnityEngine.InputSystem;
using UnityEngine.UIElements;
using static PlayerInputAction;
public class PlayerInputs : MonoBehaviour, IPlayerActions, IUIActions
{
    Player player;
    PlayerInputAction playerInputAction;
    public static PlayerActions Player;
    public static UIActions UI;
    public Vector2 Direction;
    [NonSerialized]public bool isRunning = true;
    [NonSerialized] public bool isAiming = false;
    [NonSerialized]public bool isUI = false;
    [NonSerialized] public ItemBoxInteractable nearbyItemBox;
    [NonSerialized] public static bool isInMap = false;
    private MobileControls mc;
    
    void OnEnable()
    {
        if (playerInputAction == null)
        {
            playerInputAction = new PlayerInputAction();
            Player = playerInputAction.Player;
            Player.SetCallbacks(this);
            UI = playerInputAction.UI;
            UI.SetCallbacks(this);
        }

    }
    void OnDisable()
    {
        Player.Walk.performed -= OnWalk;
        playerInputAction.Player.Walk.canceled -= OnWalkRelease;
        //playerInputAction.Player.CameraAngle.performed -= player.OnCameraAngle;
        playerInputAction.Player.Move.canceled -= OnMoveRelease;
        Player.Aim.performed -= OnAim;
        Player.Aim.canceled -= OnAimReleased;
        Player.Inventory.performed -= Inventory;
        UI.CloseInventory.performed -= InventoryClose;
        playerInputAction.Disable();
    }
    public void EnablePlayerActions()
    {
        playerInputAction.Enable();
    }
    void Start()
    {
        player = GetComponent<Player>();
        mc = FindAnyObjectByType<MobileControls>();

        Player.Walk.performed += OnWalk;
        Player.Walk.canceled += OnWalkRelease;
        //Player.CameraAngle.performed += player.OnCameraAngle;
        Player.Move.canceled += OnMoveRelease;
        Player.Aim.performed += OnAim;
        Player.Aim.canceled += OnAimReleased;
        Player.Inventory.performed += Inventory;
        UI.CloseInventory.performed += InventoryClose;
        
        
        UI.MapClose.performed += InventoryClose;
        
        UI.NoteClose.performed += NoteClose;
        
        Player.Pause.performed += Pause;
        UI.ClosePause.performed += ClosePause;
        //UI.CloseInventory.performed += InventoryClose;
        
        if (!isUI)
        {
            UI.Disable();
            Player.Enable();
            player.inventory.parent.AddToClassList("parent-close");
            isUI = false;
        }
        else if (isUI)
        {
            UI.Enable();
            Player.Disable();
            player.inventory.parent.RemoveFromClassList("parent-close");
            isUI = true;
        }
        StartCoroutine(HandleStartDelay(.1f));
    }
    IEnumerator HandleStartDelay(float duration)
    {
        yield return new WaitForSeconds(duration);
        UI.Disable();
    }
    public void Update()
    {
        if (!player.lockInState.isJoyStick)
        {
            
            if (!Player.Aim.IsPressed())
            {
                isAiming = false;
            }
                
        }
        if(Player.Walk.IsPressed())
        {
            OnWalk(new InputAction.CallbackContext());
        }
    }
    public static void DisableAllInputs()
    {
        UI.Disable();
        Player.Disable();
    }
    public static void EnableAllInputs()
    {
        UI.Enable();
        Player.Enable();
    }
    public void OnMove(InputAction.CallbackContext context)
    {
        Direction = context.ReadValue<Vector2>().normalized;
    }
    void OnMoveRelease(InputAction.CallbackContext context)
    {

    }
    
    public void OnWalk(InputAction.CallbackContext context)
    {
        isRunning = false;
        /*if (!isRunning && player.attackStateMachines.currentState != player.lockInState
        && player.attackStateMachines.currentState != player.attackState)
            isRunning = true;
        else
        {
            isRunning = false;
        }*/
    }
    public void OnWalkRelease(InputAction.CallbackContext context)
    {
        if (!isRunning && player.attackStateMachines.currentState != player.lockInState
        && player.attackStateMachines.currentState != player.attackState)
            isRunning = true;
    }
    public void OnAttack(InputAction.CallbackContext context)
    {
        
    }
    public void OnAim(InputAction.CallbackContext context)
    {
        if(player.attackStateMachines.currentState != player.attackState
        && player.attackStateMachines.currentState != player.lockInState
        && player.movementStateMachine.currentState != player.hitState
        && player.inventory.equipedItem != null)
        {
            isAiming = true;
            player.attackStateMachines.ChangeState(player.lockInState);
        }
        
    }
    public void OnAimReleased(InputAction.CallbackContext context)
    {
        if (player.attackStateMachines.currentState != player.attackState)
        {
            //isAiming = false;
            player.attackStateMachines.ChangeState(player.idleAttackState);
        }
    }
    public void OnFire(InputAction.CallbackContext context)
    {
        if (player.attackStateMachines.currentState == player.lockInState
        && player.attackStateMachines.currentState != player.attackState
        && player.movementStateMachine.currentState != player.hitState)
        {
            
            player.attackStateMachines.ChangeState(player.attackState);
        }
            
    }

    public void OnCameraAngle(InputAction.CallbackContext context)
    {
        
    }
    //==================INVENTORY==============//
    // In OnPickUp or your interact callback:
public void OnPickUp(InputAction.CallbackContext context)
{
    if (!context.performed) return;

    // Check item box first
    if (nearbyItemBox != null && nearbyItemBox.IsPlayerInRange())
    {
        nearbyItemBox.OpenItemBox();
        UI.Enable();
        Player.Disable();
        isUI = true;
        return;
    }
    player.itemPickUp.pickUpItem();
}
    public void OnInventory(InputAction.CallbackContext context)
    {

    }
    //Gawa ko
    public void Inventory(InputAction.CallbackContext context)
    {
        UI.Enable();
        Player.Disable();
        player.inventory.parent.RemoveFromClassList("parent-close");
        player.inventory.root.Q<Label>("page-label").text = "Inventory";
        if(mc!=null)
            mc.RemoveControls();

        //player.inventory.root.style.display = DisplayStyle.Flex;
        
        isUI = true;
        Time.timeScale = 0f;
        if(player.inventory.inventoryParent.ClassListContains("inventory-close"))
        {
            isInMap = false;
            player.inventory.inventoryParent.RemoveFromClassList("inventory-close");
            player.inventory.navigationLeft.style.opacity = .3f;
            player.inventory.navigationRight.style.opacity = 1f;
            player.inventory.pageLabel.text = "Inventory";
        }
        
        player.inventory.CacheScrollPosition();
    }
    public void InventoryClose(InputAction.CallbackContext context)
    {
        //Close item box if open
        //ItemBoxUI itemBoxUI = FindObjectOfType<ItemBoxUI>();
        ItemBoxUI itemBoxUI = null;
        if(player.isSaving)
        {
            CloseSaveMenu();
            return;
        }
        if(nearbyItemBox!=null)
            itemBoxUI = nearbyItemBox.GetItemBoxUI();
        if(mc!=null)
            mc.AddControls();
        if (itemBoxUI != null && itemBoxUI.IsOpen)
        {
            itemBoxUI.Close();
            UI.Disable();
            Player.Enable();
            isUI = false;
            return;
        }
        if (!NotesManager.isFocused && NotesManager.isOpen)
        {
            if(context.control.name != "m")
                FindAnyObjectByType<NotesManager>().Close();
           // UI.Disable();
           // Player.Enable();    
            ///isUI = false;
           // return;
        }
        UI.Disable();
        Player.Enable();
        player.inventory.parent.AddToClassList("parent-close");
        player.inventory.inventoryParent.RemoveFromClassList("inventory-close");
        //player.inventory.root.style.display = DisplayStyle.None;
        isUI = false;
        player.inventory.navigationLeft.style.opacity = .3f;
        player.inventory.navigationRight.style.opacity = 1f;
        player.inventory.pageLabel.text = "Inventory";
        player.inventory.CancelAllCombines();
        Time.timeScale = 1f;
        //navigate back to inventory
        if(player.inventory.inventoryParent.ClassListContains("inventory-close"))
        {
            isInMap = false;
            player.inventory.inventoryParent.RemoveFromClassList("inventory-close");
            player.inventory.navigationLeft.style.opacity = .3f;
            player.inventory.navigationRight.style.opacity = 1f;
            player.inventory.pageLabel.text = "Inventory";
        }

    }
    public void OnMap(InputAction.CallbackContext context)
    {
        if(isUI)
            return;
        UI.Enable();
        Player.Disable();
        player.inventory.parent.RemoveFromClassList("parent-close");
        
        if(mc!=null)
            mc.RemoveControls();
        //player.inventory.root.style.display = DisplayStyle.Flex;
        isUI = true;
        Time.timeScale = 0f;
        player.inventory.CacheScrollPosition();

        if(!player.inventory.inventoryParent.ClassListContains("inventory-close"))
        {
            isInMap = true;
            player.inventory.inventoryParent.AddToClassList("inventory-close");
            player.inventory.navigationLeft.style.opacity = 1f;
            player.inventory.navigationRight.style.opacity = .3f;
            player.inventory.pageLabel.text = "Map";
        }
    }

    public void OnNotes(InputAction.CallbackContext context)
    {
        if(isUI)
            return;
        FindAnyObjectByType<NotesManager>().Open();
        isUI= true;
    }
    public void MapClose(InputAction.CallbackContext contex)
    {
        
    }
    public void NoteClose(InputAction.CallbackContext contex)
    {
        if (!NotesManager.isFocused && NotesManager.isOpen)
        {
            FindAnyObjectByType<NotesManager>().Close();
           // UI.Disable();
           // Player.Enable();    
            ///isUI = false;
           // return;
        }
    }
    public void OnNavigateLeft(InputAction.CallbackContext context)
    {
        //go to inventory
        if(player.inventory.inventoryParent.ClassListContains("inventory-close"))
        {
            isInMap = false;
            player.inventory.inventoryParent.RemoveFromClassList("inventory-close");
            player.inventory.navigationLeft.style.opacity = .3f;
            player.inventory.navigationRight.style.opacity = 1f;
            player.inventory.pageLabel.text = "Inventory";
        }
            
    }

    public void OnNavigateRight(InputAction.CallbackContext context)
    {
        //go to map
        if(!player.inventory.inventoryParent.ClassListContains("inventory-close"))
        {
            isInMap = true;
            player.inventory.inventoryParent.AddToClassList("inventory-close");
            player.inventory.navigationLeft.style.opacity = 1f;
            player.inventory.navigationRight.style.opacity = .3f;
            player.inventory.pageLabel.text = "Map";
            /*if(MapStateManager.instance.currentFloor.Equals(FloorLocation._2ndFloor))
            {
                player.inventory.GoToNextFloor(FloorLocation._2ndFloor);
            }else
            {
                
                player.inventory.GoToNextFloor(FloorLocation._1stFloor);
            }*/
        }
            
    }
    public void Pause(InputAction.CallbackContext contex)
    {
        if(player.isSaving)
        {
            
        }else
        {
            player.pauseManager.OnPause();
            UI.Enable();
            Player.Disable();
            isUI = true;
            Time.timeScale = 0f;
        }
        
    }
    public void ClosePause(InputAction.CallbackContext contex)
    {
        if(player.isSaving)
        {
            player.saveScreen.parent.style.display = DisplayStyle.None;
            player.isSaving = false;
            UI.Disable();
            Player.Enable();
            isUI = false;
            Time.timeScale = 1f;
        }else
        {
            player.pauseManager.OnReturn();
            UI.Disable();
            Player.Enable();
            isUI = false;
            Time.timeScale = 1f;
        }
        
    }
    public void CloseSaveMenu()
    {
        if(mc!=null)
            mc.AddControls();
            
        PlayerSounds.instance.PlayClickButton(S_VolumeStats.buttonClick);
        player.saveScreen.parent.style.display = DisplayStyle.None;
        player.isSaving = false;
        UI.Disable();
        Player.Enable();
        isUI = false;
        Time.timeScale = 1f;
        
    }
    
    public void OnPause(InputAction.CallbackContext context)
    {
       
    }
    
    public void OnCrouch(InputAction.CallbackContext context)
    {

    }

    public void OnInteract(InputAction.CallbackContext context)
    {

    }

    public void OnJump(InputAction.CallbackContext context)
    {

    }

    public void OnLook(InputAction.CallbackContext context)
    {

    }
    public void OnNext(InputAction.CallbackContext context)
    {

    }

    public void OnPrevious(InputAction.CallbackContext context)
    {

    }

    public void OnNavigate(InputAction.CallbackContext context)
    {
        
    }

    //==============UI=====================//
    public void OnCloseInventory(InputAction.CallbackContext context)
    {
        
    }   
    public void OnSubmit(InputAction.CallbackContext context)
    {

    }

    public void OnCancel(InputAction.CallbackContext context)
    {
        
    }

    public void OnPoint(InputAction.CallbackContext context)
    {
        
    }

    public void OnClick(InputAction.CallbackContext context)
    {
        
    }

    public void OnScrollWheel(InputAction.CallbackContext context)
    {
        
    }

    public void OnMiddleClick(InputAction.CallbackContext context)
    {
        
    }

    public void OnRightClick(InputAction.CallbackContext context)
    {
        
    }

    public void OnTrackedDevicePosition(InputAction.CallbackContext context)
    {
        
    }

    public void OnTrackedDeviceOrientation(InputAction.CallbackContext context)
    {
        
    }

    public void OnClosePause(InputAction.CallbackContext context)
    {
        
    }

    public void OnNavigateOtherFloor(InputAction.CallbackContext context)
    {
        if(!isInMap)
            return;
        player.inventory.GoToNextFloor();
    }

    public void OnMapClose(InputAction.CallbackContext context)
    {
        
    }

    public void OnNoteClose(InputAction.CallbackContext context)
    {
        
    }
}
