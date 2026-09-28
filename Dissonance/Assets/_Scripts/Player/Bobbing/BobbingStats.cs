using UnityEngine;

public class BobbingStats : MonoBehaviour
{
    [Header("IdleEquip")]
    [SerializeField]public float amplitudeIdleEquip;
    [SerializeField]public float frequencyIdleEquip;
    [Header("RunEquip")]
    [SerializeField]public float amplitudeRunEquip;
    [SerializeField]public float frequencyRunEquip;
    [Header("PistolFireStats")]
    [SerializeField]public float fireDuration = 0.1f;
    [SerializeField]public float verticalRecoil = 0f;
    [SerializeField]public float backwardRecoil = 0f;
    [SerializeField]public float xRotation = 0f;
    [SerializeField]public float peakDuration = 0.1f;
    [SerializeField]public float recoveryDuration = 0.1f;

}
