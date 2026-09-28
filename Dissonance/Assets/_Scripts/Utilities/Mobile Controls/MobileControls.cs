using UnityEngine;
using UnityEngine.UIElements;
using UnityEngine.InputSystem;
using System.Collections;

public class MobileControls : MonoBehaviour
{
    [SerializeField] private UIDocument uiDocument;
    [SerializeField] private float joystickRadius = 100f;
    [SerializeField] private PlayerInputs playerInputs;
    [SerializeField] private Player player;
    [SerializeField] private float aimDeadZone = 0.2f;

    private VisualElement root;
    private VisualElement overlayHost;


    private VisualElement leftZone;
    private VisualElement leftBase;
    private VisualElement leftKnob;
    private int leftFingerId = -1;
    private Vector2 leftOrigin;
    private Vector2 leftDelta;


    private VisualElement rightZone;
    private VisualElement rightBase;
    private VisualElement rightKnob;
    private int rightFingerId = -1;
    private Vector2 rightOrigin;
    private Vector2 rightDelta;
    
    public VisualElement interactButton;

    VisualElement overlay;


    private VisualElement fireButton;
    private bool pendingCancelFire = false;



    void Start()
    {
        root = uiDocument.rootVisualElement;
        BuildUI();
        RegisterTouchEvents();
    }

    public void RemoveControls()
    {
        ForceResetJoysticks();
        overlay?.RemoveFromHierarchy();
        overlayHost?.RemoveFromHierarchy();
    }

    private void ForceResetJoysticks()
    {
        if (leftFingerId != -1 && leftZone != null)
        {
            leftZone.ReleasePointer(leftFingerId);
            leftFingerId = -1;
        }
        leftDelta = Vector2.zero;
        if (leftBase != null) leftBase.style.display = DisplayStyle.None;
        if (leftKnob != null) ResetKnob(leftKnob);
        if (playerInputs != null) playerInputs.Direction = Vector2.zero;

        if (rightFingerId != -1 && rightZone != null)
        {
            rightZone.ReleasePointer(rightFingerId);
            rightFingerId = -1;
        }
        rightDelta = Vector2.zero;
        if (rightBase != null) rightBase.style.display = DisplayStyle.None;
        if (fireButton != null) fireButton.style.display = DisplayStyle.None;
        if (rightKnob != null) ResetKnob(rightKnob);

        if (player != null && player.input != null)
        {
            player.input.OnAimReleased(new InputAction.CallbackContext());
            player.lockInState.isJoyStick = false;
            player.lockInState.joystickAimDir = Vector3.zero;
        }
    }
    public void AddControls()
    {
        if (overlay == null || overlay.parent != null)
            return;

        root.Add(overlay);
        root.Add(overlayHost);
        overlayHost?.BringToFront();

    }
    void BuildUI()
    {
        overlay = root.Q<VisualElement>("mobile-overlay");
        if (overlay == null)
        {
            overlay = new VisualElement
            {
                name = "mobile-overlay"
            };
            root.Add(overlay);
        }

        overlayHost = overlay.parent ?? root;
        overlay.style.position       = Position.Absolute;
        overlay.style.left           = 0; overlay.style.right  = 0;
        overlay.style.top            = 0; overlay.style.bottom = 0;
        overlay.pickingMode          = PickingMode.Ignore;
        overlay.name                 = "mobile-overlay";
        if (overlay.parent != root)
            root.Add(overlay);

        overlayHost?.BringToFront();

        leftZone = new VisualElement();
        leftZone.style.position = Position.Absolute;
        leftZone.style.left     = 0;
        leftZone.style.top      = 0;
        leftZone.style.bottom   = 0;
        leftZone.style.width    = Length.Percent(50);
        leftZone.pickingMode    = PickingMode.Position;
        overlay.Add(leftZone);

        leftBase = MakeJoystickBase();
        leftKnob = MakeJoystickKnob();
        leftBase.Add(leftKnob);
        leftBase.style.display = DisplayStyle.None; // hidden until touched
        overlay.Add(leftBase);

        rightZone = new VisualElement();
        rightZone.style.position = Position.Absolute;
        rightZone.style.right    = 0;
        rightZone.style.top      = 0;
        rightZone.style.bottom   = 0;
        rightZone.style.width    = Length.Percent(50);
        rightZone.pickingMode    = PickingMode.Position;
        overlay.Add(rightZone);

        rightBase = MakeJoystickBase();
        rightKnob = MakeJoystickKnob();
        rightBase.Add(rightKnob);
        rightBase.style.display = DisplayStyle.None;
        overlay.Add(rightBase);

        fireButton = new VisualElement();
        fireButton.style.position = Position.Absolute;
        fireButton.style.right = 170;
        fireButton.style.top = 100;
        fireButton.style.width = 110;
        fireButton.style.height  = 110;
        fireButton.style.borderTopLeftRadius = 100;
        fireButton.style.borderTopRightRadius = 100;
        fireButton.style.borderBottomLeftRadius = 100;
        fireButton.style.borderBottomRightRadius = 100;
        fireButton.style.backgroundColor = new StyleColor(new Color(1f, 1f, 1f, 0.15f));
        fireButton.pickingMode = PickingMode.Position;

        var fireLabel = new Label("X");
        fireLabel.style.position = Position.Absolute;
        fireLabel.style.left = 0; fireLabel.style.right = 0;
        fireLabel.style.top = 0; fireLabel.style.bottom = 0;
        fireLabel.style.unityTextAlign = TextAnchor.MiddleCenter;
        fireLabel.style.color = 
            new StyleColor(new Color(1f, 1f, 1f, 0.4f));
        fireLabel.style.fontSize = 14;
        fireButton.Add(fireLabel);
        fireButton.style.display = DisplayStyle.None;
        overlay.Add(fireButton);

        fireButton.RegisterCallback<PointerEnterEvent>(CancelFire);
        fireButton.RegisterCallback<PointerLeaveEvent>(ResumeFire);
    }

