using UnityEngine;
[CreateAssetMenu(fileName = "IK_Base", menuName = "Scriptable Objects/Inverse Kinematics/IK_Base")]
public class IK_Base : ScriptableObject
{
    [Header("Aim & Equip Position")]
    [Space(10)]
    public GameObject equipPosition;
    public GameObject aimPosition;
    [Header("IK Equip Pos")]
    [Space(10)]
    public Vector3 equipLeftLocalPos;
    public Vector3 equipLeftLocalRot;
    public Vector3 equipRightLocalPos;
    public Vector3 equipRightLocalRot;
    [Header("IK Aim Pos")]
    [Space(10)]
    public Vector3 aimLeftLocalPos;
    public Vector3 aimLeftLocalRot;
    public Vector3 aimRightLocalPos;
    public Vector3 aimRightLocalRot;
    [Header("IK Equip Hint")]
    [Space(10)]
    public Vector3 H_equipLeftLocalPos;
    public Vector3 H_equipLeftLocalRot;
    public Vector3 H_equipRightLocalPos;
    public Vector3 H_equipRightLocalRot;
    [Header("IK Aim Hint")]
    [Space(10)]
    public Vector3 H_aimLeftLocalPos;
    public Vector3 H_aimLeftLocalRot;
    public Vector3 H_aimRightLocalPos;
    public Vector3 H_aimRightLocalRot;
}
