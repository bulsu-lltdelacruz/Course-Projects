using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Rendering.Universal;

[RequireComponent(typeof(Light2D))]
public class Light2DTargetSprite : MonoBehaviour
{
    [Header("Target Settings")]
    [SerializeField] private SpriteRenderer[] targetSprites;
    [SerializeField] private bool updateEveryFrame = false;

    [Header("Light Shape Settings")]
    [Tooltip("Padding around the sprite bounds")]
    [SerializeField] private float padding = 0.1f;
    [Tooltip("How many points to use when generating the polygon shape")]
    [SerializeField] [Range(4, 64)] private int shapeResolution = 8;

    private Light2D light2D;
    private Bounds combinedBounds;

    void Awake()
    {
        light2D = GetComponent<Light2D>();
        light2D.lightType = Light2D.LightType.Freeform;
        ApplyTargets();
    }

    void Update()
    {
        if (updateEveryFrame)
            ApplyTargets();
    }

    [ContextMenu("Apply Targets Now")]
    public void ApplyTargets()
    {
        if (targetSprites == null || targetSprites.Length == 0)
        {
            Debug.LogWarning("Light2DTargetSprite: No target sprites assigned.");
            return;
        }

        // ── Calculate combined bounds of all targets ───────────────────────
        bool boundsInitialized = false;
        combinedBounds = new Bounds();

        foreach (SpriteRenderer sr in targetSprites)
        {
            if (sr == null) continue;
            if (!boundsInitialized)
            {
                combinedBounds = sr.bounds;
                boundsInitialized = true;
            }
            else
            {
                combinedBounds.Encapsulate(sr.bounds);
            }
        }

        if (!boundsInitialized) return;

        // ── Convert world bounds to local space of the light ──────────────
        Vector3 min = transform.InverseTransformPoint(combinedBounds.min);
        Vector3 max = transform.InverseTransformPoint(combinedBounds.max);

        float left   = min.x - padding;
        float right  = max.x + padding;
        float bottom = min.y - padding;
        float top    = max.y + padding;

        // ── Build a rectangle shape for the light ─────────────────────────
        Vector3[] shape = new Vector3[]
        {
            new Vector3(left,  bottom, 0f),
            new Vector3(left,  top,    0f),
            new Vector3(right, top,    0f),
            new Vector3(right, bottom, 0f),
        };

        light2D.SetShapePath(shape);
    }

    void OnDrawGizmosSelected()
    {
        if (targetSprites == null) return;

        Gizmos.color = new Color(1f, 1f, 0f, 0.3f);
        bool boundsInitialized = false;
        Bounds bounds = new Bounds();

        foreach (SpriteRenderer sr in targetSprites)
        {
            if (sr == null) continue;
            if (!boundsInitialized) { bounds = sr.bounds; boundsInitialized = true; }
            else bounds.Encapsulate(sr.bounds);
        }

        if (boundsInitialized)
            Gizmos.DrawWireCube(bounds.center, bounds.size + Vector3.one * padding * 2f);
    }
}