    VisualElement MakeJoystickBase()
    {
        var el = new VisualElement();
        el.style.position                = Position.Absolute;
        el.style.width                   = joystickRadius * 2;
        el.style.height                  = joystickRadius * 2;
        el.style.borderTopLeftRadius     = joystickRadius;
        el.style.borderTopRightRadius    = joystickRadius;
        el.style.borderBottomLeftRadius  = joystickRadius;
        el.style.borderBottomRightRadius = joystickRadius;
        el.style.backgroundColor         =
            new StyleColor(new Color(1f, 1f, 1f, 0.15f));
        el.style.borderLeftWidth = el.style.borderRightWidth = el.style.borderTopWidth  = el.style.borderBottomWidth = 2;
        el.style.borderLeftColor         = el.style.borderRightColor =
        el.style.borderTopColor          = el.style.borderBottomColor =
            new StyleColor(new Color(1f, 1f, 1f, 0.4f));
        el.pickingMode = PickingMode.Ignore;
        return el;
    }

    VisualElement MakeJoystickKnob()
    {
        float knobSize = joystickRadius * 0.6f;
        var el = new VisualElement();
        el.style.position                = Position.Absolute;
        el.style.width                   = knobSize;
        el.style.height                  = knobSize;
        el.style.left                    = joystickRadius - knobSize / 2;
        el.style.top                     = joystickRadius - knobSize / 2;
        el.style.borderTopLeftRadius     = knobSize / 2;
        el.style.borderTopRightRadius    = knobSize / 2;
        el.style.borderBottomLeftRadius  = knobSize / 2;
        el.style.borderBottomRightRadius = knobSize / 2;
        el.style.backgroundColor         =
            new StyleColor(new Color(1f, 1f, 1f, 0.5f));
        el.pickingMode = PickingMode.Ignore;
        return el;
    }



