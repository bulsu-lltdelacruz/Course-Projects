using Unity.Mathematics;
using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.AI;

public class EnemyModelHandler : MonoBehaviour
{
    public static bool IsApplyRotation = true;
    [SerializeField] private GameObject player;
    [SerializeField] private float interpolateSpeed;
    static Vector3 moveDir;
    private Rigidbody rb;

    void Awake()
    {
        rb = GetComponent<Rigidbody>();
    }
    void Start()
    {

    }

    void Update()
    {
        if (EnemyMovementHandler.GetMovementRelativeToCam() != Vector3.zero)
            moveDir = EnemyMovementHandler.GetMovementRelativeToCam();
    }
    void FixedUpdate()
    {
      //  if (IsApplyRotation)
            //ApplyRotation(moveDir);
    }

    private void ApplyRotation(Vector3 moveDir)
    {
        Vector3 lookTowards = new Vector3(moveDir.x, 0f, moveDir.z).normalized;

        if (lookTowards != transform.forward && lookTowards != Vector3.zero)
        {
            Quaternion targetRotation = Quaternion.LookRotation(lookTowards * Time.deltaTime);
            rb.MoveRotation(Quaternion.Slerp(transform.rotation, targetRotation, interpolateSpeed));
        }

    }
    public static void SetRotationDirection(Vector3 moveDirs)
    {
        moveDir = moveDirs;
    }
}
