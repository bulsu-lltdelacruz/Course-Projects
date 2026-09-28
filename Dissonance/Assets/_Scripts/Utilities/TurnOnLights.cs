using System.Collections;
using UnityEngine;

public class TurnOnLights : MonoBehaviour
{
    [SerializeField] public Enemy[] enemiesInside;
    [SerializeField] public GameObject[] lightsInside;
    
    private Coroutine disableCoroutine;

    void Awake()
    {
        DisableEnemiesHere();
    }

    void OnTriggerEnter2D(Collider2D collision)
    {
        if (!collision.CompareTag("Player")) return;
        EnableEnemiesOnSpawnPoint();
    }

    void OnTriggerExit2D(Collider2D collision)
    {
        if (!collision.CompareTag("Player")) return;
        DisableEnemiesHere();
    }

    void EnableEnemiesOnSpawnPoint()
    {
        if (disableCoroutine != null)
        {
            StopCoroutine(disableCoroutine);
            disableCoroutine = null;
        }

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

    public void DisableEnemiesHere()
    {
        if (lightsInside != null)
        {
            foreach (GameObject light in lightsInside)
            {
                if (light == null) continue;
                light.gameObject.SetActive(false);
            }
        }
        if (enemiesInside != null && lightsInside != null)
        {
            if (disableCoroutine != null) StopCoroutine(disableCoroutine);
            disableCoroutine = StartCoroutine(DisableAfterTime());
        }
    }

    IEnumerator DisableAfterTime()
    {
        yield return new WaitForSeconds(0.5f);

        foreach (Enemy enemy in enemiesInside)
        {
            if (enemy == null) continue;
            if (enemy.isAlive)
                enemy.gameObject.SetActive(false);
        }

    }
}