using UnityEngine;

public class StretchCamera : MonoBehaviour
{
    public float xStretchAmount = 0.85f;

    private Camera cam;
    private float lastAspect = -1f;
    private int   lastScreenW = -1;
    private int   lastScreenH = -1;

    void Awake()
    {
        cam = GetComponent<Camera>();
        ApplyStretch();
    }

    void Update()
    {
        if (Mathf.Abs(cam.aspect - lastAspect) > 0.001f
            || Screen.width  != lastScreenW
            || Screen.height != lastScreenH)
        {
            ApplyStretch();
        }
    }

    void ApplyStretch()
    {
        lastAspect  = cam.aspect;
        lastScreenW = Screen.width;
        lastScreenH = Screen.height;

        cam.ResetProjectionMatrix();

        Matrix4x4 matrix = cam.projectionMatrix;

        float aspectRatio      = (float)Screen.width / Screen.height;
        float referenceAspect  = 16f / 9f;
        float aspectCompensation = referenceAspect / aspectRatio;

        matrix.m00 *= xStretchAmount * aspectCompensation;
        cam.projectionMatrix = matrix;
    }

    public void SetStretch(float amount)
    {
        xStretchAmount = amount;
        ApplyStretch();
    }
}