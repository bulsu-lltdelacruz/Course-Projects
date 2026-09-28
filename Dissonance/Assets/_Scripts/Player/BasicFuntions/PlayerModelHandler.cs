using Unity.Mathematics;
using Unity.VisualScripting;
using UnityEngine;

public class PlayerModelHandler : MonoBehaviour
{
    public static bool IsApplyRotation = true;
    [SerializeField] private GameObject playerModel;
    [SerializeField] private float interpolateSpeed;
    static Vector3 moveDir;
    private CharacterController controller;

    void Awake()
    {
        controller = GetComponent<CharacterController>();
    }
    void Start()
    {

    }

    void Update()
    {
        if (MovementHandler.GetMovementRelativeToCam() != Vector3.zero)
            moveDir = MovementHandler.GetMovementRelativeToCam();
    }
    void FixedUpdate()
    {
        if (IsApplyRotation)
            ApplyRotation(moveDir);
    }

    private void ApplyRotation(Vector3 moveDir)
    {
        
        //Quaternion xOffset = Quaternion.Euler(-40f, 0f, 0f);
        Vector3 lookTowards = new Vector3(moveDir.x,0f, moveDir.y).normalized;
        
        
        if (/*lookTowards != transform.forward && */lookTowards != Vector3.zero)
        {
            Quaternion targetRotation = Quaternion.LookRotation(lookTowards);
            playerModel.transform.localRotation = Quaternion.Slerp(playerModel.transform.localRotation, targetRotation, interpolateSpeed);
            //playerModel.transform.localRotation
        }

    }
    public static void SetRotationDirection(Vector3 moveDirs)
    {
        moveDir = moveDirs;
    }
}
