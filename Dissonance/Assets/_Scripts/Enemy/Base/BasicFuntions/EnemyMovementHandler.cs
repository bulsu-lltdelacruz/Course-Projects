using System;
using UnityEngine;

public class EnemyMovementHandler : MonoBehaviour
{
    Enemy enemy;
    //public static Vector3 moveDir;
    public static Vector2 prevMoveDir;
    private static Vector3 movementRelativeToCam;
    private Vector3 prevRelativeToCamMoveDir;
    //state vars
    private bool isGrounded = true;
    public bool enableMovement = true;
    private static Vector3 forward;

    private void Start()
    {
        forward = transform.forward;
    }
    public void InitComponents(Enemy enemy)
    {
        this.enemy = enemy;
    }
    public void FrameUpdate()
    {
        if(enableMovement)
            MoveCharacter();
        forward = transform.forward;
    }
    public void PhysUpdate()
    {
        
    }
    private void MoveCharacter()
    {

        float angle = CheckSlope();
    }
    //yung prevmove saka now move is m=becming the same, fix that

    private float CheckSlope()
    {
        float angle = 0;

        Vector3 origin = transform.position + (transform.forward * 0.3f);
        RaycastHit hit;
        if (Physics.Raycast(origin, Vector3.down, out hit, Mathf.Infinity, LayerMask.GetMask("Ground")))
        {
            angle = Vector3.Angle(hit.normal, transform.up);
        }
        return angle;
    }
    /// EVENTS EVENTS EVENTS EVENTS EVENTS EVENTS EVENTS EVENTS EVENTS EVENTS
    /*void OnTriggerStay(Collider other)
    {
        if (!isGrounded)
        {
            //rb.linearDamping = 12;
            isGrounded = true;
        }
    }
    void OnTriggerExit(Collider other)
    {
        if (isGrounded)
        {
            //rb.linearDamping = 0;
            isGrounded = false;
        }
    }*/
    // GETTERS SETTERS    GETTERS SETTERS    GETTERS SETTERS    GETTERS SETTERS    GETTERS SETTERS    
    public static Vector3 GetTransformForward()
    {
        return forward;
    }
    public static Vector3 GetMovementRelativeToCam()
    {
        return movementRelativeToCam;
    }
    
}
