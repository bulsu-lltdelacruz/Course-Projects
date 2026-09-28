using System;
using UnityEngine;
using UnityEngine.UI;

public class LowHealthPulse : MonoBehaviour
{
    [SerializeField] private RawImage pulseImage;
    [SerializeField] private float pulseSpeed = 5f;
    
    [Range(0f, 1f)]
    [SerializeField] private float minAlpha = 0.1f;
    
    [Range(0f, 1f)]
    [SerializeField] private float maxAlpha = 0.7f;
    [NonSerialized]public bool isLowHealth = false;

    private Color imageColor;

    private void Start()
    {
        if (pulseImage != null)
        {
            imageColor = pulseImage.color;
            
            imageColor.a = 0f;
            pulseImage.color = imageColor;
        }
    }

    private void Update()
    {
        if (pulseImage == null) return;

        if (isLowHealth)
        {
            float wave = (Mathf.Sin(Time.time * pulseSpeed) + 1f) / 2f;
            imageColor.a = Mathf.Lerp(minAlpha, maxAlpha, wave);
            
            pulseImage.color = imageColor;
        }
        else if (pulseImage.color.a > 0)
        {
            imageColor.a = Mathf.MoveTowards(imageColor.a, 0f, Time.deltaTime * 2f);
            pulseImage.color = imageColor;
        }
    }

    public void StartPulsing()
    {
        isLowHealth = true;
    }

    public void StopPulsing()
    {
        isLowHealth = false;
    }
}