using System;
using UnityEngine;

public class MovementHandler : MonoBehaviour, IDataPersistence
{
    [NonSerialized]public Rigidbody2D rb;
    Player player;
    [NonSerialized]public CamBehaviorHandler camBehaviorHandler;
    [NonSerialized]public PlayerInputs inputs;
    public static Vector3 moveDir;
    public static Vector2 prevMoveDir;
    private static Vector3 movementRelativeToCam;
    private Vector3 prevRelativeToCamMoveDir;
    private bool didChangeDir = true;
    //state vars
    private bool isGrounded = false;
    public bool enableMovement = true;

    void Awake()
    {
        rb = GetComponent<Rigidbody2D>();
        player = GetComponent<Player>();
    }
    private void Start()
    {
        
    }
    public void InitComponents(Player player, CamBehaviorHandler camBehaviorHandler, PlayerInputs playerInputs)
    {
        this.camBehaviorHandler = camBehaviorHandler;
        this.inputs = playerInputs;
        //this.player = player;

    }
    public void FrameUpdate()
    {
        moveDir =  inputs.Direction;
    }
    public void PhysUpdate()
    {
        rb.linearVelocity = new Vector2(
            moveDir.x*S_PlayerStats.currentMoveSpeed*Time.deltaTime,
            moveDir.y*S_PlayerStats.currentMoveSpeed*Time.deltaTime
        ) * S_PlayerStats.currentMoveSpeed;
        //rb2d.linearVelocity = Vector2.zero;
    }

    /// EVENTS EVENTS EVENTS EVENTS EVENTS EVENTS EVENTS EVENTS EVENTS EVENTS
    void OnTriggerStay(Collider other)
    {
        if (!isGrounded)
        {
//            rb.linearDamping = 12;
            isGrounded = true;
        }
    }
    void OnTriggerExit(Collider other)
    {
        if (isGrounded)
        {
        //    rb.linearDamping = 0;
            isGrounded = false;
        }
    }
    // GETTERS SETTERS    GETTERS SETTERS    GETTERS SETTERS    GETTERS SETTERS    GETTERS SETTERS    
 // In MovementHandler.cs
// Remove the static 'forward' variable and change the getter:
public static Vector3 GetTransformForward(Transform playerTransform)
    {
        // Always calculate based on the current instance's transform
        return playerTransform.forward;
    }
    public static Vector3 GetMovementRelativeToCam()
    {
        return moveDir;
    }

    public void LoadData(GameData data)
    {
        this.player.transform.position = data.playerCoordinates;
        float damageAmount = 100-data.currentHp;
        //this.player.Damage(damageAmount, Vector2.zero, null);
        this.player.SetHealth(damageAmount);
    }

    public void SaveData(ref GameData data)
    {
        data.playerCoordinates = this.player.transform.position;
        data.currentHp = this.player.currentHealth;
    }
}