    void RegisterTouchEvents()
    {
        leftZone.RegisterCallback<PointerDownEvent>(OnLeftDown);
        leftZone.RegisterCallback<PointerMoveEvent>(OnLeftMove);
        leftZone.RegisterCallback<PointerUpEvent>(OnLeftUp);
        leftZone.RegisterCallback<PointerCancelEvent>(OnLeftCancel);

        rightZone.RegisterCallback<PointerDownEvent>(OnRightDown);
        rightZone.RegisterCallback<PointerMoveEvent>(OnRightMove);
        rightZone.RegisterCallback<PointerUpEvent>(OnRightUp);
        rightZone.RegisterCallback<PointerCancelEvent>(OnRightCancel);

        var pauseButton = overlayHost.Q<VisualElement>("pause");
        interactButton = overlayHost.Q<VisualElement>("interact");
        var mapButton = overlayHost.Q<VisualElement>("map");
        var inventoryButton = overlayHost.Q<VisualElement>("inventory");
        
        var notes = overlayHost.Q<VisualElement>("notes");

        interactButton.style.display = DisplayStyle.None;
        
        pauseButton?.RegisterCallback<ClickEvent>(e => { player.input.Pause(new InputAction.CallbackContext());});
        interactButton?.RegisterCallback<ClickEvent>(PickUp);
        mapButton?.RegisterCallback<ClickEvent>(e => {
             RightUp();
             player.input.Inventory(new InputAction.CallbackContext()); 
             player.input.OnNavigateRight(new InputAction.CallbackContext());
             });
        inventoryButton?.RegisterCallback<ClickEvent>(e => { 
            RightUp();
            player.input.Inventory(new InputAction.CallbackContext()); 
            });
        notes?.RegisterCallback<ClickEvent>(e => { 
            RightUp();
            player.input.OnNotes(new InputAction.CallbackContext()); 
            });
    }
    private void PickUp(ClickEvent e)
    {
        if (player.input.nearbyItemBox != null && player.input.nearbyItemBox.IsPlayerInRange())
        {
            player.input.nearbyItemBox.OpenItemBox();
            PlayerInputs.UI.Enable();
            PlayerInputs.Player.Disable();
            player.input.isUI = true;
            return;
        }
        player.itemPickUp.pickUpItem();
    }

    //cancel fire//
    private void CancelFire(PointerEnterEvent e)
    {
        fireButton.style.backgroundColor = new StyleColor(new Color(1f, 1f, 1f, 0.5f));
        pendingCancelFire = true;
    }
    private void ResumeFire(PointerLeaveEvent e)
    {
        fireButton.style.backgroundColor = new StyleColor(new Color(1f, 1f, 1f, 0.15f));
        pendingCancelFire = false;
    }

    //cancel fire//


    void OnLeftDown(PointerDownEvent evt)
    {
        if (leftFingerId != -1) return;
        leftFingerId = evt.pointerId;
        leftOrigin = evt.position;
        leftDelta = Vector2.zero;

        // Show base at touch position
        ShowJoystick(leftBase, leftOrigin);
        leftZone.CapturePointer(evt.pointerId);
    }

    void OnLeftMove(PointerMoveEvent evt)
    {
        if (evt.pointerId != leftFingerId) return;

        Vector2 delta = (Vector2)evt.position - leftOrigin;
        delta.y = -delta.y;

        float dist = delta.magnitude;
        if (dist > joystickRadius)
            delta = delta.normalized * joystickRadius;

        leftDelta = delta / joystickRadius;

        float cx = joystickRadius + delta.x - leftKnob.resolvedStyle.width  / 2;
        float cy = joystickRadius - delta.y - leftKnob.resolvedStyle.height / 2;
        leftKnob.style.left = cx;
        leftKnob.style.top  = cy;
        float adderY = 0;
        playerInputs.Direction = new Vector2(leftDelta.x, leftDelta.y+adderY);
    }

    void OnLeftUp(PointerUpEvent evt)
    {
        if (evt.pointerId != leftFingerId) return;
        ResetLeft();
    }

    void OnLeftCancel(PointerCancelEvent evt)
    {
        if (evt.pointerId != leftFingerId) return;
        ResetLeft();
    }

    void ResetLeft()
    {
        if (leftFingerId == -1) return;

        leftZone.ReleasePointer(leftFingerId);

        leftFingerId = -1;
        leftDelta = Vector2.zero;
        leftBase.style.display = DisplayStyle.None;
        ResetKnob(leftKnob);
        playerInputs.Direction = Vector2.zero;
    }



    void OnRightDown(PointerDownEvent evt)
    {
        if (rightFingerId != -1) return;
        
        fireButton.style.display = DisplayStyle.Flex;
        rightFingerId = evt.pointerId;
        rightOrigin = evt.position;
        rightDelta = Vector2.zero;

        ShowJoystick(rightBase, rightOrigin);
        rightZone.CapturePointer(evt.pointerId);
    }

