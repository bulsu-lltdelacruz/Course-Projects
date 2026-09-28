using System;
using NaughtyAttributes;
using Unity.Cinemachine;
using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.Animations.Rigging;
using UnityEngine.InputSystem;
using UnityEngine.UIElements;
using UnityEngine.XR;

public class Player : MonoBehaviour, IDamageable
{
    /*
        FIX SHOOTING, YUNG UPDATEATTACKTARGET, KAHIT DI NAKA AIM EH TUMATAMA haahahhah
    */
    public static bool isBeingChased;
    [SerializeField] CinemachineFollow mainCamera;
    [SerializeField]GameObject bloodFilter;
    public static CinemachineFollow followCam;
    [SerializeField]private CinemachineBasicMultiChannelPerlin noise;
    public PlayerStateMachine movementStateMachine;
    public PlayerStateMachine attackStateMachines;
    [NonSerialized] public MovementHandler playerMovement;
    //[NonSerialized] public CharacterController controller;
    [Space(10)]
    [NonSerialized] public Transform switchTarget;
    #region states
    public PlayerIdleState idleState;
    public PlayerWalkState walkingState;
    public PlayerRunningState runningState;
    public PlayerLockInState lockInState;
    public PlayerIdleAttackState idleAttackState;
    public PlayerAttackState attackState;
    public PlayerHitState hitState;
    public PlayerDieState dieState;
    #endregion states
    [Header("Animations")]
    [SerializeField] public GameObject playerModel;
    [SerializeField] public Animator animator;
    [SerializeField] public PlayerInputs input;
    [NonSerialized] public PlayerSounds playerSounds;

    [SerializeField] public GameObject InventoryUIObject;
    [NonSerialized] public UIDocument UIDOC;
    [NonSerialized] public PauseManager pauseManager;
    [NonSerialized] public SaveScreen saveScreen;
    

    [NonSerialized] public HandleItemInteraction itemPickUp;
    [NonSerialized] public static bool enteredNewRoom;
    [NonSerialized] public bool isSaving = false;
    static readonly int inputMagnitude = Animator.StringToHash("Input Magnitude");
    [Header("Inverse Kunematics")]
    [SerializeField]IK_Base ikPistol;

    //==========INVENTORY===========//
    public InventoryObject inventory;
    
    [SerializeField] public GameObject bulletPrefab;
    [SerializeField] public GameObject bulletStartPosition;
    ItemEquip itemEquip;
    [Header("Events")]
    Events events;

    [Header("Bobbing")]
    [SerializeField] public BobbingStats bobbingStats;
    [SerializeField]public RenderTexture heartRate;
    int animatorInjuryLevelLayerIndex;

    public float maxHealth { get ; set; }
    public float currentHealth { get ; set ; }

