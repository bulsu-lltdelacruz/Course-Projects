using System;
using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.UIElements;

public class UIImage : MonoBehaviour
{
    //wag na alalahanin na isang instance lang, isa lang talaga iibahin lang text
    static UIDocument UIDOC;
    static VisualElement root;
    static VisualElement photo;
    public static bool IsReady => photo != null;
    void Start()
    {
        UIDOC = GetComponent<UIDocument>();

        root = UIDOC.rootVisualElement;
        photo = root.Q<VisualElement>("photo");
        
        photo.RegisterCallback<ClickEvent>(ClosePhoto);
        photo.visible = false;
    }
    public static void ClosePhoto(ClickEvent e)
    {
        photo.style.backgroundSize = new BackgroundSize(BackgroundSizeType.Contain);
        photo.visible = false;
        photo.style.backgroundImage = null;
        PlayerInputs.Player.Enable();
    }
    public static void OpenPhoto(Texture2D texture2D)
    {
        photo.style.backgroundImage = texture2D;
        photo.visible = true;
        PlayerInputs.Player.Disable();
    }
    public static void ClosePhotoFillScreen(ClickEvent e)
    {
        photo.style.backgroundSize = new BackgroundSize(BackgroundSizeType.Cover);
        photo.visible = false;
        photo.style.backgroundImage = null;
        PlayerInputs.Player.Enable();
    }
    public static void OpenPhotoFillScreen(Texture2D texture2D)
    {
        photo.style.backgroundSize = new BackgroundSize(BackgroundSizeType.Cover);
        photo.style.backgroundImage = texture2D;
        photo.visible = true;
        PlayerInputs.Player.Disable();
    }
    public static void ChangePhotoBGAlpha(float newAlpha)
    {
        Color currentColor = photo.style.backgroundColor.value;
        photo.style.backgroundColor = new Color(currentColor.r, currentColor.g, currentColor.b, newAlpha);
    }
}