    void OnRightMove(PointerMoveEvent evt)
    {
        if (evt.pointerId != rightFingerId) return;

        Vector2 delta = (Vector2)evt.position - rightOrigin;
        delta.y = -delta.y;

        float dist = delta.magnitude;
        if (dist > joystickRadius)
            delta = delta.normalized * joystickRadius;

        rightDelta = delta / joystickRadius;

        float cx = joystickRadius + delta.x - rightKnob.resolvedStyle.width  / 2;
        float cy = joystickRadius - delta.y - rightKnob.resolvedStyle.height / 2;
        rightKnob.style.left = cx;
        rightKnob.style.top  = cy;

        // Stop here if the drag distance hasn't crossed the deadzone
        if (rightDelta.magnitude < aimDeadZone) return;

        // If we crossed the deadzone and aren't aiming yet, lock in the aim
        if (!isActuallyAiming)
        {
            isActuallyAiming = true;
            
            if (player.inventory.equipedItem != null)
            {
                playerInputs.isAiming = true;
                player.lockInState.isJoyStick = true;

                if (player.attackStateMachines.currentState != player.lockInState
                &&  player.attackStateMachines.currentState != player.attackState
                &&  player.movementStateMachine.currentState != player.hitState)
                {
                    player.attackStateMachines.ChangeState(player.lockInState);
                }
            }
        }

        Vector3 aimDir = new Vector3(rightDelta.x, 0f, rightDelta.y).normalized;
        if (aimDir != Vector3.zero)
        {
            player.lockInState.joystickAimDir = aimDir;
        }
    }
    private bool isActuallyAiming = false;
    void TriggerAim()
    {
        if (player.inventory.equipedItem == null) return;
        playerInputs.isAiming  = true;
        player.lockInState.isJoyStick = true;
        isActuallyAiming = false;
    }

    void OnRightUp(PointerUpEvent evt)
    {
        if (evt.pointerId != rightFingerId) return;
        
        fireButton.style.display = DisplayStyle.None;
        ResetRight();
    }
    void RightUp()
    {
        fireButton.style.display = DisplayStyle.None;
        ResetRight();
    }

    void OnRightCancel(PointerCancelEvent evt)
    {
        if (evt.pointerId != rightFingerId) return;
        fireButton.style.display = DisplayStyle.None;
        ResetRight();
    }
    void ResetRight()
    {
        if (rightFingerId == -1) return;

        // Only fire if the deadzone was actually crossed
        if(!pendingCancelFire && isActuallyAiming)
        {
            player.input.OnFire(new InputAction.CallbackContext());
        }

        rightZone.ReleasePointer(rightFingerId);
        rightFingerId = -1;
        rightDelta = Vector2.zero;
        rightBase.style.display = DisplayStyle.None;
        ResetKnob(rightKnob);

        // Only release the aim state if we actually entered it
        if (isActuallyAiming) 
        {
            player.input.OnAimReleased(new InputAction.CallbackContext());
            player.lockInState.isJoyStick = false;
            player.lockInState.joystickAimDir = Vector3.zero;
        }
        
        isActuallyAiming = false;
    }
    void OnFirePressed(PointerDownEvent evt)
    {
        player.input.OnFire(new InputAction.CallbackContext());
       /* if (!isAiming) return;
        if (player.attackStateMachines.currentState == player.lockInState)
            player.attackStateMachines.ChangeState(player.attackState);*/
    }

    void OnFireReleased(PointerUpEvent evt) { }



    void ShowJoystick(VisualElement joystickBase, Vector2 screenPos)
    {
        joystickBase.style.display = DisplayStyle.Flex;

        float panelHeight = root.resolvedStyle.height;
        float x = screenPos.x - joystickRadius;
        float y = screenPos.y - joystickRadius;

        joystickBase.style.left = x;
        joystickBase.style.top  = y;
    }

    void ResetKnob(VisualElement knob)
    {
        float knobSize = joystickRadius * 0.6f;
        knob.style.left = joystickRadius - knobSize / 2;
        knob.style.top  = joystickRadius - knobSize / 2;
    }

    void Update()
    {
        if (leftFingerId != -1)
            playerInputs.Direction = new Vector2(leftDelta.x, leftDelta.y);
    }
}
