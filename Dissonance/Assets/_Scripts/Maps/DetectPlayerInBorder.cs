using UnityEngine;

public class DetectPlayerInBorder : MonoBehaviour
{
    void OnTriggerEnter2D(Collider2D collision)
    {
        /*CamBehaviorHandler.isPlayerInRange = true;
        CamBehaviorHandler.lockedXPosition = CamBehaviorHandler.camFollowTransform.position.x;
        if(Player.enteredNewRoom)
        {
            Vector3 updatedPosition = CamBehaviorHandler.camFollowTransform.localPosition;
            updatedPosition.x = -updatedPosition.x;
            CamBehaviorHandler.camFollowTransform.localPosition = updatedPosition;
            Player.enteredNewRoom = false;
        }*/
    }
    void OnTriggerExit2D(Collider2D collision)
    {
        //CamBehaviorHandler.isPlayerInRange = false;
    }
}