    private void Awake()
    {
        followCam = mainCamera;
        PlayerModelHandler.IsApplyRotation = true;
        MovementHandler.moveDir = Vector3.zero;
        enteredNewRoom = false;
        for (int i = 0; i < transform.childCount; i++)
        {
            if (transform.GetChild(i).CompareTag("Utility"))
            {
                switchTarget = transform.GetChild(i);
            }
        }
        maxHealth = 100;
        currentHealth = 100;
        playerSounds = GetComponent<PlayerSounds>();

        pauseManager = GetComponent<PauseManager>();
        saveScreen = GetComponent<SaveScreen>();

        itemPickUp = GetComponentInChildren<HandleItemInteraction>();
        UIDOC = InventoryUIObject.GetComponent<UIDocument>();
        itemEquip = GetComponent<ItemEquip>();

        //Others
        bobbingStats = GetComponent<BobbingStats>();

        //========EVENTS============//
        events = GetComponent<Events>();
        inventory.Initialize(UIDOC, itemEquip, ikPistol, this);
        events.Initialize();
        movementStateMachine = new PlayerStateMachine();
        attackStateMachines = new PlayerStateMachine();
        playerMovement = GetComponent<MovementHandler>();

        // Locomotion States
        idleState = new PlayerIdleState(this, movementStateMachine, attackStateMachines, animator);
        walkingState = new PlayerWalkState(this, movementStateMachine, attackStateMachines, animator);
        runningState = new PlayerRunningState(this, movementStateMachine, attackStateMachines, animator);
        hitState = new PlayerHitState(this, movementStateMachine, attackStateMachines, animator);
        dieState = new PlayerDieState(this, movementStateMachine, attackStateMachines, animator);

        // Attacking States
        lockInState = new PlayerLockInState(this, movementStateMachine, attackStateMachines, events, animator);
        idleAttackState = new PlayerIdleAttackState(this, movementStateMachine, attackStateMachines, animator);
        attackState = new PlayerAttackState(this, movementStateMachine, attackStateMachines, events, animator);

    }
    void Start()
    {
        input.EnablePlayerActions();
        movementStateMachine.Initialize(idleState);
        attackStateMachines.Initialize(idleAttackState);
        UIDialogueBox.Initialize(this);
        if(UIFade.StartDarkToLightTransition() != null)
            StartCoroutine(UIFade.StartDarkToLightTransition());
        animatorInjuryLevelLayerIndex = animator.GetLayerIndex("Injury Level");
    }
    private void OnDisable() {
        inventory.OnDisable();
        PlayerInputs.isInMap = false;
    }
    void Update()
    {
        movementStateMachine.currentState.FrameUpdate();
        attackStateMachines.currentState.FrameUpdate();
        inventory.Update();
        //UITEMP.HEIGHT = "currentattach state : " + attackStateMachines.currentState;
        if(isBeingChased)
        {
            PlayerSounds.instance.PlayChaseSound(1);
        }else
        {
            PlayerSounds.instance.StopChaseSound();
        }
    }
    void FixedUpdate()
    {
        movementStateMachine.currentState.PhysUpdate();
        attackStateMachines.currentState.PhysUpdate();
    }

