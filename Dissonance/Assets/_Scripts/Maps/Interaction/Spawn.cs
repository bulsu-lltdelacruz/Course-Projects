using System.Collections;
using UnityEngine;

public class Spawn : MonoBehaviour
{
    [SerializeField] public Enemy[] enemiesInside;
    [SerializeField] public GameObject[] lightsInside;
    public void EnableEnemies()
    {
        if (enemiesInside != null)
        {
            foreach (Enemy enemy in enemiesInside)
            {
                if (enemy == null) continue;
                enemy.gameObject.SetActive(true);
            }
        }
        if (lightsInside != null)
        {
            foreach (GameObject light in lightsInside)
            {
                if (light == null) continue;
                light.gameObject.SetActive(true);
            }
        }
    }
    public void DisableEnemies()
    {
        if(lightsInside != null)
        {
            //StartCoroutine(DisableLightsAfterTime());
        }
        if(enemiesInside != null && lightsInside != null)
        {
            StartCoroutine(DisableAfterTime());
        }
        
    }
    IEnumerator DisableLightsAfterTime()
    {
        float totalTime = Time.time + 0.5f;
        while (Time.time < totalTime)
            yield return null;

        if (lightsInside != null)
        {
            foreach (GameObject light in lightsInside)
            {
                if (light == null) continue;
                light.gameObject.SetActive(false);
            }
        }

        
    }
    IEnumerator DisableAfterTime()
    {
        float totalTime = Time.time + 0.5f;
        while (Time.time < totalTime)
            yield return null;

        foreach (Enemy enemy in enemiesInside)
        {
            if (enemy == null) continue;
            if (enemy.isAlive)
                enemy.gameObject.SetActive(false);
        }

        
    }
}
