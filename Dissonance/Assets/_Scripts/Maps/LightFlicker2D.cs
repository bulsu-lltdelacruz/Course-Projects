using System.Collections;
using UnityEngine;
using UnityEngine.Rendering.Universal;

public class LightFlicker2D : MonoBehaviour
{
    [Header("Base Settings")]
    [SerializeField] private float baseIntensity = 1f;
    [SerializeField] private float minIntensity  = 0.4f;
    [SerializeField] private float maxIntensity  = 1f;

    [Header("Flicker Speed")]
    [SerializeField] private float minFlickerInterval = 0.05f;
    [SerializeField] private float maxFlickerInterval = 0.2f;

    [Header("Smooth Transition")]
    [SerializeField] private bool  smoothTransition    = true;
    [SerializeField] private float transitionSpeed     = 8f;

    [Header("Burst Flicker")]
    [Tooltip("Occasionally triggers a rapid burst of flickers")]
    [SerializeField] private bool  enableBursts        = true;
    [SerializeField] private float burstChance         = 0.15f;   // 0–1 probability per cycle
    [SerializeField] private int   minBurstCount       = 3;
    [SerializeField] private int   maxBurstCount       = 8;
    [SerializeField] private float burstInterval       = 0.04f;

    [Header("Dropout")]
    [Tooltip("Chance light fully turns off for a moment")]
    [SerializeField] private bool  enableDropout       = true;
    [SerializeField] private float dropoutChance       = 0.05f;
    [SerializeField] private float minDropoutDuration  = 0.05f;
    [SerializeField] private float maxDropoutDuration  = 0.3f;

    [Header("Randomness")]
    [SerializeField] private bool  addRandomness       = true;
    [Tooltip("How much random noise is added each cycle (0 = none)")]
    [SerializeField] [Range(0f, 1f)] private float randomnessFactor = 0.3f;
    [Tooltip("Seed for randomness — 0 = truly random each play")]
    [SerializeField] private int   randomSeed          = 0;

    [Header("Color Flicker")]
    [SerializeField] private bool  enableColorFlicker  = false;
    [SerializeField] private Color baseColor           = Color.white;
    [SerializeField] private Color flickerColor        = new Color(1f, 0.9f, 0.7f);
    [SerializeField] [Range(0f, 1f)] private float colorFlickerStrength = 0.5f;

    [Header("Debug")]
    [SerializeField] private bool  flickerEnabled      = true;

    // ── Private ───────────────────────────────────────────────────────────────

    private Light2D light2D;
    private float   targetIntensity;
    private bool    isDropout = false;

    void Awake()
    {
        light2D = GetComponent<Light2D>();
        if (light2D == null)
        {
            Debug.LogError("LightFlicker2D: No Light2D component found on " + gameObject.name);
            enabled = false;
            return;
        }

        if (randomSeed != 0)
            Random.InitState(randomSeed);

        targetIntensity     = baseIntensity;
        light2D.intensity   = baseIntensity;
        if (enableColorFlicker)
            light2D.color   = baseColor;
    }

    void OnEnable()
    {
        StartCoroutine(FlickerRoutine());
    }

    void OnDisable()
    {
        StopAllCoroutines();
        if (light2D != null)
        {
            light2D.intensity = baseIntensity;
            if (enableColorFlicker)
                light2D.color = baseColor;
        }
    }

    void Update()
    {
        if (!flickerEnabled || light2D == null || isDropout) return;

        if (smoothTransition)
        {
            light2D.intensity = Mathf.Lerp(
                light2D.intensity,
                targetIntensity,
                Time.deltaTime * transitionSpeed);
        }
        else
        {
            light2D.intensity = targetIntensity;
        }
    }

    // ── Flicker coroutine ─────────────────────────────────────────────────────

    IEnumerator FlickerRoutine()
    {
        while (true)
        {
            if (!flickerEnabled)
            {
                yield return null;
                continue;
            }

            // ── Dropout ───────────────────────────────────────────────────
            if (enableDropout && Random.value < dropoutChance)
            {
                yield return StartCoroutine(DropoutRoutine());
                continue;
            }

            // ── Burst ─────────────────────────────────────────────────────
            if (enableBursts && Random.value < burstChance)
            {
                yield return StartCoroutine(BurstRoutine());
                continue;
            }

            // ── Normal flicker ────────────────────────────────────────────
            SetRandomTarget();

            float interval = Random.Range(minFlickerInterval, maxFlickerInterval);

            if (addRandomness)
                interval *= 1f + Random.Range(-randomnessFactor, randomnessFactor);

            yield return new WaitForSeconds(Mathf.Max(0.01f, interval));
        }
    }

    IEnumerator DropoutRoutine()
    {
        isDropout         = true;
        light2D.intensity = 0f;
        targetIntensity   = 0f;

        float duration = Random.Range(minDropoutDuration, maxDropoutDuration);

        if (addRandomness)
            duration *= 1f + Random.Range(-randomnessFactor * 0.5f, randomnessFactor * 0.5f);

        yield return new WaitForSeconds(duration);

        isDropout = false;
        SetRandomTarget();
    }

    IEnumerator BurstRoutine()
    {
        int count = Random.Range(minBurstCount, maxBurstCount + 1);

        for (int i = 0; i < count; i++)
        {
            // Alternate between low and high intensity rapidly
            targetIntensity   = (i % 2 == 0) ? minIntensity : maxIntensity;
            light2D.intensity = targetIntensity;

            float interval = burstInterval;
            if (addRandomness)
                interval *= 1f + Random.Range(-randomnessFactor, randomnessFactor);

            yield return new WaitForSeconds(Mathf.Max(0.01f, interval));
        }

        SetRandomTarget();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    void SetRandomTarget()
    {
        targetIntensity = Random.Range(minIntensity, maxIntensity);

        if (addRandomness)
        {
            float noise = Random.Range(-randomnessFactor, randomnessFactor)
                          * (maxIntensity - minIntensity);
            targetIntensity = Mathf.Clamp(targetIntensity + noise, minIntensity, maxIntensity);
        }

        if (enableColorFlicker && light2D != null)
        {
            float t       = Mathf.InverseLerp(minIntensity, maxIntensity, targetIntensity);
            float blend   = Mathf.Lerp(0f, colorFlickerStrength, 1f - t);
            light2D.color = Color.Lerp(baseColor, flickerColor, blend);

            if (addRandomness)
            {
                float randomBlend = Random.Range(0f, colorFlickerStrength * randomnessFactor);
                light2D.color     = Color.Lerp(light2D.color, flickerColor, randomBlend);
            }
        }
    }

    // ── Public API ────────────────────────────────────────────────────────────

    public void SetFlickerEnabled(bool enabled)
    {
        flickerEnabled = enabled;
        if (!enabled && light2D != null)
            light2D.intensity = baseIntensity;
    }

    public void TriggerDropout(float duration = -1f)
    {
        if (duration < 0)
            duration = Random.Range(minDropoutDuration, maxDropoutDuration);
        StartCoroutine(ManualDropout(duration));
    }

    IEnumerator ManualDropout(float duration)
    {
        isDropout         = true;
        light2D.intensity = 0f;
        yield return new WaitForSeconds(duration);
        isDropout         = false;
        SetRandomTarget();
    }
}