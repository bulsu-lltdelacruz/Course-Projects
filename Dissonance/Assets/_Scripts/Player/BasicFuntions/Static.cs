using System;
using UnityEngine;


public struct S_PlayerStats
{
    public static float walkSpeed = 10f;
    public static float runningSpeed = 17f;
    public static float currentMoveSpeed = walkSpeed;
    public static float inputAngleDiffOnAngleChange = 30f;//for entering room first time, limit it first then change
    public static bool camAngleChanged = false;
    //after moving away 
    public static float rotateSpeed = 30f;
    //difference of two raycast
    public static float slopeValue = 0.3f;
    public static float slopeAngleThreshHold = 20f;
    public static float hitboxYoffset;

}
public struct S_BasicEnemyStats
{
    public static float walkSpeed = 10f;
    public static float runningSpeed = 17f;
    public static float currentMoveSpeed = walkSpeed;

}
public struct S_Animations
{
    public static float damp = 0.05f;
}
public struct S_VolumeStats
{
    public static float gunfire = 0.05f;
    public static float buttonHover = 0.35f;
    public static float buttonClick = 0.5f;
    public static float openDoor = 0.5f;
    public static float openLockedDoor = 0.5f;
    public static float unlockDoor = 0.5f;
    public static float wirecutter = 0.5f;
    public static float screwdriver = 0.5f;
    public static float pickUpItem = 0.3f;
}

public struct Strings
{
    public static string DOOR = "door";
    public static string ITEM = "item";
    public static string NOTES = "notes";
}
