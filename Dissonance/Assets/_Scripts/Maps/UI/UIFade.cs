using System;
using System.Collections;
using System.Reflection;
using UnityEngine;
using UnityEngine.UIElements;

public class UIFade : MonoBehaviour
{
    //wag na alalahanin na isang instance lang, isa lang talaga iibahin lang text
     UIDocument UIDOC;
     VisualElement root;
    public static VisualElement fade;
    static float timer = 0f;
    void Awake()
    {
        UIDOC = GetComponent<UIDocument>();
        root = UIDOC.rootVisualElement;
        fade = root.Q<VisualElement>("fade");
        
        fade.visible = false;
    }
    void Update()
    {
        
    }
    /*public static void ShowFade()
    {
        fade.visible = true;
        if(PlayerInputs.Player.enabled)
            PlayerInputs.Player.Disable();
        timer+= Time.deltaTime;
        fade.style.opacity = Mathf.Clamp01(timer);
        if(timer >=1)
        {
            timer = 0;
            PlayerInputs.Player.Enable();
            fade.visible = false;
        }
    }*/
    public static IEnumerator StartLightToDarkTransition(Action TeleportPlayer)
    {
        fade.visible = true;
        if(Initializer.isInGame)
        {
            if(PlayerInputs.Player.enabled)
                PlayerInputs.Player.Disable();
        }
        
        while(timer<0.4f)
        {
            timer+= Time.deltaTime;
            float fadeAmount = timer/0.4f;
            fade.style.opacity = Mathf.Clamp01(fadeAmount);
            yield return null;
        }
        timer = 0;
        TeleportPlayer?.Invoke();
    }
    public static IEnumerator StartDarkToLightTransition()
    {
        while(timer>0)
        {
            timer-= Time.deltaTime;
            float fadeAmount = timer/0.4f;
            fade.style.opacity = Mathf.Clamp01(fadeAmount);
            yield return null;
        }
        timer = 0;
        fade.visible = false;
        if(!PlayerInputs.Player.enabled)
            PlayerInputs.Player.Enable();
            
    }



    public static IEnumerator MenuStartLightToDarkTransition(Action action)
    {
        fade.visible = true;
        while(timer<0.4f)
        {
            timer+= Time.deltaTime;
            float fadeAmount = timer/0.4f;
            fade.style.opacity = Mathf.Clamp01(fadeAmount);
            yield return null;
        }
        timer = 0;
        action?.Invoke();
    }
    public static IEnumerator MenuStartDarkToLightTransition()
    {
        while(timer>0)
        {
            timer-= Time.deltaTime;
            float fadeAmount = timer/0.4f;
            fade.style.opacity = Mathf.Clamp01(fadeAmount);
            yield return null;
        }
        timer = 0;
        fade.visible = false;
    }
    public static void SetOpacity(float opacity)
    {
        if (fade == null) return;
        fade.visible = opacity > 0f;
        fade.style.opacity = Mathf.Clamp01(opacity);
        timer = opacity * 0.4f;
    }
}
