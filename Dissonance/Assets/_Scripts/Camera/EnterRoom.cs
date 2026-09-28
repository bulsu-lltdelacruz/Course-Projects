using Unity.Cinemachine;
using UnityEngine;

public class EnterRoom : MonoBehaviour
{
    [SerializeField] private CinemachineVirtualCameraBase roomCamera;
    private void OnTriggerEnter(Collider other)
    {
        // after entering room, make the leeway higher for preserving input
        //revert back in movement handler after moving away
        if(other.CompareTag("Player"))
        {
            CinemachineVirtualCameraBase[] allCameras = FindObjectsByType<CinemachineCamera>(FindObjectsSortMode.None);

            foreach (CinemachineVirtualCameraBase camera in allCameras)
            {//if cur loop is camera attached to script, set its priority to highesst
                if (camera.GetInstanceID() == roomCamera.GetInstanceID())
                {
                    camera.Priority = 5;
                }
                else //else leave everything else to low priority
                {
                    camera.Priority = 1;
                }
            }
        }
        
    }
}
