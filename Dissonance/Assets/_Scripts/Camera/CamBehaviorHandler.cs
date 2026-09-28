using Unity.Cinemachine;
using UnityEngine;

public class CamBehaviorHandler : MonoBehaviour
{
    [SerializeField]public  CinemachineCamera camera;
    [SerializeField]public static GameObject camFollow;
    public static Transform camFollowTransform;
    [SerializeField] CinemachineBrain brain;
    public static bool isPlayerInRange = false;
    public static 
    float lockedXPosition = 0f;

    void Start()
    {
        camFollow = camera.Follow.gameObject;
        camFollowTransform = camFollow.transform;
    }
    void LateUpdate()
    {
        /*if(isPlayerInRange)
        {
            Vector3 currenPos = camFollowTransform.position;
            currenPos.x = lockedXPosition;
            camFollowTransform.position = currenPos;
        }else if(camFollowTransform.localPosition!= Vector3.zero)
        {
                camFollowTransform.localPosition = Vector3.Lerp(
                camFollowTransform.localPosition, 
                Vector3.zero, 
                Time.deltaTime * .5f
            );
        }*/
    }
}
