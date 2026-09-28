using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.UIElements;

public class MapUI : MonoBehaviour
{
    [SerializeField]private UIDocument mapDocument;
    private VisualElement mapRoot;
    void Awake()
    {
        mapRoot = mapDocument.rootVisualElement;
        var scrollView = mapRoot.Q<ScrollView>("map-child-scroll");
        var scroller = scrollView.verticalScroller;
        scroller.value = scroller.highValue;
        /*scrollView.verticalScroller.valueChanged += (float newValue) => {
            Debug.Log($"Scrolled to: {newValue}");
        };

        scrollView.horizontalScroller.valueChanged += (float newValue) => {
            Debug.Log($"Horizontal scroll: {newValue}");
        };*/
    }
    public void QueryDoorByID(string id)
    {
        mapRoot.Q<VisualElement>(id.Trim());
    }
    public void SetDoorLockedStateByID(string id, bool unlocked)
    {
        VisualElement door = mapRoot.Q<VisualElement>(id.Trim());
        if(door == null)
        {
            return;
        }
        
       // Debug.Log(id +" is interacted"+"and then "+ unlocked +" at start");
        if(unlocked)
        {
            RemoveLocked(door);
            RemoveUnopenable(door);
            RemoveNotInteracted(door);
            door.AddToClassList("unlocked");
        }else
        {
            
            RemoveUnlocked(door);
            RemoveUnopenable(door);
            RemoveNotInteracted(door);

            door.AddToClassList("locked");
        }
        foreach(string s in door.GetClasses())
        {
            //Debug.Log(id + ":  "+  s);
        }
        
    }
    public void SetDoorUnopenableByID(string id)
    {
        VisualElement door = mapRoot.Q<VisualElement>(id.Trim());
        if(door == null)
            return;
        RemoveLocked(door);
        RemoveUnlocked(door);
        RemoveNotInteracted(door);
        door.AddToClassList("unopenable");
    }
    public void SetDoorInteractedByID(string id, bool interacted)
    {
        VisualElement door = mapRoot.Q<VisualElement>(id.Trim());
        if(door == null)//gana if false
        {
            Debug.Log("nopers");
            return;
        }
        if(interacted)
        {
            RemoveNotInteracted(door);
            return;
        }
            
        RemoveLocked(door);
        RemoveUnlocked(door);
        RemoveUnopenable(door);

        door.AddToClassList("notInteracted");
    }
    public void SetLocationVisibilityByID(string id, bool visible)
    {
        VisualElement location = mapRoot.Q<VisualElement>(id.Trim());
        if(location == null)
        {   
            Debug.Log("not fount location   ");
            return;
        }
        location.style.display = visible?DisplayStyle.Flex:DisplayStyle.None;
    }
    public void SetPlayerInLocation(string id, bool inLocation)
    {
        VisualElement location = mapRoot.Q<VisualElement>(id.Trim());
        if(location == null)
        {   
            Debug.Log("not fount location   ");
            return;
        }
        if(inLocation)
        {
            if(location.ClassListContains("inArea"))
            {
                location.RemoveFromClassList("inArea");
            }
            location.AddToClassList("inArea");
        }else
        {
            if(location.ClassListContains("inArea"))
            {
                location.RemoveFromClassList("inArea");
            }
        }
    }
    private void RemoveLocked(VisualElement door)
    {
        if(door.ClassListContains("locked"))
            door.RemoveFromClassList("locked");
    }
    private void RemoveUnlocked(VisualElement door)
    {
        
            if(door.ClassListContains("unlocked"))
                door.RemoveFromClassList("unlocked");
    }
    private void RemoveUnopenable(VisualElement door)
    {
        if(door.ClassListContains("unopenable"))
            door.RemoveFromClassList("unopenable");
    }
    private void RemoveNotInteracted(VisualElement door)
    {
        if(door.ClassListContains("notInteracted"))
            door.RemoveFromClassList("notInteracted");
    }
}
