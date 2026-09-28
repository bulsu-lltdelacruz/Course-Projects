using UnityEngine;

[RequireComponent(typeof(MeshFilter))]
public class Skew : MonoBehaviour
{
    [Header("Trapezoid Settings")]
    [Tooltip("1 = Normal width, 0.5 = Half width at the top, 0 = Point (Triangle)")]
    [Range(0f, 2f)]
    public float topWidthScale = 0.5f;

    [Tooltip("Shift the top flat part left or right to make a slanted trapezoid")]
    public float topHorizontalShift = 0f;

    void Start()
    {
        MakeTrapezoid();
    }
    public void MakeTrapezoid()
    {
        MeshFilter meshFilter = GetComponent<MeshFilter>();
        if (meshFilter == null || meshFilter.sharedMesh == null) return;

        // Instantiate the mesh so we don't overwrite the original asset file
        Mesh mesh = Instantiate(meshFilter.sharedMesh);
        meshFilter.mesh = mesh;

        Vector3[] vertices = mesh.vertices;
        
        Bounds bounds = mesh.bounds;
        float minY = bounds.min.y;
        float maxY = bounds.max.y;
        float height = maxY - minY;
        
        // Find the center line of the mesh
        float centerX = bounds.center.x;

        for (int i = 0; i < vertices.Length; i++)
        {
            Vector3 v = vertices[i];
            
            // Calculate height from 0 (bottom) to 1 (top)
            float normalizedY = (v.y - minY) / height;
            
            // Calculate how much we should scale the width at this specific height
            float currentScale = Mathf.Lerp(1f, topWidthScale, normalizedY);
            
            // 1. Find how far the vertex is from the center
            float distFromCenter = v.x - centerX;
            
            // 2. Scale that distance and place it back relative to the center
            v.x = centerX + (distFromCenter * currentScale);

            // 3. Apply the horizontal shift (so it only shifts the top, not the bottom)
            v.x += (topHorizontalShift * normalizedY);

            vertices[i] = v;
        }

        // Apply new vertices
        mesh.vertices = vertices;
        
        // Recalculate for lighting and boundaries
        mesh.RecalculateNormals();
        mesh.RecalculateBounds();
        
        // Update collider if one exists
        MeshCollider meshCollider = GetComponent<MeshCollider>();
        if (meshCollider != null)
        {
            meshCollider.sharedMesh = mesh;
        }
    }
}