    public void Damage(float damageAmount, Enemy enemy)
    {
        //Vector2 pushDirection = (transform.position - enemy.transform.position).normalized;
        //rb2d.linearVelocity = Vector2.zero;
        //playerMovement.rb.AddForce(pushDirection * 1000f, ForceMode2D.Impulse);
        hitState.SetEnemy(enemy);
        attackStateMachines.ChangeState(idleAttackState);
        movementStateMachine.ChangeState(hitState);
        Debug.Log("Player hit for " + damageAmount + " damage");
    }
    public void Damage(float damageAmount)
    {
        Debug.Log("damage 1");
    }
    public float GetRandomDamageAmount()
    {
        //8 max shots, 4 min
        RangedItem weapon = (RangedItem)inventory.equipedItem;
        return UnityEngine.Random.Range(weapon.damageFloor, weapon.damageCeiling);
    }
    public void Heal(int amount)
    {
        if(currentHealth+amount>maxHealth)
            currentHealth = maxHealth;
        else
            currentHealth+=amount;
            
        float healthPercentage = currentHealth/maxHealth;
        Debug.Log("healed fopr " + amount);
        Color color =  GetHealthColor(healthPercentage);
        
        noise.AmplitudeGain = 0f;
        noise.FrequencyGain = 0f;
        FindAnyObjectByType<LowHealthPulse>().isLowHealth = false;
        if (healthPercentage <= .30f)
        {
            noise.AmplitudeGain = .1f;
            noise.FrequencyGain = 1f;
            FindAnyObjectByType<LowHealthPulse>().isLowHealth = true;
            PlayerSounds.instance.PlayHeartBeat(1);
        }
        else
        {
            PlayerSounds.instance.StopHeartBeat();
        }
        animator.SetLayerWeight(animatorInjuryLevelLayerIndex, (1-healthPercentage)-.3f);
        inventory.health.style.unityBackgroundImageTintColor = color;
        Color col = new Color(color.r, color.g, color.b, .06f);
        inventory.health.style.backgroundColor = col;
    }
    public void Die()
    {
        
    }
    public void Damage(float damageAmount, Vector2 hitNormal, Enemy enemy)
    {
        input.isAiming = false;
        if(enemy != null)
        {
            hitState.SetEnemy(enemy);
            movementStateMachine.ChangeState(hitState);
        }
        attackStateMachines.ChangeState(idleAttackState);
        currentHealth -= damageAmount;
        Vector3 worldNormal = new Vector3(hitNormal.x, hitNormal.y, 0f);
        Vector3 localNormal = playerModel.transform.InverseTransformDirection(worldNormal);
        float customHitY = Vector3.Dot(worldNormal, playerModel.transform.forward);
        animator.SetTrigger("triggerHit");
        animator.SetFloat("hitX", localNormal.x);
        animator.SetFloat("hitY", localNormal.y);

        float healthPercentage = currentHealth/maxHealth;

        Color color =  GetHealthColor(healthPercentage);
        noise.AmplitudeGain = 0f;
        noise.FrequencyGain = 0f;
        FindAnyObjectByType<LowHealthPulse>().isLowHealth = false;
        if (healthPercentage <= .30f)
        {
            noise.AmplitudeGain = .1f;
            noise.FrequencyGain = 1f;
            FindAnyObjectByType<LowHealthPulse>().isLowHealth = true;
            PlayerSounds.instance.PlayHeartBeat(1);
        }else
        {
            PlayerSounds.instance.StopHeartBeat();
        }
        
        animator.SetLayerWeight(animatorInjuryLevelLayerIndex, (1-healthPercentage)-.3f);
        inventory.health.style.unityBackgroundImageTintColor = color;
        Color col = new Color(color.r, color.g, color.b, .06f);
        inventory.health.style.backgroundColor = col;
        if(currentHealth<=0)
        {
            animator.SetLayerWeight(animatorInjuryLevelLayerIndex, 0);
            attackStateMachines.ChangeState(dieState);
            movementStateMachine.ChangeState(dieState);
        }
    }
    public void SetHealth(float damageAmount )
    {
        currentHealth -= damageAmount;

        float healthPercentage = currentHealth/maxHealth;

        Color color =  GetHealthColor(healthPercentage);
        noise.AmplitudeGain = 0f;
        noise.FrequencyGain = 0f;
        FindAnyObjectByType<LowHealthPulse>().isLowHealth = false;
        if (healthPercentage <= .30f)
        {
            noise.AmplitudeGain = .1f;
            noise.FrequencyGain = 1f;
            FindAnyObjectByType<LowHealthPulse>().isLowHealth = true;
            PlayerSounds.instance.PlayHeartBeat(1);
        }else
        {
            PlayerSounds.instance.StopHeartBeat();
        }
        
        animator.SetLayerWeight(animatorInjuryLevelLayerIndex, (1-healthPercentage)-.3f);
        inventory.health.style.unityBackgroundImageTintColor = color;
        Color col = new Color(color.r, color.g, color.b, .06f);
        inventory.health.style.backgroundColor = col;
        if(currentHealth<=0)
        {
            animator.SetLayerWeight(animatorInjuryLevelLayerIndex, 0);
            attackStateMachines.ChangeState(dieState);
            movementStateMachine.ChangeState(dieState);
        }
    }

    private Color GetHealthColor(float healthPercentage)
    {
        Color color =  new Color();
        Colors colorManager = GetComponent<Colors>();
        if (healthPercentage >= 1f) {
            color = colorManager.full;
        } else if (healthPercentage <= .25f) {
            color = colorManager.lowest;
        } else if (healthPercentage <= .5f) {
            color = colorManager.low;
        } else if (healthPercentage <= .75f) {
            color = colorManager.mid;
        } else {
            color = colorManager.high;
        }
        return color;
    }


    //==============INPUT EVENTS==================//
    /*public void OnCameraAngle(InputAction.CallbackContext val)
    {
        Vector2 playerForward = new Vector2(MovementHandler.GetTransformForward().x, MovementHandler.GetTransformForward().z);
        Vector2 camForward = new Vector2(playerMovement.camBehaviorHandler.GetCamera().transform.parent.right.x, playerMovement.camBehaviorHandler.GetCamera().transform.parent.right.z);
        float angle = Vector2.SignedAngle(camForward, playerForward);
        playerMovement.camBehaviorHandler.changeCamAngle(angle);
    }*/

}