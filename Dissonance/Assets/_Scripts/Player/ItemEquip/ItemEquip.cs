using System;
using UnityEngine;
using UnityEngine.Animations.Rigging;

public class ItemEquip : MonoBehaviour
{
    
    [SerializeField] public GameObject TEMPITEMPREFAB;
    [SerializeField] public GameObject TEMPLEFT;
    [SerializeField] public GameObject TEMPRIGHT;

    [Header("Pistol Equip and Aim Position")]
    [Space(2)]
    [SerializeField] public Transform weaponPosParent;
    [SerializeField] public GameObject Pistol_equipWeaponPos;
    [SerializeField] public GameObject Pistol_aimWeaponPos;
    [Header("Rifle Equip and Aim Position")]
    [Space(2)]
    [SerializeField] public GameObject Rifle_equipWeaponPos;
    [SerializeField] public GameObject Rifle_aimWeaponPos;
    [Header("Shotgun Equip and Aim Position")]
    [Space(2)]
    [SerializeField] public GameObject Shotgun_equipWeaponPos;
    [SerializeField] public GameObject Shotgun_aimWeaponPos;

    //[SerializeField] public GameObject weaponAimPlaceHolder;
    [Header("Right Hand Target")]
    [Space(2)]
    [SerializeField] public TwoBoneIKConstraint rightHandIK;
    [SerializeField] public Transform rightHandTarget;
    [SerializeField] public Transform rightHandHint;
    [Header("Left Hand Target")]
    [Space(2)]
    [SerializeField] public TwoBoneIKConstraint leftHandIK;
    [SerializeField] public Transform leftHandTarget;
    [SerializeField] public Transform leftHandHint;

}
