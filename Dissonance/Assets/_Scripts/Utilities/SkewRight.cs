using UnityEngine;

[RequireComponent(typeof(MeshFilter))]
public class SkewRIght : MonoBehaviour
{
    // Creates a dropdown in the Inspector
    public enum StraightEdge { Left, Right }

    [Header("Trapezoid Settings")]
    [Tooltip("Which side should remain perfectly straight (vertical)?")]
    public StraightEdge straightEdgeSide = StraightEdge.Left;

    [Tooltip("1 = Full width (Square), 0.5 = Half width, 0 = Point (Right Triangle)")]
    [Range(0f, 1f)]
    public float topWidthScale = 0.5f;

    void Start()
    {
        MakeRightTrapezoid();
    }
    public void MakeRightTrapezoid()
    {
        MeshFilter meshFilter = GetComponent<MeshFilter>();
        if (meshFilter == null || meshFilter.sharedMesh == null) return;

        // Instantiate the mesh so we don't overwrite the original asset file
        Mesh mesh = Instantiate(meshFilter.sharedMesh);
        meshFilter.mesh = mesh;

        Vector3[] vertices = mesh.vertices;
        
        // Get the boundaries of the original mesh
        Bounds bounds = mesh.bounds;
        float minY = bounds.min.y;
        float maxY = bounds.max.y;
        float height = maxY - minY;
        
        float minX = bounds.min.x;
        float maxX = bounds.max.x;
        float width = maxX - minX;

        // Loop through every vertex
        for (int i = 0; i < vertices.Length; i++)
        {
            Vector3 v = vertices[i];
            
            // Safety check to prevent dividing by zero on flat planes
            if (height == 0 || width == 0) continue;

            // Calculate height from 0 (bottom) to 1 (top)
            float normalizedY = (v.y - minY) / height;
            
            // Calculate horizontal position from 0 (left edge) to 1 (right edge)
            float normalizedX = (v.x - minX) / width;
            
            // How wide should the mesh be at this specific height?
            float currentWidth = Mathf.Lerp(width, width * topWidthScale, normalizedY);
            
            // Apply the skew based on the chosen anchor side
            if (straightEdgeSide == StraightEdge.Left)
            {
                // Left edge is anchored at minX. The rest of the points scale inward.
                v.x = minX + (normalizedX * currentWidth);
            }
            else if (straightEdgeSide == StraightEdge.Right)
            {
                // Right edge is anchored at maxX. The rest of the points scale inward.
                // We use (1f - normalizedX) because we are measuring from the right side now.
                v.x = maxX - ((1f - normalizedX) * currentWidth);
            }

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