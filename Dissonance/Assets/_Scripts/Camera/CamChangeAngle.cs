using UnityEngine;

public class CamChangeAngle : MonoBehaviour
{
    // di nag tutrue after sa loob kasi na change na, 
    private void OnTriggerEnter(Collider other)
    {
        // more leeway for inpt angle
        if (other.CompareTag("Player"))
            S_PlayerStats.camAngleChanged = true;
    }

    private void OnTriggerExit(Collider other)
    {
        //S_PlayerStats.camAngleChanged = false;
    }
}
