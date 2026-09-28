using UnityEngine;
using UnityEngine.UIElements;

public class UITEMP : MonoBehaviour
{
    private UIDocument UITEMPO;
    VisualElement root; 
    Label labelx;
    Label labely;
    public static string WIDTH;
    public static string HEIGHT;
    public static string RENDER_WIDTH;
    public static string RENDER_HEIGHT;
    
    void Start()
    {
        UITEMPO = GetComponent<UIDocument>();
        root = UITEMPO.rootVisualElement;
        
        labelx = root.Q<Label>("XDIR");
        
        labely = root.Q<Label>("YDIR");
    }
   /* void Update()
    {
        float fps = 1.0f / Time.unscaledDeltaTime;
      labelx.text = Mathf.RoundToInt(fps).ToString();
        
      labely.text = "HEIGHT : " +  HEIGHT + "   RENDER HEIGHT : " + Mathf.RoundToInt(fps).ToString();;
    }*/
}
