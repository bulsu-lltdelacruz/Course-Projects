using System;
using System.Collections;
using UnityEngine;
using UnityEngine.UIElements;

public class NumLockUIManager : MonoBehaviour
{
    public static NumLockUIManager Instance { get; private set; }

    [SerializeField] private UIDocument numLockDoc;
    
    private VisualElement root;
    private VisualElement parent;
    private Label screenLabel;
    private string currentPassword;
    private Action onUnlockSuccess;

    private void Awake()
    {
        if (Instance == null) Instance = this;
        else Destroy(gameObject);
    }
    public void ChangeLogo(Texture2D imageLogo)
    {
        root.Q<VisualElement>("logo").style.backgroundImage = imageLogo;
    }
    
    private void Start()
    {
        root = numLockDoc.rootVisualElement;
        parent = root.Q<VisualElement>("parent");
        screenLabel = root.Q<Label>("screen-text");
        
        var enterBtn = root.Q<VisualElement>("enter");
        var exitBtn = root.Q<VisualElement>("exit");

        // Register numeric keys once
        root.Query(className: "key").ForEach(el => 
        {
            if (el != enterBtn && el != exitBtn)
            {
                Label keyLabel = el.Q<Label>();
                el.RegisterCallback<ClickEvent>(e => 
                {
                    if (screenLabel.text.Length < 6) screenLabel.text += keyLabel.text;
                });
            }
        });

        exitBtn.RegisterCallback<ClickEvent>(e => Close());
        
        enterBtn.RegisterCallback<ClickEvent>(e =>
        {
            if (screenLabel.text == currentPassword)
            {
                screenLabel.text = "SUCCESS";
                onUnlockSuccess?.Invoke();
                StartCoroutine(DelayClose());
            }
            else
            {
                screenLabel.text = "";
            }
        });

        parent.style.display = DisplayStyle.None;
    }

    public void Open(string password, Action successCallback)
    {
        currentPassword = password;
        onUnlockSuccess = successCallback;
        screenLabel.text = "";
        parent.style.display = DisplayStyle.Flex;
    }

    public void Close()
    {
        parent.style.display = DisplayStyle.None;
    }

    private IEnumerator DelayClose()
    {
        yield return new WaitForSeconds(1.5f);
        Close();
    }
}