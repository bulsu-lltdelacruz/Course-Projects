using System;
using Unity.Mathematics;
using Unity.VisualScripting;
using UnityEngine;

public class EquipWeapon//equips weapon and fixes positioningsss
{

    GameObject IKWeaponLeftHandPos;
    GameObject IKWeaponRightHandPos;
    public GameObject itemPrefab;
    public ItemEquip itemEquipInfo;
    IK_Base ikPistol;
    String equipedItemName = "";
    bool isWeaponEquipped = false;
    public Vector3 startPos;
    private float time;
    private Player player;
    #region 
    //bobbing

    #endregion

    public EquipWeapon(ItemEquip itemEquipInfo, IK_Base ikPistol, Player player)
    {
        this.itemEquipInfo = itemEquipInfo;
        this.ikPistol = ikPistol;
        this.player = player;
    }
    public void Equip(GameObject itemPrefab, string equipedItemName)
    {
        startPos = itemPrefab.transform.localPosition;
        
        this.itemPrefab = itemPrefab;
        IKWeaponLeftHandPos = new GameObject("IKLeftPos");
        IKWeaponRightHandPos = new GameObject("IKRightPos");

        this.equipedItemName = equipedItemName;
        isWeaponEquipped = true;
        UnAim();

    }
    public void UnEquip()
    {
        itemPrefab = null;
        isWeaponEquipped = false;
        itemEquipInfo.leftHandIK.weight = 0f;
        itemEquipInfo.rightHandIK.weight = 0f;

    }
    public void Update()
    {
        //if merong equipped
        Vector3 direction = Vector3.zero;
        if(itemPrefab== null)
            return;
        float offset = 0f;;
        if(player.movementStateMachine.currentState != player.runningState
        && player.attackStateMachines.currentState != player.lockInState)
        {
            time += Time.deltaTime * player.bobbingStats.frequencyIdleEquip;
            offset = Mathf.Sin(time) * player.bobbingStats.amplitudeIdleEquip;
            direction = itemPrefab.transform.up;

        }else if(player.movementStateMachine.currentState == player.runningState
        && player.attackStateMachines.currentState != player.lockInState)
        {
            time += Time.deltaTime * player.bobbingStats.frequencyRunEquip;
            offset = Mathf.Sin(time) * player.bobbingStats.amplitudeRunEquip;
            direction = itemPrefab.transform.right;

        }else if(player.attackStateMachines.currentState != player.lockInState)
        {
            
        }

        itemPrefab.transform.localPosition = startPos + direction * offset;

        itemEquipInfo.rightHandTarget.position = IKWeaponRightHandPos.transform.position;
        itemEquipInfo.rightHandTarget.rotation = IKWeaponRightHandPos.transform.rotation;
        
        itemEquipInfo.leftHandTarget.position = IKWeaponLeftHandPos.transform.position;
        itemEquipInfo.leftHandTarget.rotation = IKWeaponLeftHandPos.transform.rotation;
        

    }
    public void Aim()
    {
        if (isWeaponEquipped)
        {
            SetIKTargetPosition(
                ikPistol.aimLeftLocalPos,
                ikPistol.aimLeftLocalRot,
                ikPistol.aimRightLocalPos,
                ikPistol.aimRightLocalRot,
                ikPistol.H_aimLeftLocalPos,
                ikPistol.H_aimLeftLocalRot,
                ikPistol.H_aimRightLocalPos,
                ikPistol.H_aimRightLocalRot,
                itemEquipInfo.Pistol_aimWeaponPos.transform
            );
        }
        
    }
    public void UnAim()
    {
        if (isWeaponEquipped)
        {
            SetIKTargetPosition(
                ikPistol.equipLeftLocalPos,
                ikPistol.equipLeftLocalRot,
                ikPistol.equipRightLocalPos,
                ikPistol.equipRightLocalRot,
                ikPistol.H_equipLeftLocalPos,
                ikPistol.H_equipLeftLocalRot,
                ikPistol.H_equipRightLocalPos,
                ikPistol.H_equipRightLocalRot,
                itemEquipInfo.Pistol_equipWeaponPos.transform
            );
        }
    }
    void SetIKTargetPosition(
        Vector3 leftLocalPos,
        Vector3 leftLocalRot,
        Vector3 rightLocalPos,
        Vector3 rightLocalRot,
        Vector3 H_LeftLocalPos,
        Vector3 H_LeftLocalRot,
        Vector3 H_RightLocalPos,
        Vector3 H_RightLocalRot,
        Transform weaponParent
    )
    {
        // PUT THE TARGET OF THE TIP(BOTH LEFT AND RIGHT) AS CHILD OF ITEMPRREFAB
        
        IKWeaponLeftHandPos.transform.parent = itemPrefab.transform;
        IKWeaponLeftHandPos.transform.localPosition = leftLocalPos;
        IKWeaponLeftHandPos.transform.localEulerAngles = leftLocalRot;

        IKWeaponRightHandPos.transform.parent = itemPrefab.transform;
        IKWeaponRightHandPos.transform.localPosition = rightLocalPos;
        IKWeaponRightHandPos.transform.localEulerAngles = rightLocalRot;
        
        //========================================================================
        //PUT ITEM PREFAB AS CHILD OF EQUIP POSITION
        itemPrefab.transform.parent = weaponParent;
        itemPrefab.transform.position = weaponParent.position;
        itemPrefab.transform.rotation = weaponParent.rotation;

        itemEquipInfo.leftHandIK.weight = 1f;
        itemEquipInfo.rightHandIK.weight = 1f;
        //===========SET HINT=============//
        
        itemEquipInfo.leftHandHint.transform.localPosition = H_LeftLocalPos;
        itemEquipInfo.leftHandHint.transform.localEulerAngles = H_LeftLocalRot;

        itemEquipInfo.rightHandHint.transform.localPosition = H_RightLocalPos;
        itemEquipInfo.rightHandHint.transform.localEulerAngles = H_RightLocalRot;

        //==== SET TARGET(TIP) JOINT TO POSITIONN IN WEAPON
        //right
        //avatar target = weapon target
        itemEquipInfo.rightHandTarget.position = IKWeaponRightHandPos.transform.position;
        itemEquipInfo.rightHandTarget.rotation = IKWeaponRightHandPos.transform.rotation;
        
        //left
        itemEquipInfo.leftHandTarget.position = IKWeaponLeftHandPos.transform.position;
        itemEquipInfo.leftHandTarget.rotation = IKWeaponLeftHandPos.transform.rotation;
    }
}
