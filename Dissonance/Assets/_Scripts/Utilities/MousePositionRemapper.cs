using UnityEngine;
using UnityEngine.UIElements;

public class PixelatedUIInputFix : MonoBehaviour
{
    private UIDocument _uiDocument;

    void OnEnable()
    {
        _uiDocument = GetComponent<UIDocument>();
        
        // This function tells the UI exactly where the mouse 'really' is 
        // relative to the Render Texture's dimensions.
        _uiDocument.panelSettings.SetScreenToPanelSpaceFunction((Vector2 screenPosition) =>
        {
            float screenWidth = Screen.width;
            float screenHeight = Screen.height;

            var target = _uiDocument.panelSettings.targetTexture;
            if (target == null) return screenPosition;

            // Calculate ratios
            float xRatio = (float)target.width / screenWidth;
            float yRatio = (float)target.height / screenHeight;

            float flippedY = (  screenPosition.y) * yRatio;
            float scaledX = screenPosition.x * xRatio;
            return new Vector2(scaledX, flippedY);
        });
    }
    

    void OnDisable()
    {
        // Always clear the function when the script is disabled to prevent errors
        if (_uiDocument != null && _uiDocument.panelSettings != null)
        {
            _uiDocument.panelSettings.SetScreenToPanelSpaceFunction(null);
        }
    }
}
